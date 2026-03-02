/*
 * Decompiled with CFR 0.152.
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.SliderSetting;

public class IQBoost
extends Module {
    private final SliderSetting iq = this.add(new SliderSetting("IQ", 114514.0, 0.0, 114514.0, 1.0));

    public IQBoost() {
        super("IQBoost", Module.Category.Combat);
        this.setChinese("IQ\u63d0\u5347");
    }

    @Override
    public String getInfo() {
        return String.valueOf(this.iq.getValue());
    }
}

