/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.item.ItemConvertible
 *  net.minecraft.block.PistonBlock
 *  net.minecraft.client.gui.DrawContext
 */
package dev.luminous.mod.modules.impl.client;

import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ItemConvertible;
import net.minecraft.block.PistonBlock;
import net.minecraft.client.gui.DrawContext;

public class ItemsCounter
extends Module {
    public static ItemsCounter INSTANCE;
    private final BooleanSetting hideEmpty = this.add(new BooleanSetting("HideEmpty", true));
    private final BooleanSetting crystal = this.add(new BooleanSetting("Crystal", true));
    private final BooleanSetting xp = this.add(new BooleanSetting("XP", true));
    private final BooleanSetting pearl = this.add(new BooleanSetting("Pearl", true));
    private final BooleanSetting obsidian = this.add(new BooleanSetting("Obsidian", true));
    private final BooleanSetting egApple = this.add(new BooleanSetting("E-GApple", true));
    private final BooleanSetting gApple = this.add(new BooleanSetting("GApple", true));
    private final BooleanSetting totem = this.add(new BooleanSetting("Totem", true));
    private final BooleanSetting web = this.add(new BooleanSetting("Web", true));
    private final BooleanSetting anchor = this.add(new BooleanSetting("Anchor", true));
    private final BooleanSetting glowstone = this.add(new BooleanSetting("Glowstone", true));
    private final BooleanSetting piston = this.add(new BooleanSetting("Piston", true));
    private final BooleanSetting redstone = this.add(new BooleanSetting("RedStone", true));
    private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true));
    private final BooleanSetting firework = this.add(new BooleanSetting("Firework", true));
    private final SliderSetting xOffset = this.add(new SliderSetting("X", 100, 0, 1500));
    private final SliderSetting yOffset = this.add(new SliderSetting("Y", 100, 0, 1000));
    private final SliderSetting offset = this.add(new SliderSetting("Offset", 18, 0, 30));
    private final class_1799 crystalStack = new class_1799((class_1935)class_1802.field_8301);
    private final class_1799 xpStack = new class_1799((class_1935)class_1802.field_8287);
    private final class_1799 pearlStack = new class_1799((class_1935)class_1802.field_8634);
    private final class_1799 obsidianStack = new class_1799((class_1935)class_1802.field_8281);
    private final class_1799 eGappleStack = new class_1799((class_1935)class_1802.field_8367);
    private final class_1799 gappleStack = new class_1799((class_1935)class_1802.field_8463);
    private final class_1799 totemStack = new class_1799((class_1935)class_1802.field_8288);
    private final class_1799 webStack = new class_1799((class_1935)class_1802.field_8786);
    private final class_1799 anchorStack = new class_1799((class_1935)class_1802.field_23141);
    private final class_1799 glowstoneStack = new class_1799((class_1935)class_1802.field_8801);
    private final class_1799 pistonStack = new class_1799((class_1935)class_1802.field_8249);
    private final class_1799 redstoneStack = new class_1799((class_1935)class_1802.field_8793);
    private final class_1799 enderChestStack = new class_1799((class_1935)class_1802.field_8466);
    private final class_1799 fireworkStack = new class_1799((class_1935)class_1802.field_8639);
    int x;
    int y;
    class_332 drawContext;

    public ItemsCounter() {
        super("Items", Module.Category.Client);
        this.setChinese("\u7269\u54c1\u6570\u91cf");
        INSTANCE = this;
    }

    @Override
    public void onRender2D(class_332 drawContext, float tickDelta) {
        int pistonCount;
        this.drawContext = drawContext;
        this.x = this.xOffset.getValueInt() - this.offset.getValueInt();
        this.y = this.yOffset.getValueInt();
        if (this.crystal.getValue()) {
            this.crystalStack.method_7939(this.getItemCount(class_1802.field_8301));
            this.drawItem(this.crystalStack);
        }
        if (this.xp.getValue()) {
            this.xpStack.method_7939(this.getItemCount(class_1802.field_8287));
            this.drawItem(this.xpStack);
        }
        if (this.pearl.getValue()) {
            this.pearlStack.method_7939(this.getItemCount(class_1802.field_8634));
            this.drawItem(this.pearlStack);
        }
        if (this.obsidian.getValue()) {
            this.obsidianStack.method_7939(this.getItemCount(class_1802.field_8281));
            this.drawItem(this.obsidianStack);
        }
        if (this.egApple.getValue()) {
            this.eGappleStack.method_7939(this.getItemCount(class_1802.field_8367));
            this.drawItem(this.eGappleStack);
        }
        if (this.gApple.getValue()) {
            this.gappleStack.method_7939(this.getItemCount(class_1802.field_8463));
            this.drawItem(this.gappleStack);
        }
        if (this.totem.getValue()) {
            this.totemStack.method_7939(this.getItemCount(class_1802.field_8288));
            this.drawItem(this.totemStack);
        }
        if (this.web.getValue()) {
            this.webStack.method_7939(this.getItemCount(class_1802.field_8786));
            this.drawItem(this.webStack);
        }
        if (this.anchor.getValue()) {
            this.anchorStack.method_7939(this.getItemCount(class_1802.field_23141));
            this.drawItem(this.anchorStack);
        }
        if (this.glowstone.getValue()) {
            this.glowstoneStack.method_7939(this.getItemCount(class_1802.field_8801));
            this.drawItem(this.glowstoneStack);
        }
        if (this.piston.getValue() && ((pistonCount = InventoryUtil.getItemCount(class_2665.class)) > 0 || !this.hideEmpty.getValue())) {
            this.x += this.offset.getValueInt();
            this.pistonStack.method_7939(Math.max(1, pistonCount));
            this.drawItem(this.pistonStack);
        }
        if (this.redstone.getValue()) {
            this.redstoneStack.method_7939(this.getItemCount(class_1802.field_8793));
            this.drawItem(this.redstoneStack);
        }
        if (this.enderChest.getValue()) {
            this.enderChestStack.method_7939(this.getItemCount(class_1802.field_8466));
            this.drawItem(this.enderChestStack);
        }
        if (this.firework.getValue()) {
            this.fireworkStack.method_7939(this.getItemCount(class_1802.field_8639));
            this.drawItem(this.fireworkStack);
        }
    }

    private int getItemCount(class_1792 item) {
        int i = InventoryUtil.getItemCount(item);
        if (this.hideEmpty.getValue() && i == 0) {
            return 0;
        }
        this.x += this.offset.getValueInt();
        return Math.max(i, 1);
    }

    private void drawItem(class_1799 itemStack) {
        this.drawContext.method_51427(itemStack, this.x, this.y);
        this.drawContext.method_51431(ItemsCounter.mc.field_1772, itemStack, this.x, this.y);
    }
}

