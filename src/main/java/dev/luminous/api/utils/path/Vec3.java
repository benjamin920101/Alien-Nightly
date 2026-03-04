/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package dev.luminous.api.utils.path;

import net.minecraft.util.math.Vec3d;

public record Vec3(double x, double y, double z) {
    public Vec3 addVector(double x, double y, double z) {
        return new Vec3(this.x + x, this.y + y, this.z + z);
    }

    public Vec3 floor() {
        return new Vec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public double squareDistanceTo(Vec3 v) {
        return Math.pow(v.x - this.x, 2.0) + Math.pow(v.y - this.y, 2.0) + Math.pow(v.z - this.z, 2.0);
    }

    public Vec3 add(Vec3 v) {
        return this.addVector(v.x(), v.y(), v.z());
    }

    public class_243 mc() {
        return new class_243(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "[" + this.x + ";" + this.y + ";" + this.z + "]";
    }

    @Override
    public boolean equals(Object obj) {
        Vec3 vec;
        return obj instanceof Vec3 && this.x == (vec = (Vec3)obj).x() && this.y == vec.y() && this.z == vec.z();
    }
}

