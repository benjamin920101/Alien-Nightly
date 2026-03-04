/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$PositionAndOnGround
 *  net.minecraft.entity.attribute.EntityAttributes
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.path.BaritoneUtil;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.SelfTrap;
import dev.luminous.mod.modules.impl.combat.Surround;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.entity.attribute.EntityAttributes;

public class Step
extends Module {
    private final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Vanilla));
    private final SliderSetting height = this.add(new SliderSetting("Height", 1.0, 0.0, 5.0, 0.5));
    private final BooleanSetting useTimer = this.add(new BooleanSetting("Timer", true, () -> this.mode.getValue() == Mode.OldNCP || this.mode.getValue() == Mode.NCP));
    private final BooleanSetting fast = this.add(new BooleanSetting("Fast", true, () -> this.mode.getValue() == Mode.NCP && this.useTimer.getValue()));
    private final BooleanSetting onlyMoving = this.add(new BooleanSetting("OnlyMoving", true));
    private final BooleanSetting surroundPause = this.add(new BooleanSetting("SurroundPause", true));
    private final BooleanSetting inWebPause = this.add(new BooleanSetting("InWebPause", true));
    private final BooleanSetting inBlockPause = this.add(new BooleanSetting("InBlockPause", true));
    private final BooleanSetting sneakingPause = this.add(new BooleanSetting("SneakingPause", true));
    private final BooleanSetting pathingPause = this.add(new BooleanSetting("PathingPause", true));
    boolean timer;
    int packets = 0;

    public Step() {
        super("Step", "Steps up blocks.", Module.Category.Movement);
        this.setChinese("\u6b65\u884c\u8f85\u52a9");
    }

    public static void setStepHeight(float v) {
        Step.mc.field_1724.method_5996(class_5134.field_47761).method_6192((double)v);
    }

    @Override
    public void onDisable() {
        if (!Step.nullCheck()) {
            Step.setStepHeight(0.6f);
            Alien.TIMER.reset();
        }
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (!(this.pathingPause.getValue() && BaritoneUtil.isActive() || this.sneakingPause.getValue() && Step.mc.field_1724.method_5715() || this.inBlockPause.getValue() && EntityUtil.isInsideBlock() || Step.mc.field_1724.method_5771() || Step.mc.field_1724.method_5799() || this.inWebPause.getValue() && Alien.PLAYER.isInWeb((class_1657)Step.mc.field_1724) || !Step.mc.field_1724.method_24828() || this.onlyMoving.getValue() && !MovementUtil.isMoving() || this.surroundPause.getValue() && (Surround.INSTANCE.isOn() || SelfTrap.INSTANCE.isOn()))) {
            Step.setStepHeight(this.height.getValueFloat());
        } else {
            Step.setStepHeight(0.6f);
        }
    }

    @EventListener
    public void onStep(TickEvent event) {
        if (event.isPost()) {
            --this.packets;
        } else {
            boolean strict;
            if (this.timer && this.packets <= 0) {
                Alien.TIMER.reset();
                this.timer = false;
            }
            boolean bl = strict = this.mode.getValue() == Mode.NCP;
            if (this.mode.getValue().equals((Object)Mode.OldNCP) || strict) {
                double stepHeight = Step.mc.field_1724.method_23318() - Step.mc.field_1724.field_6036;
                if (stepHeight <= 0.75 || stepHeight > this.height.getValue()) {
                    return;
                }
                double[] offsets = this.getOffset(stepHeight);
                if (offsets != null && offsets.length > 1) {
                    if (this.useTimer.getValue()) {
                        Alien.TIMER.set((float)this.getTimer(stepHeight));
                        this.timer = true;
                        this.packets = 2;
                    }
                    for (double offset : offsets) {
                        mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Step.mc.field_1724.field_6014, Step.mc.field_1724.field_6036 + offset, Step.mc.field_1724.field_5969, false));
                    }
                }
            }
        }
    }

    public double getTimer(double height) {
        if (height > 0.6 && height <= 1.0) {
            return !this.fast.getValue() && this.mode.getValue() == Mode.NCP ? 0.3333333333333333 : 0.5;
        }
        double[] offsets = this.getOffset(height);
        return offsets == null ? 1.0 : 1.0 / (double)offsets.length;
    }

    public double[] getOffset(double height) {
        double[] dArray;
        boolean strict;
        boolean bl = strict = this.mode.getValue() == Mode.NCP;
        if (height == 0.75) {
            double[] dArray2;
            if (strict) {
                double[] dArray3 = new double[3];
                dArray3[0] = 0.42;
                dArray3[1] = 0.753;
                dArray2 = dArray3;
                dArray3[2] = 0.75;
            } else {
                double[] dArray4 = new double[2];
                dArray4[0] = 0.42;
                dArray2 = dArray4;
                dArray4[1] = 0.753;
            }
            return dArray2;
        }
        if (height == 0.8125) {
            double[] dArray5;
            if (strict) {
                double[] dArray6 = new double[3];
                dArray6[0] = 0.39;
                dArray6[1] = 0.7;
                dArray5 = dArray6;
                dArray6[2] = 0.8125;
            } else {
                double[] dArray7 = new double[2];
                dArray7[0] = 0.39;
                dArray5 = dArray7;
                dArray7[1] = 0.7;
            }
            return dArray5;
        }
        if (height == 0.875) {
            double[] dArray8;
            if (strict) {
                double[] dArray9 = new double[3];
                dArray9[0] = 0.39;
                dArray9[1] = 0.7;
                dArray8 = dArray9;
                dArray9[2] = 0.875;
            } else {
                double[] dArray10 = new double[2];
                dArray10[0] = 0.39;
                dArray8 = dArray10;
                dArray10[1] = 0.7;
            }
            return dArray8;
        }
        if (height == 1.0) {
            double[] dArray11;
            if (strict) {
                double[] dArray12 = new double[3];
                dArray12[0] = 0.42;
                dArray12[1] = 0.753;
                dArray11 = dArray12;
                dArray12[2] = 1.0;
            } else {
                double[] dArray13 = new double[2];
                dArray13[0] = 0.42;
                dArray11 = dArray13;
                dArray13[1] = 0.753;
            }
            return dArray11;
        }
        if (height == 1.5) {
            return new double[]{0.42, 0.75, 1.0, 1.16, 1.23, 1.2};
        }
        if (height == 2.0) {
            return new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
        }
        if (height == 2.5) {
            double[] dArray14 = new double[10];
            dArray14[0] = 0.425;
            dArray14[1] = 0.821;
            dArray14[2] = 0.699;
            dArray14[3] = 0.599;
            dArray14[4] = 1.022;
            dArray14[5] = 1.372;
            dArray14[6] = 1.652;
            dArray14[7] = 1.869;
            dArray14[8] = 2.019;
            dArray = dArray14;
            dArray14[9] = 1.907;
        } else {
            dArray = null;
        }
        return dArray;
    }

    public static enum Mode {
        Vanilla,
        OldNCP,
        NCP;

    }
}

