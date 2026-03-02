/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2338
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
package dev.luminous.api.utils.world;

import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public class BlockPosX
extends class_2338 {
    public BlockPosX(double x, double y, double z) {
        super(class_3532.method_15357((double)x), class_3532.method_15357((double)y), class_3532.method_15357((double)z));
    }

    public BlockPosX(double x, double y, double z, boolean fix) {
        this(x, y + (fix ? 0.3 : 0.0), z);
    }

    public BlockPosX(class_243 vec3d) {
        this(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350);
    }

    public BlockPosX(class_243 vec3d, boolean fix) {
        this(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, fix);
    }
}

