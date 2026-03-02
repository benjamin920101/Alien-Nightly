/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 */
package dev.luminous.mod.gui.windows;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.mod.gui.ClickGuiScreen;
import dev.luminous.mod.gui.windows.WindowBase;
import dev.luminous.mod.modules.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class WindowsScreen
extends class_437 {
    public static WindowBase lastClickedWindow;
    public static WindowBase draggingWindow;
    private List<WindowBase> windows = new ArrayList<WindowBase>();

    public WindowsScreen(WindowBase ... windows) {
        super(class_2561.method_30163((String)"CustomWindows"));
        this.windows.clear();
        lastClickedWindow = null;
        this.windows = Arrays.stream(windows).toList();
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        if (Module.nullCheck()) {
            this.method_25420(context, mouseX, mouseY, delta);
        }
        this.windows.stream().filter(WindowBase::isVisible).forEach(w -> {
            if (w != lastClickedWindow) {
                w.render(context, mouseX, mouseY);
            }
        });
        if (lastClickedWindow != null && lastClickedWindow.isVisible()) {
            lastClickedWindow.render(context, mouseX, mouseY);
        }
    }

    public boolean method_25406(double mouseX, double mouseY, int button) {
        this.windows.forEach(w -> w.mouseReleased(mouseX, mouseY, button));
        return super.method_25406(mouseX, mouseY, button);
    }

    public boolean method_25402(double mouseX, double mouseY, int button) {
        this.windows.stream().filter(WindowBase::isVisible).forEach(wx -> wx.mouseClicked(mouseX, mouseY, button));
        int i = Wrapper.mc.method_22683().method_4486() / 2;
        float offset = (float)this.windows.size() * 20.0f / -2.0f - 23.0f;
        if (Render2DUtil.isHovered(mouseX, mouseY, (float)i + offset + 1.0f, Wrapper.mc.method_22683().method_4502() - 23, 15.0, 15.0)) {
            Wrapper.mc.method_1507((class_437)ClickGuiScreen.getInstance());
        }
        offset += 23.0f;
        for (WindowBase w : this.windows) {
            if (Render2DUtil.isHovered(mouseX, mouseY, (float)i + offset, Wrapper.mc.method_22683().method_4502() - 24, 17.0, 17.0)) {
                w.setVisible(!w.isVisible());
            }
            offset += 20.0f;
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    public boolean method_25404(int keyCode, int scanCode, int modifiers) {
        this.windows.stream().filter(WindowBase::isVisible).forEach(w -> w.keyPressed(keyCode, scanCode, modifiers));
        return super.method_25404(keyCode, scanCode, modifiers);
    }

    public boolean method_25400(char key, int keyCode) {
        this.windows.stream().filter(WindowBase::isVisible).forEach(w -> w.charTyped(key, keyCode));
        return super.method_25400(key, keyCode);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.windows.stream().filter(WindowBase::isVisible).forEach(w -> w.mouseScrolled((int)(verticalAmount * 5.0)));
        return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}

