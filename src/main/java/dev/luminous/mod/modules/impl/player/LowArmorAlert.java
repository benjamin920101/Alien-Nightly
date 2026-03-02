/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1738
 *  net.minecraft.class_1799
 *  net.minecraft.class_332
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1738;
import net.minecraft.class_1799;
import net.minecraft.class_332;

public class LowArmorAlert
extends Module {
    public static LowArmorAlert INSTANCE;
    private final SliderSetting threshold = this.add(new SliderSetting("Threshold", 35.0, 0.0, 100.0, 1.0));
    private final SliderSetting yOffset = this.add(new SliderSetting("YOffset", -60.0, 0.0, 200.0, 1.0));
    private long lastFlashTime;

    public LowArmorAlert() {
        super("LowArmorAlert", Module.Category.Player);
        this.setChinese("\u76d4\u7532\u4f4e\u8010\u4e45\u8b66\u62a5");
        INSTANCE = this;
    }

    @Override
    public void onRender2D(class_332 drawContext, float tickDelta) {
        class_1799 boots;
        class_1799 leggings;
        class_1799 chestplate;
        if (LowArmorAlert.nullCheck()) {
            return;
        }
        double thresholdValue = this.threshold.getValue();
        int screenWidth = mc.method_22683().method_4486();
        int screenHeight = mc.method_22683().method_4502();
        int yOffset = (int)this.yOffset.getValue();
        boolean hasLowDurability = false;
        class_1799 helmet = (class_1799)LowArmorAlert.mc.field_1724.method_31548().field_7548.get(3);
        if (!helmet.method_7960() && helmet.method_7909() instanceof class_1738) {
            float f;
            float durabilityPercent4 = this.getDurabilityPercent(helmet);
            if ((double)f < thresholdValue) {
                hasLowDurability = true;
            }
        }
        if (!(chestplate = (class_1799)LowArmorAlert.mc.field_1724.method_31548().field_7548.get(2)).method_7960() && chestplate.method_7909() instanceof class_1738) {
            float f;
            float durabilityPercent3 = this.getDurabilityPercent(chestplate);
            if ((double)f < thresholdValue) {
                hasLowDurability = true;
            }
        }
        if (!(leggings = (class_1799)LowArmorAlert.mc.field_1724.method_31548().field_7548.get(1)).method_7960() && leggings.method_7909() instanceof class_1738) {
            float f;
            float durabilityPercent2 = this.getDurabilityPercent(leggings);
            if ((double)f < thresholdValue) {
                hasLowDurability = true;
            }
        }
        if (!(boots = (class_1799)LowArmorAlert.mc.field_1724.method_31548().field_7548.get(0)).method_7960() && boots.method_7909() instanceof class_1738) {
            float f;
            float durabilityPercent = this.getDurabilityPercent(boots);
            if ((double)f < thresholdValue) {
                hasLowDurability = true;
            }
        }
        if (hasLowDurability) {
            this.drawWarning(drawContext, "Your armor durability is low!", screenWidth, screenHeight, yOffset);
        }
    }

    private float getDurabilityPercent(class_1799 stack) {
        if (stack.method_7960()) {
            return 0.0f;
        }
        float maxDamage = stack.method_7936();
        float currentDamage = stack.method_7919();
        float durability = maxDamage - currentDamage;
        return durability / maxDamage * 100.0f;
    }

    private void drawWarning(class_332 drawContext, String text, int screenWidth, int screenHeight, int yOffset) {
        int textWidth = LowArmorAlert.mc.field_1772.method_1727(text);
        Objects.requireNonNull(LowArmorAlert.mc.field_1772);
        int textHeight = 9;
        int x = (screenWidth - textWidth) / 2;
        int y = screenHeight / 2 - 100 + yOffset;
        long currentTime = System.currentTimeMillis();
        boolean shouldFlash = currentTime - this.lastFlashTime < 250L;
        boolean bl = shouldFlash;
        if (currentTime - this.lastFlashTime >= 500L) {
            this.lastFlashTime = currentTime;
        }
        if (shouldFlash) {
            int padding = 2;
            int bgColor = -2136342528;
            int left = x - padding;
            int top = y - padding;
            int right = x + textWidth + padding;
            int bottom = y + textHeight + padding;
            drawContext.method_25294(left + 1, top, right - 1, top + 1, bgColor);
            drawContext.method_25294(left + 1, bottom - 1, right - 1, bottom, bgColor);
            drawContext.method_25294(left, top + 1, left + 1, bottom - 1, bgColor);
            drawContext.method_25294(right - 1, top + 1, right, bottom - 1, bgColor);
            drawContext.method_25294(left + 1, top + 1, right - 1, bottom - 1, bgColor);
        }
        drawContext.method_51433(LowArmorAlert.mc.field_1772, text, x, y, -65536, false);
    }
}

