/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.enchantment.Enchantments
 *  net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
 *  net.minecraft.component.type.ItemEnchantmentsComponent
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.util.Hand;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.component.type.ItemEnchantmentsComponent;

public class AutoXP
extends Module {
    public static AutoXP INSTANCE;
    public final BooleanSetting rotation = this.add(new BooleanSetting("Rotation", true).setParent());
    private final BooleanSetting instant = this.add(new BooleanSetting("Instant", false, this.rotation::isOpen));
    public final BooleanSetting onlyBroken = this.add(new BooleanSetting("OnlyBroken", true));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 3, 0, 5));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    public final EnumSetting<SwingSide> interactSwing = this.add(new EnumSetting<SwingSide>("InteractSwing", SwingSide.All));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true));
    public final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
    private final Timer delayTimer = new Timer();
    boolean lookDown = false;
    int exp = 0;
    private boolean throwing = false;

    public AutoXP() {
        super("AutoXP", Module.Category.Combat);
        this.setChinese("\u81ea\u52a8\u7ecf\u9a8c\u74f6");
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.throwing = false;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.throwing = this.checkThrow();
        if (!this.inventory.getValue() || EntityUtil.inInventory()) {
            if (this.lookDown && this.isThrow() && this.delayTimer.passed((long)this.delay.getValueInt() * 20L) && (!this.onlyGround.getValue() || AutoXP.mc.field_1724.method_24828())) {
                this.exp = InventoryUtil.getItemCount(class_1802.field_8287) - 1;
                if (this.rotation.getValue() && this.instant.getValue()) {
                    Alien.ROTATION.snapAt(Alien.ROTATION.rotationYaw, 88.0f);
                }
                this.throwExp();
                if (this.rotation.getValue() && this.instant.getValue()) {
                    Alien.ROTATION.snapBack();
                }
            }
            if (this.autoDisable.getValue() && !this.isThrow()) {
                this.disable();
            }
        }
    }

    @Override
    public boolean onEnable() {
        boolean bl = this.lookDown = !this.rotation.getValue() || this.instant.getValue();
        if (AutoXP.nullCheck()) {
            this.disable();
        } else {
            this.exp = InventoryUtil.getItemCount(class_1802.field_8287);
        }
        return false;
    }

    @Override
    public String getInfo() {
        return String.valueOf(this.exp);
    }

    public void throwExp() {
        int newSlot;
        int oldSlot = AutoXP.mc.field_1724.method_31548().field_7545;
        if (this.inventory.getValue() && (newSlot = InventoryUtil.findItemInventorySlotFromZero(class_1802.field_8287)) != -1) {
            InventoryUtil.inventorySwap(newSlot, AutoXP.mc.field_1724.method_31548().field_7545);
            AutoXP.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
            EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
            InventoryUtil.inventorySwap(newSlot, AutoXP.mc.field_1724.method_31548().field_7545);
            EntityUtil.syncInventory();
            this.delayTimer.reset();
        } else {
            newSlot = InventoryUtil.findItem(class_1802.field_8287);
            if (newSlot != -1) {
                InventoryUtil.switchToSlot(newSlot);
                AutoXP.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
                InventoryUtil.switchToSlot(oldSlot);
                this.delayTimer.reset();
            }
        }
    }

    @EventListener(priority=-200)
    public void RotateEvent(UpdateRotateEvent event) {
        if (this.rotation.getValue() && !this.instant.getValue() && this.isThrow()) {
            event.setPitch(88.0f);
            this.lookDown = true;
        }
    }

    public boolean isThrow() {
        return this.throwing;
    }

    public boolean checkThrow() {
        if (this.isOff()) {
            return false;
        }
        if (AutoXP.mc.field_1755 != null) {
            return false;
        }
        if (this.usingPause.getValue() && AutoXP.mc.field_1724.method_6115()) {
            return false;
        }
        if (InventoryUtil.findItem(class_1802.field_8287) != -1 || this.inventory.getValue() && InventoryUtil.findItemInventorySlotFromZero(class_1802.field_8287) != -1) {
            if (this.onlyBroken.getValue()) {
                for (class_1799 armor : AutoXP.mc.field_1724.method_31548().field_7548) {
                    if (armor.method_7960() || EntityUtil.getDamagePercent(armor) >= 100) continue;
                    class_9304 enchants = class_1890.method_57532((class_1799)armor);
                    return enchants.method_57534().contains(AutoXP.mc.field_1687.method_30349().method_46762(class_1893.field_9101.method_58273()).method_46746(class_1893.field_9101).get());
                }
                return false;
            }
            return true;
        }
        return false;
    }
}

