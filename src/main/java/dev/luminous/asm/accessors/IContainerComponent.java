/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_2371
 *  net.minecraft.class_9288
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.class_1799;
import net.minecraft.class_2371;
import net.minecraft.class_9288;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_9288.class})
public interface IContainerComponent {
    @Accessor(value="field_49338")
    public class_2371<class_1799> getStacks();
}

