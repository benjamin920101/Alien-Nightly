/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2649
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_2649;

public class InventoryS2CPacketEvent
extends Event {
    private static final InventoryS2CPacketEvent INSTANCE = new InventoryS2CPacketEvent();
    public class_2649 packet;

    private InventoryS2CPacketEvent() {
    }

    public static InventoryS2CPacketEvent get(class_2649 hand) {
        InventoryS2CPacketEvent.INSTANCE.packet = hand;
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }
}

