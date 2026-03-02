/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_279
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_761
 *  org.lwjgl.opengl.GL11
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.core.impl.ShaderManager;
import dev.luminous.mod.modules.impl.player.Freecam;
import dev.luminous.mod.modules.impl.render.Chams;
import dev.luminous.mod.modules.impl.render.ShaderModule;
import net.minecraft.class_1297;
import net.minecraft.class_279;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_761;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_761.class})
public abstract class MixinWorldRenderer {
    @Unique
    boolean renderingChams = false;
    @Unique
    boolean renderingEntity = false;

    @Inject(method={"method_22977"}, at={@At(value="HEAD")})
    private void injectChamsForEntity(class_1297 entity, double cameraX, double cameraY, double cameraZ, float tickDelta, class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
        if (Chams.INSTANCE.isOn() && Chams.INSTANCE.throughWall.getValue()) {
            if (Chams.INSTANCE.chams(entity)) {
                if (this.renderingEntity) {
                    Wrapper.mc.method_22940().method_23000().method_22993();
                    this.renderingEntity = false;
                }
                GL11.glEnable((int)32823);
                GL11.glPolygonOffset((float)1.0f, (float)-1000000.0f);
                this.renderingChams = true;
            } else {
                this.renderingEntity = true;
            }
        }
    }

    @Inject(method={"method_22977"}, at={@At(value="RETURN")})
    private void injectChamsForEntityPost(class_1297 entity, double cameraX, double cameraY, double cameraZ, float tickDelta, class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
        if (Chams.INSTANCE.isOn() && Chams.INSTANCE.throughWall.getValue() && this.renderingChams) {
            Wrapper.mc.method_22940().method_23000().method_22993();
            GL11.glPolygonOffset((float)1.0f, (float)1000000.0f);
            GL11.glDisable((int)32823);
            this.renderingChams = false;
        }
    }

    @Redirect(method={"method_22710"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_279;method_1258(F)V", ordinal=0), require=0)
    void replaceShaderHook(class_279 instance, float tickDelta) {
        ShaderManager.Shader shaders = ShaderModule.INSTANCE.mode.getValue();
        if (ShaderModule.INSTANCE.isOn() && Wrapper.mc.field_1687 != null) {
            Alien.SHADER.setupShader(shaders, Alien.SHADER.getShaderOutline(shaders));
        } else {
            instance.method_1258(tickDelta);
        }
    }

    @ModifyArg(method={"method_22710"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_761;method_3273(Lnet/minecraft/class_4184;Lnet/minecraft/class_4604;ZZ)V"), index=3)
    private boolean renderSetupTerrainModifyArg(boolean spectator) {
        return Freecam.INSTANCE.isOn() || spectator;
    }
}

