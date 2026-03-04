/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.Pair
 *  it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair
 *  net.minecraft.util.Hand
 *  net.minecraft.item.Items
 *  net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
 *  net.minecraft.client.network.ServerAddress
 *  net.minecraft.client.network.ServerInfo
 *  net.minecraft.network.packet.s2c.play.GameMessageS2CPacket
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.ServerConnectBeginEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.misc.AutoLog;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import java.util.HashMap;
import net.minecraft.util.Hand;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AutoReconnect
extends Module {
    public static AutoReconnect INSTANCE;
    public final BooleanSetting rejoin = this.add(new BooleanSetting("Rejoin", true));
    public final SliderSetting delay = this.add(new SliderSetting("Delay", 5.0, 0.0, 20.0, 0.1).setSuffix("s"));
    public final BooleanSetting autoLogin = this.add(new BooleanSetting("AutoAuth", true));
    public final SliderSetting afterLoginTime = this.add(new SliderSetting("AfterLoginTime", 3.0, 0.0, 10.0, 0.1).setSuffix("s"));
    public final BooleanSetting autoQueue = this.add(new BooleanSetting("AutoQueue", true));
    public final SliderSetting joinQueueDelay = this.add(new SliderSetting("JoinQueueDelay", 3.0, 0.0, 10.0, 0.1).setSuffix("s"));
    final StringSetting password = this.add(new StringSetting("password", "123456"));
    public final BooleanSetting autoAnswer = this.add(new BooleanSetting("AutoAnswer", true));
    public static boolean inQueueServer;
    private final Timer queueTimer = new Timer();
    private final Timer timer = new Timer();
    public Pair<class_639, class_642> lastServerConnection;
    private boolean login = false;
    final String[] abc = new String[]{"A", "B", "C"};
    public static final HashMap<String, String> asks;

    public AutoReconnect() {
        super("AutoReconnect", Module.Category.Misc);
        this.setChinese("\u81ea\u52a8\u91cd\u8fde");
        INSTANCE = this;
        Alien.EVENT_BUS.subscribe(new StaticListener());
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.login && this.timer.passedS(this.afterLoginTime.getValue())) {
            mc.method_1562().method_45730("login " + this.password.getValue());
            this.login = false;
        }
        if (this.autoQueue.getValue() && InventoryUtil.findItem(class_1802.field_8251) != -1 && this.queueTimer.passedS(this.joinQueueDelay.getValue())) {
            InventoryUtil.switchToSlot(InventoryUtil.findItem(class_1802.field_8251));
            AutoReconnect.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
            this.queueTimer.reset();
        }
        inQueueServer = AutoReconnect.nullCheck() ? false : InventoryUtil.findItem(class_1802.field_8251) != -1;
    }

    @Override
    public void onLogin() {
        if (this.autoLogin.getValue()) {
            this.login = true;
            this.timer.reset();
        }
    }

    public boolean rejoin() {
        return this.isOn() && this.rejoin.getValue() && !AutoLog.loggedOut;
    }

    @Override
    public void onLogout() {
        inQueueServer = false;
    }

    @Override
    public void onDisable() {
        inQueueServer = false;
    }

    @EventListener
    public void onPacketReceive(PacketEvent.Receive e) {
        Object object;
        if (!AutoReconnect.nullCheck() && this.autoAnswer.getValue() && inQueueServer && (object = e.getPacket()) instanceof class_7439) {
            class_7439 packet = (class_7439)object;
            for (String key : asks.keySet()) {
                if (!packet.comp_763().getString().contains(key)) continue;
                for (String s : this.abc) {
                    if (!packet.comp_763().getString().contains(s + "." + asks.get(key))) continue;
                    mc.method_1562().method_45729(s.toLowerCase());
                    return;
                }
            }
        }
    }

    static {
        asks = new HashMap<String, String>(){
            {
                this.put("\u7ea2\u77f3\u706b\u628a", "15");
                this.put("\u732a\u88ab\u95ea\u7535", "\u50f5\u5c38\u732a\u4eba");
                this.put("\u5c0f\u7bb1\u5b50\u80fd", "27");
                this.put("\u5f00\u670d\u5e74\u4efd", "2020");
                this.put("\u5b9a\u4f4d\u672b\u5730\u9057\u8ff9", "0");
                this.put("\u722c\u884c\u8005\u88ab\u95ea\u7535", "\u9ad8\u538b\u722c\u884c\u8005");
                this.put("\u5927\u7bb1\u5b50\u80fd", "54");
                this.put("\u7f8a\u9a7c\u4f1a\u4e3b\u52a8", "\u4e0d\u4f1a");
                this.put("\u65e0\u9650\u6c34", "3");
                this.put("\u6316\u6398\u901f\u5ea6\u6700\u5feb", "\u91d1\u9550");
                this.put("\u51cb\u7075\u6b7b\u540e", "\u4e0b\u754c\u4e4b\u661f");
                this.put("\u82e6\u529b\u6015\u7684\u5b98\u65b9", "\u722c\u884c\u8005");
                this.put("\u5357\u74dc\u7684\u751f\u957f", "\u4e0d\u9700\u8981");
                this.put("\u5b9a\u4f4d\u672b\u5730", "0");
            }
        };
    }

    private class StaticListener {
        private StaticListener() {
        }

        @EventListener
        private void onGameJoined(ServerConnectBeginEvent event) {
            AutoReconnect.this.lastServerConnection = new ObjectObjectImmutablePair((Object)event.address, (Object)event.info);
        }
    }
}

