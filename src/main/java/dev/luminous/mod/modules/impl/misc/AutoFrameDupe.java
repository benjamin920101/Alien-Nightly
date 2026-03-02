/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1533
 *  net.minecraft.class_1657
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1533;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;

public class AutoFrameDupe
extends Module {
    SliderSetting range = this.add(new SliderSetting("Range", 1.0, 5.0, 7.0));
    SliderSetting turns = this.add(new SliderSetting("Turns", 1, 5, 10));
    SliderSetting ticks = this.add(new SliderSetting("Ticks", 1, 5, 10));
    BooleanSetting switchxd = this.add(new BooleanSetting("Switch", true));
    private int timeoutTicks = 0;

    public AutoFrameDupe() {
        super("AutoFrameDupe", "Automatically dupes using frames", Module.Category.Misc);
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (AutoFrameDupe.mc.field_1687 == null || AutoFrameDupe.mc.field_1724 == null || AutoFrameDupe.mc.field_1761 == null) {
            return;
        }
        for (class_1297 entity : AutoFrameDupe.mc.field_1687.method_18112()) {
            class_1533 frame;
            if (!(entity instanceof class_1533) || !((double)AutoFrameDupe.mc.field_1724.method_5739((class_1297)(frame = (class_1533)entity)) <= this.range.getValue())) continue;
            if ((double)this.timeoutTicks >= this.ticks.getValue()) {
                int shulkerSlot;
                boolean isHolding;
                class_1799 displayedItem = frame.method_6940();
                boolean hasItem = !displayedItem.method_7960();
                boolean bl = isHolding = !AutoFrameDupe.mc.field_1724.method_6047().method_7960();
                if (!(!this.switchxd.getValue() || isHolding && this.isShulkerBox(AutoFrameDupe.mc.field_1724.method_6047()) || (shulkerSlot = this.findShulkers()) == -1)) {
                    AutoFrameDupe.mc.field_1724.method_31548().field_7545 = shulkerSlot;
                    isHolding = true;
                }
                if (!hasItem && isHolding) {
                    AutoFrameDupe.mc.field_1761.method_2905((class_1657)AutoFrameDupe.mc.field_1724, (class_1297)frame, class_1268.field_5808);
                }
                if (hasItem) {
                    int i = 0;
                    while ((double)i < this.turns.getValue()) {
                        AutoFrameDupe.mc.field_1761.method_2905((class_1657)AutoFrameDupe.mc.field_1724, (class_1297)frame, class_1268.field_5808);
                        ++i;
                    }
                    AutoFrameDupe.mc.field_1761.method_2918((class_1657)AutoFrameDupe.mc.field_1724, (class_1297)frame);
                }
                this.timeoutTicks = 0;
                continue;
            }
            ++this.timeoutTicks;
        }
    }

    private boolean isShulkerBox(class_1799 stack) {
        class_1792 item = stack.method_7909();
        return item == class_1802.field_8545 || item == class_1802.field_8722 || item == class_1802.field_8380 || item == class_1802.field_8050 || item == class_1802.field_8829 || item == class_1802.field_8271 || item == class_1802.field_8548 || item == class_1802.field_8520 || item == class_1802.field_8627 || item == class_1802.field_8451 || item == class_1802.field_8213 || item == class_1802.field_8816 || item == class_1802.field_8350 || item == class_1802.field_8584 || item == class_1802.field_8461 || item == class_1802.field_8676 || item == class_1802.field_8268;
    }

    private int findShulkers() {
        for (int i = 0; i < 9; ++i) {
            class_1799 stack = AutoFrameDupe.mc.field_1724.method_31548().method_5438(i);
            if (!this.isShulkerBox(stack)) continue;
            return i;
        }
        return -1;
    }
}

