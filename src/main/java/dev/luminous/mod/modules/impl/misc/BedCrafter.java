/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1714
 *  net.minecraft.class_1748
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_3532
 *  net.minecraft.class_516
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_8786
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1714;
import net.minecraft.class_1748;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_3532;
import net.minecraft.class_516;
import net.minecraft.class_7225;
import net.minecraft.class_8786;

public class BedCrafter
extends Module {
    public static BedCrafter INSTANCE;
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", false));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5, 0, 8));
    private final BooleanSetting disable = this.add(new BooleanSetting("Disable", true));

    public BedCrafter() {
        super("BedCrafter", Module.Category.Misc);
        this.setChinese("\u81ea\u52a8\u5236\u4f5c\u5e8a");
        INSTANCE = this;
    }

    public static int getEmptySlots() {
        int emptySlots = 0;
        for (int i = 0; i < 36; ++i) {
            class_1799 itemStack = BedCrafter.mc.field_1724.method_31548().method_5438(i);
            if (!itemStack.method_7960()) continue;
            ++emptySlots;
        }
        return emptySlots;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (BedCrafter.getEmptySlots() == 0) {
            if (BedCrafter.mc.field_1724.field_7512 instanceof class_1714) {
                BedCrafter.mc.field_1724.method_7346();
            }
            if (this.disable.getValue()) {
                this.disable();
            }
        } else if (BedCrafter.mc.field_1724.field_7512 instanceof class_1714) {
            boolean craft = false;
            block0: for (class_516 recipeResult : BedCrafter.mc.field_1724.method_3130().method_1393()) {
                for (class_8786 recipe : recipeResult.method_2648(true)) {
                    if (!(recipe.comp_1933().method_8110((class_7225.class_7874)BedCrafter.mc.field_1687.method_30349()).method_7909() instanceof class_1748)) continue;
                    for (int i = 0; i < BedCrafter.getEmptySlots(); ++i) {
                        craft = true;
                        BedCrafter.mc.field_1761.method_2912(BedCrafter.mc.field_1724.field_7512.field_7763, recipe, false);
                        BedCrafter.mc.field_1761.method_2906(BedCrafter.mc.field_1724.field_7512.field_7763, 0, 1, class_1713.field_7794, (class_1657)BedCrafter.mc.field_1724);
                    }
                    continue block0;
                }
            }
            if (!craft) {
                if (BedCrafter.mc.field_1724.field_7512 instanceof class_1714) {
                    BedCrafter.mc.field_1724.method_7346();
                }
                if (this.disable.getValue()) {
                    this.disable();
                }
            }
        } else {
            this.doPlace();
        }
    }

    private void doPlace() {
        class_2338 bestPos = null;
        double distance = 100.0;
        boolean place = true;
        for (class_2338 pos : BlockUtil.getSphere(this.range.getValueFloat())) {
            if (BedCrafter.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_9980 && BlockUtil.getClickSideStrict(pos) != null) {
                place = false;
                bestPos = pos;
                break;
            }
            if (!BlockUtil.canPlace(pos) || bestPos != null && !((double)class_3532.method_15355((float)((float)BedCrafter.mc.field_1724.method_5707(pos.method_46558()))) < distance)) continue;
            bestPos = pos;
            distance = class_3532.method_15355((float)((float)BedCrafter.mc.field_1724.method_5707(pos.method_46558())));
        }
        if (bestPos != null) {
            if (!place) {
                BlockUtil.clickBlock(bestPos, BlockUtil.getClickSide(bestPos), this.rotate.getValue());
            } else {
                if (InventoryUtil.findItem(class_1802.field_8465) == -1) {
                    return;
                }
                int old = BedCrafter.mc.field_1724.method_31548().field_7545;
                InventoryUtil.switchToSlot(InventoryUtil.findItem(class_1802.field_8465));
                BlockUtil.placeBlock(bestPos, this.rotate.getValue());
                InventoryUtil.switchToSlot(old);
            }
        }
    }
}

