/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_2338
 *  net.minecraft.class_243
 */
package dev.luminous.api.utils.math;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.DamageUtils;
import net.minecraft.class_1309;
import net.minecraft.class_2338;
import net.minecraft.class_243;

public class ExplosionUtil
implements Wrapper {
    public static float anchorDamage(class_2338 pos, class_1309 target, class_1309 predict) {
        return DamageUtils.anchorDamage(target, predict, pos.method_46558());
    }

    public static float calculateDamage(class_243 pos, class_1309 entity, class_1309 predict, float power) {
        return DamageUtils.explosionDamage(entity, predict, pos, power * 2.0f);
    }
}

