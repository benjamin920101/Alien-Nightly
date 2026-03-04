/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.screen.ScreenHandler
 *  net.minecraft.screen.GenericContainerScreenHandler
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.screen.ShulkerBoxScreenHandler
 *  net.minecraft.screen.slot.Slot
 *  net.minecraft.item.BlockItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.world.BlockView
 *  net.minecraft.block.Blocks
 *  net.minecraft.block.Block
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.block.ShulkerBoxBlock
 *  net.minecraft.block.BlockState
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen
 *  net.minecraft.registry.Registries
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.core.Manager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.BlockView;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.registry.Registries;

public class ChestStealer
extends Module {
    private final StringSetting kit = this.add(new StringSetting("Kit", "1"));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
    private final SliderSetting disableTime = this.add(new SliderSetting("DisableTime", 500, 0, 1000));
    private final SliderSetting range = this.add(new SliderSetting("Range", 4.0, 0.0, 6.0, 0.1));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting mine = this.add(new BooleanSetting("Mine", true));
    private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", false));
    private final BindSetting placeKey = this.add(new BindSetting("PlaceKey", -1));
    private boolean wasPlaceKeyPressed = false;
    private final Timer timer = new Timer();
    private final Timer timeoutTimer = new Timer();
    private final Map<Integer, class_1792> desiredInventory = new HashMap<Integer, class_1792>();
    private class_2338 placePos = null;
    private class_2338 openPos = null;
    private boolean opend = false;
    private final List<class_2338> openList = new ArrayList<class_2338>();

    public ChestStealer() {
        super("ChestStealer", Module.Category.Misc);
        this.setChinese("\u66f4\u597d\u7684\u81ea\u52a8\u8865\u7ed9");
    }

    @Override
    public boolean onEnable() {
        this.opend = false;
        this.openPos = null;
        this.placePos = null;
        this.timeoutTimer.reset();
        this.desiredInventory.clear();
        this.loadKit();
        if (this.desiredInventory.isEmpty()) {
            this.sendMessage("\u00a7cKit is empty or not found!");
            this.disable();
            return false;
        }
        return false;
    }

    @Override
    public void onDisable() {
        this.opend = false;
        if (this.mine.getValue() && this.placePos != null) {
            SpeedMine.INSTANCE.mine(this.placePos);
        }
        this.placePos = null;
        this.openPos = null;
    }

    private void loadKit() {
        try {
            String line;
            File file = Manager.getFile(this.kit.getValue() + ".kit");
            if (!file.exists()) {
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)new FileInputStream(file), StandardCharsets.UTF_8));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length < 2) continue;
                try {
                    int slot = Integer.parseInt(parts[0]);
                    String key = parts[1];
                    class_1792 item = class_1802.field_8162;
                    for (class_1792 i : class_7923.field_41178) {
                        if (!i.method_7876().equals(key)) continue;
                        item = i;
                        break;
                    }
                    if (item == class_1802.field_8162) continue;
                    this.desiredInventory.put(slot, item);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doPlace() {
        int oldSlot = ChestStealer.mc.field_1724.method_31548().field_7545;
        double distance = 100.0;
        class_2338 bestPos = null;
        for (class_2338 pos : BlockUtil.getSphere((float)this.range.getValue())) {
            if (!ChestStealer.mc.field_1687.method_22347(pos.method_10084())) continue;
            class_2338 belowPos = pos.method_10093(class_2350.field_11033);
            class_2680 belowState = ChestStealer.mc.field_1687.method_8320(belowPos);
            if (!BlockUtil.clientCanPlace(pos, false) || !BlockUtil.isStrictDirection(belowPos, class_2350.field_11036) || !BlockUtil.canClick(belowPos) || belowState.method_26215() || !belowState.method_26212((class_1922)ChestStealer.mc.field_1687, belowPos) || bestPos != null && !((double)class_3532.method_15355((float)((float)ChestStealer.mc.field_1724.method_5707(pos.method_46558()))) < distance)) continue;
            distance = class_3532.method_15355((float)((float)ChestStealer.mc.field_1724.method_5707(pos.method_46558())));
            bestPos = pos;
        }
        if (bestPos != null) {
            int shulkerSlot = this.findShulker();
            if (shulkerSlot == -1) {
                this.sendMessage(this.enderChest.getValue() ? "\u00a74No shulkerbox or enderchest found." : "\u00a74No shulkerbox found.");
                this.disable();
                return;
            }
            if (this.inventory.getValue()) {
                InventoryUtil.inventorySwap(shulkerSlot, oldSlot);
                this.placeBlock(bestPos);
                this.placePos = bestPos;
                InventoryUtil.inventorySwap(shulkerSlot, oldSlot);
            } else {
                InventoryUtil.switchToSlot(shulkerSlot);
                this.placeBlock(bestPos);
                this.placePos = bestPos;
                InventoryUtil.switchToSlot(oldSlot);
            }
            this.timer.reset();
        } else {
            this.sendMessage("\u00a74No place position found.");
            this.disable();
        }
    }

    private void placeBlock(class_2338 pos) {
        BlockUtil.clickBlock(pos.method_10093(class_2350.field_11033), class_2350.field_11036, this.rotate.getValue());
    }

    private int findShulker() {
        class_1799 stack;
        int i;
        if (this.inventory.getValue()) {
            for (i = 0; i < 36; ++i) {
                class_1747 blockItem;
                class_1792 item;
                stack = ChestStealer.mc.field_1724.method_31548().method_5438(i);
                if (stack.method_7960() || !((item = stack.method_7909()) instanceof class_1747) || !((blockItem = (class_1747)item).method_7711() instanceof class_2480)) continue;
                return i < 9 ? i + 36 : i;
            }
        } else {
            int slot = InventoryUtil.findClass(class_2480.class);
            if (slot != -1) {
                return slot;
            }
        }
        if (this.enderChest.getValue()) {
            if (this.inventory.getValue()) {
                for (i = 0; i < 36; ++i) {
                    stack = ChestStealer.mc.field_1724.method_31548().method_5438(i);
                    if (stack.method_7960() || stack.method_7909() != class_1802.field_8466) continue;
                    return i < 9 ? i + 36 : i;
                }
            } else {
                return InventoryUtil.findItem(class_1802.field_8466);
            }
        }
        return -1;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (ChestStealer.mc.field_1724 == null || ChestStealer.mc.field_1687 == null) {
            return;
        }
        this.openList.removeIf(pos -> {
            class_2248 block = ChestStealer.mc.field_1687.method_8320(pos).method_26204();
            return !(block instanceof class_2480) && block != class_2246.field_10443;
        });
        boolean isInGui = ChestStealer.mc.field_1755 != null;
        boolean isInShulkerGui = ChestStealer.mc.field_1755 instanceof class_495;
        boolean isInEnderChestGui = ChestStealer.mc.field_1724.field_7512 instanceof class_1707;
        if (!isInGui || !isInShulkerGui && !isInEnderChestGui) {
            if (this.opend) {
                this.opend = false;
                this.placePos = null;
                if (this.mine.getValue() && this.openPos != null) {
                    class_2248 block = ChestStealer.mc.field_1687.method_8320(this.openPos).method_26204();
                    if (block instanceof class_2480) {
                        SpeedMine.INSTANCE.mine(this.openPos);
                    } else {
                        this.openPos = null;
                    }
                }
                if (this.autoDisable.getValue()) {
                    this.disable();
                }
            } else if (this.placePos != null) {
                class_2248 block = ChestStealer.mc.field_1687.method_8320(this.placePos).method_26204();
                if (block instanceof class_2480 || block == class_2246.field_10443) {
                    this.openPos = this.placePos;
                    BlockUtil.clickBlock(this.placePos, BlockUtil.getClickSide(this.placePos), this.rotate.getValue());
                }
            } else {
                boolean isPlaceKeyPressed = this.placeKey.isPressed();
                if (isPlaceKeyPressed && !this.wasPlaceKeyPressed && this.findShulker() != -1) {
                    this.doPlace();
                }
                this.wasPlaceKeyPressed = isPlaceKeyPressed;
            }
        } else {
            class_1703 screenHandler;
            this.opend = true;
            if (this.openPos != null) {
                this.openList.add(this.openPos);
            }
            if ((screenHandler = ChestStealer.mc.field_1724.field_7512) instanceof class_1733) {
                class_1733 shulker = (class_1733)screenHandler;
                this.processContainer((class_1703)shulker, shulker.field_7763);
            } else {
                screenHandler = ChestStealer.mc.field_1724.field_7512;
                if (screenHandler instanceof class_1707) {
                    class_1707 container = (class_1707)screenHandler;
                    class_2248 block = this.openPos != null ? ChestStealer.mc.field_1687.method_8320(this.openPos).method_26204() : null;
                    if ((block = block) == class_2246.field_10443) {
                        this.processContainer((class_1703)container, container.field_7763);
                    }
                }
            }
        }
    }

    private void processContainer(class_1703 handler, int syncId) {
        boolean stoleInThisIteration;
        boolean hasStolen = false;
        do {
            stoleInThisIteration = false;
            block1: for (Map.Entry<Integer, class_1792> entry : this.desiredInventory.entrySet()) {
                int targetSlot = entry.getKey();
                class_1792 targetItem = entry.getValue();
                class_1799 playerStack = ChestStealer.mc.field_1724.method_31548().method_5438(targetSlot);
                boolean needItem = false;
                if (playerStack.method_7960()) {
                    needItem = true;
                } else if (playerStack.method_7909() != targetItem) {
                    needItem = true;
                } else if (playerStack.method_7947() < playerStack.method_7914()) {
                    needItem = true;
                }
                if (!needItem) continue;
                for (class_1735 slot : handler.field_7761) {
                    if (slot.field_7874 >= 27 || slot.method_7677().method_7960() || slot.method_7677().method_7909() != targetItem) continue;
                    int windowSlotId = (targetSlot < 9 ? targetSlot + 36 : targetSlot) + 18;
                    ChestStealer.mc.field_1761.method_2906(syncId, slot.field_7874, 0, class_1713.field_7790, (class_1657)ChestStealer.mc.field_1724);
                    ChestStealer.mc.field_1761.method_2906(syncId, windowSlotId, 0, class_1713.field_7790, (class_1657)ChestStealer.mc.field_1724);
                    if (!ChestStealer.mc.field_1724.field_7512.method_34255().method_7960()) {
                        ChestStealer.mc.field_1761.method_2906(syncId, slot.field_7874, 0, class_1713.field_7790, (class_1657)ChestStealer.mc.field_1724);
                    }
                    hasStolen = true;
                    stoleInThisIteration = true;
                    continue block1;
                }
            }
        } while (stoleInThisIteration);
        if (this.isKitted()) {
            ChestStealer.mc.field_1724.method_7346();
        }
    }

    private boolean isKitted() {
        for (Map.Entry<Integer, class_1792> entry : this.desiredInventory.entrySet()) {
            int slot = entry.getKey();
            class_1792 item = entry.getValue();
            class_1799 stack = ChestStealer.mc.field_1724.method_31548().method_5438(slot);
            if (stack.method_7909() != item) {
                return false;
            }
            if (stack.method_7960() || stack.method_7947() >= stack.method_7914()) continue;
            return false;
        }
        return true;
    }

    private void timeoutToDisable() {
        if (this.timeoutTimer.passed(this.disableTime.getValueInt())) {
            this.disable();
        }
    }
}

