/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2586
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_2586;

public class RenderBlockEntityEvent
extends Event {
    private static final RenderBlockEntityEvent INSTANCE = new RenderBlockEntityEvent();
    public class_2586 blockEntity;

    public static RenderBlockEntityEvent get(class_2586 blockEntity) {
        INSTANCE.setCancelled(false);
        RenderBlockEntityEvent.INSTANCE.blockEntity = blockEntity;
        return INSTANCE;
    }
}

