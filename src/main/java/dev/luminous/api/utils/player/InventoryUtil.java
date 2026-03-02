/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1291
 *  net.minecraft.class_1293
 *  net.minecraft.class_1304
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1747
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1844
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2596
 *  net.minecraft.class_2838
 *  net.minecraft.class_2868
 *  net.minecraft.class_9334
 */
package dev.luminous.api.utils.player;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1304;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2596;
import net.minecraft.class_2838;
import net.minecraft.class_2868;
import net.minecraft.class_9334;

public class InventoryUtil
implements Wrapper {
    static int lastSlot = -1;
    static int lastSelect = -1;

    public static void inventorySwap(int slot, int selectedSlot) {
        if (slot == lastSlot) {
            InventoryUtil.switchToSlot(lastSelect);
            lastSlot = -1;
            lastSelect = -1;
        } else if (slot - 36 != selectedSlot && EntityUtil.inInventory()) {
            if (AntiCheat.INSTANCE.invSwapBypass.getValue()) {
                if (slot - 36 >= 0) {
                    lastSlot = slot;
                    lastSelect = selectedSlot;
                    InventoryUtil.switchToSlot(slot - 36);
                    return;
                }
                mc.method_1562().method_52787((class_2596)new class_2838(slot));
            } else {
                InventoryUtil.mc.field_1761.method_2906(InventoryUtil.mc.field_1724.field_7512.field_7763, slot, selectedSlot, class_1713.field_7791, (class_1657)InventoryUtil.mc.field_1724);
                InventoryUtil.mc.field_1724.method_31548().method_7381();
            }
        }
    }

    public static void switchToSlot(int slot) {
        InventoryUtil.mc.field_1724.method_31548().field_7545 = slot;
        mc.method_1562().method_52787((class_2596)new class_2868(slot));
    }

    public static int findItem(class_1792 input) {
        for (int i = 0; i < 9; ++i) {
            class_1792 item = InventoryUtil.mc.field_1724.method_31548().method_5438(i).method_7909();
            if (class_1792.method_7880((class_1792)item) != class_1792.method_7880((class_1792)input)) continue;
            return i;
        }
        return -1;
    }

    public static int getFood() {
        for (int i = 0; i < 9; ++i) {
            if (!InventoryUtil.mc.field_1724.method_31548().method_5438(i).method_57353().method_57832(class_9334.field_50075)) continue;
            return i;
        }
        return -1;
    }

    public static int getPotionCount(class_1291 targetEffect) {
        int count = 0;
        for (int i = 35; i >= 0; --i) {
            class_1799 itemStack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (class_1792.method_7880((class_1792)itemStack.method_7909()) != class_1792.method_7880((class_1792)class_1802.field_8436)) continue;
            class_1844 potionContentsComponent = (class_1844)itemStack.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
            for (class_1293 effect : potionContentsComponent.method_57397()) {
                if (effect.method_5579().comp_349() != targetEffect) continue;
                count += itemStack.method_7947();
            }
        }
        return count;
    }

    public static int getItemCount(Class<?> clazz) {
        int count = 0;
        for (Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!(entry.getValue().method_7909() instanceof class_1747) || !clazz.isInstance(((class_1747)entry.getValue().method_7909()).method_7711())) continue;
            count += entry.getValue().method_7947();
        }
        return count;
    }

    public static int getItemCount(class_1792 item) {
        int count = 0;
        for (Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().method_7909() != item) continue;
            count += entry.getValue().method_7947();
        }
        if (InventoryUtil.mc.field_1724.method_6079().method_7909() == item) {
            count += InventoryUtil.mc.field_1724.method_6079().method_7947();
        }
        return count;
    }

    public static int findClass(Class<?> clazz) {
        for (int i = 0; i < 9; ++i) {
            class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (stack == class_1799.field_8037) continue;
            if (clazz.isInstance(stack.method_7909())) {
                return i;
            }
            if (!(stack.method_7909() instanceof class_1747) || !clazz.isInstance(((class_1747)stack.method_7909()).method_7711())) continue;
            return i;
        }
        return -1;
    }

    public static int findClassInventorySlot(Class<?> clazz) {
        if (AntiCheat.INSTANCE.priorHotbar.getValue()) {
            for (int i = 0; i < 36; ++i) {
                class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
                if (stack == class_1799.field_8037) continue;
                if (clazz.isInstance(stack.method_7909())) {
                    return i < 9 ? i + 36 : i;
                }
                if (!(stack.method_7909() instanceof class_1747) || !clazz.isInstance(((class_1747)stack.method_7909()).method_7711())) continue;
                return i < 9 ? i + 36 : i;
            }
        } else {
            for (int ix = 35; ix >= 0; --ix) {
                class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(ix);
                if (stack == class_1799.field_8037) continue;
                if (clazz.isInstance(stack.method_7909())) {
                    return ix < 9 ? ix + 36 : ix;
                }
                if (!(stack.method_7909() instanceof class_1747) || !clazz.isInstance(((class_1747)stack.method_7909()).method_7711())) continue;
                return ix < 9 ? ix + 36 : ix;
            }
        }
        return -1;
    }

    public static int findBlock(class_2248 blockIn) {
        for (int i = 0; i < 9; ++i) {
            class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (stack == class_1799.field_8037 || !(stack.method_7909() instanceof class_1747) || ((class_1747)stack.method_7909()).method_7711() != blockIn) continue;
            return i;
        }
        return -1;
    }

    public static int findUnBlock() {
        for (int i = 0; i < 9; ++i) {
            class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7909() instanceof class_1747) continue;
            return i;
        }
        return -1;
    }

    public static int findBlock() {
        for (int i = 0; i < 9; ++i) {
            class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (!(stack.method_7909() instanceof class_1747) || BlockUtil.isClickable(class_2248.method_9503((class_1792)stack.method_7909())) || ((class_1747)stack.method_7909()).method_7711() == class_2246.field_10343) continue;
            return i;
        }
        return -1;
    }

    public static int findBlockInventorySlot(class_2248 block) {
        return InventoryUtil.findItemInventorySlot(block.method_8389());
    }

    public static int findItemInventorySlot(class_1792 item) {
        if (AntiCheat.INSTANCE.priorHotbar.getValue()) {
            return InventoryUtil.findItemInventorySlotFromZero(item.method_8389());
        }
        for (int i = 35; i >= 0; --i) {
            class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7909() != item) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static int findItemInventorySlotFromZero(class_1792 item) {
        for (int i = 0; i < 36; ++i) {
            class_1799 stack = InventoryUtil.mc.field_1724.method_31548().method_5438(i);
            if (stack.method_7909() != item) continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static Map<Integer, class_1799> getInventoryAndHotbarSlots() {
        HashMap<Integer, class_1799> fullInventorySlots = new HashMap<Integer, class_1799>();
        for (int current = 0; current <= 35; ++current) {
            fullInventorySlots.put(current, InventoryUtil.mc.field_1724.method_31548().method_5438(current));
        }
        return fullInventorySlots;
    }

    public static boolean silentSwapEquipChestplate() {
        if (!InventoryUtil.mc.field_1724.method_6118(class_1304.field_6174).method_7909().equals(class_1802.field_22028) && !InventoryUtil.mc.field_1724.method_6118(class_1304.field_6174).method_7909().equals(class_1802.field_8058)) {
            int hotbarChestplateSlot = InventoryUtil.findItem(class_1802.field_22028);
            if (hotbarChestplateSlot == -1) {
                hotbarChestplateSlot = InventoryUtil.findItem(class_1802.field_8058);
            }
            if (hotbarChestplateSlot != -1) {
                InventoryUtil.mc.field_1761.method_2906(InventoryUtil.mc.field_1724.field_7512.field_7763, 6, hotbarChestplateSlot, class_1713.field_7791, (class_1657)InventoryUtil.mc.field_1724);
                EntityUtil.syncInventory();
                return true;
            }
            int inventorySlot = InventoryUtil.findItemInventorySlot(class_1802.field_22028);
            if (inventorySlot == -1) {
                inventorySlot = InventoryUtil.findItemInventorySlot(class_1802.field_8058);
            }
            if (inventorySlot == -1) {
                return false;
            }
            InventoryUtil.mc.field_1761.method_2906(InventoryUtil.mc.field_1724.field_7512.field_7763, inventorySlot, 0, class_1713.field_7790, (class_1657)InventoryUtil.mc.field_1724);
            InventoryUtil.mc.field_1761.method_2906(InventoryUtil.mc.field_1724.field_7512.field_7763, 6, 0, class_1713.field_7790, (class_1657)InventoryUtil.mc.field_1724);
            InventoryUtil.mc.field_1761.method_2906(InventoryUtil.mc.field_1724.field_7512.field_7763, inventorySlot, 0, class_1713.field_7790, (class_1657)InventoryUtil.mc.field_1724);
            EntityUtil.syncInventory();
            return true;
        }
        return false;
    }

    public static boolean silentSwapEquipElytra() {
        if (InventoryUtil.mc.field_1724.method_6118(class_1304.field_6174).method_7909().equals(class_1802.field_8833)) {
            return false;
        }
        int inventorySlot = InventoryUtil.findItem(class_1802.field_8833);
        if (inventorySlot != -1) {
            InventoryUtil.mc.field_1761.method_2906(InventoryUtil.mc.field_1724.field_7512.field_7763, 6, inventorySlot, class_1713.field_7791, (class_1657)InventoryUtil.mc.field_1724);
            EntityUtil.syncInventory();
            return true;
        }
        inventorySlot = InventoryUtil.findItemInventorySlot(class_1802.field_8833);
        if (inventorySlot == -1) {
            return false;
        }
        InventoryUtil.mc.field_1761.method_2906(InventoryUtil.mc.field_1724.field_7512.field_7763, inventorySlot, 0, class_1713.field_7790, (class_1657)InventoryUtil.mc.field_1724);
        InventoryUtil.mc.field_1761.method_2906(InventoryUtil.mc.field_1724.field_7512.field_7763, 6, 0, class_1713.field_7790, (class_1657)InventoryUtil.mc.field_1724);
        InventoryUtil.mc.field_1761.method_2906(InventoryUtil.mc.field_1724.field_7512.field_7763, inventorySlot, 0, class_1713.field_7790, (class_1657)InventoryUtil.mc.field_1724);
        EntityUtil.syncInventory();
        return true;
    }
}

