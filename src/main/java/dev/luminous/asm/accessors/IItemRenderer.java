/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1087
 *  net.minecraft.class_1799
 *  net.minecraft.class_4587
 *  net.minecraft.class_4588
 *  net.minecraft.class_756
 *  net.minecraft.class_918
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.luminous.asm.accessors;

import net.minecraft.class_1087;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_756;
import net.minecraft.class_918;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_918.class})
public interface IItemRenderer {
    @Accessor(value="field_27770")
    public class_756 hookGetBuiltinModelItemRenderer();

    @Invoker(value="method_23182")
    public void hookRenderBakedItemModel(class_1087 var1, class_1799 var2, int var3, int var4, class_4587 var5, class_4588 var6);
}

