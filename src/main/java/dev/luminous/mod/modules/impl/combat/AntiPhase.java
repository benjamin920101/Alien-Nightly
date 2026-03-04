/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.ExperienceOrbEntity
 *  net.minecraft.entity.decoration.ItemFrameEntity
 *  net.minecraft.entity.ItemEntity
 *  net.minecraft.entity.projectile.ArrowEntity
 *  net.minecraft.entity.projectile.thrown.ExperienceBottleEntity
 *  net.minecraft.item.Items
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket
 *  net.minecraft.block.ScaffoldingBlock
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.block.ScaffoldingBlock;

public class AntiPhase
extends Module {
    private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 4, 0, 8));
    private final BooleanSetting ladder = this.add(new BooleanSetting("Ladder", true).setParent());
    private final BooleanSetting onlyHard = this.add(new BooleanSetting("OnlyHard", true, this.ladder::isOpen));
    private final BooleanSetting itemFrame = this.add(new BooleanSetting("ItemFrame", true).setParent());
    private final BooleanSetting fill = this.add(new BooleanSetting("Fill", false, this.itemFrame::isOpen));
    private final BooleanSetting scaffolding = this.add(new BooleanSetting("Scaffolding", true));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true));
    private final BooleanSetting collideSkip = this.add(new BooleanSetting("CollideSkip", true));
    private final BooleanSetting crawlingSkip = this.add(new BooleanSetting("CrawlingSkip", true));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("InAirSkip", false));
    private final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 5.0, 0.0, 7.0, 0.1));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 100.0, 0.0, 2000.0, 1.0));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final Timer timer = new Timer();

    public AntiPhase() {
        super("AntiPhase", Module.Category.Combat);
        this.setChinese("\u53cd\u7a7f\u5899");
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (!(this.eatingPause.getValue() && AntiPhase.mc.field_1724.method_6115() || Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue())) {
            for (class_1297 class_12972 : CombatUtil.getEnemies(this.targetRange.getValue())) {
                class_2350 facing;
                int block;
                if (this.crawlingSkip.getValue() && class_12972.method_20448() || this.onlyGround.getValue() && !class_12972.method_24828() || this.collideSkip.getValue() && BlockUtil.canCollide(class_12972, class_12972.method_5829()) || !this.timer.passed(this.delay.getValueInt())) continue;
                if (this.scaffolding.getValue() && BlockUtil.canReplace(class_12972.method_24515()) && (block = this.getScaffolding()) != -1) {
                    class_2338 bp = class_12972.method_24515();
                    class_2350 downSide = null;
                    class_2350 placeSide = BlockUtil.getPlaceSide(bp, 6.0);
                    if (placeSide != null || (downSide = this.getSideIgnore(bp.method_10074())) != null && BlockUtil.getBlock(bp.method_10074()) instanceof class_3736 && !AntiPhase.mc.field_1724.method_5715() || AntiPhase.mc.field_1724.method_5715() && (downSide = this.getSideOnly(bp.method_10074())) != null && BlockUtil.getBlock(bp.method_10074()) instanceof class_3736) {
                        class_243 targetPos = placeSide != null ? bp.method_10093(placeSide).method_46558().method_1031((double)placeSide.method_10153().method_10163().method_10263() * 0.5, (double)placeSide.method_10153().method_10163().method_10264() * 0.5, (double)placeSide.method_10153().method_10163().method_10260() * 0.5) : bp.method_10074().method_46558().method_1031((double)downSide.method_10163().method_10263() * 0.5, (double)downSide.method_10163().method_10264() * 0.5, (double)downSide.method_10163().method_10260() * 0.5);
                        double distance = AntiPhase.mc.field_1724.method_33571().method_1022(targetPos);
                        if (distance <= this.placeRange.getValue()) {
                            int old = AntiPhase.mc.field_1724.method_31548().field_7545;
                            this.doSwap(block);
                            if (BlockUtil.getBlock(bp.method_10074()) instanceof class_3736 && downSide != null) {
                                BlockUtil.clickBlock(bp.method_10074(), downSide, this.rotate.getValue());
                            } else {
                                BlockUtil.placeBlock(bp, this.rotate.getValue());
                            }
                            this.timer.reset();
                            if (this.inventory.getValue()) {
                                this.doSwap(block);
                                EntityUtil.syncInventory();
                            } else {
                                this.doSwap(old);
                            }
                        }
                    }
                }
                if (this.itemFrame.getValue() && AntiPhase.mc.field_1687.method_22347(class_12972.method_24515())) {
                    int block2;
                    class_1533 itemFrameEntity = this.hasItemFrame(new class_238(class_12972.method_24515()));
                    if (itemFrameEntity == null && (block2 = this.getItemFrame()) != -1) {
                        class_2338 bp = class_12972.method_24515().method_10074();
                        double distance = AntiPhase.mc.field_1724.method_33571().method_1022(bp.method_61082().method_1031(0.0, 1.0, 0.0));
                        if (distance <= this.placeRange.getValue() && BlockUtil.isStrictDirection(bp, class_2350.field_11036) && !BlockUtil.canReplace(bp) && BlockUtil.canClick(bp)) {
                            int oldx = AntiPhase.mc.field_1724.method_31548().field_7545;
                            this.doSwap(block2);
                            BlockUtil.clickBlock(bp, class_2350.field_11036, this.rotate.getValue());
                            this.timer.reset();
                            if (this.inventory.getValue()) {
                                this.doSwap(block2);
                                EntityUtil.syncInventory();
                            } else {
                                this.doSwap(oldx);
                            }
                        }
                    }
                    if (this.fill.getValue() && itemFrameEntity != null && itemFrameEntity.method_6940().method_7960()) {
                        int block3;
                        class_243 hitVec = MathUtil.getClosestPointToBox(AntiPhase.mc.field_1724.method_33571(), itemFrameEntity.method_5829());
                        if (AntiPhase.mc.field_1724.method_33571().method_1022(hitVec) <= AntiCheat.INSTANCE.ieRange.getValue() && (block3 = this.getObsidian()) != -1) {
                            int oldx = AntiPhase.mc.field_1724.method_31548().field_7545;
                            this.doSwap(block3);
                            if (this.rotate.getValue()) {
                                Alien.ROTATION.snapAt(hitVec);
                            }
                            AntiPhase.mc.field_1724.field_3944.method_52787((class_2596)class_2824.method_34207((class_1297)itemFrameEntity, (boolean)AntiPhase.mc.field_1724.method_5715(), (class_1268)class_1268.field_5808));
                            this.timer.reset();
                            if (this.inventory.getValue()) {
                                this.doSwap(block3);
                                EntityUtil.syncInventory();
                            } else {
                                this.doSwap(oldx);
                            }
                            if (this.rotate.getValue()) {
                                Alien.ROTATION.snapBack();
                            }
                        }
                    }
                }
                if (!this.ladder.getValue() || (block = this.getLadder()) == -1 || !BlockUtil.canReplace(class_12972.method_24515()) || (facing = this.targetFacing(class_12972.method_19538())) == null) continue;
                class_2338 bp = class_12972.method_24515().method_10093(facing);
                double distance = AntiPhase.mc.field_1724.method_33571().method_1022(bp.method_46558().method_1031((double)facing.method_10153().method_10163().method_10263() * 0.5, (double)facing.method_10153().method_10163().method_10264() * 0.5, (double)facing.method_10153().method_10163().method_10260() * 0.5));
                if (!(distance <= this.placeRange.getValue())) continue;
                BlockUtil.placedPos.add(class_12972.method_24515());
                int oldxx = AntiPhase.mc.field_1724.method_31548().field_7545;
                this.doSwap(block);
                BlockUtil.clickBlock(bp, facing.method_10153(), this.rotate.getValue());
                this.timer.reset();
                if (this.inventory.getValue()) {
                    this.doSwap(block);
                    EntityUtil.syncInventory();
                    continue;
                }
                this.doSwap(oldxx);
            }
        }
    }

    private class_2350 getSideOnly(class_2338 pos) {
        return BlockUtil.isStrictDirection(pos, class_2350.field_11036) ? class_2350.field_11036 : null;
    }

    private class_2350 getSideIgnore(class_2338 pos) {
        for (class_2350 i : class_2350.values()) {
            if (i == class_2350.field_11036 || !BlockUtil.isStrictDirection(pos, i)) continue;
            return i;
        }
        return null;
    }

    private class_1533 hasItemFrame(class_238 box) {
        for (class_1297 entity : BlockUtil.getEntities(box)) {
            if (!(entity instanceof class_1533)) continue;
            class_1533 itemFrameEntity = (class_1533)entity;
            if (entity.method_58149() != class_2350.field_11036) continue;
            return itemFrameEntity;
        }
        return null;
    }

    private static class_238 getBox(class_2350 facing, class_2338 bp) {
        class_238 box = null;
        double wide = 0.1875;
        double x = (double)facing.method_10148() * 0.5 + (double)bp.method_10263() + 0.5;
        double y = bp.method_10264();
        double z = (double)facing.method_10165() * 0.5 + (double)bp.method_10260() + 0.5;
        switch (facing) {
            case field_11039: {
                box = new class_238(x, y, z, x + wide, y + 1.0, z + 1.0);
                break;
            }
            case field_11034: {
                box = new class_238(x, y, z, x - wide, y + 1.0, z + 1.0);
                break;
            }
            case field_11043: {
                box = new class_238(x, y, z, x + 1.0, y + 1.0, z + wide);
                break;
            }
            case field_11035: {
                box = new class_238(x, y, z, x + 1.0, y + 1.0, z - wide);
            }
        }
        return box;
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, AntiPhase.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getFlintAndSteel() {
        return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_8884) : InventoryUtil.findItem(class_1802.field_8884);
    }

    private int getObsidian() {
        return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_8281) : InventoryUtil.findItem(class_1802.field_8281);
    }

    private int getItemFrame() {
        return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_8143) : InventoryUtil.findItem(class_1802.field_8143);
    }

    private int getLadder() {
        return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_9983) : InventoryUtil.findBlock(class_2246.field_9983);
    }

    private int getScaffolding() {
        return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_16492) : InventoryUtil.findBlock(class_2246.field_16492);
    }

    private class_2350 targetFacing(class_243 vec3d) {
        BlockPosX blockPos = new BlockPosX(vec3d);
        class_243 centerPos = blockPos.method_61082();
        float factorValue = 0.4f;
        double minDistance = Double.MAX_VALUE;
        class_2350 facing = null;
        for (class_2350 direction : class_2350.values()) {
            class_243 tempPos;
            double distance;
            class_238 box;
            class_2338 bp;
            if (direction == class_2350.field_11036 || direction == class_2350.field_11033 || !BlockUtil.isStrictDirection(bp = blockPos.method_10093(direction), direction.method_10153()) || !(this.onlyHard.getValue() ? Alien.HOLE.isHard(bp) : !BlockUtil.canReplace(bp) && BlockUtil.canClick(bp)) || (box = AntiPhase.getBox(direction, blockPos)) == null || AntiPhase.hasEntity(box) || !((distance = (tempPos = centerPos.method_1031((double)((float)direction.method_10148() * factorValue), 0.0, (double)((float)direction.method_10165() * factorValue))).method_1022(vec3d)) < minDistance)) continue;
            minDistance = distance;
            facing = direction;
        }
        return facing;
    }

    public static boolean hasEntity(class_238 box) {
        for (class_1297 entity : BlockUtil.getEntities(box)) {
            if (!entity.method_5805() || entity instanceof class_1542 || entity instanceof class_1303 || entity instanceof class_1683 || entity instanceof class_1667 || entity instanceof class_1533) continue;
            return true;
        }
        return false;
    }
}

