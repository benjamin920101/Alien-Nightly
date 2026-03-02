/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer
 *  net.minecraft.class_1087
 *  net.minecraft.class_2338
 *  net.minecraft.class_2680
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins.sodium;

import dev.luminous.mod.modules.impl.render.Xray;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.class_1087;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={BlockRenderer.class})
public abstract class SodiumBlockRendererMixin {
    @Inject(method={"renderModel"}, at={@At(value="INVOKE", target="Lnet/caffeinemc/mods/sodium/client/model/color/ColorProviderRegistry;getColorProvider(Lnet/minecraft/class_2248;)Lnet/caffeinemc/mods/sodium/client/model/color/ColorProvider;", shift=At.Shift.AFTER)}, cancellable=true)
    private void onRenderModel(class_1087 model, class_2680 state, class_2338 pos, class_2338 origin, CallbackInfo info) {
        if (Xray.shouldBlock(state)) {
            info.cancel();
        }
    }
}

