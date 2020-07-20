package com.natchuz.hub.kitpvp.impl.ui;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.natchuz.hub.paper.items.StackBuilder;
import com.natchuz.hub.paper.managers.Dialog;
import com.natchuz.hub.kitpvp.ConcreteKitGamemode;
import com.natchuz.hub.kitpvp.impl.Arena;
import com.natchuz.hub.kitpvp.impl.ArenaState;
import com.natchuz.hub.kitpvp.map.ArenaSpawn;
import com.natchuz.hub.kitpvp.model.content.EmptyKit;
import com.natchuz.hub.kitpvp.model.content.Kit;

import static com.natchuz.hub.paper.Color.*;

@AllArgsConstructor
public class SpawnContextDialog extends Dialog {

    private final ConcreteKitGamemode gamemode;
    private final Arena arena;
    private final String id;

    @Override
    protected void init() {
        setName("Select spawn or select kit");
        setWidth(7);
        setHeight(4);
    }

    @Override
    protected void build() {
        Optional<Kit> selectedKit = gamemode.getProfile(viewer()).getKit(id).flatMap(gamemode.getKitConfig()::getKit);

        AtomicReference<ItemStack> stack = new AtomicReference<>();

        selectedKit.ifPresent(k -> {
            stack.set(new StackBuilder(k.getIcon().clone())
                    .name(GREEN + BOLD + "Kit: " + AQUA + BOLD + k.getName())
                    .lore("\n" + GREY + "Click to change")
                    .doneGUI());
        });

        if (!selectedKit.isPresent()) {
            stack.set(new StackBuilder(Material.BUCKET)
                    .name(GREEN + BOLD + "No kit selected")
                    .lore("\n" + YELLOW + BOLD + "Click to select")
                    .doneGUI());
        }

        setField(stack.get(), 3, 4, c -> {
            open(new KitSelector(gamemode, sk -> {
                gamemode.getProfile(viewer()).setKit(id, sk.getName());
            }));
        });

        for (int i = 0; i < 7 * 4; i++) {
            ItemStack target;
            Consumer<ClickType> action = c -> {
            };

            if (arena.getArenaSpawns().size() > i) {
                ArenaSpawn spawn = arena.getArenaSpawns().get(i);
                target = new StackBuilder(spawn.getIcon().clone())
                        .name(GREEN + BOLD + "Spawn: " + AQUA + BOLD + spawn.getId())
                        .lore("\n" + GREY + "Click to join arena")
                        .doneGUI();
                action = c -> {
                    close();
                    ArenaState.beginArenaState(gamemode, viewer(), arena, spawn, selectedKit.orElse(EmptyKit.INSTANCE));
                };
            } else {
                target = new ItemStack(Material.AIR);
            }

            int x = i % 7;
            int y = i / 7;
            setField(target, x, y, action);
        }
    }
}
