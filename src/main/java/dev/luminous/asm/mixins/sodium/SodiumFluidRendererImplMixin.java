/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers
 *  net.caffeinemc.mods.sodium.client.render.chunk.translucent_sorting.TranslucentGeometryCollector
 *  net.caffeinemc.mods.sodium.client.world.LevelSlice
 *  net.caffeinemc.mods.sodium.fabric.render.FluidRendererImpl
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.block.BlockState
 *  net.minecraft.fluid.FluidState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins.sodium;

import dev.luminous.mod.modules.impl.render.Xray;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.caffeinemc.mods.sodium.client.render.chunk.translucent_sorting.TranslucentGeometryCollector;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.caffeinemc.mods.sodium.fabric.render.FluidRendererImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={FluidRendererImpl.class}, remap=false)
public abstract class SodiumFluidRendererImplMixin {
    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRender(LevelSlice level, class_2680 blockState, class_3610 fluidState, class_2338 blockPos, class_2338 offset, TranslucentGeometryCollector collector, ChunkBuildBuffers buffers, CallbackInfo info) {
        if (Xray.shouldBlock(fluidState.method_15759())) {
            info.cancel();
        }
    }
}

