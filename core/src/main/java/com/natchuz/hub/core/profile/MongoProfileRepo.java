package com.natchuz.hub.core.profile;

import com.google.common.base.CaseFormat;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.SneakyThrows;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.pojo.Conventions.ANNOTATION_CONVENTION;
import static org.bson.codecs.pojo.Conventions.SET_PRIVATE_FIELDS_CONVENTION;

public class MongoProfileRepo<T extends UserProfile> implements ProfileRepo<T> {

    private final MongoCollection<T> collection;
    private final Class<T> profileClass;

    @SuppressWarnings("ConstantConditions")
    public MongoProfileRepo(MongoDatabase database, Class<T> profileClass) {
        this.profileClass = profileClass;
        PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                .conventions(asList(SET_PRIVATE_FIELDS_CONVENTION, ANNOTATION_CONVENTION))
                .automatic(true)
                .build();


        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(pojoCodecProvider));

        String collectionName = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL)
                .convert(profileClass.getSimpleName());
        collection = database.getCollection(collectionName, profileClass).withCodecRegistry(pojoCodecRegistry);
    }

    @SneakyThrows
    @Override
    public T getProfile(UUID uuid) {
        T result = collection.find(eq("UUID", uuid.toString())).first();

        if (result == null) {
            T freshProfile = profileClass.newInstance();
            freshProfile.setUUID(uuid);
            freshProfile.initNew();
            collection.insertOne(freshProfile);
            return freshProfile;
        }

        return result;
    }

    @Override
    public void updateProfile(T profile) {
        collection.replaceOne(eq("UUID", profile.getUUID().toString()), profile);
    }
}
