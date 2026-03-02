/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ConcurrentSet
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2708
 *  net.minecraft.class_2793
 *  net.minecraft.class_2828
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_2828$class_2831
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 *  net.minecraft.class_434
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import io.netty.util.internal.ConcurrentSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2793;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_434;

public class PacketFly
extends Module {
    public static PacketFly INSTANCE;
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Factor));
    public final SliderSetting factor = this.add(new SliderSetting("Factor", 1.0, 0.0, 10.0));
    public final EnumSetting<Phase> phase = this.add(new EnumSetting<Phase>("Phase", Phase.Full));
    public final EnumSetting<Type> type = this.add(new EnumSetting<Type>("Type", Type.Up));
    public final BooleanSetting antiKick = this.add(new BooleanSetting("AntiKick", true));
    public final BooleanSetting noRotation = this.add(new BooleanSetting("NoRotation", false));
    public final BooleanSetting noMovePacket = this.add(new BooleanSetting("NoMovePacket", false));
    public final BooleanSetting bbOffset = this.add(new BooleanSetting("BB-Offset", false));
    public final SliderSetting invalidY = this.add(new SliderSetting("Invalid-Offset", 1337, 0, 1337));
    public final SliderSetting invalids = this.add(new SliderSetting("Invalids", 1, 0, 10));
    public final SliderSetting sendTeleport = this.add(new SliderSetting("Teleport", 1, 0, 10));
    public final SliderSetting concealY = this.add(new SliderSetting("C-Y", 0.0, -256.0, 256.0));
    public final SliderSetting conceal = this.add(new SliderSetting("C-Multiplier", 1.0, 0.0, 2.0));
    public final SliderSetting ySpeed = this.add(new SliderSetting("Y-Multiplier", 1.0, 0.0, 2.0));
    public final SliderSetting xzSpeed = this.add(new SliderSetting("X/Z-Multiplier", 1.0, 0.0, 2.0));
    public final BooleanSetting elytra = this.add(new BooleanSetting("Elytra", false));
    public final BooleanSetting xzJitter = this.add(new BooleanSetting("Jitter-XZ", false));
    public final BooleanSetting yJitter = this.add(new BooleanSetting("Jitter-Y", false));
    public final BooleanSetting zeroSpeed = this.add(new BooleanSetting("Zero-Speed", false));
    public final BooleanSetting zeroY = this.add(new BooleanSetting("Zero-Y", false));
    public final BooleanSetting zeroTeleport = this.add(new BooleanSetting("Zero-Teleport", true));
    public final SliderSetting zoomer = this.add(new SliderSetting("Zoomies", 3, 0, 10));
    public final Map<Integer, TimeVec> posLooks = new ConcurrentHashMap<Integer, TimeVec>();
    public final Set<class_2596<?>> playerPackets = new ConcurrentSet();
    public final AtomicInteger teleportID = new AtomicInteger();
    public class_243 vecDelServer;
    public int packetCounter;
    public boolean zoomies;
    public float lastFactor;
    public int zoomTimer = 0;

    public PacketFly() {
        super("PacketFly", Module.Category.Movement);
        this.setChinese("\u53d1\u5305\u98de\u884c");
        INSTANCE = this;
    }

    @Override
    public void onLogin() {
        this.disable();
        this.clearValues();
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.posLooks.entrySet().removeIf(entry -> System.currentTimeMillis() - ((TimeVec)((Object)((Object)entry.getValue()))).getTime() > TimeUnit.SECONDS.toMillis(30L));
    }

    @EventListener
    public void invoke(TickEvent event) {
        if (event.isPre() && this.mode.getValue() != Mode.Compatibility) {
            double ySpeed;
            MovementUtil.setMotionX(0.0);
            MovementUtil.setMotionY(0.0);
            MovementUtil.setMotionZ(0.0);
            if (this.mode.getValue() != Mode.Setback && this.teleportID.get() == 0) {
                if (this.checkPackets(6)) {
                    this.sendPackets(0.0, 0.0, 0.0, true);
                }
                return;
            }
            boolean isPhasing = this.isPlayerCollisionBoundingBoxEmpty();
            if (!PacketFly.mc.field_1724.field_3913.field_3904 || !isPhasing && MovementUtil.isMoving()) {
                ySpeed = !PacketFly.mc.field_1724.field_3913.field_3903 ? (!isPhasing ? (this.checkPackets(4) ? (this.antiKick.getValue() ? -0.04 : 0.0) : 0.0) : 0.0) : (this.yJitter.getValue() && this.zoomies ? -0.061 : -0.062);
            } else if (this.antiKick.getValue() && !isPhasing) {
                ySpeed = this.checkPackets(this.mode.getValue() == Mode.Setback ? 10 : 20) ? -0.032 : 0.062;
            } else {
                double d = ySpeed = this.yJitter.getValue() && this.zoomies ? 0.061 : 0.062;
            }
            if (this.phase.getValue() == Phase.Full && isPhasing && MovementUtil.isMoving() && ySpeed != 0.0) {
                ySpeed /= 2.5;
            }
            double high = this.xzJitter.getValue() && this.zoomies ? 0.25 : 0.26;
            double low = this.xzJitter.getValue() && this.zoomies ? 0.03 : 0.031;
            double[] dirSpeed = MovementUtil.directionSpeed(this.phase.getValue() == Phase.Full && isPhasing ? low : high);
            if (this.mode.getValue() == Mode.Increment) {
                if ((double)this.lastFactor >= this.factor.getValue()) {
                    this.lastFactor = 1.0f;
                } else {
                    float f;
                    this.lastFactor += 1.0f;
                    if ((double)f > this.factor.getValue()) {
                        this.lastFactor = this.factor.getValueFloat();
                    }
                }
            } else {
                this.lastFactor = this.factor.getValueFloat();
            }
            int i = 1;
            while (true) {
                float f = i;
                float f2 = this.mode.getValue() != Mode.Factor && this.mode.getValue() != Mode.Slow && this.mode.getValue() != Mode.Increment ? 1.0f : this.lastFactor;
                if (!(f <= f2)) break;
                double conceal = PacketFly.mc.field_1724.method_23318() < this.concealY.getValue() && MovementUtil.isMoving() ? this.conceal.getValue() : 1.0;
                MovementUtil.setMotionX(dirSpeed[0] * (double)i * conceal * this.xzSpeed.getValue());
                MovementUtil.setMotionY(ySpeed * (double)i * this.ySpeed.getValue());
                MovementUtil.setMotionZ(dirSpeed[1] * (double)i * conceal * this.xzSpeed.getValue());
                this.sendPackets(MovementUtil.getMotionX(), MovementUtil.getMotionY(), MovementUtil.getMotionZ(), this.mode.getValue() != Mode.Setback);
                ++i;
            }
            ++this.zoomTimer;
            if ((double)this.zoomTimer > this.zoomer.getValue()) {
                this.zoomies = !this.zoomies;
                this.zoomTimer = 0;
            }
        }
    }

    @EventListener
    public void invoke(PacketEvent.Receive event) {
        class_2596<?> class_25962;
        if (!PacketFly.nullCheck() && this.mode.getValue() != Mode.Compatibility && (class_25962 = event.getPacket()) instanceof class_2708) {
            TimeVec vec;
            class_2708 packet = (class_2708)class_25962;
            if (PacketFly.mc.field_1724.method_5805() && this.mode.getValue() != Mode.Setback && this.mode.getValue() != Mode.Slow && !(PacketFly.mc.field_1755 instanceof class_434) && (vec = this.posLooks.remove(packet.method_11737())) != null && vec.field_1352 == packet.method_11734() && vec.field_1351 == packet.method_11735() && vec.field_1350 == packet.method_11738()) {
                event.setCancelled(true);
                return;
            }
            this.teleportID.set(packet.method_11737());
        }
    }

    @EventListener
    public void invoke(MoveEvent event) {
        if (this.phase.getValue() == Phase.Semi || this.isPlayerCollisionBoundingBoxEmpty()) {
            PacketFly.mc.field_1724.field_5960 = true;
        }
        if (this.mode.getValue() != Mode.Compatibility && (this.mode.getValue() == Mode.Setback || this.teleportID.get() != 0)) {
            if (this.zeroSpeed.getValue()) {
                event.setX(0.0);
                event.setY(0.0);
                event.setZ(0.0);
            } else {
                event.setX(MovementUtil.getMotionX());
                event.setY(MovementUtil.getMotionY());
                event.setZ(MovementUtil.getMotionZ());
            }
            if (this.zeroY.getValue()) {
                event.setY(0.0);
            }
        }
    }

    @EventListener
    public void onPacket(PacketEvent.Send event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2828) {
            class_2828 packet = (class_2828)class_25962;
            if (this.mode.getValue() != Mode.Compatibility && !this.playerPackets.remove(event.getPacket())) {
                if (packet instanceof class_2828.class_2831 && !this.noRotation.getValue()) {
                    return;
                }
                if (!this.noMovePacket.getValue()) {
                    return;
                }
                event.setCancelled(true);
            }
        }
    }

    @Override
    public boolean onEnable() {
        this.clearValues();
        if (PacketFly.mc.field_1724 == null) {
            this.disable();
        }
        return false;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().toString();
    }

    public void clearValues() {
        this.lastFactor = 1.0f;
        this.packetCounter = 0;
        this.teleportID.set(0);
        this.playerPackets.clear();
        this.posLooks.clear();
        this.vecDelServer = null;
    }

    public boolean isPlayerCollisionBoundingBoxEmpty() {
        double o = this.bbOffset.getValue() ? -0.0625 : 0.0;
        return BlockUtil.canCollide((class_1297)PacketFly.mc.field_1724, PacketFly.mc.field_1724.method_5829().method_1009(o, o, o));
    }

    public boolean checkPackets(int amount) {
        if (++this.packetCounter >= amount) {
            this.packetCounter = 0;
            return true;
        }
        return false;
    }

    public void sendPackets(double x, double y, double z, boolean confirm) {
        int i;
        class_243 vec;
        class_243 offset = new class_243(x, y, z);
        this.vecDelServer = vec = PacketFly.mc.field_1724.method_19538().method_1019(offset);
        class_243 oOB = this.type.getValue().createOutOfBounds(vec, this.invalidY.getValueInt());
        this.sendCPacket((class_2596<?>)new class_2828.class_2829(vec.field_1352, vec.field_1351, vec.field_1350, PacketFly.mc.field_1724.method_24828()));
        if (!mc.method_1542()) {
            i = 0;
            while ((double)i < this.invalids.getValue()) {
                this.sendCPacket((class_2596<?>)new class_2828.class_2829(oOB.field_1352, oOB.field_1351, oOB.field_1350, PacketFly.mc.field_1724.method_24828()));
                oOB = this.type.getValue().createOutOfBounds(oOB, this.invalidY.getValueInt());
                ++i;
            }
        }
        if (confirm && (this.zeroTeleport.getValue() || this.teleportID.get() != 0)) {
            i = 0;
            while ((double)i < this.sendTeleport.getValue()) {
                this.sendConfirmTeleport(vec);
                ++i;
            }
        }
        if (this.elytra.getValue()) {
            mc.method_1562().method_52787((class_2596)new class_2848((class_1297)PacketFly.mc.field_1724, class_2848.class_2849.field_12982));
        }
    }

    public void sendConfirmTeleport(class_243 vec) {
        int id = this.teleportID.incrementAndGet();
        mc.method_1562().method_52787((class_2596)new class_2793(id));
        this.posLooks.put(id, new TimeVec(vec));
    }

    public void sendCPacket(class_2596<?> packet) {
        this.playerPackets.add(packet);
        mc.method_1562().method_52787(packet);
    }

    public static enum Mode {
        Setback,
        Fast,
        Factor,
        Slow,
        Increment,
        Compatibility;

    }

    public static enum Phase {
        Off,
        Semi,
        Full;

    }

    public static enum Type {
        Down{

            @Override
            public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
                return vec3d.method_1031(0.0, (double)(-invalid), 0.0);
            }
        }
        ,
        Up{

            @Override
            public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
                return vec3d.method_1031(0.0, (double)invalid, 0.0);
            }
        }
        ,
        Preserve{
            final Random random = new Random();

            private int randomInt() {
                int result = this.random.nextInt(29000000);
                return this.random.nextBoolean() ? result : -result;
            }

            @Override
            public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
                return vec3d.method_1031((double)this.randomInt(), 0.0, (double)this.randomInt());
            }
        }
        ,
        Switch{
            final Random random = new Random();

            @Override
            public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
                boolean down = this.random.nextBoolean();
                return down ? vec3d.method_1031(0.0, (double)(-invalid), 0.0) : vec3d.method_1031(0.0, (double)invalid, 0.0);
            }
        }
        ,
        X{

            @Override
            public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
                return vec3d.method_1031((double)invalid, 0.0, 0.0);
            }
        }
        ,
        Z{

            @Override
            public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
                return vec3d.method_1031(0.0, 0.0, (double)invalid);
            }
        }
        ,
        XZ{

            @Override
            public class_243 createOutOfBounds(class_243 vec3d, int invalid) {
                return vec3d.method_1031((double)invalid, 0.0, (double)invalid);
            }
        };


        public abstract class_243 createOutOfBounds(class_243 var1, int var2);
    }

    public static class TimeVec
    extends class_243 {
        final long time;

        public TimeVec(class_243 vec3d) {
            this(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, System.currentTimeMillis());
        }

        public TimeVec(double xIn, double yIn, double zIn, long time) {
            super(xIn, yIn, zIn);
            this.time = time;
        }

        public long getTime() {
            return this.time;
        }
    }
}

