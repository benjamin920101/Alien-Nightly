/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class DropShulkerBox
extends Module {
    private final Timer timer = new Timer();
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 100.0, 0.0, 1000.0, 10.0).setSuffix("ms"));
    private final BooleanSetting checkInventory = this.add(new BooleanSetting("CheckInventory", true));
    private final BooleanSetting checkHotbar = this.add(new BooleanSetting("CheckHotbar", true));
    private final BooleanSetting checkOffhand = this.add(new BooleanSetting("CheckOffhand", true));
    private final BooleanSetting dropFullShulkers = this.add(new BooleanSetting("DropFullShulkers", false));
    private final BooleanSetting onlyWhenEmpty = this.add(new BooleanSetting("OnlyWhenEmpty", false));

    public DropShulkerBox() {
        super("DropShulkerBox", Module.Category.Misc);
        this.setChinese("\u4e22\u5f03\u6f5c\u5f71\u76d2");
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        class_1799 stack;
        class_1799 stack2;
        int i;
        if (DropShulkerBox.mc.field_1724 == null || DropShulkerBox.mc.field_1687 == null) {
            return;
        }
        if (!this.timer.passedMs(this.delay.getValueInt())) {
            return;
        }
        if (this.checkInventory.getValue()) {
            for (i = 9; i < 36; ++i) {
                stack2 = DropShulkerBox.mc.field_1724.method_31548().method_5438(i);
                if (!this.shouldDropShulker(stack2)) continue;
                this.dropItem(i);
                this.timer.reset();
                return;
            }
        }
        if (this.checkHotbar.getValue()) {
            for (i = 0; i < 9; ++i) {
                stack2 = DropShulkerBox.mc.field_1724.method_31548().method_5438(i);
                if (!this.shouldDropShulker(stack2)) continue;
                this.dropItem(i < 9 ? i + 36 : i);
                this.timer.reset();
                return;
            }
        }
        if (this.checkOffhand.getValue() && this.shouldDropShulker(stack = DropShulkerBox.mc.field_1724.method_6079())) {
            this.dropItem(45);
            this.timer.reset();
            return;
        }
    }

    private boolean shouldDropShulker(class_1799 stack) {
        if (stack.method_7960() || stack.method_7909() != class_1802.field_8545) {
            return false;
        }
        return false;
    }

    private void dropItem(int slot) {
        int syncId = DropShulkerBox.mc.field_1724.field_7512.field_7763;
        DropShulkerBox.mc.field_1761.method_2906(syncId, slot, 1, class_1713.field_7795, (class_1657)DropShulkerBox.mc.field_1724);
    }
}

