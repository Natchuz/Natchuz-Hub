package com.natchuz.hub.core.user;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

/**
 * All possible ranks
 */
public enum Rank {
    REGULAR(Text.EMPTY, Text.of(TextColors.DARK_GRAY, TextStyles.BOLD, "Regular"), false),
    VIP(Text.of(TextColors.GREEN, TextStyles.BOLD, "VIP")),
    PATRON(Text.of(TextColors.RED, TextStyles.BOLD, "PATRON")),
    MOD(Text.of(TextColors.DARK_GREEN, TextStyles.BOLD, "MOD"), true),
    ADMIN(Text.of(TextColors.DARK_RED, TextStyles.BOLD, "ADMIN"), true);

    private final Text display;
    private final Text name;
    private final boolean ownsAll;

    Rank(Text display) {
        this(display, display, false);
    }

    Rank(Text display, boolean ownsAll) {
        this(display, display, ownsAll);
    }

    Rank(Text display, Text name, boolean ownsAll) {
        this.display = display;
        this.name = name;
        this.ownsAll = ownsAll;
    }

    public int permissionLevel() {
        return this.ordinal();
    }

    public Text title() {
        return display;
    }

    public Text getName() {
        return name;
    }

    public boolean ownsAll() {
        return ownsAll;
    }
}
