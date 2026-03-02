/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2338
 */
package dev.luminous.api.events.impl;

import net.minecraft.class_2338;

public class BlockBreakingProgressEvent {
    private static final BlockBreakingProgressEvent INSTANCE = new BlockBreakingProgressEvent();
    private class_2338 pos;
    private int breakerId;
    private int progress;

    private BlockBreakingProgressEvent() {
    }

    public static BlockBreakingProgressEvent get(class_2338 pos, int breakerId, int progress) {
        BlockBreakingProgressEvent.INSTANCE.pos = pos;
        BlockBreakingProgressEvent.INSTANCE.breakerId = breakerId;
        BlockBreakingProgressEvent.INSTANCE.progress = progress;
        return INSTANCE;
    }

    public class_2338 getPosition() {
        return this.pos;
    }

    public int getBreakerId() {
        return this.breakerId;
    }

    public int getProgress() {
        return this.progress;
    }
}

