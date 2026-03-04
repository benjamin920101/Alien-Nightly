/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.chunk.light.LightingProvider
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.NoRender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_3568.class})
public class MixinLightingProvider {
    @Inject(at={@At(value="HEAD")}, method={"method_15513"}, cancellable=true)
    public void checkBlock(class_2338 pos, CallbackInfo ci) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.lightsUpdate.getValue()) {
            ci.cancel();
        }
    }

    @Inject(at={@At(value="RETURN")}, method={"method_15516"}, cancellable=true)
    public void doLightUpdates(CallbackInfoReturnable<Integer> cir) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.lightsUpdate.getValue()) {
            cir.setReturnValue((Object)0);
        }
    }
}

