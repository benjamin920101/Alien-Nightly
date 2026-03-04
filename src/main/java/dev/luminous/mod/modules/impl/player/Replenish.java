/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.item.ItemStack
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.Sorter;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ItemStack;

public class Replenish
extends Module {
    private final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.QuickMove));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 2.0, 0.0, 5.0, 0.01).setSuffix("s"));
    private final SliderSetting min = this.add(new SliderSetting("Min", 50, 1, 100)).setSuffix("%");
    private final SliderSetting forceDelay = this.add(new SliderSetting("ForceDelay", 0.2, 0.0, 4.0, 0.01).setSuffix("s"));
    private final SliderSetting forceMin = this.add(new SliderSetting("ForceMin", 16, 1, 100)).setSuffix("%");
    private final Timer timer = new Timer();

    public Replenish() {
        super("Replenish", Module.Category.Player);
        this.setChinese("\u7269\u54c1\u680f\u8865\u5145");
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        for (int i = 0; i < 9; ++i) {
            if (!this.replenish(i)) continue;
            this.timer.reset();
            return;
        }
    }

    private boolean replenish(int slot) {
        class_1799 stack = Replenish.mc.field_1724.method_31548().method_5438(slot);
        if (stack.method_7960()) {
            return false;
        }
        if (!stack.method_7946()) {
            return false;
        }
        int percent = (int)((double)stack.method_7947() / (double)stack.method_7914() * 100.0);
        if ((double)percent > this.min.getValue()) {
            return false;
        }
        for (int i = 9; i < 36; ++i) {
            class_1799 item = Replenish.mc.field_1724.method_31548().method_5438(i);
            if (item.method_7960() || !Sorter.canMerge(stack, item)) continue;
            if ((float)percent > this.forceMin.getValueFloat() ? !this.timer.passedS(this.delay.getValue()) : !this.timer.passedS(this.forceDelay.getValue())) {
                return false;
            }
            switch (this.mode.getValue().ordinal()) {
                case 0: {
                    Replenish.mc.field_1761.method_2906(Replenish.mc.field_1724.field_7498.field_7763, i, 0, class_1713.field_7794, (class_1657)Replenish.mc.field_1724);
                    break;
                }
                case 1: {
                    Replenish.mc.field_1761.method_2906(Replenish.mc.field_1724.field_7498.field_7763, i, 0, class_1713.field_7790, (class_1657)Replenish.mc.field_1724);
                    Replenish.mc.field_1761.method_2906(Replenish.mc.field_1724.field_7498.field_7763, slot + 36, 0, class_1713.field_7790, (class_1657)Replenish.mc.field_1724);
                    Replenish.mc.field_1761.method_2906(Replenish.mc.field_1724.field_7498.field_7763, i, 0, class_1713.field_7790, (class_1657)Replenish.mc.field_1724);
                }
            }
            return true;
        }
        return false;
    }

    public static enum Mode {
        QuickMove,
        ClickSlot;

    }
}

