package com.natchuz.hub.core.content.ui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

import com.natchuz.hub.paper.items.StackBuilder;
import com.natchuz.hub.paper.managers.Dialog;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.content.Items;
import com.natchuz.hub.core.user.User;

import static com.natchuz.hub.paper.Color.GREY;
import static com.natchuz.hub.paper.Color.RESET;
import static org.bukkit.event.inventory.ClickType.*;

/**
 * Dialog that manages friends
 */
public class FriendsDialog extends Dialog {

    private User user;
    private int page = 0;
    private DialogView context = DialogView.FRIENDS;

    @Override
    protected void init() {
        user = NetworkMain.getInstance().getProfile(viewer());
        setWidth(7);
        setHeight(4);
        setName("Friends");
    }

    @Override
    protected void build() {
        List<User> list = (context == DialogView.FRIENDS) ? user.getFriendsUser() : user.getFriendsPendingInvitesUser();
        int allSize = list.size();
        list = list.subList(page * 4 * 7, Math.min((page + 1) * 4 * 7, list.size()));

        for (int i = 0; i < 4 * 7; i++) {
            if (i < list.size()) {
                User subject = list.get(i);

                String description;

                if (context == DialogView.FRIENDS) {
                    description = GREY + "Click to access player menu\n" + RESET + GREY + "Shift click to remove from list" + RESET;
                } else {
                    description = GREY + "Left click to accept\n" + RESET + GREY + "Right click to decline\n" + RESET + GREY + "Shift click to access profile" + RESET;
                }

                ItemStack skull = new StackBuilder(Material.PLAYER_HEAD).name(subject.chatName())
                        .lore(description).meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(subject.getUUID()))).doneGUI();

                setField(skull, i % 7, i / 7, (c) -> {
                    if (context == DialogView.FRIENDS) {
                        if (c == SHIFT_LEFT) {
                            open(new ConfirmationDialog(() -> {
                                NetworkMain.getInstance().getFriendUtils().removeFriend(user.getUUID(), subject.getUUID());
                            }, () -> {
                            }, skull, "Confirm you wish to remove " + subject.chatName() + " from friends"));
                        } else {
                            open(new PlayerMenuDialog(subject));
                        }
                    } else {
                        if (c == SHIFT_LEFT) {
                            open(new PlayerMenuDialog(subject));
                        } else if (c == SHIFT_RIGHT) {
                            NetworkMain.getInstance().getFriendUtils().declineFriend(user.getUUID(), subject.getUUID());
                        } else if (c == LEFT) {
                            NetworkMain.getInstance().getFriendUtils().acceptFriend(user.getUUID(), subject.getUUID());
                        }
                    }
                });
            } else {
                setField(new ItemStack(Material.AIR), i % 7, i / 7, (c) -> {
                });
            }
        }

        setField(new StackBuilder(Material.BOOK).name("Friends").enchantedGUI(context == DialogView.FRIENDS).doneGUI(), -1, 1, (c) -> {
            page = 0;
            context = DialogView.FRIENDS;
        });
        setField(new StackBuilder(Material.FEATHER).name("Invites").enchantedGUI(context == DialogView.INVITES).doneGUI(), -1, 2, (c) -> {
            page = 0;
            context = DialogView.INVITES;
        });

        boolean nextEx = (page + 1) * 4 * 7 < allSize;

        setField(page > 0 ? Items.Gui.PREVIOUS : Items.Gui.PREVIOUS_DISABLED, 0, 4, (c) -> {
            if (page > 0)
                page--;
        });
        setField(nextEx ? Items.Gui.NEXT : Items.Gui.NEXT_DISABLED, 6, 4, (c) -> {
            if (nextEx) page++;
        });
        setField(Items.Gui.BACK, 3, 4, (c) -> close());
    }

    private enum DialogView {
        FRIENDS, INVITES
    }
}
