/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.hit.HitResult
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 */
package dev.luminous.api.utils.rotation;

import dev.luminous.Alien;
import dev.luminous.api.utils.rotation.Rotation;
import dev.luminous.api.utils.rotation.RotationManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;

public class RotationUtils {
    private static final class_310 mc = class_310.method_1551();

    public static Rotation calculate(class_243 from, class_243 to) {
        class_243 diff = to.method_1020(from);
        double distance = Math.hypot(diff.field_1352, diff.field_1350);
        float yaw = (float)(Math.atan2(diff.field_1350, diff.field_1352) * 57.2957763671875) - 90.0f;
        float pitch = (float)(-(Math.atan2(diff.field_1351, distance) * 57.2957763671875));
        return new Rotation(yaw, pitch);
    }

    public static Rotation calculate(class_1297 entity) {
        if (RotationUtils.mc.field_1724 == null) {
            return new Rotation(0.0f, 0.0f);
        }
        double eyeHeight = RotationUtils.mc.field_1724.method_18381(RotationUtils.mc.field_1724.method_18376());
        double clamped = Math.max(0.0, Math.min(RotationUtils.mc.field_1724.method_23318() - entity.method_23318() + eyeHeight, (entity.method_5829().field_1325 - entity.method_5829().field_1322) * 0.9));
        return RotationUtils.calculate(entity.method_19538().method_1031(0.0, clamped, 0.0));
    }

    public static Rotation calculate(class_1297 entity, boolean adaptive, double range) {
        Rotation normalRotations = RotationUtils.calculate(entity);
        class_239 result = RotationUtils.rayCast(normalRotations, range);
        if (adaptive && (result == null || result.method_17783() != class_239.class_240.field_1331)) {
            class_238 bb = entity.method_5829();
            double minX = bb.field_1323;
            double maxX = bb.field_1320;
            double minY = bb.field_1322;
            double maxY = bb.field_1325;
            double minZ = bb.field_1321;
            double maxZ = bb.field_1324;
            class_243 basePos = entity.method_19538();
            for (double yPercent = 1.0; yPercent >= 0.0; yPercent -= 0.25 + Math.random() * 0.1) {
                for (double xPercent = 1.0; xPercent >= -0.5; xPercent -= 0.5) {
                    for (double zPercent = 1.0; zPercent >= -0.5; zPercent -= 0.5) {
                        double offsetX = (maxX - minX) * xPercent;
                        double offsetY = (maxY - minY) * yPercent;
                        double offsetZ = (maxZ - minZ) * zPercent;
                        class_243 targetPoint = basePos.method_1031(offsetX, offsetY, offsetZ);
                        Rotation adaptiveRotations = RotationUtils.calculate(targetPoint);
                        class_239 rayCastResult = RotationUtils.rayCast(adaptiveRotations, range);
                        if (rayCastResult == null || rayCastResult.method_17783() != class_239.class_240.field_1331) continue;
                        return adaptiveRotations;
                    }
                }
            }
            return normalRotations;
        }
        return normalRotations;
    }

    private static class_239 rayCast(Rotation rotation, double range) {
        class_243 eyePos = RotationUtils.mc.field_1724.method_33571();
        class_243 direction = RotationUtils.getRotationVector(rotation.getPitch(), rotation.getYaw());
        class_243 endPos = eyePos.method_1019(direction.method_1021(range));
        return RotationUtils.mc.field_1687.method_17742(new class_3959(eyePos, endPos, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)RotationUtils.mc.field_1724));
    }

    private static class_243 getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = class_3532.method_15362((float)g);
        return new class_243((double)(h * class_3532.method_15362((float)f)), (double)class_3532.method_15374((float)f), (double)(class_3532.method_15374((float)g) * class_3532.method_15362((float)f)));
    }

    public static Rotation calculate(class_2338 to) {
        return RotationUtils.calculate(RotationUtils.mc.field_1724.method_33571(), new class_243((double)to.method_10263(), (double)to.method_10264(), (double)to.method_10260()).method_1031(0.5, 0.5, 0.5));
    }

    public static Rotation calculate(class_243 to) {
        return RotationUtils.calculate(RotationUtils.mc.field_1724.method_33571(), to);
    }

    public static Rotation calculate(class_243 position, class_2350 enumFacing) {
        double x = position.field_1352 + 0.5;
        double y = position.field_1351 + 0.5;
        double z = position.field_1350 + 0.5;
        return RotationUtils.calculate(new class_243(x += (double)enumFacing.method_10163().method_10263() * 0.5, y += (double)enumFacing.method_10163().method_10264() * 0.5, z += (double)enumFacing.method_10163().method_10260() * 0.5));
    }

    public static Rotation calculate(class_2338 position, class_2350 enumFacing) {
        double x = (double)position.method_10263() + 0.5;
        double y = (double)position.method_10264() + 0.5;
        double z = (double)position.method_10260() + 0.5;
        return RotationUtils.calculate(new class_243(x += (double)enumFacing.method_10163().method_10263() * 0.5, y += (double)enumFacing.method_10163().method_10264() * 0.5, z += (double)enumFacing.method_10163().method_10260() * 0.5));
    }

    public static Rotation applySensitivityPatch(Rotation rotation) {
        Rotation previousRotation = RotationManager.lastRotations;
        if (previousRotation == null) {
            previousRotation = new Rotation(RotationUtils.mc.field_1724.field_5982, RotationUtils.mc.field_1724.field_6004);
        }
        return RotationUtils.applySensitivityPatch(rotation, previousRotation);
    }

    public static Rotation applySensitivityPatch(Rotation rotation, Rotation previousRotation) {
        float mouseSensitivity = (float)((Double)RotationUtils.mc.field_1690.method_42495().method_41753() * (double)0.6f + (double)0.2f);
        double multiplier = (double)(mouseSensitivity * mouseSensitivity * mouseSensitivity * 8.0f) * 0.15;
        float yaw = previousRotation.getYaw() + (float)((double)Math.round((double)(rotation.getYaw() - previousRotation.getYaw()) / multiplier) * multiplier);
        float pitch = previousRotation.getPitch() + (float)((double)Math.round((double)(rotation.getPitch() - previousRotation.getPitch()) / multiplier) * multiplier);
        return new Rotation(yaw, class_3532.method_15363((float)pitch, (float)-90.0f, (float)90.0f));
    }

    public static Rotation relateToPlayerRotation(Rotation rotation) {
        Rotation previousRotation = new Rotation(RotationUtils.mc.field_1724.field_5982, RotationUtils.mc.field_1724.field_6004);
        float yaw = previousRotation.getYaw() + class_3532.method_15393((float)(rotation.getYaw() - previousRotation.getYaw()));
        float pitch = class_3532.method_15363((float)rotation.getPitch(), (float)-90.0f, (float)90.0f);
        return new Rotation(yaw, pitch);
    }

    public static Rotation resetRotation(Rotation rotation) {
        if (rotation == null) {
            return null;
        }
        float yaw = rotation.getYaw() + class_3532.method_15393((float)(RotationUtils.mc.field_1724.method_36454() - rotation.getYaw()));
        float pitch = RotationUtils.mc.field_1724.method_36455();
        return new Rotation(yaw, pitch);
    }

    public static Rotation move(Rotation targetRotation, double speed) {
        return RotationUtils.move(RotationManager.lastRotations, targetRotation, speed);
    }

    public static Rotation move(Rotation lastRotation, Rotation targetRotation, double speed) {
        if (speed != 0.0) {
            double deltaYaw = class_3532.method_15393((float)(targetRotation.getYaw() - lastRotation.getYaw()));
            double deltaPitch = targetRotation.getPitch() - lastRotation.getPitch();
            double distance = Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
            double distributionYaw = Math.abs(deltaYaw / distance);
            double distributionPitch = Math.abs(deltaPitch / distance);
            double maxYaw = speed * distributionYaw;
            double maxPitch = speed * distributionPitch;
            float moveYaw = (float)Math.max(Math.min(deltaYaw, maxYaw), -maxYaw);
            float movePitch = (float)Math.max(Math.min(deltaPitch, maxPitch), -maxPitch);
            return new Rotation(moveYaw, movePitch);
        }
        return new Rotation(0.0f, 0.0f);
    }

    public static Rotation smooth(Rotation targetRotation, double speed) {
        return RotationUtils.smooth(RotationManager.lastRotations, targetRotation, speed);
    }

    public static Rotation smooth(Rotation lastRotation, Rotation targetRotation, double speed) {
        float yaw = targetRotation.getYaw();
        float pitch = targetRotation.getPitch();
        float lastYaw = lastRotation.getYaw();
        float lastPitch = lastRotation.getPitch();
        if (speed != 0.0) {
            Rotation move = RotationUtils.move(targetRotation, speed);
            yaw = lastYaw + move.getYaw();
            pitch = lastPitch + move.getPitch();
            for (int i = 1; i <= (int)((float)Alien.FPS.getFps() / 20.0f + (float)Math.random() * 10.0f); ++i) {
                if ((double)(Math.abs(move.getYaw()) + Math.abs(move.getPitch())) > 1.0E-4) {
                    yaw = (float)((double)yaw + (Math.random() - 0.5) / 1000.0);
                    pitch = (float)((double)pitch - Math.random() / 200.0);
                }
                Rotation rotations = new Rotation(yaw, pitch);
                Rotation fixedRotations = RotationUtils.applySensitivityPatch(rotations, lastRotation);
                yaw = RotationUtils.shortestYaw(lastYaw, fixedRotations.getYaw());
                pitch = Math.max(-90.0f, Math.min(90.0f, fixedRotations.getPitch()));
            }
        }
        return new Rotation(yaw, pitch);
    }

    public static double getDistance(class_1297 entity) {
        class_243 eyes = RotationUtils.mc.field_1724.method_33571();
        class_238 aabb = entity.method_5829();
        double x = class_3532.method_15350((double)eyes.field_1352, (double)aabb.field_1323, (double)aabb.field_1320);
        double y = class_3532.method_15350((double)eyes.field_1351, (double)aabb.field_1322, (double)aabb.field_1325);
        double z = class_3532.method_15350((double)eyes.field_1350, (double)aabb.field_1321, (double)aabb.field_1324);
        return Math.sqrt(eyes.method_1028(x, y, z));
    }

    public static double getDistance(class_1657 player, class_1297 entity) {
        class_243 eyes = player.method_33571();
        class_238 aabb = entity.method_5829();
        double x = class_3532.method_15350((double)eyes.field_1352, (double)aabb.field_1323, (double)aabb.field_1320);
        double y = class_3532.method_15350((double)eyes.field_1351, (double)aabb.field_1322, (double)aabb.field_1325);
        double z = class_3532.method_15350((double)eyes.field_1350, (double)aabb.field_1321, (double)aabb.field_1324);
        return Math.sqrt(eyes.method_1028(x, y, z));
    }

    public static float shortestYaw(float from, float to) {
        return from + class_3532.method_15393((float)(to - from));
    }

    public static float getActualYaw() {
        if (RotationManager.getRotation() != null) {
            return RotationManager.getRotation().getYaw();
        }
        return RotationUtils.mc.field_1724.method_36454();
    }

    public static float getActualPitch() {
        if (RotationManager.getRotation() != null) {
            return RotationManager.getRotation().getPitch();
        }
        return RotationUtils.mc.field_1724.method_36455();
    }
}

