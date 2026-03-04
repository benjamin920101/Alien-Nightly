/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 */
package dev.luminous.api.utils.entity;

import dev.luminous.api.utils.entity.CopyPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerEntityPredict {
    public final class_1657 player;
    public final class_1657 predict;

    public PlayerEntityPredict(class_1657 player, double maxMotionY, int ticks, int simulation, boolean step, boolean doubleStep, boolean jump, boolean inBlockPause) {
        this.player = player;
        this.predict = ticks > 0 ? new CopyPlayerEntity(player, true, maxMotionY, ticks, simulation, step, doubleStep, jump, inBlockPause) : player;
    }
}

