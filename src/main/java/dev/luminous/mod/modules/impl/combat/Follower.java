/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_4587
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.events.impl.SendMovementPacketsEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.utils.math.Animation;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.JelloUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.asm.accessors.IEntity;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4587;

public class Follower
extends Module {
    public static Follower INSTANCE;
    public class_1297 target;
    private final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.LowestDistance));
    private final SliderSetting range = this.add(new SliderSetting("Range", 50.0, 5.0, 100.0));
    private final BooleanSetting onlyElytraFly = this.add(new BooleanSetting("OnlyElytraFly", true));
    private final EnumSetting<TargetESP> renderMode = this.add(new EnumSetting<TargetESP>("RenderMode", TargetESP.Fill));
    private final SliderSetting animationTime = this.add(new SliderSetting("AnimationTime", 200.0, 0.0, 2000.0, 1.0));
    private final EnumSetting<Easing> ease = this.add(new EnumSetting<Easing>("Ease", Easing.CubicInOut));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(160, 0, 225, 50)));
    private final ColorSetting outlineColor = this.add(new ColorSetting("OutlineColor", new Color(255, 255, 255, 100)));
    private final ColorSetting hitColor = this.add(new ColorSetting("HitColor", new Color(255, 255, 255, 150)));
    private final ColorSetting hitOutlineColor = this.add(new ColorSetting("HitOutlineColor", new Color(255, 255, 255, 150)));
    private final Animation animation = new Animation();
    private final BooleanSetting render = this.add(new BooleanSetting("Render", true));
    private final SliderSetting rotationSpeed = this.add(new SliderSetting("RotationSpeed", 180.0, 10.0, 360.0));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 5.0, 0.0, 10.0));

    public Follower() {
        super("Follower", Module.Category.Combat);
        this.setChinese("\u81ea\u52a8\u8ffd\u8e2a");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.target == null ? null : this.target.method_5477().getString();
    }

    @Override
    public void onDisable() {
        this.target = null;
    }

    @EventListener
    public void onTick(TickEvent event) {
        if (!Follower.nullCheck()) {
            if (this.onlyElytraFly.getValue() && !ElytraFly.INSTANCE.isOn()) {
                this.target = null;
                return;
            }
            this.target = this.findTarget();
        }
    }

    @EventListener
    public void onSendMovementPackets(SendMovementPacketsEvent event) {
    }

    @EventListener
    public void onRotate(RotationEvent event) {
        if (!(Follower.nullCheck() || this.target == null || this.onlyElytraFly.getValue() && !ElytraFly.INSTANCE.isOn())) {
            class_243 hitVec = this.getHitVec(this.target);
            event.setTarget(hitVec, this.rotationSpeed.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        if (this.target != null && this.render.getValue()) {
            this.doRender(matrixStack, mc.method_60646().method_60637(true), this.target);
        }
    }

    public void doRender(class_4587 stack, float partialTicks, class_1297 entity) {
        switch (this.renderMode.getValue().ordinal()) {
            case 0: {
                Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009(0.0, 0.1, 0.0), ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), this.animation.get(0.0, this.animationTime.getValueInt(), this.ease.getValue())), ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), this.animation.get(0.0, this.animationTime.getValueInt(), this.ease.getValue())), false, true);
                break;
            }
            case 1: {
                Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009(0.0, 0.1, 0.0), ColorUtil.fadeColor(this.color.getValue(), this.hitColor.getValue(), this.animation.get(0.0, this.animationTime.getValueInt(), this.ease.getValue())), ColorUtil.fadeColor(this.outlineColor.getValue(), this.hitOutlineColor.getValue(), this.animation.get(0.0, this.animationTime.getValueInt(), this.ease.getValue())), true, true);
                break;
            }
            case 2: {
                JelloUtil.drawJello(stack, entity, this.color.getValue());
                break;
            }
            case 3: {
                Render3DUtil.drawTargetEsp(stack, this.target, this.color.getValue());
            }
        }
    }

    private class_1297 findTarget() {
        class_1297 bestTarget = null;
        for (class_1297 entity : Alien.THREAD.getEntities()) {
            if (!this.isValidTarget(entity)) continue;
            if (bestTarget == null) {
                bestTarget = entity;
                continue;
            }
            double distance = Follower.mc.field_1724.method_33571().method_1022(entity.method_19538());
            float health = EntityUtil.getHealth(entity);
            switch (this.mode.getValue().ordinal()) {
                case 0: {
                    double bestDistance = Follower.mc.field_1724.method_33571().method_1022(bestTarget.method_19538());
                    if (!(distance < bestDistance)) break;
                    bestTarget = entity;
                    break;
                }
                case 1: {
                    double currentTargetDistance;
                    float bestHealth = EntityUtil.getHealth(bestTarget);
                    if (health < bestHealth) {
                        bestTarget = entity;
                        break;
                    }
                    if (health != bestHealth || !(distance < (currentTargetDistance = Follower.mc.field_1724.method_33571().method_1022(bestTarget.method_19538())))) break;
                    bestTarget = entity;
                }
            }
        }
        return bestTarget;
    }

    private boolean isValidTarget(class_1297 entity) {
        if (!(entity instanceof class_1657)) {
            return false;
        }
        class_1657 player = (class_1657)entity;
        if (entity == Follower.mc.field_1724) {
            return false;
        }
        if (Alien.FRIEND.isFriend(player)) {
            return false;
        }
        if (!entity.method_5805()) {
            return false;
        }
        double distance = Follower.mc.field_1724.method_33571().method_1022(entity.method_19538());
        return !(distance > this.range.getValue());
    }

    private class_243 getHitVec(class_1297 entity) {
        class_243 eyePos = Follower.mc.field_1724.method_33571();
        class_238 box = entity.method_5829();
        double targetCenterX = (box.field_1323 + box.field_1320) / 2.0;
        double targetCenterY = (box.field_1322 + box.field_1325) / 2.0;
        double targetCenterZ = (box.field_1321 + box.field_1324) / 2.0;
        double playerEyeHeight = Follower.mc.field_1724.method_18381(Follower.mc.field_1724.method_18376());
        double heightOffset = playerEyeHeight * 0.5;
        if (entity instanceof class_1657) {
            heightOffset = (double)entity.method_17682() * 0.5;
        }
        double targetY = box.field_1322 + heightOffset;
        double closestX = Math.max(box.field_1323, Math.min(eyePos.field_1352, box.field_1320));
        double closestZ = Math.max(box.field_1321, Math.min(eyePos.field_1350, box.field_1324));
        return new class_243(closestX, targetY, closestZ);
    }

    public static enum Mode {
        LowestDistance,
        LowestHealth;

    }

    public static enum TargetESP {
        Fill,
        Box,
        Jello,
        ThunderHack;

    }
}

