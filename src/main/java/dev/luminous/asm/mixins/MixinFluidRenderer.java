/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.BlockRenderView
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.block.BlockState
 *  net.minecraft.fluid.FluidState
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.client.render.block.FluidRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.Xray;
import net.minecraft.world.BlockRenderView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_775.class})
public abstract class MixinFluidRenderer {
    @Inject(method={"method_3347"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRender(class_1920 world, class_2338 pos, class_4588 vertexConsumer, class_2680 blockState, class_3610 fluidState, CallbackInfo info) {
        if (Xray.shouldBlock(fluidState.method_15759())) {
            info.cancel();
        }
    }
}

