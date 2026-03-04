/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.impl.movement.Velocity;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import dev.luminous.mod.modules.settings.enums.Timing;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.util.Hand;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;

public class KeyPearl
extends Module {
    public static KeyPearl INSTANCE;
    public static boolean throwing;
    public final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final EnumSetting<Timing> timing = this.add(new EnumSetting<Timing>("Timing", Timing.All));
    public final EnumSetting<SwingSide> interactSwing = this.add(new EnumSetting<SwingSide>("Swing", SwingSide.All));
    private final BooleanSetting rotation = this.add(new BooleanSetting("Rotation", true));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false).setParent());
    private final BooleanSetting whenElytra = this.add(new BooleanSetting("FallFlying", true, this.yawStep::isOpen));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, this.yawStep::isOpen));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 20.0, 0.0, 360.0, 0.1, this.yawStep::isOpen));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 100, 0, 100, this.yawStep::isOpen));
    private final BooleanSetting sync = this.add(new BooleanSetting("Sync", true, this.yawStep::isOpen));

    public KeyPearl() {
        super("KeyPearl", Module.Category.Player);
        this.setChinese("\u6254\u73cd\u73e0");
        INSTANCE = this;
    }

    @Override
    public boolean onEnable() {
        if (KeyPearl.nullCheck()) {
            this.disable();
        } else {
            if (KeyPearl.INSTANCE.inventory.getValue()) {
                if (InventoryUtil.findItemInventorySlotFromZero(class_1802.field_8634) == -1) {
                    this.disable();
                    return false;
                }
            } else if (InventoryUtil.findItem(class_1802.field_8634) == -1) {
                this.disable();
                return false;
            }
            if (!(this.shouldYawStep() || this.inventory.getValue() && !EntityUtil.inInventory() || Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue())) {
                this.throwPearl(KeyPearl.mc.field_1724.method_36454(), KeyPearl.mc.field_1724.method_36455());
                this.disable();
            }
        }
        return false;
    }

    @EventListener
    public void onUpdate(TickEvent event) {
        if (!(KeyPearl.nullCheck() || this.timing.is(Timing.Pre) && event.isPost() || this.timing.is(Timing.Post) && event.isPre() || Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue())) {
            if (!this.shouldYawStep()) {
                this.throwPearl(KeyPearl.mc.field_1724.method_36454(), KeyPearl.mc.field_1724.method_36455());
                this.disable();
            } else if (Alien.ROTATION.inFov(KeyPearl.mc.field_1724.method_36454(), KeyPearl.mc.field_1724.method_36455(), this.fov.getValueFloat())) {
                if (this.sync.getValue()) {
                    this.throwPearl(KeyPearl.mc.field_1724.method_36454(), KeyPearl.mc.field_1724.method_36455());
                } else {
                    int pearl;
                    throwing = true;
                    if (KeyPearl.mc.field_1724.method_6047().method_7909() == class_1802.field_8634) {
                        KeyPearl.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                        EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
                    } else if (this.inventory.getValue() && (pearl = InventoryUtil.findItemInventorySlotFromZero(class_1802.field_8634)) != -1) {
                        InventoryUtil.inventorySwap(pearl, KeyPearl.mc.field_1724.method_31548().field_7545);
                        KeyPearl.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                        EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
                        InventoryUtil.inventorySwap(pearl, KeyPearl.mc.field_1724.method_31548().field_7545);
                        EntityUtil.syncInventory();
                    } else {
                        pearl = InventoryUtil.findItem(class_1802.field_8634);
                        if (pearl != -1) {
                            int old = KeyPearl.mc.field_1724.method_31548().field_7545;
                            InventoryUtil.switchToSlot(pearl);
                            KeyPearl.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                            EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
                            InventoryUtil.switchToSlot(old);
                        }
                    }
                    throwing = false;
                }
                this.disable();
            }
        }
    }

    @EventListener
    public void onRotate(RotationEvent event) {
        if (this.shouldYawStep()) {
            event.setRotation(KeyPearl.mc.field_1724.method_36454(), KeyPearl.mc.field_1724.method_36455(), this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    private boolean shouldYawStep() {
        return this.whenElytra.getValue() || !KeyPearl.mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() && !Velocity.INSTANCE.noRotation() : false;
    }

    public void throwPearl(float yaw, float pitch) {
        int pearl;
        throwing = true;
        if (KeyPearl.mc.field_1724.method_6047().method_7909() == class_1802.field_8634) {
            if (this.rotation.getValue()) {
                Alien.ROTATION.snapAt(yaw, pitch);
            }
            KeyPearl.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, yaw, pitch));
            EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
            if (this.rotation.getValue()) {
                Alien.ROTATION.snapBack();
            }
        } else if (this.inventory.getValue() && (pearl = InventoryUtil.findItemInventorySlotFromZero(class_1802.field_8634)) != -1) {
            InventoryUtil.inventorySwap(pearl, KeyPearl.mc.field_1724.method_31548().field_7545);
            if (this.rotation.getValue()) {
                Alien.ROTATION.snapAt(yaw, pitch);
            }
            KeyPearl.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, yaw, pitch));
            EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
            InventoryUtil.inventorySwap(pearl, KeyPearl.mc.field_1724.method_31548().field_7545);
            EntityUtil.syncInventory();
            if (this.rotation.getValue()) {
                Alien.ROTATION.snapBack();
            }
        } else {
            pearl = InventoryUtil.findItem(class_1802.field_8634);
            if (pearl != -1) {
                int old = KeyPearl.mc.field_1724.method_31548().field_7545;
                InventoryUtil.switchToSlot(pearl);
                if (this.rotation.getValue()) {
                    Alien.ROTATION.snapAt(yaw, pitch);
                }
                KeyPearl.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, yaw, pitch));
                EntityUtil.swingHand(class_1268.field_5808, this.interactSwing.getValue());
                InventoryUtil.switchToSlot(old);
                if (this.rotation.getValue()) {
                    Alien.ROTATION.snapBack();
                }
            }
        }
        throwing = false;
    }

    static {
        throwing = false;
    }
}

