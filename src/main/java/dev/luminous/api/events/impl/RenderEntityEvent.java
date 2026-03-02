/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_1297;

public class RenderEntityEvent
extends Event {
    private static final RenderEntityEvent INSTANCE = new RenderEntityEvent();
    private class_1297 entity;

    private RenderEntityEvent() {
    }

    public static RenderEntityEvent get(class_1297 entity) {
        RenderEntityEvent.INSTANCE.entity = entity;
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }

    public class_1297 getEntity() {
        return this.entity;
    }
}

