/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package dev.luminous.api.utils.math;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.DamageUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ExplosionUtil
implements Wrapper {
    public static float anchorDamage(class_2338 pos, class_1309 target, class_1309 predict) {
        return DamageUtils.anchorDamage(target, predict, pos.method_46558());
    }

    public static float calculateDamage(class_243 pos, class_1309 entity, class_1309 predict, float power) {
        return DamageUtils.explosionDamage(entity, predict, pos, power * 2.0f);
    }
}

