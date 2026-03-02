/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_332
 *  net.minecraft.class_742
 */
package dev.luminous.mod.modules.impl.client;

import dev.luminous.Alien;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.class_124;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_332;
import net.minecraft.class_742;

public class TextRadar
extends Module {
    public static TextRadar INSTANCE;
    private final DecimalFormat df = new DecimalFormat("0.0");
    private final BooleanSetting font = this.add(new BooleanSetting("Font", true));
    private final BooleanSetting shadow = this.add(new BooleanSetting("Shadow", true));
    private final SliderSetting x = this.add(new SliderSetting("X", 0, 0, 1500));
    private final SliderSetting y = this.add(new SliderSetting("Y", 100, 0, 1000));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255)));
    private final ColorSetting friend = this.add(new ColorSetting("Friend").injectBoolean(true));
    private final BooleanSetting doubleBlank = this.add(new BooleanSetting("Double", false));
    private final BooleanSetting health = this.add(new BooleanSetting("Health", true));
    private final BooleanSetting pops = this.add(new BooleanSetting("Pops", true));
    public final BooleanSetting red = this.add(new BooleanSetting("Red", false));
    private final BooleanSetting distance = this.add(new BooleanSetting("Distance", true));
    private final BooleanSetting effects = this.add(new BooleanSetting("Effects", true));

    public TextRadar() {
        super("TextRadar", Module.Category.Client);
        this.setChinese("\u6587\u5b57\u96f7\u8fbe");
        INSTANCE = this;
    }

    @Override
    public void onRender2D(class_332 drawContext, float tickDelta) {
        int currentY = this.y.getValueInt();
        ArrayList<class_742> players = new ArrayList<class_742>(TextRadar.mc.field_1687.method_18456());
        players.sort(Comparator.comparingDouble(playerx -> TextRadar.mc.field_1724.method_5739((class_1297)playerx)));
        for (class_1657 class_16572 : players) {
            int color;
            boolean isFriend;
            int totemPopped;
            String blank;
            if (class_16572 == TextRadar.mc.field_1724) continue;
            StringBuilder stringBuilder = new StringBuilder();
            String string = blank = this.doubleBlank.getValue() ? "  " : " ";
            if (this.health.getValue()) {
                stringBuilder.append(TextRadar.getHealthColor(class_16572));
                stringBuilder.append(this.df.format(class_16572.method_6032() + class_16572.method_6067()));
                stringBuilder.append(blank);
            }
            stringBuilder.append(class_124.field_1070);
            stringBuilder.append(class_16572.method_5477().getString());
            if (this.distance.getValue()) {
                stringBuilder.append(blank);
                stringBuilder.append(class_124.field_1068);
                stringBuilder.append(this.df.format(TextRadar.mc.field_1724.method_5739((class_1297)class_16572)));
                stringBuilder.append("m");
            }
            if (this.effects.getValue()) {
                if (class_16572.method_6059(class_1294.field_5909)) {
                    stringBuilder.append(blank);
                    stringBuilder.append(class_124.field_1080);
                    stringBuilder.append("Lv.");
                    stringBuilder.append(class_16572.method_6112(class_1294.field_5909).method_5578() + 1);
                    stringBuilder.append(blank);
                    stringBuilder.append(class_16572.method_6112(class_1294.field_5909).method_5584() / 20 + 1);
                    stringBuilder.append("s");
                }
                if (class_16572.method_6059(class_1294.field_5904)) {
                    stringBuilder.append(blank);
                    stringBuilder.append(class_124.field_1075);
                    stringBuilder.append("Lv.");
                    stringBuilder.append(class_16572.method_6112(class_1294.field_5904).method_5578() + 1);
                    stringBuilder.append(blank);
                    stringBuilder.append(class_16572.method_6112(class_1294.field_5904).method_5584() / 20 + 1);
                    stringBuilder.append("s");
                }
                if (class_16572.method_6059(class_1294.field_5910)) {
                    stringBuilder.append(blank);
                    stringBuilder.append(class_124.field_1079);
                    stringBuilder.append("Lv.");
                    stringBuilder.append(class_16572.method_6112(class_1294.field_5910).method_5578() + 1);
                    stringBuilder.append(blank);
                    stringBuilder.append(class_16572.method_6112(class_1294.field_5910).method_5584() / 20 + 1);
                    stringBuilder.append("s");
                }
                if (class_16572.method_6059(class_1294.field_5907)) {
                    stringBuilder.append(blank);
                    stringBuilder.append(class_124.field_1078);
                    stringBuilder.append("Lv.");
                    stringBuilder.append(class_16572.method_6112(class_1294.field_5907).method_5578() + 1);
                    stringBuilder.append(blank);
                    stringBuilder.append(class_16572.method_6112(class_1294.field_5907).method_5584() / 20 + 1);
                    stringBuilder.append("s");
                }
            }
            if (this.pops.getValue() && (totemPopped = Alien.POP.getPop(class_16572)) > 0) {
                stringBuilder.append(blank);
                stringBuilder.append(TextRadar.getPopColor(totemPopped));
                stringBuilder.append("-");
                stringBuilder.append(totemPopped);
            }
            if ((isFriend = Alien.FRIEND.isFriend(class_16572)) && !this.friend.booleanValue) continue;
            int n = color = isFriend ? this.friend.getValue().getRGB() : this.color.getValue().getRGB();
            if (this.font.getValue()) {
                FontManager.ui.drawString(drawContext.method_51448(), stringBuilder.toString(), (double)this.x.getValueInt(), (double)currentY, color, this.shadow.getValue());
            } else {
                drawContext.method_51433(TextRadar.mc.field_1772, stringBuilder.toString(), this.x.getValueInt(), currentY, color, this.shadow.getValue());
            }
            currentY += this.font.getValue() ? (int)FontManager.ui.getFontHeight() : 9;
        }
    }

    public static class_124 getHealthColor(class_1657 player) {
        double health = player.method_6032() + player.method_6067();
        if (health > 18.0) {
            return class_124.field_1060;
        }
        if (health > 16.0) {
            return class_124.field_1077;
        }
        if (health > 12.0) {
            return class_124.field_1054;
        }
        if (health > 8.0) {
            return class_124.field_1065;
        }
        return health > 4.0 ? class_124.field_1061 : class_124.field_1079;
    }

    public static class_124 getPopColor(int totems) {
        if (TextRadar.INSTANCE.red.getValue()) {
            return class_124.field_1061;
        }
        if (totems > 10) {
            return class_124.field_1079;
        }
        if (totems > 8) {
            return class_124.field_1061;
        }
        if (totems > 6) {
            return class_124.field_1065;
        }
        if (totems > 4) {
            return class_124.field_1054;
        }
        return totems > 2 ? class_124.field_1077 : class_124.field_1060;
    }
}

