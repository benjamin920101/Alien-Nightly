/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1294
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventHandler;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1294;

public class AntiSlowFall
extends Module {
    public static AntiSlowFall INSTANCE;
    private final BooleanSetting bypassSlowFalling = this.add(new BooleanSetting("BypassSlowFalling", true));
    final SliderSetting y = this.add(new SliderSetting("motion-y", 0.01, 0.0, 1.0, this.bypassSlowFalling::getValue));
    final SliderSetting jumpDelay = this.add(new SliderSetting("jump-delay", 0, 0, 500, this.bypassSlowFalling::getValue));
    Timer jumpTimer = new Timer();

    public AntiSlowFall() {
        super("AntiSlowFall", Module.Category.Movement);
        this.setChinese("\u53cd\u7f13\u89e3");
        INSTANCE = this;
    }

    @EventHandler
    public void onUpdate() {
        if (AntiSlowFall.nullCheck()) {
            return;
        }
        if (!this.bypassSlowFalling.getValue()) {
            return;
        }
        if (!this.jumpTimer.passedMs(this.jumpDelay.getValue())) {
            return;
        }
        if (!AntiSlowFall.mc.field_1724.method_6059(class_1294.field_5906)) {
            return;
        }
        if (AntiSlowFall.mc.field_1724.method_7325()) {
            return;
        }
        if (!EntityUtil.isInsideBlock()) {
            return;
        }
        if (AntiSlowFall.mc.field_1724.method_24828() && AntiSlowFall.mc.field_1724.field_6017 <= 0.0f) {
            MovementUtil.setMotionY(this.y.getValue());
            this.jumpTimer.reset();
        }
    }
}

