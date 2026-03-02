/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_3414
 *  net.minecraft.class_3419
 *  net.minecraft.class_638
 *  net.minecraft.class_7202
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.luminous.asm.accessors;

import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_638;
import net.minecraft.class_7202;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_638.class})
public interface AccessorClientWorld {
    @Invoker(value="method_43207")
    public void hookPlaySound(double var1, double var3, double var5, class_3414 var7, class_3419 var8, float var9, float var10, boolean var11, long var12);

    @Invoker(value="method_41925")
    public class_7202 hookGetPendingUpdateManager();
}

