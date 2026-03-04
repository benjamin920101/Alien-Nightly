/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.entity.BlockEntity
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.RenderBlockEntityEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_824.class})
public abstract class MixinBlockEntityRenderDispatcher {
    @Inject(method={"method_3555(Lnet/minecraft/class_2586;FLnet/minecraft/class_4587;Lnet/minecraft/class_4597;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private <E extends class_2586> void onRenderEntity(E blockEntity, float tickDelta, class_4587 matrix, class_4597 vertexConsumerProvider, CallbackInfo info) {
        RenderBlockEntityEvent event = Alien.EVENT_BUS.post(RenderBlockEntityEvent.get(blockEntity));
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}

