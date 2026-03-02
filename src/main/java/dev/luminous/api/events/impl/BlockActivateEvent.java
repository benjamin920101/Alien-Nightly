/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2680
 */
package dev.luminous.api.events.impl;

import net.minecraft.class_2680;

public class BlockActivateEvent {
    private static final BlockActivateEvent INSTANCE = new BlockActivateEvent();
    public class_2680 blockState;

    private BlockActivateEvent() {
    }

    public static BlockActivateEvent get(class_2680 blockState) {
        BlockActivateEvent.INSTANCE.blockState = blockState;
        return INSTANCE;
    }
}

