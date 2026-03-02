/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 *  net.minecraft.class_2708
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_4587
 *  net.minecraft.class_757
 *  net.minecraft.class_9801
 *  org.joml.Matrix4f
 *  org.lwjgl.opengl.GL11
 */
package dev.luminous.mod.modules.impl.movement;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.KeyboardInputEvent;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.events.impl.TimerEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.player.Freecam;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2708;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class HoleSnap
extends Module {
    public static HoleSnap INSTANCE;
    public final BooleanSetting any = this.add(new BooleanSetting("AnyHole", true));
    public final SliderSetting timer = this.add(new SliderSetting("Timer", 1.0, 0.1, 8.0, 0.1));
    public final BooleanSetting up = this.add(new BooleanSetting("Up", true));
    public final BooleanSetting grim = this.add(new BooleanSetting("Grim", false));
    public final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
    public final SliderSetting circleSize = this.add(new SliderSetting("CircleSize", 1.0, 0.1f, 2.5));
    public final BooleanSetting fade = this.add(new BooleanSetting("Fade", true));
    public final SliderSetting segments = this.add(new SliderSetting("Segments", 180, 0, 360));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5, 1, 50));
    private final SliderSetting timeoutTicks = this.add(new SliderSetting("TimeOut", 40, 0, 100));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.8, 0.0, 1.0, 0.01, this.grim::getValue));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, this.grim::getValue));
    boolean resetMove = false;
    boolean applyTimer = false;
    class_243 targetPos;
    private class_2338 holePos;
    private int stuckTicks;
    private int enabledTicks;

    public HoleSnap() {
        super("HoleSnap", "HoleSnap", Module.Category.Movement);
        this.setChinese("\u62c9\u5751");
        INSTANCE = this;
    }

    public static class_241 getRotationTo(class_243 posFrom, class_243 posTo) {
        class_243 vec3d = posTo.method_1020(posFrom);
        return HoleSnap.getRotationFromVec(vec3d);
    }

    public static void drawCircle(class_4587 matrixStack, Color color, double circleSize, class_243 pos, int segments) {
        class_243 camPos = HoleSnap.mc.method_31975().field_4344.method_19326();
        RenderSystem.disableDepthTest();
        Matrix4f matrix = matrixStack.method_23760().method_23761();
        RenderSystem.setShader(class_757::method_34540);
        float a = (float)color.getAlpha() / 255.0f;
        float r = (float)color.getRed() / 255.0f;
        float g = (float)color.getGreen() / 255.0f;
        float b = (float)color.getBlue() / 255.0f;
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27381, class_290.field_1576);
        for (double i = 0.0; i < 360.0; i += 360.0 / (double)segments) {
            double x = Math.sin(Math.toRadians(i)) * circleSize;
            double z = Math.cos(Math.toRadians(i)) * circleSize;
            class_243 tempPos = new class_243(pos.field_1352 + x, pos.field_1351, pos.field_1350 + z).method_1031(-camPos.field_1352, -camPos.field_1351, -camPos.field_1350);
            bufferBuilder.method_22918(matrix, (float)tempPos.field_1352, (float)tempPos.field_1351, (float)tempPos.field_1350).method_22915(r, g, b, a);
        }
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        RenderSystem.enableDepthTest();
    }

    private static class_241 getRotationFromVec(class_243 vec) {
        double d = vec.field_1352;
        double d2 = vec.field_1350;
        double xz = Math.hypot(d, d2);
        d2 = vec.field_1350;
        double d3 = vec.field_1352;
        double yaw = HoleSnap.normalizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - 90.0);
        double pitch = HoleSnap.normalizeAngle(Math.toDegrees(-Math.atan2(vec.field_1351, xz)));
        return new class_241((float)yaw, (float)pitch);
    }

    private static double normalizeAngle(double angleIn) {
        double d;
        double angle = angleIn % 360.0;
        if (d >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    @EventListener(priority=-99)
    public void onTimer(TimerEvent event) {
        if (this.applyTimer) {
            event.set(this.timer.getValueFloat());
        }
    }

    @Override
    public boolean onEnable() {
        this.applyTimer = false;
        if (HoleSnap.nullCheck()) {
            this.disable();
        } else {
            this.resetMove = false;
            this.holePos = Alien.HOLE.getHole((float)this.range.getValue(), true, this.any.getValue(), this.up.getValue());
        }
        return false;
    }

    @Override
    public void onDisable() {
        this.holePos = null;
        this.stuckTicks = 0;
        this.enabledTicks = 0;
        if (!HoleSnap.nullCheck() && this.resetMove && !this.grim.getValue()) {
            MovementUtil.setMotionX(0.0);
            MovementUtil.setMotionZ(0.0);
        }
    }

    @EventListener
    public void onReceivePacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof class_2708) {
            this.disable();
        }
    }

    @EventListener(priority=-999)
    public void onKeyInput(KeyboardInputEvent e) {
        if (this.grim.getValue()) {
            if (!AntiCheat.INSTANCE.movementSync()) {
                this.sendMessage("\u00a74HoleSnap require MovementSync.");
                this.disable();
            } else if (!HoleSnap.mc.field_1724.method_3144() && !Freecam.INSTANCE.isOn()) {
                HoleSnap.mc.field_1724.field_3913.field_3907 = 0.0f;
                HoleSnap.mc.field_1724.field_3913.field_3905 = 1.0f;
            }
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.holePos = Alien.HOLE.getHole((float)this.range.getValue(), true, this.any.getValue(), this.up.getValue());
        if (this.holePos == null) {
            this.disable();
        } else {
            ++this.enabledTicks;
            if ((double)this.enabledTicks > this.timeoutTicks.getValue() - 1.0) {
                this.disable();
            } else {
                this.applyTimer = true;
                if (this.grim.getValue()) {
                    if (!HoleSnap.mc.field_1724.method_5805() || HoleSnap.mc.field_1724.method_6128()) {
                        this.disable();
                    } else if (this.stuckTicks > 8) {
                        this.disable();
                    } else if (this.holePos == null) {
                        this.disable();
                    } else {
                        class_2350 facing;
                        class_243 playerPos = HoleSnap.mc.field_1724.method_19538();
                        this.targetPos = new class_243((double)this.holePos.method_10263() + 0.5, HoleSnap.mc.field_1724.method_23318(), (double)this.holePos.method_10260() + 0.5);
                        if (Alien.HOLE.isDoubleHole(this.holePos) && (facing = Alien.HOLE.is3Block(this.holePos)) != null) {
                            this.targetPos = this.targetPos.method_1019(new class_243((double)facing.method_10163().method_10263() * 0.5, (double)facing.method_10163().method_10264() * 0.5, (double)facing.method_10163().method_10260() * 0.5));
                        }
                        this.applyTimer = true;
                        this.resetMove = true;
                        float rotation = HoleSnap.getRotationTo((class_243)playerPos, (class_243)this.targetPos).field_1343;
                        float yawRad = rotation / 180.0f * (float)Math.PI;
                        double dist = playerPos.method_1022(this.targetPos);
                        double cappedSpeed = Math.min(0.2873, dist);
                        double x = (double)(-((float)Math.sin(yawRad))) * cappedSpeed;
                        double z = (double)((float)Math.cos(yawRad)) * cappedSpeed;
                        if (Math.abs(x) < 0.25 && Math.abs(z) < 0.25 && playerPos.field_1351 <= (double)this.holePos.method_10264() + 0.8) {
                            this.disable();
                        } else {
                            this.stuckTicks = HoleSnap.mc.field_1724.field_5976 ? ++this.stuckTicks : 0;
                        }
                    }
                }
            }
        }
    }

    @EventListener
    public void onRotate(RotationEvent event) {
        if (this.grim.getValue() && this.holePos != null) {
            class_2350 facing;
            this.targetPos = new class_243((double)this.holePos.method_10263() + 0.5, HoleSnap.mc.field_1724.method_23318(), (double)this.holePos.method_10260() + 0.5);
            if (Alien.HOLE.isDoubleHole(this.holePos) && (facing = Alien.HOLE.is3Block(this.holePos)) != null) {
                this.targetPos = this.targetPos.method_1019(new class_243((double)facing.method_10163().method_10263() * 0.5, (double)facing.method_10163().method_10264() * 0.5, (double)facing.method_10163().method_10260() * 0.5));
            }
            event.setTarget(this.targetPos, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @EventListener
    public void onMove(MoveEvent event) {
        if (!this.grim.getValue()) {
            if (!HoleSnap.mc.field_1724.method_5805() || HoleSnap.mc.field_1724.method_6128()) {
                this.disable();
            } else if (this.stuckTicks > 8) {
                this.disable();
            } else if (this.holePos == null) {
                this.disable();
            } else {
                class_2350 facing;
                class_243 playerPos = HoleSnap.mc.field_1724.method_19538();
                this.targetPos = new class_243((double)this.holePos.method_10263() + 0.5, HoleSnap.mc.field_1724.method_23318(), (double)this.holePos.method_10260() + 0.5);
                if (Alien.HOLE.isDoubleHole(this.holePos) && (facing = Alien.HOLE.is3Block(this.holePos)) != null) {
                    this.targetPos = this.targetPos.method_1019(new class_243((double)facing.method_10163().method_10263() * 0.5, (double)facing.method_10163().method_10264() * 0.5, (double)facing.method_10163().method_10260() * 0.5));
                }
                this.applyTimer = true;
                this.resetMove = true;
                float rotation = HoleSnap.getRotationTo((class_243)playerPos, (class_243)this.targetPos).field_1343;
                float yawRad = rotation / 180.0f * (float)Math.PI;
                double dist = playerPos.method_1022(this.targetPos);
                double cappedSpeed = Math.min(0.2873, dist);
                double x = (double)(-((float)Math.sin(yawRad))) * cappedSpeed;
                double z = (double)((float)Math.cos(yawRad)) * cappedSpeed;
                event.setX(x);
                event.setZ(z);
                if (Math.abs(x) < 0.1 && Math.abs(z) < 0.1 && playerPos.field_1351 <= (double)this.holePos.method_10264() + 0.5) {
                    this.disable();
                }
                this.stuckTicks = HoleSnap.mc.field_1724.field_5976 ? ++this.stuckTicks : 0;
            }
        }
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        if (this.targetPos != null && this.holePos != null) {
            GL11.glEnable((int)3042);
            Color color = this.color.getValue();
            class_243 pos = new class_243(this.targetPos.field_1352, (double)this.holePos.method_10264(), this.targetPos.method_10215());
            if (this.fade.getValue()) {
                double temp = 0.01;
                for (double i = 0.0; i < this.circleSize.getValue(); i += temp) {
                    HoleSnap.drawCircle(matrixStack, ColorUtil.injectAlpha(color, (int)Math.min((double)(color.getAlpha() * 2) / (this.circleSize.getValue() / temp), 255.0)), i, pos, this.segments.getValueInt());
                }
            } else {
                HoleSnap.drawCircle(matrixStack, color, this.circleSize.getValue(), pos, this.segments.getValueInt());
            }
            GL11.glDisable((int)3042);
        }
    }
}

