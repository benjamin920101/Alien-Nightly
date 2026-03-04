/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket$Mode
 */
package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventBus;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketSneakingEvent;
import dev.luminous.core.Manager;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class MovementManager
extends Manager {
    private boolean packetSneaking;

    public MovementManager() {
        EventBus.INSTANCE.subscribe(this);
    }

    public void setMotionY(double y) {
        MovementManager.mc.field_1724.method_18800(MovementManager.mc.field_1724.method_18798().method_10216(), y, MovementManager.mc.field_1724.method_18798().method_10215());
    }

    public void setMotionXZ(double x, double z) {
        MovementManager.mc.field_1724.method_18800(x, MovementManager.mc.field_1724.method_18798().field_1351, z);
    }

    public void setMotionX(double x) {
        MovementManager.mc.field_1724.method_18800(x, MovementManager.mc.field_1724.method_18798().field_1351, MovementManager.mc.field_1724.method_18798().field_1350);
    }

    public void setMotionZ(double z) {
        MovementManager.mc.field_1724.method_18800(MovementManager.mc.field_1724.method_18798().field_1352, MovementManager.mc.field_1724.method_18798().field_1351, z);
    }

    public void setPacketSneaking(boolean packetSneaking) {
        this.packetSneaking = packetSneaking;
        if (packetSneaking) {
            Alien.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)MovementManager.mc.field_1724, class_2848.class_2849.field_12979));
        } else {
            Alien.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)MovementManager.mc.field_1724, class_2848.class_2849.field_12984));
        }
    }

    @EventListener
    public void onPacketSneak(PacketSneakingEvent event) {
        event.setCanceled(this.packetSneaking);
    }
}

