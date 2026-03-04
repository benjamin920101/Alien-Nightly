/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gl.Framebuffer
 *  net.minecraft.client.gl.PostEffectProcessor
 *  net.minecraft.client.gl.PostEffectPass
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.api.interfaces.IShaderEffectHook;
import dev.luminous.asm.accessors.IPostProcessShader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.PostEffectPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_279.class})
public class MixinShaderEffect
implements IShaderEffectHook {
    @Unique
    private final List<String> fakedBufferNames = new ArrayList<String>();
    @Shadow
    @Final
    private Map<String, class_276> field_1495;
    @Shadow
    @Final
    private List<class_283> field_1497;

    @Override
    public void alienClient$addHook(String name, class_276 buffer) {
        class_276 previousFramebuffer = this.field_1495.get(name);
        if (previousFramebuffer != buffer) {
            if (previousFramebuffer != null) {
                for (class_283 pass : this.field_1497) {
                    if (pass.field_1536 == previousFramebuffer) {
                        ((IPostProcessShader)pass).setInput(buffer);
                    }
                    if (pass.field_1538 != previousFramebuffer) continue;
                    ((IPostProcessShader)pass).setOutput(buffer);
                }
                this.field_1495.remove(name);
                this.fakedBufferNames.remove(name);
            }
            this.field_1495.put(name, buffer);
            this.fakedBufferNames.add(name);
        }
    }

    @Inject(method={"close"}, at={@At(value="HEAD")})
    void deleteFakeBuffersHook(CallbackInfo ci) {
        for (String fakedBufferName : this.fakedBufferNames) {
            this.field_1495.remove(fakedBufferName);
        }
    }
}

