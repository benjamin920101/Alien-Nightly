/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.PickaxeItem
 *  net.minecraft.block.Blocks
 *  net.minecraft.block.Block
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Position
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.AntiCrawl;
import dev.luminous.mod.modules.impl.combat.CevBreaker;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PickaxeItem;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;

public class AutoMine
extends Module {
    public static AutoMine INSTANCE;
    public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 6.0, 0.0, 8.0, 0.1).setSuffix("m"));
    public final SliderSetting range = this.add(new SliderSetting("Range", 6.0, 0.0, 8.0, 0.1).setSuffix("m"));
    private final BooleanSetting burrow = this.add(new BooleanSetting("Burrow", true));
    private final BooleanSetting head = this.add(new BooleanSetting("Head", true));
    private final BooleanSetting face = this.add(new BooleanSetting("Face", true));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", false));
    private final BooleanSetting surround = this.add(new BooleanSetting("Surround", true));
    private final BooleanSetting cevPause = this.add(new BooleanSetting("CevPause", true));
    private final BooleanSetting forceDouble = this.add(new BooleanSetting("ForceDouble", false));
    public static final List<class_2248> hard;

    public AutoMine() {
        super("AutoMine", Module.Category.Combat);
        this.setChinese("\u81ea\u52a8\u6316\u6398");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        class_1657 player;
        if (!(CevBreaker.INSTANCE.isOn() && this.cevPause.getValue() || AntiCrawl.INSTANCE.work || Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue() || (player = CombatUtil.getClosestEnemy(this.targetRange.getValue())) == null)) {
            this.doBreak(player);
        }
    }

    private void doBreak(class_1657 player) {
        class_2338 pos = EntityUtil.getEntityPos((class_1297)player, true);
        if (SpeedMine.getBreakPos() == null || SpeedMine.getBreakPos().equals((Object)SpeedMine.secondPos) || SpeedMine.secondPos == null || AutoMine.mc.field_1687.method_22347(SpeedMine.secondPos) || !this.forceDouble.getValue()) {
            double[] yOffset = new double[]{-0.8, 0.3, 1.1};
            double[] xzOffset = new double[]{0.3, -0.3};
            for (class_1657 entity : CombatUtil.getEnemies(this.targetRange.getValue())) {
                for (double y2 : yOffset) {
                    for (double x : xzOffset) {
                        for (double z : xzOffset) {
                            BlockPosX offsetPos = new BlockPosX(entity.method_23317() + x, entity.method_23318() + y2, entity.method_23321() + z);
                            if (!this.canBreak(offsetPos) || !offsetPos.equals(SpeedMine.getBreakPos())) continue;
                            return;
                        }
                    }
                }
            }
            ArrayList<Float> yList = new ArrayList<Float>();
            if (this.down.getValue()) {
                yList.add(Float.valueOf(-0.8f));
            }
            if (this.head.getValue()) {
                yList.add(Float.valueOf(2.3f));
            }
            if (this.burrow.getValue()) {
                yList.add(Float.valueOf(0.3f));
            }
            if (this.face.getValue()) {
                yList.add(Float.valueOf(1.1f));
            }
            Iterator var32 = yList.iterator();
            while (var32.hasNext()) {
                double y = ((Float)var32.next()).floatValue();
                double[] dArray = xzOffset;
                int y2 = dArray.length;
                for (int i = 0; i < y2; ++i) {
                    double offset = dArray[i];
                    BlockPosX offsetPos = new BlockPosX(player.method_23317() + offset, player.method_23318() + y, player.method_23321() + offset);
                    if (!this.canBreak(offsetPos)) continue;
                    SpeedMine.INSTANCE.mine(offsetPos);
                    return;
                }
            }
            var32 = yList.iterator();
            while (var32.hasNext()) {
                double y = ((Float)var32.next()).floatValue();
                for (double offsetx : xzOffset) {
                    for (double offset2 : xzOffset) {
                        BlockPosX offsetPos = new BlockPosX(player.method_23317() + offset2, player.method_23318() + y, player.method_23321() + offsetx);
                        if (!this.canBreak(offsetPos)) continue;
                        SpeedMine.INSTANCE.mine(offsetPos);
                        return;
                    }
                }
            }
            if (this.surround.getValue()) {
                for (class_2350 i : class_2350.values()) {
                    if (i == class_2350.field_11036 || i == class_2350.field_11033 || Math.sqrt(AutoMine.mc.field_1724.method_33571().method_1025(pos.method_10093(i).method_46558())) > this.range.getValue() || !AutoMine.mc.field_1687.method_22347(pos.method_10093(i)) && !pos.method_10093(i).equals((Object)SpeedMine.getBreakPos()) || !this.canPlaceCrystal(pos.method_10093(i), false) || pos.method_10093(i).equals((Object)SpeedMine.secondPos)) continue;
                    return;
                }
                ArrayList<class_2338> list = new ArrayList<class_2338>();
                for (class_2350 ix : class_2350.values()) {
                    if (ix == class_2350.field_11036 || ix == class_2350.field_11033 || Math.sqrt(AutoMine.mc.field_1724.method_33571().method_1025(pos.method_10093(ix).method_46558())) > this.range.getValue() || !this.canBreak(pos.method_10093(ix)) || !this.canPlaceCrystal(pos.method_10093(ix), true) || this.isSurroundPos(pos.method_10093(ix))) continue;
                    list.add(pos.method_10093(ix));
                }
                if (!list.isEmpty()) {
                    SpeedMine.INSTANCE.mine(list.stream().min(Comparator.comparingDouble(E -> E.method_19770((class_2374)AutoMine.mc.field_1724.method_33571()))).get());
                } else {
                    for (class_2350 ixx : class_2350.values()) {
                        if (ixx == class_2350.field_11036 || ixx == class_2350.field_11033 || Math.sqrt(AutoMine.mc.field_1724.method_33571().method_1025(pos.method_10093(ixx).method_46558())) > this.range.getValue() || !this.canBreak(pos.method_10093(ixx)) || !this.canPlaceCrystal(pos.method_10093(ixx), false)) continue;
                        list.add(pos.method_10093(ixx));
                    }
                    if (!list.isEmpty()) {
                        SpeedMine.INSTANCE.mine(list.stream().min(Comparator.comparingDouble(E -> E.method_19770((class_2374)AutoMine.mc.field_1724.method_33571()))).get());
                    }
                }
            }
        }
    }

    private boolean isSurroundPos(class_2338 pos) {
        for (class_2350 i : class_2350.values()) {
            class_2338 self;
            if (i == class_2350.field_11036 || i == class_2350.field_11033 || !(self = EntityUtil.getPlayerPos(true)).method_10093(i).equals((Object)pos)) continue;
            return true;
        }
        return false;
    }

    public boolean canPlaceCrystal(class_2338 pos, boolean block) {
        class_2338 obsPos = pos.method_10074();
        class_2338 boost = obsPos.method_10084();
        return (BlockUtil.getBlock(obsPos) == class_2246.field_9987 || BlockUtil.getBlock(obsPos) == class_2246.field_10540 || !block) && BlockUtil.noEntityBlockCrystal(boost, true, true) && BlockUtil.noEntityBlockCrystal(boost.method_10084(), true, true);
    }

    private boolean isObsidian(class_2338 pos) {
        return AutoMine.mc.field_1724.method_33571().method_1022(pos.method_46558()) <= SpeedMine.INSTANCE.range.getValue() && hard.contains(BlockUtil.getBlock(pos)) && BlockUtil.getClickSideStrict(pos) != null;
    }

    private boolean canBreak(class_2338 pos) {
        return this.isObsidian(pos) && (BlockUtil.getClickSideStrict(pos) != null || pos.equals((Object)SpeedMine.getBreakPos())) && (!pos.equals((Object)SpeedMine.secondPos) || !(AutoMine.mc.field_1724.method_6047().method_7909() instanceof class_1810) && !SpeedMine.INSTANCE.autoSwitch.getValue() && !SpeedMine.INSTANCE.noGhostHand.getValue());
    }

    static {
        hard = Arrays.asList(class_2246.field_10540, class_2246.field_10443, class_2246.field_22108, class_2246.field_22423, class_2246.field_23152, class_2246.field_22109, class_2246.field_10535);
    }
}

