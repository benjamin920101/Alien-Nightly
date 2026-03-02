/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_434
 *  net.minecraft.class_435
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.mod.modules.Module;
import net.minecraft.class_434;
import net.minecraft.class_435;

public class NoTerrainScreen
extends Module {
    public NoTerrainScreen() {
        super("NoTerrainScreen", Module.Category.Misc);
        this.setChinese("\u6ca1\u6709\u52a0\u8f7d\u754c\u9762");
    }

    @EventListener
    public void onEvent(ClientTickEvent event) {
        if (!NoTerrainScreen.nullCheck() && (NoTerrainScreen.mc.field_1755 instanceof class_434 || NoTerrainScreen.mc.field_1755 instanceof class_435)) {
            NoTerrainScreen.mc.field_1755 = null;
        }
    }
}

