/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2596
 *  net.minecraft.class_2793
 *  net.minecraft.class_2799
 *  net.minecraft.class_2813
 *  net.minecraft.class_2815
 *  net.minecraft.class_2824
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_2828$class_2830
 *  net.minecraft.class_2828$class_2831
 *  net.minecraft.class_2828$class_5911
 *  net.minecraft.class_2833
 *  net.minecraft.class_2838
 *  net.minecraft.class_2846
 *  net.minecraft.class_2848
 *  net.minecraft.class_2868
 *  net.minecraft.class_2879
 *  net.minecraft.class_2885
 *  net.minecraft.class_2886
 *  net.minecraft.class_3965
 *  net.minecraft.class_6374
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.EntityVelocityUpdateEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.Criticals;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.class_2596;
import net.minecraft.class_2793;
import net.minecraft.class_2799;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import net.minecraft.class_2833;
import net.minecraft.class_2838;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.minecraft.class_3965;
import net.minecraft.class_6374;

public class PacketLogger
extends Module {
    public static PacketLogger INSTANCE;
    private final BooleanSetting moveFull = this.add(new BooleanSetting("MoveFull", true));
    private final BooleanSetting movePos = this.add(new BooleanSetting("MovePosition", true));
    private final BooleanSetting moveLook = this.add(new BooleanSetting("MoveLook", true));
    private final BooleanSetting moveGround = this.add(new BooleanSetting("MoveGround", true));
    private final BooleanSetting vehicleMove = this.add(new BooleanSetting("VehicleMove", true));
    private final BooleanSetting playerAction = this.add(new BooleanSetting("PlayerAction", true));
    private final BooleanSetting updateSlot = this.add(new BooleanSetting("UpdateSlot", true));
    private final BooleanSetting handSwing = this.add(new BooleanSetting("HandSwing", true));
    private final BooleanSetting pong = this.add(new BooleanSetting("Pong", true));
    private final BooleanSetting interactEntity = this.add(new BooleanSetting("InteractEntity", true));
    private final BooleanSetting interactBlock = this.add(new BooleanSetting("InteractBlock", true));
    private final BooleanSetting interactItem = this.add(new BooleanSetting("InteractItem", true));
    private final BooleanSetting closeScreen = this.add(new BooleanSetting("CloseScreen", true));
    private final BooleanSetting command = this.add(new BooleanSetting("ClientCommand", true));
    private final BooleanSetting status = this.add(new BooleanSetting("ClientStatus", true));
    private final BooleanSetting clickSlot = this.add(new BooleanSetting("ClickSlot", true));
    private final BooleanSetting pickInventory = this.add(new BooleanSetting("PickInventory", true));
    private final BooleanSetting teleportConfirm = this.add(new BooleanSetting("TeleportConfirm", true));
    private final BooleanSetting s2cVelocity = this.add(new BooleanSetting("S2cVelocity", true));

    public PacketLogger() {
        super("PacketLogger", Module.Category.Misc);
        this.setChinese("\u6570\u636e\u5305\u8bb0\u5f55");
        INSTANCE = this;
    }

    private void logPacket(String msg, Object ... args) {
        String s = String.format(msg, args);
        CommandManager.sendMessage(s);
    }

    @EventListener(priority=999999)
    public void velocity(EntityVelocityUpdateEvent event) {
        if (this.s2cVelocity.getValue() && event.getEntity() == PacketLogger.mc.field_1724) {
            this.logPacket("S2C Velocity, x: %s, y: %s, z: %s, isExplosion: %s", event.getX(), event.getY(), event.getZ(), event.isExplosion());
        }
    }

    @EventListener(priority=-999999)
    public void onPacketSend(PacketEvent.Send event) {
        Object s;
        Object builderx;
        Object builder;
        class_2828.class_2830 packet;
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2828.class_2830) {
            packet = (class_2828.class_2830)class_25962;
            if (this.moveFull.getValue()) {
                builder = new StringBuilder();
                ((StringBuilder)builder).append("PlayerMove Full - ");
                if (packet.method_36171()) {
                    ((StringBuilder)builder).append("x: ").append(packet.method_12269(0.0)).append(", y: ").append(packet.method_12268(0.0)).append(", z: ").append(packet.method_12274(0.0)).append(" ");
                }
                if (packet.method_36172()) {
                    ((StringBuilder)builder).append("yaw: ").append(packet.method_12271(0.0f)).append(", pitch: ").append(packet.method_12270(0.0f)).append(" ");
                }
                ((StringBuilder)builder).append(" onground: ").append(packet.method_12273());
                this.logPacket(((StringBuilder)builder).toString(), new Object[0]);
            }
        }
        if ((builder = event.getPacket()) instanceof class_2828.class_2829) {
            packet = (class_2828.class_2829)builder;
            if (this.movePos.getValue()) {
                builderx = new StringBuilder();
                ((StringBuilder)builderx).append("PlayerMove PosGround - ");
                if (packet.method_36171()) {
                    ((StringBuilder)builderx).append("x: ").append(packet.method_12269(0.0)).append(", y: ").append(packet.method_12268(0.0)).append(", z: ").append(packet.method_12274(0.0)).append(" ");
                }
                ((StringBuilder)builderx).append(" onground: ").append(packet.method_12273());
                this.logPacket(((StringBuilder)builderx).toString(), new Object[0]);
            }
        }
        if ((builderx = event.getPacket()) instanceof class_2828.class_2831) {
            packet = (class_2828.class_2831)builderx;
            if (this.moveLook.getValue()) {
                builderx = new StringBuilder();
                ((StringBuilder)builderx).append("PlayerMove LookGround - ");
                if (packet.method_36172()) {
                    ((StringBuilder)builderx).append("yaw: ").append(packet.method_12271(0.0f)).append(", pitch: ").append(packet.method_12270(0.0f)).append(" ");
                }
                ((StringBuilder)builderx).append(" onground: ").append(packet.method_12273());
                this.logPacket(((StringBuilder)builderx).toString(), new Object[0]);
            }
        }
        if ((builderx = event.getPacket()) instanceof class_2828.class_5911) {
            packet = (class_2828.class_5911)builderx;
            if (this.moveGround.getValue()) {
                s = "PlayerMove Ground - onground: " + packet.method_12273();
                this.logPacket((String)s, new Object[0]);
            }
        }
        if ((s = event.getPacket()) instanceof class_2833) {
            packet = (class_2833)s;
            if (this.vehicleMove.getValue()) {
                this.logPacket("VehicleMove - x: %s, y: %s, z: %s, yaw: %s, pitch: %s", packet.method_12279(), packet.method_12280(), packet.method_12276(), Float.valueOf(packet.method_12281()), Float.valueOf(packet.method_12277()));
            }
        }
        if ((s = event.getPacket()) instanceof class_2846) {
            packet = (class_2846)s;
            if (this.playerAction.getValue() && packet.method_12360() != null) {
                this.logPacket("PlayerAction - action: %s, direction: %s, pos: %s", packet.method_12363().name(), packet.method_12360().name(), packet.method_12362().method_23854());
            }
        }
        if ((s = event.getPacket()) instanceof class_2868) {
            packet = (class_2868)s;
            if (this.updateSlot.getValue()) {
                this.logPacket("UpdateSlot - slot: %d", packet.method_12442());
            }
        }
        if ((s = event.getPacket()) instanceof class_2879) {
            packet = (class_2879)s;
            if (this.handSwing.getValue()) {
                this.logPacket("HandSwing - hand: %s", packet.method_12512().name());
            }
        }
        if ((s = event.getPacket()) instanceof class_6374) {
            packet = (class_6374)s;
            if (this.pong.getValue()) {
                this.logPacket("Pong - %d", packet.method_36960());
            }
        }
        if ((s = event.getPacket()) instanceof class_2824) {
            packet = (class_2824)s;
            if (this.interactEntity.getValue()) {
                this.logPacket("InteractEntity - Entity: %s, id: %s", Criticals.getEntity((class_2824)packet).method_5477().getString(), Criticals.getEntity((class_2824)packet).method_5628());
            }
        }
        if ((s = event.getPacket()) instanceof class_2885) {
            packet = (class_2885)s;
            if (this.interactBlock.getValue()) {
                class_3965 blockHitResult = packet.method_12543();
                this.logPacket("InteractBlock - pos: %s, dir: %s, hand: %s", blockHitResult.method_17777().method_23854(), blockHitResult.method_17780().name(), packet.method_12546().name());
            }
        }
        if ((class_25962 = event.getPacket()) instanceof class_2886) {
            packet = (class_2886)class_25962;
            if (this.interactItem.getValue()) {
                this.logPacket("InteractItem - hand: %s", packet.method_12551().name());
            }
        }
        if ((class_25962 = event.getPacket()) instanceof class_2815) {
            packet = (class_2815)class_25962;
            if (this.closeScreen.getValue()) {
                this.logPacket("CloseScreen - id: %s", packet.method_36168());
            }
        }
        if ((class_25962 = event.getPacket()) instanceof class_2848) {
            packet = (class_2848)class_25962;
            if (this.command.getValue()) {
                this.logPacket("ClientCommand - mode: %s", packet.method_12365().name());
            }
        }
        if ((class_25962 = event.getPacket()) instanceof class_2799) {
            packet = (class_2799)class_25962;
            if (this.status.getValue()) {
                this.logPacket("ClientStatus - mode: %s", packet.method_12119().name());
            }
        }
        if ((class_25962 = event.getPacket()) instanceof class_2813) {
            packet = (class_2813)class_25962;
            if (this.clickSlot.getValue()) {
                this.logPacket("ClickSlot - type: %s, slot: %s, button: %s, id: %s", packet.method_12195().name(), packet.method_12192(), packet.method_12193(), packet.method_12194());
            }
        }
        if ((class_25962 = event.getPacket()) instanceof class_2838) {
            packet = (class_2838)class_25962;
            if (this.pickInventory.getValue()) {
                this.logPacket("PickInventory - slot: %s", packet.method_12293());
            }
        }
        if ((class_25962 = event.getPacket()) instanceof class_2793) {
            packet = (class_2793)class_25962;
            if (this.teleportConfirm.getValue()) {
                this.logPacket("TeleportConfirm - id: %s", packet.method_12086());
            }
        }
    }
}

