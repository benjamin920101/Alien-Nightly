/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.item.ItemStack
 *  net.minecraft.client.util.math.MatrixStack
 */
package dev.luminous.api.events.impl;

import net.minecraft.util.Hand;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.math.MatrixStack;

public class HeldItemRendererEvent {
    private static final HeldItemRendererEvent INSTANCE = new HeldItemRendererEvent();
    private class_1268 hand;
    private class_1799 item;
    private float ep;
    private class_4587 stack;

    private HeldItemRendererEvent() {
    }

    public static HeldItemRendererEvent get(class_1268 hand, class_1799 item, float equipProgress, class_4587 stack) {
        HeldItemRendererEvent.INSTANCE.hand = hand;
        HeldItemRendererEvent.INSTANCE.item = item;
        HeldItemRendererEvent.INSTANCE.ep = equipProgress;
        HeldItemRendererEvent.INSTANCE.stack = stack;
        return INSTANCE;
    }

    public class_1268 getHand() {
        return this.hand;
    }

    public class_1799 getItem() {
        return this.item;
    }

    public float getEp() {
        return this.ep;
    }

    public class_4587 getStack() {
        return this.stack;
    }
}

