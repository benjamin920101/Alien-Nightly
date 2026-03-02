/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  net.minecraft.class_1657
 *  net.minecraft.class_1934
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_2703
 *  net.minecraft.class_2703$class_2705
 *  net.minecraft.class_2703$class_5893
 *  net.minecraft.class_4587
 *  net.minecraft.class_742
 *  net.minecraft.class_7828
 */
package dev.luminous.mod.modules.impl.render;

import com.google.common.collect.Maps;
import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.render.ModelPlayer;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.asm.accessors.IEntity;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.TextRadar;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.class_1657;
import net.minecraft.class_1934;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2703;
import net.minecraft.class_4587;
import net.minecraft.class_742;
import net.minecraft.class_7828;

public class LogoutSpots
extends Module {
    private final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 255, 255, 100)).injectBoolean(true));
    private final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 255, 255, 100)).injectBoolean(true));
    private final ColorSetting text = this.add(new ColorSetting("Text", new Color(255, 255, 255, 255)).injectBoolean(true));
    private final ColorSetting chamsFill = this.add(new ColorSetting("ChamsFill", new Color(255, 255, 255, 100)).injectBoolean(true));
    private final ColorSetting chamsLine = this.add(new ColorSetting("ChamsLine", new Color(255, 255, 255, 100)).injectBoolean(true));
    final Map<UUID, class_1657> playerCache = Maps.newConcurrentMap();
    final Map<UUID, ModelPlayer> logoutCache = Maps.newConcurrentMap();
    private final BooleanSetting health = this.add(new BooleanSetting("Health", true));
    private final BooleanSetting totem = this.add(new BooleanSetting("Totem", true));
    private final BooleanSetting message = this.add(new BooleanSetting("Message", true));

    public LogoutSpots() {
        super("LogoutSpots", Module.Category.Render);
        this.setChinese("\u9000\u51fa\u8bb0\u5f55");
    }

    @EventListener
    public void onPacketReceive(PacketEvent.Receive event) {
        if (!LogoutSpots.nullCheck()) {
            Object object = event.getPacket();
            if (object instanceof class_2703) {
                class_2703 packet = (class_2703)object;
                if (packet.method_46327().contains(class_2703.class_5893.field_29136)) {
                    for (class_2703.class_2705 addedPlayer : packet.method_46330()) {
                        if (addedPlayer.comp_1110() == class_1934.field_9219) continue;
                        for (UUID uuid : this.logoutCache.keySet()) {
                            if (!uuid.equals(addedPlayer.comp_1107().getId())) continue;
                            class_1657 player = this.logoutCache.get((Object)uuid).player;
                            if (this.message.getValue()) {
                                mc.execute(() -> this.sendMessage("\u00a7f" + player.method_5477().getString() + " \u00a7rLogged back at \u00a7f" + player.method_31477() + ", " + player.method_31478() + ", " + player.method_31479()));
                            }
                            this.logoutCache.remove(uuid);
                        }
                    }
                }
            } else {
                object = event.getPacket();
                if (object instanceof class_7828) {
                    List profileIds;
                    class_7828 class_78282 = (class_7828)object;
                    try {
                        List addedPlayer;
                        profileIds = addedPlayer = class_78282.comp_1105();
                    }
                    catch (Throwable throwable) {
                        throw new MatchException(throwable.toString(), throwable);
                    }
                    for (UUID uuid2 : profileIds) {
                        for (UUID uuidx : this.playerCache.keySet()) {
                            if (!uuidx.equals(uuid2)) continue;
                            class_1657 player = this.playerCache.get(uuidx);
                            if (this.logoutCache.containsKey(uuidx) || player == null) continue;
                            ModelPlayer modelPlayer = new ModelPlayer(player);
                            if (this.message.getValue()) {
                                mc.execute(() -> this.sendMessage("\u00a7f" + player.method_5477().getString() + " \u00a7rLogged out at \u00a7f" + player.method_31477() + ", " + player.method_31478() + ", " + player.method_31479()));
                            }
                            this.logoutCache.put(uuidx, modelPlayer);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        this.playerCache.clear();
        this.logoutCache.clear();
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.playerCache.clear();
        for (class_742 player : Alien.THREAD.getPlayers()) {
            if (player == null || player.equals((Object)LogoutSpots.mc.field_1724)) continue;
            this.playerCache.put(player.method_7334().getId(), (class_1657)player);
        }
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        for (ModelPlayer data : this.logoutCache.values()) {
            class_1657 player = data.player;
            class_238 box = ((IEntity)player).getDimensions().method_30757(player.method_19538());
            if (this.box.booleanValue) {
                Render3DUtil.drawBox(matrixStack, box, this.box.getValue());
            }
            if (this.fill.booleanValue) {
                Render3DUtil.drawFill(matrixStack, box, this.fill.getValue());
            }
            if (this.chamsFill.booleanValue || this.chamsLine.booleanValue) {
                data.render(matrixStack, this.chamsFill, this.chamsLine);
            }
            if (!this.text.booleanValue) continue;
            Render3DUtil.drawText3D(player.method_5477().getString() + (String)(this.health.getValue() ? String.valueOf(TextRadar.getHealthColor(player)) + " " + LogoutSpots.round2(player.method_6032() + player.method_6067()) : "") + (String)(this.totem.getValue() && Alien.POP.getPop(player) > 0 ? String.valueOf(TextRadar.getPopColor(Alien.POP.getPop(player))) + " -" + Alien.POP.getPop(player) : ""), new class_243(player.method_23317(), ((IEntity)player).getDimensions().method_30757((class_243)player.method_19538()).field_1325 + 0.5, player.method_23321()), this.text.getValue());
        }
    }

    public static float round2(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}

