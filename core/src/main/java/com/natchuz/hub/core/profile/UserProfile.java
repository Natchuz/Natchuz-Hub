package com.natchuz.hub.core.profile;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

/**
 * Represents user that will be stored in database.
 * <p>
 * Current restrictions:
 * <ul>
 *     <li>All fields to be serialized are required to have getter,
 *     (best working with lombok {@link lombok.Getter getter annotation})</li>
 *     <li>All getters that should not be serialized must be annotated with
 *     {@link org.bson.codecs.pojo.annotations.BsonIgnore BsonIgnore annotation}</li>
 *     <li>Sometimes explicit field name is required using {@link BsonProperty BsonProperty annotation}</li>
 *     <li>Empty constructor is required OR {@link org.bson.codecs.pojo.annotations.BsonCreator BsonCreator annotated}
 *     constructor implemented according to
 *     <a href="https://mongodb.github.io/mongo-java-driver/4.1/bson/pojos/#supporting-pojos-without-no-args-constructors">
 *         official mongo documentation</a></li>
 * </ul>
 */
@ToString
@EqualsAndHashCode
public abstract class UserProfile {

    @Getter(onMethod_ = {@BsonProperty(value = "UUID")})
    @Setter
    private UUID UUID;

    /**
     * A method that initializes fresh profile
     */
    protected abstract void initNew();
}