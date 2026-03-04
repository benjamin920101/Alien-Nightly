/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Formatting
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.sound.SoundCategory
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DeathEvent;
import dev.luminous.api.events.impl.EntitySpawnEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.RemoveEntityEvent;
import dev.luminous.api.events.impl.TotemEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import net.minecraft.util.Formatting;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;

public class Tips
extends Module {
    public static Tips INSTANCE;
    public final BooleanSetting visualRange = this.add(new BooleanSetting("VisualRange", false).setParent());
    public final BooleanSetting friends = this.add(new BooleanSetting("Friends", false, this.visualRange::isOpen));
    public final BooleanSetting popCounter = this.add(new BooleanSetting("PopCounter", true));
    public final BooleanSetting deathCoords = this.add(new BooleanSetting("DeathCoords", true));
    public final BooleanSetting serverLag = this.add(new BooleanSetting("ServerLag", true));
    public final BooleanSetting lagBack = this.add(new BooleanSetting("LagBack", true));
    public final BooleanSetting potion = this.add(new BooleanSetting("Potion", true).setParent());
    public final BooleanSetting resistanceLevelCheck = this.add(new BooleanSetting("ResistanceLevelCheck", true, this.potion::isOpen));
    private final SliderSetting yOffset = this.add(new SliderSetting("YOffset", 0, -200, 200, this.potion::isOpen));
    final DecimalFormat df = new DecimalFormat("0.0");
    final int color = new Color(190, 0, 0).getRGB();
    private final Timer lagTimer = new Timer();
    private final Timer lagBackTimer = new Timer();
    int turtles = 0;

    public Tips() {
        super("Tips", Module.Category.Misc);
        this.setChinese("\u63d0\u793a");
        INSTANCE = this;
    }

    @EventListener
    public void onAddEntity(EntitySpawnEvent event) {
        String playerName;
        boolean isFriend;
        if (this.visualRange.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null && (!(isFriend = Alien.FRIEND.isFriend(playerName = event.getEntity().method_5476().getString())) || this.friends.getValue()) && event.getEntity() != Tips.mc.field_1724) {
            CommandManager.sendMessageId((isFriend ? String.valueOf(class_124.field_1075) + playerName : String.valueOf(class_124.field_1068) + playerName) + "\u00a7f entered your visual range.", event.getEntity().method_5628() + 777);
            Tips.mc.field_1687.method_8396((class_1657)Tips.mc.field_1724, Tips.mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0f, 1.9f);
        }
    }

    @EventListener
    public void onRemoveEntity(RemoveEntityEvent event) {
        String playerName;
        boolean isFriend;
        if (this.visualRange.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null && (!(isFriend = Alien.FRIEND.isFriend(playerName = event.getEntity().method_5476().getString())) || this.friends.getValue()) && event.getEntity() != Tips.mc.field_1724) {
            CommandManager.sendMessageId((isFriend ? String.valueOf(class_124.field_1075) + playerName : String.valueOf(class_124.field_1068) + playerName) + "\u00a7f left your visual range.", event.getEntity().method_5628() + 777);
            Tips.mc.field_1687.method_8396((class_1657)Tips.mc.field_1724, Tips.mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0f, 1.9f);
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.potion.getValue()) {
            this.turtles = InventoryUtil.getPotionCount((class_1291)class_1294.field_5907.comp_349());
        }
    }

    @EventListener
    public void onPacketEvent(PacketEvent.Receive event) {
        this.lagTimer.reset();
        if (event.getPacket() instanceof class_2708) {
            this.lagBackTimer.reset();
        }
    }

    @Override
    public void onRender2D(class_332 drawContext, float tickDelta) {
        String text;
        if (this.serverLag.getValue() && this.lagTimer.passedS(1.4)) {
            text = "Server not responding (" + this.df.format((double)this.lagTimer.getMs() / 1000.0) + "s)";
            drawContext.method_51433(Tips.mc.field_1772, text, mc.method_22683().method_4486() / 2 - Tips.mc.field_1772.method_1727(text) / 2, 19, this.color, true);
        }
        if (this.lagBack.getValue() && !this.lagBackTimer.passedS(1.5)) {
            text = "Lagback (" + this.df.format((double)(1500L - this.lagBackTimer.getMs()) / 1000.0) + "s)";
            drawContext.method_51433(Tips.mc.field_1772, text, mc.method_22683().method_4486() / 2 - Tips.mc.field_1772.method_1727(text) / 2, 28, this.color, true);
        }
        if (this.potion.getValue()) {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.turtles > 0) {
                stringBuilder.append("\u00a7e").append(this.turtles);
            }
            if (Tips.mc.field_1724.method_6059(class_1294.field_5907) && (!this.resistanceLevelCheck.getValue() || Tips.mc.field_1724.method_6112(class_1294.field_5907).method_5578() > 0)) {
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append("\u00a79").append(Tips.mc.field_1724.method_6112(class_1294.field_5907).method_5584() / 20 + 1);
            }
            if (Tips.mc.field_1724.method_6059(class_1294.field_5910)) {
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append("\u00a74").append(Tips.mc.field_1724.method_6112(class_1294.field_5910).method_5584() / 20 + 1);
            }
            if (Tips.mc.field_1724.method_6059(class_1294.field_5904)) {
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append("\u00a7b").append(Tips.mc.field_1724.method_6112(class_1294.field_5904).method_5584() / 20 + 1);
            }
            drawContext.method_51433(Tips.mc.field_1772, stringBuilder.toString(), mc.method_22683().method_4486() / 2 - Tips.mc.field_1772.method_1727(stringBuilder.toString()) / 2, mc.method_22683().method_4502() / 2 + 9 - this.yOffset.getValueInt(), -1, true);
        }
    }

    @EventListener
    public void onPlayerDeath(DeathEvent event) {
        class_1657 player = event.getPlayer();
        if (this.popCounter.getValue()) {
            if (Alien.POP.popContainer.containsKey(player.method_5477().getString())) {
                int l_Count = Alien.POP.popContainer.get(player.method_5477().getString());
                if (l_Count == 1) {
                    if (player.equals((Object)Tips.mc.field_1724)) {
                        this.sendMessage("\u00a7fYou\u00a7r died after popping \u00a7f" + l_Count + "\u00a7r totem.", player.method_5628());
                    } else {
                        this.sendMessage("\u00a7f" + player.method_5477().getString() + "\u00a7r died after popping \u00a7f" + l_Count + "\u00a7r totem.", player.method_5628());
                    }
                } else if (player.equals((Object)Tips.mc.field_1724)) {
                    this.sendMessage("\u00a7fYou\u00a7r died after popping \u00a7f" + l_Count + "\u00a7r totems.", player.method_5628());
                } else {
                    this.sendMessage("\u00a7f" + player.method_5477().getString() + "\u00a7r died after popping \u00a7f" + l_Count + "\u00a7r totems.", player.method_5628());
                }
            } else if (player.equals((Object)Tips.mc.field_1724)) {
                this.sendMessage("\u00a7fYou\u00a7r died.", player.method_5628());
            } else {
                this.sendMessage("\u00a7f" + player.method_5477().getString() + "\u00a7r died.", player.method_5628());
            }
        }
        if (this.deathCoords.getValue() && player == Tips.mc.field_1724) {
            this.sendMessage("\u00a74You died at " + player.method_31477() + ", " + player.method_31478() + ", " + player.method_31479());
        }
    }

    @EventListener
    public void onTotem(TotemEvent event) {
        if (this.popCounter.getValue()) {
            class_1657 player = event.getPlayer();
            int l_Count = 1;
            if (Alien.POP.popContainer.containsKey(player.method_5477().getString())) {
                l_Count = Alien.POP.popContainer.get(player.method_5477().getString());
            }
            if (l_Count == 1) {
                if (player.equals((Object)Tips.mc.field_1724)) {
                    this.sendMessage("\u00a7fYou\u00a7r popped \u00a7f" + l_Count + "\u00a7r totem.", player.method_5628());
                } else {
                    this.sendMessage("\u00a7f" + player.method_5477().getString() + " \u00a7rpopped \u00a7f" + l_Count + "\u00a7r totems.", player.method_5628());
                }
            } else if (player.equals((Object)Tips.mc.field_1724)) {
                this.sendMessage("\u00a7fYou\u00a7r popped \u00a7f" + l_Count + "\u00a7r totem.", player.method_5628());
            } else {
                this.sendMessage("\u00a7f" + player.method_5477().getString() + " \u00a7rhas popped \u00a7f" + l_Count + "\u00a7r totems.", player.method_5628());
            }
        }
    }

    public void sendMessage(String message, int id) {
        if (!Tips.nullCheck()) {
            if (ClientSetting.INSTANCE.messageStyle.getValue() == ClientSetting.Style.Moon) {
                CommandManager.sendMessageId("\u00a7f[\u00a73" + this.getName() + "\u00a7f] " + message, id);
                return;
            }
            CommandManager.sendMessageId(message, id);
        }
    }
}

