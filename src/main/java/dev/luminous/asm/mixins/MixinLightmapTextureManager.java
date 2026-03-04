/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.texture.NativeImage
 *  net.minecraft.client.texture.NativeImageBackedTexture
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.world.dimension.DimensionType
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.client.network.ClientPlayerEntity
 *  net.minecraft.client.render.GameRenderer
 *  net.minecraft.client.render.LightmapTextureManager
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.Ambience;
import dev.luminous.mod.modules.impl.render.NoRender;
import java.awt.Color;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_765.class})
public class MixinLightmapTextureManager {
    @Final
    @Shadow
    private class_1043 field_4138;
    @Final
    @Shadow
    private class_1011 field_4133;
    @Shadow
    private boolean field_4135;
    @Shadow
    private float field_21528;
    @Final
    @Shadow
    private class_757 field_4134;
    @Final
    @Shadow
    private class_310 field_4137;

    @Shadow
    private static void method_46557(Vector3f vec) {
        vec.set(class_3532.method_15363((float)vec.x, (float)0.0f, (float)1.0f), class_3532.method_15363((float)vec.y, (float)0.0f, (float)1.0f), class_3532.method_15363((float)vec.z, (float)0.0f, (float)1.0f));
    }

    @Shadow
    public static float method_23284(class_2874 type, int lightLevel) {
        float f = (float)lightLevel / 15.0f;
        float g = f / (4.0f - 3.0f * f);
        return class_3532.method_16439((float)type.comp_656(), (float)g, (float)1.0f);
    }

    @Redirect(method={"method_3313"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_746;method_6059(Lnet/minecraft/class_6880;)Z", ordinal=0, remap=false), require=0)
    private boolean nightVisionHook(class_746 instance, class_6880<class_1291> registryEntry) {
        return Ambience.INSTANCE.isOn() && Ambience.INSTANCE.fullBright.getValue() ? true : instance.method_6059(registryEntry);
    }

    @Inject(method={"method_3313"}, at={@At(value="HEAD")}, cancellable=true)
    public void updateHook(float delta, CallbackInfo ci) {
        if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.worldColor.booleanValue) {
            ci.cancel();
            if (this.field_4135) {
                this.field_4135 = false;
                this.field_4137.method_16011().method_15396("lightTex");
                class_638 clientWorld = this.field_4137.field_1687;
                if (clientWorld != null) {
                    float f = clientWorld.method_23783(1.0f);
                    float g = clientWorld.method_23789() > 0 ? 1.0f : f * 0.95f + 0.05f;
                    float h = ((Double)this.field_4137.field_1690.method_42472().method_41753()).floatValue();
                    float i = this.method_42597(delta) * h;
                    float j = this.method_42596((class_1309)this.field_4137.field_1724, i, delta) * h;
                    Vector3f vector3f = new Vector3f(f, f, 1.0f).lerp((Vector3fc)new Vector3f(1.0f, 1.0f, 1.0f), 0.35f);
                    float m = this.field_21528 + 1.5f;
                    Vector3f vector3f2 = new Vector3f();
                    for (int n = 0; n < 16; ++n) {
                        for (int o = 0; o < 16; ++o) {
                            float p = MixinLightmapTextureManager.method_23284(clientWorld.method_8597(), n) * g;
                            float q = MixinLightmapTextureManager.method_23284(clientWorld.method_8597(), o) * m;
                            float s = q * ((q * 0.6f + 0.4f) * 0.6f + 0.4f);
                            float t = q * (q * q * 0.6f + 0.4f);
                            vector3f2.set(q, s, t);
                            boolean bl = clientWorld.method_28103().method_28114();
                            if (bl) {
                                vector3f2.lerp((Vector3fc)new Vector3f(0.99f, 1.12f, 1.0f), 0.25f);
                                MixinLightmapTextureManager.method_46557(vector3f2);
                            } else {
                                Vector3f vector3f3 = new Vector3f((Vector3fc)vector3f).mul(p);
                                vector3f2.add((Vector3fc)vector3f3);
                                vector3f2.lerp((Vector3fc)new Vector3f(0.75f, 0.75f, 0.75f), 0.04f);
                                if (this.field_4134.method_3195(delta) > 0.0f) {
                                    float u = this.field_4134.method_3195(delta);
                                    Vector3f vector3f4 = new Vector3f((Vector3fc)vector3f2).mul(0.7f, 0.6f, 0.6f);
                                    vector3f2.lerp((Vector3fc)vector3f4, u);
                                }
                            }
                            float v = Math.max(vector3f2.x(), Math.max(vector3f2.y(), vector3f2.z()));
                            if (v < 1.0f) {
                                this.field_4133.method_4305(o, n, new Color(Ambience.INSTANCE.worldColor.getValue().getBlue(), Ambience.INSTANCE.worldColor.getValue().getGreen(), Ambience.INSTANCE.worldColor.getValue().getRed(), Ambience.INSTANCE.worldColor.getValue().getAlpha()).getRGB());
                                continue;
                            }
                            if (!bl) {
                                if (j > 0.0f) {
                                    vector3f2.add(-j, -j, -j);
                                }
                                MixinLightmapTextureManager.method_46557(vector3f2);
                            }
                            v = ((Double)this.field_4137.field_1690.method_42473().method_41753()).floatValue();
                            Vector3f vector3f5 = new Vector3f(this.method_23795(vector3f2.x), this.method_23795(vector3f2.y), this.method_23795(vector3f2.z));
                            vector3f2.lerp((Vector3fc)vector3f5, Math.max(0.0f, v - i));
                            vector3f2.lerp((Vector3fc)new Vector3f(0.75f, 0.75f, 0.75f), 0.04f);
                            MixinLightmapTextureManager.method_46557(vector3f2);
                            vector3f2.mul(255.0f);
                            int x = (int)vector3f2.x();
                            int y = (int)vector3f2.y();
                            int z = (int)vector3f2.z();
                            this.field_4133.method_4305(o, n, 0xFF000000 | z << 16 | y << 8 | x);
                        }
                    }
                    this.field_4138.method_4524();
                    this.field_4137.method_16011().method_15407();
                }
            }
        }
    }

    @Inject(method={"method_42597(F)F"}, at={@At(value="HEAD")}, cancellable=true)
    private void getDarknessFactor(float tickDelta, CallbackInfoReturnable<Float> info) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.darkness.getValue()) {
            info.setReturnValue((Object)Float.valueOf(0.0f));
        }
    }

    @Shadow
    private float method_23795(float x) {
        float f = 1.0f - x;
        return 1.0f - f * f * f * f;
    }

    @Shadow
    private float method_42597(float delta) {
        class_1293 statusEffectInstance = this.field_4137.field_1724.method_6112(class_1294.field_38092);
        return statusEffectInstance != null ? statusEffectInstance.method_55653((class_1309)this.field_4137.field_1724, delta) : 0.0f;
    }

    @Shadow
    private float method_42596(class_1309 entity, float factor, float delta) {
        float f = 0.45f * factor;
        return Math.max(0.0f, class_3532.method_15362((float)(((float)entity.field_6012 - delta) * (float)Math.PI * 0.025f)) * f);
    }
}

