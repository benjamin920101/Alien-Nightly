/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffects
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.utils.path.BaritoneUtil;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.Fly;
import dev.luminous.mod.modules.impl.movement.HoleSnap;
import dev.luminous.mod.modules.impl.movement.Speed;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import java.util.Objects;
import net.minecraft.entity.effect.StatusEffects;

public class Strafe
extends Module {
    public static Strafe INSTANCE;
    private final BooleanSetting airStop = this.add(new BooleanSetting("AirStop", true));
    private final BooleanSetting slowCheck = this.add(new BooleanSetting("SlowCheck", true));

    public Strafe() {
        super("Strafe", "Modifies sprinting", Module.Category.Movement);
        this.setChinese("\u7075\u6d3b\u79fb\u52a8");
        INSTANCE = this;
    }

    @EventListener
    public void onStrafe(MoveEvent event) {
        if (!(BaritoneUtil.isActive() || Strafe.mc.field_1724.method_5715() || Fly.INSTANCE.isOn() || HoleSnap.INSTANCE.isOn() || Speed.INSTANCE.isOn() || Strafe.mc.field_1724.method_6128() || EntityUtil.isInsideBlock() || Strafe.mc.field_1724.method_5771() || Strafe.mc.field_1724.method_5799() || Strafe.mc.field_1724.method_31549().field_7479)) {
            if (!MovementUtil.isMoving()) {
                if (this.airStop.getValue()) {
                    MovementUtil.setMotionX(0.0);
                    MovementUtil.setMotionZ(0.0);
                }
            } else {
                double[] dir = MovementUtil.directionSpeed(this.getBaseMoveSpeed());
                event.setX(dir[0]);
                event.setZ(dir[1]);
            }
        }
    }

    public double getBaseMoveSpeed() {
        double n = 0.2873;
        if (!(!Strafe.mc.field_1724.method_6059(class_1294.field_5904) || this.slowCheck.getValue() && Strafe.mc.field_1724.method_6059(class_1294.field_5909))) {
            n *= 1.0 + 0.2 * (double)(Objects.requireNonNull(Strafe.mc.field_1724.method_6112(class_1294.field_5904)).method_5578() + 1);
        }
        return n;
    }
}

