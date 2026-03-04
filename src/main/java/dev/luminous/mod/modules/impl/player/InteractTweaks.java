/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Items
 *  net.minecraft.item.PickaxeItem
 *  net.minecraft.item.SwordItem
 *  net.minecraft.block.AnvilBlock
 *  net.minecraft.block.Block
 *  net.minecraft.block.ChestBlock
 *  net.minecraft.block.EnderChestBlock
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
 *  net.minecraft.client.gui.screen.DeathScreen
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.client.gui.screen.DeathScreen;

public class InteractTweaks
extends Module {
    public static InteractTweaks INSTANCE;
    public final BooleanSetting noEntityTrace = this.add(new BooleanSetting("NoEntityTrace", true).setParent());
    public final BooleanSetting onlyPickaxe = this.add(new BooleanSetting("OnlyPickaxe", true, this.noEntityTrace::isOpen));
    public final BooleanSetting multiTask = this.add(new BooleanSetting("MultiTask", true));
    public final BooleanSetting respawn = this.add(new BooleanSetting("Respawn", true));
    public final BooleanSetting ghostHand = this.add(new BooleanSetting("IgnoreBedrock", false));
    private final BooleanSetting noAbort = this.add(new BooleanSetting("NoMineAbort", false));
    private final BooleanSetting noReset = this.add(new BooleanSetting("NoMineReset", false));
    private final BooleanSetting noDelay = this.add(new BooleanSetting("NoMineDelay", false));
    private final BooleanSetting noInteract = this.add(new BooleanSetting("NoInteract", false));
    private final BooleanSetting pickaxeSwitch = this.add(new BooleanSetting("SwitchEat", false).setParent());
    private final BooleanSetting allowSword = this.add(new BooleanSetting("Sword", true, this.pickaxeSwitch::isOpen));
    private final BooleanSetting allowPickaxe = this.add(new BooleanSetting("Pickaxe", true, this.pickaxeSwitch::isOpen));
    private final BooleanSetting reach = this.add(new BooleanSetting("Reach", false));
    public final SliderSetting blockRange = this.add(new SliderSetting("BlockRange", 5.0, 0.0, 15.0, 0.1, this.reach::getValue));
    public final SliderSetting entityRange = this.add(new SliderSetting("EntityRange", 5.0, 0.0, 15.0, 0.1, this.reach::getValue));
    private final SliderSetting delay = this.add(new SliderSetting("UseDelay", 4.0, 0.0, 4.0, 1.0));
    public boolean isActive;
    boolean swapped = false;
    int lastSlot = 0;

    public InteractTweaks() {
        super("InteractTweaks", Module.Category.Player);
        this.setChinese("\u4ea4\u4e92\u8c03\u6574");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.respawn.getValue() && InteractTweaks.mc.field_1755 instanceof class_418) {
            InteractTweaks.mc.field_1724.method_7331();
            mc.method_1507(null);
        }
        if (InteractTweaks.mc.field_1752 <= 4 - this.delay.getValueInt()) {
            InteractTweaks.mc.field_1752 = 0;
        }
        if (this.pickaxeSwitch.getValue()) {
            if (!(InteractTweaks.mc.field_1724.method_6047().method_7909() instanceof class_1810 && this.allowPickaxe.getValue() || InteractTweaks.mc.field_1724.method_6047().method_7909() instanceof class_1829 && this.allowSword.getValue() || InteractTweaks.mc.field_1724.method_6047().method_7909() == class_1802.field_8367 || InteractTweaks.mc.field_1724.method_6047().method_7909() == class_1802.field_8463)) {
                this.swapped = false;
                return;
            }
            int gapple = InventoryUtil.findItem(class_1802.field_8367);
            if (gapple == -1) {
                gapple = InventoryUtil.findItem(class_1802.field_8463);
            }
            if (gapple == -1) {
                if (this.swapped) {
                    InventoryUtil.switchToSlot(this.lastSlot);
                    this.swapped = false;
                }
                return;
            }
            if (InteractTweaks.mc.field_1690.field_1904.method_1434()) {
                if ((InteractTweaks.mc.field_1724.method_6047().method_7909() instanceof class_1810 && this.allowPickaxe.getValue() || InteractTweaks.mc.field_1724.method_6047().method_7909() instanceof class_1829 && this.allowSword.getValue()) && InteractTweaks.mc.field_1724.method_6079().method_7909() != class_1802.field_8367 && InteractTweaks.mc.field_1724.method_6047().method_7909() != class_1802.field_8463) {
                    this.lastSlot = InteractTweaks.mc.field_1724.method_31548().field_7545;
                    InventoryUtil.switchToSlot(gapple);
                    this.swapped = true;
                }
            } else if (this.swapped) {
                InventoryUtil.switchToSlot(this.lastSlot);
                this.swapped = false;
            }
        }
    }

    @EventListener
    public void onPacket(PacketEvent.Send event) {
        class_2596<?> class_25962;
        if (!InteractTweaks.nullCheck() && this.noInteract.getValue() && (class_25962 = event.getPacket()) instanceof class_2885) {
            class_2885 packet = (class_2885)class_25962;
            class_2248 var4 = InteractTweaks.mc.field_1687.method_8320(packet.method_12543().method_17777()).method_26204();
            if (!InteractTweaks.mc.field_1724.method_5715() && (var4 instanceof class_2281 || var4 instanceof class_2336 || var4 instanceof class_2199)) {
                event.cancel();
            }
        }
    }

    @Override
    public void onDisable() {
        this.isActive = false;
    }

    public boolean reach() {
        return this.isOn() && this.reach.getValue();
    }

    public boolean noAbort() {
        return this.isOn() && this.noAbort.getValue() && !InteractTweaks.mc.field_1690.field_1904.method_1434();
    }

    public boolean noReset() {
        return this.isOn() && this.noReset.getValue();
    }

    public boolean noDelay() {
        return this.isOn() && this.noDelay.getValue();
    }

    public boolean multiTask() {
        return this.isOn() && this.multiTask.getValue();
    }

    public boolean noEntityTrace() {
        if (this.isOff() || !this.noEntityTrace.getValue()) {
            return false;
        }
        return !this.onlyPickaxe.getValue() ? true : InteractTweaks.mc.field_1724.method_6047().method_7909() instanceof class_1810 || InteractTweaks.mc.field_1724.method_6115() && !(InteractTweaks.mc.field_1724.method_6047().method_7909() instanceof class_1829);
    }

    public boolean ghostHand() {
        return this.isOn() && this.ghostHand.getValue() && !InteractTweaks.mc.field_1690.field_1904.method_1434() && !InteractTweaks.mc.field_1690.field_1832.method_1434();
    }
}

