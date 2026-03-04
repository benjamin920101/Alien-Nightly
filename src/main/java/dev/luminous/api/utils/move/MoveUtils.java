/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.api.utils.move;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class MoveUtils {
    private static final class_310 mc = class_310.method_1551();

    private static float direction(float forward, float strafe) {
        boolean isMovingStraight;
        float direction = MoveUtils.mc.field_1724.method_36454();
        boolean isMovingForward = forward > 0.0f;
        boolean isMovingBack = forward < 0.0f;
        boolean isMovingRight = strafe > 0.0f;
        boolean isMovingLeft = strafe < 0.0f;
        boolean isMovingSideways = isMovingRight || isMovingLeft;
        boolean bl = isMovingStraight = isMovingForward || isMovingBack;
        if (forward != 0.0f || strafe != 0.0f) {
            if (isMovingBack && !isMovingSideways) {
                return direction + 180.0f;
            }
            if (isMovingForward && isMovingLeft) {
                return direction + 45.0f;
            }
            if (isMovingForward && isMovingRight) {
                return direction - 45.0f;
            }
            if (!isMovingStraight && isMovingLeft) {
                return direction + 90.0f;
            }
            if (!isMovingStraight && isMovingRight) {
                return direction - 90.0f;
            }
            if (isMovingBack && isMovingLeft) {
                return direction + 135.0f;
            }
            if (isMovingBack) {
                return direction - 135.0f;
            }
        }
        return direction;
    }

    public static void fixMovement(float yaw) {
        if (MoveUtils.mc.field_1724 != null) {
            float forward = MoveUtils.mc.field_1724.field_3913.field_3905;
            float strafe = MoveUtils.mc.field_1724.field_3913.field_3907;
            int angleUnit = 45;
            float angleTolerance = 22.5f;
            float directionFactor = Math.max(Math.abs(forward), Math.abs(strafe));
            double angleDifference = class_3532.method_15393((float)(MoveUtils.direction(forward, strafe) - yaw));
            double angleDistance = Math.abs(angleDifference);
            forward = 0.0f;
            strafe = 0.0f;
            if (angleDistance <= (double)((float)angleUnit + angleTolerance)) {
                forward += 1.0f;
            } else if (angleDistance >= (double)(180.0f - (float)angleUnit - angleTolerance)) {
                forward -= 1.0f;
            }
            if (angleDifference >= (double)((float)angleUnit - angleTolerance) && angleDifference <= (double)(180.0f - (float)angleUnit + angleTolerance)) {
                strafe -= 1.0f;
            } else if (angleDifference <= (double)((float)(-angleUnit) + angleTolerance) && angleDifference >= (double)(-180.0f + (float)angleUnit - angleTolerance)) {
                strafe += 1.0f;
            }
            MoveUtils.mc.field_1724.field_3913.field_3905 = forward *= directionFactor;
            MoveUtils.mc.field_1724.field_3913.field_3907 = strafe *= directionFactor;
        }
    }

    public static boolean isMoving() {
        if (MoveUtils.mc.field_1724 == null) {
            return false;
        }
        return MoveUtils.mc.field_1724.field_3913.field_3905 != 0.0f || MoveUtils.mc.field_1724.field_3913.field_3907 != 0.0f || MoveUtils.mc.field_1690.field_1894.method_1434() || MoveUtils.mc.field_1690.field_1881.method_1434() || MoveUtils.mc.field_1690.field_1913.method_1434() || MoveUtils.mc.field_1690.field_1849.method_1434() || MoveUtils.mc.field_1690.field_1903.method_1434();
    }
}

