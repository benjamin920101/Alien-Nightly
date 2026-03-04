/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screen.Screen
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.client.gui.screen.Screen;

public class OpenScreenEvent
extends Event {
    private static final OpenScreenEvent INSTANCE = new OpenScreenEvent();
    public class_437 screen;

    private OpenScreenEvent() {
    }

    public static OpenScreenEvent get(class_437 screen) {
        OpenScreenEvent.INSTANCE.screen = screen;
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }
}

