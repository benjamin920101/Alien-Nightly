/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.ItemEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.projectile.ArrowEntity
 *  net.minecraft.item.BlockItem
 *  net.minecraft.item.ItemStack
 *  net.minecraft.block.Blocks
 *  net.minecraft.block.Block
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.block.TrapdoorBlock
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.block.BlockState
 *  net.minecraft.state.property.Property
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.PredictUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.move.MoveUtils;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.rotation.Rotation;
import dev.luminous.api.utils.rotation.RotationUtils;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.network.packet.Packet;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

public class TrapdoorAura
extends Module {
    public static TrapdoorAura INSTANCE;
    public class_1657 target;
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 8.0).setSuffix("m"));
    private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 4.5, 0.0, 6.0).setSuffix("m"));
    private final SliderSetting predictTicks = this.add(new SliderSetting("PredictTicks", 2.0, 0.0, 50.0, 1.0));
    private final SliderSetting closeDelay = this.add(new SliderSetting("CloseDelay", 75, 0, 500).setSuffix("ms"));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private final BooleanSetting movementFix = this.add(new BooleanSetting("MovementFix", true));
    private final BooleanSetting silent = this.add(new BooleanSetting("Silent", false));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting ghostHand = this.add(new BooleanSetting("GhostHand", true));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final BooleanSetting pauseEat = this.add(new BooleanSetting("PauseEat", true));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", false));
    private final BooleanSetting ignoreNakeds = this.add(new BooleanSetting("IgnoreNakeds", false));
    private final BooleanSetting antiStandUp = this.add(new BooleanSetting("AntiStandUp", true));
    private final BooleanSetting placeOtherBlocks = this.add(new BooleanSetting("PlaceOtherBlocks", true));
    private final BooleanSetting useTrapdoors = this.add(new BooleanSetting("UseTrapdoors", true));
    private final BooleanSetting useWebs = this.add(new BooleanSetting("UseWebs", false));
    private final BooleanSetting useLadders = this.add(new BooleanSetting("UseLadders", false));
    private final BooleanSetting useVines = this.add(new BooleanSetting("UseVines", false));
    private final BooleanSetting useScaffolds = this.add(new BooleanSetting("UseScaffolds", false));
    private final Timer timer = new Timer();
    private final Timer closeTimer = new Timer();
    private boolean waitingClose = false;
    private class_2338 pendingClosePos = null;
    private class_2338 completedPos = null;
    private class_1657 currentTarget = null;
    private static final Set<Class<? extends class_2248>> TRAPDOOR_TYPES;

    public TrapdoorAura() {
        super("TrapdoorAura", Module.Category.Combat);
        this.setChinese("\u6d3b\u677f\u95e8\u5149\u73af");
        INSTANCE = this;
    }

    @Override
    public boolean onEnable() {
        this.target = null;
        return false;
    }

    @Override
    public void onDisable() {
        this.reset();
    }

    private void reset() {
        this.waitingClose = false;
        this.pendingClosePos = null;
        this.completedPos = null;
        this.currentTarget = null;
        this.timer.reset();
        this.closeTimer.reset();
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.waitingClose) {
            if (this.pendingClosePos != null && this.closeTimer.passed((long)this.closeDelay.getValue())) {
                if (this.toggleTrapdoor(this.pendingClosePos, false)) {
                    this.completedPos = this.pendingClosePos;
                    this.timer.reset();
                }
                this.pendingClosePos = null;
                this.waitingClose = false;
            }
        } else if (!this.shouldPause() && this.timer.passed((long)this.delay.getValue())) {
            class_1657 target = CombatUtil.getClosestEnemy(this.range.getValue());
            if (target == null) {
                if (this.autoDisable.getValue()) {
                    this.disable();
                }
                this.currentTarget = null;
            } else {
                class_243 targetPos;
                BlockPosX trapdoorPos;
                this.currentTarget = target;
                if (!(this.ignoreNakeds.getValue() && this.isNaked(target) || (trapdoorPos = new BlockPosX((targetPos = this.predictTicks.getValue() > 0.0 ? PredictUtil.getPos(target, this.predictTicks.getValueInt()) : target.method_19538()).method_10216(), targetPos.method_10214() + 1.0, targetPos.method_10215())).method_10264() >= 320 || trapdoorPos.method_10264() < -64)) {
                    if (this.isTrapdoor(trapdoorPos)) {
                        this.handleExistingTrapdoor(trapdoorPos);
                    } else {
                        if (this.completedPos != null && this.completedPos.equals((Object)trapdoorPos)) {
                            this.completedPos = null;
                        }
                        this.placeBlockOnTarget(trapdoorPos, targetPos);
                    }
                }
            }
        }
    }

    private boolean shouldPause() {
        if (Blink.INSTANCE != null && Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
            return true;
        }
        if (this.usingPause.getValue() && TrapdoorAura.mc.field_1724.method_6115()) {
            return true;
        }
        return this.pauseEat.getValue() && TrapdoorAura.mc.field_1724.method_6115();
    }

    private void handleExistingTrapdoor(class_2338 trapdoorPos) {
        class_2680 state = TrapdoorAura.mc.field_1687.method_8320(trapdoorPos);
        if (state.method_26204() instanceof class_2533) {
            boolean isOpen = Boolean.TRUE.equals(state.method_11654((class_2769)class_2533.field_11631));
            if (isOpen) {
                if (this.toggleTrapdoor(trapdoorPos, false)) {
                    this.completedPos = trapdoorPos;
                    this.timer.reset();
                }
                return;
            }
            if (this.completedPos != null && this.completedPos.equals((Object)trapdoorPos)) {
                return;
            }
            if (this.toggleTrapdoor(trapdoorPos, true)) {
                this.waitingClose = true;
                this.pendingClosePos = trapdoorPos;
                this.closeTimer.reset();
                this.timer.reset();
            }
        }
    }

    private void placeBlockOnTarget(class_2338 trapdoorPos, class_243 targetPos) {
        int slot;
        if (this.canPlaceBlock(trapdoorPos) && (slot = this.findSuitableSlot(trapdoorPos)) != -1) {
            int oldSlot = TrapdoorAura.mc.field_1724.method_31548().field_7545;
            this.selectSlot(slot, oldSlot);
            this.placeBlock(trapdoorPos, slot);
            this.restoreSlot(slot, oldSlot);
            class_2248 block = TrapdoorAura.mc.field_1687.method_8320(trapdoorPos).method_26204();
            if (block instanceof class_2533 && this.antiStandUp.getValue()) {
                this.waitingClose = true;
                this.pendingClosePos = trapdoorPos;
                this.closeTimer.reset();
            }
            this.timer.reset();
        }
    }

    private boolean canPlaceBlock(class_2338 pos) {
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        if (this.ghostHand.getValue() ? this.hasEntityIgnoringPlayers(pos) : BlockUtil.hasEntity(pos, false)) {
            return false;
        }
        if (this.placeRange.getValue() > 0.0 && mc.method_1560().method_19538().method_1022(pos.method_46558()) > this.placeRange.getValue()) {
            return false;
        }
        if (BlockUtil.getPlaceSide(pos, this.placeRange.getValue()) == null && !BlockUtil.allowAirPlace()) {
            return false;
        }
        class_2338 breakPos = SpeedMine.getBreakPos();
        return SpeedMine.INSTANCE == null || !SpeedMine.INSTANCE.isOn() || breakPos == null || !pos.equals((Object)breakPos);
    }

    private int findSuitableSlot(class_2338 targetPos) {
        if (this.useTrapdoors.getValue()) {
            int trapdoorSlot;
            int n = trapdoorSlot = this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(class_2533.class) : InventoryUtil.findClass(class_2533.class);
            if (trapdoorSlot != -1) {
                return trapdoorSlot;
            }
        }
        if (this.placeOtherBlocks.getValue()) {
            int slot;
            if (this.useWebs.getValue() && (slot = this.findBlockSlot(class_2246.field_10343)) != -1) {
                return slot;
            }
            if (this.useLadders.getValue() && (slot = this.findBlockSlot(class_2246.field_9983)) != -1) {
                return slot;
            }
            if (this.useVines.getValue() && (slot = this.findBlockSlot(class_2246.field_10597)) != -1) {
                return slot;
            }
            if (this.useScaffolds.getValue() && (slot = this.findBlockSlot(class_2246.field_16492)) != -1) {
                return slot;
            }
        }
        return -1;
    }

    private int findBlockSlot(class_2248 block) {
        return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(block) : InventoryUtil.findBlock(block);
    }

    private void placeBlock(class_2338 pos, int slot) {
        if (this.isTrapdoorItem(slot)) {
            BlockUtil.placeBlock(pos, this.rotate.getValue());
        } else {
            BlockUtil.placeBlock(pos, this.rotate.getValue());
        }
    }

    private boolean isTrapdoorItem(int slot) {
        if (slot >= 0 && slot < TrapdoorAura.mc.field_1724.method_31548().field_7547.size()) {
            class_1799 stack = (class_1799)TrapdoorAura.mc.field_1724.method_31548().field_7547.get(slot);
            if (!stack.method_7960() && stack.method_7909() instanceof class_1747) {
                class_2248 block = ((class_1747)stack.method_7909()).method_7711();
                return block instanceof class_2533;
            }
            return false;
        }
        return false;
    }

    private boolean toggleTrapdoor(class_2338 trapdoorPos, boolean desiredOpen) {
        class_2680 state = TrapdoorAura.mc.field_1687.method_8320(trapdoorPos);
        if (!(state.method_26204() instanceof class_2533)) {
            return false;
        }
        boolean isOpen = Boolean.TRUE.equals(state.method_11654((class_2769)class_2533.field_11631));
        if (isOpen == desiredOpen) {
            return false;
        }
        if (this.placeRange.getValue() > 0.0 && mc.method_1560().method_19538().method_1022(trapdoorPos.method_46558()) > this.placeRange.getValue()) {
            return false;
        }
        class_2350 clickSide = BlockUtil.getClickSide(trapdoorPos);
        if (clickSide == null) {
            return false;
        }
        if (this.movementFix.getValue() && this.rotate.getValue()) {
            class_243 hitVec = trapdoorPos.method_46558().method_1031((double)clickSide.method_10163().method_10263() * 0.5, (double)clickSide.method_10163().method_10264() * 0.5, (double)clickSide.method_10163().method_10260() * 0.5);
            Rotation rotation = RotationUtils.calculate(hitVec);
            MoveUtils.fixMovement(rotation.getYaw());
        }
        BlockUtil.clickBlock(trapdoorPos, clickSide, this.rotate.getValue());
        return true;
    }

    private boolean isTrapdoor(class_2338 pos) {
        return TrapdoorAura.mc.field_1687.method_8320(pos).method_26204() instanceof class_2533;
    }

    private boolean hasEntityIgnoringPlayers(class_2338 pos) {
        for (class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
            if (entity == null || !entity.method_5805() || entity instanceof class_1542 || entity instanceof class_1667 || entity instanceof class_1657) continue;
            return true;
        }
        return false;
    }

    private boolean isNaked(class_1657 player) {
        return player.method_31548().method_5438(0).method_7960() && player.method_31548().method_5438(1).method_7960() && player.method_31548().method_5438(2).method_7960() && player.method_31548().method_5438(3).method_7960();
    }

    private void selectSlot(int slot, int selectedSlot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, selectedSlot);
        } else if (this.silent.getValue()) {
            mc.method_1562().method_52787((class_2596)new class_2868(slot));
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private void restoreSlot(int slot, int selectedSlot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, selectedSlot);
            EntityUtil.syncInventory();
        } else if (this.silent.getValue()) {
            mc.method_1562().method_52787((class_2596)new class_2868(selectedSlot));
        } else {
            InventoryUtil.switchToSlot(selectedSlot);
        }
    }

    public class_1657 getCurrentTarget() {
        return this.currentTarget;
    }

    public String getStatus() {
        if (this.currentTarget == null) {
            return "\u7b49\u5f85\u76ee\u6807";
        }
        return this.waitingClose ? "\u5173\u95ed\u7b49\u5f85\u4e2d" : "\u6fc0\u6d3b\u4e2d";
    }

    static {
        TRAPDOOR_TYPES = new HashSet<Class<? extends class_2248>>();
        TRAPDOOR_TYPES.add(class_2533.class);
    }
}

