/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1303
 *  net.minecraft.class_1542
 *  net.minecraft.class_1657
 *  net.minecraft.class_1667
 *  net.minecraft.class_1683
 *  net.minecraft.class_2269
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.move.MoveUtils;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.rotation.Rotation;
import dev.luminous.api.utils.rotation.RotationUtils;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1303;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1667;
import net.minecraft.class_1683;
import net.minecraft.class_2269;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;

public class AutoButton
extends Module {
    public static AutoButton INSTANCE;
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 8.0).setSuffix("m"));
    private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 4.5, 0.0, 6.0).setSuffix("m"));
    private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting ghostHand = this.add(new BooleanSetting("GhostHand", true));
    private final BooleanSetting movementFix = this.add(new BooleanSetting("MovementFix", true));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final BooleanSetting pauseEat = this.add(new BooleanSetting("PauseEat", true));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", false));
    private final Timer timer = new Timer();

    public AutoButton() {
        super("AutoButton", Module.Category.Combat);
        this.setChinese("\u81ea\u52a8\u6309\u94ae");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (!this.timer.passed((long)this.delay.getValue())) {
            return;
        }
        if (this.inventorySwap.getValue() && !EntityUtil.inInventory()) {
            return;
        }
        class_1657 target = CombatUtil.getClosestEnemy(this.range.getValue());
        if (target == null) {
            return;
        }
        class_2338 pos = EntityUtil.getEntityPos((class_1297)target, true);
        if (pos.method_10264() >= 320) {
            return;
        }
        if (this.shouldSkipPlace(pos)) {
            return;
        }
        int buttonSlot = this.getButtonSlot();
        if (buttonSlot == -1) {
            return;
        }
        int oldSlot = AutoButton.mc.field_1724.method_31548().field_7545;
        this.doSwap(buttonSlot);
        if (this.movementFix.getValue()) {
            class_2350 side;
            class_2350 class_23502 = side = BlockUtil.allowAirPlace() ? BlockUtil.getClickSide(pos) : BlockUtil.getPlaceSide(pos, this.placeRange.getValue());
            if (side != null) {
                class_243 hitVec = pos.method_46558().method_1031((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5);
                Rotation rotation = RotationUtils.calculate(hitVec);
                MoveUtils.fixMovement(rotation.getYaw());
            }
        }
        BlockUtil.placeBlock(pos, true);
        if (this.inventorySwap.getValue()) {
            this.doSwap(buttonSlot);
            EntityUtil.syncInventory();
        } else {
            this.doSwap(oldSlot);
        }
        this.timer.reset();
        if (!this.shouldPause() && this.timer.passed((long)this.delay.getValue()) && (target = CombatUtil.getClosestEnemy(this.range.getValue())) == null && this.autoDisable.getValue()) {
            this.disable();
        }
    }

    private boolean shouldSkipPlace(class_2338 pos) {
        if (!BlockUtil.canReplace(pos)) {
            return true;
        }
        if (this.ghostHand.getValue() ? this.hasEntityIgnoringPlayers(pos) : BlockUtil.hasEntity(pos, false)) {
            return true;
        }
        if (this.placeRange.getValue() > 0.0 && AutoButton.mc.field_1724.method_33571().method_1022(pos.method_46558()) > this.placeRange.getValue()) {
            return true;
        }
        return BlockUtil.getPlaceSide(pos, this.placeRange.getValue()) == null && !BlockUtil.allowAirPlace();
    }

    private boolean hasEntityIgnoringPlayers(class_2338 pos) {
        for (class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
            if (entity == null || !entity.method_5805() || entity instanceof class_1542 || entity instanceof class_1303 || entity instanceof class_1683 || entity instanceof class_1667 || entity instanceof class_1657) continue;
            return true;
        }
        return false;
    }

    private void doSwap(int slot) {
        if (this.inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, AutoButton.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getButtonSlot() {
        if (this.inventorySwap.getValue()) {
            return InventoryUtil.findClassInventorySlot(class_2269.class);
        }
        return InventoryUtil.findClass(class_2269.class);
    }

    private boolean shouldPause() {
        if (Blink.INSTANCE != null && Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
            return true;
        }
        if (this.usingPause.getValue() && AutoButton.mc.field_1724.method_6115()) {
            return true;
        }
        return this.pauseEat.getValue() && AutoButton.mc.field_1724.method_6115();
    }
}

