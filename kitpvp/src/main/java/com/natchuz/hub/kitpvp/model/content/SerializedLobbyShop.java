package com.natchuz.hub.kitpvp.model.content;

import java.util.LinkedList;
import java.util.List;

public class SerializedLobbyShop implements ItemShop {

    private final List<ItemShopEntry> items;

    private SerializedLobbyShop() {
        items = new LinkedList<>();
    }

    @Override
    public List<ItemShopEntry> getItems() {
        return items;
    }
}
