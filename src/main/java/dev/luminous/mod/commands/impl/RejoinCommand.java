/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.text.Text
 *  net.minecraft.network.packet.s2c.common.DisconnectS2CPacket
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.screen.multiplayer.ConnectScreen
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.screen.TitleScreen
 *  net.minecraft.client.network.ServerAddress
 *  net.minecraft.client.network.ServerInfo
 *  net.minecraft.network.listener.ClientCommonPacketListener
 */
package dev.luminous.mod.commands.impl;

import dev.luminous.mod.commands.Command;
import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.listener.ClientCommonPacketListener;

public class RejoinCommand
extends Command {
    public RejoinCommand() {
        super("rejoin", "");
    }

    @Override
    public void runCommand(String[] parameters) {
        mc.method_18859(() -> {
            if (RejoinCommand.mc.field_1687 != null && mc.method_1558() != null) {
                class_642 lastestServerEntry = mc.method_1558();
                new class_2661(class_2561.method_30163((String)"Self kick")).method_11467((class_8705)mc.method_1562());
                class_412.method_36877((class_437)new class_442(), (class_310)mc, (class_639)class_639.method_2950((String)lastestServerEntry.field_3761), (class_642)lastestServerEntry, (boolean)false, null);
            }
        });
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}

