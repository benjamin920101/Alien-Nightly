/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.entity.EntityRenderDispatcher
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.RenderEntityEvent;
import net.minecraft.entity.Entity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_898.class})
public class MixinEntityRenderDispatcher {
    @Inject(method={"method_3954"}, at={@At(value="HEAD")}, cancellable=true)
    public <E extends class_1297> void onRender(E entity, double x, double y, double z, float yaw, float tickDelta, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
        RenderEntityEvent event = RenderEntityEvent.get(entity);
        Alien.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}

