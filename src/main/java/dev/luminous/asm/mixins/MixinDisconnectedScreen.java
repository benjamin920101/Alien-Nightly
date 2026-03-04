/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.Pair
 *  net.minecraft.text.Text
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.screen.multiplayer.ConnectScreen
 *  net.minecraft.client.gui.widget.ButtonWidget
 *  net.minecraft.client.gui.widget.ButtonWidget$Builder
 *  net.minecraft.client.gui.screen.DisconnectedScreen
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.screen.TitleScreen
 *  net.minecraft.client.network.ServerAddress
 *  net.minecraft.client.network.ServerInfo
 *  net.minecraft.client.gui.widget.Widget
 *  net.minecraft.client.gui.widget.DirectionalLayoutWidget
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.impl.misc.AutoReconnect;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_419.class})
public abstract class MixinDisconnectedScreen
extends class_437 {
    @Shadow
    @Final
    private class_8667 field_44552;
    @Unique
    private class_4185 reconnectBtn;
    @Unique
    private double time;

    protected MixinDisconnectedScreen(class_2561 title) {
        super(title);
        this.time = AutoReconnect.INSTANCE.delay.getValue() * 20.0;
    }

    @Inject(method={"method_25426"}, at={@At(value="INVOKE", target="Lnet/minecraft/class_8667;method_48222()V", shift=At.Shift.BEFORE)})
    private void addButtons(CallbackInfo ci) {
        if (AutoReconnect.INSTANCE.lastServerConnection != null) {
            this.reconnectBtn = new class_4185.class_7840((class_2561)class_2561.method_43470((String)this.getText()), button -> this.tryConnecting()).method_46431();
            this.field_44552.method_52736((class_8021)this.reconnectBtn);
        }
    }

    public void method_25393() {
        if (AutoReconnect.INSTANCE.rejoin() && AutoReconnect.INSTANCE.lastServerConnection != null) {
            if (this.time <= 0.0) {
                this.tryConnecting();
            } else {
                this.time -= 1.0;
                if (this.reconnectBtn != null) {
                    this.reconnectBtn.method_25355((class_2561)class_2561.method_43470((String)this.getText()));
                }
            }
        }
    }

    @Unique
    private String getText() {
        Object reconnectText = "Reconnect";
        if (AutoReconnect.INSTANCE.rejoin()) {
            reconnectText = (String)reconnectText + " " + String.format("(%.1f)", this.time / 20.0);
        }
        return reconnectText;
    }

    @Unique
    private void tryConnecting() {
        Pair<class_639, class_642> lastServer = AutoReconnect.INSTANCE.lastServerConnection;
        class_412.method_36877((class_437)new class_442(), (class_310)Wrapper.mc, (class_639)((class_639)lastServer.left()), (class_642)((class_642)lastServer.right()), (boolean)false, null);
    }
}

