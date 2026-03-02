/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1299
 *  net.minecraft.class_1538
 *  net.minecraft.class_1657
 *  net.minecraft.class_1937
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DeathEvent;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1538;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_3417;
import net.minecraft.class_3419;

public class KillEffect
extends Module {
    private final BooleanSetting lightning = this.add(new BooleanSetting("Lightning", true));
    private final BooleanSetting levelUp = this.add(new BooleanSetting("LevelUp", true).setParent());
    private final SliderSetting lMaxPitch = this.add(new SliderSetting("LMaxPitch", 1.0, 0.0, 2.0, 0.1, this.levelUp::isOpen));
    private final SliderSetting lMinPitch = this.add(new SliderSetting("LMinPitch", 1.0, 0.0, 2.0, 0.1, this.levelUp::isOpen));
    private final BooleanSetting trident = this.add(new BooleanSetting("Trident", false).setParent());
    private final SliderSetting tMaxPitch = this.add(new SliderSetting("TMaxPitch", 1.0, 0.0, 2.0, 0.1, this.trident::isOpen));
    private final SliderSetting tMinPitch = this.add(new SliderSetting("TMinPitch", 1.0, 0.0, 2.0, 0.1, this.trident::isOpen));
    private final SliderSetting factor = this.add(new SliderSetting("Factor", 1.0, 1.0, 10.0, 1.0));

    public KillEffect() {
        super("KillEffect", Module.Category.Misc);
        this.setChinese("\u51fb\u6740\u6548\u679c");
    }

    @EventListener
    public void onPlayerDeath(DeathEvent event) {
        class_1657 player;
        if (!KillEffect.nullCheck() && (player = event.getPlayer()) != null) {
            int i = 0;
            while ((double)i < this.factor.getValue()) {
                this.doEffect(player);
                ++i;
            }
        }
    }

    private void doEffect(class_1657 player) {
        double x = player.method_23317();
        double y = player.method_23318();
        double z = player.method_23321();
        if (this.lightning.getValue()) {
            class_1538 lightningEntity = new class_1538(class_1299.field_6112, (class_1937)KillEffect.mc.field_1687);
            lightningEntity.method_30634(x, y, z);
            lightningEntity.method_24203(x, y, z);
            KillEffect.mc.field_1687.method_53875((class_1297)lightningEntity);
        }
        if (this.levelUp.getValue()) {
            KillEffect.mc.field_1687.method_43128((class_1657)KillEffect.mc.field_1724, x, y, z, class_3417.field_14709, class_3419.field_15248, 100.0f, MathUtil.random(this.lMinPitch.getValueFloat(), this.lMaxPitch.getValueFloat()));
        }
        if (this.trident.getValue()) {
            KillEffect.mc.field_1687.method_60511((class_1657)KillEffect.mc.field_1724, x, y, z, class_3417.field_14896, class_3419.field_15250, 999.0f, MathUtil.random(this.tMinPitch.getValueFloat(), this.tMaxPitch.getValueFloat()));
        }
    }
}

