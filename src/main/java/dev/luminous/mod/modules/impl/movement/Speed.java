/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.decoration.ArmorStandEntity
 *  net.minecraft.entity.vehicle.BoatEntity
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.s2c.play.ExplosionS2CPacket
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.events.impl.MovedEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.path.BaritoneUtil;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.Sprint;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;

public class Speed
extends Module {
    public static Speed INSTANCE;
    private final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Strafe));
    public final SliderSetting collideSpeed = this.add(new SliderSetting("CollideSpeed", 0.08, 0.0, 0.08, 0.01, () -> this.mode.is(Mode.Grim)));
    private final BooleanSetting strict = this.add(new BooleanSetting("Strict", true, () -> this.mode.is(Mode.Grim)));
    private final BooleanSetting boat = this.add(new BooleanSetting("BoatLongJump", true, () -> this.mode.is(Mode.Grim)));
    public final SliderSetting boatExpand = this.add(new SliderSetting("BoatExpand", 0.2, 0.0, 1.0, 0.01, () -> this.mode.is(Mode.Grim)));
    public final SliderSetting boatSpeed = this.add(new SliderSetting("BoatSpeed", 0.2, -2.0, 2.0, 0.01, () -> this.mode.is(Mode.Grim)));
    public final SliderSetting boatJump = this.add(new SliderSetting("BoatJump", 0.2, 0.0, 2.0, 0.01, () -> this.mode.is(Mode.Grim)));
    private final BooleanSetting inWater = this.add(new BooleanSetting("InWater", false, () -> !this.mode.is(Mode.Grim)));
    private final BooleanSetting inBlock = this.add(new BooleanSetting("InBlock", false, () -> !this.mode.is(Mode.Grim)));
    private final BooleanSetting airStop = this.add(new BooleanSetting("AirStop", false, () -> !this.mode.is(Mode.Grim)));
    private final SliderSetting lagTime = this.add(new SliderSetting("LagTime", 500.0, 0.0, 1000.0, 1.0, () -> !this.mode.is(Mode.Grim)));
    private final BooleanSetting jump = this.add(new BooleanSetting("Jump", true, () -> this.mode.is(Mode.Strafe)));
    private final SliderSetting strafeSpeed = this.add(new SliderSetting("Speed", 0.2873, 0.0, 1.0, 1.0E-4, () -> this.mode.is(Mode.Strafe)));
    private final BooleanSetting explosions = this.add(new BooleanSetting("ExplosionsBoost", false, () -> this.mode.is(Mode.Strafe)));
    private final BooleanSetting velocity = this.add(new BooleanSetting("VelocityBoost", true, () -> this.mode.is(Mode.Strafe)));
    private final SliderSetting multiplier = this.add(new SliderSetting("H-Factor", 1.0, 0.0, 5.0, 0.01, () -> this.mode.is(Mode.Strafe)));
    private final SliderSetting vertical = this.add(new SliderSetting("V-Factor", 1.0, 0.0, 5.0, 0.01, () -> this.mode.is(Mode.Strafe)));
    private final SliderSetting coolDown = this.add(new SliderSetting("CoolDown", 1000.0, 0.0, 5000.0, 1.0, () -> this.mode.is(Mode.Strafe)));
    private final BooleanSetting slow = this.add(new BooleanSetting("Slowness", false, () -> this.mode.is(Mode.Strafe)));
    private final Timer expTimer = new Timer();
    private final Timer lagTimer = new Timer();
    private boolean stop;
    private double speed;
    private double distance;
    private int strictTicks;
    private int strafe = 4;
    private int stage;
    private double lastExp;
    private boolean boost;

    public Speed() {
        super("Speed", Module.Category.Movement);
        this.setChinese("\u52a0\u901f");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @Override
    public boolean onEnable() {
        if (Speed.mc.field_1724 != null) {
            this.speed = MovementUtil.getSpeed(false);
            this.distance = MovementUtil.getDistance2D();
        }
        this.stage = 4;
        return false;
    }

    @EventListener(priority=100)
    public void invoke(PacketEvent.Receive event) {
        if (!BaritoneUtil.isActive()) {
            if (this.mode.is(Mode.Strafe)) {
                class_2596<?> class_25962 = event.getPacket();
                if (class_25962 instanceof class_2743) {
                    class_2743 packet = (class_2743)class_25962;
                    if (Speed.mc.field_1724 != null && packet.method_11818() == Speed.mc.field_1724.method_5628() && this.velocity.getValue()) {
                        double speed = Math.sqrt(packet.method_11815() * packet.method_11815() + packet.method_11819() * packet.method_11819());
                        double d = this.lastExp = this.expTimer.passed(this.coolDown.getValueInt()) ? speed : speed - this.lastExp;
                        if (this.lastExp > 0.0) {
                            this.expTimer.reset();
                            this.speed += this.lastExp * this.multiplier.getValue();
                            this.distance += this.lastExp * this.multiplier.getValue();
                            if (MovementUtil.getMotionY() > 0.0 && this.vertical.getValue() != 0.0) {
                                MovementUtil.setMotionY(MovementUtil.getMotionY() * this.vertical.getValue());
                            }
                        }
                    }
                } else {
                    class_2596<?> speed = event.getPacket();
                    if (speed instanceof class_2664) {
                        class_2664 packetx = (class_2664)speed;
                        if (this.explosions.getValue()) {
                            class_243 class_2432 = new class_243(packetx.method_11475(), packetx.method_11477(), packetx.method_11478());
                            if (Speed.mc.field_1724.method_19538().method_1022(class_2432) < 15.0) {
                                double speed2 = Math.sqrt(packetx.method_11472() * packetx.method_11472() + packetx.method_11474() * packetx.method_11474());
                                double d = this.lastExp = this.expTimer.passed(this.coolDown.getValueInt()) ? speed2 : speed2 - this.lastExp;
                                if (this.lastExp > 0.0) {
                                    this.expTimer.reset();
                                    this.speed += this.lastExp * this.multiplier.getValue();
                                    this.distance += this.lastExp * this.multiplier.getValue();
                                    if (MovementUtil.getMotionY() > 0.0) {
                                        MovementUtil.setMotionY(MovementUtil.getMotionY() * this.vertical.getValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (event.getPacket() instanceof class_2708) {
                this.lagTimer.reset();
                this.resetStrafe();
            }
        }
    }

    @EventListener
    public void onMove(MovedEvent event) {
        if (!Speed.nullCheck()) {
            double dx = Speed.mc.field_1724.method_23317() - Speed.mc.field_1724.field_6014;
            double dz = Speed.mc.field_1724.method_23321() - Speed.mc.field_1724.field_5969;
            this.distance = Math.sqrt(dx * dx + dz * dz);
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.mode.is(Mode.Grim)) {
            if (!MovementUtil.isMoving()) {
                return;
            }
            int collisions = 0;
            class_238 box = this.strict.getValue() ? Speed.mc.field_1724.method_5829() : Speed.mc.field_1724.method_5829().method_1014(1.0);
            for (class_1297 entity : Alien.THREAD.getEntities()) {
                class_238 entityBox = entity.method_5829();
                if (this.boat.getValue() && Speed.mc.field_1724.method_24828() && entity instanceof class_1690 && box.method_994(entityBox.method_1014(this.boatExpand.getValue()))) {
                    double yaw = Math.toRadians(Sprint.getSprintYaw(Speed.mc.field_1724.method_36454()));
                    double boost = this.boatSpeed.getValue();
                    Speed.mc.field_1724.method_18800(-Math.sin(yaw) * boost, this.boatJump.getValue(), Math.cos(yaw) * boost);
                    return;
                }
                if (!box.method_994(entityBox) || !this.canCauseSpeed(entity)) continue;
                ++collisions;
            }
            double yaw = Math.toRadians(Sprint.getSprintYaw(Speed.mc.field_1724.method_36454()));
            double boost = this.collideSpeed.getValue() * (double)collisions;
            Speed.mc.field_1724.method_5762(-Math.sin(yaw) * boost, 0.0, Math.cos(yaw) * boost);
        }
    }

    private boolean canCauseSpeed(class_1297 entity) {
        return entity != Speed.mc.field_1724 && entity instanceof class_1309 && !(entity instanceof class_1531);
    }

    @EventListener
    public void invoke(MoveEvent event) {
        if (!MovementUtil.isMoving() && this.airStop.getValue() && !this.mode.is(Mode.Grim)) {
            MovementUtil.setMotionX(0.0);
            MovementUtil.setMotionZ(0.0);
        }
        if (!(!this.inWater.getValue() && (Speed.mc.field_1724.method_5869() || Speed.mc.field_1724.method_5799() || Speed.mc.field_1724.method_5771()) || Speed.mc.field_1724.method_3144() || Speed.mc.field_1724.method_21754() || !this.inBlock.getValue() && EntityUtil.isInsideBlock() || Speed.mc.field_1724.method_31549().field_7479 || Speed.mc.field_1724.method_6128() || !MovementUtil.isMoving())) {
            if (!this.mode.is(Mode.Strafe)) {
                double amplifier;
                double speedEffect = 1.0;
                double slowEffect = 1.0;
                if (Speed.mc.field_1724.method_6059(class_1294.field_5904)) {
                    amplifier = Speed.mc.field_1724.method_6112(class_1294.field_5904).method_5578();
                    speedEffect = 1.0 + 0.2 * (amplifier + 1.0);
                }
                if (Speed.mc.field_1724.method_6059(class_1294.field_5909)) {
                    amplifier = Speed.mc.field_1724.method_6112(class_1294.field_5909).method_5578();
                    slowEffect = 1.0 + 0.2 * (amplifier + 1.0);
                }
                double base = (double)0.2873f * speedEffect / slowEffect;
                float jumpEffect = 0.0f;
                if (Speed.mc.field_1724.method_6059(class_1294.field_5913)) {
                    jumpEffect += (float)(Speed.mc.field_1724.method_6112(class_1294.field_5913).method_5578() + 1) * 0.1f;
                }
                if (this.mode.getValue() == Mode.StrafeStrict) {
                    if (!this.lagTimer.passed(this.lagTime.getValueInt())) {
                        return;
                    }
                    if (this.strafe == 1) {
                        this.speed = (double)1.35f * base - (double)0.01f;
                    } else if (this.strafe == 2) {
                        if (Speed.mc.field_1724.field_3913.field_3904 || !Speed.mc.field_1724.method_24828()) {
                            return;
                        }
                        float jump = 0.39999995f + jumpEffect;
                        event.setY(jump);
                        MovementUtil.setMotionY(jump);
                        this.speed *= 2.149;
                    } else if (this.strafe == 3) {
                        double moveSpeed = 0.66 * (this.distance - base);
                        this.speed = this.distance - moveSpeed;
                    } else {
                        if ((!Speed.mc.field_1687.method_8587((class_1297)Speed.mc.field_1724, Speed.mc.field_1724.method_5829().method_989(0.0, Speed.mc.field_1724.method_18798().method_10214(), 0.0)) || Speed.mc.field_1724.field_5992) && this.strafe > 0) {
                            this.strafe = 1;
                        }
                        this.speed = this.distance - this.distance / 159.0;
                    }
                    ++this.strictTicks;
                    this.speed = Math.max(this.speed, base);
                    double baseMax = 0.465 * speedEffect / slowEffect;
                    double baseMin = 0.44 * speedEffect / slowEffect;
                    this.speed = Math.min(this.speed, this.strictTicks > 25 ? baseMax : baseMin);
                    if (this.strictTicks > 50) {
                        this.strictTicks = 0;
                    }
                    class_241 motion = this.handleStrafeMotion((float)this.speed);
                    event.setX(motion.field_1343);
                    event.setZ(motion.field_1342);
                    ++this.strafe;
                }
            } else if (this.stop) {
                this.stop = false;
            } else if (this.lagTimer.passed(this.lagTime.getValueInt())) {
                if (this.stage == 1) {
                    this.speed = 1.35 * MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue()) - 0.01;
                } else if (this.stage != 2 || !Speed.mc.field_1724.method_24828() || !Speed.mc.field_1690.field_1903.method_1434() && !this.jump.getValue()) {
                    if (this.stage == 3) {
                        this.speed = this.distance - 0.66 * (this.distance - MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue()));
                        this.boost = !this.boost;
                    } else {
                        if ((BlockUtil.canCollide(null, Speed.mc.field_1724.method_5829().method_989(0.0, MovementUtil.getMotionY(), 0.0)) || Speed.mc.field_1724.field_34927) && this.stage > 0) {
                            this.stage = 1;
                        }
                        this.speed = this.distance - this.distance / 159.0;
                    }
                } else {
                    double yMotion = 0.3999 + MovementUtil.getJumpSpeed();
                    MovementUtil.setMotionY(yMotion);
                    event.setY(yMotion);
                    this.speed *= this.boost ? 1.6835 : 1.395;
                }
                this.speed = Math.min(this.speed, 10.0);
                this.speed = Math.max(this.speed, MovementUtil.getSpeed(this.slow.getValue(), this.strafeSpeed.getValue()));
                double n = Speed.mc.field_1724.field_3913.field_3905;
                double n2 = Speed.mc.field_1724.field_3913.field_3907;
                double n3 = Speed.mc.field_1724.method_36454();
                if (n == 0.0 && n2 == 0.0) {
                    event.setX(0.0);
                    event.setZ(0.0);
                } else if (n != 0.0 && n2 != 0.0) {
                    n *= Math.sin(0.7853981633974483);
                    n2 *= Math.cos(0.7853981633974483);
                }
                event.setX((n * this.speed * -Math.sin(Math.toRadians(n3)) + n2 * this.speed * Math.cos(Math.toRadians(n3))) * 0.99);
                event.setZ((n * this.speed * Math.cos(Math.toRadians(n3)) - n2 * this.speed * -Math.sin(Math.toRadians(n3))) * 0.99);
                ++this.stage;
            }
        } else {
            this.resetStrafe();
            this.stop = true;
        }
    }

    public class_241 handleStrafeMotion(float speed) {
        float forward = Speed.mc.field_1724.field_3913.field_3905;
        float strafe = Speed.mc.field_1724.field_3913.field_3907;
        float yaw = Speed.mc.field_1724.field_5982 + (Speed.mc.field_1724.method_36454() - Speed.mc.field_1724.field_5982) * mc.method_60646().method_60637(true);
        if (forward == 0.0f && strafe == 0.0f) {
            return class_241.field_1340;
        }
        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += forward > 0.0f ? -45.0f : 45.0f;
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += forward > 0.0f ? 45.0f : -45.0f;
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        float rx = (float)Math.cos(Math.toRadians(yaw));
        float rz = (float)(-Math.sin(Math.toRadians(yaw)));
        return new class_241(forward * speed * rz + strafe * speed * rx, forward * speed * rx - strafe * speed * rz);
    }

    public void resetStrafe() {
        this.strafe = 4;
        this.strictTicks = 0;
        this.speed = 0.0;
        this.distance = 0.0;
    }

    public static enum Mode {
        Strafe,
        StrafeStrict,
        Grim;

    }
}

