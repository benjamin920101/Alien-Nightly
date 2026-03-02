/*
 * Decompiled with CFR 0.152.
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.utils.math.AnimateUtil;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;

public class MotionCamera
extends Module {
    public static MotionCamera INSTANCE;
    public final BooleanSetting noFirstPerson = this.add(new BooleanSetting("NoFirstPerson", true));
    public final SliderSetting firstPersonSpeed = this.add(new SliderSetting("FirstPersonSpeed", 0.6, 0.0, 1.0, 0.01));
    public final SliderSetting speed = this.add(new SliderSetting("Speed", 0.3, 0.0, 1.0, 0.01));
    private double fakeX;
    private double fakeY;
    private double fakeZ;
    private double prevFakeX;
    private double prevFakeY;
    private double prevFakeZ;

    public MotionCamera() {
        super("MotionCamera", Module.Category.Render);
        INSTANCE = this;
        this.setChinese("\u8fd0\u52a8\u76f8\u673a");
    }

    public boolean on() {
        return this.isOn() && (!this.noFirstPerson.getValue() || !MotionCamera.mc.field_1690.method_31044().method_31034());
    }

    @Override
    public boolean onEnable() {
        if (!MotionCamera.nullCheck()) {
            this.fakeX = MotionCamera.mc.field_1724.method_23317();
            this.fakeY = MotionCamera.mc.field_1724.method_23318() + (double)MotionCamera.mc.field_1724.method_18381(MotionCamera.mc.field_1724.method_18376());
            this.fakeZ = MotionCamera.mc.field_1724.method_23321();
            this.prevFakeX = this.fakeX;
            this.prevFakeY = this.fakeY;
            this.prevFakeZ = this.fakeZ;
        }
        return false;
    }

    @EventListener
    public void onUpdate(ClientTickEvent event) {
        if (!event.isPre() && !MotionCamera.nullCheck()) {
            this.prevFakeX = this.fakeX;
            this.prevFakeY = this.fakeY;
            this.prevFakeZ = this.fakeZ;
            double speed = MotionCamera.mc.field_1690.method_31044().method_31034() ? this.firstPersonSpeed.getValue() : this.speed.getValue();
            this.fakeX = AnimateUtil.animate(this.fakeX, MotionCamera.mc.field_1724.method_23317(), speed);
            this.fakeY = AnimateUtil.animate(this.fakeY, MotionCamera.mc.field_1724.method_23318() + (double)MotionCamera.mc.field_1724.method_18381(MotionCamera.mc.field_1724.method_18376()), speed);
            this.fakeZ = AnimateUtil.animate(this.fakeZ, MotionCamera.mc.field_1724.method_23321(), speed);
        }
    }

    public double getFakeX() {
        return MathUtil.interpolate(this.prevFakeX, this.fakeX, (double)mc.method_60646().method_60637(true));
    }

    public double getFakeY() {
        return MathUtil.interpolate(this.prevFakeY, this.fakeY, (double)mc.method_60646().method_60637(true));
    }

    public double getFakeZ() {
        return MathUtil.interpolate(this.prevFakeZ, this.fakeZ, (double)mc.method_60646().method_60637(true));
    }
}

