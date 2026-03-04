/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.decoration.EndCrystalEntity
 *  net.minecraft.client.render.RenderLayer
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.Identifier
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.OverlayTexture
 *  net.minecraft.client.render.entity.EntityRendererFactory$Context
 *  net.minecraft.client.gl.ShaderProgram
 *  net.minecraft.client.model.ModelPart
 *  net.minecraft.client.render.GameRenderer
 *  net.minecraft.util.math.RotationAxis
 *  net.minecraft.client.render.entity.EndCrystalEntityRenderer
 *  net.minecraft.client.render.entity.EnderDragonEntityRenderer
 *  net.minecraft.client.render.entity.EntityRenderer
 *  net.minecraft.client.render.item.ItemRenderer
 *  org.joml.Quaternionf
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.api.utils.render.ModelPlayer;
import dev.luminous.mod.modules.impl.render.Chams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_892.class})
public abstract class MixinEndCrystalEntityRenderer
extends class_897<class_1511> {
    @Mutable
    @Final
    @Shadow
    private static class_1921 field_21736;
    @Shadow
    @Final
    private static class_2960 field_4663;
    @Unique
    private static final class_2960 BLANK;
    @Unique
    private static final class_1921 END_CRYSTAL_BLANK;
    @Unique
    private static final class_1921 END_CRYSTAL_CUSTOM;
    @Final
    @Shadow
    private static float field_21002;
    @Final
    @Shadow
    private class_630 field_21003;
    @Final
    @Shadow
    private class_630 field_21004;
    @Final
    @Shadow
    private class_630 field_21005;

    protected MixinEndCrystalEntityRenderer(class_5617.class_5618 ctx) {
        super(ctx);
    }

    @Unique
    private float yOffset(int age, float tickDelta, Chams module) {
        float f = ((float)age + tickDelta) * module.floatValue.getValueFloat();
        float g = class_3532.method_15374((float)(f * 0.2f)) / 2.0f + 0.5f;
        g = (g * g + g) * 0.4f * module.bounceHeight.getValueFloat();
        return g - 1.4f + module.floatOffset.getValueFloat();
    }

    @Inject(method={"method_3908(Lnet/minecraft/class_1511;FFLnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void render(class_1511 endCrystalEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        Chams module = Chams.INSTANCE;
        if (module.customCrystal()) {
            ci.cancel();
            int age = module.spinSync.getValue() ? module.age : endCrystalEntity.field_7034;
            float h = this.yOffset(age, g, module);
            float j = ((float)age + g) * 3.0f * module.spinValue.getValueFloat();
            matrixStack.method_22903();
            if (module.custom.getValue()) {
                class_5944 s = RenderSystem.getShader();
                if (module.depth.getValue()) {
                    RenderSystem.enableDepthTest();
                }
                RenderSystem.enableBlend();
                if (module.chamsTexture.getValue()) {
                    RenderSystem.setShaderTexture((int)0, (class_2960)field_4663);
                    RenderSystem.setShader(class_757::method_34543);
                } else {
                    RenderSystem.setShader(class_757::method_34540);
                }
                matrixStack.method_22903();
                matrixStack.method_22905(2.0f * module.scale.getValueFloat(), 2.0f * module.scale.getValueFloat(), 2.0f * module.scale.getValueFloat());
                matrixStack.method_46416(0.0f, -0.5f, 0.0f);
                matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
                matrixStack.method_46416(0.0f, 1.5f + h / 2.0f, 0.0f);
                matrixStack.method_22907(new Quaternionf().setAngleAxis(1.0471976f, field_21002, 0.0f, field_21002));
                if (module.outerFrame.booleanValue) {
                    ModelPlayer.render(matrixStack, this.field_21004, module.fill, module.line, 1.0, module.chamsTexture.getValue());
                }
                matrixStack.method_22905(0.875f, 0.875f, 0.875f);
                matrixStack.method_22907(new Quaternionf().setAngleAxis(1.0471976f, field_21002, 0.0f, field_21002));
                matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
                if (module.innerFrame.booleanValue) {
                    ModelPlayer.render(matrixStack, this.field_21004, module.fill, module.line, 1.0, module.chamsTexture.getValue());
                }
                matrixStack.method_22905(0.875f, 0.875f, 0.875f);
                matrixStack.method_22907(new Quaternionf().setAngleAxis(1.0471976f, field_21002, 0.0f, field_21002));
                matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
                if (module.core.booleanValue) {
                    ModelPlayer.render(matrixStack, this.field_21003, module.fill, module.line, 1.0, module.chamsTexture.getValue());
                }
                matrixStack.method_22909();
                RenderSystem.setShader(() -> s);
                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();
            }
            class_4588 vertexConsumer = class_918.method_29711((class_4597)vertexConsumerProvider, (class_1921)(module.texture.getValue() ? END_CRYSTAL_CUSTOM : END_CRYSTAL_BLANK), (boolean)false, (boolean)module.glint.getValue());
            matrixStack.method_22903();
            matrixStack.method_22905(2.0f * module.scale.getValueFloat(), 2.0f * module.scale.getValueFloat(), 2.0f * module.scale.getValueFloat());
            matrixStack.method_46416(0.0f, -0.5f, 0.0f);
            int k = class_4608.field_21444;
            if (endCrystalEntity.method_6836()) {
                this.field_21005.method_22698(matrixStack, vertexConsumer, i, k);
            }
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
            matrixStack.method_46416(0.0f, 1.5f + h / 2.0f, 0.0f);
            matrixStack.method_22907(new Quaternionf().setAngleAxis(1.0471976f, field_21002, 0.0f, field_21002));
            if (module.outerFrame.booleanValue) {
                this.field_21004.method_22699(matrixStack, vertexConsumer, i, k, module.outerFrame.getValue().getRGB());
            }
            matrixStack.method_22905(0.875f, 0.875f, 0.875f);
            matrixStack.method_22907(new Quaternionf().setAngleAxis(1.0471976f, field_21002, 0.0f, field_21002));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
            if (module.innerFrame.booleanValue) {
                this.field_21004.method_22699(matrixStack, vertexConsumer, i, k, module.innerFrame.getValue().getRGB());
            }
            matrixStack.method_22905(0.875f, 0.875f, 0.875f);
            matrixStack.method_22907(new Quaternionf().setAngleAxis(1.0471976f, field_21002, 0.0f, field_21002));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(j));
            if (module.core.booleanValue) {
                this.field_21003.method_22699(matrixStack, vertexConsumer, i, k, module.core.getValue().getRGB());
            }
            matrixStack.method_22909();
            matrixStack.method_22909();
            class_2338 blockPos = endCrystalEntity.method_6838();
            if (blockPos != null) {
                float m = (float)blockPos.method_10263() + 0.5f;
                float n = (float)blockPos.method_10264() + 0.5f;
                float o = (float)blockPos.method_10260() + 0.5f;
                float p = (float)((double)m - endCrystalEntity.method_23317());
                float q = (float)((double)n - endCrystalEntity.method_23318());
                float r = (float)((double)o - endCrystalEntity.method_23321());
                matrixStack.method_46416(p, q, r);
                class_895.method_3917((float)(-p), (float)(-q + h), (float)(-r), (float)g, (int)endCrystalEntity.field_7034, (class_4587)matrixStack, (class_4597)vertexConsumerProvider, (int)i);
            }
            super.method_3936((class_1297)endCrystalEntity, f, g, matrixStack, vertexConsumerProvider, i);
        }
    }

    static {
        BLANK = class_2960.method_60654((String)"textures/blank.png");
        END_CRYSTAL_BLANK = class_1921.method_23580((class_2960)BLANK);
        END_CRYSTAL_CUSTOM = class_1921.method_23580((class_2960)field_4663);
    }
}

