/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.player.PlayerInventory
 *  net.minecraft.screen.ScreenHandler
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.screen.slot.Slot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.collection.DefaultedList
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 */
package dev.luminous.core.impl;

import com.google.common.collect.Lists;
import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventBus;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.CacheTimer;
import dev.luminous.api.utils.math.Timer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

public class InventoryManager
implements Wrapper {
    private final List<PreSwapData> swapData = new CopyOnWriteArrayList<PreSwapData>();
    private int slot;

    public InventoryManager() {
        EventBus.INSTANCE.subscribe(this);
    }

    public void setSlot(int barSlot) {
        if (this.slot != barSlot && class_1661.method_7380((int)barSlot)) {
            this.setSlotForced(barSlot);
            class_1799[] hotbarCopy = new class_1799[9];
            for (int i = 0; i < 9; ++i) {
                hotbarCopy[i] = InventoryManager.mc.field_1724.method_31548().method_5438(i);
            }
            this.swapData.add(new PreSwapData(hotbarCopy, this.slot, barSlot));
        }
    }

    public void setSlotForced(int barSlot) {
        Alien.NETWORK.sendPacket((class_2596<?>)new class_2868(barSlot));
    }

    public void syncToClient() {
        if (this.isDesynced()) {
            this.setSlotForced(InventoryManager.mc.field_1724.method_31548().field_7545);
            for (PreSwapData swapData : this.swapData) {
                swapData.beginClear();
            }
        }
    }

    public boolean isDesynced() {
        return InventoryManager.mc.field_1724.method_31548().field_7545 != this.slot;
    }

    public int click(int slot, int button, class_1713 type) {
        if (slot < 0) {
            return -1;
        }
        class_1703 screenHandler = InventoryManager.mc.field_1724.field_7512;
        class_2371 defaultedList = screenHandler.field_7761;
        int i = defaultedList.size();
        ArrayList list = Lists.newArrayListWithCapacity((int)i);
        for (class_1735 slot1 : defaultedList) {
            list.add(slot1.method_7677().method_7972());
        }
        screenHandler.method_7593(slot, button, type, (class_1657)InventoryManager.mc.field_1724);
        Int2ObjectOpenHashMap int2ObjectMap = new Int2ObjectOpenHashMap();
        for (int j = 0; j < i; ++j) {
            class_1799 itemStack2;
            class_1799 itemStack = (class_1799)list.get(j);
            if (class_1799.method_7973((class_1799)itemStack, (class_1799)(itemStack2 = ((class_1735)defaultedList.get(j)).method_7677()))) continue;
            int2ObjectMap.put(j, (Object)itemStack2.method_7972());
        }
        InventoryManager.mc.field_1724.field_3944.method_52787((class_2596)new class_2813(screenHandler.field_7763, screenHandler.method_37421(), slot, button, type, screenHandler.method_34255().method_7972(), (Int2ObjectMap)int2ObjectMap));
        return screenHandler.method_37421();
    }

    public static class PreSwapData {
        private final class_1799[] preHotbar;
        private final int starting;
        private final int swapTo;
        private Timer clearTime;

        public PreSwapData(class_1799[] preHotbar, int start, int swapTo) {
            this.preHotbar = preHotbar;
            this.starting = start;
            this.swapTo = swapTo;
        }

        public void beginClear() {
            this.clearTime = new CacheTimer();
            this.clearTime.reset();
        }

        public boolean isPassedClearTime() {
            return this.clearTime != null && this.clearTime.passed(300L);
        }

        public class_1799 getPreHolding(int i) {
            return this.preHotbar[i];
        }

        public int getStarting() {
            return this.starting;
        }

        public int getSlot() {
            return this.swapTo;
        }
    }
}

