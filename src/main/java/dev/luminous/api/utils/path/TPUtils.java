/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$PositionAndOnGround
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$OnGroundOnly
 */
package dev.luminous.api.utils.path;

import com.google.common.collect.Lists;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.path.PathUtils;
import dev.luminous.api.utils.path.Vec3;
import java.util.List;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class TPUtils
implements Wrapper {
    public static void teleportWithBack(class_243 newPos, TeleportType type, Runnable runnable) {
        switch (type.ordinal()) {
            case 0: {
                TPUtils.legacyTeleportWithBack(newPos, runnable);
                break;
            }
            case 1: {
                TPUtils.newTeleportWithBack(newPos, runnable);
            }
        }
    }

    public static void legacyTeleportWithBack(class_243 newPos, Runnable runnable) {
        List<Vec3> tpPath = PathUtils.computePath(newPos);
        tpPath.removeFirst();
        tpPath.forEach(vec3 -> TPUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(vec3.x(), vec3.y(), vec3.z(), false)));
        runnable.run();
        tpPath = Lists.reverse(tpPath);
        tpPath.removeFirst();
        tpPath.forEach(vec3 -> TPUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(vec3.x(), vec3.y(), vec3.z(), false)));
        TPUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(TPUtils.mc.field_1724.method_23317(), TPUtils.mc.field_1724.method_23318(), TPUtils.mc.field_1724.method_23321(), false));
    }

    public static void newTeleportWithBack(class_243 newPos, Runnable runnable) {
        int i;
        int packetsRequired = (int)Math.ceil(TPUtils.mc.field_1724.method_19538().method_1022(newPos) / 10.0) - 1;
        for (i = 0; i < packetsRequired; ++i) {
            TPUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_5911(true));
        }
        TPUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(newPos.field_1352, newPos.field_1351, newPos.field_1350, true));
        runnable.run();
        for (i = 0; i < packetsRequired; ++i) {
            TPUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_5911(true));
        }
        TPUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(TPUtils.mc.field_1724.method_23317(), TPUtils.mc.field_1724.method_23318(), TPUtils.mc.field_1724.method_23321(), false));
    }

    public static void newTeleport(class_243 newPos) {
        int packetsRequired = (int)Math.ceil(TPUtils.mc.field_1724.method_19538().method_1022(newPos) / 10.0) - 1;
        for (int i = 0; i < packetsRequired; ++i) {
            TPUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_5911(true));
        }
        TPUtils.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(newPos.field_1352, newPos.field_1351, newPos.field_1350, true));
    }

    public static enum TeleportType {
        Legacy,
        New;

    }
}

