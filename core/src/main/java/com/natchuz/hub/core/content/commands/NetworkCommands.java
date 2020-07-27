package com.natchuz.hub.core.content.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.server.PluginEnableEvent;
import org.spongepowered.api.event.Listener;

import java.util.Objects;

import com.natchuz.hub.paper.managers.DialogManager;
import com.natchuz.hub.protocol.state.JoinFlags;
import com.natchuz.hub.utils.VersionInfo;
import com.natchuz.hub.core.NetworkMain;
import com.natchuz.hub.core.content.ui.MenuDialog;

public class NetworkCommands {

    private final VersionInfo info;

    public NetworkCommands() {
        info = new VersionInfo(NetworkCommands.class);
    }

    //region event handlers

    @Listener
    public void onEnable(Object event) {
        for (String s : new String[]{"menu", "v", "hub"}) {
            Objects.requireNonNull(NetworkMain.getInstance().getPlugin().getCommand(s)).setExecutor(this::onCommand);
        }
    }

    //endregion

    //region commands

    private boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            final Player player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "menu":
                    DialogManager.openDialog(new MenuDialog(), player);
                    break;
                case "hub":
                    NetworkMain.getInstance().send(player.getName(), "lb", JoinFlags.LOBBY_RETURN);
                    break;
            }
        }

        if ("v".equals(command.getName().toLowerCase())) {
            sender.sendMessage(info.getFull());
        }

        return true;
    }

    //endregion
}
