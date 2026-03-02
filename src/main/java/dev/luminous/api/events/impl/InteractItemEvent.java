/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.class_1268;

public class InteractItemEvent
extends Event {
    private static final InteractItemEvent INSTANCE = new InteractItemEvent();
    public class_1268 hand;

    private InteractItemEvent() {
    }

    public static InteractItemEvent getPre(class_1268 hand) {
        InteractItemEvent.INSTANCE.hand = hand;
        InteractItemEvent.INSTANCE.stage = Event.Stage.Pre;
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }

    public static InteractItemEvent getPost(class_1268 hand) {
        InteractItemEvent.INSTANCE.hand = hand;
        InteractItemEvent.INSTANCE.stage = Event.Stage.Post;
        return INSTANCE;
    }
}

