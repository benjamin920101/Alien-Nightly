/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1087
 *  net.minecraft.class_124
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1887
 *  net.minecraft.class_1890
 *  net.minecraft.class_1937
 *  net.minecraft.class_243
 *  net.minecraft.class_308
 *  net.minecraft.class_327$class_6415
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_4597$class_4598
 *  net.minecraft.class_4608
 *  net.minecraft.class_640
 *  net.minecraft.class_6880
 *  net.minecraft.class_7833
 *  net.minecraft.class_811
 *  net.minecraft.class_9304
 *  org.joml.Matrix4f
 *  org.lwjgl.opengl.GL11
 */
package dev.luminous.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.Render3DEvent;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.TextRadar;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1087;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_308;
import net.minecraft.class_327;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4608;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_7833;
import net.minecraft.class_811;
import net.minecraft.class_9304;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class NameTags
extends Module {
    public static NameTags INSTANCE;
    final ColorSetting colorConfig = this.add(new ColorSetting("Color", new Color(255, 255, 255)));
    final ColorSetting friendConfig = this.add(new ColorSetting("Friend", new Color(155, 155, 255)).injectBoolean(true));
    final ColorSetting invisibleConfig = this.add(new ColorSetting("Invisible", new Color(200, 200, 200)).injectBoolean(true));
    final ColorSetting died = this.add(new ColorSetting("Died", new Color(180, 0, 0)).injectBoolean(true));
    final ColorSetting sneakingConfig = this.add(new ColorSetting("Sneaking", new Color(200, 200, 0)).injectBoolean(true));
    final ColorSetting rectConfig = this.add(new ColorSetting("Rectangle", new Color(0, 0, 0, 100)).injectBoolean(true));
    final BooleanSetting armorConfig = this.add(new BooleanSetting("Armor", true).setParent());
    final BooleanSetting drawItemConfig = this.add(new BooleanSetting("DrawItem", true, this.armorConfig::isOpen));
    final SliderSetting offsetConfig = this.add(new SliderSetting("Offset", -20.0, -30.0, 10.0, 0.01, this.armorConfig::isOpen));
    final BooleanSetting enchantmentsConfig = this.add(new BooleanSetting("Enchantments", true));
    final BooleanSetting durabilityConfig = this.add(new BooleanSetting("Durability", true).setParent());
    final BooleanSetting forceBarConfig = this.add(new BooleanSetting("ForceBar", true, this.durabilityConfig::isOpen));
    final BooleanSetting itemNameConfig = this.add(new BooleanSetting("ItemName", false));
    final BooleanSetting entityIdConfig = this.add(new BooleanSetting("EntityId", false));
    final BooleanSetting gamemodeConfig = this.add(new BooleanSetting("Gamemode", false));
    final BooleanSetting pingConfig = this.add(new BooleanSetting("Ping", true));
    final BooleanSetting healthConfig = this.add(new BooleanSetting("Health", true));
    final BooleanSetting totemsConfig = this.add(new BooleanSetting("Totems", false));
    final SliderSetting scaleConfig = this.add(new SliderSetting("Scale", 1.0, 0.0, 3.0, 0.1));
    final BooleanSetting factorConfig = this.add(new BooleanSetting("Factor", true).setParent());
    final SliderSetting scalingConfig = this.add(new SliderSetting("Scaling", 1.0, 0.0, 3.0, 0.1, this.factorConfig::isOpen));
    final SliderSetting distanceConfig = this.add(new SliderSetting("Distance", 6.0, 0.0, 20.0, 0.1, this.factorConfig::isOpen));
    final SliderSetting heightConfig = this.add(new SliderSetting("Height", 0.0, -3.0, 3.0, 0.01));
    final DecimalFormat df = new DecimalFormat("0.0");

    public NameTags() {
        super("NameTags", "Renders info on player NameTags", Module.Category.Render);
        this.setChinese("\u540d\u5b57\u6807\u7b7e");
        INSTANCE = this;
    }

    @EventListener
    public void onRender3D(Render3DEvent event) {
        if (NameTags.mc.field_1773 != null && mc.method_1560() != null) {
            class_4184 camera = NameTags.mc.field_1773.method_19418();
            RenderSystem.enableBlend();
            GL11.glDepthFunc((int)519);
            class_4587 matrixStack = new class_4587();
            for (class_1657 class_16572 : Alien.THREAD.getPlayers()) {
                if (!this.died.booleanValue && !class_16572.method_5805() || class_16572 == NameTags.mc.field_1724 && NameTags.mc.field_1690.method_31044().method_31034() || !this.invisibleConfig.booleanValue && class_16572.method_5767()) continue;
                String info = this.getNametagInfo(class_16572);
                class_243 renderPosition = MathUtil.getRenderPosition((class_1297)class_16572, event.tickDelta);
                double x = renderPosition.method_10216();
                double y = renderPosition.method_10214();
                double z = renderPosition.method_10215();
                int width = NameTags.mc.field_1772.method_1727(info);
                float hwidth = (float)width / 2.0f;
                this.renderInfo(info, hwidth, class_16572, x, y, z, camera, matrixStack);
            }
            GL11.glDepthFunc((int)515);
            RenderSystem.disableBlend();
        }
    }

    private void renderInfo(String info, float width, class_1657 entity, double x, double y, double z, class_4184 camera, class_4587 matrices) {
        class_243 pos = camera.method_19326();
        double eyeY = y + (double)entity.method_17682() + (double)(entity.method_5715() ? 0.4f : 0.43f) + (double)this.heightConfig.getValueFloat();
        float scale = (float)((double)(-0.025f * this.scaleConfig.getValueFloat()) + (this.factorConfig.getValue() && pos.method_1028(x, eyeY, z) > (double)(this.distanceConfig.getValueFloat() * this.distanceConfig.getValueFloat()) ? (Math.sqrt(pos.method_1028(x, eyeY, z)) - (double)this.distanceConfig.getValueFloat()) * (double)-0.0025f * (double)this.scalingConfig.getValueFloat() : 0.0));
        matrices.method_22903();
        matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        matrices.method_22904(x - pos.method_10216(), eyeY - pos.method_10214() + (double)((scale / -0.025f - 1.0f) / 4.0f), z - pos.method_10215());
        matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22905(scale, scale, -1.0f);
        if (this.rectConfig.booleanValue) {
            Render2DUtil.drawRect(matrices, -width - 2.0f, -1.0f, width * 2.0f + 3.0f, 10.0f, this.rectConfig.getValue());
        }
        this.drawWithShadow(matrices, info, -width, 0.0f, this.getNametagColor(entity));
        if (this.armorConfig.getValue()) {
            this.renderItems(matrices, entity);
        }
        matrices.method_22909();
    }

    private void drawWithShadow(class_4587 matrices, String info, float x, float y, int color) {
        class_4597.class_4598 immediate = mc.method_22940().method_23000();
        NameTags.mc.field_1772.method_1724(info, x, y, color, true, matrices.method_23760().method_23761(), (class_4597)immediate, class_327.class_6415.field_33994, 0, 0xF000F0);
        immediate.method_22993();
        NameTags.mc.field_1772.method_27521(info, x, y, color, false, matrices.method_23760().method_23761(), (class_4597)immediate, class_327.class_6415.field_33994, 0, 0xF000F0);
        immediate.method_22993();
    }

    private void renderItems(class_4587 matrixStack, class_1657 player) {
        CopyOnWriteArrayList<class_1799> displayItems = new CopyOnWriteArrayList<class_1799>();
        if (!player.method_6079().method_7960()) {
            displayItems.add(player.method_6079());
        }
        player.method_31548().field_7548.forEach(armorStack -> {
            if (!armorStack.method_7960()) {
                displayItems.add((class_1799)armorStack);
            }
        });
        if (!player.method_6047().method_7960()) {
            displayItems.add(player.method_6047());
        }
        Collections.reverse(displayItems);
        float x = 0.0f;
        int n11 = 0;
        for (class_1799 class_17992 : displayItems) {
            x -= 8.0f;
            if (class_17992.method_58657().method_57541() <= n11) continue;
            n11 = class_17992.method_58657().method_57541();
        }
        float y = this.offsetConfig.getValueFloat();
        for (class_1799 stackx : displayItems) {
            GL11.glDepthFunc((int)519);
            if (this.drawItemConfig.getValue()) {
                this.renderItemStack(matrixStack, stackx, x, y + 1.0f);
            }
            this.renderItemOverlay(matrixStack, stackx, x, y + 2.5f);
            matrixStack.method_22905(0.5f, 0.5f, 0.5f);
            if (this.durabilityConfig.getValue()) {
                this.renderDurability(matrixStack, stackx, x + 2.0f, y + 11.5f);
            }
            if (this.enchantmentsConfig.getValue()) {
                this.renderEnchants(matrixStack, stackx, x + 2.0f, y + 7.0f);
            }
            matrixStack.method_22905(2.0f, 2.0f, 2.0f);
            x += 16.0f;
            GL11.glDepthFunc((int)515);
        }
        class_1799 class_17993 = player.method_6047();
        if (!class_17993.method_7960()) {
            matrixStack.method_22905(0.5f, 0.5f, 0.5f);
            if (this.itemNameConfig.getValue()) {
                this.renderItemName(matrixStack, class_17993, y - 4.5f + this.enchantOffset(n11));
            }
            matrixStack.method_22905(2.0f, 2.0f, 2.0f);
        }
    }

    private void renderItemStack(class_4587 matrixStack, class_1799 stack, float x, float y) {
        matrixStack.method_22903();
        matrixStack.method_46416(x, y, 0.0f);
        matrixStack.method_46416(8.0f, 8.0f, 0.0f);
        matrixStack.method_22905(16.0f, 16.0f, 1.0E-8f);
        matrixStack.method_34425(new Matrix4f().scaling(1.0f, -1.0f, 1.0f));
        class_308.method_24210();
        class_1087 model = mc.method_1480().method_4019(stack, (class_1937)NameTags.mc.field_1687, null, 0);
        class_4597.class_4598 i = mc.method_22940().method_23000();
        mc.method_1480().method_23179(stack, class_811.field_4317, false, matrixStack, (class_4597)i, 0xFF0000, class_4608.field_21444, model);
        i.method_22993();
        class_308.method_24211();
        matrixStack.method_22909();
    }

    private void renderItemOverlay(class_4587 matrixStack, class_1799 stack, float x, float y) {
        matrixStack.method_22903();
        if (stack.method_7947() != 1) {
            String string = String.valueOf(stack.method_7947());
            this.drawWithShadow(matrixStack, string, x + 17.0f - (float)NameTags.mc.field_1772.method_1727(string), y + 9.0f, -1);
        }
        if (stack.method_31578() || stack.method_7963() && this.forceBarConfig.getValue()) {
            int i = stack.method_31579();
            int j = stack.method_31580();
            float k = x + 2.0f;
            float l = y + 13.0f;
            Render2DUtil.drawRect(matrixStack, k, l, 13.0f, 2.0f, -16777216);
            Render2DUtil.drawRect(matrixStack, k, l, (float)i, 1.0f, j | 0xFF000000);
        }
        matrixStack.method_22909();
    }

    private void renderDurability(class_4587 matrixStack, class_1799 itemStack, float x, float y) {
        if (itemStack.method_7963()) {
            int n = itemStack.method_7936();
            int n2 = itemStack.method_7919();
            int durability = (int)((float)(n - n2) / (float)n * 100.0f);
            this.drawWithShadow(matrixStack, durability + "%", x * 2.0f, y * 2.0f, ColorUtil.hslToColor((float)(n - n2) / (float)n * 120.0f, 100.0f, 50.0f, 1.0f).getRGB());
        }
    }

    private void renderEnchants(class_4587 matrixStack, class_1799 itemStack, float x, float y) {
        if (itemStack.method_7909() == class_1802.field_8367) {
            this.drawWithShadow(matrixStack, "God", x * 2.0f, y * 2.0f, -3977663);
        } else if (itemStack.method_7942()) {
            class_9304 enchants = class_1890.method_57532((class_1799)itemStack);
            float n2 = 0.0f;
            for (class_6880 enchantment : enchants.method_57534()) {
                int lvl = enchants.method_57536(enchantment);
                StringBuilder enchantString = new StringBuilder();
                String translatedName = ((class_1887)enchantment.comp_349()).toString().replace("Enchantment ", "");
                if (translatedName.contains("Vanish")) {
                    enchantString.append("\u00a7cVan");
                } else if (translatedName.contains("Bind")) {
                    enchantString.append("\u00a7cBind");
                } else {
                    int maxLen;
                    int n = maxLen = lvl > 1 ? 2 : 3;
                    if (translatedName.length() > maxLen) {
                        translatedName = translatedName.substring(0, maxLen);
                    }
                    enchantString.append(translatedName);
                    enchantString.append(lvl);
                }
                this.drawWithShadow(matrixStack, enchantString.toString(), x * 2.0f, (y + n2) * 2.0f, -1);
                n2 -= 4.5f;
            }
        }
    }

    private float enchantOffset(int n) {
        if (this.enchantmentsConfig.getValue() && n > 2) {
            float value = -2.0f;
            return value - (float)(n - 3) * 4.5f;
        }
        return 0.0f;
    }

    private void renderItemName(class_4587 matrixStack, class_1799 itemStack, float y) {
        String itemName = itemStack.method_7964().getString();
        float width = (float)NameTags.mc.field_1772.method_1727(itemName) / 4.0f;
        this.drawWithShadow(matrixStack, itemName, (0.0f - width) * 2.0f, y * 2.0f, -1);
    }

    private String getNametagInfo(class_1657 player) {
        int totems;
        StringBuilder info = new StringBuilder();
        if (this.gamemodeConfig.getValue()) {
            if (player.method_7337()) {
                info.append(class_124.field_1065);
                info.append("[C] ");
            } else if (player.method_7325()) {
                info.append(class_124.field_1080);
                info.append("[I] ");
            } else {
                info.append(class_124.field_1067);
                info.append("[S] ");
            }
        }
        if (this.pingConfig.getValue()) {
            info.append(this.getEntityPing(player));
            info.append("ms ");
            info.append(class_124.field_1070);
        }
        info.append(player.method_5477().getString());
        info.append(" ");
        if (this.entityIdConfig.getValue()) {
            info.append("ID: ");
            info.append(player.method_5628());
            info.append(" ");
        }
        if (this.healthConfig.getValue()) {
            double health = player.method_6032() + player.method_6067();
            class_124 hcolor = health > 18.0 ? class_124.field_1060 : (health > 16.0 ? class_124.field_1077 : (health > 12.0 ? class_124.field_1054 : (health > 8.0 ? class_124.field_1065 : (health > 4.0 ? class_124.field_1061 : class_124.field_1079))));
            String phealth = this.df.format(health);
            info.append(hcolor);
            info.append(phealth);
            info.append(" ");
        }
        if (this.totemsConfig.getValue() && (totems = Alien.POP.getPop(player)) > 0) {
            class_124 c = TextRadar.getPopColor(totems);
            info.append(c);
            info.append(-totems);
            info.append(" ");
        }
        return info.toString().trim();
    }

    private String getEntityPing(class_1657 entity) {
        if (mc.method_1562() == null) {
            return "\u00a77-1";
        }
        class_640 playerListEntry = mc.method_1562().method_2871(entity.method_5667());
        if (playerListEntry == null) {
            return "\u00a77-1";
        }
        int ping = playerListEntry.method_2959();
        class_124 color = ping >= 200 ? class_124.field_1061 : (ping >= 100 ? class_124.field_1054 : class_124.field_1060);
        return color.toString() + ping;
    }

    private int getNametagColor(class_1657 player) {
        if (this.friendConfig.booleanValue && player.method_5476() != null && Alien.FRIEND.isFriend(player)) {
            return this.friendConfig.getValue().getRGB();
        }
        if (this.invisibleConfig.booleanValue && player.method_5767()) {
            return this.invisibleConfig.getValue().getRGB();
        }
        if (this.sneakingConfig.booleanValue && player.method_5715()) {
            return this.sneakingConfig.getValue().getRGB();
        }
        return !player.method_5805() ? this.died.getValue().getRGB() : this.colorConfig.getValue().getRGB();
    }
}

