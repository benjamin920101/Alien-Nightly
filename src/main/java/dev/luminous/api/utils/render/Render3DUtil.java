/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1297
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_286
 *  net.minecraft.class_287
 *  net.minecraft.class_289
 *  net.minecraft.class_290
 *  net.minecraft.class_293$class_5596
 *  net.minecraft.class_327$class_6415
 *  net.minecraft.class_3532
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_4587$class_4665
 *  net.minecraft.class_4588
 *  net.minecraft.class_4597
 *  net.minecraft.class_4597$class_4598
 *  net.minecraft.class_5348
 *  net.minecraft.class_757
 *  net.minecraft.class_7833
 *  net.minecraft.class_9799
 *  net.minecraft.class_9801
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Matrix4f
 *  org.joml.Vector3f
 */
package dev.luminous.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.render.ColorUtil;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_293;
import net.minecraft.class_327;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import net.minecraft.class_5348;
import net.minecraft.class_757;
import net.minecraft.class_7833;
import net.minecraft.class_9799;
import net.minecraft.class_9801;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Render3DUtil
implements Wrapper {
    public static void endBuilding(class_287 bb) {
        class_9801 builtBuffer = bb.method_60794();
        if (builtBuffer != null) {
            class_286.method_43433((class_9801)builtBuffer);
        }
    }

    public static class_4587 matrixFrom(double x, double y, double z) {
        class_4587 matrices = new class_4587();
        class_4184 camera = Render3DUtil.mc.field_1773.method_19418();
        matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrices.method_22907(class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        matrices.method_22904(x - camera.method_19326().field_1352, y - camera.method_19326().field_1351, z - camera.method_19326().field_1350);
        return matrices;
    }

    public static void drawText3D(String text, class_243 vec3d, Color color) {
        Render3DUtil.drawText3D(class_2561.method_30163((String)text), vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, 0.0, 0.0, 1.0, color.getRGB());
    }

    public static void drawText3D(String text, class_243 vec3d, int color) {
        Render3DUtil.drawText3D(class_2561.method_30163((String)text), vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, 0.0, 0.0, 1.0, color);
    }

    public static void drawText3D(class_2561 text, class_243 vec3d, double offX, double offY, double scale, Color color) {
        Render3DUtil.drawText3D(text, vec3d.field_1352, vec3d.field_1351, vec3d.field_1350, offX, offY, scale, color.getRGB());
    }

    public static void drawText3D(class_2561 text, double x, double y, double z, double offX, double offY, double scale, int color) {
        RenderSystem.disableDepthTest();
        class_4587 matrices = Render3DUtil.matrixFrom(x, y, z);
        class_4184 camera = Render3DUtil.mc.field_1773.method_19418();
        matrices.method_22907(class_7833.field_40716.rotationDegrees(-camera.method_19330()));
        matrices.method_22907(class_7833.field_40714.rotationDegrees(camera.method_19329()));
        RenderSystem.enableBlend();
        matrices.method_22904(offX, offY, 0.0);
        matrices.method_22905(-0.025f * (float)scale, -0.025f * (float)scale, 1.0f);
        int halfWidth = Render3DUtil.mc.field_1772.method_27525((class_5348)text) / 2;
        class_4597.class_4598 immediate = class_4597.method_22991((class_9799)new class_9799(1536));
        Render3DUtil.mc.field_1772.method_1724(text.getString(), (float)(-halfWidth), 0.0f, -1, true, matrices.method_23760().method_23761(), (class_4597)immediate, class_327.class_6415.field_33994, 0, 0xF000F0);
        immediate.method_22993();
        Render3DUtil.mc.field_1772.method_30882((class_2561)text.method_27661(), (float)(-halfWidth), 0.0f, color, false, matrices.method_23760().method_23761(), (class_4597)immediate, class_327.class_6415.field_33994, 0, 0xF000F0);
        immediate.method_22993();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    public static void drawFill(class_4587 matrixStack, class_238 bb, Color fillColor) {
        Render3DUtil.draw3DBox(matrixStack, bb, fillColor, new Color(0, 0, 0, 0), false, true);
    }

    public static void drawBox(class_4587 matrixStack, class_238 bb, Color outlineColor) {
        Render3DUtil.draw3DBox(matrixStack, bb, new Color(0, 0, 0, 0), outlineColor, true, false);
    }

    public static void drawBox(class_4587 matrixStack, class_238 bb, Color outlineColor, float lineWidth) {
        Render3DUtil.draw3DBox(matrixStack, bb, new Color(0, 0, 0, 0), outlineColor, true, false, lineWidth);
    }

    public static void draw3DBox(class_4587 matrixStack, class_238 box, Color fillColor, Color outlineColor) {
        Render3DUtil.draw3DBox(matrixStack, box, fillColor, outlineColor, true, true);
    }

    public static void draw3DBox(class_4587 matrixStack, class_238 box, Color fillColor, Color outlineColor, boolean outline, boolean fill) {
        Render3DUtil.draw3DBox(matrixStack, box, fillColor, outlineColor, outline, fill, 1.5f);
    }

    public static void draw3DBox(class_4587 matrixStack, class_238 box, Color fillColor, Color outlineColor, boolean outline, boolean fill, float lineWidth) {
        class_287 bufferBuilder;
        float b;
        float g;
        float r;
        float a;
        box = box.method_997(Render3DUtil.mc.field_1773.method_19418().method_19326().method_22882());
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        Matrix4f matrix = matrixStack.method_23760().method_23761();
        if (outline) {
            a = (float)outlineColor.getAlpha() / 255.0f;
            r = (float)outlineColor.getRed() / 255.0f;
            g = (float)outlineColor.getGreen() / 255.0f;
            b = (float)outlineColor.getBlue() / 255.0f;
            RenderSystem.setShader(class_757::method_34540);
            RenderSystem.lineWidth((float)lineWidth);
            bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_29344, class_290.field_1576);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            class_286.method_43433((class_9801)bufferBuilder.method_60800());
        }
        if (fill) {
            a = (float)fillColor.getAlpha() / 255.0f;
            r = (float)fillColor.getRed() / 255.0f;
            g = (float)fillColor.getGreen() / 255.0f;
            b = (float)fillColor.getBlue() / 255.0f;
            RenderSystem.setShader(class_757::method_34540);
            bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1320, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1321).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1322, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1324).method_22915(r, g, b, a);
            bufferBuilder.method_22918(matrix, (float)box.field_1323, (float)box.field_1325, (float)box.field_1321).method_22915(r, g, b, a);
            class_286.method_43433((class_9801)bufferBuilder.method_60800());
        }
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    public static void drawFadeFill(class_4587 stack, class_238 box, Color c, Color c1) {
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(class_757::method_34540);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, class_290.field_1576);
        Matrix4f posMatrix = stack.method_23760().method_23761();
        float minX = (float)(box.field_1323 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10216());
        float minY = (float)(box.field_1322 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10214());
        float minZ = (float)(box.field_1321 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10215());
        float maxX = (float)(box.field_1320 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10216());
        float maxY = (float)(box.field_1325 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10214());
        float maxZ = (float)(box.field_1324 - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10215());
        buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, maxX, minY, minZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, maxX, minY, maxZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, minX, minY, minZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, minX, minY, maxZ).method_39415(c.getRGB());
        buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, minX, maxY, minZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, minX, maxY, maxZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, maxX, maxY, maxZ).method_39415(c1.getRGB());
        buffer.method_22918(posMatrix, maxX, maxY, minZ).method_39415(c1.getRGB());
        class_286.method_43433((class_9801)buffer.method_60800());
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void drawLine(class_243 start, class_243 end, Color color) {
        Render3DUtil.drawLine(start.field_1352, start.method_10214(), start.field_1350, end.method_10216(), end.method_10214(), end.method_10215(), color, 1.0f);
    }

    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, Color color, float width) {
        RenderSystem.enableBlend();
        class_4587 matrices = Render3DUtil.matrixFrom(x1, y1, z1);
        RenderSystem.disableCull();
        RenderSystem.setShader(class_757::method_34535);
        RenderSystem.lineWidth((float)width);
        class_287 buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27377, class_290.field_29337);
        Render3DUtil.vertexLine(matrices, (class_4588)buffer, 0.0, 0.0, 0.0, (float)(x2 - x1), (float)(y2 - y1), (float)(z2 - z1), color);
        class_286.method_43433((class_9801)buffer.method_60800());
        RenderSystem.enableCull();
        RenderSystem.lineWidth((float)1.0f);
        RenderSystem.disableBlend();
    }

    public static void vertexLine(class_4587 matrices, class_4588 buffer, double x1, double y1, double z1, double x2, double y2, double z2, Color lineColor) {
        Matrix4f model = matrices.method_23760().method_23761();
        class_4587.class_4665 entry = matrices.method_23760();
        Vector3f normalVec = Render3DUtil.getNormal((float)x1, (float)y1, (float)z1, (float)x2, (float)y2, (float)z2);
        buffer.method_22918(model, (float)x1, (float)y1, (float)z1).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x(), normalVec.y(), normalVec.z());
        buffer.method_22918(model, (float)x2, (float)y2, (float)z2).method_1336(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), lineColor.getAlpha()).method_60831(entry, normalVec.x(), normalVec.y(), normalVec.z());
    }

    public static Vector3f getNormal(float x1, float y1, float z1, float x2, float y2, float z2) {
        float xNormal = x2 - x1;
        float yNormal = y2 - y1;
        float zNormal = z2 - z1;
        float normalSqrt = class_3532.method_15355((float)(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal));
        return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
    }

    public static void drawTargetEsp(class_4587 stack, @NotNull class_1297 target, Color color) {
        int j;
        ArrayList<class_243> vecs = new ArrayList<class_243>();
        ArrayList<class_243> vecs1 = new ArrayList<class_243>();
        ArrayList<class_243> vecs2 = new ArrayList<class_243>();
        double x = target.field_6014 + (target.method_23317() - target.field_6014) * (double)Render3DUtil.getTickDelta() - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10216();
        double y = target.field_6036 + (target.method_23318() - target.field_6036) * (double)Render3DUtil.getTickDelta() - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10214();
        double z = target.field_5969 + (target.method_23321() - target.field_5969) * (double)Render3DUtil.getTickDelta() - Render3DUtil.mc.method_1561().field_4686.method_19326().method_10215();
        double height = target.method_17682();
        for (int i = 0; i <= 361; ++i) {
            double v = Math.sin(Math.toRadians(i));
            double u = Math.cos(Math.toRadians(i));
            class_243 vec = new class_243((double)((float)(u * 0.5)), height, (double)((float)(v * 0.5)));
            vecs.add(vec);
            double v1 = Math.sin(Math.toRadians((i + 120) % 360));
            double u1 = Math.cos(Math.toRadians(i + 120) % 360.0);
            class_243 vec1 = new class_243((double)((float)(u1 * 0.5)), height, (double)((float)(v1 * 0.5)));
            vecs1.add(vec1);
            double v2 = Math.sin(Math.toRadians((i + 240) % 360));
            double u2 = Math.cos(Math.toRadians((i + 240) % 360));
            class_243 vec2 = new class_243((double)((float)(u2 * 0.5)), height, (double)((float)(v2 * 0.5)));
            vecs2.add(vec2);
            height -= (double)0.004f;
        }
        stack.method_22903();
        stack.method_22904(x, y, z);
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(class_757::method_34540);
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27380, class_290.field_1576);
        Matrix4f matrix = stack.method_23760().method_23761();
        for (j = 0; j < vecs.size() - 1; ++j) {
            float alpha = 1.0f - ((float)j + (float)(System.currentTimeMillis() - Alien.initTime) / 5.0f) % 360.0f / 60.0f;
            bufferBuilder.method_22918(matrix, (float)((class_243)vecs.get((int)j)).field_1352, (float)((class_243)vecs.get((int)j)).field_1351, (float)((class_243)vecs.get((int)j)).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (int)((float)j / 20.0f), 10, 1.0), (int)(alpha * 255.0f)).getRGB());
            bufferBuilder.method_22918(matrix, (float)((class_243)vecs.get((int)(j + 1))).field_1352, (float)((class_243)vecs.get((int)(j + 1))).field_1351 + 0.1f, (float)((class_243)vecs.get((int)(j + 1))).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (int)((float)j / 20.0f), 10, 1.0), (int)(alpha * 255.0f)).getRGB());
        }
        Render3DUtil.endBuilding(bufferBuilder);
        RenderSystem.setShader(class_757::method_34540);
        bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27380, class_290.field_1576);
        for (j = 0; j < vecs1.size() - 1; ++j) {
            float alpha = 1.0f - ((float)j + (float)(System.currentTimeMillis() - Alien.initTime) / 5.0f) % 360.0f / 60.0f;
            bufferBuilder.method_22918(matrix, (float)((class_243)vecs1.get((int)j)).field_1352, (float)((class_243)vecs1.get((int)j)).field_1351, (float)((class_243)vecs1.get((int)j)).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (int)((float)j / 20.0f), 10, 1.0), (int)(alpha * 255.0f)).getRGB());
            bufferBuilder.method_22918(matrix, (float)((class_243)vecs1.get((int)(j + 1))).field_1352, (float)((class_243)vecs1.get((int)(j + 1))).field_1351 + 0.1f, (float)((class_243)vecs1.get((int)(j + 1))).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (int)((float)j / 20.0f), 10, 1.0), (int)(alpha * 255.0f)).getRGB());
        }
        Render3DUtil.endBuilding(bufferBuilder);
        RenderSystem.setShader(class_757::method_34540);
        bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_27380, class_290.field_1576);
        for (j = 0; j < vecs2.size() - 1; ++j) {
            float alpha = 1.0f - ((float)j + (float)(System.currentTimeMillis() - Alien.initTime) / 5.0f) % 360.0f / 60.0f;
            bufferBuilder.method_22918(matrix, (float)((class_243)vecs2.get((int)j)).field_1352, (float)((class_243)vecs2.get((int)j)).field_1351, (float)((class_243)vecs2.get((int)j)).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (int)((float)j / 20.0f), 10, 1.0), (int)(alpha * 255.0f)).getRGB());
            bufferBuilder.method_22918(matrix, (float)((class_243)vecs2.get((int)(j + 1))).field_1352, (float)((class_243)vecs2.get((int)(j + 1))).field_1351 + 0.1f, (float)((class_243)vecs2.get((int)(j + 1))).field_1350).method_39415(ColorUtil.injectAlpha(ColorUtil.pulseColor(color, (int)((float)j / 20.0f), 10, 1.0), (int)(alpha * 255.0f)).getRGB());
        }
        Render3DUtil.endBuilding(bufferBuilder);
        RenderSystem.enableCull();
        stack.method_22904(-x, -y, -z);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        stack.method_22909();
    }

    public static float getTickDelta() {
        return mc.method_60646().method_60637(true);
    }
}

