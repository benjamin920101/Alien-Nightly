/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.screen.multiplayer.ConnectScreen
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.network.ServerAddress
 *  net.minecraft.client.network.ServerInfo
 *  net.minecraft.client.network.CookieStorage
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.ServerConnectBeginEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.network.CookieStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_412.class})
public abstract class MixinConnectScreen {
    @Inject(method={"method_36877(Lnet/minecraft/class_437;Lnet/minecraft/class_310;Lnet/minecraft/class_639;Lnet/minecraft/class_642;ZLnet/minecraft/class_9112;)V"}, at={@At(value="HEAD")})
    private static void tryConnectEvent(class_437 screen, class_310 client, class_639 address, class_642 info, boolean quickPlay, class_9112 cookieStorage, CallbackInfo ci) {
        Alien.EVENT_BUS.post(ServerConnectBeginEvent.get(address, info));
    }

    @Inject(method={"method_2130(Lnet/minecraft/class_310;Lnet/minecraft/class_639;Lnet/minecraft/class_642;Lnet/minecraft/class_9112;)V"}, at={@At(value="HEAD")})
    private void tryConnectEvent(class_310 client, class_639 address, class_642 info, class_9112 cookieStorage, CallbackInfo ci) {
        Alien.EVENT_BUS.post(ServerConnectBeginEvent.get(address, info));
    }
}

