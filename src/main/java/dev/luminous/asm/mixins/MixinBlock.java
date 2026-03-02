/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.class_1922
 *  net.minecraft.class_1935
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2680
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.luminous.mod.modules.impl.movement.NoSlow;
import dev.luminous.mod.modules.impl.render.Xray;
import net.minecraft.class_1922;
import net.minecraft.class_1935;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_2248.class})
public abstract class MixinBlock
implements class_1935 {
    @Inject(at={@At(value="HEAD")}, method={"method_23349()F"}, cancellable=true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        if (NoSlow.INSTANCE.soulSand() && cir.getReturnValueF() < 1.0f) {
            cir.setReturnValue((Object)Float.valueOf(1.0f));
        }
    }

    @ModifyReturnValue(method={"method_9607"}, at={@At(value="RETURN")})
    private static boolean onShouldDrawSide(boolean original, class_2680 state, class_1922 world, class_2338 pos, class_2350 side, class_2338 blockPos) {
        Xray xray = Xray.INSTANCE;
        return xray.isOn() ? xray.modifyDrawSide(state, world, pos, side, original) : original;
    }
}

