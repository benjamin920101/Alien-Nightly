/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.BlockView
 *  net.minecraft.client.render.Camera
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArgs
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 *  org.spongepowered.asm.mixin.injection.invoke.arg.Args
 */
package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.player.FreeLook;
import dev.luminous.mod.modules.impl.player.Freecam;
import dev.luminous.mod.modules.impl.render.CameraClip;
import dev.luminous.mod.modules.impl.render.MotionCamera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={class_4184.class})
public abstract class MixinCamera {
    @Shadow
    private boolean field_18719;

    @Shadow
    protected abstract float method_19318(float var1);

    @ModifyArgs(method={"method_19321"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_4184;method_19324(FFF)V", ordinal=0))
    private void modifyCameraDistance(Args args) {
        if (CameraClip.INSTANCE.isOn()) {
            args.set(0, (Object)Float.valueOf(-this.method_19318((float)CameraClip.INSTANCE.getDistance())));
        }
    }

    @Inject(method={"method_19318"}, at={@At(value="HEAD")}, cancellable=true)
    private void onClipToSpace(float f, CallbackInfoReturnable<Float> cir) {
        if (CameraClip.INSTANCE.isOn()) {
            cir.setReturnValue((Object)Float.valueOf((float)CameraClip.INSTANCE.getDistance()));
        }
    }

    @Inject(method={"method_19321"}, at={@At(value="TAIL")})
    private void updateHook(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (Freecam.INSTANCE.isOn()) {
            this.field_18719 = true;
        }
    }

    @ModifyArgs(method={"method_19321"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_4184;method_19325(FF)V"))
    private void setRotationHook(Args args) {
        if (Freecam.INSTANCE.isOn()) {
            args.setAll(new Object[]{Float.valueOf(Freecam.INSTANCE.getFakeYaw()), Float.valueOf(Freecam.INSTANCE.getFakePitch())});
        } else if (FreeLook.INSTANCE.isOn()) {
            args.setAll(new Object[]{Float.valueOf(FreeLook.INSTANCE.getFakeYaw()), Float.valueOf(FreeLook.INSTANCE.getFakePitch())});
        }
    }

    @ModifyArgs(method={"method_19321"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_4184;method_19327(DDD)V"))
    private void setPosHook(Args args) {
        if (Freecam.INSTANCE.isOn()) {
            args.setAll(new Object[]{Freecam.INSTANCE.getFakeX(), Freecam.INSTANCE.getFakeY(), Freecam.INSTANCE.getFakeZ()});
        } else if (MotionCamera.INSTANCE.on()) {
            args.setAll(new Object[]{MotionCamera.INSTANCE.getFakeX(), MotionCamera.INSTANCE.getFakeY(), MotionCamera.INSTANCE.getFakeZ()});
        }
    }
}

