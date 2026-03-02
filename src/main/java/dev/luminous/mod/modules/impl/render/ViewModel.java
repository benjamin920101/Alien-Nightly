/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_4587
 *  net.minecraft.class_7833
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.HeldItemRendererEvent;
import dev.luminous.asm.accessors.IHeldItemRenderer;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1268;
import net.minecraft.class_4587;
import net.minecraft.class_7833;

public class ViewModel
extends Module {
    public static ViewModel INSTANCE;
    public final BooleanSetting mainhandSwap = this.add(new BooleanSetting("MainhandSwap", true));
    public final BooleanSetting offhandSwap = this.add(new BooleanSetting("OffhandSwap", true));
    public final SliderSetting scaleMainX = this.add(new SliderSetting("ScaleMainX", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting scaleMainY = this.add(new SliderSetting("ScaleMainY", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting scaleMainZ = this.add(new SliderSetting("ScaleMainZ", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting positionMainX = this.add(new SliderSetting("PositionMainX", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting positionMainY = this.add(new SliderSetting("PositionMainY", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting positionMainZ = this.add(new SliderSetting("PositionMainZ", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting rotationMainX = this.add(new SliderSetting("RotationMainX", 0.0, -180.0, 180.0, 0.01));
    public final SliderSetting rotationMainY = this.add(new SliderSetting("RotationMainY", 0.0, -180.0, 180.0, 0.01));
    public final SliderSetting rotationMainZ = this.add(new SliderSetting("RotationMainZ", 0.0, -180.0, 180.0, 0.01));
    public final SliderSetting scaleOffX = this.add(new SliderSetting("ScaleOffX", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting scaleOffY = this.add(new SliderSetting("ScaleOffY", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting scaleOffZ = this.add(new SliderSetting("ScaleOffZ", 1.0, (double)0.1f, 5.0, 0.01));
    public final SliderSetting positionOffX = this.add(new SliderSetting("PositionOffX", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting positionOffY = this.add(new SliderSetting("PositionOffY", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting positionOffZ = this.add(new SliderSetting("PositionOffZ", 0.0, -3.0, 3.0, 0.01));
    public final SliderSetting rotationOffX = this.add(new SliderSetting("RotationOffX", 0.0, -180.0, 180.0, 0.01));
    public final SliderSetting rotationOffY = this.add(new SliderSetting("RotationOffY", 0.0, -180.0, 180.0, 0.01));
    public final SliderSetting rotationOffZ = this.add(new SliderSetting("RotationOffZ", 0.0, -180.0, 180.0, 0.01));
    public final BooleanSetting slowAnimation = this.add(new BooleanSetting("SwingSpeed", true));
    public final SliderSetting slowAnimationVal = this.add(new SliderSetting("Value", 6, 1, 50));

    public ViewModel() {
        super("ViewModel", Module.Category.Render);
        this.setChinese("\u624b\u6301\u6a21\u578b");
        INSTANCE = this;
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        if (!this.mainhandSwap.getValue() && ((IHeldItemRenderer)mc.method_1561().method_43336()).getEquippedProgressMainHand() <= 1.0f) {
            ((IHeldItemRenderer)mc.method_1561().method_43336()).setEquippedProgressMainHand(1.0f);
            ((IHeldItemRenderer)mc.method_1561().method_43336()).setItemStackMainHand(ViewModel.mc.field_1724.method_6047());
        }
        if (!this.offhandSwap.getValue() && ((IHeldItemRenderer)mc.method_1561().method_43336()).getEquippedProgressOffHand() <= 1.0f) {
            ((IHeldItemRenderer)mc.method_1561().method_43336()).setEquippedProgressOffHand(1.0f);
            ((IHeldItemRenderer)mc.method_1561().method_43336()).setItemStackOffHand(ViewModel.mc.field_1724.method_6079());
        }
    }

    @EventListener
    private void onHeldItemRender(HeldItemRendererEvent event) {
        if (event.getHand() == class_1268.field_5808) {
            event.getStack().method_46416(this.positionMainX.getValueFloat(), this.positionMainY.getValueFloat(), this.positionMainZ.getValueFloat());
            event.getStack().method_22905(this.scaleMainX.getValueFloat(), this.scaleMainY.getValueFloat(), this.scaleMainZ.getValueFloat());
            event.getStack().method_22907(class_7833.field_40714.rotationDegrees(this.rotationMainX.getValueFloat()));
            event.getStack().method_22907(class_7833.field_40716.rotationDegrees(this.rotationMainY.getValueFloat()));
            event.getStack().method_22907(class_7833.field_40718.rotationDegrees(this.rotationMainZ.getValueFloat()));
        } else {
            event.getStack().method_46416(this.positionOffX.getValueFloat(), this.positionOffY.getValueFloat(), this.positionOffZ.getValueFloat());
            event.getStack().method_22905(this.scaleOffX.getValueFloat(), this.scaleOffY.getValueFloat(), this.scaleOffZ.getValueFloat());
            event.getStack().method_22907(class_7833.field_40714.rotationDegrees(this.rotationOffX.getValueFloat()));
            event.getStack().method_22907(class_7833.field_40716.rotationDegrees(this.rotationOffY.getValueFloat()));
            event.getStack().method_22907(class_7833.field_40718.rotationDegrees(this.rotationOffZ.getValueFloat()));
        }
    }
}

