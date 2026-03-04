/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.sound.SoundInstance
 *  net.minecraft.client.sound.SoundSystem
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.PlaySoundEvent;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1140.class})
public class MixinSoundSystem {
    @Inject(method={"method_4854(Lnet/minecraft/class_1113;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPlay(class_1113 soundInstance, CallbackInfo info) {
        PlaySoundEvent event = PlaySoundEvent.get(soundInstance);
        Alien.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}

