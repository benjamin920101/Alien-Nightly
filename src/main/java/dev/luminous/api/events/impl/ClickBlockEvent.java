/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ClickBlockEvent
extends Event {
    private class_2338 pos;
    private class_2350 direction;
    private static final ClickBlockEvent INSTANCE = new ClickBlockEvent();

    private ClickBlockEvent() {
    }

    public class_2350 getDirection() {
        return this.direction;
    }

    public class_2338 getPos() {
        return this.pos;
    }

    public static ClickBlockEvent get(class_2338 pos, class_2350 direction) {
        ClickBlockEvent.INSTANCE.pos = pos;
        ClickBlockEvent.INSTANCE.direction = direction;
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }
}

