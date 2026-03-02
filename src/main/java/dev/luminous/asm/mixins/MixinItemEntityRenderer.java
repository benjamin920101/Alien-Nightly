/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1087
 *  net.minecraft.class_1297
 *  net.minecraft.class_1542
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_4608
 *  net.minecraft.class_5617$class_5618
 *  net.minecraft.class_5819
 *  net.minecraft.class_811
 *  net.minecraft.class_897
 *  net.minecraft.class_916
 *  net.minecraft.class_918
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.NoRender;
import net.minecraft.class_1087;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_5617;
import net.minecraft.class_5819;
import net.minecraft.class_811;
import net.minecraft.class_897;
import net.minecraft.class_916;
import net.minecraft.class_918;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_916.class})
public abstract class MixinItemEntityRenderer
extends class_897<class_1542> {
    @Final
    @Shadow
    private class_918 field_4726;
    @Final
    @Shadow
    private class_5819 field_4725;

    protected MixinItemEntityRenderer(class_5617.class_5618 ctx) {
        super(ctx);
    }

    @Unique
    private static int getRenderedAmount(class_1799 stackSize) {
        int count = stackSize.method_7947();
        if (count <= 1) {
            return 1;
        }
        if (count <= 16) {
            return 2;
        }
        if (count <= 32) {
            return 3;
        }
        return count <= 48 ? 4 : 5;
    }

    @Inject(method={"method_3996*"}, at={@At(value="HEAD")}, cancellable=true)
    public void render(class_1542 itemEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fastItem.getValue()) {
            float t;
            float s;
            matrixStack.method_22903();
            class_1799 itemStack = itemEntity.method_6983();
            long j = itemStack.method_7960() ? 187L : (long)(class_1792.method_7880((class_1792)itemStack.method_7909()) + itemStack.method_7919());
            this.field_4725.method_43052(j);
            class_1087 bakedModel = this.field_4726.method_4019(itemStack, itemEntity.method_37908(), null, itemEntity.method_5628());
            boolean hasDepth = bakedModel.method_4712();
            this.field_4673 = NoRender.INSTANCE.castShadow.getValue() ? 0.15f : 0.0f;
            float l = class_3532.method_15374((float)(((float)itemEntity.method_6985() + g) / 10.0f + itemEntity.field_7203)) * 0.1f + 0.1f;
            float m = bakedModel.method_4709().method_3503((class_811)class_811.field_4318).field_4285.y();
            matrixStack.method_46416(0.0f, l + 0.25f * m, 0.0f);
            matrixStack.method_22907(this.field_4676.method_24197());
            float o = bakedModel.method_4709().field_4303.field_4285.x();
            float p = bakedModel.method_4709().field_4303.field_4285.y();
            float q = bakedModel.method_4709().field_4303.field_4285.z();
            int renderedAmount = MixinItemEntityRenderer.getRenderedAmount(itemStack);
            if (!hasDepth) {
                float r = -0.0f * (float)(renderedAmount - 1) * 0.5f * o;
                s = -0.0f * (float)(renderedAmount - 1) * 0.5f * p;
                t = -0.09375f * (float)(renderedAmount - 1) * 0.5f * q;
                matrixStack.method_46416(r, s, t);
            }
            for (int u = 0; u < renderedAmount; ++u) {
                matrixStack.method_22903();
                if (u > 0) {
                    if (hasDepth) {
                        s = (this.field_4725.method_43057() * 2.0f - 1.0f) * 0.15f;
                        t = (this.field_4725.method_43057() * 2.0f - 1.0f) * 0.15f;
                        float v = (this.field_4725.method_43057() * 2.0f - 1.0f) * 0.15f;
                        matrixStack.method_46416(s, t, v);
                    } else {
                        s = (this.field_4725.method_43057() * 2.0f - 1.0f) * 0.15f * 0.5f;
                        t = (this.field_4725.method_43057() * 2.0f - 1.0f) * 0.15f * 0.5f;
                        matrixStack.method_46416(s, t, 0.0f);
                    }
                }
                this.field_4726.method_23179(itemStack, class_811.field_4318, false, matrixStack, vertexConsumerProvider, i, class_4608.field_21444, bakedModel);
                matrixStack.method_22909();
                if (hasDepth) continue;
                matrixStack.method_46416(0.0f * o, 0.0f * p, 0.0425f * q);
            }
            matrixStack.method_22909();
            super.method_3936((class_1297)itemEntity, f, g, matrixStack, vertexConsumerProvider, i);
            ci.cancel();
        }
    }
}

