/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.ServerAddress
 *  net.minecraft.client.network.ServerInfo
 */
package dev.luminous.api.events.impl;

import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

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

