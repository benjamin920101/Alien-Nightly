/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.combat.AutoCrystal;
import dev.luminous.mod.modules.impl.combat.SelfTrap;
import dev.luminous.mod.modules.impl.combat.Surround;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;

public class Blocker
extends Module {
    public static Blocker INSTANCE;
    private final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    final List<class_2338> placePos = new ArrayList<class_2338>();
    final List<class_2338> blockerPos = new ArrayList<class_2338>();
    final List<class_2338> list = new ArrayList<class_2338>();
    private final Timer timer = new Timer();
    private final SliderSetting delay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, () -> this.page.getValue() == Page.General));
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8, () -> this.page.getValue() == Page.General));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.getValue() == Page.General));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Break", true, () -> this.page.getValue() == Page.General));
    private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.getValue() == Page.General));
    private final BooleanSetting bevelCev = this.add(new BooleanSetting("BevelCev", true, () -> this.page.getValue() == Page.Target));
    private final BooleanSetting burrow = this.add(new BooleanSetting("Burrow", true, () -> this.page.getValue() == Page.Target));
    private final BooleanSetting face = this.add(new BooleanSetting("Face", true, () -> this.page.getValue() == Page.Target).setParent());
    private final BooleanSetting faceUp = this.add(new BooleanSetting("FaceUp", false, () -> this.page.getValue() == Page.Target && this.face.isOpen()));
    private final BooleanSetting feet = this.add(new BooleanSetting("Feet", true, () -> this.page.getValue() == Page.Target).setParent());
    private final BooleanSetting extend = this.add(new BooleanSetting("Extend", false, () -> this.page.getValue() == Page.Target && this.feet.isOpen()));
    private final BooleanSetting onlySurround = this.add(new BooleanSetting("OnlySurround", true, () -> this.page.getValue() == Page.Target && this.feet.isOpen()));
    private final BooleanSetting inAirPause = this.add(new BooleanSetting("InAirPause", false, () -> this.page.getValue() == Page.Check));
    private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", true, () -> this.page.getValue() == Page.Check));
    private final BooleanSetting eatingPause = this.add(new BooleanSetting("EatingPause", true, () -> this.page.getValue() == Page.Check));
    private int placeProgress = 0;
    private class_2338 playerBP;

    public Blocker() {
        super("Blocker", Module.Category.Combat);
        this.setChinese("\u6c34\u6676\u963b\u6321");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.list.clear();
        if (!(this.inventorySwap.getValue() && !EntityUtil.inInventory() || Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue() || !this.timer.passedMs(this.delay.getValue()) || this.eatingPause.getValue() && Blocker.mc.field_1724.method_6115())) {
            class_2338 blockerPos;
            this.placeProgress = 0;
            if (this.playerBP != null && !this.playerBP.equals((Object)EntityUtil.getPlayerPos(true))) {
                this.placePos.clear();
                this.blockerPos.clear();
            }
            this.playerBP = EntityUtil.getPlayerPos(true);
            double[] offset = new double[]{AntiCheat.getOffset(), -AntiCheat.getOffset(), 0.0};
            if (this.bevelCev.getValue()) {
                for (class_2350 i : class_2350.values()) {
                    if (i == class_2350.field_11033 || this.isBedrock(this.playerBP.method_10093(i).method_10084()) || !this.crystalHere(blockerPos = this.playerBP.method_10093(i).method_10086(2)) || this.placePos.contains(blockerPos)) continue;
                    this.placePos.add(blockerPos);
                }
            }
            if (this.face.getValue() && (!this.onlySurround.getValue() || Surround.INSTANCE.isOn() || SelfTrap.INSTANCE.isOn())) {
                for (double x : offset) {
                    for (double z : offset) {
                        for (class_2350 ix : class_2350.values()) {
                            for (int d = 0; d < 3; ++d) {
                                class_2338 aroundPos = new BlockPosX(Blocker.mc.field_1724.method_23317() + x, Blocker.mc.field_1724.method_23318() + 0.5, Blocker.mc.field_1724.method_23321() + z).method_10079(ix, 1).method_10084();
                                class_2338 blockerPos2 = new BlockPosX(Blocker.mc.field_1724.method_23317() + x, Blocker.mc.field_1724.method_23318() + 0.5, Blocker.mc.field_1724.method_23321() + z).method_10079(ix, d).method_10084();
                                if (!this.crystalHere(blockerPos2) || this.placePos.contains(blockerPos2) || Alien.HOLE.isHard(aroundPos)) continue;
                                this.placePos.add(blockerPos2);
                            }
                        }
                    }
                }
                if (this.faceUp.getValue()) {
                    for (class_2350 ix : class_2350.values()) {
                        if (ix == class_2350.field_11033 || this.isBedrock(this.playerBP.method_10093(ix).method_10084()) || !this.crystalHere(blockerPos = this.playerBP.method_10093(ix).method_10086(2)) || this.placePos.contains(blockerPos)) continue;
                        this.placePos.add(blockerPos);
                    }
                }
            }
            if (this.getObsidian() != -1) {
                this.placePos.removeIf(pos -> !BlockUtil.clientCanPlace(pos, true));
                if (this.burrow.getValue()) {
                    for (double x : offset) {
                        for (double z : offset) {
                            BlockPosX surroundPos = new BlockPosX(Blocker.mc.field_1724.method_23317() + x, Blocker.mc.field_1724.method_23318(), Blocker.mc.field_1724.method_23321() + z);
                            if (this.isBedrock(surroundPos) || !Alien.BREAK.isMining(surroundPos)) continue;
                            class_2350[] class_2350Array = class_2350.values();
                            int n = class_2350Array.length;
                            for (int ix = 0; ix < n; ++ix) {
                                class_2350 direction = class_2350Array[ix];
                                if (direction == class_2350.field_11033 || direction == class_2350.field_11036) continue;
                                class_2338 defensePos = surroundPos.method_10093(direction);
                                if (this.detectMining.getValue() && Alien.BREAK.isMining(defensePos)) continue;
                                if (this.breakCrystal.getValue()) {
                                    CombatUtil.attackCrystal(defensePos, this.rotate.getValue(), false);
                                }
                                if (!BlockUtil.canPlace(defensePos, 6.0, this.breakCrystal.getValue())) continue;
                                this.blockerPos.add(defensePos);
                            }
                        }
                    }
                }
                if (this.feet.getValue() && (!this.onlySurround.getValue() || Surround.INSTANCE.isOn() || SelfTrap.INSTANCE.isOn())) {
                    for (double x : offset) {
                        for (double zx : offset) {
                            for (class_2350 ixx : class_2350.values()) {
                                class_2338 surroundPos = new BlockPosX(Blocker.mc.field_1724.method_23317() + x, Blocker.mc.field_1724.method_23318() + 0.5, Blocker.mc.field_1724.method_23321() + zx).method_10093(ixx);
                                if (this.isBedrock(surroundPos) || !Alien.BREAK.isMining(surroundPos)) continue;
                                for (class_2350 directionx : class_2350.values()) {
                                    class_2338 defensePos = surroundPos.method_10093(directionx);
                                    if (this.detectMining.getValue() && Alien.BREAK.isMining(defensePos)) continue;
                                    if (this.breakCrystal.getValue()) {
                                        CombatUtil.attackCrystal(defensePos, this.rotate.getValue(), false);
                                    }
                                    if (BlockUtil.canPlace(defensePos, 6.0, this.breakCrystal.getValue())) {
                                        this.blockerPos.add(defensePos);
                                        continue;
                                    }
                                    if (!BlockUtil.canReplace(defensePos) || BlockUtil.hasEntity(defensePos, true) || this.getHelper(defensePos) == null) continue;
                                    this.blockerPos.add(this.getHelper(defensePos));
                                }
                            }
                        }
                    }
                }
                if (this.feet.getValue() && this.extend.getValue() && (!this.onlySurround.getValue() || Surround.INSTANCE.isOn() || SelfTrap.INSTANCE.isOn())) {
                    for (double x : offset) {
                        for (double zx : offset) {
                            for (class_2350 ixxx : class_2350.values()) {
                                class_2338 surroundPos;
                                if (ixxx == class_2350.field_11036 || ixxx == class_2350.field_11033 || this.isBedrock(surroundPos = new BlockPosX(Blocker.mc.field_1724.method_23317() + x, Blocker.mc.field_1724.method_23318() + 0.5, Blocker.mc.field_1724.method_23321() + zx).method_10093(ixxx))) continue;
                                for (class_2350 directionxx : class_2350.values()) {
                                    class_2338 blockPos;
                                    if (directionxx == class_2350.field_11036 || directionxx == class_2350.field_11033 || !AutoCrystal.INSTANCE.canPlaceCrystal(blockPos = surroundPos.method_10093(directionxx), true, true) || this.detectMining.getValue() && Alien.BREAK.isMining(blockPos)) continue;
                                    if (this.breakCrystal.getValue()) {
                                        CombatUtil.attackCrystal(blockPos, this.rotate.getValue(), false);
                                    }
                                    if (!BlockUtil.canPlace(blockPos, 6.0, this.breakCrystal.getValue())) continue;
                                    this.blockerPos.add(blockPos);
                                }
                            }
                        }
                    }
                }
                this.blockerPos.removeIf(pos -> !BlockUtil.clientCanPlace(pos, true));
                if (!(this.inAirPause.getValue() && !Blocker.mc.field_1724.method_24828() || this.blockerPos.isEmpty())) {
                    int n = Blocker.mc.field_1724.method_31548().field_7545;
                    int block = this.getObsidian();
                    if (block != -1) {
                        this.doSwap(block);
                        for (class_2338 defensePos : this.blockerPos) {
                            if (!BlockUtil.canPlace(defensePos, 6.0, this.breakCrystal.getValue())) continue;
                            this.doPlace(defensePos);
                        }
                        if (this.inventorySwap.getValue()) {
                            this.doSwap(block);
                            EntityUtil.syncInventory();
                        } else {
                            this.doSwap(n);
                        }
                    }
                }
            }
        }
    }

    public class_2338 getHelper(class_2338 pos) {
        for (class_2350 i : class_2350.values()) {
            if (i == class_2350.field_11036 || this.detectMining.getValue() && Alien.BREAK.isMining(pos.method_10093(i)) || !BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) || !BlockUtil.canPlace(pos.method_10093(i), 6.0)) continue;
            return pos.method_10093(i);
        }
        return null;
    }

    private boolean crystalHere(class_2338 pos) {
        return BlockUtil.getEndCrystals(new class_238(pos)).stream().anyMatch(entity -> entity.method_24515().equals((Object)pos));
    }

    private boolean isBedrock(class_2338 pos) {
        return Blocker.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_9987;
    }

    private void doPlace(class_2338 pos) {
        if (!this.list.contains(pos)) {
            this.list.add(pos);
            if ((double)this.placeProgress < this.blocksPer.getValue()) {
                BlockUtil.placeBlock(pos, this.rotate.getValue());
                this.timer.reset();
                ++this.placeProgress;
            }
        }
    }

    private void tryPlaceObsidian(class_2338 pos) {
        if (!this.list.contains(pos)) {
            this.list.add(pos);
            if (!(!((double)this.placeProgress < this.blocksPer.getValue()) || this.detectMining.getValue() && Alien.BREAK.isMining(pos))) {
                int oldSlot = Blocker.mc.field_1724.method_31548().field_7545;
                int block = this.getObsidian();
                if (block != -1) {
                    this.doSwap(block);
                    BlockUtil.placeBlock(pos, this.rotate.getValue());
                    this.timer.reset();
                    if (this.inventorySwap.getValue()) {
                        this.doSwap(block);
                        EntityUtil.syncInventory();
                    } else {
                        this.doSwap(oldSlot);
                    }
                    ++this.placeProgress;
                }
            }
        }
    }

    private void doSwap(int slot) {
        if (this.inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, Blocker.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getObsidian() {
        return this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
    }

    public static enum Page {
        General,
        Target,
        Check;

    }
}

