/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.model.BakedModel
 *  net.minecraft.item.ItemStack
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.item.BuiltinModelItemRenderer
 *  net.minecraft.client.render.item.ItemRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.luminous.asm.accessors;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
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

