/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.model.BakedModel
 *  net.minecraft.item.ItemStack
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.model.json.ModelTransformationMode
 *  net.minecraft.client.render.item.ItemRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.api.utils.render.SimpleItemModel;
import dev.luminous.mod.modules.impl.render.NoRender;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_918.class})
public class MixinItemRenderer {
    @Unique
    private final SimpleItemModel flattenedModel = new SimpleItemModel();
    @Unique
    private class_811 renderMode;

    @Inject(method={"method_23179*"}, at={@At(value="HEAD")})
    private void getRenderType(class_1799 itemStack, class_811 transformationMode, boolean leftHand, class_4587 matrices, class_4597 vertexConsumers, int light, int overlay, class_1087 model, CallbackInfo ci) {
        this.renderMode = transformationMode;
    }

    @ModifyVariable(method={"method_23182"}, at=@At(value="HEAD"), index=1, argsOnly=true, require=0)
    private class_1087 replaceItemModelClass(class_1087 model, class_1087 arg, class_1799 stack, int light, int overlay, class_4587 matrices, class_4588 vertices) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fastItem.getValue() && !NoRender.INSTANCE.renderSidesOfItems.getValue() && !stack.method_7960() && !model.method_4712() && this.renderMode == class_811.field_4318) {
            this.flattenedModel.setItem(model);
            return this.flattenedModel;
        }
        return model;
    }
}

