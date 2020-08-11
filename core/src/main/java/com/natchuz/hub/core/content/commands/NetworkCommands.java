package com.natchuz.hub.core.content.commands;

public class NetworkCommands {

    /*private final VersionInfo info;

    public NetworkCommands() {
        info = new VersionInfo(NetworkCommands.class);
    }

    //region event handlers

    @Listener
    public void onEnable(Object event) {
        for (String s : new String[]{"menu", "v", "hub"}) {
            Objects.requireNonNull(NetworkMain.getInstance().getPlugin().getCommand(s)).setExecutor(this::onCommand);
        }
    }*/

    //endregion

    //region commands

    /*private boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

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
    }*/

    //endregion
}
