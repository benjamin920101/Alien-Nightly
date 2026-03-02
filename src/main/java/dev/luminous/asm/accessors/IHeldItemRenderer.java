/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_759
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.class_1799;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_759.class})
public interface IHeldItemRenderer {
    @Accessor(value="field_4043")
    public float getEquippedProgressMainHand();

    @Accessor(value="field_4043")
    public void setEquippedProgressMainHand(float var1);

    @Accessor(value="field_4052")
    public float getEquippedProgressOffHand();

    @Accessor(value="field_4052")
    public void setEquippedProgressOffHand(float var1);

    @Accessor(value="field_4047")
    public void setItemStackMainHand(class_1799 var1);

    @Accessor(value="field_4048")
    public void setItemStackOffHand(class_1799 var1);
}

