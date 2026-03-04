/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  com.llamalad7.mixinextras.sugar.ref.LocalIntRef
 *  net.minecraft.client.gui.hud.PlayerListHud
 *  net.minecraft.client.network.PlayerListEntry
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.impl.misc.ExtraTab;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_355.class})
public abstract class MixinPlayerListHud {
    @Final
    @Shadow
    private static Comparator<class_640> field_2156;

    @Shadow
    protected abstract List<class_640> method_48213();

    @Inject(method={"method_48213"}, at={@At(value="HEAD")}, cancellable=true)
    private void collectPlayerEntriesHook(CallbackInfoReturnable<List<class_640>> cir) {
        if (ExtraTab.INSTANCE.isOn()) {
            cir.setReturnValue(Wrapper.mc.field_1724.field_3944.method_45732().stream().sorted(field_2156).limit(ExtraTab.INSTANCE.size.getValueInt()).toList());
        }
    }

    @Inject(method={"method_1919"}, at={@At(value="INVOKE", target="Ljava/lang/Math;min(II)I", shift=At.Shift.BEFORE)})
    private void hookRender(CallbackInfo ci, @Local(ordinal=5) LocalIntRef o, @Local(ordinal=6) LocalIntRef p) {
        int totalPlayers = this.method_48213().size();
        int newP = 1;
        int newO = (totalPlayers + newP - 1) / newP;
        while (newO > ExtraTab.INSTANCE.columns.getValueInt()) {
            newO = (totalPlayers + ++newP - 1) / newP;
        }
        o.set(newO);
        p.set(newP);
    }
}

