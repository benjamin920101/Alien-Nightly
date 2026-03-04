/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket
 */
package dev.luminous.core.impl;

import com.google.common.collect.Lists;
import dev.luminous.api.events.eventbus.EventBus;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DisconnectEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.TickCounterEvent;
import dev.luminous.api.utils.more.EvictingQueue;
import dev.luminous.core.Manager;
import dev.luminous.core.impl.TickSync;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

public class TickManager
extends Manager {
    private final Deque<Float> ticks = new EvictingQueue<Float>(20);
    private long time;
    private float clientTick = 1.0f;

    public TickManager() {
        EventBus.INSTANCE.subscribe(this);
    }

    @EventListener
    public void onDisconnect(DisconnectEvent event) {
        this.ticks.clear();
    }

    @EventListener
    public void onPacketInbound(PacketEvent event) {
        if (TickManager.mc.field_1724 == null || TickManager.mc.field_1687 == null) {
            return;
        }
        if (event.getPacket() instanceof class_2761) {
            float last = 20000.0f / (float)(System.currentTimeMillis() - this.time);
            this.ticks.addFirst(Float.valueOf(last));
            this.time = System.currentTimeMillis();
        }
    }

    public void setClientTick(float ticks) {
        this.clientTick = ticks;
    }

    @EventListener
    public void onTickCounter(TickCounterEvent event) {
        if (this.clientTick != 1.0f) {
            event.cancel();
            event.setTicks(this.clientTick);
        }
    }

    public Queue<Float> getTicks() {
        return this.ticks;
    }

    public float getTpsAverage() {
        float avg = 0.0f;
        try {
            ArrayList ticksCopy = Lists.newArrayList(this.ticks);
            if (!ticksCopy.isEmpty()) {
                Iterator iterator = ticksCopy.iterator();
                while (iterator.hasNext()) {
                    float t = ((Float)iterator.next()).floatValue();
                    avg += t;
                }
                avg /= Math.max((float)ticksCopy.size(), 1.0f);
            }
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return Math.min(100.0f, avg);
    }

    public float getTpsCurrent() {
        try {
            if (!this.ticks.isEmpty()) {
                return Math.min(100.0f, this.ticks.getFirst().floatValue());
            }
        }
        catch (NoSuchElementException noSuchElementException) {
            // empty catch block
        }
        return 20.0f;
    }

    public float getTpsMin() {
        float min = 20.0f;
        try {
            for (float t : this.ticks) {
                if (!(t < min)) continue;
                min = t;
            }
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return min;
    }

    public boolean isTicksFilled() {
        return this.ticks.size() >= 20;
    }

    public float getTickSync(TickSync tps) {
        return switch (tps) {
            default -> throw new MatchException(null, null);
            case TickSync.AVERAGE -> this.getTpsAverage();
            case TickSync.CURRENT -> this.getTpsCurrent();
            case TickSync.MINIMAL -> this.getTpsMin();
            case TickSync.NONE -> 20.0f;
        };
    }
}

