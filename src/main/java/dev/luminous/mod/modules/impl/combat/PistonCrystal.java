/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1511
 *  net.minecraft.class_1657
 *  net.minecraft.class_1792
 *  net.minecraft.class_1802
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2318
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_2665
 *  net.minecraft.class_2769
 *  net.minecraft.class_3532
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.combat.AutoCrystal;
import dev.luminous.mod.modules.impl.combat.AutoPush;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2318;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2665;
import net.minecraft.class_2769;
import net.minecraft.class_3532;

public class PistonCrystal
extends Module {
    public static PistonCrystal INSTANCE;
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", false));
    private final BooleanSetting pistonPacket = this.add(new BooleanSetting("PistonPacket", false));
    private final BooleanSetting noEating = this.add(new BooleanSetting("NoEating", true));
    private final BooleanSetting eatingBreak = this.add(new BooleanSetting("EatingBreak", false));
    private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 5.0, 1.0, 8.0));
    private final SliderSetting range = this.add(new SliderSetting("Range", 4.0, 1.0, 8.0));
    private final BooleanSetting fire = this.add(new BooleanSetting("Fire", true));
    private final BooleanSetting switchPos = this.add(new BooleanSetting("Switch", false));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("SelfGround", true));
    private final BooleanSetting onlyStatic = this.add(new BooleanSetting("MovingPause", true));
    private final SliderSetting updateDelay = this.add(new SliderSetting("PlaceDelay", 100, 0, 500));
    private final SliderSetting posUpdateDelay = this.add(new SliderSetting("PosUpdateDelay", 500, 0, 1000));
    private final SliderSetting stageSetting = this.add(new SliderSetting("Stage", 4, 1, 10));
    private final SliderSetting pistonStage = this.add(new SliderSetting("PistonStage", 1, 1, 10));
    private final SliderSetting pistonMaxStage = this.add(new SliderSetting("PistonMaxStage", 1, 1, 10));
    private final SliderSetting powerStage = this.add(new SliderSetting("PowerStage", 3, 1, 10));
    private final SliderSetting powerMaxStage = this.add(new SliderSetting("PowerMaxStage", 3, 1, 10));
    private final SliderSetting crystalStage = this.add(new SliderSetting("CrystalStage", 4, 1, 10));
    private final SliderSetting crystalMaxStage = this.add(new SliderSetting("CrystalMaxStage", 4, 1, 10));
    private final SliderSetting fireStage = this.add(new SliderSetting("FireStage", 2, 1, 10));
    private final SliderSetting fireMaxStage = this.add(new SliderSetting("FireMaxStage", 2, 1, 10));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting debug = this.add(new BooleanSetting("Debug", false));
    private final Timer timer = new Timer();
    private final Timer crystalTimer = new Timer();
    public class_2338 bestPos = null;
    public class_2338 bestOPos = null;
    public class_2350 bestFacing = null;
    public double distance = 100.0;
    public boolean getPos = false;
    public int stage = 1;
    private class_1657 target = null;
    private boolean isPiston = false;

    public PistonCrystal() {
        super("PistonCrystal", Module.Category.Combat);
        this.setChinese("\u6d3b\u585e\u6c34\u6676");
        INSTANCE = this;
    }

    private static boolean canFire(class_2338 pos) {
        if (BlockUtil.canReplace(pos.method_10074())) {
            return false;
        }
        if (!PistonCrystal.mc.field_1687.method_22347(pos)) {
            return false;
        }
        return !BlockUtil.canClick(pos.method_10093(class_2350.field_11033)) ? false : BlockUtil.isStrictDirection(pos.method_10074(), class_2350.field_11036);
    }

    public void onTick() {
        if (this.pistonStage.getValue() > this.stageSetting.getValue()) {
            this.pistonStage.setValue(this.stageSetting.getValue());
        }
        if (this.fireStage.getValue() > this.stageSetting.getValue()) {
            this.fireStage.setValue(this.stageSetting.getValue());
        }
        if (this.powerStage.getValue() > this.stageSetting.getValue()) {
            this.powerStage.setValue(this.stageSetting.getValue());
        }
        if (this.crystalStage.getValue() > this.stageSetting.getValue()) {
            this.crystalStage.setValue(this.stageSetting.getValue());
        }
        if (this.pistonMaxStage.getValue() > this.stageSetting.getValue()) {
            this.pistonMaxStage.setValue(this.stageSetting.getValue());
        }
        if (this.fireMaxStage.getValue() > this.stageSetting.getValue()) {
            this.fireMaxStage.setValue(this.stageSetting.getValue());
        }
        if (this.powerMaxStage.getValue() > this.stageSetting.getValue()) {
            this.powerMaxStage.setValue(this.stageSetting.getValue());
        }
        if (this.crystalMaxStage.getValue() > this.stageSetting.getValue()) {
            this.crystalMaxStage.setValue(this.stageSetting.getValue());
        }
        if (this.crystalMaxStage.getValue() < this.crystalStage.getValue()) {
            this.crystalStage.setValue(this.crystalMaxStage.getValue());
        }
        if (this.powerMaxStage.getValue() < this.powerStage.getValue()) {
            this.powerStage.setValue(this.powerMaxStage.getValue());
        }
        if (this.pistonMaxStage.getValue() < this.pistonStage.getValue()) {
            this.pistonStage.setValue(this.pistonMaxStage.getValue());
        }
        if (this.fireMaxStage.getValue() < this.fireStage.getValue()) {
            this.fireStage.setValue(this.fireMaxStage.getValue());
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.onTick();
        this.target = CombatUtil.getClosestEnemy(this.range.getValue());
        if (!(this.target == null || this.noEating.getValue() && PistonCrystal.mc.field_1724.method_6115() || this.check(this.onlyStatic.getValue(), !PistonCrystal.mc.field_1724.method_24828(), this.onlyGround.getValue()))) {
            class_2338 pos = EntityUtil.getEntityPos((class_1297)this.target, true);
            if (!PistonCrystal.mc.field_1724.method_6115() || this.eatingBreak.getValue()) {
                if (this.checkCrystal(pos.method_10086(0))) {
                    CombatUtil.attackCrystal(pos.method_10086(0), this.rotate.getValue(), true);
                }
                if (this.checkCrystal(pos.method_10086(1))) {
                    CombatUtil.attackCrystal(pos.method_10086(1), this.rotate.getValue(), true);
                }
                if (this.checkCrystal(pos.method_10086(2))) {
                    CombatUtil.attackCrystal(pos.method_10086(2), this.rotate.getValue(), true);
                }
            }
            if (this.bestPos != null && PistonCrystal.mc.field_1687.method_8320(this.bestPos).method_26204() instanceof class_2665) {
                this.isPiston = true;
            } else if (this.isPiston) {
                this.isPiston = false;
                this.crystalTimer.reset();
                this.bestPos = null;
            }
            if (this.crystalTimer.passedMs(this.posUpdateDelay.getValueInt())) {
                this.stage = 0;
                this.distance = 100.0;
                this.getPos = false;
                this.getBestPos(pos.method_10086(2));
                this.getBestPos(pos.method_10084());
            }
            if (this.timer.passedMs(this.updateDelay.getValueInt()) && this.getPos && this.bestPos != null) {
                this.timer.reset();
                if (this.debug.getValue()) {
                    this.sendMessage("[Debug] PistonPos:" + String.valueOf(this.bestPos) + " Facing:" + String.valueOf(this.bestFacing) + " CrystalPos:" + String.valueOf(this.bestOPos.method_10093(this.bestFacing)));
                }
                this.doPistonAura(this.bestPos, this.bestFacing, this.bestOPos);
            }
        }
    }

    public boolean check(boolean onlyStatic, boolean onGround, boolean onlyGround) {
        if (MovementUtil.isMoving() && onlyStatic) {
            return true;
        }
        if (onGround && onlyGround) {
            return true;
        }
        if (this.findBlock(class_2246.field_10002) == -1) {
            return true;
        }
        return this.findClass(class_2665.class) == -1 ? true : this.findItem(class_1802.field_8301) == -1;
    }

    private boolean checkCrystal(class_2338 pos) {
        for (class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
            float damage;
            if (!(entity instanceof class_1511) || !((damage = AutoCrystal.INSTANCE.calculateDamage(entity.method_19538(), this.target, this.target)) > 7.0f)) continue;
            return true;
        }
        return false;
    }

    private boolean checkCrystal2(class_2338 pos) {
        for (class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
            if (!(entity instanceof class_1511) || !EntityUtil.getEntityPos(entity).equals((Object)pos)) continue;
            return true;
        }
        return false;
    }

    @Override
    public String getInfo() {
        return this.target != null ? this.target.method_5477().getString() : null;
    }

    private void getBestPos(class_2338 pos) {
        for (class_2350 i : class_2350.values()) {
            if (i == class_2350.field_11033 || i == class_2350.field_11036) continue;
            this.getPos(pos, i);
        }
    }

    private void getPos(class_2338 pos, class_2350 i) {
        if (BlockUtil.canPlaceCrystal(pos.method_10093(i)) || this.checkCrystal2(pos.method_10093(i))) {
            this.getPos(pos.method_10079(i, 3), i, pos);
            this.getPos(pos.method_10079(i, 3).method_10084(), i, pos);
            int offsetX = pos.method_10093(i).method_10263() - pos.method_10263();
            int offsetZ = pos.method_10093(i).method_10260() - pos.method_10260();
            this.getPos(pos.method_10079(i, 3).method_10069(offsetZ, 0, offsetX), i, pos);
            this.getPos(pos.method_10079(i, 3).method_10069(-offsetZ, 0, -offsetX), i, pos);
            this.getPos(pos.method_10079(i, 3).method_10069(offsetZ, 1, offsetX), i, pos);
            this.getPos(pos.method_10079(i, 3).method_10069(-offsetZ, 1, -offsetX), i, pos);
            this.getPos(pos.method_10079(i, 2), i, pos);
            this.getPos(pos.method_10079(i, 2).method_10084(), i, pos);
            this.getPos(pos.method_10079(i, 2).method_10069(offsetZ, 0, offsetX), i, pos);
            this.getPos(pos.method_10079(i, 2).method_10069(-offsetZ, 0, -offsetX), i, pos);
            this.getPos(pos.method_10079(i, 2).method_10069(offsetZ, 1, offsetX), i, pos);
            this.getPos(pos.method_10079(i, 2).method_10069(-offsetZ, 1, -offsetX), i, pos);
        }
    }

    private void getPos(class_2338 pos, class_2350 facing, class_2338 oPos) {
        if (!(this.switchPos.getValue() && this.bestPos != null && this.bestPos.equals((Object)pos) && PistonCrystal.mc.field_1687.method_22347(this.bestPos) || !BlockUtil.canPlace(pos, this.placeRange.getValue()) && !(this.getBlock(pos) instanceof class_2665) || this.findClass(class_2665.class) == -1 || ClientSetting.INSTANCE.lowVersion.getValue() && !(this.getBlock(pos) instanceof class_2665) && (PistonCrystal.mc.field_1724.method_23318() - (double)pos.method_10264() <= -2.0 || PistonCrystal.mc.field_1724.method_23318() - (double)pos.method_10264() >= 3.0) && BlockUtil.distanceToXZ((double)pos.method_10263() + 0.5, (double)pos.method_10260() + 0.5) < 2.6 || !PistonCrystal.mc.field_1687.method_22347(pos.method_10079(facing, -1)) || PistonCrystal.mc.field_1687.method_8320(pos.method_10079(facing, -1)).method_26204() == class_2246.field_10036 || this.getBlock(pos.method_10093(facing.method_10153())) == class_2246.field_10008 && !this.checkCrystal2(pos.method_10093(facing.method_10153())) || !BlockUtil.canPlace(pos, this.placeRange.getValue()) && !this.isPiston(pos, facing) || !((double)class_3532.method_15355((float)((float)PistonCrystal.mc.field_1724.method_33571().method_1025(pos.method_46558()))) < this.distance) && this.bestPos != null)) {
            this.bestPos = pos;
            this.bestOPos = oPos;
            this.bestFacing = facing;
            this.distance = class_3532.method_15355((float)((float)PistonCrystal.mc.field_1724.method_33571().method_1025(pos.method_46558())));
            this.getPos = true;
            this.crystalTimer.reset();
        }
    }

    private void doPistonAura(class_2338 pos, class_2350 facing, class_2338 oPos) {
        if ((double)this.stage >= this.stageSetting.getValue()) {
            this.stage = 0;
        }
        ++this.stage;
        if (PistonCrystal.mc.field_1687.method_22347(pos)) {
            if (!BlockUtil.canPlace(pos)) {
                return;
            }
            if ((double)this.stage >= this.pistonStage.getValue() && (double)this.stage <= this.pistonMaxStage.getValue()) {
                class_2350 side = BlockUtil.getPlaceSide(pos);
                if (side == null) {
                    return;
                }
                int old = PistonCrystal.mc.field_1724.method_31548().field_7545;
                AutoPush.pistonFacing(facing);
                int piston = this.findClass(class_2665.class);
                this.doSwap(piston);
                BlockUtil.placeBlock(pos, false, this.pistonPacket.getValue());
                if (this.inventory.getValue()) {
                    this.doSwap(piston);
                    EntityUtil.syncInventory();
                } else {
                    this.doSwap(old);
                }
                class_2338 neighbour = pos.method_10093(side);
                class_2350 opposite = side.method_10153();
                if (this.rotate.getValue()) {
                    Alien.ROTATION.lookAt(neighbour, opposite);
                }
            }
        }
        if ((double)this.stage >= this.powerStage.getValue() && (double)this.stage <= this.powerMaxStage.getValue()) {
            this.doRedStone(pos, facing, oPos.method_10093(facing));
        }
        if ((double)this.stage >= this.crystalStage.getValue() && (double)this.stage <= this.crystalMaxStage.getValue()) {
            this.placeCrystal(oPos, facing);
        }
        if ((double)this.stage >= this.fireStage.getValue() && (double)this.stage <= this.fireMaxStage.getValue()) {
            this.doFire(oPos, facing);
        }
    }

    private void placeCrystal(class_2338 pos, class_2350 facing) {
        int crystal;
        if (BlockUtil.canPlaceCrystal(pos.method_10093(facing)) && (crystal = this.findItem(class_1802.field_8301)) != -1) {
            int old = PistonCrystal.mc.field_1724.method_31548().field_7545;
            this.doSwap(crystal);
            BlockUtil.placeCrystal(pos.method_10093(facing), true);
            if (this.inventory.getValue()) {
                this.doSwap(crystal);
                EntityUtil.syncInventory();
            } else {
                this.doSwap(old);
            }
        }
    }

    private boolean isPiston(class_2338 pos, class_2350 facing) {
        if (!(PistonCrystal.mc.field_1687.method_8320(pos).method_26204() instanceof class_2665)) {
            return false;
        }
        return ((class_2350)PistonCrystal.mc.field_1687.method_8320(pos).method_11654((class_2769)class_2318.field_10927)).method_10153() != facing ? false : PistonCrystal.mc.field_1687.method_22347(pos.method_10079(facing, -1)) || this.getBlock(pos.method_10079(facing, -1)) == class_2246.field_10036 || this.getBlock(pos.method_10093(facing.method_10153())) == class_2246.field_10008;
    }

    private void doFire(class_2338 pos, class_2350 facing) {
        int fire;
        if (this.fire.getValue() && (fire = this.findItem(class_1802.field_8884)) != -1) {
            int old = PistonCrystal.mc.field_1724.method_31548().field_7545;
            int[] xOffset = new int[]{0, facing.method_10165(), -facing.method_10165()};
            int[] yOffset = new int[]{0, 1};
            int[] zOffset = new int[]{0, facing.method_10148(), -facing.method_10148()};
            for (int x : xOffset) {
                for (int y : yOffset) {
                    for (int z : zOffset) {
                        if (this.getBlock(pos.method_10069(x, y, z)) != class_2246.field_10036) continue;
                        return;
                    }
                }
            }
            for (int x : xOffset) {
                for (int y : yOffset) {
                    for (int zx : zOffset) {
                        if (!PistonCrystal.canFire(pos.method_10069(x, y, zx))) continue;
                        this.doSwap(fire);
                        this.placeFire(pos.method_10069(x, y, zx));
                        if (this.inventory.getValue()) {
                            this.doSwap(fire);
                            EntityUtil.syncInventory();
                        } else {
                            this.doSwap(old);
                        }
                        return;
                    }
                }
            }
        }
    }

    public void placeFire(class_2338 pos) {
        class_2338 neighbour = pos.method_10093(class_2350.field_11033);
        BlockUtil.clickBlock(neighbour, class_2350.field_11036, this.rotate.getValue());
    }

    private void doRedStone(class_2338 pos, class_2350 facing, class_2338 crystalPos) {
        if (PistonCrystal.mc.field_1687.method_22347(pos.method_10079(facing, -1)) || this.getBlock(pos.method_10079(facing, -1)) == class_2246.field_10036 || this.getBlock(pos.method_10093(facing.method_10153())) == class_2246.field_10008) {
            for (class_2350 i : class_2350.values()) {
                if (this.getBlock(pos.method_10093(i)) != class_2246.field_10002) continue;
                return;
            }
            int power = this.findBlock(class_2246.field_10002);
            if (power != -1) {
                int old = PistonCrystal.mc.field_1724.method_31548().field_7545;
                class_2350 bestNeighboring = BlockUtil.getBestNeighboring(pos, facing);
                if (bestNeighboring != null && bestNeighboring != facing.method_10153() && BlockUtil.canPlace(pos.method_10093(bestNeighboring), this.placeRange.getValue()) && !pos.method_10093(bestNeighboring).equals((Object)crystalPos)) {
                    this.doSwap(power);
                    BlockUtil.placeBlock(pos.method_10093(bestNeighboring), this.rotate.getValue());
                    if (this.inventory.getValue()) {
                        this.doSwap(power);
                        EntityUtil.syncInventory();
                    } else {
                        this.doSwap(old);
                    }
                } else {
                    for (class_2350 ix : class_2350.values()) {
                        if (!BlockUtil.canPlace(pos.method_10093(ix), this.placeRange.getValue()) || pos.method_10093(ix).equals((Object)crystalPos) || ix == facing.method_10153()) continue;
                        this.doSwap(power);
                        BlockUtil.placeBlock(pos.method_10093(ix), this.rotate.getValue());
                        if (this.inventory.getValue()) {
                            this.doSwap(power);
                            EntityUtil.syncInventory();
                        } else {
                            this.doSwap(old);
                        }
                        return;
                    }
                }
            }
        }
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, PistonCrystal.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public int findItem(class_1792 itemIn) {
        return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(itemIn) : InventoryUtil.findItem(itemIn);
    }

    public int findBlock(class_2248 blockIn) {
        return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(blockIn) : InventoryUtil.findBlock(blockIn);
    }

    public int findClass(Class<?> clazz) {
        return this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(clazz) : InventoryUtil.findClass(clazz);
    }

    private class_2248 getBlock(class_2338 pos) {
        return PistonCrystal.mc.field_1687.method_8320(pos).method_26204();
    }
}

