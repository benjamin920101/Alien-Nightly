/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_4587
 */
package dev.luminous.core.impl;

import dev.luminous.mod.gui.fonts.FontRenderer;
import dev.luminous.mod.modules.impl.client.Fonts;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import net.minecraft.class_4587;

public class FontManager {
    public static FontRenderer ui;
    public static FontRenderer small;
    public static FontRenderer icon;

    public static void init() {
        try {
            ui = FontManager.assets(8.0f, "default", 0);
            small = FontManager.assets(6.0f, "default", 0);
            icon = FontManager.assetsWithoutOffset(8.0f, "icon", 0);
        }
        catch (Exception var1) {
            var1.printStackTrace();
        }
    }

    public static FontRenderer assets(float size, String font, int style, String alternate) throws IOException, FontFormatException {
        return new FontRenderer(Font.createFont(0, Objects.requireNonNull(FontManager.class.getClassLoader().getResourceAsStream("assets/alienclient/font/" + font + ".ttf"))).deriveFont(style, size), FontManager.getFont(alternate, style, (int)size), size){

            @Override
            public void drawString(class_4587 stack, String s, float x, float y, float r, float g, float b, float a, boolean shadow) {
                super.drawString(stack, s, x + (float)Fonts.INSTANCE.translate.getValueInt(), y + (float)Fonts.INSTANCE.shift.getValueInt(), r, g, b, a, shadow);
            }
        };
    }

    public static FontRenderer assetsWithoutOffset(float size, String name, int style) throws IOException, FontFormatException {
        return new FontRenderer(Font.createFont(0, Objects.requireNonNull(FontManager.class.getClassLoader().getResourceAsStream("assets/alienclient/font/" + name + ".ttf"))).deriveFont(style, size), size);
    }

    public static FontRenderer assets(float size, String name, int style) throws IOException, FontFormatException {
        return new FontRenderer(Font.createFont(0, Objects.requireNonNull(FontManager.class.getClassLoader().getResourceAsStream("assets/alienclient/font/" + name + ".ttf"))).deriveFont(style, size), size){

            @Override
            public void drawString(class_4587 stack, String s, float x, float y, float r, float g, float b, float a, boolean shadow) {
                super.drawString(stack, s, x + (float)Fonts.INSTANCE.translate.getValueInt(), y + (float)Fonts.INSTANCE.shift.getValueInt(), r, g, b, a, shadow);
            }
        };
    }

    public static FontRenderer create(int size, String font, int style, String alternate) {
        return new FontRenderer(FontManager.getFont(font, style, size), FontManager.getFont(alternate, style, size), size){

            @Override
            public void drawString(class_4587 stack, String s, float x, float y, float r, float g, float b, float a, boolean shadow) {
                super.drawString(stack, s, x + (float)Fonts.INSTANCE.translate.getValueInt(), y + (float)Fonts.INSTANCE.shift.getValueInt(), r, g, b, a, shadow);
            }
        };
    }

    public static FontRenderer create(int size, String font, int style) {
        return new FontRenderer(FontManager.getFont(font, style, size), size){

            @Override
            public void drawString(class_4587 stack, String s, float x, float y, float r, float g, float b, float a, boolean shadow) {
                super.drawString(stack, s, x + (float)Fonts.INSTANCE.translate.getValueInt(), y + (float)Fonts.INSTANCE.shift.getValueInt(), r, g, b, a, shadow);
            }
        };
    }

    private static Font getFont(String font, int style, int size) {
        File fontDir = new File("C:\\Windows\\Fonts");
        try {
            for (File file : fontDir.listFiles()) {
                if (!file.getName().replace(".ttf", "").replace(".ttc", "").replace(".otf", "").equalsIgnoreCase(font)) continue;
                try {
                    return Font.createFont(0, file).deriveFont(style, size);
                }
                catch (Exception var9) {
                    var9.printStackTrace();
                }
            }
            for (File filex : fontDir.listFiles()) {
                if (!filex.getName().startsWith(font)) continue;
                try {
                    return Font.createFont(0, filex).deriveFont(style, size);
                }
                catch (Exception var10) {
                    var10.printStackTrace();
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return new Font(null, style, size);
    }
}

