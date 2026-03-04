/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.util.math.MatrixStack
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.KeyboardInputEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.path.BaritoneUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.core.impl.RotationManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.client.util.math.MatrixStack;

public class Freecam
extends Module {
    public static Freecam INSTANCE;
    private final SliderSetting speed = this.add(new SliderSetting("HSpeed", 1.0, 0.0, 3.0));
    private final SliderSetting hspeed = this.add(new SliderSetting("VSpeed", 0.42, 0.0, 3.0));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private float fakeYaw;
    private float fakePitch;
    private float prevFakeYaw;
    private float prevFakePitch;
    private double fakeX;
    private double fakeY;
    private double fakeZ;
    private double prevFakeX;
    private double prevFakeY;
    private double prevFakeZ;
    private float playerYaw;
    private float playerPitch;

    public Freecam() {
        super("Freecam", Module.Category.Player);
        this.setChinese("\u81ea\u7531\u76f8\u673a");
        INSTANCE = this;
    }

    @Override
    public boolean onEnable() {
        if (Freecam.nullCheck()) {
            this.disable();
        } else {
            this.playerYaw = this.getYaw();
            this.playerPitch = this.getPitch();
            this.fakePitch = this.getPitch();
            this.fakeYaw = this.getYaw();
            this.prevFakePitch = this.fakePitch;
            this.prevFakeYaw = this.fakeYaw;
            this.fakeX = Freecam.mc.field_1724.method_23317();
            this.fakeY = Freecam.mc.field_1724.method_23318() + (double)Freecam.mc.field_1724.method_18381(Freecam.mc.field_1724.method_18376());
            this.fakeZ = Freecam.mc.field_1724.method_23321();
            this.prevFakeX = this.fakeX;
            this.prevFakeY = this.fakeY;
            this.prevFakeZ = this.fakeZ;
        }
        return false;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.rotate.getValue() && Freecam.mc.field_1765 != null && Freecam.mc.field_1765.method_17784() != null) {
            float[] angle = RotationManager.getRotation(Freecam.mc.field_1765.method_17784());
            this.playerYaw = angle[0];
            this.playerPitch = angle[1];
        }
        if (BaritoneUtil.isPathing()) {
            double[] motion = MovementUtil.directionSpeed(this.speed.getValue());
            this.prevFakeX = this.fakeX;
            this.prevFakeY = this.fakeY;
            this.prevFakeZ = this.fakeZ;
            this.fakeX += motion[0];
            this.fakeZ += motion[1];
            if (Freecam.mc.field_1690.field_1903.method_1434()) {
                this.fakeY += this.hspeed.getValue();
            }
            if (Freecam.mc.field_1690.field_1832.method_1434()) {
                this.fakeY -= this.hspeed.getValue();
            }
        }
    }

    @EventListener(priority=200)
    public void onRotate(UpdateRotateEvent event) {
        if (!BaritoneUtil.isPathing() && !event.isModified()) {
            event.setYawWithoutSync(this.playerYaw);
            event.setPitchWithoutSync(this.playerPitch);
        }
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        this.prevFakeYaw = this.fakeYaw;
        this.prevFakePitch = this.fakePitch;
        this.fakeYaw = this.getYaw();
        this.fakePitch = this.getPitch();
    }

    private float getYaw() {
        return Freecam.mc.field_1724.method_36454();
    }

    private float getPitch() {
        return Freecam.mc.field_1724.method_36455();
    }

    @EventListener
    public void onKeyboardInput(KeyboardInputEvent event) {
        if (Freecam.mc.field_1724 != null) {
            double[] motion = MovementUtil.directionSpeed(this.speed.getValue());
            this.prevFakeX = this.fakeX;
            this.prevFakeY = this.fakeY;
            this.prevFakeZ = this.fakeZ;
            this.fakeX += motion[0];
            this.fakeZ += motion[1];
            if (Freecam.mc.field_1690.field_1903.method_1434()) {
                this.fakeY += this.hspeed.getValue();
            }
            if (Freecam.mc.field_1690.field_1832.method_1434()) {
                this.fakeY -= this.hspeed.getValue();
            }
            Freecam.mc.field_1724.field_3913.field_3905 = 0.0f;
            Freecam.mc.field_1724.field_3913.field_3907 = 0.0f;
            Freecam.mc.field_1724.field_3913.field_3904 = false;
            Freecam.mc.field_1724.field_3913.field_3903 = false;
        }
    }

    public float getFakeYaw() {
        return MathUtil.interpolate(this.prevFakeYaw, this.fakeYaw, mc.method_60646().method_60637(true));
    }

    public float getFakePitch() {
        return MathUtil.interpolate(this.prevFakePitch, this.fakePitch, mc.method_60646().method_60637(true));
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

