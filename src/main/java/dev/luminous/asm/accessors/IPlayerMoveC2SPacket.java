/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2828.class})
public interface IPlayerMoveC2SPacket {
    @Mutable
    @Accessor(value="field_29179")
    public void setOnGround(boolean var1);

    @Mutable
    @Accessor(value="field_12885")
    public void setPitch(float var1);

    @Mutable
    @Accessor(value="field_12887")
    public void setYaw(float var1);

    @Mutable
    @Accessor(value="field_12889")
    public void setX(double var1);

    @Mutable
    @Accessor(value="field_12884")
    public void setZ(double var1);
}

