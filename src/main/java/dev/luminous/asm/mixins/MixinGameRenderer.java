/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.projectile.ProjectileUtil
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Position
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.hit.HitResult
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.util.hit.EntityHitResult
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.block.enums.CameraSubmersionType
 *  net.minecraft.client.render.GameRenderer
 *  net.minecraft.util.math.RotationAxis
 *  net.minecraft.client.render.RenderTickCounter
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.utils.render.TextUtil;
import dev.luminous.api.utils.world.InteractUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.Freecam;
import dev.luminous.mod.modules.impl.player.InteractTweaks;
import dev.luminous.mod.modules.impl.render.AspectRatio;
import dev.luminous.mod.modules.impl.render.Chams;
import dev.luminous.mod.modules.impl.render.Fov;
import dev.luminous.mod.modules.impl.render.HighLight;
import dev.luminous.mod.modules.impl.render.NoRender;
import dev.luminous.mod.modules.impl.render.Zoom;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.client.render.RenderTickCounter;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_757.class})
public class MixinGameRenderer {
    @Shadow
    @Final
    class_310 field_4015;
    @Shadow
    private float field_4019;
    @Shadow
    private float field_3999;
    @Shadow
    private boolean field_4001;
    @Shadow
    private float field_4005;
    @Shadow
    private float field_3988;
    @Shadow
    private float field_4004;
    @Shadow
    private float field_4025;
    @Unique
    private class_1297 cameraEntity;
    @Unique
    private float originalYaw;
    @Unique
    private float originalPitch;

    @Shadow
    private static class_239 method_56154(class_239 hitResult, class_243 cameraPos, double interactionRange) {
        class_243 vec3d = hitResult.method_17784();
        if (!vec3d.method_24802((class_2374)cameraPos, interactionRange)) {
            class_243 vec3d2 = hitResult.method_17784();
            class_2350 direction = class_2350.method_10142((double)(vec3d2.field_1352 - cameraPos.field_1352), (double)(vec3d2.field_1351 - cameraPos.field_1351), (double)(vec3d2.field_1350 - cameraPos.field_1350));
            return class_3965.method_17778((class_243)vec3d2, (class_2350)direction, (class_2338)class_2338.method_49638((class_2374)vec3d2));
        }
        return hitResult;
    }

    @Inject(method={"method_3189"}, at={@At(value="HEAD")}, cancellable=true)
    private void onShowFloatingItem(class_1799 floatingItem, CallbackInfo info) {
        if (floatingItem.method_7909() == class_1802.field_8288 && NoRender.INSTANCE.isOn() && NoRender.INSTANCE.totem.getValue()) {
            info.cancel();
        }
    }

    @Redirect(method={"method_3188"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_3532;method_16439(FFF)F"), require=0)
    private float applyCameraTransformationsMathHelperLerpProxy(float delta, float first, float second) {
        return NoRender.INSTANCE.isOn() && NoRender.INSTANCE.nausea.getValue() ? 0.0f : class_3532.method_16439((float)delta, (float)first, (float)second);
    }

    @Inject(method={"method_3198"}, at={@At(value="HEAD")}, cancellable=true)
    private void tiltViewWhenHurtHook(class_4587 matrices, float tickDelta, CallbackInfo ci) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.hurtCam.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_3202"}, at={@At(value="HEAD")}, cancellable=true)
    public void hookOutline(CallbackInfoReturnable<Boolean> cir) {
        if (HighLight.INSTANCE.isOn()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(at={@At(value="FIELD", target="Lnet/minecraft/class_757;field_3992:Z", opcode=180, ordinal=0)}, method={"method_3188"})
    void render3dHook(class_9779 tickCounter, CallbackInfo ci) {
        if (!Module.nullCheck()) {
            class_4184 camera = class_310.method_1551().field_1773.method_19418();
            class_4587 matrixStack = new class_4587();
            matrixStack.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
            matrixStack.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
            TextUtil.lastProjMat.set((Matrix4fc)RenderSystem.getProjectionMatrix());
            TextUtil.lastModMat.set((Matrix4fc)RenderSystem.getModelViewMatrix());
            TextUtil.lastWorldSpaceMatrix.set((Matrix4fc)matrixStack.method_23760().method_23761());
            Alien.FPS.record();
            Alien.MODULE.render3D(matrixStack);
        }
    }

    @Inject(at={@At(value="TAIL")}, method={"method_3188"})
    void render3dTail(class_9779 tickCounter, CallbackInfo ci) {
        if (Chams.INSTANCE.isOn() && Chams.INSTANCE.hand.booleanValue) {
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    @Inject(method={"method_3196(Lnet/minecraft/class_4184;FZ)D"}, at={@At(value="HEAD")}, cancellable=true)
    public void getFov(class_4184 camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> ci) {
        if (!this.field_4001 && (Fov.INSTANCE.isOn() || Zoom.on)) {
            class_5636 cameraSubmersionType;
            double d = 70.0;
            if (changingFov) {
                if (Fov.INSTANCE.isOn()) {
                    double fov = Fov.INSTANCE.fov.getValue();
                    if (Zoom.on) {
                        ci.setReturnValue((Object)Math.min(Math.max(fov - Zoom.INSTANCE.currentFov, 1.0), 177.0));
                    } else {
                        ci.setReturnValue((Object)fov);
                    }
                    return;
                }
                d = ((Integer)this.field_4015.field_1690.method_41808().method_41753()).intValue();
                d *= (double)class_3532.method_16439((float)tickDelta, (float)this.field_3999, (float)this.field_4019);
                if (Zoom.on) {
                    d = Math.min(Math.max(d - Zoom.INSTANCE.currentFov, 1.0), 177.0);
                }
            } else if (Fov.INSTANCE.isOn()) {
                ci.setReturnValue((Object)Fov.INSTANCE.itemFov.getValue());
                return;
            }
            if (camera.method_19331() instanceof class_1309 && ((class_1309)camera.method_19331()).method_29504()) {
                float f = Math.min((float)((class_1309)camera.method_19331()).field_6213 + tickDelta, 20.0f);
                d /= (double)((1.0f - 500.0f / (f + 500.0f)) * 2.0f + 1.0f);
            }
            if ((cameraSubmersionType = camera.method_19334()) == class_5636.field_27885 || cameraSubmersionType == class_5636.field_27886) {
                d *= class_3532.method_16436((double)((Double)this.field_4015.field_1690.method_42454().method_41753()), (double)1.0, (double)0.8571428656578064);
            }
            ci.setReturnValue((Object)d);
        }
    }

    @Inject(method={"method_3174"}, at={@At(value="HEAD")}, cancellable=true)
    private static void getNightVisionStrengthHook(class_1309 entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
        class_1293 statusEffectInstance = entity.method_6112(class_1294.field_5925);
        if (statusEffectInstance == null) {
            cir.setReturnValue((Object)Float.valueOf(1.0f));
        }
    }

    @Inject(method={"method_3188"}, at={@At(value="INVOKE", target="Lnet/minecraft/class_757;method_3172(Lnet/minecraft/class_4184;FLorg/joml/Matrix4f;)V", shift=At.Shift.AFTER)})
    public void postRender3dHook(class_9779 tickCounter, CallbackInfo ci) {
        Alien.SHADER.renderShaders();
    }

    @Inject(method={"method_22973"}, at={@At(value="TAIL")}, cancellable=true)
    public void getBasicProjectionMatrixHook(double fov, CallbackInfoReturnable<Matrix4f> cir) {
        if (AspectRatio.INSTANCE.isOn()) {
            class_4587 matrixStack = new class_4587();
            matrixStack.method_23760().method_23761().identity();
            if (this.field_4005 != 1.0f) {
                matrixStack.method_46416(this.field_3988, -this.field_4004, 0.0f);
                matrixStack.method_22905(this.field_4005, this.field_4005, 1.0f);
            }
            matrixStack.method_23760().method_23761().mul((Matrix4fc)new Matrix4f().setPerspective((float)(fov * 0.01745329238474369), AspectRatio.INSTANCE.ratio.getValueFloat(), 0.05f, this.field_4025 * 4.0f));
            cir.setReturnValue((Object)matrixStack.method_23760().method_23761());
        }
    }

    @Inject(method={"method_56153"}, at={@At(value="HEAD")}, cancellable=true)
    private void findCrosshairTarget(class_1297 camera, double blockInteractionRange, double entityInteractionRange, float tickDelta, CallbackInfoReturnable<class_239> cir) {
        if (Freecam.INSTANCE.isOn()) {
            cir.setReturnValue((Object)InteractUtil.getRtxTarget(Freecam.INSTANCE.getFakeYaw(), Freecam.INSTANCE.getFakePitch(), Freecam.INSTANCE.getFakeX(), Freecam.INSTANCE.getFakeY(), Freecam.INSTANCE.getFakeZ()));
        } else {
            double d = Math.max(blockInteractionRange, entityInteractionRange);
            double e = class_3532.method_33723((double)d);
            class_243 vec3d = camera.method_5836(tickDelta);
            InteractTweaks.INSTANCE.isActive = InteractTweaks.INSTANCE.ghostHand();
            class_239 hitResult = camera.method_5745(d, tickDelta, false);
            InteractTweaks.INSTANCE.isActive = false;
            double f = hitResult.method_17784().method_1025(vec3d);
            if (hitResult.method_17783() != class_239.class_240.field_1333) {
                e = f;
                d = Math.sqrt(f);
            }
            class_243 vec3d2 = camera.method_5828(tickDelta);
            class_243 vec3d3 = vec3d.method_1031(vec3d2.field_1352 * d, vec3d2.field_1351 * d, vec3d2.field_1350 * d);
            class_238 box = camera.method_5829().method_18804(vec3d2.method_1021(d)).method_1009(1.0, 1.0, 1.0);
            if (!InteractTweaks.INSTANCE.noEntityTrace()) {
                class_3966 entityHitResult = class_1675.method_18075((class_1297)camera, (class_243)vec3d, (class_243)vec3d3, (class_238)box, entity -> !entity.method_7325() && entity.method_5863(), (double)e);
                cir.setReturnValue((Object)(entityHitResult != null && entityHitResult.method_17784().method_1025(vec3d) < f ? MixinGameRenderer.method_56154((class_239)entityHitResult, vec3d, entityInteractionRange) : MixinGameRenderer.method_56154(hitResult, vec3d, blockInteractionRange)));
            } else {
                cir.setReturnValue((Object)MixinGameRenderer.method_56154(hitResult, vec3d, blockInteractionRange));
            }
        }
    }
}

