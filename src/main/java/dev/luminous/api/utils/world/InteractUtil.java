/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.projectile.ProjectileUtil
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.hit.HitResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 *  net.minecraft.util.hit.EntityHitResult
 */
package dev.luminous.api.utils.world;

import dev.luminous.api.utils.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.EntityHitResult;

public class InteractUtil
implements Wrapper {
    public static class_239 getRtxTarget(float yaw, float pitch, double x, double y, double z) {
        class_238 box;
        class_239 result = InteractUtil.rayTrace(5.0, yaw, pitch, x, y, z);
        class_243 vec3d = new class_243(x, y, z);
        double distancePow2 = 25.0;
        if (result != null) {
            distancePow2 = result.method_17784().method_1025(vec3d);
        }
        class_243 vec3d2 = InteractUtil.getRotationVector(pitch, yaw);
        class_243 vec3d3 = vec3d.method_1031(vec3d2.field_1352 * 5.0, vec3d2.field_1351 * 5.0, vec3d2.field_1350 * 5.0);
        class_3966 entityHitResult = class_1675.method_18075((class_1297)InteractUtil.mc.field_1724, (class_243)vec3d, (class_243)vec3d3, (class_238)(box = new class_238(x - 0.3, y, z - 0.3, x + 0.3, y + 1.8, z + 0.3).method_18804(vec3d2.method_1021(5.0)).method_1009(1.0, 1.0, 1.0)), entity -> !entity.method_7325() && entity.method_5863(), (double)distancePow2);
        if (entityHitResult != null) {
            class_1297 entity2 = entityHitResult.method_17782();
            class_243 vec3d4 = entityHitResult.method_17784();
            double g = vec3d.method_1025(vec3d4);
            if ((g < distancePow2 || result == null) && entity2 instanceof class_1309) {
                return entityHitResult;
            }
        }
        return result;
    }

    public static class_239 rayTrace(double dst, float yaw, float pitch, double x, double y, double z) {
        class_243 vec3d = new class_243(x, y, z);
        class_243 vec3d2 = InteractUtil.getRotationVector(pitch, yaw);
        class_243 vec3d3 = vec3d.method_1031(vec3d2.field_1352 * dst, vec3d2.field_1351 * dst, vec3d2.field_1350 * dst);
        return InteractUtil.mc.field_1687.method_17742(new class_3959(vec3d, vec3d3, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)InteractUtil.mc.field_1724));
    }

    private static class_243 getRotationVector(float yaw, float pitch) {
        return new class_243((double)(class_3532.method_15374((float)(-pitch * ((float)Math.PI / 180))) * class_3532.method_15362((float)(yaw * ((float)Math.PI / 180)))), (double)(-class_3532.method_15374((float)(yaw * ((float)Math.PI / 180)))), (double)(class_3532.method_15362((float)(-pitch * ((float)Math.PI / 180))) * class_3532.method_15362((float)(yaw * ((float)Math.PI / 180)))));
    }
}

