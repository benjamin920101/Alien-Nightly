/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.client.texture.Sprite
 *  net.minecraft.client.render.model.BakedModel
 *  net.minecraft.util.math.Direction
 *  net.minecraft.block.BlockState
 *  net.minecraft.util.math.random.Random
 *  net.minecraft.client.render.model.BakedQuad
 *  net.minecraft.client.render.model.json.ModelOverrideList
 *  net.minecraft.client.render.model.json.ModelTransformation
 *  org.jetbrains.annotations.Nullable
 */
package dev.luminous.api.utils.render;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.Direction;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.random.Random;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import org.jetbrains.annotations.Nullable;

public class SimpleItemModel
implements class_1087 {
    private class_1087 flattenedItem;
    private final List<class_777> nullQuadList = new ObjectArrayList();

    public void setItem(class_1087 model) {
        this.flattenedItem = model;
    }

    private boolean isCorrectDirectionForType(class_2350 direction) {
        return direction == class_2350.field_11035;
    }

    public List<class_777> method_4707(@Nullable class_2680 state, @Nullable class_2350 face, class_5819 random) {
        if (face != null) {
            return this.isCorrectDirectionForType(face) ? this.flattenedItem.method_4707(state, face, random) : ImmutableList.of();
        }
        this.nullQuadList.clear();
        for (class_777 quad : this.flattenedItem.method_4707(state, null, random)) {
            if (!this.isCorrectDirectionForType(quad.method_3358())) continue;
            this.nullQuadList.add(quad);
        }
        return this.nullQuadList;
    }

    public boolean method_4708() {
        return this.flattenedItem.method_4708();
    }

    public boolean method_4712() {
        return this.flattenedItem.method_4712();
    }

    public boolean method_24304() {
        return this.flattenedItem.method_24304();
    }

    public boolean method_4713() {
        return this.flattenedItem.method_4713();
    }

    public class_1058 method_4711() {
        return this.flattenedItem.method_4711();
    }

    public class_809 method_4709() {
        return this.flattenedItem.method_4709();
    }

    public class_806 method_4710() {
        return this.flattenedItem.method_4710();
    }
}

