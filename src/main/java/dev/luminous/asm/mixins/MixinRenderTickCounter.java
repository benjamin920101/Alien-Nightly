/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.render.RenderTickCounter$Dynamic
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.TimerEvent;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_9779.class_9781.class})
public class MixinRenderTickCounter {
    @Shadow
    private float field_51958;
    @Shadow
    private float field_51959;
    @Shadow
    private long field_51962;
    @Final
    @Shadow
    private float field_51964;

    @Inject(method={"method_60639(J)I"}, at={@At(value="HEAD")}, cancellable=true)
    private void beginRenderTickHook(long timeMillis, CallbackInfoReturnable<Integer> cir) {
        TimerEvent event = TimerEvent.getEvent();
        Alien.EVENT_BUS.post(event);
        if (!event.isCancelled()) {
            float timer = event.isModified() ? event.get() : Alien.TIMER.get();
            if (timer == 1.0f) {
                return;
            }
            this.field_51958 = (float)(timeMillis - this.field_51962) / this.field_51964 * timer;
            this.field_51962 = timeMillis;
            this.field_51959 += this.field_51958;
            int i = (int)this.field_51959;
            this.field_51959 -= (float)i;
            cir.setReturnValue((Object)i);
        }
    }
}

