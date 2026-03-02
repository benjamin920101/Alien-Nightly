/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_639
 *  net.minecraft.class_642
 */
package dev.luminous.api.events.impl;

import net.minecraft.class_639;
import net.minecraft.class_642;

public class ServerConnectBeginEvent {
    private static final ServerConnectBeginEvent INSTANCE = new ServerConnectBeginEvent();
    public class_639 address;
    public class_642 info;

    public static ServerConnectBeginEvent get(class_639 address, class_642 info) {
        ServerConnectBeginEvent.INSTANCE.address = address;
        ServerConnectBeginEvent.INSTANCE.info = info;
        return INSTANCE;
    }
}

