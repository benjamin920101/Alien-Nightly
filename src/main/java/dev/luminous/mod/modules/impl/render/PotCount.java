/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1293
 *  net.minecraft.class_1294
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.Render2DEvent;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Objects;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_332;
import net.minecraft.class_4587;

public class PotCount
extends Module {
    public final SliderSetting posX = this.add(new SliderSetting("PosX", 10.0, 0.0, 1920.0, 1.0));
    public final SliderSetting posY = this.add(new SliderSetting("PosY", 10.0, 0.0, 1080.0, 1.0));
    public final SliderSetting width = this.add(new SliderSetting("Width", 120.0, 80.0, 200.0, 1.0));
    public final SliderSetting height = this.add(new SliderSetting("Height", 80.0, 60.0, 150.0, 1.0));
    public final ColorSetting bgColor = this.add(new ColorSetting("Background", new Color(Integer.MIN_VALUE, true)));
    public final ColorSetting borderColor = this.add(new ColorSetting("Border", new Color(-16711936, true)));
    public final BooleanSetting roundedCorners = this.add(new BooleanSetting("Rounded", true));
    public final SliderSetting cornerRadius = this.add(new SliderSetting("CornerRadius", 8.0, 0.0, 20.0, 0.5));
    public final SliderSetting borderThickness = this.add(new SliderSetting("BorderThickness", 2.0, 0.0, 5.0, 0.5));
    public final BooleanSetting enableBlur = this.add(new BooleanSetting("EnableBlur", true));
    public final SliderSetting blurStrength = this.add(new SliderSetting("BlurStrength", 5.0, 1.0, 20.0, 0.5));
    public final SliderSetting blurRadius = this.add(new SliderSetting("BlurRadius", 8.0, 0.0, 20.0, 0.5));
    public final ColorSetting blurOverlay = this.add(new ColorSetting("BlurOverlay", new Color(0x60000000, true)));
    public final EnumSetting<FontMode> fontMode = this.add(new EnumSetting<FontMode>("Font", FontMode.VANILLA));
    public final SliderSetting textScale = this.add(new SliderSetting("TextScale", 1.0, 0.5, 2.0, 0.1));
    public final ColorSetting textColor = this.add(new ColorSetting("TextColor", new Color(-1, true)));
    public final ColorSetting potCountColor = this.add(new ColorSetting("PotCountColor", new Color(-16711936, true)));
    public final ColorSetting timeColor = this.add(new ColorSetting("TimeColor", new Color(-22016, true)));
    public final BooleanSetting showSplashPotCount = this.add(new BooleanSetting("ShowSplashPotion", true));
    public final BooleanSetting showResistanceTime = this.add(new BooleanSetting("ShowResistanceTime", true));
    private final DecimalFormat timeFormat = new DecimalFormat("0.0");

    public PotCount() {
        super("PotCount", Module.Category.Render);
        this.setChinese("\u836f\u6c34\u8ba1\u6570HUD");
    }

    @EventListener
    public void onRender2D(Render2DEvent event) {
        if (PotCount.mc.field_1724 == null || PotCount.mc.field_1687 == null) {
            return;
        }
        class_332 context = event.drawContext;
        class_4587 matrices = context.method_51448();
        float x = this.posX.getValueFloat();
        float y = this.posY.getValueFloat();
        float w = this.width.getValueFloat();
        float h = this.height.getValueFloat();
        if (this.enableBlur.getValue()) {
            this.drawBlurBackground(matrices, x, y, w, h);
        }
        this.drawRoundedBackground(matrices, x, y, w, h);
        this.drawPotionInfo(context, matrices, x, y, w, h);
    }

    private void drawBlurBackground(class_4587 matrices, float x, float y, float w, float h) {
        float radius = this.cornerRadius.getValueFloat();
        float blurStrengthValue = this.blurStrength.getValueFloat();
        int i = 0;
        while ((float)i < blurStrengthValue) {
            float offset = (float)i * 0.5f;
            float alpha = (float)((double)this.blurOverlay.getValue().getAlpha() / 255.0 * (1.0 - (double)((float)i / blurStrengthValue)));
            Color blurColor = new Color(this.blurOverlay.getValue().getRed(), this.blurOverlay.getValue().getGreen(), this.blurOverlay.getValue().getBlue(), (int)(alpha * 255.0f));
            Render2DUtil.drawRoundedRect(matrices, x - offset, y - offset, w + offset * 2.0f, h + offset * 2.0f, radius + offset, blurColor);
            ++i;
        }
    }

    private void drawRoundedBackground(class_4587 matrices, float x, float y, float w, float h) {
        float radius = this.cornerRadius.getValueFloat();
        Render2DUtil.drawRoundedRect(matrices, x, y, w, h, radius, this.bgColor.getValue());
        if (this.borderThickness.getValueFloat() > 0.0f) {
            Render2DUtil.drawRoundedStroke(matrices, x, y, w, h, radius, this.borderColor.getValue(), 32);
        }
    }

    private void drawPotionInfo(class_332 context, class_4587 matrices, float x, float y, float w, float h) {
        float padding = 10.0f;
        float contentX = x + padding;
        float contentY = y + padding;
        float contentWidth = w - padding * 2.0f;
        int splashPotCount = this.countSplashPotions();
        float resistanceTimeLeft = this.getResistanceTimeLeft();
        float currentY = contentY;
        if (this.showSplashPotCount.getValue()) {
            String potText = "POT: " + splashPotCount;
            this.drawTextWithScale(context, matrices, potText, contentX, currentY, contentWidth, this.potCountColor.getValue());
            float textHeight = this.getTextHeight();
            currentY += textHeight + 5.0f;
        }
        if (this.showResistanceTime.getValue()) {
            String timeText = resistanceTimeLeft > 0.0f ? "Time: " + this.timeFormat.format(resistanceTimeLeft) + "s" : "Time: --";
            this.drawTextWithScale(context, matrices, timeText, contentX, currentY, contentWidth, this.timeColor.getValue());
        }
    }

    private void drawTextWithScale(class_332 context, class_4587 matrices, String text, float x, float y, float maxWidth, Color color) {
        matrices.method_22903();
        matrices.method_46416(x, y, 0.0f);
        matrices.method_22905(this.textScale.getValueFloat(), this.textScale.getValueFloat(), 1.0f);
        if (this.fontMode.getValue() == FontMode.VANILLA) {
            float scaledTextWidth = (float)PotCount.mc.field_1772.method_1727(text) * this.textScale.getValueFloat();
            float centeredX = (maxWidth - scaledTextWidth) / (2.0f * this.textScale.getValueFloat());
            context.method_51433(PotCount.mc.field_1772, text, (int)centeredX, 0, color.getRGB(), true);
        } else {
            float scaledTextWidth = FontManager.ui.getWidth(text) * this.textScale.getValueFloat();
            float centeredX = (maxWidth - scaledTextWidth) / (2.0f * this.textScale.getValueFloat());
            FontManager.ui.drawString(matrices, text, (double)centeredX, 0.0, color.getRGB());
        }
        matrices.method_22909();
    }

    private float getTextHeight() {
        float f;
        if (this.fontMode.getValue() == FontMode.VANILLA) {
            Objects.requireNonNull(PotCount.mc.field_1772);
            f = 9.0f;
        } else {
            f = FontManager.ui.getFontHeight();
        }
        float baseHeight = f;
        return baseHeight * this.textScale.getValueFloat();
    }

    private int countSplashPotions() {
        class_1799 offHand;
        if (PotCount.mc.field_1724 == null) {
            return 0;
        }
        int count = 0;
        class_1799 mainHand = PotCount.mc.field_1724.method_6047();
        if (mainHand.method_7909() == class_1802.field_8436) {
            ++count;
        }
        if ((offHand = PotCount.mc.field_1724.method_6079()).method_7909() == class_1802.field_8436) {
            ++count;
        }
        for (int i = 0; i < PotCount.mc.field_1724.method_31548().method_5439(); ++i) {
            class_1799 stack = PotCount.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7909() != class_1802.field_8436) continue;
            count += stack.method_7947();
        }
        return count;
    }

    private float getResistanceTimeLeft() {
        if (PotCount.mc.field_1724 == null) {
            return 0.0f;
        }
        class_1293 resistanceEffect = PotCount.mc.field_1724.method_6112(class_1294.field_5907);
        if (resistanceEffect != null && resistanceEffect.method_5578() >= 3) {
            int durationTicks = resistanceEffect.method_5584();
            float durationSeconds = (float)durationTicks / 20.0f;
            return durationSeconds;
        }
        return 0.0f;
    }

    @Override
    public boolean onEnable() {
        super.onEnable();
        return false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static enum FontMode {
        VANILLA("Vanilla"),
        CUSTOM("Custom");

        private final String name;

        private FontMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

