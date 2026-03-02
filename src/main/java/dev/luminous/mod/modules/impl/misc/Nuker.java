/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.impl.render.PlaceRender;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2846;

public class Nuker
extends Module {
    private final SliderSetting range = this.add(new SliderSetting("Range", 4.0, 0.0, 8.0, 0.1));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", false));
    private final BooleanSetting sand = this.add(new BooleanSetting("Sand", false));
    private final SliderSetting breaks = this.add(new SliderSetting("Breaks", 10, 0, 20, this.sand::getValue));

    public Nuker() {
        super("Nuker", Module.Category.Misc);
        this.setChinese("\u8303\u56f4\u6316\u6398");
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (SpeedMine.getBreakPos() == null || Nuker.mc.field_1687.method_22347(SpeedMine.getBreakPos())) {
            if (this.sand.getValue()) {
                if (!Nuker.mc.field_1724.method_24828()) {
                    return;
                }
                int b = 0;
                for (class_2338 sand : BlockUtil.getSphere(this.range.getValueFloat(), Nuker.mc.field_1724.method_33571())) {
                    class_2350 side;
                    if (class_2246.field_10102 != Nuker.mc.field_1687.method_8320(sand).method_26204() && class_2246.field_10534 != Nuker.mc.field_1687.method_8320(sand).method_26204() || (side = BlockUtil.getClickSideStrict(sand)) == null) continue;
                    PlaceRender.INSTANCE.create(sand);
                    Alien.ROTATION.snapAt(sand.method_46558());
                    Nuker.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12973, sand, side, id));
                    Nuker.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12968, sand, side, id));
                    Nuker.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12973, sand, side, id));
                    Alien.ROTATION.snapBack();
                    if (!((double)(++b) >= this.breaks.getValue())) continue;
                    return;
                }
            } else {
                class_2338 pos = this.getBlock();
                if (pos != null) {
                    SpeedMine.INSTANCE.mine(pos);
                }
            }
        }
    }

    private class_2338 getBlock() {
        class_2338 down = null;
        for (class_2338 pos : BlockUtil.getSphere(this.range.getValueFloat(), Nuker.mc.field_1724.method_33571())) {
            if (Nuker.mc.field_1687.method_22347(pos) || SpeedMine.unbreakable(pos) || BlockUtil.getClickSideStrict(pos) == null) continue;
            if (!((double)pos.method_10264() < Nuker.mc.field_1724.method_23318())) {
                return pos;
            }
            if (down != null || !this.down.getValue()) continue;
            down = pos;
        }
        return down;
    }
}

