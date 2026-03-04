/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.world.World
 *  net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket
 *  net.minecraft.client.network.AbstractClientPlayerEntity
 */
package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DeathEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.TotemEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.Module;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.client.network.AbstractClientPlayerEntity;

public class PopManager
implements Wrapper {
    public final HashMap<String, Integer> popContainer = new HashMap();
    private final List<class_1657> deadPlayer = new ArrayList<class_1657>();

    public PopManager() {
        this.init();
    }

    public void init() {
        Alien.EVENT_BUS.subscribe(this);
    }

    public int getPop(String s) {
        return this.popContainer.getOrDefault(s, 0);
    }

    public int getPop(class_1657 player) {
        return this.getPop(player.method_5477().getString());
    }

    public void onUpdate() {
        if (!Module.nullCheck()) {
            for (class_742 player : Alien.THREAD.getPlayers()) {
                if (player == null || !player.method_29504()) {
                    this.deadPlayer.remove(player);
                    continue;
                }
                if (this.deadPlayer.contains(player)) continue;
                Alien.EVENT_BUS.post(DeathEvent.get((class_1657)player));
                this.onDeath((class_1657)player);
                this.deadPlayer.add((class_1657)player);
            }
        }
    }

    @EventListener
    public void onPacketReceive(PacketEvent.Receive event) {
        class_2663 packet;
        class_1297 class_12972;
        if (!Module.nullCheck() && (class_12972 = event.getPacket()) instanceof class_2663 && (packet = (class_2663)class_12972).method_11470() == 35 && (class_12972 = packet.method_11469((class_1937)PopManager.mc.field_1687)) instanceof class_1657) {
            class_1657 player = (class_1657)class_12972;
            this.onTotemPop(player);
        }
    }

    public void onDeath(class_1657 player) {
        this.popContainer.remove(player.method_5477().getString());
    }

    public void onTotemPop(class_1657 player) {
        int l_Count = 1;
        if (this.popContainer.containsKey(player.method_5477().getString())) {
            l_Count = this.popContainer.get(player.method_5477().getString());
            this.popContainer.put(player.method_5477().getString(), ++l_Count);
        } else {
            this.popContainer.put(player.method_5477().getString(), l_Count);
        }
        Alien.EVENT_BUS.post(TotemEvent.get(player));
    }
}

