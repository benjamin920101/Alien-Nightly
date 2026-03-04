/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.ItemEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.projectile.thrown.EnderPearlEntity
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.block.entity.BlockEntity
 *  net.minecraft.block.entity.ChestBlockEntity
 *  net.minecraft.block.entity.EnderChestBlockEntity
 *  net.minecraft.block.entity.ShulkerBoxBlockEntity
 *  net.minecraft.block.entity.EndPortalBlockEntity
 *  net.minecraft.client.util.math.MatrixStack
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.EntitySpawnedEvent;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.asm.accessors.IEntity;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.util.math.MatrixStack;

public class ESP
extends Module {
    public static ESP INSTANCE;
    public final BooleanSetting box = this.add(new BooleanSetting("BoxESP", true).setParent());
    private final ColorSetting endPortalFill = this.add(new ColorSetting("EndPortalFill", new Color(255, 243, 129, 100), this.box::isOpen).injectBoolean(false));
    private final ColorSetting endPortalOutline = this.add(new ColorSetting("EndPortalOutline", new Color(255, 243, 129, 100), this.box::isOpen).injectBoolean(false));
    private final ColorSetting itemFill = this.add(new ColorSetting("ItemFill", new Color(255, 255, 255, 100), this.box::isOpen).injectBoolean(true));
    private final ColorSetting itemOutline = this.add(new ColorSetting("ItemOutline", new Color(255, 255, 255, 100), this.box::isOpen).injectBoolean(true));
    private final ColorSetting playerFill = this.add(new ColorSetting("PlayerFill", new Color(255, 255, 255, 100), this.box::isOpen).injectBoolean(true));
    private final ColorSetting playerOutline = this.add(new ColorSetting("PlayerOutline", new Color(255, 255, 255, 100), this.box::isOpen).injectBoolean(true));
    private final ColorSetting chestFill = this.add(new ColorSetting("ChestFill", new Color(255, 198, 123, 100), this.box::isOpen).injectBoolean(false));
    private final ColorSetting chestOutline = this.add(new ColorSetting("ChestOutline", new Color(255, 198, 123, 100), this.box::isOpen).injectBoolean(false));
    private final ColorSetting enderChestFill = this.add(new ColorSetting("EnderChestFill", new Color(255, 100, 255, 100), this.box::isOpen).injectBoolean(false));
    private final ColorSetting enderChestOutline = this.add(new ColorSetting("EnderChestOutline", new Color(255, 100, 255, 100), this.box::isOpen).injectBoolean(false));
    private final ColorSetting shulkerBoxFill = this.add(new ColorSetting("ShulkerBoxFill", new Color(15, 255, 255, 100), this.box::isOpen).injectBoolean(false));
    private final ColorSetting shulkerBoxOutline = this.add(new ColorSetting("ShulkerBoxOutline", new Color(15, 255, 255, 100), this.box::isOpen).injectBoolean(false));
    public final BooleanSetting item = this.add(new BooleanSetting("ItemName", false).setParent());
    public final BooleanSetting customName = this.add(new BooleanSetting("CustomName", false, this.item::isOpen));
    public final BooleanSetting count = this.add(new BooleanSetting("Count", true, this.item::isOpen));
    private final ColorSetting text = this.add(new ColorSetting("Text", new Color(255, 255, 255, 255), this.item::isOpen));
    public final BooleanSetting pearl = this.add(new BooleanSetting("PearlOwner", true));

    public ESP() {
        super("ESP", Module.Category.Render);
        this.setChinese("\u900f\u89c6");
        INSTANCE = this;
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        if (this.item.getValue()) {
            for (class_1297 entity : Alien.THREAD.getEntities()) {
                if (!(entity instanceof class_1542)) continue;
                class_1542 itemEntity = (class_1542)entity;
                int itemCount = itemEntity.method_6983().method_7947();
                String s = this.count.getValue() && itemCount > 1 ? " x" + itemCount : "";
                String name = (this.customName.getValue() ? itemEntity.method_6983().method_7964() : itemEntity.method_6983().method_7909().method_7848()).getString();
                Render3DUtil.drawText3D(name + s, ((IEntity)itemEntity).getDimensions().method_30757(new class_243(MathUtil.interpolate(itemEntity.field_6038, itemEntity.method_23317(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(itemEntity.field_5971, itemEntity.method_23318(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(itemEntity.field_5989, itemEntity.method_23321(), (double)mc.method_60646().method_60637(true)))).method_1009(0.0, 0.1, 0.0).method_1005().method_1031(0.0, 0.5, 0.0), this.text.getValue());
            }
        }
        if (this.box.getValue()) {
            if (this.itemFill.booleanValue || this.playerFill.booleanValue) {
                for (class_1297 entityx : Alien.THREAD.getEntities()) {
                    Color color;
                    if (!(entityx instanceof class_1542) || !this.itemFill.booleanValue && !this.itemOutline.booleanValue) {
                        if (!(entityx instanceof class_1657) || !this.playerFill.booleanValue && !this.playerOutline.booleanValue) continue;
                        color = this.playerFill.getValue();
                        Render3DUtil.draw3DBox(matrixStack, ((IEntity)entityx).getDimensions().method_30757(new class_243(MathUtil.interpolate(entityx.field_6038, entityx.method_23317(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entityx.field_5971, entityx.method_23318(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entityx.field_5989, entityx.method_23321(), (double)mc.method_60646().method_60637(true)))).method_1009(0.0, 0.1, 0.0), color, this.playerOutline.getValue(), this.playerOutline.booleanValue, this.playerFill.booleanValue);
                        continue;
                    }
                    color = this.itemFill.getValue();
                    Render3DUtil.draw3DBox(matrixStack, ((IEntity)entityx).getDimensions().method_30757(new class_243(MathUtil.interpolate(entityx.field_6038, entityx.method_23317(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entityx.field_5971, entityx.method_23318(), (double)mc.method_60646().method_60637(true)), MathUtil.interpolate(entityx.field_5989, entityx.method_23321(), (double)mc.method_60646().method_60637(true)))), color, this.itemOutline.getValue(), this.itemOutline.booleanValue, this.itemFill.booleanValue);
                }
            }
            for (class_2586 blockEntity : BlockUtil.getTileEntities()) {
                class_238 box;
                if (!(blockEntity instanceof class_2595) || !this.chestFill.booleanValue && !this.chestOutline.booleanValue) {
                    if (!(blockEntity instanceof class_2611) || !this.enderChestFill.booleanValue && !this.enderChestOutline.booleanValue) {
                        if (!(blockEntity instanceof class_2627) || !this.shulkerBoxFill.booleanValue && !this.shulkerBoxOutline.booleanValue) {
                            if (!(blockEntity instanceof class_2640) || !this.endPortalFill.booleanValue && !this.endPortalOutline.booleanValue) continue;
                            box = new class_238(blockEntity.method_11016());
                            Render3DUtil.draw3DBox(matrixStack, box, this.endPortalFill.getValue(), this.endPortalOutline.getValue(), this.endPortalOutline.booleanValue, this.endPortalFill.booleanValue);
                            continue;
                        }
                        box = new class_238(blockEntity.method_11016());
                        Render3DUtil.draw3DBox(matrixStack, box, this.shulkerBoxFill.getValue(), this.shulkerBoxOutline.getValue(), this.shulkerBoxOutline.booleanValue, this.shulkerBoxFill.booleanValue);
                        continue;
                    }
                    box = new class_238(blockEntity.method_11016());
                    Render3DUtil.draw3DBox(matrixStack, box, this.enderChestFill.getValue(), this.enderChestOutline.getValue(), this.enderChestOutline.booleanValue, this.enderChestFill.booleanValue);
                    continue;
                }
                box = new class_238(blockEntity.method_11016());
                Render3DUtil.draw3DBox(matrixStack, box, this.chestFill.getValue(), this.chestOutline.getValue(), this.chestOutline.booleanValue, this.chestFill.booleanValue);
            }
        }
    }

    @EventListener
    public void onReceivePacket(EntitySpawnedEvent event) {
        class_1297 class_12972;
        if (!ESP.nullCheck() && this.pearl.getValue() && (class_12972 = event.getEntity()) instanceof class_1684) {
            class_1684 pearlEntity = (class_1684)class_12972;
            if (pearlEntity.method_24921() != null) {
                pearlEntity.method_5665(pearlEntity.method_24921().method_5477());
                pearlEntity.method_5880(true);
            } else {
                ESP.mc.field_1687.method_18456().stream().min(Comparator.comparingDouble(p -> p.method_19538().method_1022(new class_243(pearlEntity.method_23317(), pearlEntity.method_23318(), pearlEntity.method_23321())))).ifPresent(player -> {
                    pearlEntity.method_5665(player.method_5477());
                    pearlEntity.method_5880(true);
                });
            }
        }
    }
}

