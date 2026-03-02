/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_636
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_636.class})
public interface IClientPlayerInteractionManager {
    @Accessor(value="field_3721")
    public int getLastSelectedSlot();

    @Accessor(value="field_3721")
    public void setLastSelectedSlot(int var1);
}

