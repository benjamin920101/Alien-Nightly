/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1922
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_2680
 *  net.minecraft.class_4587
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2680;
import net.minecraft.class_4587;

public class PhaseESP
extends Module {
    public static PhaseESP INSTANCE;
    private final SliderSetting distance = this.add(new SliderSetting("Distance", 0.1, 0.0, 1.0, 0.1));
    private final SliderSetting bevelDistance = this.add(new SliderSetting("BevelDistance", 0.2, 0.0, 1.0, 0.1));
    private final ColorSetting safeFill = this.add(new ColorSetting("SafeFill", new Color(0, 255, 0, 50)).injectBoolean(true));
    private final ColorSetting safeBox = this.add(new ColorSetting("SafeBox", new Color(0, 255, 0, 100)).injectBoolean(true));
    private final ColorSetting semiSafeFill = this.add(new ColorSetting("SemiSafeFill", new Color(244, 255, 0, 50)).injectBoolean(true));
    private final ColorSetting semiSafeBox = this.add(new ColorSetting("SemiSafeBox", new Color(244, 255, 0, 100)).injectBoolean(true));
    private final ColorSetting unsafeFill = this.add(new ColorSetting("UnsafeFill", new Color(148, 0, 0, 50)).injectBoolean(true));
    private final ColorSetting unsafeBox = this.add(new ColorSetting("UnsafeBox", new Color(148, 0, 0, 100)).injectBoolean(true));
    List<class_2338> safe = new ArrayList<class_2338>();
    List<class_2338> semiSafe = new ArrayList<class_2338>();
    List<class_2338> unsafe = new ArrayList<class_2338>();
    int[] offsets = new int[]{1, 0, -1};

    public PhaseESP() {
        super("PhaseESP", Module.Category.Render);
        this.setChinese("\u7a7f\u5899\u663e\u793a");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.safe.clear();
        this.semiSafe.clear();
        this.unsafe.clear();
        for (int x : this.offsets) {
            for (int z : this.offsets) {
                class_2248 downBlock;
                class_2338 pos = PhaseESP.mc.field_1724.method_24515().method_10069(x, 0, z);
                double d = PhaseESP.mc.field_1724.method_19538().method_1022(pos.method_61082());
                double d2 = x != 0 && z != 0 ? this.bevelDistance.getValue() + 1.0 : this.distance.getValue() + 0.8;
                if (!(d <= d2)) continue;
                class_2680 blockState = PhaseESP.mc.field_1687.method_8320(pos);
                class_2338 downPos = pos.method_10074();
                if (blockState.method_26204() == class_2246.field_9987) {
                    downBlock = PhaseESP.mc.field_1687.method_8320(downPos).method_26204();
                    if (downBlock == class_2246.field_9987) {
                        this.safe.add(pos);
                        continue;
                    }
                    this.unsafe.add(pos);
                    continue;
                }
                if (!blockState.method_26234((class_1922)PhaseESP.mc.field_1687, pos)) continue;
                downBlock = PhaseESP.mc.field_1687.method_8320(downPos).method_26204();
                if (downBlock == class_2246.field_9987) {
                    this.semiSafe.add(pos);
                    continue;
                }
                this.unsafe.add(pos);
            }
        }
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        this.draw(matrixStack, this.safe, this.safeFill, this.safeBox);
        this.draw(matrixStack, this.unsafe, this.unsafeFill, this.unsafeBox);
        this.draw(matrixStack, this.semiSafe, this.semiSafeFill, this.semiSafeBox);
    }

    private void draw(class_4587 matrixStack, List<class_2338> list, ColorSetting fill, ColorSetting box) {
        for (class_2338 pos : list) {
            class_238 espBox = new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)pos.method_10264(), (double)(pos.method_10260() + 1));
            if (fill.booleanValue) {
                Render3DUtil.drawFill(matrixStack, espBox, fill.getValue());
            }
            if (!box.booleanValue) continue;
            Render3DUtil.drawBox(matrixStack, espBox, box.getValue());
        }
    }

    public static enum Type {
        None,
        Air,
        Normal,
        Bedrock;

    }
}

