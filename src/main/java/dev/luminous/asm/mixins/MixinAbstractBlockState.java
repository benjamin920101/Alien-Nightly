/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.AbstractBlock$AbstractBlockState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.render.Ambience;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_4970.class_4971.class})
public class MixinAbstractBlockState {
    @Inject(method={"method_26213"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLuminanceHook(CallbackInfoReturnable<Integer> cir) {
        if (!Module.nullCheck() && Ambience.INSTANCE.customLuminance.getValue()) {
            cir.setReturnValue((Object)Ambience.INSTANCE.luminance.getValueInt());
        }
    }
}

