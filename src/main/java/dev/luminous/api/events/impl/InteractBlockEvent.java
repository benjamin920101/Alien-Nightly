/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.events.Event;
import net.minecraft.util.Hand;

public class InteractBlockEvent
extends Event {
    private static final InteractBlockEvent INSTANCE = new InteractBlockEvent();
    public class_1268 hand;

    private InteractBlockEvent() {
    }

    public static InteractBlockEvent getPre(class_1268 hand) {
        InteractBlockEvent.INSTANCE.hand = hand;
        InteractBlockEvent.INSTANCE.stage = Event.Stage.Pre;
        INSTANCE.setCancelled(false);
        return INSTANCE;
    }

    public static InteractBlockEvent getPost(class_1268 hand) {
        InteractBlockEvent.INSTANCE.hand = hand;
        InteractBlockEvent.INSTANCE.stage = Event.Stage.Post;
        return INSTANCE;
    }
}

