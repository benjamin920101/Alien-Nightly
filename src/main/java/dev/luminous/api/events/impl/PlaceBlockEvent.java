/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 */
package dev.luminous.api.events.impl;

import net.minecraft.class_2248;
import net.minecraft.class_2338;

public class PlaceBlockEvent {
    private static final PlaceBlockEvent INSTANCE = new PlaceBlockEvent();
    public class_2338 blockPos;
    public class_2248 block;

    private PlaceBlockEvent() {
    }

    public static PlaceBlockEvent get(class_2338 blockPos, class_2248 block) {
        PlaceBlockEvent.INSTANCE.blockPos = blockPos;
        PlaceBlockEvent.INSTANCE.block = block;
        return INSTANCE;
    }
}

