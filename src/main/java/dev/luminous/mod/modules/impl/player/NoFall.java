/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2596
 *  net.minecraft.class_2828
 *  net.minecraft.class_2828$class_2830
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.asm.accessors.IPlayerMoveC2SPacket;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.BowBomb;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2828;

public class NoFall
extends Module {
    private final EnumSetting<NoFallMode> mode = this.add(new EnumSetting<NoFallMode>("Mode", NoFallMode.Packet));
    private final SliderSetting distance = this.add(new SliderSetting("Distance", 3.0, 0.0, 8.0, 0.1));

    public NoFall() {
        super("NoFall", "Prevents fall damage.", Module.Category.Player);
        this.setChinese("\u6ca1\u6709\u6454\u843d\u4f24\u5bb3");
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (!NoFall.nullCheck() && this.mode.is(NoFallMode.Grim) && this.checkFalling()) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2830(NoFall.mc.field_1724.method_23317(), NoFall.mc.field_1724.method_23318() + 1.0E-9, NoFall.mc.field_1724.method_23321(), NoFall.mc.field_1724.method_36454(), NoFall.mc.field_1724.method_36455(), false));
            NoFall.mc.field_1724.method_38785();
        }
    }

    private boolean checkFalling() {
        return NoFall.mc.field_1724.field_6017 > (float)NoFall.mc.field_1724.method_5850() && !NoFall.mc.field_1724.method_24828() && !NoFall.mc.field_1724.method_6128();
    }

    @EventListener
    public void onPacketSend(PacketEvent.Send event) {
        if (!NoFall.nullCheck()) {
            class_2596<?> class_25962;
            for (class_1799 is : NoFall.mc.field_1724.method_5661()) {
                if (is.method_7909() != class_1802.field_8833) continue;
                return;
            }
            if (this.mode.is(NoFallMode.Packet) && (class_25962 = event.getPacket()) instanceof class_2828) {
                class_2828 packet = (class_2828)class_25962;
                if (NoFall.mc.field_1724.field_6017 >= (float)this.distance.getValue() && !BowBomb.send) {
                    ((IPlayerMoveC2SPacket)packet).setOnGround(true);
                }
            }
        }
    }

    public static enum NoFallMode {
        Packet,
        Grim;

    }
}

