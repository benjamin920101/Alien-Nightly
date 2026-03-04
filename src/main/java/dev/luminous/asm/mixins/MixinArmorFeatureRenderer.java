/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.item.ArmorMaterial
 *  net.minecraft.util.Identifier
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.VertexConsumerProvider
 *  net.minecraft.client.render.entity.model.BipedEntityModel
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.item.trim.ArmorTrim
 *  net.minecraft.client.render.entity.feature.ArmorFeatureRenderer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.mod.modules.impl.render.NoRender;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_970.class})
public class MixinArmorFeatureRenderer<T extends class_1309, A extends class_572<T>> {
    @Inject(method={"method_23192"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderArmorParts(class_4587 matrices, class_4597 vertexConsumers, int light, A model, int i, class_2960 identifier, CallbackInfo ci) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.armorParts.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_48482"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderArmorTrim(class_6880<class_1741> armorMaterial, class_4587 matrices, class_4597 vertexConsumers, int light, class_8053 trim, A model, boolean leggings, CallbackInfo ci) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.armorTrim.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_52224"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderArmorGlint(class_4587 matrices, class_4597 vertexConsumers, int light, A model, CallbackInfo ci) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.armorGlint.getValue()) {
            ci.cancel();
        }
    }
}

