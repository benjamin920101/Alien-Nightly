/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_4068
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_442
 *  net.minecraft.class_4587
 *  org.ladysnake.satin.api.managed.ManagedShaderEffect
 *  org.lwjgl.glfw.GLFW
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.asm.accessors.IScreen;
import dev.luminous.core.impl.FontManager;
import dev.luminous.core.impl.ShaderManager;
import java.awt.Color;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4068;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_4587;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_442.class})
public class MixinTitleScreen {
    @Inject(method={"method_25394"}, at={@At(value="HEAD")}, cancellable=true)
    private void vitalityBackground(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float py;
        float px;
        float ry;
        float rx;
        float a;
        int i;
        float x0;
        float i2;
        float w = context.method_51421();
        float h = context.method_51443();
        class_4587 m = context.method_51448();
        long now = System.currentTimeMillis();
        float tNorm = (float)(now % 8000L) / 8000.0f;
        Color blue = new Color(28, 60, 110, 255);
        Color purple = new Color(190, 50, 160, 255);
        float mix = (float)((Math.sin((double)tNorm * Math.PI * 2.0) + 1.0) * 0.5);
        int topR = (int)((float)blue.getRed() * (1.0f - mix) + (float)purple.getRed() * mix);
        int topG = (int)((float)blue.getGreen() * (1.0f - mix) + (float)purple.getGreen() * mix);
        int topB = (int)((float)blue.getBlue() * (1.0f - mix) + (float)purple.getBlue() * mix);
        int botR = (int)((float)purple.getRed() * (1.0f - mix) + (float)blue.getRed() * mix);
        int botG = (int)((float)purple.getGreen() * (1.0f - mix) + (float)blue.getGreen() * mix);
        int botB = (int)((float)purple.getBlue() * (1.0f - mix) + (float)blue.getBlue() * mix);
        Color g1 = new Color(topR, topG, topB, 255);
        Color g2 = new Color(botR, botG, botB, 255);
        Render2DUtil.verticalGradient(m, 0.0f, 0.0f, w, h, g1, g2);
        context.method_25296(0, 0, (int)w, (int)h, g1.getRGB(), g2.getRGB());
        float phase = (float)(now % 7000L) / 7000.0f;
        float angle = 0.523599f;
        float dx = (float)Math.tan(angle) * h;
        float base = -h;
        float spacing = 68.0f;
        float shift = phase * spacing * 3.1f;
        Color c1 = new Color(255, 150, 240, 26);
        Color c2 = new Color(120, 220, 255, 20);
        for (i2 = base; i2 < w; i2 += spacing) {
            x0 = i2 + shift;
            Render2DUtil.drawLine(m, x0, 0.0f, x0 + dx, h, c1.getRGB());
        }
        for (i2 = base + spacing / 2.0f; i2 < w; i2 += spacing) {
            x0 = i2 + shift * 0.85f;
            Render2DUtil.drawLine(m, x0, 0.0f, x0 + dx, h, c2.getRGB());
        }
        float pulse = (float)Math.sin((double)((float)(now % 4000L) / 4000.0f) * Math.PI * 2.0) * 0.5f + 0.5f;
        float radius = Math.min(w, h) * (0.12f + 0.08f * pulse);
        Color ring = new Color(255, 255, 255, 30);
        Render2DUtil.drawCircle(m, w / 2.0f, h / 2.0f, radius, ring, 80);
        Render2DUtil.drawCircle(m, w / 2.0f, h / 2.0f, radius * 1.2f, new Color(120, 220, 255, 24), 80);
        int dots = 22;
        for (i = 0; i < dots; ++i) {
            a = (float)(Math.PI * 2 * (double)i / (double)dots + (double)(phase * 2.0f) * Math.PI);
            rx = (float)Math.cos(a) * (radius * 1.2f);
            ry = (float)Math.sin(a) * (radius * 0.7f);
            px = w / 2.0f + rx;
            py = h / 2.0f + ry + (float)Math.sin(a * 2.0f + phase * 4.0f) * 6.0f;
            Render2DUtil.drawCircle(m, px, py, 1.5f, new Color(255, 255, 255, 38), 30);
        }
        for (i = 0; i < 12; ++i) {
            a = (float)(Math.PI * 2 * (double)i / 12.0 + (double)(phase * 3.1f));
            rx = (float)Math.cos(a) * (radius * 1.6f);
            ry = (float)Math.sin(a) * (radius * 1.0f);
            px = w / 2.0f + rx;
            py = h / 2.0f + ry;
            Render2DUtil.drawCircle(m, px, py, 2.4f, new Color(120, 220, 255, 36), 36);
        }
        int y = 0;
        while ((float)y < h) {
            int alpha = 18;
            int c = new Color(0, 0, 0, alpha).getRGB();
            Render2DUtil.drawLine(m, 0.0f, y, w, y, c);
            y += 3;
        }
        if (!Alien.SHADER.fullNullCheck()) {
            ManagedShaderEffect gradient = Alien.SHADER.getShader(ShaderManager.Shader.Gradient);
            gradient.setUniformValue("alpha2", 0.36f);
            gradient.setUniformValue("rgb", 0.1f, 0.75f, 1.0f);
            gradient.setUniformValue("rgb1", 0.98f, 0.35f, 0.74f);
            gradient.setUniformValue("rgb2", 0.46f, 0.19f, 0.81f);
            gradient.setUniformValue("rgb3", 0.12f, 0.5f, 0.95f);
            gradient.setUniformValue("step", 99.0f);
            gradient.setUniformValue("radius", 1.6f);
            gradient.setUniformValue("quality", 0.8f);
            gradient.setUniformValue("divider", 220.0f);
            gradient.setUniformValue("maxSample", 6.0f);
            gradient.setUniformValue("resolution", w, h);
            float t = (float)(now % 100000L) / 1000.0f;
            gradient.setUniformValue("time", t * 220.0f);
            gradient.render(class_310.method_1551().method_60646().method_60637(true));
        }
        m.method_22903();
        float titleScale = 3.6f;
        m.method_46416(w / 2.0f, h * 0.24f, 0.0f);
        m.method_22905(titleScale, titleScale, 1.0f);
        FontManager.ui.drawCenteredString(m, "Alien", 0.0, 0.0, new Color(230, 255, 255, 255));
        FontManager.ui.drawCenteredString(m, "Alien", 0.0, (double)2.2f, new Color(120, 220, 255, 180));
        FontManager.ui.drawCenteredString(m, "Alien", 0.0, (double)-2.2f, new Color(255, 160, 240, 160));
        m.method_22909();
        class_437 c = class_310.method_1551().field_1755;
        if (c instanceof class_442) {
            class_442 ts = (class_442)c;
            int idx = 0;
            for (class_4068 d : ((IScreen)IScreen.class.cast(ts)).getDrawables()) {
                if (d instanceof class_4185) {
                    class_4185 bw = (class_4185)d;
                    if (idx < 3) {
                        int bx = bw.method_46426();
                        int by = bw.method_46427();
                        int bwid = bw.method_25368();
                        int bhei = bw.method_25364();
                        boolean hovered = mouseX >= bx && mouseX <= bx + bwid && mouseY >= by && mouseY <= by + bhei;
                        boolean pressed = hovered && GLFW.glfwGetMouseButton((long)class_310.method_1551().method_22683().method_4490(), (int)0) == 1;
                        Color accent = new Color(0, 120, 212, 255);
                        Color neon = new Color(0, 224, 255, 180);
                        Color amber = new Color(255, 210, 0, 160);
                        Render2DUtil.drawRoundedStroke(context.method_51448(), bx, by, bwid, bhei, 5.0f, hovered ? new Color(220, 224, 230, 200) : new Color(220, 224, 230, 160), 64);
                        Render2DUtil.drawLine(context.method_51448(), (float)bx + 2.0f, (float)by + 2.0f, (float)(bx + bwid) - 2.0f, (float)by + 2.0f, new Color(255, 255, 255, 80).getRGB());
                        Render2DUtil.drawLine(context.method_51448(), (float)bx + 2.0f, (float)(by + bhei) - 2.2f, (float)(bx + bwid) - 2.0f, (float)(by + bhei) - 2.2f, new Color(120, 130, 140, 60).getRGB());
                        Render2DUtil.drawLine(context.method_51448(), (float)bx + 4.0f, (float)by + 4.0f, (float)bx + 18.0f, (float)by + 10.0f, neon.getRGB());
                        Render2DUtil.drawLine(context.method_51448(), (float)(bx + bwid) - 18.0f, (float)(by + bhei) - 6.0f, (float)(bx + bwid) - 6.0f, (float)(by + bhei) - 2.0f, amber.getRGB());
                        if (pressed) {
                            Render2DUtil.drawGlow(context.method_51448(), (float)bx - 2.0f, (float)by - 2.0f, (float)bwid + 4.0f, (float)bhei + 4.0f, new Color(0, 0, 0, 18).getRGB());
                            Render2DUtil.verticalGradient(context.method_51448(), (float)bx + 1.5f, (float)by + 1.5f, (float)(bx + bwid) - 1.5f, (float)(by + bhei) - 1.5f, new Color(255, 255, 255, 50), new Color(0, 0, 0, 46));
                            Render2DUtil.drawRoundedStroke(context.method_51448(), (float)bx + 1.0f, (float)by + 1.0f, (float)bwid - 2.0f, (float)bhei - 2.0f, 4.2f, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 160), 96);
                            Render2DUtil.drawRoundedStroke(context.method_51448(), (float)bx + 1.6f, (float)by + 1.6f, (float)bwid - 3.2f, (float)bhei - 3.2f, 3.8f, new Color(255, 255, 255, 140), 96);
                            Render2DUtil.drawRoundedStroke(context.method_51448(), (float)bx + 2.0f, (float)(by + bhei) - 2.8f, (float)bwid - 4.0f, 1.6f, 2.0f, new Color(90, 100, 110, 100), 96);
                            by = (int)((float)by + 1.2f);
                            bx = (int)((float)bx + 0.6f);
                        } else if (hovered) {
                            Render2DUtil.drawRoundedStroke(context.method_51448(), (float)bx - 0.5f, (float)by - 0.5f, (float)bwid + 1.0f, (float)bhei + 1.0f, 5.4f, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 160), 96);
                        }
                        d.method_25394(context, mouseX, mouseY, delta);
                    }
                }
                if (d instanceof class_4185) {
                    ++idx;
                }
                if (!(d instanceof class_4185)) {
                    d.method_25394(context, mouseX, mouseY, delta);
                    continue;
                }
                if (idx < 3) continue;
                d.method_25394(context, mouseX, mouseY, delta);
            }
        }
        ci.cancel();
    }

    @Inject(method={"method_25426()V"}, at={@At(value="TAIL")})
    private void vitalityLayout(CallbackInfo ci) {
        class_442 self = (class_442)this;
        int w = self.field_22789;
        int h = self.field_22790;
        int btnW = Math.min(300, (int)((double)w * 0.42));
        int btnH = 24;
        int startY = (int)((double)h * 0.5);
        int spacing = 8 + btnH;
        int xLeft = (int)((double)w * 0.26) - btnW / 2;
        int xRight = (int)((double)w * 0.74) - btnW / 2;
        int xCenter = w / 2 - btnW / 2;
        int idx = 0;
        for (class_4068 d : ((IScreen)IScreen.class.cast(self)).getDrawables()) {
            if (!(d instanceof class_4185)) continue;
            class_4185 bw = (class_4185)d;
            int col = idx % 2;
            int row = idx / 2;
            if (idx == 0) {
                bw.method_25358(btnW);
                bw.method_48229(xLeft, startY);
            } else if (idx == 1) {
                bw.method_25358(btnW);
                bw.method_48229(xRight, startY);
            } else if (idx == 2) {
                bw.method_25358(btnW);
                bw.method_48229(xCenter, startY + spacing);
            } else {
                int x = col == 0 ? xLeft : xRight;
                bw.method_48229(x, startY + spacing * row);
            }
            ++idx;
        }
    }
}

