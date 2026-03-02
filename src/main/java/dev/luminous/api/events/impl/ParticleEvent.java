/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_703
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_703;

public class ParticleEvent
extends Event {
    private static final ParticleEvent instance = new ParticleEvent();
    public class_703 particle;

    public static ParticleEvent get(class_703 particle) {
        ParticleEvent.instance.particle = particle;
        instance.setCancelled(false);
        return instance;
    }
}

