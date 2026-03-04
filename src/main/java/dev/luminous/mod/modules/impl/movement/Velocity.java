/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.projectile.FishingBobberEntity
 *  net.minecraft.world.World
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Direction$AxisDirection
 *  net.minecraft.util.math.Box
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket$Action
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.EntityVelocityUpdateEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;

public class Velocity
extends Module {
    public static Velocity INSTANCE;
    private final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Plain));
    private final SliderSetting lagPause = this.add(new SliderSetting("LagPause", 50, 0, 500, () -> this.mode.is(Mode.Grim) || this.mode.is(Mode.Wall)));
    public final BooleanSetting ignorePearlLag = this.add(new BooleanSetting("IgnorePearlLag", true, () -> this.mode.is(Mode.Grim) || this.mode.is(Mode.Wall)).setParent());
    private final SliderSetting phaseTime = this.add(new SliderSetting("PhaseTime", 250, 0, 1000, () -> (this.mode.is(Mode.Grim) || this.mode.is(Mode.Wall)) && this.ignorePearlLag.isOpen()));
    public final BooleanSetting noRotation = this.add(new BooleanSetting("NoRotation", false, () -> this.mode.is(Mode.Grim) || this.mode.is(Mode.Wall)));
    public final BooleanSetting flagInWall = this.add(new BooleanSetting("FlagInWall", false, () -> this.mode.is(Mode.Grim) || this.mode.is(Mode.Wall)).setParent());
    public final BooleanSetting whenPushOutOfBlocks = this.add(new BooleanSetting("WhilePushOut", false, () -> (this.mode.is(Mode.Grim) || this.mode.is(Mode.Wall)) && this.flagInWall.isOpen()));
    public final BooleanSetting staticSetting = this.add(new BooleanSetting("Static", false, () -> this.mode.is(Mode.Grim)));
    public final BooleanSetting cancelAll = this.add(new BooleanSetting("CancelAll", false, () -> !this.mode.is(Mode.None)));
    private final SliderSetting horizontal = this.add(new SliderSetting("Horizontal", 0.0, 0.0, 100.0, 1.0, () -> !this.mode.is(Mode.None) && !this.cancelAll.getValue()));
    private final SliderSetting vertical = this.add(new SliderSetting("Vertical", 0.0, 0.0, 100.0, 1.0, () -> !this.mode.is(Mode.None) && !this.cancelAll.getValue()));
    public final BooleanSetting whileLiquid = this.add(new BooleanSetting("WhileLiquid", false));
    public final BooleanSetting whileElytra = this.add(new BooleanSetting("FallFlying", false));
    public final BooleanSetting noClimb = this.add(new BooleanSetting("NoClimb", false));
    public final BooleanSetting waterPush = this.add(new BooleanSetting("NoWaterPush", false));
    public final BooleanSetting entityPush = this.add(new BooleanSetting("NoEntityPush", true));
    public final BooleanSetting blockPush = this.add(new BooleanSetting("NoBlockPush", true));
    public final BooleanSetting fishBob = this.add(new BooleanSetting("NoFishBob", true));
    public final Timer pearlTimer = new Timer();
    private final Timer lagBackTimer = new Timer();
    private boolean flag;
    static boolean pushOutOfBlocks;

    public Velocity() {
        super("Velocity", Module.Category.Movement);
        this.setChinese("\u53cd\u51fb\u9000");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.is(Mode.None) ? null : this.mode.getValue().name() + ", " + (String)(this.cancelAll.getValue() ? "Cancel" : this.horizontal.getValueInt() + "%, " + this.vertical.getValueInt() + "%");
    }

    @EventListener
    public void onRotate(UpdateRotateEvent event) {
        if (this.noRotation()) {
            event.setRotation(Alien.ROTATION.rotationYaw, 89.0f);
        }
    }

    public boolean noRotation() {
        return this.isOn() && (this.mode.is(Mode.Grim) || this.mode.is(Mode.Wall)) && EntityUtil.isInsideBlock() && this.noRotation.getValue();
    }

    @EventListener
    public void onVelocity(EntityVelocityUpdateEvent event) {
        if (!(Velocity.nullCheck() || event.getEntity() != Velocity.mc.field_1724 || this.mode.is(Mode.None) || Velocity.mc.field_1724.method_52535() && !this.whileLiquid.getValue() || Velocity.mc.field_1724.method_6128() && !this.whileElytra.getValue())) {
            switch (this.mode.getValue().ordinal()) {
                default: {
                    break;
                }
                case 1: {
                    if (!this.lagBackTimer.passedMs(this.lagPause.getValue())) {
                        return;
                    }
                    if (!(EntityUtil.isInsideBlock() || this.getPos() != null || this.staticSetting.getValue() && MovementUtil.isStatic())) {
                        return;
                    }
                    if (event.getX() == 0.0 && event.getZ() == 0.0) break;
                    this.flag = true;
                    break;
                }
                case 2: {
                    if (!this.lagBackTimer.passedMs(this.lagPause.getValue())) {
                        return;
                    }
                    if (!EntityUtil.isInsideBlock()) {
                        return;
                    }
                    if (event.getX() == 0.0 && event.getZ() == 0.0) break;
                    this.flag = true;
                }
            }
            if (this.cancelAll.getValue()) {
                event.cancel();
            } else {
                double h = (double)this.horizontal.getValueInt() / 100.0;
                double v = (double)this.vertical.getValueInt() / 100.0;
                event.setX(event.getX() * h);
                event.setZ(event.getZ() * h);
                event.setY(event.getY() * v);
            }
        }
    }

    @EventListener
    public void onReceivePacket(PacketEvent.Receive event) {
        class_1536 fishHook;
        class_2663 packet;
        class_1297 class_12972;
        if (event.getPacket() instanceof class_2708 && (!this.ignorePearlLag.getValue() || this.pearlTimer.passed(this.phaseTime.getValueInt()))) {
            this.lagBackTimer.reset();
        }
        if (!Velocity.nullCheck() && (!Velocity.mc.field_1724.method_52535() || this.whileLiquid.getValue()) && this.fishBob.getValue() && (class_12972 = event.getPacket()) instanceof class_2663 && (packet = (class_2663)class_12972).method_11470() == 31 && (class_12972 = packet.method_11469((class_1937)Velocity.mc.field_1687)) instanceof class_1536 && (fishHook = (class_1536)class_12972).method_26957() == Velocity.mc.field_1724) {
            event.setCancelled(true);
        }
    }

    @EventListener
    public void onUpdate(TickEvent event) {
        if (!(Velocity.nullCheck() || event.isPost() || Velocity.mc.field_1724.method_52535() && !this.whileLiquid.getValue())) {
            if (this.flagInWall.getValue()) {
                pushOutOfBlocks = false;
                Velocity.pushOutOfBlocks(Velocity.mc.field_1724.method_23317() - (double)Velocity.mc.field_1724.method_17681() * 0.35, Velocity.mc.field_1724.method_23321() + (double)Velocity.mc.field_1724.method_17681() * 0.35);
                Velocity.pushOutOfBlocks(Velocity.mc.field_1724.method_23317() - (double)Velocity.mc.field_1724.method_17681() * 0.35, Velocity.mc.field_1724.method_23321() - (double)Velocity.mc.field_1724.method_17681() * 0.35);
                Velocity.pushOutOfBlocks(Velocity.mc.field_1724.method_23317() + (double)Velocity.mc.field_1724.method_17681() * 0.35, Velocity.mc.field_1724.method_23321() - (double)Velocity.mc.field_1724.method_17681() * 0.35);
                Velocity.pushOutOfBlocks(Velocity.mc.field_1724.method_23317() + (double)Velocity.mc.field_1724.method_17681() * 0.35, Velocity.mc.field_1724.method_23321() + (double)Velocity.mc.field_1724.method_17681() * 0.35);
            }
            if (!(Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue() || !this.flag)) {
                if (this.lagBackTimer.passedMs(this.lagPause.getValue()) && (this.flagInWall.getValue() && (!pushOutOfBlocks || this.whenPushOutOfBlocks.getValue()) || !EntityUtil.isInsideBlock())) {
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2830(Velocity.mc.field_1724.method_23317(), Velocity.mc.field_1724.method_23318(), Velocity.mc.field_1724.method_23321(), Alien.ROTATION.rotationYaw, Alien.ROTATION.rotationPitch, Velocity.mc.field_1724.method_24828()));
                    class_2338 pos = this.getPos();
                    if (pos != null) {
                        mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12973, pos, Velocity.mc.field_1724.method_5735().method_10153()));
                    }
                }
                this.flag = false;
            }
        }
    }

    public class_2338 getPos() {
        return Velocity.mc.field_1687.method_8320(Velocity.mc.field_1724.method_24515().method_10074()).method_26204() == class_2246.field_10540 ? Velocity.mc.field_1724.method_24515().method_10074() : null;
    }

    private static void pushOutOfBlocks(double x, double z) {
        class_2338 blockPos = class_2338.method_49637((double)x, (double)Velocity.mc.field_1724.method_23318(), (double)z);
        if (Velocity.wouldCollideAt(blockPos)) {
            class_2350[] directions;
            double d = x - (double)blockPos.method_10263();
            double e = z - (double)blockPos.method_10260();
            class_2350 direction = null;
            double f = Double.MAX_VALUE;
            for (class_2350 direction2 : directions = new class_2350[]{class_2350.field_11039, class_2350.field_11034, class_2350.field_11043, class_2350.field_11035}) {
                double h;
                double g = direction2.method_10166().method_10172(d, 0.0, e);
                double d2 = h = direction2.method_10171() == class_2350.class_2352.field_11056 ? 1.0 - g : g;
                if (!(h < f) || Velocity.wouldCollideAt(blockPos.method_10093(direction2))) continue;
                f = h;
                direction = direction2;
            }
            if (direction != null) {
                pushOutOfBlocks = true;
            }
        }
    }

    private static boolean wouldCollideAt(class_2338 pos) {
        class_238 box = Velocity.mc.field_1724.method_5829();
        class_238 box2 = new class_238((double)pos.method_10263(), box.field_1322, (double)pos.method_10260(), (double)pos.method_10263() + 1.0, box.field_1325, (double)pos.method_10260() + 1.0).method_1011(1.0E-7);
        return Velocity.mc.field_1724.method_37908().method_39454((class_1297)Velocity.mc.field_1724, box2);
    }

    static {
        pushOutOfBlocks = false;
    }

    public static enum Mode {
        Plain,
        Grim,
        Wall,
        None;

    }
}

