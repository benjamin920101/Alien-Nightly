/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1747
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_2371
 *  net.minecraft.class_2480
 *  net.minecraft.class_2487
 *  net.minecraft.class_2499
 *  net.minecraft.class_308
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_742
 *  net.minecraft.class_9279
 *  net.minecraft.class_9323
 *  net.minecraft.class_9334
 *  org.lwjgl.opengl.GL11
 *  org.spongepowered.asm.mixin.Unique
 */
package dev.luminous.mod.modules.impl.misc;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.asm.accessors.IContainerComponent;
import dev.luminous.core.impl.PlayerManager;
import dev.luminous.mod.gui.PeekScreen;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2371;
import net.minecraft.class_2480;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_308;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_7225;
import net.minecraft.class_742;
import net.minecraft.class_9279;
import net.minecraft.class_9323;
import net.minecraft.class_9334;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Unique;

public class ShulkerViewer
extends Module {
    private static final class_1799[] ITEMS = new class_1799[27];
    public static ShulkerViewer INSTANCE;
    private static int offset;
    public final BooleanSetting toolTips = this.add(new BooleanSetting("ToolTips", true));
    public final BooleanSetting icon = this.add(new BooleanSetting("Icon", true));
    private final HashMap<UUID, Shulker> map = new HashMap();
    private final BooleanSetting peek = this.add(new BooleanSetting("Peek", false).setParent());
    private final SliderSetting renderTime = this.add(new SliderSetting("RenderTime", 10.0, 0.0, 100.0, 0.1, this.peek::isOpen).setSuffix("s"));
    private final SliderSetting xOffset = this.add(new SliderSetting("X", 0, 0, 1500, this.peek::isOpen));
    private final SliderSetting yOffset = this.add(new SliderSetting("Y", 120, 0, 1000, this.peek::isOpen));
    private final SliderSetting space = this.add(new SliderSetting("Space", 78.0, 0.0, 200.0, 1.0, this.peek::isOpen));

    public ShulkerViewer() {
        super("ShulkerViewer", Module.Category.Misc);
        this.setChinese("\u6f5c\u5f71\u76d2\u67e5\u770b");
        INSTANCE = this;
    }

    public static void renderShulkerToolTip(class_332 context, int mouseX, int mouseY, class_1799 stack) {
        ShulkerViewer.getItemsInContainerItem(stack, ITEMS);
        ShulkerViewer.draw(context, mouseX, mouseY);
    }

    @Unique
    private static void draw(class_332 context, int mouseX, int mouseY) {
        RenderSystem.disableDepthTest();
        GL11.glClear((int)256);
        Render2DUtil.drawRect(context.method_51448(), (float)(mouseX += 8), (float)(mouseY -= 82), 176.0f, 67.0f, new Color(0, 0, 0, 120));
        class_308.method_24211();
        int row = 0;
        int i = 0;
        for (class_1799 itemStack : ITEMS) {
            context.method_51427(itemStack, mouseX + 8 + i * 18, mouseY + 7 + row * 18);
            context.method_51431(ShulkerViewer.mc.field_1772, itemStack, mouseX + 8 + i * 18, mouseY + 7 + row * 18);
            if (++i < 9) continue;
            i = 0;
            ++row;
        }
        class_308.method_24210();
        RenderSystem.enableDepthTest();
    }

    public static boolean hasItems(class_1799 itemStack) {
        IContainerComponent container = (IContainerComponent)itemStack.method_57824(class_9334.field_49622);
        if (container != null && !container.getStacks().isEmpty()) {
            return true;
        }
        class_2487 compoundTag = ((class_9279)itemStack.method_57825(class_9334.field_49611, (Object)class_9279.field_49302)).method_57463();
        return compoundTag != null && compoundTag.method_10573("Items", 9);
    }

    public static void getItemsInContainerItem(class_1799 itemStack, class_1799[] items) {
        block4: {
            class_9279 nbt2;
            class_9323 components;
            block5: {
                block3: {
                    if (itemStack.method_7909() != class_1802.field_8466) break block3;
                    for (int i = 0; i < Alien.PLAYER.ENDERCHEST_ITEM.size(); ++i) {
                        items[i] = (class_1799)Alien.PLAYER.ENDERCHEST_ITEM.get(i);
                    }
                    break block4;
                }
                Arrays.fill(items, class_1799.field_8037);
                components = itemStack.method_57353();
                if (!components.method_57832(class_9334.field_49622)) break block5;
                IContainerComponent container = (IContainerComponent)components.method_57829(class_9334.field_49622);
                class_2371<class_1799> stacks = container.getStacks();
                for (int i = 0; i < stacks.size(); ++i) {
                    if (i < 0 || i >= items.length) continue;
                    items[i] = (class_1799)stacks.get(i);
                }
                break block4;
            }
            if (!components.method_57832(class_9334.field_49611) || !(nbt2 = (class_9279)components.method_57829(class_9334.field_49611)).method_57450("Items")) break block4;
            class_2499 nbt3 = (class_2499)nbt2.method_57463().method_10580("Items");
            for (int ix = 0; ix < nbt3.size(); ++ix) {
                byte slot = nbt3.method_10602(ix).method_10571("Slot");
                if (slot < 0 || slot >= items.length) continue;
                items[slot] = class_1799.method_57359((class_7225.class_7874)ShulkerViewer.mc.field_1724.method_56673(), (class_2487)nbt3.method_10602(ix));
            }
        }
    }

    public static boolean openContainer(class_1799 itemStack, class_1799[] contents, boolean pause) {
        if (!ShulkerViewer.hasItems(itemStack) && itemStack.method_7909() != class_1802.field_8466) {
            return false;
        }
        ShulkerViewer.getItemsInContainerItem(itemStack, contents);
        if (pause) {
            PlayerManager.screenToOpen = new PeekScreen(itemStack, contents);
        } else {
            mc.method_1507((class_437)new PeekScreen(itemStack, contents));
        }
        return true;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.peek.getValue()) {
            for (class_742 player : Alien.THREAD.getPlayers()) {
                class_1747 blockItem;
                class_1799 stack = player.method_6047();
                class_1792 class_17922 = stack.method_7909();
                if (!(class_17922 instanceof class_1747) || !((blockItem = (class_1747)class_17922).method_7711() instanceof class_2480)) {
                    stack = player.method_6079();
                }
                if (!((class_17922 = stack.method_7909()) instanceof class_1747) || !((blockItem = (class_1747)class_17922).method_7711() instanceof class_2480)) continue;
                this.map.put(player.method_7334().getId(), new Shulker(stack, player.method_7334().getName()));
            }
        }
    }

    @Override
    public void onRender2D(class_332 drawContext, float tickDelta) {
        if (this.peek.getValue()) {
            offset = 0;
            this.map.values().removeIf(shulker -> shulker.draw(drawContext));
        }
    }

    class Shulker {
        final class_1799 itemStack;
        final String name;
        private final Timer timer;

        public Shulker(class_1799 itemStack, String name) {
            this.itemStack = itemStack;
            this.timer = new Timer();
            this.name = name;
        }

        public boolean draw(class_332 context) {
            if (this.timer.passedS(ShulkerViewer.this.renderTime.getValue())) {
                return true;
            }
            ShulkerViewer.renderShulkerToolTip(context, ShulkerViewer.this.xOffset.getValueInt() - 8, ShulkerViewer.this.yOffset.getValueInt() + offset, this.itemStack);
            context.method_51433(Wrapper.mc.field_1772, this.name, ShulkerViewer.this.xOffset.getValueInt(), ShulkerViewer.this.yOffset.getValueInt() + offset - 9 - 82, -1, true);
            offset += ShulkerViewer.this.space.getValueInt();
            return false;
        }
    }
}

