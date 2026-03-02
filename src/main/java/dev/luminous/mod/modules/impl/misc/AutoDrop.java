/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1738
 *  net.minecraft.class_1747
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_2480
 *  org.apache.commons.io.IOUtils
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.core.Manager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1738;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2480;
import org.apache.commons.io.IOUtils;

public class AutoDrop
extends Module {
    public static AutoDrop INSTANCE;
    private final SliderSetting tasksPerTick = this.add(new SliderSetting("TasksPerTick", 2, 1, 20));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 0.1, 0.0, 5.0, 0.01).setSuffix("s"));
    private final BooleanSetting kit = this.add(new BooleanSetting("Kit", true));
    private final StringSetting kitName = this.add(new StringSetting("KitName", "1"));
    private final BooleanSetting dropWrongItems = this.add(new BooleanSetting("DropWrongItems", true));
    private final BooleanSetting dropExcess = this.add(new BooleanSetting("DropExcess", true));
    private final BooleanSetting ignoreArmor = this.add(new BooleanSetting("IgnoreArmor", true));
    private final BooleanSetting ignoreShulker = this.add(new BooleanSetting("IgnoreShulker", true));
    private final BooleanSetting onlyHotbar = this.add(new BooleanSetting("OnlyHotbar", false));
    private final BooleanSetting dropEnabled = this.add(new BooleanSetting("DropEnabled", true));
    private final BooleanSetting sortItems = this.add(new BooleanSetting("SortItems", true));
    private final BooleanSetting inventoryOnly = this.add(new BooleanSetting("InventoryOnly", false));
    private final Timer timer = new Timer();
    private final Map<Integer, String> kitMap = new ConcurrentHashMap<Integer, String>();

    public AutoDrop() {
        super("AutoDrop", Module.Category.Misc);
        this.setChinese("\u81ea\u52a8\u4e22\u5f03");
        INSTANCE = this;
    }

    @Override
    public boolean onEnable() {
        if (!AutoDrop.nullCheck()) {
            this.loadKit();
        }
        return false;
    }

    private void loadKit() {
        Alien.THREAD.execute(() -> {
            this.kitMap.clear();
            try {
                File file = Manager.getFile(this.kitName.getValue() + ".kit");
                if (!file.exists()) {
                    this.sendMessage("\u00a7cKit file not found: " + this.kitName.getValue() + ".kit");
                    return;
                }
                for (String s : IOUtils.readLines((InputStream)new FileInputStream(file), (Charset)StandardCharsets.UTF_8)) {
                    String[] split = s.split(":");
                    if (split.length != 2) continue;
                    this.kitMap.put(Integer.valueOf(split[0]), split[1]);
                }
                this.sendMessage("\u00a7aLoaded kit: " + this.kitName.getValue() + " with " + this.kitMap.size() + " items");
            }
            catch (Exception e) {
                e.printStackTrace();
                this.sendMessage("\u00a7cError loading kit: " + e.getMessage());
            }
        });
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.timer.passedS(this.delay.getValue())) {
            boolean shouldProcess = this.inventoryOnly.getValue() ? EntityUtil.inInventory() : true;
            boolean bl = shouldProcess;
            if (shouldProcess) {
                int i = 0;
                while ((double)i < this.tasksPerTick.getValue()) {
                    if (this.processSort()) {
                        this.timer.reset();
                        return;
                    }
                    if (this.processDrop()) {
                        this.timer.reset();
                        return;
                    }
                    ++i;
                }
            }
        }
    }

    private boolean processDrop() {
        if (!this.kit.getValue() || this.kitMap.isEmpty() || !this.dropEnabled.getValue()) {
            return false;
        }
        int startSlot = this.onlyHotbar.getValue() ? 0 : 0;
        int endSlot = this.onlyHotbar.getValue() ? 9 : 36;
        for (int slot = startSlot; slot < endSlot; ++slot) {
            class_1747 blockItem;
            class_1792 item;
            class_1799 stack = AutoDrop.mc.field_1724.method_31548().method_5438(slot);
            if (stack.method_7960() || this.ignoreArmor.getValue() && stack.method_7909() instanceof class_1738 || this.ignoreShulker.getValue() && (item = stack.method_7909()) instanceof class_1747 && (blockItem = (class_1747)item).method_7711() instanceof class_2480) continue;
            if (this.kitMap.containsKey(slot)) {
                String expectedItem = this.kitMap.get(slot);
                String actualItem = stack.method_7909().method_7876();
                if (!this.dropWrongItems.getValue() || actualItem.equals(expectedItem)) continue;
                this.dropItem(slot);
                return true;
            }
            if (!this.dropWrongItems.getValue()) continue;
            this.dropItem(slot);
            return true;
        }
        return false;
    }

    private void dropItem(int slot) {
        if (!this.dropEnabled.getValue()) {
            return;
        }
        if (!EntityUtil.inInventory() && this.inventoryOnly.getValue()) {
            return;
        }
        try {
            int syncId = AutoDrop.mc.field_1724.field_7512.field_7763;
            int slotId = slot < 9 ? slot + 36 : slot;
            AutoDrop.mc.field_1761.method_2906(syncId, slotId, 1, class_1713.field_7795, (class_1657)AutoDrop.mc.field_1724);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private boolean processSort() {
        if (!this.kit.getValue() || this.kitMap.isEmpty() || !this.sortItems.getValue()) {
            return false;
        }
        boolean shouldSort = this.inventoryOnly.getValue() ? EntityUtil.inInventory() : true;
        boolean bl = shouldSort;
        if (!shouldSort) {
            return false;
        }
        int startSlot = this.onlyHotbar.getValue() ? 0 : 0;
        int endSlot = this.onlyHotbar.getValue() ? 9 : 36;
        for (int targetSlot = startSlot; targetSlot < endSlot; ++targetSlot) {
            if (!this.kitMap.containsKey(targetSlot)) continue;
            String expectedItem = this.kitMap.get(targetSlot);
            class_1799 currentStack = AutoDrop.mc.field_1724.method_31548().method_5438(targetSlot);
            String currentItem = currentStack.method_7960() ? "" : currentStack.method_7909().method_7876();
            String string = currentItem;
            if (expectedItem.equals(currentItem)) continue;
            for (int sourceSlot = startSlot; sourceSlot < endSlot; ++sourceSlot) {
                String sourceItem;
                class_1799 sourceStack;
                if (sourceSlot == targetSlot || (sourceStack = AutoDrop.mc.field_1724.method_31548().method_5438(sourceSlot)).method_7960() || !(sourceItem = sourceStack.method_7909().method_7876()).equals(expectedItem)) continue;
                this.moveItem(sourceSlot, targetSlot);
                return true;
            }
        }
        return false;
    }

    private void moveItem(int fromSlot, int toSlot) {
        boolean canMove = this.inventoryOnly.getValue() ? EntityUtil.inInventory() : true;
        boolean bl = canMove;
        if (!canMove) {
            return;
        }
        try {
            int syncId = AutoDrop.mc.field_1724.field_7498.field_7763;
            int fromSlotId = fromSlot < 9 ? fromSlot + 36 : fromSlot;
            int toSlotId = toSlot < 9 ? toSlot + 36 : toSlot;
            AutoDrop.mc.field_1761.method_2906(syncId, fromSlotId, 0, class_1713.field_7790, (class_1657)AutoDrop.mc.field_1724);
            AutoDrop.mc.field_1761.method_2906(syncId, toSlotId, 0, class_1713.field_7790, (class_1657)AutoDrop.mc.field_1724);
            AutoDrop.mc.field_1761.method_2906(syncId, fromSlotId, 0, class_1713.field_7790, (class_1657)AutoDrop.mc.field_1724);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void onDisable() {
        this.kitMap.clear();
    }
}

