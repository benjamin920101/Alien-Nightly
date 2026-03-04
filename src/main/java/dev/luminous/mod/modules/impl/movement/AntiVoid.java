/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class AntiVoid
extends Module {
    private final SliderSetting voidHeight = this.add(new SliderSetting("VoidHeight", -64.0, -64.0, 319.0, 1.0));
    private final SliderSetting height = this.add(new SliderSetting("Height", 100.0, -40.0, 256.0, 1.0));

    public AntiVoid() {
        super("AntiVoid", "Allows you to fly over void blocks", Module.Category.Movement);
        this.setChinese("\u53cd\u865a\u7a7a");
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        boolean isVoid = true;
        for (int i = (int)AntiVoid.mc.field_1724.method_23318(); i > this.voidHeight.getValueInt() - 1; --i) {
            if (AntiVoid.mc.field_1687.method_8320((class_2338)new BlockPosX(AntiVoid.mc.field_1724.method_23317(), i, AntiVoid.mc.field_1724.method_23321())).method_26215() || AntiVoid.mc.field_1687.method_8320((class_2338)new BlockPosX(AntiVoid.mc.field_1724.method_23317(), i, AntiVoid.mc.field_1724.method_23321())).method_26204() == class_2246.field_10243) continue;
            isVoid = false;
            break;
        }
        if (AntiVoid.mc.field_1724.method_23318() < this.height.getValue() + this.voidHeight.getValue() && isVoid) {
            MovementUtil.setMotionY(0.0);
        }
    }
}

