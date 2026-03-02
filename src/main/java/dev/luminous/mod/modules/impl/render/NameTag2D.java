/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1887
 *  net.minecraft.class_1890
 *  net.minecraft.class_1921
 *  net.minecraft.class_1934
 *  net.minecraft.class_243
 *  net.minecraft.class_308
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_640
 *  net.minecraft.class_6880
 *  net.minecraft.class_9304
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector4d
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.Render2DEvent;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.api.utils.render.TextUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.Freecam;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1921;
import net.minecraft.class_1934;
import net.minecraft.class_243;
import net.minecraft.class_308;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_9304;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4d;

public class NameTag2D
extends Module {
    public static NameTag2D INSTANCE;
    private final SliderSetting scale = this.add(new SliderSetting("Scale", (double)0.68f, (double)0.1f, 2.0, 0.01));
    private final SliderSetting minScale = this.add(new SliderSetting("MinScale", (double)0.2f, (double)0.1f, 1.0, 0.01));
    private final SliderSetting scaled = this.add(new SliderSetting("Scaled", 1.0, 0.0, 2.0, 0.01));
    private final SliderSetting offset = this.add(new SliderSetting("Offset", (double)0.315f, (double)0.001f, 1.0, 0.001));
    private final SliderSetting height = this.add(new SliderSetting("Height", 0.0, -3.0, 3.0, 0.01));
    private final BooleanSetting god = this.add(new BooleanSetting("God", true));
    private final BooleanSetting gamemode = this.add(new BooleanSetting("Gamemode", false));
    private final BooleanSetting ping = this.add(new BooleanSetting("Ping", false));
    private final BooleanSetting health = this.add(new BooleanSetting("Health", true));
    private final BooleanSetting distance = this.add(new BooleanSetting("Distance", true));
    private final BooleanSetting pops = this.add(new BooleanSetting("TotemPops", true));
    private final BooleanSetting enchants = this.add(new BooleanSetting("Enchants", true));
    private final ColorSetting outline = this.add(new ColorSetting("Outline", new Color(-1711276033, true)).injectBoolean(true));
    private final ColorSetting rect = this.add(new ColorSetting("Rect", new Color(-1728053247, true)).injectBoolean(true));
    private final ColorSetting friendColor = this.add(new ColorSetting("FriendColor", new Color(-14811363, true)));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(-1, true)));
    public final EnumSetting<Font> font = this.add(new EnumSetting<Font>("FontMode", Font.Fast));
    private final SliderSetting armorHeight = this.add(new SliderSetting("ArmorHeight", 0.3f, -10.0, 10.0));
    private final SliderSetting armorScale = this.add(new SliderSetting("ArmorScale", (double)0.9f, (double)0.1f, 2.0, 0.01f));
    private final EnumSetting<Armor> armorMode = this.add(new EnumSetting<Armor>("ArmorMode", Armor.Full));

    public NameTag2D() {
        super("NameTag2D", Module.Category.Render);
        this.setChinese("2D\u540d\u5b57\u6807\u7b7e");
        INSTANCE = this;
    }

    @EventListener
    public void onRender2D(Render2DEvent event) {
        class_332 context = event.drawContext;
        float tickDelta = event.tickDelta;
        for (class_1657 ent : NameTag2D.mc.field_1687.method_18456()) {
            class_243 vector;
            if (ent == NameTag2D.mc.field_1724 && NameTag2D.mc.field_1690.method_31044().method_31034() && Freecam.INSTANCE.isOff()) continue;
            double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)tickDelta;
            double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)tickDelta;
            double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)tickDelta;
            class_243 preVec = vector = new class_243(x, y + this.height.getValue() + ent.method_5829().method_17940() + 0.3, z);
            vector = TextUtil.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
            if (!(vector.field_1350 > 0.0) || !(vector.field_1350 < 1.0)) continue;
            Vector4d position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0);
            position.x = Math.min(vector.field_1352, position.x);
            position.y = Math.min(vector.field_1351, position.y);
            position.z = Math.max(vector.field_1352, position.z);
            Object final_string = "";
            if (this.god.getValue() && ent.method_6059(class_1294.field_5909)) {
                final_string = (String)final_string + "\u00a74GOD ";
            }
            if (this.ping.getValue()) {
                final_string = (String)final_string + NameTag2D.getEntityPing(ent) + "ms ";
            }
            if (this.gamemode.getValue()) {
                final_string = (String)final_string + this.translateGamemode(NameTag2D.getEntityGamemode(ent)) + " ";
            }
            final_string = (String)final_string + String.valueOf(class_124.field_1070) + ent.method_5477().getString();
            if (this.health.getValue()) {
                final_string = (String)final_string + " " + String.valueOf(this.getHealthColor(ent)) + NameTag2D.round2(ent.method_6067() + ent.method_6032());
            }
            if (this.distance.getValue()) {
                final_string = (String)final_string + " " + String.valueOf(class_124.field_1070) + String.format("%.1f", Float.valueOf(NameTag2D.mc.field_1724.method_5739((class_1297)ent))) + "m";
            }
            if (this.pops.getValue() && Alien.POP.getPop(ent.method_5477().getString()) != 0) {
                final_string = (String)final_string + " \u00a7bPop " + String.valueOf(class_124.field_1076) + Alien.POP.getPop(ent.method_5477().getString());
            }
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            float diff = (float)(endPosX - posX) / 2.0f;
            float textWidth = this.font.getValue() == Font.Fancy ? FontManager.ui.getWidth((String)final_string) * 1.0f : (float)NameTag2D.mc.field_1772.method_1727((String)final_string);
            float tagX = (float)((posX + (double)diff - (double)(textWidth / 2.0f)) * 1.0);
            ArrayList<class_1799> stacks = new ArrayList<class_1799>();
            stacks.add(ent.method_6047());
            stacks.add((class_1799)ent.method_31548().field_7548.get(3));
            stacks.add((class_1799)ent.method_31548().field_7548.get(2));
            stacks.add((class_1799)ent.method_31548().field_7548.get(1));
            stacks.add((class_1799)ent.method_31548().field_7548.get(0));
            stacks.add(ent.method_6079());
            context.method_51448().method_22903();
            context.method_51448().method_46416(tagX - 2.0f + (textWidth + 4.0f) / 2.0f, (float)(posY - 13.0) + 6.5f, 0.0f);
            float size = (float)Math.max(1.0 - (double)class_3532.method_15355((float)((float)NameTag2D.mc.field_1719.method_5707(preVec))) * 0.01 * this.scaled.getValue(), 0.0);
            context.method_51448().method_22905(Math.max(this.scale.getValueFloat() * size, this.minScale.getValueFloat()), Math.max(this.scale.getValueFloat() * size, this.minScale.getValueFloat()), 1.0f);
            context.method_51448().method_46416(0.0f, this.offset.getValueFloat() * class_3532.method_15355((float)((float)NameTag2D.mc.field_1719.method_5707(preVec))), 0.0f);
            context.method_51448().method_46416(-(tagX - 2.0f + (textWidth + 4.0f) / 2.0f), -((float)(posY - 13.0 + 6.5)), 0.0f);
            float item_offset = 0.0f;
            if (this.armorMode.getValue() != Armor.None) {
                int count = 0;
                for (class_1799 armorComponent : stacks) {
                    ++count;
                    if (!armorComponent.method_7960()) {
                        context.method_51448().method_22903();
                        context.method_51448().method_46416(tagX - 2.0f + (textWidth + 4.0f) / 2.0f, (float)(posY - 13.0) + 6.5f, 0.0f);
                        context.method_51448().method_22905(this.armorScale.getValueFloat(), this.armorScale.getValueFloat(), 1.0f);
                        context.method_51448().method_46416(-(tagX - 2.0f + (textWidth + 4.0f) / 2.0f), -((float)(posY - 13.0 + 6.5)), 0.0f);
                        context.method_51448().method_22904(posX - 52.5 + (double)item_offset, (double)((float)(posY - 29.0) + this.armorHeight.getValueFloat()), 0.0);
                        float durability = armorComponent.method_7936() - armorComponent.method_7919();
                        int percent = (int)(durability / (float)armorComponent.method_7936() * 100.0f);
                        Color color = percent <= 33 ? Color.RED : (percent <= 66 ? Color.ORANGE : Color.GREEN);
                        switch (this.armorMode.getValue().ordinal()) {
                            case 4: {
                                if (count <= 1 || count >= 6) break;
                                class_308.method_24210();
                                context.method_51427(armorComponent, 0, 0);
                                context.method_51431(NameTag2D.mc.field_1772, armorComponent, 0, 0);
                                break;
                            }
                            case 3: {
                                class_308.method_24210();
                                context.method_51427(armorComponent, 0, 0);
                                context.method_51431(NameTag2D.mc.field_1772, armorComponent, 0, 0);
                                break;
                            }
                            case 1: {
                                class_308.method_24210();
                                context.method_51427(armorComponent, 0, 0);
                                context.method_51431(NameTag2D.mc.field_1772, armorComponent, 0, 0);
                                if (armorComponent.method_7936() <= 0) break;
                                if (this.font.getValue() == Font.Fancy) {
                                    FontManager.ui.drawString(context.method_51448(), String.valueOf(percent), (double)(9.0f - FontManager.ui.getWidth(String.valueOf(percent)) / 2.0f), (double)(-FontManager.ui.getFontHeight() + 3.0f), color.getRGB());
                                    break;
                                }
                                class_327 PackResourceMetadata = NameTag2D.mc.field_1772;
                                String string = String.valueOf(percent);
                                int n = 9 - NameTag2D.mc.field_1772.method_1727(String.valueOf(percent)) / 2;
                                Objects.requireNonNull(NameTag2D.mc.field_1772);
                                context.method_51433(PackResourceMetadata, string, n, -8, color.getRGB(), true);
                                break;
                            }
                            case 2: {
                                context.method_51431(NameTag2D.mc.field_1772, armorComponent, 0, 0);
                                if (armorComponent.method_7936() <= 0) break;
                                if (!armorComponent.method_31578()) {
                                    int i = armorComponent.method_31579();
                                    int j = armorComponent.method_31580();
                                    int k = 2;
                                    int l = 13;
                                    context.method_51739(class_1921.method_51785(), k, l, k + 13, l + 2, -16777216);
                                    context.method_51739(class_1921.method_51785(), k, l, k + i, l + 1, j | 0xFF000000);
                                }
                                if (this.font.getValue() == Font.Fancy) {
                                    FontManager.ui.drawString(context.method_51448(), String.valueOf(percent), (double)(9.0f - FontManager.ui.getWidth(String.valueOf(percent)) / 2.0f), 7.0, color.getRGB());
                                    break;
                                }
                                context.method_51433(NameTag2D.mc.field_1772, String.valueOf(percent), 9 - NameTag2D.mc.field_1772.method_1727(String.valueOf(percent)) / 2, 5, color.getRGB(), true);
                            }
                        }
                        context.method_51448().method_22909();
                        if (this.enchants.getValue()) {
                            float enchantmentY = 0.0f;
                            if (armorComponent.method_7942()) {
                                class_9304 enchants = class_1890.method_57532((class_1799)armorComponent);
                                for (class_6880 enchantment : enchants.method_57534()) {
                                    int lvl = enchants.method_57536(enchantment);
                                    StringBuilder encName = new StringBuilder();
                                    String translatedName = ((class_1887)enchantment.comp_349()).toString().replace("Enchantment ", "");
                                    if (translatedName.contains("BlastProtection")) {
                                        encName.append("B").append(lvl);
                                    } else if (translatedName.contains("Protection")) {
                                        encName.append("P").append(lvl);
                                    } else if (translatedName.contains("Thorns")) {
                                        encName.append("T").append(lvl);
                                    } else if (translatedName.contains("Sharpness")) {
                                        encName.append("S").append(lvl);
                                    } else if (translatedName.contains("Efficiency")) {
                                        encName.append("E").append(lvl);
                                    } else if (translatedName.contains("Unbreaking")) {
                                        encName.append("U").append(lvl);
                                    } else {
                                        if (!translatedName.contains("Power")) continue;
                                        encName.append("PO").append(lvl);
                                    }
                                    if (this.font.getValue() == Font.Fancy) {
                                        FontManager.ui.drawString(context.method_51448(), encName.toString(), posX - 50.0 + (double)item_offset, (double)((float)posY - 45.0f + enchantmentY), -1);
                                    } else {
                                        context.method_51448().method_22903();
                                        context.method_51448().method_22904(posX - 50.0 + (double)item_offset, posY - 45.0 + (double)enchantmentY, 0.0);
                                        context.method_51433(NameTag2D.mc.field_1772, encName.toString(), 0, 0, -1, true);
                                        context.method_51448().method_22909();
                                    }
                                    enchantmentY -= 8.0f;
                                }
                            }
                        }
                    }
                    item_offset += 18.0f;
                }
            }
            if (this.rect.booleanValue) {
                Render2DUtil.drawRect(context.method_51448(), tagX - 2.0f, (float)(posY - 14.0), textWidth + 4.0f, 13.0f, this.rect.getValue());
            }
            if (this.outline.booleanValue) {
                Render2DUtil.drawRect(context.method_51448(), tagX - 3.0f, (float)(posY - 14.0), textWidth + 6.0f, 1.0f, this.outline.getValue());
                Render2DUtil.drawRect(context.method_51448(), tagX - 3.0f, (float)(posY - 2.0), textWidth + 6.0f, 1.0f, this.outline.getValue());
                Render2DUtil.drawRect(context.method_51448(), tagX - 3.0f, (float)(posY - 14.0), 1.0f, 12.0f, this.outline.getValue());
                Render2DUtil.drawRect(context.method_51448(), tagX + textWidth + 2.0f, (float)(posY - 14.0), 1.0f, 12.0f, this.outline.getValue());
            }
            if (this.font.getValue() == Font.Fancy) {
                FontManager.ui.drawString(context.method_51448(), (String)final_string, (double)tagX, (double)((float)posY - 10.0f), Alien.FRIEND.isFriend(ent) ? this.friendColor.getValue().getRGB() : this.color.getValue().getRGB());
            } else {
                context.method_51448().method_22903();
                context.method_51448().method_46416(tagX, (float)posY - 11.0f, 0.0f);
                context.method_51433(NameTag2D.mc.field_1772, (String)final_string, 0, 0, Alien.FRIEND.isFriend(ent) ? this.friendColor.getValue().getRGB() : this.color.getValue().getRGB(), true);
                context.method_51448().method_22909();
            }
            context.method_51448().method_22909();
        }
    }

    public static String getEntityPing(class_1657 entity) {
        if (mc.method_1562() == null) {
            return "-1";
        }
        class_640 playerListEntry = mc.method_1562().method_2871(entity.method_5667());
        if (playerListEntry == null) {
            return "-1";
        }
        int ping = playerListEntry.method_2959();
        class_124 color = class_124.field_1060;
        if (ping >= 100) {
            color = class_124.field_1054;
        }
        if (ping >= 250) {
            color = class_124.field_1061;
        }
        return color.toString() + ping;
    }

    public static class_1934 getEntityGamemode(class_1657 entity) {
        if (entity == null) {
            return null;
        }
        class_640 playerListEntry = mc.method_1562().method_2871(entity.method_5667());
        return playerListEntry == null ? null : playerListEntry.method_2958();
    }

    private String translateGamemode(class_1934 gamemode) {
        if (gamemode == null) {
            return "\u00a77[BOT]";
        }
        return switch (gamemode) {
            default -> throw new MatchException(null, null);
            case class_1934.field_9215 -> "\u00a7b[S]";
            case class_1934.field_9220 -> "\u00a7c[C]";
            case class_1934.field_9219 -> "\u00a77[SP]";
            case class_1934.field_9216 -> "\u00a7e[A]";
        };
    }

    private class_124 getHealthColor(@NotNull class_1657 entity) {
        int health = (int)((float)((int)entity.method_6032()) + entity.method_6067());
        if (health >= 18) {
            return class_124.field_1060;
        }
        if (health >= 12) {
            return class_124.field_1054;
        }
        if (health >= 6) {
            return class_124.field_1061;
        }
        return class_124.field_1079;
    }

    public static float round2(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static enum Font {
        Fancy,
        Fast;

    }

    public static enum Armor {
        None,
        Full,
        Durability,
        Item,
        OnlyArmor;

    }
}

