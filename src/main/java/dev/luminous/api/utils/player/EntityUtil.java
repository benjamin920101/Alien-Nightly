/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.AxeItem
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.SwordItem
 *  net.minecraft.item.TridentItem
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket
 *  net.minecraft.network.packet.c2s.play.HandSwingC2SPacket
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.client.gui.screen.ChatScreen
 *  net.minecraft.client.gui.screen.option.OptionsScreen
 *  net.minecraft.client.gui.screen.GameMenuScreen
 *  net.minecraft.client.gui.screen.option.GameOptionsScreen
 *  net.minecraft.client.gui.screen.ingame.InventoryScreen
 *  net.minecraft.item.MaceItem
 */
package dev.luminous.api.utils.player;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.gui.ClickGuiScreen;
import dev.luminous.mod.gui.PeekScreen;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.MaceItem;

public class EntityUtil
implements Wrapper {
    public static boolean inInventory() {
        return EntityUtil.mc.field_1755 == null || EntityUtil.mc.field_1755 instanceof class_4667 || EntityUtil.mc.field_1755 instanceof class_429 || EntityUtil.mc.field_1755 instanceof PeekScreen || EntityUtil.mc.field_1755 instanceof class_408 || EntityUtil.mc.field_1755 instanceof class_490 || EntityUtil.mc.field_1755 instanceof ClickGuiScreen || EntityUtil.mc.field_1755 instanceof class_433;
    }

    public static boolean isHoldingWeapon(class_1657 player) {
        return player.method_6047().method_7909() instanceof class_1829 || player.method_6047().method_7909() instanceof class_1743 || player.method_6047().method_7909() instanceof class_9362 || player.method_6047().method_7909() instanceof class_1835;
    }

    public static boolean isInsideBlock(class_1657 player) {
        return BlockUtil.canCollide((class_1297)player, player.method_5829());
    }

    public static boolean isInsideBlock() {
        return EntityUtil.isInsideBlock((class_1657)EntityUtil.mc.field_1724);
    }

    public static int getDamagePercent(class_1799 stack) {
        return stack.method_7919() == stack.method_7936() ? 100 : (int)((double)(stack.method_7936() - stack.method_7919()) / Math.max(0.1, (double)stack.method_7936()) * 100.0);
    }

    public static boolean isArmorLow(class_1657 player, int durability) {
        for (class_1799 piece : player.method_5661()) {
            if (piece == null || piece.method_7960()) {
                return true;
            }
            if (EntityUtil.getDamagePercent(piece) >= durability) continue;
            return true;
        }
        return false;
    }

    public static float getHealth(class_1297 entity) {
        if (entity.method_5709()) {
            class_1309 livingBase = (class_1309)entity;
            return livingBase.method_6032() + livingBase.method_6067();
        }
        return 0.0f;
    }

    public static class_2338 getEntityPos(class_1297 entity) {
        return new BlockPosX(entity.method_19538());
    }

    public static class_2338 getPlayerPos(boolean fix) {
        return new BlockPosX(EntityUtil.mc.field_1724.method_19538(), fix);
    }

    public static class_2338 getEntityPos(class_1297 entity, boolean fix) {
        return new BlockPosX(entity.method_19538(), fix);
    }

    public static boolean canSee(class_2338 pos, class_2350 side) {
        class_243 testVec = pos.method_46558().method_1031((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5);
        class_3965 result = EntityUtil.mc.field_1687.method_17742(new class_3959(EntityUtil.mc.field_1724.method_33571(), testVec, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)EntityUtil.mc.field_1724));
        return result == null || result.method_17783() == class_239.class_240.field_1333;
    }

    public static void swingHand(class_1268 hand, SwingSide side) {
        switch (side) {
            case All: {
                EntityUtil.mc.field_1724.method_6104(hand);
                break;
            }
            case Client: {
                EntityUtil.mc.field_1724.method_23667(hand, false);
                break;
            }
            case Server: {
                mc.method_1562().method_52787((class_2596)new class_2879(hand));
            }
        }
    }

    public static void syncInventory() {
        if (AntiCheat.INSTANCE.closeScreen.getValue()) {
            mc.method_1562().method_52787((class_2596)new class_2815(EntityUtil.mc.field_1724.field_7512.field_7763));
        }
    }
}

