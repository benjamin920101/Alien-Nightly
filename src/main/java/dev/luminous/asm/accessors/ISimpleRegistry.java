/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2370
 *  net.minecraft.class_6880$class_6883
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import java.util.Map;
import net.minecraft.class_2370;
import net.minecraft.class_6880;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2370.class})
public interface ISimpleRegistry<T> {
    @Accessor(value="field_36461")
    public Map<T, class_6880.class_6883<T>> getValueToEntry();
}

