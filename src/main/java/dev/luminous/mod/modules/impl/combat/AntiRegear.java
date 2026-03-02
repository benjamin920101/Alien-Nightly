/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2338
 *  net.minecraft.class_2480
 *  net.minecraft.class_2586
 *  net.minecraft.class_2627
 *  net.minecraft.class_3532
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PlaceBlockEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.AutoRegear;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2480;
import net.minecraft.class_2586;
import net.minecraft.class_2627;
import net.minecraft.class_3532;

public class AntiRegear
extends Module {
    public static AntiRegear INSTANCE;
    public final List<class_2338> safe = new ArrayList<class_2338>();
    private final SliderSetting safeRange = this.add(new SliderSetting("SafeRange", 2.0, 0.0, 8.0, 0.1));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 8.0, 0.1));
    private final BooleanSetting checkSelf = this.add(new BooleanSetting("CheckSelf", true));

    public AntiRegear() {
        super("AntiRegear", Module.Category.Combat);
        this.setChinese("\u53cd\u8865\u7ed9");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (SpeedMine.getBreakPos() == null || !(AntiRegear.mc.field_1687.method_8320(SpeedMine.getBreakPos()).method_26204() instanceof class_2480)) {
            this.safe.removeIf(pos -> !(AntiRegear.mc.field_1687.method_8320(pos).method_26204() instanceof class_2480));
            if (this.getBlock() != null) {
                SpeedMine.INSTANCE.mine(this.getBlock().method_11016());
            }
        }
    }

    @EventListener
    public void onPlace(PlaceBlockEvent event) {
        if (event.block instanceof class_2480) {
            this.safe.add(event.blockPos);
        }
    }

    private class_2627 getBlock() {
        for (class_2586 entity : BlockUtil.getTileEntities()) {
            class_2627 shulker;
            if (!(entity instanceof class_2627) || (double)class_3532.method_15355((float)((float)AntiRegear.mc.field_1724.method_5707((shulker = (class_2627)entity).method_11016().method_46558()))) <= this.safeRange.getValue() || this.checkSelf.getValue() && (this.safe.contains(shulker.method_11016()) || shulker.method_11016().equals((Object)AutoRegear.INSTANCE.placePos) && !AutoRegear.INSTANCE.timeoutTimer.passed(100L)) || !((double)class_3532.method_15355((float)((float)AntiRegear.mc.field_1724.method_5707(shulker.method_11016().method_46558()))) <= this.range.getValue())) continue;
            return shulker;
        }
        return null;
    }
}

