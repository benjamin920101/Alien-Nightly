/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item$TooltipContext
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.tooltip.TooltipType
 *  net.minecraft.block.ShulkerBoxBlock
 *  net.minecraft.text.Text
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.misc.ShulkerViewer;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_2480.class})
public class MixinShulkerBoxBlock {
    @Inject(method={"method_9568"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAppendTooltip(class_1799 stack, class_1792.class_9635 context, List<class_2561> tooltip, class_1836 options, CallbackInfo ci) {
        if (ShulkerViewer.INSTANCE.isOn() && ShulkerViewer.INSTANCE.toolTips.getValue()) {
            ci.cancel();
        }
    }
}

