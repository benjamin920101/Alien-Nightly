/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.item.ArmorItem
 *  net.minecraft.item.ElytraItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.enchantment.Enchantments
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.component.type.ItemEnchantmentsComponent
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.Hand;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.component.type.ItemEnchantmentsComponent;

public class AutoArmor
extends Module {
    public static AutoArmor INSTANCE;
    private final EnumSetting<EnchantPriority> head = this.add(new EnumSetting<EnchantPriority>("Head", EnchantPriority.Protection));
    private final EnumSetting<EnchantPriority> body = this.add(new EnumSetting<EnchantPriority>("Body", EnchantPriority.Protection));
    private final EnumSetting<EnchantPriority> tights = this.add(new EnumSetting<EnchantPriority>("Tights", EnchantPriority.Protection));
    private final EnumSetting<EnchantPriority> feet = this.add(new EnumSetting<EnchantPriority>("Feet", EnchantPriority.Protection));
    private final BooleanSetting ignoreCurse = this.add(new BooleanSetting("IgnoreCurse", true));
    private final BooleanSetting noMove = this.add(new BooleanSetting("NoMove", false));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 3.0, 0.0, 10.0, 1.0));
    private final BooleanSetting autoElytra = this.add(new BooleanSetting("AutoElytra", true));
    private final EnumSetting<HotbarSwapMode> hotbarSwap = this.add(new EnumSetting<HotbarSwapMode>("HotbarSwap", HotbarSwapMode.Swap));
    private final EnumSetting<InventorySwapMode> inventorySwap = this.add(new EnumSetting<InventorySwapMode>("InventorySwap", InventorySwapMode.ClickSlot));
    private int tickDelay = 0;

    public AutoArmor() {
        super("AutoArmor", Module.Category.Player);
        this.setChinese("\u81ea\u52a8\u7a7f\u7532");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (!(!EntityUtil.inInventory() || AutoArmor.mc.field_1724.field_7498 != AutoArmor.mc.field_1724.field_7512 || MovementUtil.isMoving() && this.noMove.getValue())) {
            if (this.tickDelay > 0) {
                --this.tickDelay;
            } else {
                this.tickDelay = this.delay.getValueInt();
                HashMap<class_1304, int[]> armorMap = new HashMap<class_1304, int[]>(4);
                armorMap.put(class_1304.field_6166, new int[]{36, this.getProtection(AutoArmor.mc.field_1724.method_31548().method_5438(36)), -1, -1});
                armorMap.put(class_1304.field_6172, new int[]{37, this.getProtection(AutoArmor.mc.field_1724.method_31548().method_5438(37)), -1, -1});
                armorMap.put(class_1304.field_6174, new int[]{38, this.getProtection(AutoArmor.mc.field_1724.method_31548().method_5438(38)), -1, -1});
                armorMap.put(class_1304.field_6169, new int[]{39, this.getProtection(AutoArmor.mc.field_1724.method_31548().method_5438(39)), -1, -1});
                for (int s = 0; s < 36; ++s) {
                    if (!(AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909() instanceof class_1738) && AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909() != class_1802.field_8833 || AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909() == class_1802.field_8833 && (ElytraFly.INSTANCE.isOff() && this.autoElytra.getValue() || ElytraFly.INSTANCE.isOn() && ElytraFly.INSTANCE.packet.getValue())) continue;
                    int protection = this.getProtection(AutoArmor.mc.field_1724.method_31548().method_5438(s));
                    class_1304 slot = AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909() instanceof class_1770 ? class_1304.field_6174 : ((class_1738)AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909()).method_7685();
                    for (Map.Entry e : armorMap.entrySet()) {
                        if (this.autoElytra.getValue() && ElytraFly.INSTANCE.isOn() && e.getKey() == class_1304.field_6174) {
                            if (!AutoArmor.mc.field_1724.method_31548().method_5438(38).method_7960() && AutoArmor.mc.field_1724.method_31548().method_5438(38).method_7909() instanceof class_1770 && class_1770.method_7804((class_1799)AutoArmor.mc.field_1724.method_31548().method_5438(38)) || ((int[])e.getValue())[2] != -1 && !AutoArmor.mc.field_1724.method_31548().method_5438(((int[])e.getValue())[2]).method_7960() && AutoArmor.mc.field_1724.method_31548().method_5438(((int[])e.getValue())[2]).method_7909() instanceof class_1770 && class_1770.method_7804((class_1799)AutoArmor.mc.field_1724.method_31548().method_5438(((int[])e.getValue())[2])) || AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7960() || !(AutoArmor.mc.field_1724.method_31548().method_5438(s).method_7909() instanceof class_1770) || !class_1770.method_7804((class_1799)AutoArmor.mc.field_1724.method_31548().method_5438(s))) continue;
                            ((int[])e.getValue())[2] = s;
                            continue;
                        }
                        if (protection <= 0 || e.getKey() != slot || protection <= ((int[])e.getValue())[1] || protection <= ((int[])e.getValue())[3]) continue;
                        ((int[])e.getValue())[2] = s;
                        ((int[])e.getValue())[3] = protection;
                    }
                }
                for (Map.Entry equipmentSlotEntry : armorMap.entrySet()) {
                    if (((int[])equipmentSlotEntry.getValue())[2] == -1) continue;
                    if (((int[])equipmentSlotEntry.getValue())[2] < 9) {
                        switch (this.hotbarSwap.getValue().ordinal()) {
                            case 0: {
                                int armorSlot = 44 - ((int[])equipmentSlotEntry.getValue())[0];
                                AutoArmor.mc.field_1761.method_2906(AutoArmor.mc.field_1724.field_7512.field_7763, armorSlot, ((int[])equipmentSlotEntry.getValue())[2], class_1713.field_7791, (class_1657)AutoArmor.mc.field_1724);
                                EntityUtil.syncInventory();
                                break;
                            }
                            case 1: {
                                int old = AutoArmor.mc.field_1724.method_31548().field_7545;
                                InventoryUtil.switchToSlot(((int[])equipmentSlotEntry.getValue())[2]);
                                AutoArmor.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                                InventoryUtil.switchToSlot(old);
                            }
                        }
                    } else if (AutoArmor.mc.field_1724.field_7498 == AutoArmor.mc.field_1724.field_7512) {
                        int armorSlot = 44 - ((int[])equipmentSlotEntry.getValue())[0];
                        int newArmorSlot = ((int[])equipmentSlotEntry.getValue())[2];
                        switch (this.inventorySwap.getValue().ordinal()) {
                            case 0: {
                                AutoArmor.mc.field_1761.method_2906(AutoArmor.mc.field_1724.field_7512.field_7763, newArmorSlot, 0, class_1713.field_7790, (class_1657)AutoArmor.mc.field_1724);
                                AutoArmor.mc.field_1761.method_2906(AutoArmor.mc.field_1724.field_7512.field_7763, armorSlot, 0, class_1713.field_7790, (class_1657)AutoArmor.mc.field_1724);
                                if (((int[])equipmentSlotEntry.getValue())[1] == -1) break;
                                AutoArmor.mc.field_1761.method_2906(AutoArmor.mc.field_1724.field_7512.field_7763, newArmorSlot, 0, class_1713.field_7790, (class_1657)AutoArmor.mc.field_1724);
                                break;
                            }
                            case 1: {
                                mc.method_1562().method_52787((class_2596)new class_2838(newArmorSlot));
                                AutoArmor.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                                mc.method_1562().method_52787((class_2596)new class_2838(newArmorSlot));
                            }
                        }
                        EntityUtil.syncInventory();
                    }
                    return;
                }
            }
        }
    }

    private int getProtection(class_1799 is) {
        if (is.method_7909() instanceof class_1738 || is.method_7909() == class_1802.field_8833) {
            class_1304 slot;
            int prot = 0;
            class_1792 class_17922 = is.method_7909();
            if (class_17922 instanceof class_1738) {
                class_1738 ai = (class_1738)class_17922;
                v0 = ai.method_7685();
            } else {
                v0 = slot = class_1304.field_48824;
            }
            if (is.method_7909() instanceof class_1770) {
                if (!class_1770.method_7804((class_1799)is)) {
                    return 0;
                }
                prot = 1;
            }
            int blastMultiplier = 1;
            int protectionMultiplier = 1;
            switch (slot) {
                case field_6169: {
                    if (this.head.is(EnchantPriority.Protection)) {
                        protectionMultiplier *= 2;
                        break;
                    }
                    blastMultiplier *= 2;
                    break;
                }
                case field_48824: {
                    if (this.body.is(EnchantPriority.Protection)) {
                        protectionMultiplier *= 2;
                        break;
                    }
                    blastMultiplier *= 2;
                    break;
                }
                case field_6172: {
                    if (this.tights.is(EnchantPriority.Protection)) {
                        protectionMultiplier *= 2;
                        break;
                    }
                    blastMultiplier *= 2;
                    break;
                }
                case field_6166: {
                    if (this.feet.is(EnchantPriority.Protection)) {
                        protectionMultiplier *= 2;
                        break;
                    }
                    blastMultiplier *= 2;
                }
            }
            if (is.method_7942()) {
                class_9304 enchants = class_1890.method_57532((class_1799)is);
                if (enchants.method_57534().contains(AutoArmor.mc.field_1687.method_30349().method_46762(class_1893.field_9111.method_58273()).method_46746(class_1893.field_9111).get())) {
                    prot += enchants.method_57536((class_6880)AutoArmor.mc.field_1687.method_30349().method_46762(class_1893.field_9111.method_58273()).method_46746(class_1893.field_9111).get()) * protectionMultiplier;
                }
                if (enchants.method_57534().contains(AutoArmor.mc.field_1687.method_30349().method_46762(class_1893.field_9107.method_58273()).method_46746(class_1893.field_9107).get())) {
                    prot += enchants.method_57536((class_6880)AutoArmor.mc.field_1687.method_30349().method_46762(class_1893.field_9107.method_58273()).method_46746(class_1893.field_9107).get()) * blastMultiplier;
                }
                if (enchants.method_57534().contains(AutoArmor.mc.field_1687.method_30349().method_46762(class_1893.field_9107.method_58273()).method_46746(class_1893.field_9113).get()) && this.ignoreCurse.getValue()) {
                    prot = -999;
                }
            }
            return (is.method_7909() instanceof class_1738 ? ((class_1738)is.method_7909()).method_7687() : 0) + prot;
        }
        return !is.method_7960() ? 0 : -1;
    }

    private static enum EnchantPriority {
        Blast,
        Protection;

    }

    public static enum HotbarSwapMode {
        Swap,
        Switch;

    }

    public static enum InventorySwapMode {
        ClickSlot,
        Pick;

    }
}

