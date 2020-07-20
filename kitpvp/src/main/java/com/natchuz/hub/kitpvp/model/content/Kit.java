package com.natchuz.hub.kitpvp.model.content;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Delegate;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import com.natchuz.hub.kitpvp.model.ItemSet;

@ToString
public class Kit {

    @Getter
    private final String name;
    @Getter
    private final ItemStack icon;
    @Getter
    @Delegate
    private final ItemSet content;

    public Kit(String name, ItemStack icon, ItemSet content) {
        this.name = name;
        this.icon = icon;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kit kit = (Kit) o;
        return name.equals(kit.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
