/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1263
 *  net.minecraft.class_1277
 *  net.minecraft.class_1733
 *  net.minecraft.class_1747
 *  net.minecraft.class_1767
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2480
 *  net.minecraft.class_2960
 *  net.minecraft.class_332
 *  net.minecraft.class_3872
 *  net.minecraft.class_3872$class_3931
 *  net.minecraft.class_437
 *  net.minecraft.class_495
 *  net.minecraft.class_757
 *  net.minecraft.class_9334
 */
package dev.luminous.mod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.impl.misc.ShulkerViewer;
import java.awt.Color;
import net.minecraft.class_1263;
import net.minecraft.class_1277;
import net.minecraft.class_1733;
import net.minecraft.class_1747;
import net.minecraft.class_1767;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2480;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3872;
import net.minecraft.class_437;
import net.minecraft.class_495;
import net.minecraft.class_757;
import net.minecraft.class_9334;

public class PeekScreen
extends class_495 {
    private final class_2960 TEXTURE = class_2960.method_60654((String)"textures/gui/container/shulker_box.png");
    private final class_1799[] contents;
    private final class_1799 storageBlock;

    public PeekScreen(class_1799 storageBlock, class_1799[] contents) {
        super(new class_1733(0, Wrapper.mc.field_1724.method_31548(), (class_1263)new class_1277(contents)), Wrapper.mc.field_1724.method_31548(), storageBlock.method_7964());
        this.contents = contents;
        this.storageBlock = storageBlock;
    }

    public static Color getShulkerColor(class_1799 shulkerItem) {
        class_1747 blockItem;
        class_1792 class_17922 = shulkerItem.method_7909();
        if (class_17922 instanceof class_1747 && (class_17922 = (blockItem = (class_1747)class_17922).method_7711()) instanceof class_2480) {
            class_2480 shulkerBlock = (class_2480)class_17922;
            class_1767 dye = shulkerBlock.method_10528();
            if (dye == null) {
                return Color.WHITE;
            }
            int color = dye.method_7787();
            return new Color(color);
        }
        return Color.WHITE;
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        if (button == 2 && this.field_2787 != null && !this.field_2787.method_7677().method_7960() && Wrapper.mc.field_1724.field_7512.method_34255().method_7960()) {
            class_1799 itemStack = this.field_2787.method_7677();
            if (ShulkerViewer.hasItems(itemStack) || itemStack.method_7909() == class_1802.field_8466) {
                return ShulkerViewer.openContainer(this.field_2787.method_7677(), this.contents, false);
            }
            if (itemStack.method_57824(class_9334.field_49606) != null || itemStack.method_57824(class_9334.field_49653) != null) {
                this.method_25419();
                Wrapper.mc.method_1507((class_437)new class_3872(class_3872.class_3931.method_17562((class_1799)itemStack)));
                return true;
            }
        }
        return false;
    }

    public boolean method_25406(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        if (keyCode != 256 && !Wrapper.mc.field_1690.field_1822.method_1417(keyCode, scanCode)) {
            return false;
        }
        this.method_25419();
        return true;
    }

    public boolean method_16803(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.method_25419();
            return true;
        }
        return false;
    }

    protected void method_2389(class_332 context, float delta, int mouseX, int mouseY) {
        Color color = PeekScreen.getShulkerColor(this.storageBlock);
        RenderSystem.setShader(class_757::method_34542);
        RenderSystem.setShaderColor((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        int i = (this.field_22789 - this.field_2792) / 2;
        int j = (this.field_22790 - this.field_2779) / 2;
        context.method_25302(this.TEXTURE, i, j, 0, 0, this.field_2792, this.field_2779);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }
}

