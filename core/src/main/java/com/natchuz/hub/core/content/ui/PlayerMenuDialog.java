package com.natchuz.hub.core.content.ui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

import com.natchuz.hub.paper.items.StackBuilder;
import com.natchuz.hub.paper.managers.Dialog;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.user.User;

/**
 * Dialog that displays possible actions for specific player
 */
public class PlayerMenuDialog extends Dialog {

    private final User targetUser;
    private User viewerUser;

    /**
     * @param target player this dialog is about
     */
    public PlayerMenuDialog(Player target) {
        this.targetUser = NetworkMain.getInstance().getProfile(target);
    }

    /**
     * @param target player this dialog is about
     */
    public PlayerMenuDialog(User target) {
        this.targetUser = target;
    }

    @Override
    protected void init() {
        viewerUser = NetworkMain.getInstance().getProfile(viewer());
        setHeight(2);
        setWidth(1);
        setName(targetUser.chatName());
    }

    @Override
    protected void build() {
        // player head
        setField(new StackBuilder(Material.PLAYER_HEAD).name(targetUser.chatName())
                .meta(SkullMeta.class, (m) -> m.setOwningPlayer(Bukkit.getOfflinePlayer(targetUser.getUUID()))).doneGUI(), 0, 0, (c) -> {
        });
    }
}
