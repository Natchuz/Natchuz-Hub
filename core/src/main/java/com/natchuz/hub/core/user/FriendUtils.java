package com.natchuz.hub.core.user;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.spongepowered.api.Sponge;

import java.util.UUID;

import com.natchuz.hub.paper.managers.DialogManager;
import com.natchuz.hub.utils.mojang.ElectroidMojangAPI;
import com.natchuz.hub.utils.mojang.MojangAPI;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.content.ui.FriendsDialog;

import static com.natchuz.hub.paper.Color.*;

/**
 * Used as Paper client for friend operations, that includes logging into the chat, sending notification, etc.
 */
public class FriendUtils implements Listener {

    private final MojangAPI mojangApi;

    public FriendUtils() {
        this.mojangApi = new ElectroidMojangAPI();
    }

    /**
     * Invites player
     *
     * @param sender player who invites
     * @param target target player
     */
    public void invite(UUID sender, UUID target) {
        User senderU = Sponge.getServiceManager().provide(ProfileService.class).get().getUserRepo().getProfile(sender);
        Player senderP = Bukkit.getPlayer(sender);
        User targetU = Sponge.getServiceManager().provide(ProfileService.class).get().getUserRepo().getProfile(target);
        Player targetP = Bukkit.getPlayer(target);

        assert senderP != null;

        switch (senderU.inviteFriend(target)) {
            case OK:
                senderP.sendMessage(YELLOW + "Sent friend invite to " + RESET + targetU.chatName());
                if (targetP != null) {
                    targetP.sendMessage(senderU.chatName() + RESET + YELLOW + " sent you friend invite!");
                }
                break;
            case ALREADY_ACCEPTED:
                senderP.sendMessage(RED + "You're already friends!");
                break;
            case INTERNAL_ERROR:
                senderP.sendMessage(RED + BOLD + "We were unable to send friend invite due to internal database error! Please, contact with support!");
                break;
        }
    }

    /**
     * Remove a player from friends
     *
     * @param sender player who removes
     * @param target player to remove
     */
    public void removeFriend(UUID sender, UUID target) {
        User senderU = Sponge.getServiceManager().provide(ProfileService.class).get().getUserRepo().getProfile(sender);
        Player senderP = Bukkit.getPlayer(sender);
        User targetU = Sponge.getServiceManager().provide(ProfileService.class).get().getUserRepo().getProfile(target);

        assert senderP != null;

        switch (senderU.removeFriend(target)) {
            case OK:
                senderP.sendMessage(YELLOW + "Removed " + RESET + targetU.chatName() + RESET + YELLOW + " from friends");
                break;
            case NOT_CONNECTED:
                senderP.sendMessage(targetU.chatName() + RESET + RED + " is not your friend!");
                break;
            case SELF_INVITE:
                senderP.sendMessage(RED + "You cannot delete yourself!");
            case INTERNAL_ERROR:
                senderP.sendMessage(RED + BOLD + "We were unable to remove friend due to internal database error! Please, contact with support!");
                break;
        }
    }

    /**
     * Accepts friend invite from a player
     *
     * @param sender a player who accepts
     * @param target a player to accept
     */
    public void acceptFriend(UUID sender, UUID target) {
        User senderU = Sponge.getServiceManager().provide(ProfileService.class).get().getUserRepo().getProfile(sender);
        Player senderP = Bukkit.getPlayer(sender);
        User targetU = Sponge.getServiceManager().provide(ProfileService.class).get().getUserRepo().getProfile(target);
        Player targetP = Bukkit.getPlayer(target);

        assert senderP != null;

        switch (senderU.acceptFriend(target)) {
            case OK:
                senderP.sendMessage(YELLOW + "Accepted " + RESET + targetU.chatName() + RESET + YELLOW + " as friend");
                if (targetP != null) {
                    targetP.sendMessage(senderU.chatName() + RESET + YELLOW + " accepted your friend invite!");
                }
                break;
            case NOT_INVITED:
                senderP.sendMessage(RED + "He didn't even invite you! :(");
            case SELF_INVITE:
                senderP.sendMessage(RED + "You are already accepted!");
            case INTERNAL_ERROR:
                senderP.sendMessage(RED + BOLD + "We were unable to accept friend invite due to internal database error! Please, contact with support!");
                break;
        }
    }

    /**
     * Decline friend invitation
     *
     * @param sender a player who declines
     * @param target a player to decline
     */
    public void declineFriend(UUID sender, UUID target) {
        User senderU = Sponge.getServiceManager().provide(ProfileService.class).get().getUserRepo().getProfile(sender);
        Player senderP = Bukkit.getPlayer(sender);
        User targetU = Sponge.getServiceManager().provide(ProfileService.class).get().getUserRepo().getProfile(target);

        assert senderP != null;

        switch (senderU.cancelFriendInvite(target)) {
            case OK:
                senderP.sendMessage(YELLOW + "Declined " + RESET + targetU.chatName() + RESET + YELLOW + " as friend");
                break;
            case NOT_INVITED:
                senderP.sendMessage(RED + "You cannot reject yourself!");
            case SELF_INVITE:
                senderP.sendMessage(RED + "You cannot delete yourself!");
            case INTERNAL_ERROR:
                senderP.sendMessage(RED + BOLD + "We were unable to decline friend request due to internal database error! Please, contact with support!");
                break;
        }
    }

    //region event handlers

    /**
     * Use this to hide command implementation
     */
    @EventHandler
    private void onEnable(PluginEnableEvent event) {
        // methods can be used as single-method interface
        //Sponge.getCommandManager().register().setExecutor(this::friendCommand);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        User user = Sponge.getServiceManager().provide(ProfileService.class).get().getUserRepo().getProfile(event.getPlayer());

        int pending = user.getFriendsPendingInvites().size();

        if (pending > 0) {
            event.getPlayer().sendMessage(YELLOW + "You have " + pending + " pending friends requests");
        }
    }

    //endregion
    //region commands

    private boolean friendCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("f")) return false;
        if (!(sender instanceof Player)) return false;

        if (args.length < 1) {
            DialogManager.openDialog(new FriendsDialog(), (Player) sender);
            return true;
        }

        switch (args[0]) {
            case "inv":
            case "invite":
            case "add":
                if (args.length < 2) return false;
                invite(((Player) sender).getUniqueId(), mojangApi.getUUID(args[1]));
                break;

            case "remove":
            case "rm":
            case "rmv":
                if (args.length < 2) return false;
                removeFriend(((Player) sender).getUniqueId(), mojangApi.getUUID(args[1]));
                break;

            case "cancel":
            case "cnl":
            case "deny":
            case "decline":
                if (args.length < 2) return false;
                declineFriend(((Player) sender).getUniqueId(), mojangApi.getUUID(args[1]));
                break;

            case "accept":
            case "accpt":
            case "acpt":
                if (args.length < 2) return false;
                acceptFriend(((Player) sender).getUniqueId(), mojangApi.getUUID(args[1]));
                break;
        }
        return true;
    }

    //endregion
}
