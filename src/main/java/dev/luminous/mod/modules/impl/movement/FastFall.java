/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2680
 *  net.minecraft.class_2708
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.TimerEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.Fly;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.HashMap;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2708;
import net.minecraft.class_3959;
import net.minecraft.class_3965;

public class FastFall
extends Module {
    private final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Fast));
    private final BooleanSetting noLag = this.add(new BooleanSetting("NoLag", true, () -> this.mode.getValue() == Mode.Fast));
    private final BooleanSetting useTimerSetting = this.add(new BooleanSetting("UseTimer", false));
    private final SliderSetting timer = this.add(new SliderSetting("Timer", 2.5, 1.0, 8.0, 0.1, this.useTimerSetting::getValue));
    private final BooleanSetting anchor = this.add(new BooleanSetting("Anchor", true));
    private final SliderSetting height = this.add(new SliderSetting("Height", 10.0, 1.0, 20.0, 0.5));
    private final Timer lagTimer = new Timer();
    boolean onGround = false;
    private boolean useTimer;

    public FastFall() {
        super("FastFall", "Miyagi son simulator", Module.Category.Movement);
        this.setChinese("\u5feb\u901f\u5760\u843d");
    }

    @Override
    public void onDisable() {
        this.useTimer = false;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @EventListener(priority=-100)
    public void onMove(MoveEvent event) {
        if (!FastFall.nullCheck() && FastFall.mc.field_1724.method_24828() && this.anchor.getValue() && this.traceDown() != 0 && (double)this.traceDown() <= this.height.getValue() && this.trace()) {
            event.setX(event.getX() * 0.05);
            event.setZ(event.getZ() * 0.05);
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (!(this.height.getValue() > 0.0 && (double)this.traceDown() > this.height.getValue() || FastFall.mc.field_1724.method_5757() || FastFall.mc.field_1724.method_5869() || FastFall.mc.field_1724.method_5771() || FastFall.mc.field_1724.method_21754() || !this.lagTimer.passed(1000L) || FastFall.mc.field_1724.method_6128() || Fly.INSTANCE.isOn() || FastFall.nullCheck() || Alien.PLAYER.isInWeb((class_1657)FastFall.mc.field_1724))) {
            if (FastFall.mc.field_1724.method_24828() && this.mode.getValue() == Mode.Fast) {
                MovementUtil.setMotionY(MovementUtil.getMotionY() - (double)(this.noLag.getValue() ? 0.62f : 1.0f));
            }
            if (this.useTimerSetting.getValue()) {
                if (!FastFall.mc.field_1724.method_24828()) {
                    if (this.onGround) {
                        this.useTimer = true;
                    }
                    if (MovementUtil.getMotionY() >= 0.0) {
                        this.useTimer = false;
                    }
                    this.onGround = false;
                } else {
                    this.useTimer = false;
                    MovementUtil.setMotionY(-0.08);
                    this.onGround = true;
                }
            } else {
                this.useTimer = false;
            }
        }
    }

    @EventListener
    public void onTimer(TimerEvent event) {
        if (!FastFall.nullCheck() && !FastFall.mc.field_1724.method_24828() && this.useTimer) {
            event.set(this.timer.getValueFloat());
        }
    }

    @EventListener
    public void onPacket(PacketEvent.Receive event) {
        if (!FastFall.nullCheck() && event.getPacket() instanceof class_2708) {
            this.lagTimer.reset();
        }
    }

    private int traceDown() {
        int y;
        int retval = 0;
        for (int tracey = y = (int)Math.round(FastFall.mc.field_1724.method_23318()) - 1; tracey >= 0; --tracey) {
            class_3965 trace = FastFall.mc.field_1687.method_17742(new class_3959(FastFall.mc.field_1724.method_19538(), new class_243(FastFall.mc.field_1724.method_23317(), (double)tracey, FastFall.mc.field_1724.method_23321()), class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)FastFall.mc.field_1724));
            if (trace != null && trace.method_17783() == class_239.class_240.field_1332) {
                return retval;
            }
            ++retval;
        }
        return retval;
    }

    private boolean trace() {
        class_238 bbox = FastFall.mc.field_1724.method_5829();
        class_243 basepos = bbox.method_1005();
        double minX = bbox.field_1323;
        double minZ = bbox.field_1321;
        double maxX = bbox.field_1320;
        double maxZ = bbox.field_1324;
        HashMap<class_243, class_243> positions = new HashMap<class_243, class_243>();
        positions.put(basepos, new class_243(basepos.field_1352, basepos.field_1351 - 1.0, basepos.field_1350));
        positions.put(new class_243(minX, basepos.field_1351, minZ), new class_243(minX, basepos.field_1351 - 1.0, minZ));
        positions.put(new class_243(maxX, basepos.field_1351, minZ), new class_243(maxX, basepos.field_1351 - 1.0, minZ));
        positions.put(new class_243(minX, basepos.field_1351, maxZ), new class_243(minX, basepos.field_1351 - 1.0, maxZ));
        positions.put(new class_243(maxX, basepos.field_1351, maxZ), new class_243(maxX, basepos.field_1351 - 1.0, maxZ));
        for (class_243 key : positions.keySet()) {
            class_3959 context = new class_3959(key, (class_243)positions.get(key), class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)FastFall.mc.field_1724);
            class_3965 result = FastFall.mc.field_1687.method_17742(context);
            if (result == null || result.method_17783() != class_239.class_240.field_1332) continue;
            return false;
        }
        class_2680 state = FastFall.mc.field_1687.method_8320((class_2338)new BlockPosX(FastFall.mc.field_1724.method_23317(), FastFall.mc.field_1724.method_23318() - 1.0, FastFall.mc.field_1724.method_23321()));
        return state.method_26215();
    }

    private static enum Mode {
        Fast,
        None;

    }
}

