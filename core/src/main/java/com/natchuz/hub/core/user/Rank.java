package com.natchuz.hub.core.user;

import static com.natchuz.hub.paper.Color.*;

/**
 * All possible ranks
 */
public enum Rank {
    REGULAR("", DARK_GREY + BOLD + "Regular", false),
    VIP(GREEN + BOLD + "VIP"),
    PATRON(RED + BOLD + "PATRON"),
    MOD(DARK_GREEN + BOLD + "MOD", true),
    ADMIN(DARK_RED + BOLD + "ADMIN", true);

    private final String display;
    private final String name;
    private final boolean ownsAll;

    Rank(String display) {
        this(display, display, false);
    }

    Rank(String display, boolean ownsAll) {
        this(display, display, ownsAll);
    }

    Rank(String display, String name, boolean ownsAll) {
        this.display = display;
        this.name = name;
        this.ownsAll = ownsAll;
    }

    public int permissionLevel() {
        return this.ordinal();
    }

    public String title() {
        return display + RESET;
    }

    public String getName() {
        return name;
    }

    public boolean ownsAll() {
        return ownsAll;
    }
}
