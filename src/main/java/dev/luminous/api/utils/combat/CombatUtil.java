/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1511
 *  net.minecraft.class_1657
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2680
 *  net.minecraft.class_2824
 *  net.minecraft.class_742
 */
package dev.luminous.api.utils.combat;

import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2824;
import net.minecraft.class_742;

public class CombatUtil
implements Wrapper {
    public static final Timer breakTimer = new Timer();
    public static boolean terrainIgnore = false;
    public static class_2338 modifyPos;
    public static class_2680 modifyBlockState;

    public static List<class_1657> getEnemies(double range) {
        ArrayList<class_1657> list = new ArrayList<class_1657>();
        for (class_742 player : Alien.THREAD.getPlayers()) {
            if (!CombatUtil.isValid((class_1297)player, range)) continue;
            list.add((class_1657)player);
        }
        return list;
    }

    public static void attackCrystal(class_2338 pos, boolean rotate, boolean eatingPause) {
        CombatUtil.attackCrystal(new class_238(pos), rotate, eatingPause);
    }

    public static void attackCrystal(class_238 box, boolean rotate, boolean eatingPause) {
        for (class_1511 entity : BlockUtil.getEndCrystals(box)) {
            CombatUtil.attackWithDelay((class_1297)entity, rotate, eatingPause);
        }
    }

    public static void attackWithDelay(class_1297 entity, boolean rotate, boolean usingPause) {
        if (!(!breakTimer.passed((long)(AntiCheat.INSTANCE.attackDelay.getValue() * 1000.0)) || usingPause && CombatUtil.mc.field_1724.method_6115())) {
            CombatUtil.attack(entity, rotate);
        }
    }

    public static void attack(class_1297 entity, boolean rotate) {
        if (entity != null) {
            class_243 attackVec = MathUtil.getClosestPointToBox(CombatUtil.mc.field_1724.method_33571(), entity.method_5829());
            if (CombatUtil.mc.field_1724.method_33571().method_1022(attackVec) > AntiCheat.INSTANCE.ieRange.getValue()) {
                return;
            }
            breakTimer.reset();
            if (rotate && AntiCheat.INSTANCE.attackRotate.getValue()) {
                Alien.ROTATION.lookAt(attackVec);
            }
            mc.method_1562().method_52787((class_2596)class_2824.method_34206((class_1297)entity, (boolean)CombatUtil.mc.field_1724.method_5715()));
            CombatUtil.mc.field_1724.method_7350();
            EntityUtil.swingHand(class_1268.field_5808, AntiCheat.INSTANCE.attackSwing.getValue());
            if (rotate && AntiCheat.INSTANCE.attackRotate.getValue()) {
                Alien.ROTATION.snapBack();
            }
        }
    }

    public static boolean isntValid(class_1297 entity, double range) {
        return !CombatUtil.isValid(entity, range);
    }

    public static boolean isValid(class_1297 entity, double range) {
        class_1657 player;
        boolean invalid = entity == null || !entity.method_5805() || entity.equals((Object)CombatUtil.mc.field_1724) || entity instanceof class_1657 && Alien.FRIEND.isFriend(player = (class_1657)entity) || CombatUtil.mc.field_1724.method_19538().method_1022(entity.method_19538()) > range;
        return !invalid;
    }

    public static boolean isValid(class_1297 entity) {
        class_1657 player;
        boolean invalid = entity == null || !entity.method_5805() || entity.equals((Object)CombatUtil.mc.field_1724) || entity instanceof class_1657 && Alien.FRIEND.isFriend(player = (class_1657)entity);
        return !invalid;
    }

    public static class_1657 getClosestEnemy(double distance) {
        class_1657 closest = null;
        for (class_1657 player : CombatUtil.getEnemies(distance)) {
            if (closest == null) {
                closest = player;
                continue;
            }
            if (!(CombatUtil.mc.field_1724.method_5707(player.method_19538()) < CombatUtil.mc.field_1724.method_5858((class_1297)closest))) continue;
            closest = player;
        }
        return closest;
    }

    static {
        modifyBlockState = class_2246.field_10124.method_9564();
    }
}

