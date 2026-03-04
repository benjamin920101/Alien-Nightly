/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Box
 *  net.minecraft.client.util.math.MatrixStack
 */
package dev.luminous.api.events.impl;

import dev.luminous.api.utils.render.Render3DUtil;
import java.awt.Color;
import net.minecraft.util.math.Box;
import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent {
    private static final Render3DEvent INSTANCE = new Render3DEvent();
    public class_4587 matrixStack;
    public float tickDelta;

    public static Render3DEvent get(class_4587 matrixStack, float tickDelta) {
        Render3DEvent.INSTANCE.matrixStack = matrixStack;
        Render3DEvent.INSTANCE.tickDelta = tickDelta;
        return INSTANCE;
    }

    public void drawBox(class_238 box, Color color) {
        Render3DUtil.drawBox(this.matrixStack, box, color);
    }

    public void drawFill(class_238 box, Color color) {
        Render3DUtil.drawFill(this.matrixStack, box, color);
    }
}

