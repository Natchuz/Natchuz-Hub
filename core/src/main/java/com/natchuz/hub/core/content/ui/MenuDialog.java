package com.natchuz.hub.core.content.ui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;

import com.natchuz.hub.paper.items.StackBuilder;
import com.natchuz.hub.paper.managers.Dialog;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.content.Items;
import com.natchuz.hub.core.user.User;

/**
 * Main menu dialog
 */
public class MenuDialog extends Dialog {

    private User user;

    @Override
    protected void init() {
        user = NetworkMain.getInstance().getProfile(viewer());
        setWidth(3);
        setHeight(2);
        setName(user.chatName());
    }

    @Override
    protected void build() {
        setField(new StackBuilder(Material.PLAYER_HEAD).name(user.chatName())
                .meta(SkullMeta.class, (m) -> {
                    m.setOwningPlayer(Bukkit.getOfflinePlayer(viewer().getUniqueId()));
                }).doneGUI(), 1, 0, (c) -> {
        });

        setField(Items.FRIENDS, 0, 1, (c) -> {
            open(new FriendsDialog());
        });

        setField(Items.COSMETICS, 1, 1, (c) -> {
            open(new CosmeticDialog());
        });
    }
}
