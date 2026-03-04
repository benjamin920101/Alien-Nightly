/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.mob.MobEntity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.passive.AnimalEntity
 *  net.minecraft.entity.mob.SlimeEntity
 *  net.minecraft.entity.passive.VillagerEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket$InteractType
 *  net.minecraft.network.packet.c2s.play.HandSwingC2SPacket
 *  net.minecraft.entity.passive.WanderingTraderEntity
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.entity.attribute.EntityAttributes
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Animation;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.JelloUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.asm.accessors.IEntity;
import dev.luminous.asm.accessors.ILivingEntity;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.Criticals;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.impl.movement.Velocity;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import dev.luminous.mod.modules.settings.enums.Timing;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributes;

public class Aura
extends Module {
    public static Aura INSTANCE;
    public static class_1297 target;
    public final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    public final SliderSetting range = this.add(new SliderSetting("Range", 6.0, (double)0.1f, 7.0, () -> this.page.getValue() == Page.General));
    private final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 8.0, (double)0.1f, 14.0, () -> this.page.getValue() == Page.General));
    private final EnumSetting<Cooldown> cooldownMode = this.add(new EnumSetting<Cooldown>("CooldownMode", Cooldown.Delay, () -> this.page.getValue() == Page.General));
    private final BooleanSetting reset = this.add(new BooleanSetting("Reset", true, () -> this.page.getValue() == Page.General && this.cooldownMode.is(Cooldown.Delay)));
    private final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("Swing", SwingSide.All, () -> this.page.getValue() == Page.General));
    private final SliderSetting hurtTime = this.add(new SliderSetting("HurtTime", 10.0, 0.0, 10.0, 1.0, () -> this.page.getValue() == Page.General));
    private final SliderSetting cooldown = this.add(new SliderSetting("Cooldown", 1.1f, 0.0, 1.2f, 0.01, () -> this.page.getValue() == Page.General));
    private final SliderSetting wallRange = this.add(new SliderSetting("WallRange", 6.0, (double)0.1f, 7.0, () -> this.page.getValue() == Page.General));
    private final BooleanSetting whileEating = this.add(new BooleanSetting("WhileUsing", true, () -> this.page.getValue() == Page.General));
    private final BooleanSetting weaponOnly = this.add(new BooleanSetting("WeaponOnly", true, () -> this.page.getValue() == Page.General));
    private final EnumSetting<Timing> timing = this.add(new EnumSetting<Timing>("Timing", Timing.All, () -> this.page.getValue() == Page.General));
    private final BooleanSetting Players = this.add(new BooleanSetting("Players", true, () -> this.page.getValue() == Page.Target).setParent());
    private final BooleanSetting armorLow = this.add(new BooleanSetting("ArmorLow", true, () -> this.page.getValue() == Page.Target && this.Players.isOpen()));
    private final BooleanSetting Mobs = this.add(new BooleanSetting("Mobs", true, () -> this.page.getValue() == Page.Target));
    private final BooleanSetting Animals = this.add(new BooleanSetting("Animals", true, () -> this.page.getValue() == Page.Target));
    private final BooleanSetting Villagers = this.add(new BooleanSetting("Villagers", true, () -> this.page.getValue() == Page.Target));
    private final BooleanSetting Slimes = this.add(new BooleanSetting("Slimes", true, () -> this.page.getValue() == Page.Target));
    private final EnumSetting<TargetMode> targetMode = this.add(new EnumSetting<TargetMode>("Filter", TargetMode.DISTANCE, () -> this.page.getValue() == Page.Target));
    private final EnumSetting<TargetESP> mode = this.add(new EnumSetting<TargetESP>("TargetESP", TargetESP.Fill, () -> this.page.getValue() == Page.Render));
    private final SliderSetting animationTime = this.add(new SliderSetting("AnimationTime", 200.0, 0.0, 2000.0, 1.0, () -> this.page.getValue() == Page.Render));
    private final EnumSetting<Easing> ease = this.add(new EnumSetting<Easing>("Ease", Easing.CubicInOut, () -> this.page.getValue() == Page.Render));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 50), () -> this.page.getValue() == Page.Render));
    private final ColorSetting outlineColor = this.add(new ColorSetting("OutlineColor", new Color(255, 255, 255, 50), () -> this.page.getValue() == Page.Render));
    private final ColorSetting hitColor = this.add(new ColorSetting("HitColor", new Color(255, 255, 255, 150), () -> this.page.getValue() == Page.Render));
    private final ColorSetting hitOutlineColor = this.add(new ColorSetting("HitOutlineColor", new Color(255, 255, 255, 150), () -> this.page.getValue() == Page.Render));
    private final Animation animation = new Animation();
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.getValue() == Page.Rotate));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false, () -> this.rotate.isOpen() && this.page.getValue() == Page.Rotate).setParent());
    private final BooleanSetting whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == Page.Rotate));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, () -> this.page.getValue() == Page.Rotate && this.yawStep.isOpen()));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.page.getValue() == Page.Rotate && this.yawStep.isOpen()));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 20.0, 0.0, 360.0, 0.1, () -> this.checkFov.getValue() && this.page.getValue() == Page.Rotate && this.yawStep.isOpen()));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.page.getValue() == Page.Rotate && this.yawStep.isOpen()));
    private final Timer tick = new Timer();
    public class_243 directionVec = null;

    public Aura() {
        super("Aura", Module.Category.Combat);
        this.setChinese("\u6740\u622e\u5149\u73af");
        INSTANCE = this;
    }

    public static void doRender(class_4587 stack, float partialTicks, class_1297 entity, Color color, Color outlineColor, TargetESP mode) {
        switch (mode.ordinal()) {
            case 0: {
                Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009(0.0, 0.1, 0.0), color, outlineColor, false, true);
                break;
            }
            case 1: {
                Render3DUtil.draw3DBox(stack, ((IEntity)entity).getDimensions().method_30757(new class_243(MathUtil.interpolate(entity.field_6038, entity.method_23317(), (double)partialTicks), MathUtil.interpolate(entity.field_5971, entity.method_23318(), (double)partialTicks), MathUtil.interpolate(entity.field_5989, entity.method_23321(), (double)partialTicks))).method_1009(0.0, 0.1, 0.0), color, outlineColor, true, true);
                break;
            }
            case 2: {
                JelloUtil.drawJello(stack, entity, color);
                break;
            }
            case 3: {
                Render3DUtil.drawTargetEsp(stack, target, color);
            }
        }
    }

    public static float getAttackCooldownProgressPerTick() {
        return (float)(1.0 / Aura.mc.field_1724.method_45325(class_5134.field_23723) * 20.0);
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        if (target != null) {
            this.doRender(matrixStack, mc.method_60646().method_60637(true), target, this.mode.getValue());
        }
    }

    public void doRender(class_4587 stack, float partialTicks, class_1297 entity, TargetESP mode) {
        switch (mode.ordinal()) {
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
                Render3DUtil.drawTargetEsp(stack, target, this.color.getValue());
            }
        }
    }

    @Override
    public String getInfo() {
        return target == null ? null : target.method_5477().getString();
    }

    @EventListener
    public void onTick(ClientTickEvent event) {
        if (!(Aura.nullCheck() || this.timing.is(Timing.Pre) && event.isPost() || this.timing.is(Timing.Post) && event.isPre())) {
            if (this.weaponOnly.getValue() && !EntityUtil.isHoldingWeapon((class_1657)Aura.mc.field_1724)) {
                target = null;
            } else {
                target = this.getTarget(this.range.getValueFloat());
                if (target == null) {
                    target = this.getTarget(this.targetRange.getValueFloat());
                } else {
                    this.doAura();
                }
            }
        }
    }

    @EventListener
    public void onRotate(RotationEvent event) {
        if (target != null && this.rotate.getValue() && this.shouldYawStep()) {
            this.directionVec = this.getAttackVec(target);
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @EventListener
    public void onPacket(PacketEvent.Send event) {
        class_2596<?> packet;
        if (this.reset.getValue() && ((packet = event.getPacket()) instanceof class_2879 || packet instanceof class_2824 && Criticals.getInteractType((class_2824)packet) == class_2824.class_5907.field_29172)) {
            this.tick.reset();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean check() {
        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
            return false;
        }
        int at = (int)(this.tick.getMs() / 50L);
        if (this.cooldownMode.getValue() == Cooldown.Vanilla) {
            at = ((ILivingEntity)Aura.mc.field_1724).getLastAttackedTicks();
        }
        if (!((double)Math.max((float)(at = (int)((float)at * Alien.SERVER.getTPSFactor())) / Aura.getAttackCooldownProgressPerTick(), 0.0f) >= this.cooldown.getValue())) {
            return false;
        }
        class_1297 class_12972 = target;
        if (class_12972 instanceof class_1309) {
            class_1309 entity = (class_1309)class_12972;
            if ((double)entity.field_6235 > this.hurtTime.getValue()) {
                return false;
            }
        }
        if (this.whileEating.getValue()) return true;
        if (Aura.mc.field_1724.method_6115()) return false;
        return true;
    }

    private void doAura() {
        if (this.check()) {
            class_243 hitVec;
            if (this.rotate.getValue() && !this.faceVector(hitVec = this.getAttackVec(target))) {
                return;
            }
            this.animation.to = 1.0;
            this.animation.from = 1.0;
            mc.method_1562().method_52787((class_2596)class_2824.method_34206((class_1297)target, (boolean)Aura.mc.field_1724.method_5715()));
            Aura.mc.field_1724.method_7350();
            EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
            this.tick.reset();
            if (this.rotate.getValue() && !this.shouldYawStep()) {
                Alien.ROTATION.snapBack();
            }
        }
    }

    private class_243 getAttackVec(class_1297 entity) {
        return MathUtil.getClosestPointToBox(Aura.mc.field_1724.method_33571(), entity.method_5829());
    }

    private boolean shouldYawStep() {
        return this.whenElytra.getValue() || !Aura.mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() && !Velocity.INSTANCE.noRotation() : false;
    }

    public boolean faceVector(class_243 directionVec) {
        if (!this.shouldYawStep()) {
            Alien.ROTATION.lookAt(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        return Alien.ROTATION.inFov(directionVec, this.fov.getValueFloat()) || !this.checkFov.getValue();
    }

    public class_1297 getTarget(double range) {
        class_1297 target = null;
        double distance = range;
        double maxHealth = 36.0;
        for (class_1297 entity : Alien.THREAD.getEntities()) {
            if (!this.isEnemy(entity)) continue;
            class_243 hitVec = this.getAttackVec(entity);
            if (Aura.mc.field_1724.method_33571().method_1022(hitVec) > range || !Aura.mc.field_1724.method_6057(entity) && Aura.mc.field_1724.method_33571().method_1022(hitVec) > this.wallRange.getValue() || !CombatUtil.isValid(entity)) continue;
            if (target == null) {
                target = entity;
                distance = Aura.mc.field_1724.method_33571().method_1022(hitVec);
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (this.armorLow.getValue() && entity instanceof class_1657 && EntityUtil.isArmorLow((class_1657)entity, 10)) {
                target = entity;
                break;
            }
            if (this.targetMode.getValue() == TargetMode.HEALTH && (double)EntityUtil.getHealth(entity) < maxHealth) {
                target = entity;
                maxHealth = EntityUtil.getHealth(entity);
                continue;
            }
            if (this.targetMode.getValue() != TargetMode.DISTANCE || !(Aura.mc.field_1724.method_33571().method_1022(hitVec) < distance)) continue;
            target = entity;
            distance = Aura.mc.field_1724.method_33571().method_1022(hitVec);
        }
        return target;
    }

    private boolean isEnemy(class_1297 entity) {
        if (entity instanceof class_1621) {
            return this.Slimes.getValue();
        }
        if (entity instanceof class_1657) {
            return this.Players.getValue();
        }
        if (entity instanceof class_1646 || entity instanceof class_3989) {
            return this.Villagers.getValue();
        }
        if (entity instanceof class_1429) {
            return this.Animals.getValue();
        }
        return entity instanceof class_1308 ? this.Mobs.getValue() : false;
    }

    public static enum Page {
        General,
        Rotate,
        Target,
        Render;

    }

    public static enum Cooldown {
        Vanilla,
        Delay;

    }

    private static enum TargetMode {
        DISTANCE,
        HEALTH;

    }

    public static enum TargetESP {
        Fill,
        Box,
        Jello,
        ThunderHack,
        None;

    }

    public static enum Mode {
        Mace,
        Axe,
        Sword;

    }
}

