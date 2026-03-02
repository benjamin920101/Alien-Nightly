/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2680
 *  net.minecraft.class_2818
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.combat.SelfTrap;
import dev.luminous.mod.modules.impl.player.InteractTweaks;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1937.class})
public abstract class MixinWorld {
    @Inject(method={"method_8320"}, at={@At(value="HEAD")}, cancellable=true)
    public void blockStateHook(class_2338 pos, CallbackInfoReturnable<class_2680> cir) {
        if (Wrapper.mc.field_1687 != null && Wrapper.mc.field_1687.method_24794(pos)) {
            if (SelfTrap.airList.contains(pos)) {
                cir.setReturnValue((Object)class_2246.field_10124.method_9564());
                return;
            }
            if (!ClientSetting.INSTANCE.mioCompatible.getValue()) {
                class_2818 worldChunkx;
                class_2680 tempStatex;
                boolean terrainIgnore = CombatUtil.terrainIgnore;
                class_2338 modifyPos = CombatUtil.modifyPos;
                class_2680 modifyBlockState = CombatUtil.modifyBlockState;
                if (terrainIgnore || modifyPos != null) {
                    class_2818 worldChunk = Wrapper.mc.field_1687.method_8497(pos.method_10263() >> 4, pos.method_10260() >> 4);
                    class_2680 tempState = worldChunk.method_8320(pos);
                    if (modifyPos != null && modifyBlockState != null && pos.equals((Object)modifyPos)) {
                        cir.setReturnValue((Object)modifyBlockState);
                        return;
                    }
                    if (terrainIgnore) {
                        if (Alien.HOLE.isHard(tempState.method_26204())) {
                            return;
                        }
                        cir.setReturnValue((Object)class_2246.field_10124.method_9564());
                    }
                } else if (InteractTweaks.INSTANCE.isActive && (tempStatex = (worldChunkx = Wrapper.mc.field_1687.method_8497(pos.method_10263() >> 4, pos.method_10260() >> 4)).method_8320(pos)).method_26204() == class_2246.field_9987) {
                    cir.setReturnValue((Object)class_2246.field_10124.method_9564());
                }
            }
        }
    }
}

