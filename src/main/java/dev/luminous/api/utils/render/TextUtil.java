/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.client.render.Camera
 *  net.minecraft.client.util.math.MatrixStack
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Vector3f
 *  org.joml.Vector4f
 *  org.lwjgl.opengl.GL11
 */
package dev.luminous.api.utils.render;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.core.impl.FontManager;
import java.awt.Color;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class TextUtil
implements Wrapper {
    public static final Matrix4f lastProjMat = new Matrix4f();
    public static final Matrix4f lastModMat = new Matrix4f();
    public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();

    public static float getWidth(String s) {
        return TextUtil.mc.field_1772.method_1727(s);
    }

    public static float getHeight() {
        return 9.0f;
    }

    public static void drawStringWithScale(class_332 drawContext, String text, float x, float y, int color, float scale) {
        class_4587 matrixStack = drawContext.method_51448();
        if (scale != 1.0f) {
            matrixStack.method_22903();
            matrixStack.method_22905(scale, scale, 1.0f);
            if (scale > 1.0f) {
                matrixStack.method_46416(-x / scale, -y / scale, 0.0f);
            } else {
                matrixStack.method_46416(x / scale / 2.0f, y / scale / 2.0f, 0.0f);
            }
        }
        TextUtil.drawString(drawContext, text, x, y, color);
        matrixStack.method_22909();
    }

    public static void drawStringScale(class_332 drawContext, String text, float x, float y, int color, float scale, boolean shadow) {
        class_4587 matrixStack = drawContext.method_51448();
        if (scale != 1.0f) {
            matrixStack.method_22903();
            matrixStack.method_22905(scale, scale, 1.0f);
            if (scale > 1.0f) {
                matrixStack.method_46416(-x / scale, -y / scale, 0.0f);
            } else {
                matrixStack.method_46416(x / scale / 2.0f, y / scale / 2.0f, 0.0f);
            }
        }
        drawContext.method_51433(TextUtil.mc.field_1772, text, (int)x, (int)y, color, shadow);
        matrixStack.method_22909();
    }

    public static void drawString(class_332 drawContext, String text, double x, double y, int color) {
        TextUtil.drawString(drawContext, text, x, y, color, false);
    }

    public static void drawString(class_332 drawContext, String text, double x, double y, int color, boolean customFont) {
        TextUtil.drawString(drawContext, text, x, y, color, customFont, true);
    }

    public static void drawString(class_332 drawContext, String text, double x, double y, int color, boolean customFont, boolean shadow) {
        if (customFont) {
            FontManager.ui.drawString(drawContext.method_51448(), text, (double)((int)x), (double)((int)y), color, shadow);
        } else {
            drawContext.method_51433(TextUtil.mc.field_1772, text, (int)x, (int)y, color, shadow);
        }
    }

    public static void drawStringPulse(class_332 drawContext, String text, double x, double y, Color startColor, Color endColor, double speed, int counter, boolean customFont) {
        char[] stringToCharArray = text.toCharArray();
        int index = 0;
        boolean color = false;
        String s = null;
        for (char c : stringToCharArray) {
            if (c == '\u00a7') {
                color = true;
                continue;
            }
            if (color) {
                s = c == 'r' ? null : "\u00a7" + c;
                color = false;
                continue;
            }
            ++index;
            if (s != null) {
                TextUtil.drawString(drawContext, s + c, x, y, startColor.getRGB(), customFont);
            } else {
                TextUtil.drawString(drawContext, String.valueOf(c), x, y, ColorUtil.pulseColor(startColor, endColor, index, counter, speed).getRGB(), customFont);
            }
            x += customFont ? (double)FontManager.ui.getWidth(String.valueOf(c)) : (double)TextUtil.mc.field_1772.method_1727(String.valueOf(c));
        }
    }

    public static void drawStringPulse(class_332 drawContext, String text, double x, double y, Color startColor, Color endColor, double speed, int counter, boolean customFont, boolean shadow) {
        char[] stringToCharArray = text.toCharArray();
        int index = 0;
        boolean color = false;
        String s = null;
        for (char c : stringToCharArray) {
            if (c == '\u00a7') {
                color = true;
                continue;
            }
            if (color) {
                s = c == 'r' ? null : "\u00a7" + c;
                color = false;
                continue;
            }
            ++index;
            if (s != null) {
                TextUtil.drawString(drawContext, s + c, x, y, startColor.getRGB(), customFont, shadow);
            } else {
                TextUtil.drawString(drawContext, String.valueOf(c), x, y, ColorUtil.pulseColor(startColor, endColor, index, counter, speed).getRGB(), customFont, shadow);
            }
            x += customFont ? (double)FontManager.ui.getWidth(String.valueOf(c)) : (double)TextUtil.mc.field_1772.method_1727(String.valueOf(c));
        }
    }

    public static class_243 worldSpaceToScreenSpace(class_243 pos) {
        class_4184 camera = TextUtil.mc.method_1561().field_4686;
        int displayHeight = mc.method_22683().method_4507();
        int[] viewport = new int[4];
        GL11.glGetIntegerv((int)2978, (int[])viewport);
        Vector3f target = new Vector3f();
        double deltaX = pos.field_1352 - camera.method_19326().field_1352;
        double deltaY = pos.field_1351 - camera.method_19326().field_1351;
        double deltaZ = pos.field_1350 - camera.method_19326().field_1350;
        Vector4f transformedCoordinates = new Vector4f((float)deltaX, (float)deltaY, (float)deltaZ, 1.0f).mul((Matrix4fc)lastWorldSpaceMatrix);
        Matrix4f matrixProj = new Matrix4f((Matrix4fc)lastProjMat);
        Matrix4f matrixModel = new Matrix4f((Matrix4fc)lastModMat);
        matrixProj.mul((Matrix4fc)matrixModel).project(transformedCoordinates.x(), transformedCoordinates.y(), transformedCoordinates.z(), viewport, target);
        return new class_243((double)target.x / mc.method_22683().method_4495(), (double)((float)displayHeight - target.y) / mc.method_22683().method_4495(), (double)target.z);
    }
}

