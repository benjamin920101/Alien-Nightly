/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.Box
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.KeyboardInputEvent;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class SafeWalk
extends Module {
    public static SafeWalk INSTANCE;
    private final BooleanSetting legit = this.add(new BooleanSetting("Legit", true));
    private final BooleanSetting onlyInsideBlock = this.add(new BooleanSetting("OnlyInsideBlock", false));

    public SafeWalk() {
        super("SafeWalk", Module.Category.Movement);
        this.setChinese("\u8fb9\u7f18\u884c\u8d70");
        INSTANCE = this;
    }

    @EventListener(priority=-200)
    public void keyboard(KeyboardInputEvent event) {
        if (SafeWalk.mc.field_1724.method_24828() && this.legit.getValue() && this.shouldSafeWalk()) {
            if (this.isOffsetBBEmpty(0.3, -1.0, 0.0)) {
                SafeWalk.mc.field_1724.field_3913.field_3903 = true;
            } else if (this.isOffsetBBEmpty(0.0, -1.0, 0.3)) {
                SafeWalk.mc.field_1724.field_3913.field_3903 = true;
            } else if (this.isOffsetBBEmpty(0.3, -1.0, 0.3)) {
                SafeWalk.mc.field_1724.field_3913.field_3903 = true;
            } else if (this.isOffsetBBEmpty(-0.3, -1.0, 0.0)) {
                SafeWalk.mc.field_1724.field_3913.field_3903 = true;
            } else if (this.isOffsetBBEmpty(0.0, -1.0, -0.3)) {
                SafeWalk.mc.field_1724.field_3913.field_3903 = true;
            } else if (this.isOffsetBBEmpty(-0.3, -1.0, -0.3)) {
                SafeWalk.mc.field_1724.field_3913.field_3903 = true;
            }
        }
    }

    @EventListener(priority=-100)
    public void onMove(MoveEvent event) {
        if (SafeWalk.mc.field_1724.method_24828() && !this.legit.getValue() && this.shouldSafeWalk()) {
            double x = event.getX();
            double y = event.getY();
            double z = event.getZ();
            double increment = 0.05;
            while (x != 0.0 && this.isOffsetBBEmpty(x, -1.0, 0.0)) {
                if (x < increment && x >= -increment) {
                    x = 0.0;
                    continue;
                }
                if (x > 0.0) {
                    x -= increment;
                    continue;
                }
                x += increment;
            }
            while (z != 0.0 && this.isOffsetBBEmpty(0.0, -1.0, z)) {
                if (z < increment && z >= -increment) {
                    z = 0.0;
                    continue;
                }
                if (z > 0.0) {
                    z -= increment;
                    continue;
                }
                z += increment;
            }
            while (x != 0.0 && z != 0.0 && this.isOffsetBBEmpty(x, -1.0, z)) {
                double d = x < increment && x >= -increment ? 0.0 : (x = x > 0.0 ? x - increment : x + increment);
                if (z < increment && z >= -increment) {
                    z = 0.0;
                    continue;
                }
                if (z > 0.0) {
                    z -= increment;
                    continue;
                }
                z += increment;
            }
            event.setX(x);
            event.setY(y);
            event.setZ(z);
        }
    }

    public boolean shouldSafeWalk() {
        return !this.onlyInsideBlock.getValue() || EntityUtil.isInsideBlock();
    }

    public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
        class_238 playerBox = SafeWalk.mc.field_1724.method_5829();
        class_238 box = new class_238(playerBox.field_1323, playerBox.field_1322, playerBox.field_1324, playerBox.field_1320, playerBox.field_1322 + 0.5, playerBox.field_1324);
        return !BlockUtil.canCollide((class_1297)SafeWalk.mc.field_1724, box.method_989(offsetX, offsetY, offsetZ));
    }
}

