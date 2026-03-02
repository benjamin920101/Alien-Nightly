/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2596
 *  net.minecraft.class_2828$class_2829
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import net.minecraft.class_2596;
import net.minecraft.class_2828;

public class VClip
extends Module {
    private final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Jump));

    public VClip() {
        super("VClip", Module.Category.Movement);
        this.setChinese("\u7eb5\u5411\u7a7f\u5899");
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.disable();
        switch (this.mode.getValue().ordinal()) {
            case 0: {
                double posX = VClip.mc.field_1724.method_23317();
                double posY = Math.round(VClip.mc.field_1724.method_23318());
                double posZ = VClip.mc.field_1724.method_23321();
                boolean onGround = VClip.mc.field_1724.method_24828();
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(posX, posY, posZ, onGround));
                double halfY = 0.005;
                VClip.mc.field_1724.method_5814(posX, posY -= halfY, posZ);
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(posX, posY, posZ, onGround));
                VClip.mc.field_1724.method_5814(posX, posY -= halfY * 300.0, posZ);
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(posX, posY, posZ, onGround));
                break;
            }
            case 1: {
                VClip.mc.field_1724.method_5814(VClip.mc.field_1724.method_23317(), VClip.mc.field_1724.method_23318() + 3.0, VClip.mc.field_1724.method_23321());
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(VClip.mc.field_1724.method_23317(), VClip.mc.field_1724.method_23318(), VClip.mc.field_1724.method_23321(), true));
                break;
            }
            case 2: {
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(VClip.mc.field_1724.method_23317(), VClip.mc.field_1724.method_23318() + 0.4199999868869781, VClip.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(VClip.mc.field_1724.method_23317(), VClip.mc.field_1724.method_23318() + 0.7531999805212017, VClip.mc.field_1724.method_23321(), false));
                VClip.mc.field_1724.method_5814(VClip.mc.field_1724.method_23317(), VClip.mc.field_1724.method_23318() + 1.0, VClip.mc.field_1724.method_23321());
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(VClip.mc.field_1724.method_23317(), VClip.mc.field_1724.method_23318(), VClip.mc.field_1724.method_23321(), true));
            }
        }
    }

    public static enum Mode {
        Glitch,
        Teleport,
        Jump;

    }
}

