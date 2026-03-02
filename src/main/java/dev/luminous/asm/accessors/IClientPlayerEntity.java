/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_746.class})
public interface IClientPlayerEntity {
    @Accessor(value="field_3923")
    public void setTicksSinceLastPositionPacketSent(int var1);

    @Accessor(value="field_3941")
    public float getLastYaw();

    @Accessor(value="field_3941")
    public void setLastYaw(float var1);

    @Accessor(value="field_3925")
    public float getLastPitch();

    @Accessor(value="field_3925")
    public void setLastPitch(float var1);
}

