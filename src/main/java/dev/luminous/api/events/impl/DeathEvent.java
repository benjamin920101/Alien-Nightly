/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 */
package dev.luminous.api.events.impl;

import net.minecraft.class_1657;

public class DeathEvent {
    private static final DeathEvent INSTANCE = new DeathEvent();
    private class_1657 player;

    private DeathEvent() {
    }

    public static DeathEvent get(class_1657 player) {
        DeathEvent.INSTANCE.player = player;
        return INSTANCE;
    }

    public class_1657 getPlayer() {
        return this.player;
    }
}

