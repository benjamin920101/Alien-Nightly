/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2596
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 *  net.minecraft.class_2886
 *  net.minecraft.class_9334
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import net.minecraft.class_2886;
import net.minecraft.class_9334;

public class PacketEat
extends Module {
    public static PacketEat INSTANCE;
    private final BooleanSetting deSync = this.add(new BooleanSetting("DeSync", false));
    private final BooleanSetting noRelease = this.add(new BooleanSetting("NoRelease", true));

    public PacketEat() {
        super("PacketEat", Module.Category.Player);
        this.setChinese("\u53d1\u5305\u8fdb\u98df");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.deSync.getValue() && PacketEat.mc.field_1724.method_6115() && PacketEat.mc.field_1724.method_6030().method_7909().method_57347().method_57832(class_9334.field_50075)) {
            Module.sendSequencedPacket(id -> new class_2886(PacketEat.mc.field_1724.method_6058(), id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
        }
    }

    @EventListener
    public void onPacket(PacketEvent.Send event) {
        class_2846 packet;
        class_2596<?> class_25962;
        if (this.noRelease.getValue() && (class_25962 = event.getPacket()) instanceof class_2846 && (packet = (class_2846)class_25962).method_12363() == class_2846.class_2847.field_12974 && PacketEat.mc.field_1724.method_6030().method_7909().method_57347().method_57832(class_9334.field_50075)) {
            event.cancel();
        }
    }
}

