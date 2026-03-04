/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.ChunkOcclusionEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_852.class})
public abstract class MixinChunkOcclusionDataBuilder {
    @Inject(method={"method_3682"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMarkClosed(class_2338 pos, CallbackInfo info) {
        ChunkOcclusionEvent event = Alien.EVENT_BUS.post(ChunkOcclusionEvent.get());
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}

