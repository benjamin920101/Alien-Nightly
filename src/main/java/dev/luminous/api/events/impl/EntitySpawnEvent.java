/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.entity.Entity;

public class EntitySpawnEvent
extends Event {
    private static final EntitySpawnEvent INSTANCE = new EntitySpawnEvent();
    private class_1297 entity;

    private EntitySpawnEvent() {
    }

    public static EntitySpawnEvent get(class_1297 entity) {
        EntitySpawnEvent.INSTANCE.entity = entity;
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }

    public class_1297 getEntity() {
        return this.entity;
    }
}

