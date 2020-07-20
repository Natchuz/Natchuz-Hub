package com.natchuz.hub.core.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.natchuz.hub.paper.Color;
import com.natchuz.hub.utils.mojang.ElectroidMojangAPI;
import com.natchuz.hub.utils.mojang.MojangAPI;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.content.cosmetics.Cosmetics;
import com.natchuz.hub.core.profile.UserProfile;

import static com.natchuz.hub.core.user.InvitingResult.*;

/**
 * It's a database representation along with some utils methods, that don't interact with Player class. All DB operations are here
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class User extends UserProfile {
    private Rank rank;
    private List<Integer> transactions;

    //cosmetics
    private Cosmetics.Traits selectedTrait;
    private Cosmetics.Shields selectedShield;
    private Cosmetics.BloodEffects selectedBloodEffect;

    //friends
    private List<UUID> friends;
    private List<UUID> friendsInvitesSent;
    private List<UUID> friendsPendingInvites;

    private int coins;

    @EqualsAndHashCode.Exclude
    private final MojangAPI mojangAPI;

    public User() {
        this.mojangAPI = new ElectroidMojangAPI();
    }

    //region constructors

    @Override
    protected void initNew() {
        rank = Rank.REGULAR;
        transactions = new ArrayList<>();

        selectedTrait = Cosmetics.Traits.NONE;
        selectedShield = Cosmetics.Shields.NONE;
        selectedBloodEffect = Cosmetics.BloodEffects.NORMAL;

        friends = new LinkedList<>();
        friendsInvitesSent = new LinkedList<>();
        friendsPendingInvites = new LinkedList<>();

        coins = 0;
    }

    public void updateDB() {
        NetworkMain.getInstance().updateProfile(this);
    }

    //endregion

    //region utility methods

    public String chatName() {
        return (rank == Rank.REGULAR ? "" : rank.title() + " ") + ((Bukkit.getPlayer(getUUID())) == null
                ? mojangAPI.getUsername(getUUID()) : Bukkit.getPlayer(getUUID()).getName()) + Color.RESET;
    }

    public Boolean owns(Purchasable product) {
        return product.getItemId() == 0 || (product.getItemId() != -1 && rank.ownsAll()) || transactions.contains(product.getItemId());
    }

    public void buy(Purchasable product) {
        transactions.add(product.getItemId());
        updateDB();
    }

    public boolean can(Rank rank) {
        return rank.permissionLevel() <= this.rank.permissionLevel();
    }

    //endregion

    //region friends

    public InvitingResult inviteFriend(UUID invUUID) {
        if (getUUID().equals(invUUID)) return SELF_INVITE;
        User target = NetworkMain.getInstance().getProfile(invUUID);

        if (friends.contains(invUUID)) {
            if (target.friends.contains(getUUID()))
                return ALREADY_ACCEPTED;
            else
                return INTERNAL_ERROR;
        }

        target.friendsPendingInvites.add(getUUID());
        target.updateDB();

        friendsInvitesSent.add(invUUID);
        updateDB();

        return OK;
    }

    public InvitingResult cancelFriendInvite(UUID invUUID) {
        if (getUUID().equals(invUUID)) return SELF_INVITE;
        User target = NetworkMain.getInstance().getProfile(invUUID);

        if (friends.contains(invUUID)) {
            if (target.friends.contains(getUUID()))
                return ALREADY_ACCEPTED;
            else
                return INTERNAL_ERROR;
        }

        if (!friendsInvitesSent.contains(invUUID)) {
            if (!target.friendsPendingInvites.contains(getUUID()))
                return NOT_INVITED;
            else
                return INTERNAL_ERROR;
        }

        target.friendsPendingInvites.remove(getUUID());
        target.updateDB();

        friendsPendingInvites.remove(invUUID);
        updateDB();

        return OK;
    }

    public InvitingResult acceptFriend(UUID acceptUUID) {
        if (getUUID().equals(acceptUUID)) return SELF_INVITE;
        User target = NetworkMain.getInstance().getProfile(acceptUUID);

        if (friends.contains(acceptUUID)) {
            if (target.friends.contains(getUUID()))
                return ALREADY_ACCEPTED;
            else
                return INTERNAL_ERROR;
        }

        if (!friendsPendingInvites.contains(acceptUUID)) {
            if (!target.friendsInvitesSent.contains(getUUID()))
                return NOT_INVITED;
            else
                return INTERNAL_ERROR;
        }

        target.friendsInvitesSent.remove(getUUID());
        target.friends.add(getUUID());
        target.updateDB();

        friendsPendingInvites.remove(acceptUUID);
        friends.add(acceptUUID);
        updateDB();

        return OK;
    }

    public InvitingResult declineFriend(UUID declineUUID) {
        if (getUUID().equals(declineUUID)) return SELF_INVITE;
        User target = NetworkMain.getInstance().getProfile(declineUUID);

        if (friends.contains(declineUUID)) {
            if (target.friends.contains(getUUID()))
                return ALREADY_ACCEPTED;
            else
                return INTERNAL_ERROR;
        }

        if (!friendsPendingInvites.contains(declineUUID)) {
            if (!target.friendsInvitesSent.contains(getUUID()))
                return NOT_INVITED;
            else
                return INTERNAL_ERROR;
        }

        target.friendsInvitesSent.remove(getUUID());
        target.updateDB();

        friendsPendingInvites.remove(declineUUID);
        updateDB();

        return OK;
    }

    public InvitingResult removeFriend(UUID removeUUID) {
        if (getUUID().equals(removeUUID)) return SELF_INVITE;
        User target = NetworkMain.getInstance().getProfile(removeUUID);

        if (!friends.contains(removeUUID)) {
            if (!target.friends.contains(getUUID()))
                return NOT_CONNECTED;
            else
                return INTERNAL_ERROR;
        }

        target.friends.remove(getUUID());
        target.updateDB();

        friends.remove(removeUUID);
        updateDB();

        return OK;
    }

    //endregion

    //region getters and setters

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Cosmetics.Traits getSelectedTrait() {
        return selectedTrait;
    }

    public void setSelectedTrait(Cosmetics.Traits selectedTrait) {
        this.selectedTrait = selectedTrait;
    }

    public Cosmetics.Shields getSelectedShield() {
        return selectedShield;
    }

    public void setSelectedShield(Cosmetics.Shields selectedShield) {
        this.selectedShield = selectedShield;
    }

    public Cosmetics.BloodEffects getSelectedBloodEffect() {
        return selectedBloodEffect;
    }

    public void setSelectedBloodEffect(Cosmetics.BloodEffects selectedBloodEffect) {
        this.selectedBloodEffect = selectedBloodEffect;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    @BsonIgnore
    public List<User> getFriendsUser() {
        return friends.stream().map(NetworkMain.getInstance()::getProfile).collect(Collectors.toList());
    }

    public List<UUID> getFriendsSentInvites() {
        return friendsInvitesSent;
    }

    @BsonIgnore
    public List<User> getFriendsSentInvitesUser() {
        return friendsInvitesSent.stream().map(NetworkMain.getInstance()::getProfile).collect(Collectors.toList());
    }

    public List<UUID> getFriendsPendingInvites() {
        return friendsPendingInvites;
    }

    @BsonIgnore
    public List<User> getFriendsPendingInvitesUser() {
        return friendsPendingInvites.stream().map(NetworkMain.getInstance()::getProfile).collect(Collectors.toList());
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    //endregion
}
