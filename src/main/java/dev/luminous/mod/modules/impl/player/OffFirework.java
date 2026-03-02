/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1802
 *  net.minecraft.class_2886
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1802;
import net.minecraft.class_2886;

public class OffFirework
extends Module {
    public static OffFirework INSTANCE;
    public final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));

    public OffFirework() {
        super("OffFirework", Module.Category.Player);
        this.setChinese("\u653e\u70df\u82b1");
        INSTANCE = this;
    }

    @Override
    public boolean onEnable() {
        if (OffFirework.nullCheck()) {
            this.disable();
        }
        this.off();
        this.disable();
        return false;
    }

    public void off() {
        int firework;
        ElytraFly.INSTANCE.fireworkTimer.reset();
        if (OffFirework.mc.field_1724.method_6047().method_7909() == class_1802.field_8639) {
            OffFirework.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, OffFirework.mc.field_1724.method_36454(), OffFirework.mc.field_1724.method_36455()));
        } else if (this.inventory.getValue() && (firework = InventoryUtil.findItemInventorySlot(class_1802.field_8639)) != -1) {
            InventoryUtil.inventorySwap(firework, OffFirework.mc.field_1724.method_31548().field_7545);
            OffFirework.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, OffFirework.mc.field_1724.method_36454(), OffFirework.mc.field_1724.method_36455()));
            InventoryUtil.inventorySwap(firework, OffFirework.mc.field_1724.method_31548().field_7545);
            EntityUtil.syncInventory();
        } else {
            firework = InventoryUtil.findItem(class_1802.field_8639);
            if (firework != -1) {
                int old = OffFirework.mc.field_1724.method_31548().field_7545;
                InventoryUtil.switchToSlot(firework);
                OffFirework.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, OffFirework.mc.field_1724.method_36454(), OffFirework.mc.field_1724.method_36455()));
                InventoryUtil.switchToSlot(old);
            }
        }
    }
}

