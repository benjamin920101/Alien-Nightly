/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1747
 *  net.minecraft.class_2246
 *  net.minecraft.class_238
 *  net.minecraft.class_239
 *  net.minecraft.class_3965
 *  net.minecraft.class_4587
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_1747;
import net.minecraft.class_2246;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.minecraft.class_4587;

public class AirPlace
extends Module {
    public static AirPlace INSTANCE;
    public final BooleanSetting module = this.add(new BooleanSetting("Module", true));
    public final BooleanSetting grimBypass = this.add(new BooleanSetting("GrimBypass", false));
    public final BooleanSetting crossHair = this.add(new BooleanSetting("Crosshair", true).setParent());
    private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 6.0, this.crossHair::isOpen));
    private final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 0, 0, 50), this.crossHair::isOpen).injectBoolean(true));
    private final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 0, 0, 100), this.crossHair::isOpen).injectBoolean(true));
    private class_3965 hit;
    private int cooldown;

    public AirPlace() {
        super("AirPlace", Module.Category.Player);
        this.setChinese("\u7a7a\u6c14\u653e\u7f6e");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.crossHair.getValue()) {
            class_3965 bhr;
            class_239 class_2392;
            if (this.cooldown > 0) {
                --this.cooldown;
            }
            this.hit = (class_2392 = mc.method_1560().method_5745(this.range.getValue(), 0.0f, false)) instanceof class_3965 ? (bhr = (class_3965)class_2392) : null;
            if (this.hit == null || !AirPlace.mc.field_1687.method_8320(this.hit.method_17777()).method_26204().equals(class_2246.field_10124) || !(AirPlace.mc.field_1724.method_6047().method_7909() instanceof class_1747)) {
                return;
            }
            boolean main = AirPlace.mc.field_1724.method_6047().method_7909() instanceof class_1747;
            if (AirPlace.mc.field_1690.field_1904.method_1434() && main && this.cooldown <= 0) {
                BlockUtil.airPlace(this.hit.method_17777(), false);
                this.cooldown = 2;
            }
        }
    }

    @Override
    public void onRender3D(class_4587 stack) {
        if (this.crossHair.getValue()) {
            if (this.hit == null || !AirPlace.mc.field_1687.method_8320(this.hit.method_17777()).method_26204().equals(class_2246.field_10124) || !(AirPlace.mc.field_1724.method_6047().method_7909() instanceof class_1747)) {
                return;
            }
            Render3DUtil.draw3DBox(stack, new class_238(this.hit.method_17777()), this.fill.getValue(), this.box.getValue(), this.box.booleanValue, this.fill.booleanValue);
        }
    }
}

