/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1542
 *  net.minecraft.class_1657
 *  net.minecraft.class_243
 *  net.minecraft.class_2586
 *  net.minecraft.class_2595
 *  net.minecraft.class_2611
 *  net.minecraft.class_2627
 *  net.minecraft.class_4587
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_2595;
import net.minecraft.class_2611;
import net.minecraft.class_2627;
import net.minecraft.class_4587;

public class Tracers
extends Module {
    private final ColorSetting item = this.add(new ColorSetting("Item", new Color(255, 255, 255, 100)).injectBoolean(true));
    private final ColorSetting player = this.add(new ColorSetting("Player", new Color(255, 255, 255, 100)).injectBoolean(true));
    private final ColorSetting chest = this.add(new ColorSetting("Chest", new Color(255, 255, 255, 100)).injectBoolean(false));
    private final ColorSetting enderChest = this.add(new ColorSetting("EnderChest", new Color(255, 100, 255, 100)).injectBoolean(false));
    private final ColorSetting shulkerBox = this.add(new ColorSetting("ShulkerBox", new Color(15, 255, 255, 100)).injectBoolean(false));

    public Tracers() {
        super("Tracers", Module.Category.Render);
        this.setChinese("\u8ffd\u8e2a\u8005");
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        Tracers.mc.field_1690.method_42448().method_41748((Object)false);
        if (this.item.booleanValue || this.player.booleanValue) {
            for (class_1297 entity : Alien.THREAD.getEntities()) {
                if (entity instanceof class_1542 && this.item.booleanValue) {
                    this.drawLine(entity.method_19538(), this.item.getValue());
                    continue;
                }
                if (!(entity instanceof class_1657) || !this.player.booleanValue || entity == Tracers.mc.field_1724) continue;
                this.drawLine(entity.method_19538(), this.player.getValue());
            }
        }
        for (class_2586 blockEntity : BlockUtil.getTileEntities()) {
            if (blockEntity instanceof class_2595 && this.chest.booleanValue) {
                this.drawLine(blockEntity.method_11016().method_46558(), this.chest.getValue());
                continue;
            }
            if (blockEntity instanceof class_2611 && this.enderChest.booleanValue) {
                this.drawLine(blockEntity.method_11016().method_46558(), this.enderChest.getValue());
                continue;
            }
            if (!(blockEntity instanceof class_2627) || !this.shulkerBox.booleanValue) continue;
            this.drawLine(blockEntity.method_11016().method_46558(), this.shulkerBox.getValue());
        }
    }

    private void drawLine(class_243 pos, Color color) {
        Render3DUtil.drawLine(pos, Tracers.mc.field_1773.method_19418().method_19326().method_1019(class_243.method_1030((float)Tracers.mc.field_1724.method_5695(mc.method_60646().method_60637(true)), (float)Tracers.mc.field_1724.method_5705(mc.method_60646().method_60637(true))).method_1021(0.2)), color);
    }
}

