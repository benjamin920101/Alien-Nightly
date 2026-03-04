/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.screen.ScreenHandler
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.screen.ShulkerBoxScreenHandler
 *  net.minecraft.screen.slot.Slot
 *  net.minecraft.item.BlockItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.component.type.PotionContentsComponent
 *  net.minecraft.block.BedBlock
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.block.ShulkerBoxBlock
 *  net.minecraft.block.PistonBlock
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen
 *  net.minecraft.component.DataComponentTypes
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.AntiRegear;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.component.DataComponentTypes;

public class AutoRegear
extends Module {
    public static AutoRegear INSTANCE;
    public final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    public final Timer timeoutTimer = new Timer();
    final int[] stealCountList = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
    private final SliderSetting disableTime = this.add(new SliderSetting("DisableTime", 500, 0, 1000));
    private final BooleanSetting place = this.add(new BooleanSetting("Place", true));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting preferOpen = this.add(new BooleanSetting("PerferOpen", true));
    private final BooleanSetting open = this.add(new BooleanSetting("Open", true));
    private final SliderSetting range = this.add(new SliderSetting("MaxRange", 4.0, 0.0, 6.0, 0.1));
    private final SliderSetting minRange = this.add(new SliderSetting("MinRange", 1.0, 0.0, 3.0, 0.1));
    private final BooleanSetting mine = this.add(new BooleanSetting("Mine", true));
    private final BooleanSetting take = this.add(new BooleanSetting("Take", true));
    private final BooleanSetting smart = this.add(new BooleanSetting("Smart", true, this.take::getValue).setParent());
    private final BooleanSetting forceMove = this.add(new BooleanSetting("ForceQuickMove", true, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting crystal = this.add(new SliderSetting("Crystal", 256, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting exp = this.add(new SliderSetting("Exp", 256, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting totem = this.add(new SliderSetting("Totem", 6, 0, 36, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting gapple = this.add(new SliderSetting("Gapple", 128, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting obsidian = this.add(new SliderSetting("Obsidian", 64, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting web = this.add(new SliderSetting("Web", 64, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting glowstone = this.add(new SliderSetting("Glowstone", 128, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting anchor = this.add(new SliderSetting("Anchor", 128, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting pearl = this.add(new SliderSetting("Pearl", 16, 0, 64, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting piston = this.add(new SliderSetting("Piston", 64, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting redstone = this.add(new SliderSetting("RedStone", 64, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting bed = this.add(new SliderSetting("Bed", 256, 0, 512, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting speed = this.add(new SliderSetting("Speed", 1, 0, 8, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting resistance = this.add(new SliderSetting("Resistance", 1, 0, 8, () -> this.take.getValue() && this.smart.isOpen()));
    private final SliderSetting strength = this.add(new SliderSetting("Strength", 1, 0, 8, () -> this.take.getValue() && this.smart.isOpen()));
    private final BindSetting placeKey = this.add(new BindSetting("PlaceKey", -1));
    private final Timer timer = new Timer();
    private final List<class_2338> openList = new ArrayList<class_2338>();
    public class_2338 placePos = null;
    private class_2338 openPos;
    private boolean opend = false;
    private boolean on = false;

    public AutoRegear() {
        super("AutoRegear", Module.Category.Combat);
        this.setChinese("\u81ea\u52a8\u8865\u7ed9");
        INSTANCE = this;
    }

    public int findShulker() {
        if (this.inventory.getValue()) {
            for (int i = 0; i < 36; ++i) {
                class_1747 blockItem;
                class_1792 class_17922;
                class_1799 stack = AutoRegear.mc.field_1724.method_31548().method_5438(i);
                if (stack.method_7960() || !((class_17922 = stack.method_7909()) instanceof class_1747) || !((blockItem = (class_1747)class_17922).method_7711() instanceof class_2480)) continue;
                return i < 9 ? i + 36 : i;
            }
            return -1;
        }
        return InventoryUtil.findClass(class_2480.class);
    }

    @Override
    public boolean onEnable() {
        this.opend = false;
        this.openPos = null;
        this.timeoutTimer.reset();
        this.placePos = null;
        if (!AutoRegear.nullCheck() && this.place.getValue()) {
            this.doPlace();
        }
        return false;
    }

    private void doPlace() {
        int oldSlot = AutoRegear.mc.field_1724.method_31548().field_7545;
        double distance = 100.0;
        class_2338 bestPos = null;
        for (class_2338 pos : BlockUtil.getSphere((float)this.range.getValue())) {
            if (!AutoRegear.mc.field_1687.method_22347(pos.method_10084())) continue;
            if (this.preferOpen.getValue() && AutoRegear.mc.field_1687.method_8320(pos).method_26204() instanceof class_2480) {
                return;
            }
            if ((double)class_3532.method_15355((float)((float)AutoRegear.mc.field_1724.method_5707(pos.method_46558()))) < this.minRange.getValue() || !BlockUtil.clientCanPlace(pos, false) || !BlockUtil.isStrictDirection(pos.method_10093(class_2350.field_11033), class_2350.field_11036) || !BlockUtil.canClick(pos.method_10093(class_2350.field_11033)) || bestPos != null && !((double)class_3532.method_15355((float)((float)AutoRegear.mc.field_1724.method_5707(pos.method_46558()))) < distance)) continue;
            distance = class_3532.method_15355((float)((float)AutoRegear.mc.field_1724.method_5707(pos.method_46558())));
            bestPos = pos;
        }
        if (bestPos != null) {
            if (this.findShulker() == -1) {
                this.sendMessage("\u00a74No shulkerbox found.");
                return;
            }
            if (this.inventory.getValue()) {
                int slot = this.findShulker();
                InventoryUtil.inventorySwap(slot, oldSlot);
                this.placeBlock(bestPos);
                this.placePos = bestPos;
                InventoryUtil.inventorySwap(slot, oldSlot);
            } else {
                InventoryUtil.switchToSlot(this.findShulker());
                this.placeBlock(bestPos);
                this.placePos = bestPos;
                InventoryUtil.switchToSlot(oldSlot);
            }
            this.timer.reset();
        } else {
            this.sendMessage("\u00a74No place position found.");
        }
    }

    private void update() {
        this.stealCountList[0] = (int)(this.crystal.getValue() - (double)InventoryUtil.getItemCount(class_1802.field_8301));
        this.stealCountList[1] = (int)(this.exp.getValue() - (double)InventoryUtil.getItemCount(class_1802.field_8287));
        this.stealCountList[2] = (int)(this.totem.getValue() - (double)InventoryUtil.getItemCount(class_1802.field_8288));
        this.stealCountList[3] = (int)(this.gapple.getValue() - (double)InventoryUtil.getItemCount(class_1802.field_8367));
        this.stealCountList[4] = (int)(this.obsidian.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_10540.method_8389()));
        this.stealCountList[5] = (int)(this.web.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_10343.method_8389()));
        this.stealCountList[6] = (int)(this.glowstone.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_10171.method_8389()));
        this.stealCountList[7] = (int)(this.anchor.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_23152.method_8389()));
        this.stealCountList[8] = (int)(this.pearl.getValue() - (double)InventoryUtil.getItemCount(class_1802.field_8634));
        this.stealCountList[9] = (int)(this.piston.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_10560.method_8389()) - (double)InventoryUtil.getItemCount(class_2246.field_10615.method_8389()));
        this.stealCountList[10] = (int)(this.redstone.getValue() - (double)InventoryUtil.getItemCount(class_2246.field_10002.method_8389()));
        this.stealCountList[11] = (int)(this.bed.getValue() - (double)InventoryUtil.getItemCount(class_2244.class));
        this.stealCountList[12] = (int)(this.speed.getValue() - (double)InventoryUtil.getPotionCount((class_1291)class_1294.field_5904.comp_349()));
        this.stealCountList[13] = (int)(this.resistance.getValue() - (double)InventoryUtil.getPotionCount((class_1291)class_1294.field_5907.comp_349()));
        this.stealCountList[14] = (int)(this.strength.getValue() - (double)InventoryUtil.getPotionCount((class_1291)class_1294.field_5910.comp_349()));
    }

    @Override
    public void onDisable() {
        this.opend = false;
        if (this.mine.getValue() && this.placePos != null) {
            SpeedMine.INSTANCE.mine(this.placePos);
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.smart.getValue()) {
            this.update();
        }
        if (this.placeKey.isPressed() && AutoRegear.mc.field_1755 == null) {
            if (!this.on) {
                this.opend = false;
                this.openPos = null;
                this.timeoutTimer.reset();
                this.placePos = null;
                this.doPlace();
            }
            this.on = true;
        } else {
            this.on = false;
        }
        this.openList.removeIf(pos -> !(AutoRegear.mc.field_1687.method_8320(pos).method_26204() instanceof class_2480));
        if (!(AutoRegear.mc.field_1755 instanceof class_495)) {
            if (this.opend) {
                this.opend = false;
                if (this.autoDisable.getValue()) {
                    this.timeoutToDisable();
                }
                if (this.mine.getValue() && this.openPos != null) {
                    if (AutoRegear.mc.field_1687.method_8320(this.openPos).method_26204() instanceof class_2480) {
                        SpeedMine.INSTANCE.mine(this.openPos);
                    } else {
                        this.openPos = null;
                    }
                }
            } else if (this.open.getValue()) {
                if (this.placePos == null || !((double)class_3532.method_15355((float)((float)AutoRegear.mc.field_1724.method_5707(this.placePos.method_46558()))) <= this.range.getValue()) || !AutoRegear.mc.field_1687.method_22347(this.placePos.method_10084()) || this.timer.passed(500L) && !(AutoRegear.mc.field_1687.method_8320(this.placePos).method_26204() instanceof class_2480)) {
                    boolean found = false;
                    for (class_2338 pos2 : BlockUtil.getSphere((float)this.range.getValue())) {
                        if (this.openList.contains(pos2) || !AutoRegear.mc.field_1687.method_22347(pos2.method_10084()) && !BlockUtil.canReplace(pos2.method_10084()) || !(AutoRegear.mc.field_1687.method_8320(pos2).method_26204() instanceof class_2480)) continue;
                        this.openPos = pos2;
                        BlockUtil.clickBlock(pos2, BlockUtil.getClickSide(pos2), this.rotate.getValue());
                        found = true;
                        break;
                    }
                    if (!found && this.autoDisable.getValue()) {
                        this.timeoutToDisable();
                    }
                } else if (AutoRegear.mc.field_1687.method_8320(this.placePos).method_26204() instanceof class_2480) {
                    this.openPos = this.placePos;
                    BlockUtil.clickBlock(this.placePos, BlockUtil.getClickSide(this.placePos), this.rotate.getValue());
                }
            } else if (!this.take.getValue() && this.autoDisable.getValue()) {
                this.timeoutToDisable();
            }
        } else {
            this.opend = true;
            if (this.openPos != null) {
                this.openList.add(this.openPos);
            }
            if (!this.take.getValue()) {
                if (this.autoDisable.getValue()) {
                    this.timeoutToDisable();
                }
            } else {
                boolean take = false;
                class_1703 class_17032 = AutoRegear.mc.field_1724.field_7512;
                if (class_17032 instanceof class_1733) {
                    class_1733 shulker = (class_1733)class_17032;
                    block1: for (class_1735 slot : shulker.field_7761) {
                        if (slot.field_7874 >= 27 || slot.method_7677().method_7960()) continue;
                        Type type = this.needSteal(slot.method_7677());
                        if (this.smart.getValue() && type != Type.QuickMove && (type != Type.Stack || !this.forceMove.getValue())) {
                            if (type != Type.Stack) continue;
                            for (int slot1 = 0; slot1 < 36; ++slot1) {
                                class_1799 stack = AutoRegear.mc.field_1724.method_31548().method_5438(slot1);
                                if (stack.method_7960() || !stack.method_7946() || stack.method_7909() != slot.method_7677().method_7909() || stack.method_7947() >= stack.method_7914()) continue;
                                int i = (slot1 < 9 ? slot1 + 36 : slot1) + 18;
                                AutoRegear.mc.field_1761.method_2906(shulker.field_7763, slot.field_7874, 0, class_1713.field_7790, (class_1657)AutoRegear.mc.field_1724);
                                AutoRegear.mc.field_1761.method_2906(shulker.field_7763, i, 0, class_1713.field_7790, (class_1657)AutoRegear.mc.field_1724);
                                AutoRegear.mc.field_1761.method_2906(shulker.field_7763, slot.field_7874, 0, class_1713.field_7790, (class_1657)AutoRegear.mc.field_1724);
                                take = true;
                                continue block1;
                            }
                            continue;
                        }
                        AutoRegear.mc.field_1761.method_2906(shulker.field_7763, slot.field_7874, 0, class_1713.field_7794, (class_1657)AutoRegear.mc.field_1724);
                        take = true;
                    }
                }
                if (this.autoDisable.getValue() && !take) {
                    this.timeoutToDisable();
                }
            }
        }
    }

    private void timeoutToDisable() {
        if (this.timeoutTimer.passed(this.disableTime.getValueInt())) {
            this.disable();
        }
    }

    private Type needSteal(class_1799 i) {
        if (i.method_7909().equals(class_1802.field_8301) && this.stealCountList[0] > 0) {
            this.stealCountList[0] = this.stealCountList[0] - i.method_7947();
            return this.stealCountList[0] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909().equals(class_1802.field_8287) && this.stealCountList[1] > 0) {
            this.stealCountList[1] = this.stealCountList[1] - i.method_7947();
            return this.stealCountList[1] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909().equals(class_1802.field_8288) && this.stealCountList[2] > 0) {
            this.stealCountList[2] = this.stealCountList[2] - i.method_7947();
            return this.stealCountList[2] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909().equals(class_1802.field_8367) && this.stealCountList[3] > 0) {
            this.stealCountList[3] = this.stealCountList[3] - i.method_7947();
            return this.stealCountList[3] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909().equals(class_2246.field_10540.method_8389()) && this.stealCountList[4] > 0) {
            this.stealCountList[4] = this.stealCountList[4] - i.method_7947();
            return this.stealCountList[4] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909().equals(class_2246.field_10343.method_8389()) && this.stealCountList[5] > 0) {
            this.stealCountList[5] = this.stealCountList[5] - i.method_7947();
            return this.stealCountList[5] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909().equals(class_2246.field_10171.method_8389()) && this.stealCountList[6] > 0) {
            this.stealCountList[6] = this.stealCountList[6] - i.method_7947();
            return this.stealCountList[6] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909().equals(class_2246.field_23152.method_8389()) && this.stealCountList[7] > 0) {
            this.stealCountList[7] = this.stealCountList[7] - i.method_7947();
            return this.stealCountList[7] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909().equals(class_1802.field_8634) && this.stealCountList[8] > 0) {
            this.stealCountList[8] = this.stealCountList[8] - i.method_7947();
            return this.stealCountList[8] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2665 && this.stealCountList[9] > 0) {
            this.stealCountList[9] = this.stealCountList[9] - i.method_7947();
            return this.stealCountList[9] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909().equals(class_2246.field_10002.method_8389()) && this.stealCountList[10] > 0) {
            this.stealCountList[10] = this.stealCountList[10] - i.method_7947();
            return this.stealCountList[10] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2244 && this.stealCountList[11] > 0) {
            this.stealCountList[11] = this.stealCountList[11] - i.method_7947();
            return this.stealCountList[11] < 0 ? Type.Stack : Type.QuickMove;
        }
        if (class_1792.method_7880((class_1792)i.method_7909()) == class_1792.method_7880((class_1792)class_1802.field_8436)) {
            class_1844 potionContentsComponent = (class_1844)i.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
            for (class_1293 effect : potionContentsComponent.method_57397()) {
                if (effect.method_5579().comp_349() == class_1294.field_5904.comp_349()) {
                    if (this.stealCountList[12] <= 0) continue;
                    this.stealCountList[12] = this.stealCountList[12] - i.method_7947();
                    if (this.stealCountList[12] < 0) {
                        return Type.Stack;
                    }
                    return Type.QuickMove;
                }
                if (effect.method_5579().comp_349() == class_1294.field_5907.comp_349()) {
                    if (this.stealCountList[13] <= 0) continue;
                    this.stealCountList[13] = this.stealCountList[13] - i.method_7947();
                    if (this.stealCountList[13] < 0) {
                        return Type.Stack;
                    }
                    return Type.QuickMove;
                }
                if (effect.method_5579().comp_349() != class_1294.field_5910.comp_349() || this.stealCountList[14] <= 0) continue;
                this.stealCountList[14] = this.stealCountList[14] - i.method_7947();
                if (this.stealCountList[14] < 0) {
                    return Type.Stack;
                }
                return Type.QuickMove;
            }
        }
        return Type.None;
    }

    private void placeBlock(class_2338 pos) {
        AntiRegear.INSTANCE.safe.add(pos);
        BlockUtil.clickBlock(pos.method_10093(class_2350.field_11033), class_2350.field_11036, this.rotate.getValue());
    }

    private static enum Type {
        None,
        Stack,
        QuickMove;

    }
}

