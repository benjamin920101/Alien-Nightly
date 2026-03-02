/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  net.minecraft.class_4048
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_4048;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1297.class})
public interface IEntity {
    @Mutable
    @Accessor(value="field_22467")
    public void setPos(class_243 var1);

    @Accessor(value="field_18065")
    public class_4048 getDimensions();
}

