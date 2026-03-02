/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1293
 *  net.minecraft.class_1294
 */
package dev.luminous.api.utils.player;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.asm.accessors.IVec3d;
import dev.luminous.mod.modules.impl.movement.HoleSnap;
import net.minecraft.class_1293;
import net.minecraft.class_1294;

public class MovementUtil
implements Wrapper {
    public static boolean isMoving() {
        return (double)MovementUtil.mc.field_1724.field_3913.field_3905 != 0.0 || (double)MovementUtil.mc.field_1724.field_3913.field_3907 != 0.0 || HoleSnap.INSTANCE.isOn();
    }

    public static boolean isStatic() {
        return MovementUtil.mc.field_1724.method_18798().method_10216() == 0.0 && MovementUtil.mc.field_1724.method_24828() && MovementUtil.mc.field_1724.method_18798().method_10215() == 0.0;
    }

    public static boolean isJumping() {
        return MovementUtil.mc.field_1724.field_3913.field_3904;
    }

    public static double getDistance2D() {
        double xDist = MovementUtil.mc.field_1724.method_23317() - MovementUtil.mc.field_1724.field_6014;
        double zDist = MovementUtil.mc.field_1724.method_23321() - MovementUtil.mc.field_1724.field_5969;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public static double getJumpSpeed() {
        double defaultSpeed = 0.0;
        if (MovementUtil.mc.field_1724.method_6059(class_1294.field_5913)) {
            int amplifier = ((class_1293)MovementUtil.mc.field_1724.method_6088().get(class_1294.field_5913)).method_5578();
            defaultSpeed += (double)(amplifier + 1) * 0.1;
        }
        return defaultSpeed;
    }

    public static double[] directionSpeed(double speed) {
        float forward = MovementUtil.mc.field_1724.field_3913.field_3905;
        float side = MovementUtil.mc.field_1724.field_3913.field_3907;
        return MovementUtil.directionSpeed(speed, forward, side);
    }

    private static double[] directionSpeed(double speed, float forward, float side) {
        float yaw = MovementUtil.mc.field_1724.field_5982 + (MovementUtil.mc.field_1724.method_36454() - MovementUtil.mc.field_1724.field_5982) * mc.method_60646().method_60637(true);
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += forward > 0.0f ? -45.0f : 45.0f;
            } else if (side < 0.0f) {
                yaw += forward > 0.0f ? 45.0f : -45.0f;
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static double getMotionX() {
        return MovementUtil.mc.field_1724.method_18798().field_1352;
    }

    public static void setMotionX(double x) {
        ((IVec3d)MovementUtil.mc.field_1724.method_18798()).setX(x);
    }

    public static double getMotionY() {
        return MovementUtil.mc.field_1724.method_18798().field_1351;
    }

    public static void setMotionY(double y) {
        ((IVec3d)MovementUtil.mc.field_1724.method_18798()).setY(y);
    }

    public static double getMotionZ() {
        return MovementUtil.mc.field_1724.method_18798().field_1350;
    }

    public static void setMotionZ(double z) {
        ((IVec3d)MovementUtil.mc.field_1724.method_18798()).setZ(z);
    }

    public static double getSpeed(boolean slowness) {
        double defaultSpeed = 0.2873;
        return MovementUtil.getSpeed(slowness, defaultSpeed);
    }

    public static double getSpeed(boolean slowness, double defaultSpeed) {
        int amplifier;
        if (MovementUtil.mc.field_1724.method_6059(class_1294.field_5904)) {
            amplifier = ((class_1293)MovementUtil.mc.field_1724.method_6088().get(class_1294.field_5904)).method_5578();
            defaultSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (slowness && MovementUtil.mc.field_1724.method_6059(class_1294.field_5909)) {
            amplifier = ((class_1293)MovementUtil.mc.field_1724.method_6088().get(class_1294.field_5909)).method_5578();
            defaultSpeed /= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        if (MovementUtil.mc.field_1724.method_5715()) {
            defaultSpeed /= 5.0;
        }
        return defaultSpeed;
    }

    public static boolean isInputtingMovement() {
        return MovementUtil.mc.field_1690.field_1894.method_1434() || MovementUtil.mc.field_1690.field_1881.method_1434() || MovementUtil.mc.field_1690.field_1913.method_1434() || MovementUtil.mc.field_1690.field_1849.method_1434();
    }
}

