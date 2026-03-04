/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket$Mode
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

public class AutoFuck
extends Module {
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 500, 0, 2000));
    private long lastTime = 0L;
    private boolean sneaking = false;

    public AutoFuck() {
        super("AutoFuck", Module.Category.Player);
        this.setChinese("\u81ea\u52a8\u4e0b\u8e72");
    }

    @Override
    public boolean onEnable() {
        this.lastTime = 0L;
        this.sneaking = false;
        return false;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (AutoFuck.mc.field_1724 == null || mc.method_1562() == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if ((double)(now - this.lastTime) >= this.delay.getValue()) {
            this.sneaking = !this.sneaking;
            class_2848.class_2849 mode = this.sneaking ? class_2848.class_2849.field_12979 : class_2848.class_2849.field_12984;
            mc.method_1562().method_52787((class_2596)new class_2848((class_1297)AutoFuck.mc.field_1724, mode));
            this.lastTime = now;
        }
    }
}

