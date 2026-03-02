/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1309
 *  net.minecraft.class_1671
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package dev.luminous.asm.accessors;

import net.minecraft.class_1309;
import net.minecraft.class_1671;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={class_1671.class})
public interface IFireworkRocketEntity {
    @Accessor(value="field_7616")
    public class_1309 getShooter();

    @Invoker(value="method_7476")
    public boolean hookWasShotByEntity();

    @Invoker(value="method_16830")
    public void hookExplodeAndRemove();
}

