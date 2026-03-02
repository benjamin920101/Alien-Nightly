/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1511
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1810
 *  net.minecraft.class_1829
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2838
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.TotemEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.AutoCrystal;
import dev.luminous.mod.modules.settings.enums.Timing;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1829;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2838;
import net.minecraft.class_2846;

public class Offhand
extends Module {
    private final EnumSetting<OffhandItem> item = this.add(new EnumSetting<OffhandItem>("Item", OffhandItem.Totem));
    private final BooleanSetting safe = this.add(new BooleanSetting("Safe", true).setParent());
    private final SliderSetting safeHealth = this.add(new SliderSetting("Health", 16.0, 0.0, 36.0, 0.1, this.safe::isOpen));
    private final BooleanSetting lethalCrystal = this.add(new BooleanSetting("LethalCrystal", true, this.safe::isOpen));
    private final BooleanSetting gapSwitch = this.add(new BooleanSetting("GapSwitch", true).setParent());
    private final BooleanSetting always = this.add(new BooleanSetting("Always", false, this.gapSwitch::isOpen));
    private final BooleanSetting gapOnTotem = this.add(new BooleanSetting("Gap-Totem", false, this.gapSwitch::isOpen));
    private final BooleanSetting gapOnSword = this.add(new BooleanSetting("Gap-Sword", true, this.gapSwitch::isOpen));
    private final BooleanSetting gapOnPick = this.add(new BooleanSetting("Gap-Pickaxe", false, this.gapSwitch::isOpen));
    private final BooleanSetting mainHandTotem = this.add(new BooleanSetting("MainHandTotem", false).setParent());
    private final SliderSetting slot = this.add(new SliderSetting("Slot", 1.0, 1.0, 9.0, 1.0, this.mainHandTotem::isOpen));
    private final BooleanSetting forceUpdate = this.add(new BooleanSetting("ForceUpdate", false, this.mainHandTotem::isOpen));
    private final BooleanSetting withOffhand = this.add(new BooleanSetting("WithOffhand", false, this.mainHandTotem::isOpen));
    private final EnumSetting<SwapMode> swapMode = this.add(new EnumSetting<SwapMode>("SwapMode", SwapMode.OffhandSwap));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 50.0, 0.0, 500.0, 1.0));
    private final EnumSetting<Timing> timing = this.add(new EnumSetting<Timing>("Timing", Timing.All));
    private final Timer timer = new Timer();

    public Offhand() {
        super("Offhand", Module.Category.Combat);
        this.setChinese("\u526f\u624b\u7269\u54c1");
    }

    @EventListener
    public void totem(TotemEvent event) {
        if (event.getPlayer() == Offhand.mc.field_1724) {
            if (Offhand.mc.field_1724.method_6047().method_7909() == class_1802.field_8288) {
                Offhand.mc.field_1724.method_31548().method_5441(0);
            } else if (Offhand.mc.field_1724.method_6079().method_7909() == class_1802.field_8288) {
                Offhand.mc.field_1724.method_31548().field_7544.set(0, (Object)class_1799.field_8037);
            }
        }
    }

    private boolean lethalCrystal() {
        if (!this.lethalCrystal.getValue()) {
            return false;
        }
        for (class_1297 entity : Alien.THREAD.getEntities()) {
            if (!(entity instanceof class_1511) || !(Offhand.mc.field_1724.method_5739(entity) <= 12.0f)) continue;
            class_243 class_2432 = new class_243(entity.method_23317(), entity.method_23318(), entity.method_23321());
            if (!(AutoCrystal.INSTANCE.calculateDamage(class_2432, (class_1657)Offhand.mc.field_1724, (class_1657)Offhand.mc.field_1724) >= EntityUtil.getHealth((class_1297)Offhand.mc.field_1724))) continue;
            return true;
        }
        return false;
    }

    @EventListener
    public void onTick(ClientTickEvent event) {
        if (!(Offhand.nullCheck() || this.timing.is(Timing.Pre) && event.isPost() || this.timing.is(Timing.Post) && event.isPre() || !this.timer.passed(this.delay.getValueInt()) || !EntityUtil.inInventory())) {
            int hotBarSlot;
            int totemSlot;
            boolean unsafe;
            boolean switchMainHandTotem = Offhand.mc.field_1724.method_6079().method_7909() != class_1802.field_8288 || this.withOffhand.getValue();
            boolean bl = unsafe = (double)EntityUtil.getHealth((class_1297)Offhand.mc.field_1724) < this.safeHealth.getValue() || this.lethalCrystal();
            if (this.mainHandTotem.getValue() && (totemSlot = InventoryUtil.findItemInventorySlot(class_1802.field_8288)) != -1 && Offhand.mc.field_1724.method_31548().method_5438(this.slot.getValueInt() - 1).method_7909() != class_1802.field_8288) {
                switch (this.swapMode.getValue().ordinal()) {
                    case 0: {
                        Offhand.mc.field_1761.method_2906(Offhand.mc.field_1724.field_7512.field_7763, totemSlot, 0, class_1713.field_7790, (class_1657)Offhand.mc.field_1724);
                        Offhand.mc.field_1761.method_2906(Offhand.mc.field_1724.field_7512.field_7763, this.slot.getValueInt() - 1 + 36, 0, class_1713.field_7790, (class_1657)Offhand.mc.field_1724);
                        Offhand.mc.field_1761.method_2906(Offhand.mc.field_1724.field_7512.field_7763, totemSlot, 0, class_1713.field_7790, (class_1657)Offhand.mc.field_1724);
                        EntityUtil.syncInventory();
                        break;
                    }
                    case 1: {
                        Offhand.mc.field_1761.method_2906(Offhand.mc.field_1724.field_7512.field_7763, totemSlot, this.slot.getValueInt() - 1, class_1713.field_7791, (class_1657)Offhand.mc.field_1724);
                        EntityUtil.syncInventory();
                        break;
                    }
                    case 2: {
                        int old = Offhand.mc.field_1724.method_31548().field_7545;
                        InventoryUtil.switchToSlot(this.slot.getValueInt() - 1);
                        mc.method_1562().method_52787((class_2596)new class_2838(totemSlot));
                        InventoryUtil.switchToSlot(old);
                    }
                }
                if (switchMainHandTotem && (!this.safe.getValue() || unsafe) && (this.slot.getValueInt() - 1 != Offhand.mc.field_1724.method_31548().field_7545 || this.forceUpdate.getValue())) {
                    InventoryUtil.switchToSlot(this.slot.getValueInt() - 1);
                }
                this.timer.reset();
                return;
            }
            if (this.safe.getValue()) {
                if (unsafe) {
                    if (!this.mainHandTotem.getValue() || !switchMainHandTotem) {
                        this.swap(class_1802.field_8288);
                        this.timer.reset();
                        return;
                    }
                    hotBarSlot = InventoryUtil.findItem(class_1802.field_8288);
                    if (hotBarSlot != -1 && (hotBarSlot != Offhand.mc.field_1724.method_31548().field_7545 || this.forceUpdate.getValue())) {
                        InventoryUtil.switchToSlot(hotBarSlot);
                    }
                }
            } else if (this.mainHandTotem.getValue() && switchMainHandTotem && (hotBarSlot = InventoryUtil.findItem(class_1802.field_8288)) != -1 && (hotBarSlot != Offhand.mc.field_1724.method_31548().field_7545 || this.forceUpdate.getValue())) {
                InventoryUtil.switchToSlot(hotBarSlot);
            }
            if ((this.gapOnSword.getValue() && Offhand.mc.field_1724.method_6047().method_7909() instanceof class_1829 || this.always.getValue() && Offhand.mc.field_1724.method_6047().method_7909() != class_1802.field_8463 && Offhand.mc.field_1724.method_6047().method_7909() != class_1802.field_8367 || this.gapOnPick.getValue() && Offhand.mc.field_1724.method_6047().method_7909() instanceof class_1810 || this.gapOnTotem.getValue() && Offhand.mc.field_1724.method_6047().method_7909() == class_1802.field_8288) && Offhand.mc.field_1690.field_1904.method_1434() && this.gapSwitch.getValue()) {
                this.swap(class_1802.field_8463);
                this.timer.reset();
            } else {
                EnumSetting<OffhandItem> item = this.item;
                OffhandItem i = item.getValue();
                if (i == OffhandItem.Shield) {
                    this.swap(class_1802.field_8255);
                    this.timer.reset();
                } else if (i == OffhandItem.Chorus) {
                    this.swap(class_1802.field_8233);
                    this.timer.reset();
                } else if (i == OffhandItem.Crystal) {
                    this.swap(class_1802.field_8301);
                    this.timer.reset();
                } else if (i == OffhandItem.Totem) {
                    this.swap(class_1802.field_8288);
                    this.timer.reset();
                } else if (i == OffhandItem.Gapple) {
                    this.swap(class_1802.field_8463);
                    this.timer.reset();
                }
            }
        }
    }

    private void swap(class_1792 item) {
        int itemSlot;
        int n = itemSlot = item == class_1802.field_8463 ? this.getGAppleSlot() : this.findItemInventorySlot(item);
        if (itemSlot != -1) {
            switch (this.swapMode.getValue().ordinal()) {
                case 0: {
                    Offhand.mc.field_1761.method_2906(Offhand.mc.field_1724.field_7512.field_7763, itemSlot, 0, class_1713.field_7790, (class_1657)Offhand.mc.field_1724);
                    Offhand.mc.field_1761.method_2906(Offhand.mc.field_1724.field_7512.field_7763, 45, 0, class_1713.field_7790, (class_1657)Offhand.mc.field_1724);
                    Offhand.mc.field_1761.method_2906(Offhand.mc.field_1724.field_7512.field_7763, itemSlot, 0, class_1713.field_7790, (class_1657)Offhand.mc.field_1724);
                    EntityUtil.syncInventory();
                    break;
                }
                case 1: {
                    Offhand.mc.field_1761.method_2906(Offhand.mc.field_1724.field_7512.field_7763, itemSlot, 40, class_1713.field_7791, (class_1657)Offhand.mc.field_1724);
                    EntityUtil.syncInventory();
                    break;
                }
                case 2: {
                    mc.method_1562().method_52787((class_2596)new class_2838(itemSlot));
                    mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12969, new class_2338(0, 0, 0), class_2350.field_11033, 0));
                    mc.method_1562().method_52787((class_2596)new class_2838(itemSlot));
                }
            }
        }
    }

    private int getGAppleSlot() {
        return this.findItemInventorySlot(class_1802.field_8367) != -1 ? this.findItemInventorySlot(class_1802.field_8367) : this.findItemInventorySlot(class_1802.field_8463);
    }

    @Override
    public String getInfo() {
        return this.item.getValue().name();
    }

    public int findItemInventorySlot(class_1792 item) {
        if (Offhand.mc.field_1724.method_6079().method_7909() == class_1802.field_8463 && item == class_1802.field_8463) {
            return -1;
        }
        if (Offhand.mc.field_1724.method_6079().method_7909() != class_1802.field_8367 || item != class_1802.field_8463 && item != class_1802.field_8367) {
            if (item == Offhand.mc.field_1724.method_6079().method_7909()) {
                return -1;
            }
            switch (this.swapMode.getValue().ordinal()) {
                case 0: 
                case 1: {
                    for (int i = 44; i >= 0; --i) {
                        class_1799 stack = Offhand.mc.field_1724.method_31548().method_5438(i);
                        if (stack.method_7909() != item) continue;
                        return i < 9 ? i + 36 : i;
                    }
                    break;
                }
                case 2: {
                    class_1799 s;
                    for (int ix = 9; ix < Offhand.mc.field_1724.method_31548().method_5439() + 1; ++ix) {
                        s = Offhand.mc.field_1724.method_31548().method_5438(ix);
                        if (s.method_7909() != item) continue;
                        return ix;
                    }
                    for (int ixx = 0; ixx < 9; ++ixx) {
                        s = Offhand.mc.field_1724.method_31548().method_5438(ixx);
                        if (s.method_7909() != item) continue;
                        return ixx;
                    }
                    break;
                }
            }
            return -1;
        }
        return -1;
    }

    public static enum OffhandItem {
        None,
        Totem,
        Crystal,
        Gapple,
        Shield,
        Chorus;

    }

    public static enum SwapMode {
        ClickSlot,
        OffhandSwap,
        Pick;

    }
}

