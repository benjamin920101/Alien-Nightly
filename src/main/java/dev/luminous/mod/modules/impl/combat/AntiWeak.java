/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1294
 *  net.minecraft.class_1511
 *  net.minecraft.class_1829
 *  net.minecraft.class_2596
 *  net.minecraft.class_2824
 *  net.minecraft.class_2824$class_5907
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.Criticals;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1294;
import net.minecraft.class_1511;
import net.minecraft.class_1829;
import net.minecraft.class_2596;
import net.minecraft.class_2824;

public class AntiWeak
extends Module {
    private final EnumSetting<SwapMode> swapMode = this.add(new EnumSetting<SwapMode>("SwapMode", SwapMode.Inventory));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
    private final BooleanSetting onlyCrystal = this.add(new BooleanSetting("OnlyCrystal", true));
    private final Timer delayTimer = new Timer();
    boolean ignore = false;
    private class_2824 lastPacket = null;

    public AntiWeak() {
        super("AntiWeak", Module.Category.Combat);
        this.setChinese("\u53cd\u865a\u5f31");
    }

    @Override
    public String getInfo() {
        return this.swapMode.getValue().name();
    }

    @EventListener(priority=200)
    public void onPacketSend(PacketEvent.Send event) {
        class_2824 packet;
        class_2596<?> class_25962;
        if (!AntiWeak.nullCheck() && !event.isCancelled() && !this.ignore && AntiWeak.mc.field_1724.method_6112(class_1294.field_5911) != null && !(AntiWeak.mc.field_1724.method_6047().method_7909() instanceof class_1829) && this.delayTimer.passedMs(this.delay.getValue()) && (class_25962 = event.getPacket()) instanceof class_2824 && Criticals.getInteractType(packet = (class_2824)class_25962) == class_2824.class_5907.field_29172) {
            if (this.onlyCrystal.getValue() && !(Criticals.getEntity(packet) instanceof class_1511)) {
                return;
            }
            this.lastPacket = packet;
            this.delayTimer.reset();
            this.ignore = true;
            this.doAnti();
            this.ignore = false;
            event.cancel();
        }
    }

    private void doAnti() {
        int strong;
        if (this.lastPacket != null && (strong = this.swapMode.getValue() != SwapMode.Inventory ? InventoryUtil.findClass(class_1829.class) : InventoryUtil.findClassInventorySlot(class_1829.class)) != -1) {
            int old = AntiWeak.mc.field_1724.method_31548().field_7545;
            if (this.swapMode.getValue() != SwapMode.Inventory) {
                InventoryUtil.switchToSlot(strong);
            } else {
                InventoryUtil.inventorySwap(strong, AntiWeak.mc.field_1724.method_31548().field_7545);
            }
            mc.method_1562().method_52787((class_2596)this.lastPacket);
            if (this.swapMode.getValue() != SwapMode.Inventory) {
                if (this.swapMode.getValue() != SwapMode.Normal) {
                    InventoryUtil.switchToSlot(old);
                }
            } else {
                InventoryUtil.inventorySwap(strong, AntiWeak.mc.field_1724.method_31548().field_7545);
                EntityUtil.syncInventory();
            }
        }
    }

    public static enum SwapMode {
        Normal,
        Silent,
        Inventory;

    }
}

