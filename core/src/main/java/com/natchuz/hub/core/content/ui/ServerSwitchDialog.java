package com.natchuz.hub.core.content.ui;

import com.natchuz.hub.paper.managers.Dialog;

/**
 * Dialog used to switch between subservers
 */
public class ServerSwitchDialog extends Dialog {

    /*@Override
    protected void init() {
        setWidth(3);
        setHeight(1);
        setName("Switch server");
    }

    @SneakyThrows
    @Override
    protected void build() {
        String[] servers = NetworkMain.getInstance().getServers(System.getenv("SERVERTYPE")).get()
                .stream()
                .sorted(Comparator.comparing(s -> s.getId().getId()))
                .map(t -> t.getId().getId())
                .toArray(String[]::new);


        setWidth(7);
        setHeight((servers.length / 7) - 2); //change to paged dialog

        for (int i = 0; i < servers.length; i++) {
            int finalI = i; //functional programming in java btw
            setField(new StackBuilder(Material.WHITE_CONCRETE).name(servers[i]).doneGUI(), i % 7, i / 7, (c) -> {
                NetworkMain.getInstance().sendToLobby(viewer());
            });
        }
    }*/
}
