/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_310
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

import dev.luminous.Alien;
import dev.luminous.api.events.impl.LookDirectionEvent;
import dev.luminous.api.events.impl.SprintEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.impl.movement.Velocity;
import dev.luminous.mod.modules.impl.render.NoRender;
import dev.luminous.mod.modules.impl.render.ShaderModule;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value={class_1297.class})
public abstract class MixinEntity {
    @Shadow
    private static final int field_29976 = 3;

    @Inject(method={"method_5872"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if (class_1297.class.cast(this) == Wrapper.mc.field_1724) {
            LookDirectionEvent lookDirectionEvent = LookDirectionEvent.get((class_1297)class_1297.class.cast(this), cursorDeltaX, cursorDeltaY);
            Alien.EVENT_BUS.post(lookDirectionEvent);
            if (lookDirectionEvent.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"method_5756(Lnet/minecraft/class_1657;)Z"}, cancellable=true)
    private void onIsInvisibleCheck(class_1657 message, CallbackInfoReturnable<Boolean> cir) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.invisible.getValue()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"method_5851"}, at={@At(value="HEAD")}, cancellable=true)
    void isGlowingHook(CallbackInfoReturnable<Boolean> cir) {
        if (ShaderModule.INSTANCE.isOn()) {
            cir.setReturnValue((Object)ShaderModule.INSTANCE.shouldRender((class_1297)class_1297.class.cast(this)));
        }
    }

    @ModifyArgs(method={"method_5697"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_1297;method_5762(DDD)V"))
    private void pushAwayFromHook(Args args) {
        if (class_1297.class.cast(this) == class_310.method_1551().field_1724 && Velocity.INSTANCE.isOn() && Velocity.INSTANCE.entityPush.getValue()) {
            args.set(0, (Object)0.0);
            args.set(1, (Object)0.0);
            args.set(2, (Object)0.0);
        }
    }

    @Inject(method={"method_5809"}, at={@At(value="HEAD")}, cancellable=true)
    void isOnFireHook(CallbackInfoReturnable<Boolean> cir) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fireEntity.getValue()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"method_5728"}, at={@At(value="HEAD")}, cancellable=true)
    public void setSprintingHook(boolean sprinting, CallbackInfo ci) {
        if (class_1297.class.cast(this) == class_310.method_1551().field_1724) {
            SprintEvent event = SprintEvent.get();
            Alien.EVENT_BUS.post(event);
            if (event.isCancelled()) {
                ci.cancel();
                sprinting = event.isSprint();
                this.method_5729(3, sprinting);
            }
        }
    }

    @Shadow
    protected void method_5729(int index, boolean value) {
    }
}

