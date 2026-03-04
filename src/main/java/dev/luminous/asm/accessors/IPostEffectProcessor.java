/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gl.PostEffectProcessor
 *  net.minecraft.client.gl.PostEffectPass
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import java.util.List;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.PostEffectPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_279.class})
public interface IPostEffectProcessor {
    @Accessor(value="field_1497")
    public List<class_283> getPasses();
}

