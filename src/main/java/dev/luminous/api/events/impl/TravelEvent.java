/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.entity.player.PlayerEntity;

public class TravelEvent
extends Event {
    private static final TravelEvent INSTANCE = new TravelEvent();
    private class_1657 entity;

    private TravelEvent() {
    }

    public static TravelEvent get(Event.Stage stage, class_1657 entity) {
        TravelEvent.INSTANCE.entity = entity;
        TravelEvent.INSTANCE.stage = stage;
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }

    public class_1657 getEntity() {
        return this.entity;
    }
}

