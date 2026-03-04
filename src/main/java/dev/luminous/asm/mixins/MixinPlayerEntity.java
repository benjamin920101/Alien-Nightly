/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.entity.EntityPose
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.Event;
import dev.luminous.api.events.impl.JumpEvent;
import dev.luminous.api.events.impl.TravelEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.player.InteractTweaks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1657.class})
public class MixinPlayerEntity
implements Wrapper {
    @Inject(method={"method_52558"}, at={@At(value="RETURN")}, cancellable=true)
    private void poseNotCollide(class_4050 pose, CallbackInfoReturnable<Boolean> cir) {
        if (class_1657.class.cast(this) == MixinPlayerEntity.mc.field_1724 && !ClientSetting.INSTANCE.crawl.getValue() && pose == class_4050.field_18079) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"method_55754"}, at={@At(value="HEAD")}, cancellable=true)
    public void getBlockInteractionRangeHook(CallbackInfoReturnable<Double> cir) {
        if (InteractTweaks.INSTANCE.reach()) {
            cir.setReturnValue((Object)InteractTweaks.INSTANCE.blockRange.getValue());
        }
    }

    @Inject(method={"method_55755"}, at={@At(value="HEAD")}, cancellable=true)
    public void getEntityInteractionRangeHook(CallbackInfoReturnable<Double> cir) {
        if (InteractTweaks.INSTANCE.reach()) {
            cir.setReturnValue((Object)InteractTweaks.INSTANCE.entityRange.getValue());
        }
    }

    @Inject(method={"method_6043"}, at={@At(value="HEAD")})
    private void onJumpPre(CallbackInfo ci) {
        Alien.EVENT_BUS.post(JumpEvent.get(Event.Stage.Pre));
    }

    @Inject(method={"method_6043"}, at={@At(value="RETURN")})
    private void onJumpPost(CallbackInfo ci) {
        Alien.EVENT_BUS.post(JumpEvent.get(Event.Stage.Post));
    }

    @Inject(method={"method_6091"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTravelPre(class_243 movementInput, CallbackInfo ci) {
        class_1657 player = (class_1657)class_1657.class.cast(this);
        if (player == MixinPlayerEntity.mc.field_1724) {
            TravelEvent event = TravelEvent.get(Event.Stage.Pre, player);
            Alien.EVENT_BUS.post(event);
            if (event.isCancelled()) {
                ci.cancel();
                event = TravelEvent.get(Event.Stage.Post, player);
                Alien.EVENT_BUS.post(event);
            }
        }
    }

    @Inject(method={"method_6091"}, at={@At(value="RETURN")})
    private void onTravelPost(class_243 movementInput, CallbackInfo ci) {
        class_1657 player = (class_1657)class_1657.class.cast(this);
        if (player == MixinPlayerEntity.mc.field_1724) {
            TravelEvent event = TravelEvent.get(Event.Stage.Post, player);
            Alien.EVENT_BUS.post(event);
        }
    }
}

