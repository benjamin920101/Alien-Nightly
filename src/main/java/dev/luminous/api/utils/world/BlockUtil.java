/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1303
 *  net.minecraft.class_1511
 *  net.minecraft.class_1531
 *  net.minecraft.class_1542
 *  net.minecraft.class_1667
 *  net.minecraft.class_1683
 *  net.minecraft.class_1802
 *  net.minecraft.class_1922
 *  net.minecraft.class_1923
 *  net.minecraft.class_1937
 *  net.minecraft.class_1941
 *  net.minecraft.class_2199
 *  net.minecraft.class_2231
 *  net.minecraft.class_2237
 *  net.minecraft.class_2244
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2269
 *  net.minecraft.class_2304
 *  net.minecraft.class_2323
 *  net.minecraft.class_2338
 *  net.minecraft.class_2349
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_2406
 *  net.minecraft.class_2428
 *  net.minecraft.class_243
 *  net.minecraft.class_2533
 *  net.minecraft.class_2586
 *  net.minecraft.class_2596
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_2818
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 *  net.minecraft.class_2885
 *  net.minecraft.class_3711
 *  net.minecraft.class_3713
 *  net.minecraft.class_3718
 *  net.minecraft.class_3965
 *  net.minecraft.class_5329
 *  net.minecraft.class_638
 *  net.minecraft.class_746
 *  org.jetbrains.annotations.Nullable
 */
package dev.luminous.api.utils.world;

import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.combat.AutoCrystal;
import dev.luminous.mod.modules.impl.combat.AutoWeb;
import dev.luminous.mod.modules.impl.player.AirPlace;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1511;
import net.minecraft.class_1531;
import net.minecraft.class_1542;
import net.minecraft.class_1667;
import net.minecraft.class_1683;
import net.minecraft.class_1802;
import net.minecraft.class_1922;
import net.minecraft.class_1923;
import net.minecraft.class_1937;
import net.minecraft.class_1941;
import net.minecraft.class_2199;
import net.minecraft.class_2231;
import net.minecraft.class_2237;
import net.minecraft.class_2244;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2269;
import net.minecraft.class_2304;
import net.minecraft.class_2323;
import net.minecraft.class_2338;
import net.minecraft.class_2349;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2406;
import net.minecraft.class_2428;
import net.minecraft.class_243;
import net.minecraft.class_2533;
import net.minecraft.class_2586;
import net.minecraft.class_2596;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2846;
import net.minecraft.class_2885;
import net.minecraft.class_3711;
import net.minecraft.class_3713;
import net.minecraft.class_3718;
import net.minecraft.class_3965;
import net.minecraft.class_5329;
import net.minecraft.class_638;
import net.minecraft.class_746;
import org.jetbrains.annotations.Nullable;

public class BlockUtil
implements Wrapper {
    public static final List<class_2338> placedPos = new ArrayList<class_2338>();
    private static final double MIN_EYE_HEIGHT = 0.4;
    private static final double MAX_EYE_HEIGHT = 1.62;
    private static final double MOVEMENT_THRESHOLD = 2.0E-4;

    public static boolean canPlace(class_2338 pos) {
        return BlockUtil.canPlace(pos, 1000.0);
    }

    public static boolean canPlace(class_2338 pos, double distance) {
        if (BlockUtil.getPlaceSide(pos, distance) == null) {
            return false;
        }
        return !BlockUtil.canReplace(pos) ? false : !BlockUtil.hasEntity(pos, false);
    }

    public static boolean canPlace(class_2338 pos, double distance, boolean ignoreCrystal) {
        if (BlockUtil.getPlaceSide(pos, distance) == null) {
            return false;
        }
        return !BlockUtil.canReplace(pos) ? false : !BlockUtil.hasEntity(pos, ignoreCrystal);
    }

    public static boolean clientCanPlace(class_2338 pos, boolean ignoreCrystal) {
        return !BlockUtil.canReplace(pos) ? false : !BlockUtil.hasEntity(pos, ignoreCrystal);
    }

    public static List<class_1297> getEntities(class_238 box) {
        ArrayList<class_1297> list = new ArrayList<class_1297>();
        for (class_1297 entity : Alien.THREAD.getEntities()) {
            if (entity == null || entity instanceof class_1531 && AntiCheat.INSTANCE.ignoreArmorStand.getValue() || !entity.method_5829().method_994(box)) continue;
            list.add(entity);
        }
        return list;
    }

    public static List<class_1511> getEndCrystals(class_238 box) {
        ArrayList<class_1511> list = new ArrayList<class_1511>();
        for (class_1297 entity : Alien.THREAD.getEntities()) {
            class_1511 crystal;
            if (!(entity instanceof class_1511) || !(crystal = (class_1511)entity).method_5829().method_994(box)) continue;
            list.add(crystal);
        }
        return list;
    }

    public static boolean hasEntity(class_2338 pos, boolean ignoreCrystal) {
        return BlockUtil.hasEntity(new class_238(pos), ignoreCrystal);
    }

    public static boolean hasEntity(class_238 box, boolean ignoreCrystal) {
        for (class_1297 entity : BlockUtil.getEntities(box)) {
            if (!entity.method_5805() || entity instanceof class_1542 || entity instanceof class_1303 || entity instanceof class_1683 || entity instanceof class_1667 || ignoreCrystal && entity instanceof class_1511 && BlockUtil.mc.field_1724.method_33571().method_1022(MathUtil.getClosestPoint(entity)) <= AntiCheat.INSTANCE.ieRange.getValue()) continue;
            return true;
        }
        return false;
    }

    public static boolean hasCrystal(class_2338 pos) {
        for (class_1297 class_12972 : BlockUtil.getEndCrystals(new class_238(pos))) {
            if (!class_12972.method_5805() || !(class_12972 instanceof class_1511)) continue;
            return true;
        }
        return false;
    }

    public static boolean noEntityBlockCrystal(class_2338 pos, boolean ignoreCrystal) {
        return BlockUtil.noEntityBlockCrystal(pos, ignoreCrystal, false);
    }

    public static boolean noEntityBlockCrystal(class_2338 pos, boolean ignoreCrystal, boolean ignoreItem) {
        for (class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
            if (!entity.method_5805() || ignoreItem && entity instanceof class_1542 || ignoreCrystal && entity instanceof class_1511 && BlockUtil.mc.field_1724.method_33571().method_1022(MathUtil.getClosestPoint(entity)) <= AntiCheat.INSTANCE.ieRange.getValue()) continue;
            return false;
        }
        return true;
    }

    public static boolean canPlaceCrystal(class_2338 pos) {
        class_2338 obsPos = pos.method_10074();
        class_2338 boost = obsPos.method_10084();
        return !(BlockUtil.getBlock(obsPos) != class_2246.field_9987 && BlockUtil.getBlock(obsPos) != class_2246.field_10540 || BlockUtil.getClickSideStrict(obsPos) == null || !BlockUtil.mc.field_1687.method_22347(boost) || !BlockUtil.noEntityBlockCrystal(boost, false) || !BlockUtil.noEntityBlockCrystal(boost.method_10084(), false) || ClientSetting.INSTANCE.lowVersion.getValue() && !BlockUtil.mc.field_1687.method_22347(boost.method_10084()));
    }

    public static void placeCrystal(class_2338 pos, boolean rotate) {
        boolean offhand = BlockUtil.mc.field_1724.method_6079().method_7909() == class_1802.field_8301;
        class_2338 obsPos = pos.method_10074();
        class_2350 facing = BlockUtil.getClickSide(obsPos);
        class_243 vec = obsPos.method_46558().method_1031((double)facing.method_10163().method_10263() * 0.5, (double)facing.method_10163().method_10264() * 0.5, (double)facing.method_10163().method_10260() * 0.5);
        if (rotate) {
            Alien.ROTATION.lookAt(vec);
        }
        BlockUtil.clickBlock(obsPos, facing, false, offhand ? class_1268.field_5810 : class_1268.field_5808);
    }

    public static void placeBlock(class_2338 pos, boolean rotate) {
        BlockUtil.placeBlock(pos, rotate, AntiCheat.INSTANCE.packetPlace.getValue());
    }

    public static void placeBlock(class_2338 pos, boolean rotate, boolean packet) {
        if (BlockUtil.allowAirPlace()) {
            placedPos.add(pos);
            BlockUtil.airPlace(pos, rotate, class_1268.field_5808, packet);
        } else {
            class_2350 side = BlockUtil.getPlaceSide(pos);
            if (side != null) {
                placedPos.add(pos);
                BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), rotate, class_1268.field_5808, packet);
            }
        }
    }

    public static void clickBlock(class_2338 pos, class_2350 side, boolean rotate) {
        BlockUtil.clickBlock(pos, side, rotate, class_1268.field_5808);
    }

    public static void clickBlock(class_2338 pos, class_2350 side, boolean rotate, class_1268 hand) {
        BlockUtil.clickBlock(pos, side, rotate, hand, AntiCheat.INSTANCE.packetPlace.getValue());
    }

    public static void clickBlock(class_2338 pos, class_2350 side, boolean rotate, boolean packet) {
        BlockUtil.clickBlock(pos, side, rotate, class_1268.field_5808, packet);
    }

    public static void clickBlock(class_2338 pos, class_2350 side, boolean rotate, class_1268 hand, boolean packet) {
        class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        if (rotate) {
            Alien.ROTATION.lookAt(directionVec);
        }
        EntityUtil.swingHand(hand, AntiCheat.INSTANCE.interactSwing.getValue());
        class_3965 result = new class_3965(directionVec, side, pos, false);
        if (packet) {
            Module.sendSequencedPacket(id -> new class_2885(hand, result, id));
        } else {
            BlockUtil.mc.field_1761.method_2896(BlockUtil.mc.field_1724, hand, result);
        }
        BlockUtil.mc.field_1752 = 4;
        if (rotate) {
            Alien.ROTATION.snapBack();
        }
    }

    public static void clickBlock(class_2338 pos, class_2350 side, boolean rotate, class_1268 hand, SwingSide swingSide) {
        class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        if (rotate) {
            Alien.ROTATION.lookAt(directionVec);
        }
        EntityUtil.swingHand(hand, swingSide);
        class_3965 result = new class_3965(directionVec, side, pos, false);
        Module.sendSequencedPacket(id -> new class_2885(hand, result, id));
        BlockUtil.mc.field_1752 = 4;
        if (rotate) {
            Alien.ROTATION.snapBack();
        }
    }

    public static void airPlace(class_2338 pos, boolean rotate) {
        BlockUtil.airPlace(pos, rotate, class_1268.field_5808, AntiCheat.INSTANCE.packetPlace.getValue());
    }

    public static void airPlace(class_2338 pos, boolean rotate, class_1268 hand, boolean packet) {
        boolean bypass;
        boolean bl = bypass = hand == class_1268.field_5808 && AirPlace.INSTANCE.grimBypass.getValue();
        if (bypass) {
            mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12969, new class_2338(0, 0, 0), class_2350.field_11033));
            hand = class_1268.field_5810;
        }
        class_2350 side = BlockUtil.getClickSide(pos);
        class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        if (rotate) {
            Alien.ROTATION.lookAt(directionVec);
        }
        EntityUtil.swingHand(hand, AntiCheat.INSTANCE.interactSwing.getValue());
        class_3965 result = new class_3965(directionVec, side, pos, false);
        if (packet) {
            class_1268 finalHand = hand;
            Module.sendSequencedPacket(id -> new class_2885(finalHand, result, id));
        } else {
            BlockUtil.mc.field_1761.method_2896(BlockUtil.mc.field_1724, hand, result);
        }
        BlockUtil.mc.field_1752 = 4;
        if (rotate) {
            Alien.ROTATION.snapBack();
        }
        if (bypass) {
            mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12969, new class_2338(0, 0, 0), class_2350.field_11033));
        }
    }

    public static double distanceToXZ(double x, double z, double x2, double z2) {
        double dx = x2 - x;
        double dz = z2 - z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static double distanceToXZ(double x, double z) {
        return BlockUtil.distanceToXZ(x, z, BlockUtil.mc.field_1724.method_23317(), BlockUtil.mc.field_1724.method_23321());
    }

    public static class_2350 getPlaceSide(class_2338 pos) {
        if (BlockUtil.allowAirPlace()) {
            return BlockUtil.getClickSide(pos);
        }
        double minDistance = Double.MAX_VALUE;
        class_2350 side = null;
        for (class_2350 i : class_2350.values()) {
            double vecDis;
            if (!BlockUtil.canClick(pos.method_10093(i)) || BlockUtil.canReplace(pos.method_10093(i)) || !BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) || (vecDis = BlockUtil.mc.field_1724.method_33571().method_1025(pos.method_46558().method_1031((double)i.method_10163().method_10263() * 0.5, (double)i.method_10163().method_10264() * 0.5, (double)i.method_10163().method_10260() * 0.5))) > minDistance) continue;
            side = i;
            minDistance = vecDis;
        }
        return side;
    }

    public static class_2350 getBestNeighboring(class_2338 pos, class_2350 facing) {
        class_2350 bestFacing = null;
        double distance = 0.0;
        for (class_2350 i : class_2350.values()) {
            if (facing != null && pos.method_10093(i).equals((Object)pos.method_10079(facing, -1)) || i == class_2350.field_11033 || BlockUtil.getPlaceSide(pos) == null || bestFacing != null && !(BlockUtil.mc.field_1724.method_33571().method_1025(pos.method_10093(i).method_46558()) < distance)) continue;
            bestFacing = i;
            distance = BlockUtil.mc.field_1724.method_33571().method_1025(pos.method_10093(i).method_46558());
        }
        return bestFacing;
    }

    public static class_2350 getPlaceSide(class_2338 pos, double reachDistance) {
        if (BlockUtil.allowAirPlace()) {
            class_2350 i = BlockUtil.getClickSide(pos);
            double vecDis = BlockUtil.mc.field_1724.method_33571().method_1025(pos.method_46558().method_1031((double)i.method_10163().method_10263() * 0.5, (double)i.method_10163().method_10264() * 0.5, (double)i.method_10163().method_10260() * 0.5));
            return Math.sqrt(vecDis) > reachDistance ? null : class_2350.field_11033;
        }
        double minDistance = Double.MAX_VALUE;
        class_2350 side = null;
        for (class_2350 i : class_2350.values()) {
            double vecDis;
            if (!BlockUtil.canClick(pos.method_10093(i)) || BlockUtil.canReplace(pos.method_10093(i)) || !BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) || Math.sqrt(vecDis = BlockUtil.mc.field_1724.method_33571().method_1025(pos.method_46558().method_1031((double)i.method_10163().method_10263() * 0.5, (double)i.method_10163().method_10264() * 0.5, (double)i.method_10163().method_10260() * 0.5))) > reachDistance || vecDis > minDistance) continue;
            side = i;
            minDistance = vecDis;
        }
        return side;
    }

    public static class_2350 getClickSide(class_2338 pos) {
        class_2350 side = class_2350.field_11036;
        double minDistance = Double.MAX_VALUE;
        for (class_2350 i : class_2350.values()) {
            double disSq;
            if (!BlockUtil.isStrictDirection(pos, i) || (disSq = BlockUtil.mc.field_1724.method_33571().method_1025(pos.method_10093(i).method_46558())) > minDistance) continue;
            side = i;
            minDistance = disSq;
        }
        return side;
    }

    public static class_2350 getClickSideStrict(class_2338 pos) {
        class_2350 side = null;
        double minDistance = Double.MAX_VALUE;
        for (class_2350 i : class_2350.values()) {
            double disSq;
            if (!BlockUtil.isStrictDirection(pos, i) || (disSq = BlockUtil.mc.field_1724.method_33571().method_1025(pos.method_10093(i).method_46558())) > minDistance) continue;
            side = i;
            minDistance = disSq;
        }
        return side;
    }

    public static boolean isStrictDirection(class_2338 pos, class_2350 side, double reachDistance) {
        double vecDis = BlockUtil.mc.field_1724.method_33571().method_1025(pos.method_46558().method_1031((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5));
        return Math.sqrt(vecDis) > reachDistance ? false : BlockUtil.isStrictDirection(pos, side);
    }

    public static boolean isStrictDirection(class_2338 pos, class_2350 side) {
        switch (AntiCheat.INSTANCE.placement.getValue()) {
            case Vanilla: {
                return true;
            }
            case Legit: {
                return EntityUtil.canSee(pos, side);
            }
            case Grim: {
                return BlockUtil.grimStrictDirectionCheck(pos, side, BlockUtil.mc.field_1687, BlockUtil.mc.field_1724);
            }
            case NCP: {
                if (BlockUtil.mc.field_1687.method_8320(pos.method_10093(side)).method_26234((class_1922)BlockUtil.mc.field_1687, pos.method_10093(side))) {
                    return false;
                }
                class_243 eyePos = BlockUtil.mc.field_1724.method_33571();
                class_243 blockCenter = pos.method_46558();
                ArrayList<class_2350> validAxis = new ArrayList<class_2350>();
                validAxis.addAll(BlockUtil.checkAxis(eyePos.field_1352 - blockCenter.field_1352, class_2350.field_11039, class_2350.field_11034, false));
                validAxis.addAll(BlockUtil.checkAxis(eyePos.field_1351 - blockCenter.field_1351, class_2350.field_11033, class_2350.field_11036, true));
                validAxis.addAll(BlockUtil.checkAxis(eyePos.field_1350 - blockCenter.field_1350, class_2350.field_11043, class_2350.field_11035, false));
                return validAxis.contains(side);
            }
        }
        return true;
    }

    public static boolean grimStrictDirectionCheck(class_2338 pos, class_2350 direction, class_638 level, class_746 player) {
        class_238 combined = BlockUtil.getCombinedBox(pos, (class_1937)level);
        class_238 eyePositions = new class_238(player.method_23317(), player.method_23318() + 0.4, player.method_23321(), player.method_23317(), player.method_23318() + 1.62, player.method_23321()).method_1014(2.0E-4);
        if (BlockUtil.isIntersected(eyePositions, combined)) {
            return true;
        }
        switch (direction) {
            case field_11043: {
                if (eyePositions.field_1321 > combined.field_1321) break;
                return true;
            }
            case field_11035: {
                if (eyePositions.field_1324 < combined.field_1324) break;
                return true;
            }
            case field_11034: {
                if (eyePositions.field_1320 < combined.field_1320) break;
                return true;
            }
            case field_11039: {
                if (eyePositions.field_1323 > combined.field_1323) break;
                return true;
            }
            case field_11036: {
                if (eyePositions.field_1325 < combined.field_1325) break;
                return true;
            }
            case field_11033: {
                if (eyePositions.field_1322 > combined.field_1322) break;
                return true;
            }
            default: {
                throw new MatchException(null, null);
            }
        }
        return false;
    }

    private static class_238 getCombinedBox(class_2338 pos, class_1937 level) {
        class_265 shape = level.method_8320(pos).method_26220((class_1922)level, pos).method_1096((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260());
        class_238 combined = new class_238(pos);
        for (class_238 box : shape.method_1090()) {
            double minX = Math.max(box.field_1323, combined.field_1323);
            double minY = Math.max(box.field_1322, combined.field_1322);
            double minZ = Math.max(box.field_1321, combined.field_1321);
            double maxX = Math.min(box.field_1320, combined.field_1320);
            double maxY = Math.min(box.field_1325, combined.field_1325);
            double maxZ = Math.min(box.field_1324, combined.field_1324);
            combined = new class_238(minX, minY, minZ, maxX, maxY, maxZ);
        }
        return combined;
    }

    private static boolean isIntersected(class_238 bb, class_238 other) {
        return other.field_1320 - 1.0E-7 > bb.field_1323 && other.field_1323 + 1.0E-7 < bb.field_1320 && other.field_1325 - 1.0E-7 > bb.field_1322 && other.field_1322 + 1.0E-7 < bb.field_1325 && other.field_1324 - 1.0E-7 > bb.field_1321 && other.field_1321 + 1.0E-7 < bb.field_1324;
    }

    public static ArrayList<class_2350> checkAxis(double diff, class_2350 negativeSide, class_2350 positiveSide, boolean vertical) {
        ArrayList<class_2350> valid = new ArrayList<class_2350>();
        if (vertical) {
            if (diff < -0.5) {
                valid.add(negativeSide);
            }
            if (AntiCheat.INSTANCE.upDirectionLimit.getValue()) {
                if (diff > 0.5) {
                    valid.add(positiveSide);
                }
            } else if (diff > -0.5) {
                valid.add(positiveSide);
            }
        } else {
            if (diff < -0.5) {
                valid.add(negativeSide);
            }
            if (diff > 0.5) {
                valid.add(positiveSide);
            }
        }
        return valid;
    }

    public static ArrayList<class_2586> getTileEntities() {
        return BlockUtil.getLoadedChunks().flatMap(chunk -> chunk.method_12214().values().stream()).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Stream<class_2818> getLoadedChunks() {
        int radius = Math.max(2, BlockUtil.mc.field_1690.method_38521()) + 3;
        int diameter = radius * 2 + 1;
        class_1923 center = BlockUtil.mc.field_1724.method_31476();
        class_1923 min = new class_1923(center.field_9181 - radius, center.field_9180 - radius);
        class_1923 max = new class_1923(center.field_9181 + radius, center.field_9180 + radius);
        return Stream.iterate(min, pos -> {
            int x = pos.field_9181;
            int z = pos.field_9180;
            if (++x > max.field_9181) {
                x = min.field_9181;
                ++z;
            }
            return new class_1923(x, z);
        }).limit((long)diameter * (long)diameter).filter(c -> BlockUtil.mc.field_1687.method_8393(c.field_9181, c.field_9180)).map(c -> BlockUtil.mc.field_1687.method_8497(c.field_9181, c.field_9180)).filter(Objects::nonNull);
    }

    public static ArrayList<class_2338> getSphere(float range) {
        return BlockUtil.getSphere(range, BlockUtil.mc.field_1724.method_33571());
    }

    public static class_2338 getBlock(class_2248 block, float range) {
        for (class_2338 pos : BlockUtil.getSphere(range)) {
            if (BlockUtil.mc.field_1687.method_8320(pos).method_26204() != block) continue;
            return pos;
        }
        return null;
    }

    public static class_2338 getBlock(Class<?> block, float range) {
        for (class_2338 pos : BlockUtil.getSphere(range)) {
            if (!block.isInstance(BlockUtil.mc.field_1687.method_8320(pos).method_26204())) continue;
            return pos;
        }
        return null;
    }

    public static ArrayList<class_2338> getSphere(float range, class_243 pos) {
        ArrayList<class_2338> list = new ArrayList<class_2338>();
        for (double y = pos.method_10214() + (double)range; y > pos.method_10214() - (double)range; y -= 1.0) {
            if (y < -64.0) continue;
            for (double x = pos.method_10216() - (double)range; x < pos.method_10216() + (double)range; x += 1.0) {
                for (double z = pos.method_10215() - (double)range; z < pos.method_10215() + (double)range; z += 1.0) {
                    BlockPosX curPos = new BlockPosX(x, y, z);
                    if (curPos.method_46558().method_1022(pos) > (double)range) continue;
                    list.add(curPos);
                }
            }
        }
        return list;
    }

    public static class_2248 getBlock(class_2338 pos) {
        return BlockUtil.mc.field_1687.method_8320(pos).method_26204();
    }

    public static boolean canReplace(class_2338 pos) {
        if (pos.method_10264() >= 320) {
            return false;
        }
        if (AntiCheat.INSTANCE.multiPlace.getValue() && placedPos.contains(pos)) {
            return false;
        }
        class_2680 state = BlockUtil.mc.field_1687.method_8320(pos);
        return state.method_26204() == class_2246.field_10343 && AutoWeb.ignore && AutoCrystal.INSTANCE.replace.getValue() || state.method_45474();
    }

    public static boolean canClick(class_2338 pos) {
        if (AntiCheat.INSTANCE.multiPlace.getValue() && placedPos.contains(pos)) {
            return true;
        }
        class_2680 state = BlockUtil.mc.field_1687.method_8320(pos);
        class_2248 block = state.method_26204();
        return block == class_2246.field_10343 && AutoWeb.ignore ? AutoCrystal.INSTANCE.airPlace.getValue() : BlockUtil.mc.field_1724.method_5715() || !BlockUtil.isClickable(block);
    }

    public static boolean isClickable(class_2248 block) {
        return block instanceof class_2304 || block instanceof class_2199 || block instanceof class_2406 || block instanceof class_3711 || block instanceof class_3713 || block instanceof class_3718 || block instanceof class_2269 || block instanceof class_2231 || block instanceof class_2237 || block instanceof class_2244 || block instanceof class_2349 || block instanceof class_2323 || block instanceof class_2428 || block instanceof class_2533;
    }

    public static boolean canCollide(class_238 box) {
        return BlockUtil.canCollide((class_1297)BlockUtil.mc.field_1724, box);
    }

    public static boolean canCollide(@Nullable class_1297 entity, class_238 box) {
        class_5329 blockCollisionSpliterator = new class_5329((class_1941)BlockUtil.mc.field_1687, entity, box, false, (pos, voxelShape) -> voxelShape);
        while (blockCollisionSpliterator.hasNext()) {
            if (((class_265)blockCollisionSpliterator.next()).method_1110()) continue;
            return true;
        }
        return false;
    }

    public static boolean allowAirPlace() {
        return AirPlace.INSTANCE.isOn() && AirPlace.INSTANCE.module.getValue();
    }
}

