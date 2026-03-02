/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1282
 *  net.minecraft.class_1297
 *  net.minecraft.class_1927
 *  net.minecraft.class_1937
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.class_1282;
import net.minecraft.class_1297;
import net.minecraft.class_1927;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_1927.class})
public interface IExplosion {
    @Mutable
    @Accessor(value="field_9195")
    public void setX(double var1);

    @Mutable
    @Accessor(value="field_9192")
    public void setY(double var1);

    @Mutable
    @Accessor(value="field_9189")
    public void setZ(double var1);

    @Mutable
    @Accessor(value="field_9190")
    public void setPower(float var1);

    @Mutable
    @Accessor(value="field_9185")
    public void setEntity(class_1297 var1);

    @Mutable
    @Accessor(value="field_9187")
    public class_1937 getWorld();

    @Mutable
    @Accessor(value="field_9187")
    public void setWorld(class_1937 var1);

    @Mutable
    @Accessor(value="field_9193")
    public class_1282 getDamageSource();
}

