/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2561
 *  net.minecraft.class_2596
 *  net.minecraft.class_2661
 *  net.minecraft.class_2868
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.TotemEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_2661;
import net.minecraft.class_2868;

public class AutoLog
extends Module {
    public static boolean loggedOut = false;
    private final BooleanSetting logOnEnable = this.add(new BooleanSetting("LogOnEnable", false));
    private final BooleanSetting onPop = this.add(new BooleanSetting("OnPop", true));
    private final BooleanSetting lowArmor = this.add(new BooleanSetting("LowArmor", true));
    private final BooleanSetting totemLess = this.add(new BooleanSetting("TotemLess", true).setParent());
    private final SliderSetting totems = this.add(new SliderSetting("Totems", 2.0, 0.0, 20.0, 1.0, this.totemLess::isOpen));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
    private final BooleanSetting showReason = this.add(new BooleanSetting("ShowReason", false));

    public AutoLog() {
        super("AutoLog", Module.Category.Misc);
        this.setChinese("\u81ea\u52a8\u4e0b\u7ebf");
    }

    @Override
    public boolean onEnable() {
        if (this.logOnEnable.getValue()) {
            this.disconnect("Enabled");
        }
        return false;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        int totem;
        if (this.totemLess.getValue() && (double)(totem = InventoryUtil.getItemCount(class_1802.field_8288)) <= this.totems.getValue()) {
            this.disconnect("You have too few totems (" + totem + ").");
            return;
        }
        if (this.lowArmor.getValue()) {
            for (class_1799 armor : AutoLog.mc.field_1724.method_31548().field_7548) {
                int damage;
                if (armor.method_7960() || (damage = EntityUtil.getDamagePercent(armor)) >= 5) continue;
                this.disconnect("Your armor has a durability of less than 5%.");
                return;
            }
        }
    }

    @EventListener
    public void onPop(TotemEvent event) {
        if (this.onPop.getValue() && event.getPlayer() == AutoLog.mc.field_1724) {
            this.disconnect("You poped 1 totem!");
        }
    }

    @Override
    public void onLogout() {
        if (this.autoDisable.getValue()) {
            this.disable();
        }
    }

    private void disconnect(String reason) {
        loggedOut = true;
        CommandManager.sendMessage("\u00a74[AutoLog] " + reason);
        mc.method_1562().method_52787((class_2596)new class_2868(114514));
        if (this.showReason.getValue()) {
            AutoLog.mc.field_1724.field_3944.method_52781(new class_2661((class_2561)class_2561.method_43470((String)("[AutoLog]" + reason))));
        }
    }
}

