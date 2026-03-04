/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.collection.DefaultedList
 *  net.minecraft.component.type.ContainerComponent
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.component.type.ContainerComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_9288.class})
public interface IContainerComponent {
    @Accessor(value="field_49338")
    public class_2371<class_1799> getStacks();
}

