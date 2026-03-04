/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.client.render.BackgroundRenderer
 *  net.minecraft.client.render.BackgroundRenderer$FogType
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.mod.modules.impl.render.Ambience;
import dev.luminous.mod.modules.impl.render.NoRender;
import dev.luminous.mod.modules.impl.render.Xray;
import java.awt.Color;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.render.BackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_758.class})
public class MixinBackgroundRenderer {
    @Redirect(method={"method_3210"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_1309;method_6059(Lnet/minecraft/class_6880;)Z", ordinal=0, remap=false), require=0)
    private static boolean nightVisionHook(class_1309 instance, class_6880<class_1291> effect) {
        return Ambience.INSTANCE.isOn() && Ambience.INSTANCE.fullBright.getValue() || instance.method_6059(effect);
    }

    @Inject(method={"method_3211"}, at={@At(value="TAIL")})
    private static void onApplyFog(class_4184 camera, class_758.class_4596 fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo info) {
        if (Ambience.INSTANCE.isOn()) {
            if (Ambience.INSTANCE.fog.booleanValue) {
                RenderSystem.setShaderFogColor((float)((float)Ambience.INSTANCE.fog.getValue().getRed() / 255.0f), (float)((float)Ambience.INSTANCE.fog.getValue().getGreen() / 255.0f), (float)((float)Ambience.INSTANCE.fog.getValue().getBlue() / 255.0f), (float)((float)Ambience.INSTANCE.fog.getValue().getAlpha() / 255.0f));
            }
            if (Ambience.INSTANCE.fogDistance.getValue()) {
                RenderSystem.setShaderFogStart((float)Ambience.INSTANCE.fogStart.getValueFloat());
                RenderSystem.setShaderFogEnd((float)Ambience.INSTANCE.fogEnd.getValueFloat());
            }
        }
        if ((NoRender.INSTANCE.isOn() && NoRender.INSTANCE.fog.getValue() || Xray.INSTANCE.isOn()) && fogType == class_758.class_4596.field_20946) {
            RenderSystem.setShaderFogStart((float)(viewDistance * 4.0f));
            RenderSystem.setShaderFogEnd((float)(viewDistance * 4.25f));
        }
    }

    @Inject(method={"method_3210"}, at={@At(value="HEAD")}, cancellable=true)
    private static void hookRender(class_4184 camera, float tickDelta, class_638 world, int viewDistance, float skyDarkness, CallbackInfo ci) {
        if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.dimensionColor.booleanValue) {
            Color color = Ambience.INSTANCE.dimensionColor.getValue();
            ci.cancel();
            RenderSystem.clearColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)0.0f);
        }
    }

    @Inject(method={"method_42588(Lnet/minecraft/class_1297;F)Lnet/minecraft/class_758$class_7286;"}, at={@At(value="HEAD")}, cancellable=true)
    private static void onGetFogModifier(class_1297 entity, float tickDelta, CallbackInfoReturnable<Object> info) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.blindness.getValue()) {
            info.setReturnValue(null);
        }
    }
}

