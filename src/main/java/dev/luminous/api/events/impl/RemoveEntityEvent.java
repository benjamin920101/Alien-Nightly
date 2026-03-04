/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.Entity$RemovalReason
 */
package dev.luminous.api.events.impl;

import net.minecraft.entity.Entity;

public class RemoveEntityEvent {
    public static final RemoveEntityEvent instance = new RemoveEntityEvent();
    private class_1297 entity;
    private class_1297.class_5529 removalReason;

    private RemoveEntityEvent() {
    }

    public static RemoveEntityEvent get(class_1297 entity, class_1297.class_5529 removalReason) {
        RemoveEntityEvent.instance.entity = entity;
        RemoveEntityEvent.instance.removalReason = removalReason;
        return instance;
    }

    public class_1297 getEntity() {
        return this.entity;
    }

    public class_1297.class_5529 getRemovalReason() {
        return this.removalReason;
    }
}

