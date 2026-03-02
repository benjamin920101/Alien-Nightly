/*
 * Decompiled with CFR 0.152.
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;

public class WindowTitle
extends Module {
    public static WindowTitle INSTANCE;
    public final StringSetting title = this.add(new StringSetting("Title", "AlienV4-Nightly 02/07/2026 21:32"));
    public final SliderSetting speed = this.add(new SliderSetting("ms", 50, 50, 1000));
    private final Timer timer = new Timer();
    private int currentLength = 0;
    private boolean increasing = true;

    public WindowTitle() {
        super("WindowTitle", Module.Category.Misc);
        this.setChinese("\u52a8\u6001\u7a97\u53e3\u6807\u9898");
        INSTANCE = this;
        this.enable();
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.timer.passedMs(this.speed.getValueInt())) {
            this.timer.reset();
            String titleValue = this.title.getValue();
            if (this.increasing) {
                ++this.currentLength;
                if (this.currentLength >= titleValue.length()) {
                    this.increasing = false;
                }
            } else {
                --this.currentLength;
                if (this.currentLength <= 0) {
                    this.increasing = true;
                }
            }
        }
    }

    public String getDynamicTitle() {
        String titleValue = this.title.getValue();
        if (this.currentLength <= 0) {
            return "";
        }
        if (this.currentLength >= titleValue.length()) {
            return titleValue;
        }
        return titleValue.substring(0, this.currentLength);
    }
}

