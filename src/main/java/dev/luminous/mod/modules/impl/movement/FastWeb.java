/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2560
 *  net.minecraft.class_2596
 *  net.minecraft.class_2680
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.TimerEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2560;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2846;

public class FastWeb
extends Module {
    public static FastWeb INSTANCE;
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Vanilla));
    public final BooleanSetting onlySneak = this.add(new BooleanSetting("OnlySneak", true));
    public final BooleanSetting grim = this.add(new BooleanSetting("Grim", false).setParent());
    public final BooleanSetting abortPacket = this.add(new BooleanSetting("AbortPacket", true, this.grim::isOpen));
    public final SliderSetting xZSlow = this.add(new SliderSetting("XZSpeed", 25.0, 0.0, 100.0, 0.1, () -> this.mode.getValue() == Mode.Custom).setSuffix("%"));
    public final SliderSetting ySlow = this.add(new SliderSetting("YSpeed", 100.0, 0.0, 100.0, 0.1, () -> this.mode.getValue() == Mode.Custom).setSuffix("%"));
    private final SliderSetting fastSpeed = this.add(new SliderSetting("Speed", 3.0, 0.0, 8.0, () -> this.mode.getValue() == Mode.Vanilla || this.mode.getValue() == Mode.Strict));
    private boolean work = false;

    public FastWeb() {
        super("FastWeb", "So you don't need to keep timer on keybind", Module.Category.Movement);
        this.setChinese("\u8718\u86db\u7f51\u52a0\u901f");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        boolean bl = this.work = !FastWeb.mc.field_1724.method_24828() && (FastWeb.mc.field_1690.field_1832.method_1434() || !this.onlySneak.getValue()) && Alien.PLAYER.isInWeb((class_1657)FastWeb.mc.field_1724);
        if (this.work && this.mode.is(Mode.Vanilla)) {
            MovementUtil.setMotionY(-this.fastSpeed.getValue());
        }
        if (this.grim.getValue() && (FastWeb.mc.field_1690.field_1832.method_1434() || !this.onlySneak.getValue())) {
            for (class_2338 pos : this.getIntersectingWebs()) {
                if (this.abortPacket.getValue()) {
                    mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12971, pos, class_2350.field_11033));
                }
                mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12973, pos, class_2350.field_11033));
            }
        }
    }

    @EventListener(priority=-100)
    public void onTimer(TimerEvent event) {
        if (this.work && this.mode.getValue() == Mode.Strict) {
            event.set(this.fastSpeed.getValueFloat());
        }
    }

    public List<class_2338> getIntersectingWebs() {
        int radius = 2;
        ArrayList<class_2338> blocks = new ArrayList<class_2338>();
        for (int x = radius; x > -radius; --x) {
            for (int y = radius; y > -radius; --y) {
                for (int z = radius; z > -radius; --z) {
                    class_2680 state;
                    class_2338 blockPos = class_2338.method_49637((double)(FastWeb.mc.field_1724.method_23317() + (double)x), (double)(FastWeb.mc.field_1724.method_23318() + (double)y), (double)(FastWeb.mc.field_1724.method_23321() + (double)z));
                    if (FastWeb.mc.field_1724.method_19538().method_1022(blockPos.method_46558()) > 1.0 && FastWeb.mc.field_1724.method_33571().method_1022(blockPos.method_46558()) > 1.0 || !((state = FastWeb.mc.field_1687.method_8320(blockPos)).method_26204() instanceof class_2560)) continue;
                    blocks.add(blockPos);
                }
            }
        }
        return blocks;
    }

    public static enum Mode {
        Vanilla,
        Strict,
        Custom,
        Ignore;

    }
}

