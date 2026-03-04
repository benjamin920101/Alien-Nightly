/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.resource.language.I18n
 *  net.minecraft.item.Item
 *  net.minecraft.block.Block
 *  net.minecraft.client.render.BufferRenderer
 *  net.minecraft.client.render.BufferBuilder
 *  net.minecraft.client.render.Tessellator
 *  net.minecraft.client.render.VertexFormats
 *  net.minecraft.client.render.VertexFormat$DrawMode
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.util.StringHelper
 *  net.minecraft.client.util.InputUtil
 *  net.minecraft.client.render.GameRenderer
 *  net.minecraft.registry.Registries
 *  net.minecraft.client.render.BuiltBuffer
 */
package dev.luminous.mod.gui.windows.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.core.Manager;
import dev.luminous.core.impl.CleanerManager;
import dev.luminous.core.impl.FontManager;
import dev.luminous.core.impl.TradeManager;
import dev.luminous.core.impl.XrayManager;
import dev.luminous.mod.gui.items.buttons.StringButton;
import dev.luminous.mod.gui.windows.WindowBase;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.StringHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.registry.Registries;
import net.minecraft.client.render.BuiltBuffer;

public class ItemSelectWindow
extends WindowBase {
    private final Manager manager;
    private final ArrayList<ItemPlate> itemPlates = new ArrayList();
    private final ArrayList<ItemPlate> allItems = new ArrayList();
    private boolean allTab = true;
    private boolean listening = false;
    private String search = "Search";

    public ItemSelectWindow(Manager manager) {
        this((float)Wrapper.mc.method_22683().method_4486() / 2.0f - 100.0f, (float)Wrapper.mc.method_22683().method_4502() / 2.0f - 150.0f, 200.0f, 300.0f, manager);
    }

    public ItemSelectWindow(float x, float y, float width, float height, Manager manager) {
        super(x, y, width, height, "Items", null);
        this.manager = manager;
        this.refreshItemPlates();
        int id1 = 0;
        for (class_2248 block : class_7923.field_41175) {
            this.allItems.add(new ItemPlate(id1, id1 * 20, block.method_8389(), block.method_9539()));
            ++id1;
        }
        for (class_1792 item : class_7923.field_41178) {
            this.allItems.add(new ItemPlate(id1, id1 * 20, item, item.method_7876()));
            ++id1;
        }
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY) {
        super.render(context, mouseX, mouseY);
        boolean hover1 = Render2DUtil.isHovered(mouseX, mouseY, this.getX() + this.getWidth() - 90.0f, this.getY() + 3.0f, 70.0, 10.0);
        Render2DUtil.drawRect(context.method_51448(), this.getX() + this.getWidth() - 90.0f, this.getY() + 3.0f, 70.0f, 10.0f, hover1 ? new Color(-981236861, true) : new Color(-984131753, true));
        FontManager.small.drawString(context.method_51448(), this.search, (double)(this.getX() + this.getWidth() - 86.0f), (double)(this.getY() + 7.0f), new Color(0xD5D5D5).getRGB());
        RenderSystem.setShader(class_757::method_34540);
        int tabColor1 = this.allTab ? new Color(0xD5D5D5).getRGB() : Color.GRAY.getRGB();
        int tabColor2 = this.allTab ? Color.GRAY.getRGB() : new Color(0xBDBDBD).getRGB();
        class_287 bufferBuilder = class_289.method_1348().method_60827(class_293.class_5596.field_29345, class_290.field_1576);
        bufferBuilder.method_22912(this.getX() + 1.5f, this.getY() + 29.0f, 0.0f).method_39415(Color.DARK_GRAY.getRGB());
        bufferBuilder.method_22912(this.getX() + 8.0f, this.getY() + 29.0f, 0.0f).method_39415(tabColor1);
        bufferBuilder.method_22912(this.getX() + 8.0f, this.getY() + 19.0f, 0.0f).method_39415(tabColor1);
        bufferBuilder.method_22912(this.getX() + 48.0f, this.getY() + 19.0f, 0.0f).method_39415(tabColor1);
        bufferBuilder.method_22912(this.getX() + 54.0f, this.getY() + 29.0f, 0.0f).method_39415(tabColor1);
        bufferBuilder.method_22912(this.getX() + 52.0f, this.getY() + 25.0f, 0.0f).method_39415(tabColor2);
        bufferBuilder.method_22912(this.getX() + 52.0f, this.getY() + 19.0f, 0.0f).method_39415(tabColor2);
        bufferBuilder.method_22912(this.getX() + 92.0f, this.getY() + 19.0f, 0.0f).method_39415(tabColor2);
        bufferBuilder.method_22912(this.getX() + 100.0f, this.getY() + 29.0f, 0.0f).method_39415(Color.GRAY.getRGB());
        bufferBuilder.method_22912(this.getX() + this.getWidth() - 1.0f, this.getY() + 29.0f, 0.0f).method_39415(Color.DARK_GRAY.getRGB());
        class_286.method_43433((class_9801)bufferBuilder.method_60800());
        FontManager.small.drawString(context.method_51448(), "All", (double)(this.getX() + 25.0f), (double)(this.getY() + 25.0f), tabColor1);
        FontManager.small.drawString(context.method_51448(), "Selected", (double)(this.getX() + 60.0f), (double)(this.getY() + 25.0f), tabColor2);
        if (!this.allTab && this.itemPlates.isEmpty()) {
            FontManager.ui.drawCenteredString(context.method_51448(), "It's empty here yet", (double)(this.getX() + this.getWidth() / 2.0f), (double)(this.getY() + this.getHeight() / 2.0f), new Color(0xBDBDBD).getRGB());
        }
        context.method_44379((int)this.getX(), (int)(this.getY() + 30.0f), (int)(this.getX() + this.getWidth()), (int)(this.getY() + this.getHeight() - 1.0f));
        for (ItemPlate itemPlate : this.allTab ? this.allItems : this.itemPlates) {
            if (itemPlate.offset + this.getY() + 25.0f + this.getScrollOffset() > this.getY() + this.getHeight() || itemPlate.offset + this.getScrollOffset() + this.getY() + 10.0f < this.getY()) continue;
            context.method_51448().method_22903();
            context.method_51448().method_46416(this.getX() + 6.0f, itemPlate.offset + this.getY() + 32.0f + this.getScrollOffset(), 0.0f);
            context.method_51427(itemPlate.item().method_7854(), 0, 0);
            context.method_51448().method_22909();
            FontManager.ui.drawString(context.method_51448(), class_1074.method_4662((String)itemPlate.key(), (Object[])new Object[0]), (double)(this.getX() + 26.0f), (double)(itemPlate.offset + this.getY() + 38.0f + this.getScrollOffset()), new Color(0xBDBDBD).getRGB());
            boolean hover2 = Render2DUtil.isHovered(mouseX, mouseY, this.getX() + this.getWidth() - 20.0f, itemPlate.offset + this.getY() + 35.0f + this.getScrollOffset(), 11.0, 11.0);
            Render2DUtil.drawRect(context.method_51448(), this.getX() + this.getWidth() - 20.0f, itemPlate.offset + this.getY() + 35.0f + this.getScrollOffset(), 11.0f, 11.0f, hover2 ? new Color(-981828998, true) : new Color(-984131753, true));
            boolean selected = this.itemPlates.stream().anyMatch(sI -> Objects.equals(sI.key, itemPlate.key));
            if (this.allTab && !selected) {
                FontManager.ui.drawString(context.method_51448(), "+", (double)(this.getX() + this.getWidth() - 17.0f), (double)(itemPlate.offset + this.getY() + 37.0f + this.getScrollOffset()), -1);
                continue;
            }
            FontManager.ui.drawString(context.method_51448(), "-", (double)(this.getX() + this.getWidth()) - 16.5, (double)(itemPlate.offset + this.getY()) + 37.5 + (double)this.getScrollOffset(), -1);
        }
        this.setMaxElementsHeight((this.allTab ? this.allItems : this.itemPlates).size() * 20);
        context.method_44380();
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (Render2DUtil.isHovered(mouseX, mouseY, this.getX() + 8.0f, this.getY() + 19.0f, 52.0, 19.0)) {
            this.allTab = true;
            this.resetScroll();
        }
        if (Render2DUtil.isHovered(mouseX, mouseY, this.getX() + 54.0f, this.getY() + 19.0f, 70.0, 19.0)) {
            this.allTab = false;
            this.resetScroll();
        }
        if (Render2DUtil.isHovered(mouseX, mouseY, this.getX() + this.getWidth() - 90.0f, this.getY() + 3.0f, 70.0, 10.0)) {
            this.listening = true;
            this.search = "";
        }
        for (ItemPlate itemPlate : Lists.newArrayList(this.allTab ? this.allItems : this.itemPlates)) {
            XrayManager mxx;
            Manager manager;
            if ((float)((int)(itemPlate.offset + this.getY() + 50.0f)) + this.getScrollOffset() > this.getY() + this.getHeight()) continue;
            String name = itemPlate.key().replace("item.minecraft.", "").replace("block.minecraft.", "");
            if (!Render2DUtil.isHovered(mouseX, mouseY, this.getX() + this.getWidth() - 20.0f, itemPlate.offset + this.getY() + 35.0f + this.getScrollOffset(), 10.0, 10.0)) continue;
            boolean selected = this.itemPlates.stream().anyMatch(sI -> Objects.equals(sI.key(), itemPlate.key));
            if (this.allTab && !selected) {
                manager = this.manager;
                if (manager instanceof TradeManager) {
                    TradeManager m = (TradeManager)manager;
                    if (m.inWhitelist(name)) continue;
                    m.add(name);
                    this.refreshItemPlates();
                    continue;
                }
                manager = this.manager;
                if (manager instanceof CleanerManager) {
                    CleanerManager mx = (CleanerManager)manager;
                    if (mx.inList(name)) continue;
                    mx.add(name);
                    this.refreshItemPlates();
                    continue;
                }
                manager = this.manager;
                if (!(manager instanceof XrayManager) || (mxx = (XrayManager)manager).inWhitelist(name)) continue;
                mxx.add(name);
                this.refreshItemPlates();
                continue;
            }
            manager = this.manager;
            if (manager instanceof TradeManager) {
                TradeManager mxx2 = (TradeManager)manager;
                mxx2.remove(name);
            } else {
                manager = this.manager;
                if (manager instanceof CleanerManager) {
                    CleanerManager mxx3 = (CleanerManager)manager;
                    mxx3.remove(name);
                } else {
                    manager = this.manager;
                    if (manager instanceof XrayManager) {
                        mxx = (XrayManager)manager;
                        mxx.remove(name);
                    }
                }
            }
            this.refreshItemPlates();
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode != 70 || !class_3675.method_15987((long)Wrapper.mc.method_22683().method_4490(), (int)341) && !class_3675.method_15987((long)Wrapper.mc.method_22683().method_4490(), (int)345)) {
            if (this.listening) {
                switch (keyCode) {
                    case 32: {
                        this.search = this.search + " ";
                        break;
                    }
                    case 256: {
                        this.listening = false;
                        this.search = "Search";
                        this.refreshAllItems();
                        break;
                    }
                    case 259: {
                        this.search = StringButton.removeLastChar(this.search);
                        this.refreshAllItems();
                        if (!Objects.equals(this.search, "")) break;
                        this.listening = false;
                        this.search = "Search";
                    }
                }
            }
        } else {
            this.listening = !this.listening;
        }
    }

    @Override
    public void charTyped(char key, int keyCode) {
        if (class_3544.method_57175((char)key) && this.listening) {
            this.search = this.search + key;
            this.refreshAllItems();
        }
    }

    private void refreshItemPlates() {
        this.itemPlates.clear();
        int id = 0;
        for (class_1792 item : class_7923.field_41178) {
            XrayManager mxx;
            Manager manager = this.manager;
            if (manager instanceof TradeManager) {
                TradeManager m = (TradeManager)manager;
                if (!m.inWhitelist(item.method_7876())) continue;
                this.itemPlates.add(new ItemPlate(id, id * 20, item.method_8389(), item.method_7876()));
                ++id;
                continue;
            }
            manager = this.manager;
            if (manager instanceof CleanerManager) {
                CleanerManager mx = (CleanerManager)manager;
                if (!mx.inList(item.method_7876())) continue;
                this.itemPlates.add(new ItemPlate(id, id * 20, item.method_8389(), item.method_7876()));
                ++id;
                continue;
            }
            manager = this.manager;
            if (!(manager instanceof XrayManager) || !(mxx = (XrayManager)manager).inWhitelist(item.method_7876())) continue;
            this.itemPlates.add(new ItemPlate(id, id * 20, item.method_8389(), item.method_7876()));
            ++id;
        }
    }

    private void refreshAllItems() {
        this.allItems.clear();
        this.resetScroll();
        int id1 = 0;
        for (class_1792 item : class_7923.field_41178) {
            if (!this.search.equals("Search") && !this.search.isEmpty() && !item.method_7876().contains(this.search) && !item.method_7848().getString().toLowerCase().contains(this.search.toLowerCase())) continue;
            this.allItems.add(new ItemPlate(id1, id1 * 20, item, item.method_7876()));
            ++id1;
        }
    }

    private record ItemPlate(float id, float offset, class_1792 item, String key) {
    }
}

