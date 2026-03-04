/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.option.Perspective
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.math.FadeUtils;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.option.Perspective;

public class CameraClip
extends Module {
    public static CameraClip INSTANCE;
    public final SliderSetting distance = this.add(new SliderSetting("Distance", 4.0, 1.0, 20.0));
    public final SliderSetting animateTime = this.add(new SliderSetting("AnimationTime", 200, 0, 1000));
    private final EnumSetting<Easing> ease = this.add(new EnumSetting<Easing>("Ease", Easing.CubicInOut));
    final FadeUtils animation = new FadeUtils(300L);
    private final BooleanSetting noFront = this.add(new BooleanSetting("NoFront", false));
    boolean first = false;

    public CameraClip() {
        super("CameraClip", Module.Category.Render);
        this.setChinese("\u6444\u50cf\u673a\u7a7f\u5899");
        INSTANCE = this;
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        if (CameraClip.mc.field_1690.method_31044() == class_5498.field_26666 && this.noFront.getValue()) {
            CameraClip.mc.field_1690.method_31043(class_5498.field_26664);
        }
        this.animation.setLength(this.animateTime.getValueInt());
        if (CameraClip.mc.field_1690.method_31044() == class_5498.field_26664) {
            if (!this.first) {
                this.first = true;
                this.animation.reset();
            }
        } else if (this.first) {
            this.first = false;
            this.animation.reset();
        }
    }

    public double getDistance() {
        double quad = CameraClip.mc.field_1690.method_31044() == class_5498.field_26664 ? 1.0 - this.animation.ease(this.ease.getValue()) : this.animation.ease(this.ease.getValue());
        return this.distance.getValue() * quad - 1.0 + 1.0;
    }
}

