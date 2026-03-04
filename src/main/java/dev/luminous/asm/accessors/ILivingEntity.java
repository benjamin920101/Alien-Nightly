/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1309.class})
public interface ILivingEntity {
    @Accessor(value="field_6273")
    public int getLastAttackedTicks();

    @Accessor(value="field_6228")
    public int getLastJumpCooldown();

    @Accessor(value="field_6228")
    public void setLastJumpCooldown(int var1);

    @Accessor(value="field_6243")
    public float getLeaningPitch();

    @Accessor(value="field_6243")
    public void setLeaningPitch(float var1);

    @Accessor(value="field_6264")
    public void setLastLeaningPitch(float var1);
}

