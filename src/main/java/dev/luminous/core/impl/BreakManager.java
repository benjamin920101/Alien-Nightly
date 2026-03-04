/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.BlockBreakingProgressEvent;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.ServerConnectBeginEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.FadeUtils;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.impl.render.BreakESP;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.math.MathHelper;

public class BreakManager
implements Wrapper {
    public final ConcurrentHashMap<Integer, BreakData> breakMap = new ConcurrentHashMap();
    public final ConcurrentHashMap<Integer, BreakData> doubleMap = new ConcurrentHashMap();

    public BreakManager() {
        Alien.EVENT_BUS.subscribe(this);
    }

    @EventListener
    public void onServerConnectBegin(ServerConnectBeginEvent event) {
        this.breakMap.clear();
        this.doubleMap.clear();
    }

    @EventListener
    public void onTick(ClientTickEvent event) {
        if (!Module.nullCheck()) {
            if (AntiCheat.INSTANCE.detectDouble.getValue()) {
                Iterator<Object> iterator = ((ConcurrentHashMap.KeySetView)Alien.BREAK.doubleMap.keySet()).iterator();
                while (iterator.hasNext()) {
                    int i = (Integer)iterator.next();
                    BreakData breakData = Alien.BREAK.doubleMap.get(i);
                    if (breakData != null && breakData.getEntity() != null && !BreakManager.mc.field_1687.method_22347(breakData.pos) && !breakData.timer.passedMs(Math.max(AntiCheat.INSTANCE.minTimeout.getValue() * 1000.0, breakData.breakTime * AntiCheat.INSTANCE.doubleMineTimeout.getValue()))) continue;
                    Alien.BREAK.doubleMap.remove(i);
                }
            }
            for (BreakData breakData : this.breakMap.values()) {
                breakData.breakTime = Math.max(BreakESP.getBreakTime(breakData.pos, false), 50.0);
                if (SpeedMine.unbreakable(breakData.pos)) {
                    breakData.fade.setLength(0L);
                    breakData.complete = false;
                    breakData.failed = true;
                    continue;
                }
                if (BreakManager.mc.field_1687.method_22347(breakData.pos)) {
                    breakData.fade.setLength(0L);
                    breakData.complete = true;
                    breakData.failed = false;
                    continue;
                }
                if (!breakData.complete && breakData.timer.passedMs(breakData.breakTime * AntiCheat.INSTANCE.breakTimeout.getValue())) {
                    breakData.fade.setLength(0L);
                    breakData.failed = true;
                    continue;
                }
                breakData.fade.setLength((long)breakData.breakTime);
            }
        }
    }

    @EventListener
    public void onPacket(PacketEvent.Receive event) {
        class_2596<?> class_25962;
        if (!Module.nullCheck() && (class_25962 = event.getPacket()) instanceof class_2620) {
            BreakData current;
            class_2620 packet = (class_2620)class_25962;
            if (packet.method_11277() == null) {
                return;
            }
            BreakData breakData = new BreakData(packet.method_11277(), packet.method_11280(), false);
            if (breakData.getEntity() == null) {
                return;
            }
            if (class_3532.method_15355((float)((float)breakData.getEntity().method_33571().method_1025(packet.method_11277().method_46558()))) > 8.0f) {
                return;
            }
            if (AntiCheat.INSTANCE.detectDouble.getValue() && packet.method_11278() != 255) {
                if (packet.method_11278() != 0) {
                    BreakData doublePos = this.doubleMap.get(packet.method_11280());
                    if (doublePos != null) {
                        doublePos.pos = packet.method_11277();
                        doublePos.timer.reset();
                    } else if (!SpeedMine.unbreakable(packet.method_11277())) {
                        this.doubleMap.put(packet.method_11280(), new BreakData(packet.method_11277(), packet.method_11280(), true));
                    }
                    return;
                }
                BreakData doublePos = this.doubleMap.get(packet.method_11280());
                if (doublePos != null && doublePos.pos.equals((Object)packet.method_11277()) && !doublePos.timer.passedS(150.0)) {
                    return;
                }
            }
            if ((current = this.breakMap.get(packet.method_11280())) != null && !current.failed && current.pos.equals((Object)packet.method_11277())) {
                return;
            }
            this.breakMap.put(packet.method_11280(), breakData);
            Alien.EVENT_BUS.post(BlockBreakingProgressEvent.get(packet.method_11277(), packet.method_11280(), packet.method_11278()));
            if (AntiCheat.INSTANCE.detectDouble.getValue() && !this.doubleMap.containsKey(packet.method_11280()) && !SpeedMine.unbreakable(packet.method_11277())) {
                this.doubleMap.put(packet.method_11280(), new BreakData(packet.method_11277(), packet.method_11280(), true));
            }
        }
    }

    public boolean isMining(class_2338 pos) {
        return this.isMining(pos, true);
    }

    public boolean isMining(class_2338 pos, boolean self) {
        if (self && SpeedMine.getBreakPos() != null && SpeedMine.getBreakPos().equals((Object)pos)) {
            return true;
        }
        for (BreakData breakData : this.breakMap.values()) {
            if (breakData.getEntity() == null || breakData.getEntity().method_33571().method_1022(pos.method_46558()) > 7.0 || breakData.failed || !breakData.pos.equals((Object)pos)) continue;
            return true;
        }
        return false;
    }

    public static class BreakData {
        public class_2338 pos;
        private final int entityId;
        public final FadeUtils fade;
        public final Timer timer;
        public double breakTime;
        public boolean failed = false;
        public boolean complete = false;

        public BreakData(class_2338 pos, int entityId, boolean extraBreak) {
            this.pos = pos;
            this.entityId = entityId;
            this.breakTime = Math.max(BreakESP.getBreakTime(pos, extraBreak), 50.0);
            this.fade = new FadeUtils((long)this.breakTime);
            this.timer = new Timer();
        }

        public class_1297 getEntity() {
            if (Wrapper.mc.field_1687 == null) {
                return null;
            }
            class_1297 entity = Wrapper.mc.field_1687.method_8469(this.entityId);
            return entity instanceof class_1657 ? entity : null;
        }
    }
}

