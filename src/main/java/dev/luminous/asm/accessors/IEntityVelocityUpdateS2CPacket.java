/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_2743.class})
public interface IEntityVelocityUpdateS2CPacket {
    @Mutable
    @Accessor(value="field_12563")
    public void setX(int var1);

    @Mutable
    @Accessor(value="field_12562")
    public void setY(int var1);

    @Mutable
    @Accessor(value="field_12561")
    public void setZ(int var1);

    @Accessor(value="field_12563")
    public int getX();

    @Accessor(value="field_12562")
    public int getY();

    @Accessor(value="field_12561")
    public int getZ();
}

