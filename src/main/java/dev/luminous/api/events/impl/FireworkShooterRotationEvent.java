/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public class FireworkShooterRotationEvent
extends Event {
    private static final FireworkShooterRotationEvent instance = new FireworkShooterRotationEvent();
    public class_1309 shooter;
    public float pitch;
    public float yaw;

    private FireworkShooterRotationEvent() {
    }

    public static FireworkShooterRotationEvent get(class_1309 shooter, float yaw, float pitch) {
        FireworkShooterRotationEvent.instance.shooter = shooter;
        FireworkShooterRotationEvent.instance.yaw = yaw;
        FireworkShooterRotationEvent.instance.pitch = pitch;
        instance.setCancelled(false);
        return instance;
    }

    public final class_243 getRotationVector() {
        float f = this.pitch * ((float)Math.PI / 180);
        float g = -this.yaw * ((float)Math.PI / 180);
        float h = class_3532.method_15362((float)g);
        float i = class_3532.method_15374((float)g);
        float j = class_3532.method_15362((float)f);
        float k = class_3532.method_15374((float)f);
        return new class_243((double)(i * j), (double)(-k), (double)(h * j));
    }
}

