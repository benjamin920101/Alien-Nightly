/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;

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

