/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1291
 *  net.minecraft.class_1293
 *  net.minecraft.class_1294
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1738
 *  net.minecraft.class_1747
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1844
 *  net.minecraft.class_2244
 *  net.minecraft.class_2246
 *  net.minecraft.class_2399
 *  net.minecraft.class_2480
 *  net.minecraft.class_2665
 *  net.minecraft.class_437
 *  net.minecraft.class_9334
 *  org.apache.commons.io.IOUtils
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.core.Manager;
import dev.luminous.mod.gui.windows.WindowsScreen;
import dev.luminous.mod.gui.windows.impl.ItemSelectWindow;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1738;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2244;
import net.minecraft.class_2246;
import net.minecraft.class_2399;
import net.minecraft.class_2480;
import net.minecraft.class_2665;
import net.minecraft.class_437;
import net.minecraft.class_9334;
import org.apache.commons.io.IOUtils;

public class Sorter
extends Module {
    public static Sorter INSTANCE;
    final int[] stealCountList = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private final SliderSetting tasksPerTicks = this.add(new SliderSetting("TasksPerTick", 2, 1, 20));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 0.1, 0.0, 5.0, 0.01).setSuffix("s"));
    private final BooleanSetting stack = this.add(new BooleanSetting("Stack", true));
    private final EnumSetting<Mode> trashMode = this.add(new EnumSetting<Mode>("TrashMode", Mode.Whitelist));
    private final BooleanSetting edit = this.add(new BooleanSetting("EditTrash", false).injectTask(this::openGui));
    private final BooleanSetting sort = this.add(new BooleanSetting("Sort", true));
    private final BooleanSetting kit = this.add(new BooleanSetting("Kit", false).injectTask(this::onEnable));
    private final StringSetting kitName = this.add(new StringSetting("KitName", "kit1"));
    private final BooleanSetting drop = this.add(new BooleanSetting("Drop", true).setParent());
    private final BooleanSetting trash = this.add(new BooleanSetting("Trash", true, this.drop::isOpen));
    private final BooleanSetting rename = this.add(new BooleanSetting("Rename", true, this.drop::isOpen));
    private final BooleanSetting kitExceed = this.add(new BooleanSetting("KitExceed", true, this.drop::isOpen));
    private final BooleanSetting exceed = this.add(new BooleanSetting("Exceed", true, this.drop::isOpen));
    private final SliderSetting crystal = this.add(new SliderSetting("Crystal", 4, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting exp = this.add(new SliderSetting("Exp", 4, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting totem = this.add(new SliderSetting("Totem", 6, 0, 36, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting eGapple = this.add(new SliderSetting("EGapple", 2, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting gapple = this.add(new SliderSetting("Gapple", 2, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting obsidian = this.add(new SliderSetting("Obsidian", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting web = this.add(new SliderSetting("Web", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting glowstone = this.add(new SliderSetting("Glowstone", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting anchor = this.add(new SliderSetting("Anchor", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting pearl = this.add(new SliderSetting("Pearl", 1, 0, 8, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting piston = this.add(new SliderSetting("Piston", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting redstone = this.add(new SliderSetting("RedStone", 1, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting ladder = this.add(new SliderSetting("Ladder", 2, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting bed = this.add(new SliderSetting("Bed", 4, 0, 12, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting speed = this.add(new SliderSetting("Speed", 1, 0, 8, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting turtle = this.add(new SliderSetting("Resistance", 1, 0, 8, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final SliderSetting strength = this.add(new SliderSetting("Strength", 1, 0, 8, () -> this.drop.isOpen() && this.exceed.isOpen()));
    private final Timer timer = new Timer();
    private final Map<Integer, String> kitMap = new ConcurrentHashMap<Integer, String>();

    public Sorter() {
        super("Sorter", Module.Category.Player);
        this.setChinese("\u80cc\u5305\u6574\u7406");
        INSTANCE = this;
    }

    @Override
    public boolean onEnable() {
        if (!Sorter.nullCheck()) {
            Alien.THREAD.execute(() -> {
                this.kitMap.clear();
                try {
                    File file = Manager.getFile(this.kitName.getValue() + ".kit");
                    if (!file.exists()) {
                        return;
                    }
                    for (String s : IOUtils.readLines((InputStream)new FileInputStream(file), (Charset)StandardCharsets.UTF_8)) {
                        String[] split = s.split(":");
                        if (split.length != 2) {
                            return;
                        }
                        this.kitMap.put(Integer.valueOf(split[0]), split[1]);
                    }
                }
                catch (Exception var6) {
                    var6.printStackTrace();
                }
            });
        }
        return false;
    }

    public static int getItemCount(class_1792 item) {
        int count = 0;
        for (Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().method_7909() != item) continue;
            ++count;
        }
        if (Sorter.mc.field_1724.method_6079().method_7909() == item) {
            ++count;
        }
        return count;
    }

    public static int getItemCount(Class<?> clazz) {
        int count = 0;
        for (Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (!(entry.getValue().method_7909() instanceof class_1747) || !clazz.isInstance(((class_1747)entry.getValue().method_7909()).method_7711())) continue;
            ++count;
        }
        return count;
    }

    private void openGui() {
        this.edit.setValueWithoutTask(false);
        if (!Sorter.nullCheck()) {
            mc.method_1507((class_437)new WindowsScreen(new ItemSelectWindow(Alien.CLEANER)));
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (this.timer.passedS(this.delay.getValue()) && EntityUtil.inInventory()) {
            if (this.exceed.getValue()) {
                this.updateItem();
            }
            int i = 0;
            while ((double)i < this.tasksPerTicks.getValue()) {
                this.tweak();
                ++i;
            }
        }
    }

    private void tweak() {
        int slot1xxxx;
        class_1799 stack;
        if (this.drop.getValue()) {
            for (int slot1 = 35; slot1 >= 0; --slot1) {
                stack = Sorter.mc.field_1724.method_31548().method_5438(slot1);
                if (stack.method_7960() || !this.shouldDrop(stack)) continue;
                this.timer.reset();
                Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7512.field_7763, slot1 < 9 ? slot1 + 36 : slot1, 1, class_1713.field_7795, (class_1657)Sorter.mc.field_1724);
                return;
            }
        }
        if (this.stack.getValue()) {
            for (int slot1x = 35; slot1x >= 9; --slot1x) {
                stack = Sorter.mc.field_1724.method_31548().method_5438(slot1x);
                if (stack.method_7960() || !stack.method_7946() || stack.method_7947() == stack.method_7914()) continue;
                for (int slot2 = 0; slot2 < 36; ++slot2) {
                    class_1799 stack2;
                    if (slot1x == slot2 || (stack2 = Sorter.mc.field_1724.method_31548().method_5438(slot2)).method_7947() == stack2.method_7914() || !Sorter.canMerge(stack, stack2)) continue;
                    Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7498.field_7763, slot1x, 0, class_1713.field_7790, (class_1657)Sorter.mc.field_1724);
                    Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7498.field_7763, slot2 < 9 ? slot2 + 36 : slot2, 0, class_1713.field_7790, (class_1657)Sorter.mc.field_1724);
                    Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7498.field_7763, slot1x, 0, class_1713.field_7790, (class_1657)Sorter.mc.field_1724);
                    this.timer.reset();
                    return;
                }
            }
        }
        if (this.drop.getValue()) {
            for (int slot1xx = 35; slot1xx >= 0; --slot1xx) {
                stack = Sorter.mc.field_1724.method_31548().method_5438(slot1xx);
                if (stack.method_7960() || !this.exceed.getValue() || !this.exceed(stack, false)) continue;
                this.timer.reset();
                Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7512.field_7763, slot1xx < 9 ? slot1xx + 36 : slot1xx, 1, class_1713.field_7795, (class_1657)Sorter.mc.field_1724);
                return;
            }
        }
        if (this.sort.getValue()) {
            if (this.kit.getValue()) {
                for (int slot1xxx = 0; slot1xxx < 36; ++slot1xxx) {
                    if (!this.kitMap.containsKey(slot1xxx)) continue;
                    String target = this.kitMap.get(slot1xxx);
                    String name = Sorter.mc.field_1724.method_31548().method_5438(slot1xxx).method_7909().method_7876();
                    if (name.equals(target)) continue;
                    for (int slot2x = 0; slot2x < 36; ++slot2x) {
                        String itemID;
                        String slot2Target = this.kitMap.get(slot2x);
                        class_1799 stack2 = Sorter.mc.field_1724.method_31548().method_5438(slot2x);
                        if (stack2.method_7960() || (itemID = stack2.method_7909().method_7876()).equals(slot2Target) || !itemID.equals(target)) continue;
                        Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7498.field_7763, slot1xxx < 9 ? slot1xxx + 36 : slot1xxx, 0, class_1713.field_7790, (class_1657)Sorter.mc.field_1724);
                        Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7498.field_7763, slot2x < 9 ? slot2x + 36 : slot2x, 0, class_1713.field_7790, (class_1657)Sorter.mc.field_1724);
                        Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7498.field_7763, slot1xxx < 9 ? slot1xxx + 36 : slot1xxx, 0, class_1713.field_7790, (class_1657)Sorter.mc.field_1724);
                        this.timer.reset();
                        return;
                    }
                }
            } else {
                for (slot1xxxx = 9; slot1xxxx < 36; ++slot1xxxx) {
                    int minId;
                    int id = class_1792.method_7880((class_1792)Sorter.mc.field_1724.method_31548().method_5438(slot1xxxx).method_7909());
                    if (Sorter.mc.field_1724.method_31548().method_5438(slot1xxxx).method_7960()) {
                        id = 114514;
                    }
                    if ((minId = this.getMinId(slot1xxxx, id)) >= id) continue;
                    for (int slot2xx = 35; slot2xx > slot1xxxx; --slot2xx) {
                        int itemID;
                        class_1799 stack3 = Sorter.mc.field_1724.method_31548().method_5438(slot2xx);
                        if (stack3.method_7960() || (itemID = class_1792.method_7880((class_1792)stack3.method_7909())) != minId) continue;
                        Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7498.field_7763, slot1xxxx, 0, class_1713.field_7790, (class_1657)Sorter.mc.field_1724);
                        Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7498.field_7763, slot2xx, 0, class_1713.field_7790, (class_1657)Sorter.mc.field_1724);
                        Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7498.field_7763, slot1xxxx, 0, class_1713.field_7790, (class_1657)Sorter.mc.field_1724);
                        this.timer.reset();
                        return;
                    }
                }
            }
        }
        if (this.drop.getValue() && this.kitExceed.getValue()) {
            for (slot1xxxx = 35; slot1xxxx >= 0; --slot1xxxx) {
                class_1747 blockItem;
                class_1792 class_17922;
                if (!this.kitMap.containsKey(slot1xxxx)) continue;
                class_1799 stack4 = Sorter.mc.field_1724.method_31548().method_5438(slot1xxxx);
                if (this.exceed.getValue() && !this.exceed(stack4, true) || stack4.method_7960() || stack4.method_7909() instanceof class_1738 || (class_17922 = stack4.method_7909()) instanceof class_1747 && (blockItem = (class_1747)class_17922).method_7711() instanceof class_2480 || stack4.method_7909().method_7876().equals(this.kitMap.get(slot1xxxx))) continue;
                this.timer.reset();
                Sorter.mc.field_1761.method_2906(Sorter.mc.field_1724.field_7512.field_7763, slot1xxxx < 9 ? slot1xxxx + 36 : slot1xxxx, 1, class_1713.field_7795, (class_1657)Sorter.mc.field_1724);
                return;
            }
        }
    }

    private boolean shouldDrop(class_1799 stack) {
        boolean inList;
        class_1747 blockItem;
        class_1792 item = stack.method_7909();
        if (this.trash.getValue() && (!(item instanceof class_1747) || !((blockItem = (class_1747)item).method_7711() instanceof class_2480)) && (!(inList = Alien.CLEANER.inList(item.method_7876())) && this.trashMode.is(Mode.Whitelist) || inList && this.trashMode.is(Mode.Blacklist))) {
            return true;
        }
        return !this.rename.getValue() ? false : stack.method_7946() && !stack.method_7964().getString().equals(item.method_7848().getString());
    }

    private boolean exceed(class_1799 i, boolean dropOther) {
        if (i.method_7909().equals(class_1802.field_8301)) {
            if ((double)this.stealCountList[0] > this.crystal.getValue()) {
                this.stealCountList[0] = this.stealCountList[0] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909().equals(class_1802.field_8287)) {
            if ((double)this.stealCountList[1] > this.exp.getValue()) {
                this.stealCountList[1] = this.stealCountList[1] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909().equals(class_1802.field_8288)) {
            if ((double)this.stealCountList[2] > this.totem.getValue()) {
                this.stealCountList[2] = this.stealCountList[2] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909().equals(class_1802.field_8367)) {
            if ((double)this.stealCountList[3] > this.eGapple.getValue()) {
                this.stealCountList[3] = this.stealCountList[3] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909().equals(class_2246.field_10540.method_8389())) {
            if ((double)this.stealCountList[4] > this.obsidian.getValue()) {
                this.stealCountList[4] = this.stealCountList[4] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909().equals(class_2246.field_10343.method_8389())) {
            if ((double)this.stealCountList[5] > this.web.getValue()) {
                this.stealCountList[5] = this.stealCountList[5] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909().equals(class_2246.field_10171.method_8389())) {
            if ((double)this.stealCountList[6] > this.glowstone.getValue()) {
                this.stealCountList[6] = this.stealCountList[6] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909().equals(class_2246.field_23152.method_8389())) {
            if ((double)this.stealCountList[7] > this.anchor.getValue()) {
                this.stealCountList[7] = this.stealCountList[7] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909().equals(class_1802.field_8634)) {
            if ((double)this.stealCountList[8] > this.pearl.getValue()) {
                this.stealCountList[8] = this.stealCountList[8] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2665) {
            if ((double)this.stealCountList[9] > this.piston.getValue()) {
                this.stealCountList[9] = this.stealCountList[9] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909().equals(class_2246.field_10002.method_8389())) {
            if ((double)this.stealCountList[10] > this.redstone.getValue()) {
                this.stealCountList[10] = this.stealCountList[10] - 1;
                return true;
            }
            return false;
        }
        if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2244) {
            if ((double)this.stealCountList[11] > this.bed.getValue()) {
                this.stealCountList[11] = this.stealCountList[11] - 1;
                return true;
            }
            return false;
        }
        if (class_1792.method_7880((class_1792)i.method_7909()) == class_1792.method_7880((class_1792)class_1802.field_8436)) {
            class_1844 potionContentsComponent = (class_1844)i.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
            for (class_1293 effect : potionContentsComponent.method_57397()) {
                if (effect.method_5579().comp_349() == class_1294.field_5904.comp_349()) {
                    if ((double)this.stealCountList[12] > this.speed.getValue()) {
                        this.stealCountList[12] = this.stealCountList[12] - 1;
                        return true;
                    }
                    return false;
                }
                if (effect.method_5579().comp_349() == class_1294.field_5907.comp_349()) {
                    if ((double)this.stealCountList[13] > this.turtle.getValue()) {
                        this.stealCountList[13] = this.stealCountList[13] - 1;
                        return true;
                    }
                    return false;
                }
                if (effect.method_5579().comp_349() != class_1294.field_5910.comp_349()) continue;
                if ((double)this.stealCountList[16] > this.strength.getValue()) {
                    this.stealCountList[16] = this.stealCountList[16] - 1;
                    return true;
                }
                return false;
            }
        }
        if (i.method_7909().equals(class_1802.field_8463) && (double)this.stealCountList[14] > this.gapple.getValue()) {
            this.stealCountList[14] = this.stealCountList[14] - 1;
            return true;
        }
        if (i.method_7909() instanceof class_1747 && ((class_1747)i.method_7909()).method_7711() instanceof class_2399 && (double)this.stealCountList[15] > this.ladder.getValue()) {
            this.stealCountList[15] = this.stealCountList[15] - 1;
            return true;
        }
        return dropOther;
    }

    private void updateItem() {
        this.stealCountList[0] = Sorter.getItemCount(class_1802.field_8301);
        this.stealCountList[1] = Sorter.getItemCount(class_1802.field_8287);
        this.stealCountList[2] = Sorter.getItemCount(class_1802.field_8288);
        this.stealCountList[3] = Sorter.getItemCount(class_1802.field_8367);
        this.stealCountList[4] = Sorter.getItemCount(class_1802.field_8281);
        this.stealCountList[5] = Sorter.getItemCount(class_1802.field_8786);
        this.stealCountList[6] = Sorter.getItemCount(class_1802.field_8801);
        this.stealCountList[7] = Sorter.getItemCount(class_1802.field_23141);
        this.stealCountList[8] = Sorter.getItemCount(class_1802.field_8634);
        this.stealCountList[9] = Sorter.getItemCount(class_1802.field_8249) - Sorter.getItemCount(class_1802.field_8105);
        this.stealCountList[10] = Sorter.getItemCount(class_1802.field_8793);
        this.stealCountList[11] = Sorter.getItemCount(class_2244.class);
        this.stealCountList[12] = InventoryUtil.getPotionCount((class_1291)class_1294.field_5904.comp_349());
        this.stealCountList[13] = InventoryUtil.getPotionCount((class_1291)class_1294.field_5907.comp_349());
        this.stealCountList[14] = Sorter.getItemCount(class_1802.field_8463);
        this.stealCountList[15] = Sorter.getItemCount(class_2399.class);
        this.stealCountList[16] = InventoryUtil.getPotionCount((class_1291)class_1294.field_5910.comp_349());
    }

    private int getMinId(int slot, int currentId) {
        int id = currentId;
        for (int slot1 = slot + 1; slot1 < 36; ++slot1) {
            int itemID;
            class_1799 stack = Sorter.mc.field_1724.method_31548().method_5438(slot1);
            if (stack.method_7960() || (itemID = class_1792.method_7880((class_1792)stack.method_7909())) >= id) continue;
            id = itemID;
        }
        return id;
    }

    public static boolean canMerge(class_1799 source, class_1799 stack) {
        return class_1799.method_31577((class_1799)source, (class_1799)stack);
    }

    private static enum Mode {
        Whitelist,
        Blacklist;

    }
}

