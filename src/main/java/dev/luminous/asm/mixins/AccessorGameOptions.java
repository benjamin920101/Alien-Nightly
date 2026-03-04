/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerModelPart
 *  net.minecraft.client.option.GameOptions
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.mixins;

import java.util.Set;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_315.class})
public interface AccessorGameOptions {
    @Accessor(value="field_1892")
    @Mutable
    public Set<class_1664> getPlayerModelParts();
}

