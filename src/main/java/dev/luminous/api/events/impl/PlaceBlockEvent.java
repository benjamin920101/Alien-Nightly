/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.util.math.BlockPos
 */
package dev.luminous.api.events.impl;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

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

