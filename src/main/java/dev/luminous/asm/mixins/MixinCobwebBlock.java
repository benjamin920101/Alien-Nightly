/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.block.CobwebBlock
 *  net.minecraft.block.BlockState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.impl.movement.FastWeb;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.CobwebBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_2560.class})
public class MixinCobwebBlock {
    @Inject(at={@At(value="HEAD")}, method={"method_9548"}, cancellable=true)
    private void onGetVelocityMultiplier(class_2680 state, class_1937 world, class_2338 pos, class_1297 entity, CallbackInfo ci) {
        if (FastWeb.INSTANCE.isOn() && (Wrapper.mc.field_1690.field_1832.method_1434() || !FastWeb.INSTANCE.onlySneak.getValue())) {
            if (FastWeb.INSTANCE.mode.is(FastWeb.Mode.Ignore)) {
                ci.cancel();
                entity.method_38785();
            } else if (FastWeb.INSTANCE.mode.is(FastWeb.Mode.Custom)) {
                ci.cancel();
                entity.method_5844(state, new class_243(FastWeb.INSTANCE.xZSlow.getValue() / 100.0, FastWeb.INSTANCE.ySlow.getValue() / 100.0, FastWeb.INSTANCE.xZSlow.getValue() / 100.0));
            }
        }
    }
}

