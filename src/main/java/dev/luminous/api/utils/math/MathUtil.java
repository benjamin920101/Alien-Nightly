/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.api.utils.math;

import dev.luminous.api.utils.Wrapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;

public class MathUtil
implements Wrapper {
    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }

    public static double clamp(double value, double min, double max) {
        return value < min ? min : Math.min(value, max);
    }

    public static double round(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double square(double input) {
        return input * input;
    }

    public static float random(float min, float max) {
        return (float)(Math.random() * (double)(max - min) + (double)min);
    }

    public static double random(double min, double max) {
        return (float)(Math.random() * (max - min) + min);
    }

    public static float rad(float angle) {
        return (float)((double)angle * Math.PI / 180.0);
    }

    public static double interpolate(double previous, double current, double delta) {
        return previous + (current - previous) * delta;
    }

    public static float interpolate(float previous, float current, float delta) {
        return previous + (current - previous) * delta;
    }

    public static class_2350 getFacingOrder(float yaw, float pitch) {
        class_2350 direction3;
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = class_3532.method_15374((float)f);
        float i = class_3532.method_15362((float)f);
        float j = class_3532.method_15374((float)g);
        float k = class_3532.method_15362((float)g);
        boolean bl = j > 0.0f;
        boolean bl2 = h < 0.0f;
        boolean bl3 = k > 0.0f;
        float l = bl ? j : -j;
        float m = bl2 ? -h : h;
        float n = bl3 ? k : -k;
        float o = l * i;
        float p = n * i;
        class_2350 direction = bl ? class_2350.field_11034 : class_2350.field_11039;
        class_2350 direction2 = bl2 ? class_2350.field_11036 : class_2350.field_11033;
        class_2350 class_23502 = direction3 = bl3 ? class_2350.field_11035 : class_2350.field_11043;
        if (l > n) {
            return m > o ? direction2 : direction;
        }
        return m > p ? direction2 : direction3;
    }

    public static class_2350 getDirectionFromEntityLiving(class_2338 pos, class_1309 entity) {
        if (Math.abs(entity.method_23317() - ((double)pos.method_10263() + 0.5)) < 2.0 && Math.abs(entity.method_23321() - ((double)pos.method_10260() + 0.5)) < 2.0) {
            double d0 = entity.method_23318() + (double)entity.method_18381(entity.method_18376());
            if (d0 - (double)pos.method_10264() > 2.0) {
                return class_2350.field_11036;
            }
            if ((double)pos.method_10264() - d0 > 0.0) {
                return class_2350.field_11033;
            }
        }
        return entity.method_5735().method_10153();
    }

    public static class_243 getRenderPosition(class_1297 entity) {
        return MathUtil.getRenderPosition(entity, mc.method_60646().method_60637(true));
    }

    public static class_243 getRenderPosition(class_1297 entity, float tickDelta) {
        return new class_243(entity.field_6014 + (entity.method_23317() - entity.field_6014) * (double)tickDelta, entity.field_6036 + (entity.method_23318() - entity.field_6036) * (double)tickDelta, entity.field_5969 + (entity.method_23321() - entity.field_5969) * (double)tickDelta);
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dX = x2 - x1;
        double dY = y2 - y1;
        double dZ = z2 - z1;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static class_243 getPointToBoxFromBottom(class_243 eye, class_243 bottom, double range, double boxSize, double step) {
        class_243 target = null;
        double halfWidth = boxSize / 2.0;
        double minOffset = Double.MAX_VALUE;
        boolean xPlus = bottom.field_1352 < eye.field_1352;
        boolean zPlus = bottom.field_1350 < eye.field_1350;
        double rangeSq = range * range;
        for (double yOffset = 0.0; yOffset <= boxSize; yOffset += step) {
            for (double xOffset = 0.0; xOffset <= halfWidth; xOffset += step) {
                for (double zOffset = 0.0; zOffset <= halfWidth; zOffset += step) {
                    double y = bottom.field_1351 + yOffset;
                    if (yOffset != 0.0 && y > eye.field_1351) {
                        return target;
                    }
                    double x = bottom.field_1352 + (xPlus ? xOffset : -xOffset);
                    double z = bottom.field_1350 + (zPlus ? zOffset : -zOffset);
                    double dxToEye = x - eye.field_1352;
                    double dyToEye = y - eye.field_1351;
                    double dzToEye = z - eye.field_1350;
                    double distSq = dxToEye * dxToEye + dyToEye * dyToEye + dzToEye * dzToEye;
                    double offsets = xOffset + yOffset + zOffset;
                    if (!(distSq <= rangeSq) || !(offsets < minOffset)) continue;
                    minOffset = offsets;
                    target = new class_243(x, y, z);
                }
            }
        }
        return target;
    }

    public static class_243 getClosestPointToBox(class_243 pos, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        double closestX = Math.max(minX, Math.min(pos.field_1352, maxX));
        double closestY = Math.max(minY, Math.min(pos.field_1351, maxY));
        double closestZ = Math.max(minZ, Math.min(pos.field_1350, maxZ));
        return new class_243(closestX, closestY, closestZ);
    }

    public static class_243 getClosestPointToBox(class_243 eyePos, class_238 boundingBox) {
        return MathUtil.getClosestPointToBox(eyePos, boundingBox.field_1323, boundingBox.field_1322, boundingBox.field_1321, boundingBox.field_1320, boundingBox.field_1325, boundingBox.field_1324);
    }

    public static class_243 getClosestPoint(class_1297 entity) {
        return MathUtil.getClosestPointToBox(MathUtil.mc.field_1724.method_33571(), entity.method_5829());
    }
}

