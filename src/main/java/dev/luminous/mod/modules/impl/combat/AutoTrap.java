/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ElytraItem
 *  net.minecraft.item.ItemStack
 *  net.minecraft.block.Blocks
 *  net.minecraft.block.ConcretePowderBlock
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.PredictUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;

public class AutoTrap
extends Module {
    public static AutoTrap INSTANCE;
    public final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
    private final EnumSetting<TargetMode> targetMod = this.add(new EnumSetting<TargetMode>("TargetMode", TargetMode.Single));
    private final EnumSetting<Mode> headMode = this.add(new EnumSetting<Mode>("BlockForHead", Mode.Anchor));
    final ArrayList<class_2338> trapList = new ArrayList();
    final ArrayList<class_2338> placeList = new ArrayList();
    private final Timer timer = new Timer();
    private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 4.0, 1.0, 6.0).setSuffix("m"));
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8));
    public final SliderSetting predictTicks = this.add(new SliderSetting("PredictTicks", 2.0, 0.0, 50.0, 1.0));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true));
    private final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 1.0, 8.0).setSuffix("m"));
    private final BooleanSetting checkMine = this.add(new BooleanSetting("DetectMining", false));
    private final BooleanSetting helper = this.add(new BooleanSetting("Helper", true));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting onlyCrawling = this.add(new BooleanSetting("OnlyCrawling", false));
    private final BooleanSetting checkElytra = this.add(new BooleanSetting("CheckElytra", false));
    private final BooleanSetting extend = this.add(new BooleanSetting("Extend", true));
    private final BooleanSetting antiStep = this.add(new BooleanSetting("AntiStep", false));
    private final BooleanSetting onlyBreak = this.add(new BooleanSetting("OnlyBreak", false, this.antiStep::getValue));
    private final BooleanSetting head = this.add(new BooleanSetting("Head", true));
    private final BooleanSetting headExtend = this.add(new BooleanSetting("HeadExtend", true));
    private final BooleanSetting chestUp = this.add(new BooleanSetting("ChestUp", true));
    private final BooleanSetting onlyBreaking = this.add(new BooleanSetting("OnlyBreaking", false, this.chestUp::getValue));
    private final BooleanSetting chest = this.add(new BooleanSetting("Chest", true));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", false, this.chest::getValue));
    private final BooleanSetting ignoreCrawling = this.add(new BooleanSetting("IgnoreCrawling", false, this.chest::getValue));
    private final BooleanSetting legs = this.add(new BooleanSetting("Legs", false));
    private final BooleanSetting legAnchor = this.add(new BooleanSetting("LegAnchor", true));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", false));
    private final BooleanSetting onlyHole = this.add(new BooleanSetting("OnlyHole", false));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Break", true));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true));
    public class_1657 target;
    int progress = 0;

    public AutoTrap() {
        super("AutoTrap", Module.Category.Combat);
        this.setChinese("\u81ea\u52a8\u56f0\u4f4f");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.trapList.clear();
        this.placeList.clear();
        this.progress = 0;
        this.target = null;
        if (!(this.selfGround.getValue() && !AutoTrap.mc.field_1724.method_24828() || this.inventory.getValue() && !EntityUtil.inInventory() || Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue() || this.usingPause.getValue() && AutoTrap.mc.field_1724.method_6115() || !this.timer.passed((long)this.delay.getValue()))) {
            if (this.targetMod.getValue() == TargetMode.Single) {
                this.target = CombatUtil.getClosestEnemy(this.range.getValue());
                if (this.target == null) {
                    if (this.autoDisable.getValue()) {
                        this.disable();
                    }
                    return;
                }
                this.trapTarget(this.target);
            } else if (this.targetMod.getValue() == TargetMode.Multi) {
                boolean found = false;
                for (class_1657 player : CombatUtil.getEnemies(this.range.getValue())) {
                    found = true;
                    this.target = player;
                    this.trapTarget(this.target);
                }
                if (!found) {
                    if (this.autoDisable.getValue()) {
                        this.disable();
                    }
                    this.target = null;
                }
            }
        }
    }

    private void trapTarget(class_1657 target) {
        if ((!this.onlyHole.getValue() || Alien.HOLE.isHole(EntityUtil.getEntityPos((class_1297)target))) && (!this.onlyCrawling.getValue() || target.method_20448() || this.checkElytra.getValue() && ((class_1799)target.method_31548().field_7548.get(2)).method_7909() instanceof class_1770 && (!(AutoTrap.mc.field_1724.method_23318() < target.method_23318() + 1.0) || target.method_6128()))) {
            class_243 playerPos = this.predictTicks.getValue() > 0.0 ? PredictUtil.getPos(target, this.predictTicks.getValueInt()) : target.method_19538();
            this.doTrap(target, new BlockPosX(playerPos.method_10216(), playerPos.method_10214(), playerPos.method_10215()));
        }
    }

    private void doTrap(class_1657 player, class_2338 pos) {
        if (pos != null && !this.trapList.contains(pos)) {
            class_2338 offsetPos;
            Object offsetPos2;
            int n;
            int n2;
            Object[] objectArray;
            int chestOffset;
            this.trapList.add(pos);
            int headOffset = player.method_20448() ? 1 : 2;
            int n3 = chestOffset = player.method_20448() ? 0 : 1;
            if (this.legs.getValue()) {
                objectArray = class_2350.values();
                n2 = objectArray.length;
                for (n = 0; n < n2; ++n) {
                    class_2350 i = objectArray[n];
                    if (i == class_2350.field_11033 || i == class_2350.field_11036) continue;
                    offsetPos2 = pos.method_10093(i);
                    this.tryPlaceBlock((class_2338)offsetPos2, this.legAnchor.getValue(), false, false);
                    if (BlockUtil.getPlaceSide(offsetPos2) != null || !BlockUtil.clientCanPlace(offsetPos2, this.breakCrystal.getValue()) || this.getHelper((class_2338)offsetPos2) == null) continue;
                    this.tryPlaceObsidian(this.getHelper((class_2338)offsetPos2));
                }
            }
            if (this.headExtend.getValue()) {
                objectArray = new int[]{1, 0, -1};
                n2 = objectArray.length;
                for (n = 0; n < n2; ++n) {
                    class_2350 x = objectArray[n];
                    offsetPos2 = new int[]{1, 0, -1};
                    int n4 = ((class_2338)offsetPos2).length;
                    for (int i = 0; i < n4; ++i) {
                        class_2338 z = offsetPos2[i];
                        offsetPos = pos.method_10069((int)z, 0, (int)x);
                        if (!this.checkEntity(new class_2338((class_2382)offsetPos))) continue;
                        this.tryPlaceBlock(offsetPos.method_10086(headOffset), this.headMode.getValue() == Mode.Anchor, this.headMode.getValue() == Mode.Concrete, this.headMode.getValue() == Mode.Web);
                    }
                }
            }
            if (this.head.getValue() && BlockUtil.clientCanPlace(pos.method_10086(headOffset), this.breakCrystal.getValue())) {
                if (BlockUtil.getPlaceSide(pos.method_10086(headOffset)) == null) {
                    boolean trapChest = this.helper.getValue();
                    if (this.getHelper(pos.method_10086(headOffset)) != null) {
                        this.tryPlaceObsidian(this.getHelper(pos.method_10086(headOffset)));
                        trapChest = false;
                    }
                    if (trapChest) {
                        int x;
                        class_2350[] class_2350Array = class_2350.values();
                        n = class_2350Array.length;
                        for (x = 0; x < n; ++x) {
                            class_2350 ix = class_2350Array[x];
                            if (ix == class_2350.field_11033 || ix == class_2350.field_11036) continue;
                            class_2338 offsetPos3 = pos.method_10093(ix).method_10086(chestOffset);
                            if (!BlockUtil.isStrictDirection(pos.method_10093(ix).method_10084(), ix.method_10153()) || !BlockUtil.clientCanPlace(offsetPos3.method_10086(chestOffset), this.breakCrystal.getValue()) || !BlockUtil.canPlace(offsetPos3, this.placeRange.getValue(), this.breakCrystal.getValue())) continue;
                            this.tryPlaceObsidian(offsetPos3);
                            trapChest = false;
                            break;
                        }
                        if (trapChest) {
                            class_2350Array = class_2350.values();
                            n = class_2350Array.length;
                            for (x = 0; x < n; ++x) {
                                class_2350 ixx = class_2350Array[x];
                                if (ixx == class_2350.field_11033 || ixx == class_2350.field_11036) continue;
                                class_2338 offsetPos4 = pos.method_10093(ixx).method_10086(chestOffset);
                                if (!BlockUtil.isStrictDirection(pos.method_10093(ixx).method_10084(), ixx.method_10153()) || !BlockUtil.clientCanPlace(offsetPos4.method_10086(chestOffset), this.breakCrystal.getValue()) || BlockUtil.getPlaceSide(offsetPos4) != null || !BlockUtil.clientCanPlace(offsetPos4, this.breakCrystal.getValue()) || this.getHelper(offsetPos4) == null) continue;
                                this.tryPlaceObsidian(this.getHelper(offsetPos4));
                                trapChest = false;
                                break;
                            }
                            if (trapChest) {
                                class_2350Array = class_2350.values();
                                n = class_2350Array.length;
                                for (x = 0; x < n; ++x) {
                                    class_2350 ixxx = class_2350Array[x];
                                    if (ixxx == class_2350.field_11033 || ixxx == class_2350.field_11036) continue;
                                    class_2338 offsetPos5 = pos.method_10093(ixxx).method_10086(chestOffset);
                                    if (!BlockUtil.isStrictDirection(pos.method_10093(ixxx).method_10084(), ixxx.method_10153()) || !BlockUtil.clientCanPlace(offsetPos5.method_10086(chestOffset), this.breakCrystal.getValue()) || BlockUtil.getPlaceSide(offsetPos5) != null || !BlockUtil.clientCanPlace(offsetPos5, this.breakCrystal.getValue()) || this.getHelper(offsetPos5) == null || BlockUtil.getPlaceSide(offsetPos5.method_10074()) != null || !BlockUtil.clientCanPlace(offsetPos5.method_10074(), this.breakCrystal.getValue()) || this.getHelper(offsetPos5.method_10074()) == null) continue;
                                    this.tryPlaceObsidian(this.getHelper(offsetPos5.method_10074()));
                                    break;
                                }
                            }
                        }
                    }
                }
                this.tryPlaceBlock(pos.method_10086(headOffset), this.headMode.getValue() == Mode.Anchor, this.headMode.getValue() == Mode.Concrete, this.headMode.getValue() == Mode.Web);
            }
            if (this.antiStep.getValue() && (Alien.BREAK.isMining(pos.method_10086(headOffset)) || !this.onlyBreak.getValue())) {
                if (BlockUtil.getPlaceSide(pos.method_10086(3)) == null && BlockUtil.clientCanPlace(pos.method_10086(3), this.breakCrystal.getValue()) && this.getHelper(pos.method_10086(3), class_2350.field_11033) != null) {
                    this.tryPlaceObsidian(this.getHelper(pos.method_10086(3)));
                }
                this.tryPlaceObsidian(pos.method_10086(3));
            }
            if (this.down.getValue()) {
                class_2338 offsetPos6 = pos.method_10074();
                this.tryPlaceObsidian(offsetPos6);
                if (BlockUtil.getPlaceSide(offsetPos6) == null && BlockUtil.clientCanPlace(offsetPos6, this.breakCrystal.getValue()) && this.getHelper(offsetPos6) != null) {
                    this.tryPlaceObsidian(this.getHelper(offsetPos6));
                }
            }
            if (this.chestUp.getValue()) {
                class_2350[] class_2350Array = class_2350.values();
                int n5 = class_2350Array.length;
                for (n = 0; n < n5; ++n) {
                    class_2350 ixxxx = class_2350Array[n];
                    if (ixxxx == class_2350.field_11033 || ixxxx == class_2350.field_11036) continue;
                    offsetPos2 = pos.method_10093(ixxxx).method_10086(headOffset);
                    if (this.onlyBreaking.getValue() && !Alien.BREAK.isMining(pos.method_10086(headOffset))) continue;
                    this.tryPlaceObsidian((class_2338)offsetPos2);
                    if (BlockUtil.getPlaceSide(offsetPos2) != null || !BlockUtil.clientCanPlace(offsetPos2, this.breakCrystal.getValue())) continue;
                    if (this.getHelper((class_2338)offsetPos2) != null) {
                        this.tryPlaceObsidian(this.getHelper((class_2338)offsetPos2));
                        continue;
                    }
                    if (BlockUtil.getPlaceSide(offsetPos2.method_10074()) != null || !BlockUtil.clientCanPlace(offsetPos2.method_10074(), this.breakCrystal.getValue()) || this.getHelper(offsetPos2.method_10074()) == null) continue;
                    this.tryPlaceObsidian(this.getHelper(offsetPos2.method_10074()));
                }
            }
            if (!(!this.chest.getValue() || this.onlyGround.getValue() && !this.target.method_24828() || this.ignoreCrawling.getValue() && this.target.method_20448())) {
                class_2350[] class_2350Array = class_2350.values();
                int n6 = class_2350Array.length;
                for (n = 0; n < n6; ++n) {
                    class_2350 ixxxxx = class_2350Array[n];
                    if (ixxxxx == class_2350.field_11033 || ixxxxx == class_2350.field_11036) continue;
                    offsetPos2 = pos.method_10093(ixxxxx).method_10086(chestOffset);
                    this.tryPlaceObsidian((class_2338)offsetPos2);
                    if (BlockUtil.getPlaceSide(offsetPos2) != null || !BlockUtil.clientCanPlace(offsetPos2, this.breakCrystal.getValue())) continue;
                    if (this.getHelper((class_2338)offsetPos2) != null) {
                        this.tryPlaceObsidian(this.getHelper((class_2338)offsetPos2));
                        continue;
                    }
                    if (BlockUtil.getPlaceSide(offsetPos2.method_10074()) != null || !BlockUtil.clientCanPlace(offsetPos2.method_10074(), this.breakCrystal.getValue()) || this.getHelper(offsetPos2.method_10074()) == null) continue;
                    this.tryPlaceObsidian(this.getHelper(offsetPos2.method_10074()));
                }
            }
            if (this.extend.getValue()) {
                for (int x : new int[]{1, 0, -1}) {
                    for (int zx : new int[]{1, 0, -1}) {
                        offsetPos = pos.method_10069(x, 0, zx);
                        if (!this.checkEntity(new class_2338((class_2382)offsetPos))) continue;
                        this.doTrap(player, offsetPos);
                    }
                }
            }
        }
    }

    @Override
    public String getInfo() {
        return this.target != null ? this.target.method_5477().getString() : null;
    }

    public class_2338 getHelper(class_2338 pos) {
        if (!this.helper.getValue()) {
            return null;
        }
        for (class_2350 i : class_2350.values()) {
            if (this.checkMine.getValue() && Alien.BREAK.isMining(pos.method_10093(i)) || !BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) || !BlockUtil.canPlace(pos.method_10093(i), this.placeRange.getValue(), this.breakCrystal.getValue())) continue;
            return pos.method_10093(i);
        }
        return null;
    }

    public class_2338 getHelper(class_2338 pos, class_2350 ignore) {
        if (!this.helper.getValue()) {
            return null;
        }
        for (class_2350 i : class_2350.values()) {
            if (i == ignore || this.checkMine.getValue() && Alien.BREAK.isMining(pos.method_10093(i)) || !BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) || !BlockUtil.canPlace(pos.method_10093(i), this.placeRange.getValue(), this.breakCrystal.getValue())) continue;
            return pos.method_10093(i);
        }
        return null;
    }

    private boolean checkEntity(class_2338 pos) {
        if (AutoTrap.mc.field_1724.method_5829().method_994(new class_238(pos))) {
            return false;
        }
        for (class_1297 class_12972 : Alien.THREAD.getPlayers()) {
            if (!class_12972.method_5829().method_994(new class_238(pos)) || !class_12972.method_5805()) continue;
            return true;
        }
        return false;
    }

    private void tryPlaceBlock(class_2338 pos, boolean anchor, boolean sand, boolean web) {
        if (!this.placeList.contains(pos) && !Alien.BREAK.isMining(pos) && BlockUtil.canPlace(pos, 6.0, this.breakCrystal.getValue()) && (double)this.progress < this.blocksPer.getValue() && !((double)class_3532.method_15355((float)((float)AutoTrap.mc.field_1724.method_33571().method_1025(pos.method_46558()))) > this.placeRange.getValue())) {
            int block;
            int old = AutoTrap.mc.field_1724.method_31548().field_7545;
            int n = sand ? this.getConcrete() : (web ? (this.getWeb() != -1 ? this.getWeb() : this.getBlock()) : (block = anchor && this.getAnchor() != -1 ? this.getAnchor() : this.getBlock()));
            if (block != -1) {
                this.placeList.add(pos);
                CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.usingPause.getValue());
                this.doSwap(block);
                BlockUtil.placeBlock(pos, this.rotate.getValue());
                if (this.inventory.getValue()) {
                    this.doSwap(block);
                    EntityUtil.syncInventory();
                } else {
                    this.doSwap(old);
                }
                this.timer.reset();
                ++this.progress;
            }
        }
    }

    private void tryPlaceObsidian(class_2338 pos) {
        if (pos != null && !this.placeList.contains(pos) && !Alien.BREAK.isMining(pos) && BlockUtil.canPlace(pos, 6.0, this.breakCrystal.getValue()) && (double)this.progress < this.blocksPer.getValue() && !((double)class_3532.method_15355((float)((float)AutoTrap.mc.field_1724.method_33571().method_1025(pos.method_46558()))) > this.placeRange.getValue())) {
            int old = AutoTrap.mc.field_1724.method_31548().field_7545;
            int block = this.getBlock();
            if (block != -1) {
                BlockUtil.placedPos.add(pos);
                this.placeList.add(pos);
                CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.usingPause.getValue());
                this.doSwap(block);
                BlockUtil.placeBlock(pos, this.rotate.getValue());
                if (this.inventory.getValue()) {
                    this.doSwap(block);
                    EntityUtil.syncInventory();
                } else {
                    this.doSwap(old);
                }
                this.timer.reset();
                ++this.progress;
            }
        }
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, AutoTrap.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getBlock() {
        return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
    }

    private int getConcrete() {
        return this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(class_2292.class) : InventoryUtil.findClass(class_2292.class);
    }

    private int getWeb() {
        return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10343) : InventoryUtil.findBlock(class_2246.field_10343);
    }

    private int getAnchor() {
        return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_23152) : InventoryUtil.findBlock(class_2246.field_23152);
    }

    public static enum TargetMode {
        Single,
        Multi;

    }

    private static enum Mode {
        Obsidian,
        Anchor,
        Web,
        Concrete;

    }
}

