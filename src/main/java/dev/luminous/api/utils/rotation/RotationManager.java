/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.api.utils.rotation;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.GameLeftEvent;
import dev.luminous.api.events.impl.KeyboardInputEvent;
import dev.luminous.api.events.impl.SendMovementPacketsEvent;
import dev.luminous.api.utils.move.MoveUtils;
import dev.luminous.api.utils.rotation.Rotation;
import dev.luminous.api.utils.rotation.RotationUtils;
import java.util.function.Function;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class RotationManager {
    private static final class_310 mc = class_310.method_1551();
    private static final Rotation offset = new Rotation(0.0f, 0.0f);
    public static Rotation rotations;
    public static Rotation lastRotations;
    public static Rotation targetRotations;
    public static Rotation animationRotation;
    public static Rotation lastAnimationRotation;
    private static boolean active;
    private static boolean smoothed;
    private static double rotationSpeed;
    private static Function<Rotation, Boolean> raycast;
    private static float randomAngle;
    private static boolean subscribed;

    public static void init() {
        if (!subscribed) {
            Alien.EVENT_BUS.subscribe(new RotationManager());
            subscribed = true;
        }
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        RotationManager.active = active;
    }

    public static boolean isSmoothed() {
        return smoothed;
    }

    public static void setRotations(Rotation rotations, double rotationSpeed) {
        RotationManager.setRotations(rotations, rotationSpeed, null);
    }

    public static void setRotations(Rotation rotations, double rotationSpeed, Function<Rotation, Boolean> raycast) {
        targetRotations = rotations;
        RotationManager.rotationSpeed = rotationSpeed;
        RotationManager.raycast = raycast;
        active = true;
        smoothed = false;
        RotationManager.smooth();
    }

    public static void smooth() {
        if (RotationManager.mc.field_1724 != null) {
            if (!smoothed) {
                float targetYaw = targetRotations.getYaw();
                float targetPitch = targetRotations.getPitch();
                if (raycast != null && (Math.abs(targetYaw - rotations.getYaw()) > 5.0f || Math.abs(targetPitch - rotations.getPitch()) > 5.0f)) {
                    Rotation trueTargetRotations = new Rotation(targetRotations.getYaw(), targetRotations.getPitch());
                    double speed = Math.random() * Math.random() * Math.random() * 20.0;
                    offset.setYaw((float)((double)offset.getYaw() + (double)(-class_3532.method_15362((float)((float)Math.toRadians(randomAngle += (float)((20.0 + (double)((float)(Math.random() - 0.5)) * Math.random() * Math.random() * Math.random() * 360.0) * (double)(RotationManager.mc.field_1724.field_6012 / 10 % 2 == 0 ? -1 : 1)))))) * speed));
                    offset.setPitch((float)((double)offset.getPitch() + (double)class_3532.method_15374((float)((float)Math.toRadians(randomAngle))) * speed));
                    if (!raycast.apply(new Rotation(targetYaw += offset.getYaw(), targetPitch += offset.getPitch())).booleanValue()) {
                        randomAngle = (float)Math.toDegrees(Math.atan2(trueTargetRotations.getYaw() - targetYaw, targetPitch - trueTargetRotations.getPitch())) - 180.0f;
                        targetYaw -= offset.getYaw();
                        targetPitch -= offset.getPitch();
                        offset.setYaw((float)((double)offset.getYaw() + (double)(-class_3532.method_15362((float)((float)Math.toRadians(randomAngle)))) * speed));
                        offset.setPitch((float)((double)offset.getPitch() + (double)class_3532.method_15374((float)((float)Math.toRadians(randomAngle))) * speed));
                        targetYaw += offset.getYaw();
                        targetPitch += offset.getPitch();
                    }
                    if (!raycast.apply(new Rotation(targetYaw, targetPitch)).booleanValue()) {
                        offset.setYaw(0.0f);
                        offset.setPitch(0.0f);
                        targetYaw = (float)((double)targetRotations.getYaw() + Math.random() * 2.0);
                        targetPitch = (float)((double)targetRotations.getPitch() + Math.random() * 2.0);
                    }
                }
                rotations = RotationUtils.smooth(new Rotation(targetYaw, targetPitch), rotationSpeed + Math.random());
            }
            smoothed = true;
        }
    }

    public static Rotation getRotation() {
        if (RotationManager.mc.field_1724 == null) {
            return new Rotation(0.0f, 0.0f);
        }
        return active && rotations != null ? rotations : new Rotation(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455());
    }

    @EventListener
    public void onLogout(GameLeftEvent e) {
        rotations = null;
        lastRotations = null;
        targetRotations = null;
        animationRotation = null;
        lastAnimationRotation = null;
        active = false;
        smoothed = false;
    }

    @EventListener(priority=4)
    public void onClientTick(ClientTickEvent e) {
        if (e.isPre() && RotationManager.mc.field_1724 != null) {
            if (!active || rotations == null || lastRotations == null || targetRotations == null) {
                lastRotations = targetRotations = new Rotation(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455());
                rotations = targetRotations;
            }
            if (active) {
                RotationManager.smooth();
            }
        }
    }

    @EventListener(priority=4)
    public void onSendMovementPackets(SendMovementPacketsEvent e) {
        if (RotationManager.mc.field_1724 != null) {
            if (active && rotations != null) {
                float yaw = rotations.getYaw();
                float pitch = rotations.getPitch();
                if (!Float.isNaN(yaw) && !Float.isNaN(pitch)) {
                    e.setYaw(yaw);
                    e.setPitch(pitch);
                }
                if (Math.abs((rotations.getYaw() - RotationManager.mc.field_1724.method_36454()) % 360.0f) < 1.0f && Math.abs(rotations.getPitch() - RotationManager.mc.field_1724.method_36455()) < 1.0f) {
                    active = false;
                    this.correctDisabledRotations();
                }
                lastRotations = rotations;
            } else {
                lastRotations = new Rotation(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455());
            }
            lastAnimationRotation = animationRotation;
            animationRotation = new Rotation(e.getYaw(), e.getPitch());
            targetRotations = new Rotation(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455());
            smoothed = false;
        }
    }

    @EventListener(priority=100)
    public void onKeyboardInput(KeyboardInputEvent event) {
        if (RotationManager.mc.field_1724 != null && active && rotations != null) {
            MoveUtils.fixMovement(rotations.getYaw());
        }
    }

    private void correctDisabledRotations() {
        Rotation rotations = new Rotation(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455());
        Rotation fixedRotations = RotationUtils.resetRotation(RotationUtils.applySensitivityPatch(rotations, lastRotations));
        RotationManager.mc.field_1724.method_36456(fixedRotations.getYaw());
        RotationManager.mc.field_1724.method_36457(fixedRotations.getPitch());
    }

    static {
        lastRotations = new Rotation(0.0f, 0.0f);
    }
}

