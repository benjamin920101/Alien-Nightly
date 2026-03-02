/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.class_243;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_243.class})
public interface IVec3d {
    @Mutable
    @Accessor(value="field_1352")
    public void setX(double var1);

    @Mutable
    @Accessor(value="field_1351")
    public void setY(double var1);

    @Mutable
    @Accessor(value="field_1350")
    public void setZ(double var1);
}

