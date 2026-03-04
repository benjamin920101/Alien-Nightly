/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.BlockView
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.shape.VoxelShapes
 *  net.minecraft.util.shape.VoxelShape
 *  net.minecraft.block.BlockState
 *  net.minecraft.block.ShapeContext
 *  net.minecraft.block.AbstractBlock
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.AmbientOcclusionEvent;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import net.minecraft.world.BlockView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_4970.class})
public abstract class MixinAbstractBlock {
    @Inject(method={"method_9575"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetAmbientOcclusionLightLevel(class_2680 state, class_1922 world, class_2338 pos, CallbackInfoReturnable<Float> info) {
        AmbientOcclusionEvent event = Alien.EVENT_BUS.post(AmbientOcclusionEvent.get());
        if (event.lightLevel != -1.0f) {
            info.setReturnValue((Object)Float.valueOf(event.lightLevel));
        }
    }

    @Inject(method={"method_9549"}, at={@At(value="HEAD")}, cancellable=true)
    private void onComputeNextCollisionBox(class_2680 state, class_1922 world, class_2338 pos, class_3726 context, CallbackInfoReturnable<class_265> cir) {
        if (SpeedMine.INSTANCE != null && pos.equals((Object)SpeedMine.getBreakPos()) && SpeedMine.INSTANCE.noCollide.getValue() && SpeedMine.ghost) {
            cir.setReturnValue((Object)class_259.method_1073());
        }
    }
}

