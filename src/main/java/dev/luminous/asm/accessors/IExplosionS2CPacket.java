/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.s2c.play.ExplosionS2CPacket
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2664.class})
public interface IExplosionS2CPacket {
    @Mutable
    @Accessor(value="field_12176")
    public void setVelocityX(float var1);

    @Mutable
    @Accessor(value="field_12183")
    public void setVelocityY(float var1);

    @Mutable
    @Accessor(value="field_12182")
    public void setVelocityZ(float var1);

    @Accessor(value="field_12176")
    public float getX();

    @Accessor(value="field_12183")
    public float getY();

    @Accessor(value="field_12182")
    public float getZ();
}

