/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.api.utils.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;

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

