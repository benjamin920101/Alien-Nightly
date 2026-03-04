/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap
 *  net.minecraft.util.ActionResult
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.world.World
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockState
 *  net.minecraft.state.property.Property
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.block.AbstractBlock$AbstractBlockState
 *  org.spongepowered.asm.mixin.Mixin
 */
package dev.luminous.asm.mixins;

import com.mojang.serialization.MapCodec;
import dev.luminous.Alien;
import dev.luminous.api.events.impl.BlockActivateEvent;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={class_2680.class})
public abstract class MixinBlockState
extends class_4970.class_4971 {
    public MixinBlockState(class_2248 block, Reference2ObjectArrayMap<class_2769<?>, Comparable<?>> propertyMap, MapCodec<class_2680> mapCodec) {
        super(block, propertyMap, mapCodec);
    }

    public class_1269 method_55781(class_1937 world, class_1657 player, class_3965 hit) {
        Alien.EVENT_BUS.post(BlockActivateEvent.get((class_2680)class_2680.class.cast((Object)this)));
        return super.method_55781(world, player, hit);
    }
}

