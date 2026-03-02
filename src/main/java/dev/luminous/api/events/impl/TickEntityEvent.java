/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_1297;

public class TickEntityEvent
extends Event {
    private static final TickEntityEvent INSTANCE = new TickEntityEvent();
    private class_1297 entity;

    private TickEntityEvent() {
    }

    public static TickEntityEvent get(class_1297 entity) {
        TickEntityEvent.INSTANCE.entity = entity;
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }

    public class_1297 getEntity() {
        return this.entity;
    }
}

