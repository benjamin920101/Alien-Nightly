/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.Entity$RemovalReason
 *  net.minecraft.world.World
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.dimension.DimensionType
 *  net.minecraft.util.profiler.Profiler
 *  net.minecraft.world.MutableWorldProperties
 *  net.minecraft.client.render.DimensionEffects
 *  net.minecraft.client.render.DimensionEffects$Overworld
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.registry.DynamicRegistryManager
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.registry.entry.RegistryEntry
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.EntitySpawnEvent;
import dev.luminous.api.events.impl.EntitySpawnedEvent;
import dev.luminous.api.events.impl.RemoveEntityEvent;
import dev.luminous.api.events.impl.TickEntityEvent;
import dev.luminous.mod.modules.impl.render.Ambience;
import dev.luminous.mod.modules.impl.render.NoRender;
import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_638.class})
public abstract class MixinClientWorld
extends class_1937 {
    @Unique
    private final class_5294 overworld = new class_5294.class_5297();

    protected MixinClientWorld(class_5269 properties, class_5321<class_1937> registryRef, class_5455 registryManager, class_6880<class_2874> dimensionEntry, Supplier<class_3695> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method={"method_18646"}, at={@At(value="HEAD")}, cancellable=true)
    public void onTickEntity(class_1297 entity, CallbackInfo ci) {
        TickEntityEvent event = TickEntityEvent.get(entity);
        Alien.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_53875"}, at={@At(value="HEAD")}, cancellable=true)
    public void onAddEntity(class_1297 entity, CallbackInfo ci) {
        EntitySpawnEvent event = EntitySpawnEvent.get(entity);
        Alien.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_2945"}, at={@At(value="HEAD")})
    private void hookRemoveEntity(int entityId, class_1297.class_5529 removalReason, CallbackInfo ci) {
        class_1297 entity = this.method_8469(entityId);
        if (entity != null) {
            RemoveEntityEvent removeEntityEvent = RemoveEntityEvent.get(entity, removalReason);
            Alien.EVENT_BUS.post(removeEntityEvent);
        }
    }

    @Inject(method={"method_53875"}, at={@At(value="TAIL")})
    public void onAddEntityTail(class_1297 entity, CallbackInfo ci) {
        EntitySpawnedEvent event = EntitySpawnedEvent.get(entity);
        Alien.EVENT_BUS.post(event);
    }

    @Inject(method={"method_23777"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetSkyColor(class_243 cameraPos, float tickDelta, CallbackInfoReturnable<class_243> info) {
        if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.sky.booleanValue) {
            Color sky = Ambience.INSTANCE.sky.getValue();
            info.setReturnValue((Object)new class_243((double)sky.getRed() / 255.0, (double)sky.getGreen() / 255.0, (double)sky.getBlue() / 255.0));
        }
    }

    @Inject(method={"method_23785"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookGetCloudsColor(float tickDelta, CallbackInfoReturnable<class_243> cir) {
        if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.cloud.booleanValue) {
            Color sky = Ambience.INSTANCE.cloud.getValue();
            cir.setReturnValue((Object)new class_243((double)sky.getRed() / 255.0, (double)sky.getGreen() / 255.0, (double)sky.getBlue() / 255.0));
        }
    }

    @Inject(method={"method_28103"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetSkyProperties(CallbackInfoReturnable<class_5294> info) {
        if (Ambience.INSTANCE.isOn() && Ambience.INSTANCE.forceOverworld.getValue()) {
            info.setReturnValue((Object)this.overworld);
        }
    }

    public float method_8430(float delta) {
        return NoRender.INSTANCE.isOn() && NoRender.INSTANCE.weather.getValue() ? 0.0f : super.method_8430(delta);
    }

    public float method_8478(float delta) {
        return NoRender.INSTANCE.isOn() && NoRender.INSTANCE.weather.getValue() ? 0.0f : super.method_8478(delta);
    }
}

