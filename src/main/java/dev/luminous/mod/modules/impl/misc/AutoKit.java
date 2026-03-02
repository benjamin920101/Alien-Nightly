/*
 * Decompiled with CFR 0.152.
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DeathEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.StringSetting;

public class AutoKit
extends Module {
    public static AutoKit INSTANCE;
    final StringSetting command = this.add(new StringSetting("Command", "kit 1"));
    boolean kit = false;
    final Timer timer = new Timer();

    public AutoKit() {
        super("AutoKit", Module.Category.Misc);
        this.setChinese("\u81ea\u52a8\u914d\u88c5\u547d\u4ee4");
        INSTANCE = this;
    }

    @Override
    public void onLogin() {
        this.kit = true;
        this.timer.reset();
    }

    @EventListener
    public void onDeath(DeathEvent event) {
        if (event.getPlayer() == AutoKit.mc.field_1724) {
            this.kit = true;
            this.timer.reset();
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.kit && this.timer.passedS(2.0)) {
            this.kit = false;
            AutoKit.mc.field_1724.field_3944.method_45731(this.command.getValue());
        }
    }
}

