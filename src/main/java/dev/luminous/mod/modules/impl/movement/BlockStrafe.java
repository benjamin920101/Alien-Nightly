/*
 * Decompiled with CFR 0.152.
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.AutoAnchor;
import dev.luminous.mod.modules.settings.impl.SliderSetting;

public class BlockStrafe
extends Module {
    public static BlockStrafe INSTANCE;
    private final SliderSetting speed = this.add(new SliderSetting("Speed", 10.0, 0.0, 20.0, 1.0).setSuffix("%"));
    private final SliderSetting aSpeed = this.add(new SliderSetting("AnchorSpeed", 3.0, 0.0, 20.0, 1.0).setSuffix("%"));

    public BlockStrafe() {
        super("BlockStrafe", Module.Category.Movement);
        this.setChinese("\u65b9\u5757\u7075\u6d3b\u79fb\u52a8");
        INSTANCE = this;
    }

    @EventListener
    public void onMove(MoveEvent event) {
        if (EntityUtil.isInsideBlock()) {
            double speed = AutoAnchor.INSTANCE.currentPos == null ? this.speed.getValue() : this.aSpeed.getValue();
            double moveSpeed = 0.002873 * speed;
            double n = BlockStrafe.mc.field_1724.field_3913.field_3905;
            double n2 = BlockStrafe.mc.field_1724.field_3913.field_3907;
            double n3 = BlockStrafe.mc.field_1724.method_36454();
            if (n == 0.0 && n2 == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            } else {
                if (n != 0.0 && n2 != 0.0) {
                    n *= Math.sin(0.7853981633974483);
                    n2 *= Math.cos(0.7853981633974483);
                }
                event.setX(n * moveSpeed * -Math.sin(Math.toRadians(n3)) + n2 * moveSpeed * Math.cos(Math.toRadians(n3)));
                event.setZ(n * moveSpeed * Math.cos(Math.toRadians(n3)) - n2 * moveSpeed * -Math.sin(Math.toRadians(n3)));
            }
        }
    }
}

