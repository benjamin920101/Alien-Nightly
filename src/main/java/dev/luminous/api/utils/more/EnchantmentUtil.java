/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2IntMap$Entry
 *  net.minecraft.item.ItemStack
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.enchantment.Enchantments
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.component.type.ItemEnchantmentsComponent
 *  net.minecraft.component.DataComponentTypes
 */
package dev.luminous.api.utils.more;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.DataComponentTypes;

public class EnchantmentUtil {
    public static int getLevel(class_1799 stack, class_5321<class_1887> enchantmentRegistryKey) {
        if (!stack.method_57353().method_57832(class_9334.field_49633)) {
            return 0;
        }
        for (Object2IntMap.Entry e : ((class_9304)stack.method_57353().method_57829(class_9334.field_49633)).method_57539()) {
            if (!((class_6880)e.getKey()).method_40230().isPresent() || !((class_5321)((class_6880)e.getKey()).method_40230().get()).equals(enchantmentRegistryKey)) continue;
            return e.getIntValue();
        }
        return 0;
    }

    public static boolean isFakeEnchant2b2t(class_1799 itemStack) {
        Set enchants = class_1890.method_57532((class_1799)itemStack).method_57539();
        if (enchants.size() > 1) {
            return false;
        }
        for (Object2IntMap.Entry e : enchants) {
            class_6880 enchantment = (class_6880)e.getKey();
            int lvl = e.getIntValue();
            if (lvl != 0 || !enchantment.method_40230().isPresent() || enchantment.method_40230().get() != class_1893.field_9111) continue;
            return true;
        }
        return false;
    }
}

