/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 */
package dev.luminous.api.events.impl;

import net.minecraft.entity.player.PlayerEntity;

public class TotemEvent {
    private static final TotemEvent INSTANCE = new TotemEvent();
    private class_1657 player;

    private TotemEvent() {
    }

    public static TotemEvent get(class_1657 player) {
        TotemEvent.INSTANCE.player = player;
        return INSTANCE;
    }

    public class_1657 getPlayer() {
        return this.player;
    }
}

