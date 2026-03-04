/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.entity.Entity;

public class LookDirectionEvent
extends Event {
    private static final LookDirectionEvent instance = new LookDirectionEvent();
    private class_1297 entity;
    private double cursorDeltaX;
    private double cursorDeltaY;

    private LookDirectionEvent() {
    }

    public static LookDirectionEvent get(class_1297 entity, double cursorDeltaX, double cursorDeltaY) {
        LookDirectionEvent.instance.entity = entity;
        LookDirectionEvent.instance.cursorDeltaX = cursorDeltaX;
        LookDirectionEvent.instance.cursorDeltaY = cursorDeltaY;
        instance.setCancelled(false);
        return instance;
    }

    public class_1297 getEntity() {
        return this.entity;
    }

    public double getCursorDeltaX() {
        return this.cursorDeltaX;
    }

    public double getCursorDeltaY() {
        return this.cursorDeltaY;
    }
}

