/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.entity.model.EntityModel
 *  net.minecraft.client.render.entity.LivingEntityRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArgs
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.invoke.arg.Args
 */
package dev.luminous.asm.mixins;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.core.impl.RotationManager;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.render.NoRender;
import java.awt.Color;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={class_922.class})
public abstract class MixinLivingEntityRenderer<T extends class_1309, M extends class_583<T>> {
    @Unique
    private class_1309 lastEntity;
    @Unique
    private float originalYaw;
    @Unique
    private float originalHeadYaw;
    @Unique
    private float originalBodyYaw;
    @Unique
    private float originalPitch;
    @Unique
    private float originalPrevYaw;
    @Unique
    private float originalPrevHeadYaw;
    @Unique
    private float originalPrevBodyYaw;

    @Inject(method={"method_4054*"}, at={@At(value="HEAD")})
    public void onRenderPre(T livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        if (class_310.method_1551().field_1724 != null && livingEntity == class_310.method_1551().field_1724 && ClientSetting.INSTANCE.rotations.getValue()) {
            this.originalYaw = livingEntity.method_36454();
            this.originalHeadYaw = ((class_1309)livingEntity).field_6241;
            this.originalBodyYaw = ((class_1309)livingEntity).field_6283;
            this.originalPitch = livingEntity.method_36455();
            this.originalPrevYaw = ((class_1309)livingEntity).field_5982;
            this.originalPrevHeadYaw = ((class_1309)livingEntity).field_6259;
            this.originalPrevBodyYaw = ((class_1309)livingEntity).field_6220;
            livingEntity.method_36456(RotationManager.getRenderYawOffset());
            ((class_1309)livingEntity).field_6241 = RotationManager.getRotationYawHead();
            ((class_1309)livingEntity).field_6283 = RotationManager.getRenderYawOffset();
            livingEntity.method_36457(RotationManager.getRenderPitch());
            ((class_1309)livingEntity).field_5982 = RotationManager.getPrevRenderYawOffset();
            ((class_1309)livingEntity).field_6259 = RotationManager.getPrevRotationYawHead();
            ((class_1309)livingEntity).field_6220 = RotationManager.getPrevRenderYawOffset();
            ((class_1309)livingEntity).field_6004 = RotationManager.getPrevRenderPitch();
        }
        this.lastEntity = livingEntity;
    }

    @Inject(method={"method_4054*"}, at={@At(value="TAIL")})
    public void onRenderPost(T livingEntity, float f, float g, class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, CallbackInfo ci) {
        if (class_310.method_1551().field_1724 != null && livingEntity == class_310.method_1551().field_1724 && ClientSetting.INSTANCE.rotations.getValue()) {
            livingEntity.method_36456(this.originalYaw);
            ((class_1309)livingEntity).field_6241 = this.originalHeadYaw;
            ((class_1309)livingEntity).field_6283 = this.originalBodyYaw;
            livingEntity.method_36457(this.originalPitch);
            ((class_1309)livingEntity).field_5982 = this.originalPrevYaw;
            ((class_1309)livingEntity).field_6259 = this.originalPrevHeadYaw;
            ((class_1309)livingEntity).field_6220 = this.originalPrevBodyYaw;
            ((class_1309)livingEntity).field_6004 = this.originalPitch;
        }
    }

    @ModifyArgs(method={"method_4054*"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_583;method_2828(Lnet/minecraft/class_4587;Lnet/minecraft/class_4588;III)V"))
    private void renderHook(Args args) {
        class_1657 pl;
        class_1309 class_13092;
        float alpha = -1.0f;
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.antiPlayerCollision.getValue() && this.lastEntity != Wrapper.mc.field_1724 && (class_13092 = this.lastEntity) instanceof class_1657 && !(pl = (class_1657)class_13092).method_5767()) {
            alpha = MathUtil.clamp((float)(Wrapper.mc.field_1724.method_5707(this.lastEntity.method_19538()) / 3.0) + 0.2f, 0.0f, 1.0f);
        }
        if (alpha != -1.0f) {
            args.set(4, (Object)this.applyOpacity(0x26FFFFFF, alpha));
        }
    }

    @Unique
    int applyOpacity(int color_int, float opacity) {
        opacity = Math.min(1.0f, Math.max(0.0f, opacity));
        Color color = new Color(color_int);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * opacity)).getRGB();
    }
}

