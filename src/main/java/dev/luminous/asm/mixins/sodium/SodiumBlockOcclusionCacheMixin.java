/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache
 *  net.minecraft.world.BlockView
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.block.BlockState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package dev.luminous.asm.mixins.sodium;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.luminous.mod.modules.impl.render.Xray;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import net.minecraft.world.BlockView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value={BlockOcclusionCache.class}, remap=false)
public abstract class SodiumBlockOcclusionCacheMixin {
    @ModifyReturnValue(method={"shouldDrawSide"}, at={@At(value="RETURN")})
    private boolean shouldDrawSide(boolean original, class_2680 state, class_1922 view, class_2338 pos, class_2350 facing) {
        return Xray.INSTANCE.isOn() ? Xray.INSTANCE.modifyDrawSide(state, view, pos, facing, original) : original;
    }
}

