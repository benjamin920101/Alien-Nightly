/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.model.BakedModel
 *  net.minecraft.world.BlockRenderView
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.block.BlockState
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.VertexConsumer
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.client.render.block.BlockModelRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.Xray;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.world.BlockRenderView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.render.block.BlockModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_778.class})
public abstract class MixinBlockModelRenderer {
    @Inject(method={"method_3361", "method_3373"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderSmooth(class_1920 world, class_1087 model, class_2680 state, class_2338 pos, class_4587 matrices, class_4588 vertexConsumer, boolean cull, class_5819 random, long seed, int overlay, CallbackInfo info) {
        if (Xray.shouldBlock(state)) {
            info.cancel();
        }
    }
}

