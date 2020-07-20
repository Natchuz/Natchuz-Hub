package com.natchuz.hub.kitpvp.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.natchuz.hub.core.profile.UserProfile;

/**
 * Coins: tokens used to buy items in shop
 */
@ToString
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class KitProfile extends UserProfile {

    @Setter
    private int coins;
    private KitPersistentStats stats;
    private Map<String, String> kits;

    @Override
    protected void initNew() {
        coins = 100;
        stats = new KitPersistentStats();
        kits = new HashMap<>();
    }

    //region getters and setters

    @BsonIgnore
    public Optional<String> getKit(String place) {
        return Optional.ofNullable(kits.get(place));
    }

    public void setKit(String place, String id) {
        kits.put(place, id);
    }

    public void removeKit(String id) {
        kits.remove(id);
    }

    //endregion
}
