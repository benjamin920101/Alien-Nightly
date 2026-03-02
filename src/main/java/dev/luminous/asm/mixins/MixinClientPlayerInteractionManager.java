/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_3965
 *  net.minecraft.class_636
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Constant
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyConstant
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.ClickBlockEvent;
import dev.luminous.api.events.impl.InteractBlockEvent;
import dev.luminous.api.events.impl.InteractItemEvent;
import dev.luminous.mod.modules.impl.player.InteractTweaks;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_3965;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_636.class})
public class MixinClientPlayerInteractionManager {
    @Shadow
    private class_1799 field_3718;

    @ModifyVariable(method={"method_2922"}, at=@At(value="STORE"))
    private class_1799 stack(class_1799 stack) {
        return InteractTweaks.INSTANCE.noReset() ? this.field_3718 : stack;
    }

    @ModifyConstant(method={"method_2902"}, constant={@Constant(intValue=5)})
    private int MiningCooldownFix(int value) {
        return InteractTweaks.INSTANCE.noDelay() ? 0 : value;
    }

    @Inject(method={"method_2919"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookInteractItem(class_1657 player, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
        InteractItemEvent event = InteractItemEvent.getPre(hand);
        Alien.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            cir.setReturnValue((Object)class_1269.field_5811);
        }
    }

    @Inject(method={"method_2919"}, at={@At(value="RETURN")})
    private void hookInteractItemReturn(class_1657 player, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
        Alien.EVENT_BUS.post(InteractItemEvent.getPost(hand));
    }

    @Inject(method={"method_2896"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookInteractBlock(class_746 player, class_1268 hand, class_3965 hitResult, CallbackInfoReturnable<class_1269> cir) {
        InteractBlockEvent event = InteractBlockEvent.getPre(hand);
        Alien.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            cir.setReturnValue((Object)class_1269.field_5811);
        }
    }

    @Inject(method={"method_2896"}, at={@At(value="RETURN")})
    private void hookInteractBlockReturn(class_746 player, class_1268 hand, class_3965 hitResult, CallbackInfoReturnable<class_1269> cir) {
        Alien.EVENT_BUS.post(InteractBlockEvent.getPost(hand));
    }

    @Inject(method={"method_2925"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookCancelBlockBreaking(CallbackInfo callbackInfo) {
        if (InteractTweaks.INSTANCE.noAbort()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"method_2910"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAttackBlock(class_2338 pos, class_2350 direction, CallbackInfoReturnable<Boolean> cir) {
        ClickBlockEvent event = ClickBlockEvent.get(pos, direction);
        Alien.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            cir.setReturnValue((Object)false);
        }
    }
}

