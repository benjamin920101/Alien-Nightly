/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1311
 *  net.minecraft.class_1511
 *  net.minecraft.class_1542
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_238
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_332
 *  net.minecraft.class_757
 *  net.minecraft.class_9801
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Matrix4f
 *  org.joml.Vector4d
 */
package dev.luminous.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.api.utils.render.TextUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1311;
import net.minecraft.class_1511;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_332;
import net.minecraft.class_757;
import net.minecraft.class_9801;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4d;

public class TwoDESP
extends Module {
    private final EnumSetting page = this.add(new EnumSetting<TwoDESPMode>("Settings", TwoDESPMode.Target));
    public final ColorSetting armorDuraColor = this.add(new ColorSetting("Armor Dura Color", new Color(0x2FFF00), () -> this.page.getValue() == TwoDESPMode.Color));
    public final ColorSetting hHealth = this.add(new ColorSetting("High Health Color", new Color(0, 255, 0, 255), () -> this.page.getValue() == TwoDESPMode.Color));
    public final ColorSetting mHealth = this.add(new ColorSetting("Mid Health Color", new Color(255, 255, 0, 255), () -> this.page.getValue() == TwoDESPMode.Color));
    public final ColorSetting lHealth = this.add(new ColorSetting("Low Health Color", new Color(255, 0, 0, 255), () -> this.page.getValue() == TwoDESPMode.Color));
    private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true, () -> this.page.getValue() == TwoDESPMode.Setting));
    private final BooleanSetting renderHealth = this.add(new BooleanSetting("renderHealth", true, () -> this.page.getValue() == TwoDESPMode.Setting));
    private final BooleanSetting renderArmor = this.add(new BooleanSetting("Armor Dura", true, () -> this.page.getValue() == TwoDESPMode.Setting));
    private final SliderSetting durascale = this.add(new SliderSetting("DuraScale", 1.0, 0.0, 2.0, 0.1, () -> this.renderArmor.getValue()));
    private final BooleanSetting drawItem = this.add(new BooleanSetting("draw Item Name", true, () -> this.page.getValue() == TwoDESPMode.Setting));
    private final BooleanSetting drawItemC = this.add(new BooleanSetting("draw Item Count", true, () -> this.page.getValue() == TwoDESPMode.Setting && this.drawItem.getValue()));
    public final ColorSetting countColor = this.add(new ColorSetting("Item Count Color", new Color(255, 255, 0, 255), () -> this.page.getValue() == TwoDESPMode.Color && this.drawItemC.getValue()));
    public final ColorSetting textcolor = this.add(new ColorSetting("Item Name Color", new Color(255, 255, 255, 255), () -> this.page.getValue() == TwoDESPMode.Color && this.drawItem.getValue()));
    private final BooleanSetting font = this.add(new BooleanSetting("CustomFont", true, () -> this.page.getValue() == TwoDESPMode.Setting));
    private final BooleanSetting players = this.add(new BooleanSetting("Players", true, () -> this.page.getValue() == TwoDESPMode.Target));
    private final BooleanSetting friends = this.add(new BooleanSetting("Friends", true, () -> this.page.getValue() == TwoDESPMode.Target));
    private final BooleanSetting crystals = this.add(new BooleanSetting("Crystals", true, () -> this.page.getValue() == TwoDESPMode.Target));
    private final BooleanSetting creatures = this.add(new BooleanSetting("Creatures", false, () -> this.page.getValue() == TwoDESPMode.Target));
    private final BooleanSetting monsters = this.add(new BooleanSetting("Monsters", false, () -> this.page.getValue() == TwoDESPMode.Target));
    private final BooleanSetting ambients = this.add(new BooleanSetting("Ambients", false, () -> this.page.getValue() == TwoDESPMode.Target));
    private final BooleanSetting others = this.add(new BooleanSetting("Others", false, () -> this.page.getValue() == TwoDESPMode.Target));
    private final ColorSetting playersC = this.add(new ColorSetting("PlayersBox", new Color(16749056), () -> this.page.getValue() == TwoDESPMode.Color));
    private final ColorSetting friendsC = this.add(new ColorSetting("FriendsBox", new Color(0x30FF00), () -> this.page.getValue() == TwoDESPMode.Color));
    private final ColorSetting crystalsC = this.add(new ColorSetting("CrystalsBox", new Color(48127), () -> this.page.getValue() == TwoDESPMode.Color));
    private final ColorSetting creaturesC = this.add(new ColorSetting("CreaturesBox", new Color(10527910), () -> this.page.getValue() == TwoDESPMode.Color));
    private final ColorSetting monstersC = this.add(new ColorSetting("MonstersBox", new Color(0xFF0000), () -> this.page.getValue() == TwoDESPMode.Color));
    private final ColorSetting ambientsC = this.add(new ColorSetting("AmbientsBox", new Color(8061183), () -> this.page.getValue() == TwoDESPMode.Color));
    private final ColorSetting othersC = this.add(new ColorSetting("OthersBox", new Color(16711778), () -> this.page.getValue() == TwoDESPMode.Color));

    public TwoDESP() {
        super("2DESP", Module.Category.Render);
    }

    public static float getRotations(class_241 vec) {
        if (TwoDESP.mc.field_1724 == null) {
            return 0.0f;
        }
        double x = (double)vec.field_1343 - TwoDESP.mc.field_1724.method_19538().field_1352;
        double z = (double)vec.field_1342 - TwoDESP.mc.field_1724.method_19538().field_1350;
        return (float)(-(Math.atan2(x, z) * 57.29577951308232));
    }

    @Override
    public void onRender2D(class_332 context, float tickDelta) {
        Matrix4f matrix = context.method_51448().method_23760().method_23761();
        Render2DUtil.setupRender();
        RenderSystem.setShader(class_757::method_34540);
        class_287 bufferBuilder = null;
        boolean hasVertices = false;
        for (class_1297 ent : TwoDESP.mc.field_1687.method_18112()) {
            if (!this.shouldRender(ent)) continue;
            if (bufferBuilder == null) {
                bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
            }
            if (!this.drawBox(bufferBuilder, ent, matrix, context)) continue;
            hasVertices = true;
        }
        if (hasVertices && bufferBuilder != null) {
            class_286.method_43433((class_9801)bufferBuilder.method_60800());
        }
        Render2DUtil.endRender();
        for (class_1297 ent : TwoDESP.mc.field_1687.method_18112()) {
            if (!this.shouldRender(ent)) continue;
            this.drawText(ent, context);
        }
    }

    public boolean shouldRender(class_1297 entity) {
        if (entity == null) {
            return false;
        }
        if (TwoDESP.mc.field_1724 == null) {
            return false;
        }
        if (entity instanceof class_1657) {
            if (entity == TwoDESP.mc.field_1724 && TwoDESP.mc.field_1690.method_31044().method_31034()) {
                return false;
            }
            if (Alien.FRIEND.isFriend((class_1657)entity)) {
                return this.friends.getValue();
            }
            return this.players.getValue();
        }
        if (entity instanceof class_1511) {
            return this.crystals.getValue();
        }
        return switch (entity.method_5864().method_5891()) {
            case class_1311.field_6294, class_1311.field_6300 -> this.creatures.getValue();
            case class_1311.field_6302 -> this.monsters.getValue();
            case class_1311.field_6303, class_1311.field_24460 -> this.ambients.getValue();
            default -> this.others.getValue();
        };
    }

    public Color getEntityColor(class_1297 entity) {
        if (entity == null) {
            return new Color(-1);
        }
        if (entity instanceof class_1657) {
            if (Alien.FRIEND.isFriend((class_1657)entity)) {
                return this.friendsC.getValue();
            }
            return this.playersC.getValue();
        }
        if (entity instanceof class_1511) {
            return this.crystalsC.getValue();
        }
        return switch (entity.method_5864().method_5891()) {
            case class_1311.field_6294, class_1311.field_6300 -> this.creaturesC.getValue();
            case class_1311.field_6302 -> this.monstersC.getValue();
            case class_1311.field_6303, class_1311.field_24460 -> this.ambientsC.getValue();
            default -> this.othersC.getValue();
        };
    }

    public boolean drawBox(class_287 bufferBuilder, @NotNull class_1297 ent, Matrix4f matrix, class_332 context) {
        double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)mc.method_60646().method_60637(true);
        double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)mc.method_60646().method_60637(true);
        double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)mc.method_60646().method_60637(true);
        class_238 axisAlignedBB2 = ent.method_5829();
        class_238 axisAlignedBB = new class_238(axisAlignedBB2.field_1323 - ent.method_23317() + x - 0.05, axisAlignedBB2.field_1322 - ent.method_23318() + y, axisAlignedBB2.field_1321 - ent.method_23321() + z - 0.05, axisAlignedBB2.field_1320 - ent.method_23317() + x + 0.05, axisAlignedBB2.field_1325 - ent.method_23318() + y + 0.15, axisAlignedBB2.field_1324 - ent.method_23321() + z + 0.05);
        class_243[] vectors = new class_243[]{new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)};
        Color col = this.getEntityColor(ent);
        Vector4d position = null;
        for (class_243 vector : vectors) {
            vector = TextUtil.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
            if (!(vector.field_1350 > 0.0) || !(vector.field_1350 < 1.0)) continue;
            if (position == null) {
                position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0);
            }
            position.x = Math.min(vector.field_1352, position.x);
            position.y = Math.min(vector.field_1351, position.y);
            position.z = Math.max(vector.field_1352, position.z);
            position.w = Math.max(vector.field_1351, position.w);
        }
        if (position != null) {
            class_1309 lent;
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            double endPosY = position.w;
            if (this.outline.getValue()) {
                this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - 1.0), (float)posY, 1.5f, (float)(endPosY - posY + 0.5), Color.BLACK);
                this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - 1.0), (float)(posY - 0.5), (float)(endPosX - posX + 1.5), 1.5f, Color.BLACK);
                this.drawRectToBuffer(bufferBuilder, matrix, (float)(endPosX - 1.0), (float)posY, 1.5f, (float)(endPosY - posY + 0.5), Color.BLACK);
                this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - 1.0), (float)(endPosY - 1.0), (float)(endPosX - posX + 1.5), 1.5f, Color.BLACK);
                this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - 0.5), (float)posY, 0.5f, (float)(endPosY - posY), col);
                this.drawRectToBuffer(bufferBuilder, matrix, (float)posX, (float)(endPosY - 0.5), (float)(endPosX - posX), 0.5f, col);
                this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - 0.5), (float)posY, (float)(endPosX - posX + 0.5), 0.5f, col);
                this.drawRectToBuffer(bufferBuilder, matrix, (float)(endPosX - 0.5), (float)posY, 0.5f, (float)(endPosY - posY), col);
            }
            if (ent instanceof class_1309 && (lent = (class_1309)ent).method_6032() != 0.0f && this.renderHealth.getValue()) {
                this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - 4.0), (float)posY, 1.0f, (float)(endPosY - posY), Color.BLACK);
                Color color = this.getcolor(lent.method_6032());
                float healthHeight = (float)(endPosY - (endPosY + (posY - endPosY) * (double)lent.method_6032() / (double)lent.method_6063()));
                this.drawRectToBuffer(bufferBuilder, matrix, (float)(posX - 4.0), (float)(endPosY - (double)healthHeight), 1.0f, healthHeight, color);
            }
            if (ent instanceof class_1657) {
                class_1657 player = (class_1657)ent;
                if (this.renderArmor.getValue()) {
                    double height = (endPosY - posY) / 4.0;
                    ArrayList<class_1799> stacks = new ArrayList<class_1799>();
                    stacks.add((class_1799)player.method_31548().field_7548.get(3));
                    stacks.add((class_1799)player.method_31548().field_7548.get(2));
                    stacks.add((class_1799)player.method_31548().field_7548.get(1));
                    stacks.add((class_1799)player.method_31548().field_7548.get(0));
                    int i = -1;
                    for (class_1799 armor : stacks) {
                        ++i;
                        if (armor.method_7960()) continue;
                        float durability = armor.method_7936() - armor.method_7919();
                        int percent = (int)(durability / (float)armor.method_7936() * 100.0f);
                        double finalH = height * (double)(percent / 100);
                        this.drawRectToBuffer(bufferBuilder, matrix, (float)(endPosX + 1.5), (float)((double)((float)posY) + height * (double)i + 1.2 * (double)(i + 1)), 1.5f, (float)finalH, this.armorDuraColor.getValue());
                    }
                }
            }
            return true;
        }
        return false;
    }

    private void drawRectToBuffer(class_287 bufferBuilder, Matrix4f matrix, float x, float y, float width, float height, Color color) {
        float a = (float)color.getAlpha() / 255.0f;
        float r = (float)color.getRed() / 255.0f;
        float g = (float)color.getGreen() / 255.0f;
        float b = (float)color.getBlue() / 255.0f;
        bufferBuilder.method_22918(matrix, x, y, 0.0f).method_22915(r, g, b, a);
        bufferBuilder.method_22918(matrix, x, y + height, 0.0f).method_22915(r, g, b, a);
        bufferBuilder.method_22918(matrix, x + width, y + height, 0.0f).method_22915(r, g, b, a);
        bufferBuilder.method_22918(matrix, x + width, y, 0.0f).method_22915(r, g, b, a);
    }

    public void drawText(class_1297 ent, class_332 context) {
        double x = ent.field_6014 + (ent.method_23317() - ent.field_6014) * (double)mc.method_60646().method_60637(true);
        double y = ent.field_6036 + (ent.method_23318() - ent.field_6036) * (double)mc.method_60646().method_60637(true);
        double z = ent.field_5969 + (ent.method_23321() - ent.field_5969) * (double)mc.method_60646().method_60637(true);
        class_238 axisAlignedBB2 = ent.method_5829();
        class_238 axisAlignedBB = new class_238(axisAlignedBB2.field_1323 - ent.method_23317() + x - 0.05, axisAlignedBB2.field_1322 - ent.method_23318() + y, axisAlignedBB2.field_1321 - ent.method_23321() + z - 0.05, axisAlignedBB2.field_1320 - ent.method_23317() + x + 0.05, axisAlignedBB2.field_1325 - ent.method_23318() + y + 0.15, axisAlignedBB2.field_1324 - ent.method_23321() + z + 0.05);
        class_243[] vectors = new class_243[]{new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1321), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1323, axisAlignedBB.field_1325, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1322, axisAlignedBB.field_1324), new class_243(axisAlignedBB.field_1320, axisAlignedBB.field_1325, axisAlignedBB.field_1324)};
        Color col = this.getEntityColor(ent);
        Vector4d position = null;
        for (class_243 vector : vectors) {
            vector = TextUtil.worldSpaceToScreenSpace(new class_243(vector.field_1352, vector.field_1351, vector.field_1350));
            if (!(vector.field_1350 > 0.0) || !(vector.field_1350 < 1.0)) continue;
            if (position == null) {
                position = new Vector4d(vector.field_1352, vector.field_1351, vector.field_1350, 0.0);
            }
            position.x = Math.min(vector.field_1352, position.x);
            position.y = Math.min(vector.field_1351, position.y);
            position.z = Math.max(vector.field_1352, position.z);
            position.w = Math.max(vector.field_1351, position.w);
        }
        if (position != null) {
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            double endPosY = position.w;
            if (ent instanceof class_1542) {
                class_1542 entity = (class_1542)ent;
                if (this.drawItem.getValue()) {
                    float diff = (float)((endPosX - posX) / 2.0);
                    float textWidth = FontManager.ui.getWidth(entity.method_5476().getString());
                    float tagX = (float)(posX + (double)diff - (double)(textWidth / 2.0f));
                    int count = entity.method_6983().method_7947();
                    context.method_51433(TwoDESP.mc.field_1772, entity.method_5476().getString(), (int)tagX, (int)(posY - 10.0), this.textcolor.getValue().getRGB(), false);
                    if (this.drawItemC.getValue()) {
                        context.method_51433(TwoDESP.mc.field_1772, "x" + count, (int)(tagX + (float)TwoDESP.mc.field_1772.method_1727(entity.method_5476().getString() + " ")), (int)posY - 10, this.countColor.getValue().getRGB(), false);
                    }
                }
            }
            if (ent instanceof class_1657) {
                class_1657 player = (class_1657)ent;
                if (this.renderArmor.getValue()) {
                    double height = (endPosY - posY) / 4.0;
                    ArrayList<class_1799> stacks = new ArrayList<class_1799>();
                    stacks.add((class_1799)player.method_31548().field_7548.get(3));
                    stacks.add((class_1799)player.method_31548().field_7548.get(2));
                    stacks.add((class_1799)player.method_31548().field_7548.get(1));
                    stacks.add((class_1799)player.method_31548().field_7548.get(0));
                    int i = -1;
                    for (class_1799 armor : stacks) {
                        ++i;
                        if (armor.method_7960()) continue;
                        float durability = armor.method_7936() - armor.method_7919();
                        int percent = (int)(durability / (float)armor.method_7936() * 100.0f);
                        double finalH = height * ((double)percent / 100.0);
                        context.method_51427(armor, (int)(endPosX + 4.0), (int)(posY + height * (double)i + 1.2 * (double)(i + 1) + finalH / 2.0));
                    }
                }
            }
        }
    }

    public Color getcolor(float health) {
        if (health >= 20.0f) {
            return this.hHealth.getValue();
        }
        if (20.0f > health && health > 10.0f) {
            return this.mHealth.getValue();
        }
        return this.lHealth.getValue();
    }

    public static enum TwoDESPMode {
        Setting,
        Target,
        Color;

    }
}

