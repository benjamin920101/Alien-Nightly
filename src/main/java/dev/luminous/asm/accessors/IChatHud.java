/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.hud.ChatHudLine$Visible
 *  net.minecraft.client.gui.hud.ChatHud
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package dev.luminous.asm.accessors;

import java.util.List;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_338.class})
public interface IChatHud {
    @Mutable
    @Accessor(value="field_2064")
    public void setVisibleMessages(List<class_303.class_7590> var1);

    @Mutable
    @Accessor(value="field_2061")
    public void setMessages(List<class_303.class_7590> var1);
}

