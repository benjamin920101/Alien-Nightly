/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_243
 */
package dev.luminous.api.events.impl;

import net.minecraft.class_243;

public class RotationEvent {
    private static final RotationEvent instance = new RotationEvent();
    public float priority;
    private class_243 target;
    private float yaw;
    private float pitch;
    private boolean rotation;
    private float speed;

    private RotationEvent() {
    }

    public static RotationEvent get() {
        RotationEvent.instance.priority = 0.0f;
        RotationEvent.instance.target = null;
        RotationEvent.instance.yaw = 0.0f;
        RotationEvent.instance.pitch = 0.0f;
        RotationEvent.instance.rotation = false;
        RotationEvent.instance.speed = 0.0f;
        return instance;
    }

    public class_243 getTarget() {
        return this.target;
    }

    public float getSpeed() {
        return this.speed;
    }

    public boolean getRotation() {
        return this.rotation;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setTarget(class_243 target, float speed, float priority) {
        if (priority >= this.priority) {
            this.rotation = false;
            this.priority = priority;
            this.target = target;
            this.speed = speed;
        }
    }

    public void setRotation(float yaw, float pitch, float speed, float priority) {
        if (priority >= this.priority) {
            this.rotation = true;
            this.priority = priority;
            this.yaw = yaw;
            this.pitch = pitch;
            this.speed = speed;
        }
    }
}

