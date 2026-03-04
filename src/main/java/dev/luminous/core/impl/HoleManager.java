/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Blocks
 *  net.minecraft.block.Block
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.impl.combat.AutoMine;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

public class HoleManager
implements Wrapper {
    public boolean isHole(class_2338 pos) {
        return this.isHole(pos, true, false, false);
    }

    public boolean isHole(class_2338 pos, boolean canStand, boolean checkTrap, boolean anyBlock) {
        int blockProgress = 0;
        for (class_2350 i : class_2350.values()) {
            if (i == class_2350.field_11036 || i == class_2350.field_11033 || (!anyBlock || HoleManager.mc.field_1687.method_22347(pos.method_10093(i))) && !Alien.HOLE.isHard(pos.method_10093(i))) continue;
            ++blockProgress;
        }
        return !(checkTrap && (!HoleManager.mc.field_1687.method_22347(pos) || !HoleManager.mc.field_1687.method_22347(pos.method_10084()) || !HoleManager.mc.field_1687.method_22347(pos.method_10086(1)) || !HoleManager.mc.field_1687.method_22347(pos.method_10086(2)) || HoleManager.mc.field_1724.method_31478() - 1 > pos.method_10264() && !HoleManager.mc.field_1687.method_22347(pos.method_10086(3)) || HoleManager.mc.field_1724.method_31478() - 2 > pos.method_10264() && !HoleManager.mc.field_1687.method_22347(pos.method_10086(4))) || blockProgress <= 3 || canStand && !BlockUtil.canCollide(new class_238(pos.method_10069(0, -1, 0))));
    }

    public class_2338 getHole(float range, boolean doubleHole, boolean any, boolean up) {
        class_2338 bestPos = null;
        double bestDistance = range + 1.0f;
        for (class_2338 pos : BlockUtil.getSphere(range, HoleManager.mc.field_1724.method_19538())) {
            if ((pos.method_10263() != HoleManager.mc.field_1724.method_31477() || pos.method_10260() != HoleManager.mc.field_1724.method_31479()) && !up && (double)(pos.method_10264() + 1) > HoleManager.mc.field_1724.method_23318() || !Alien.HOLE.isHole(pos, true, true, any) && (!doubleHole || !this.isDoubleHole(pos)) || pos.method_10264() - HoleManager.mc.field_1724.method_31478() > 1) continue;
            double distance = class_3532.method_15355((float)((float)HoleManager.mc.field_1724.method_5649((double)pos.method_10263() + 0.5, (double)pos.method_10264() + 0.5, (double)pos.method_10260() + 0.5)));
            if (bestPos != null && !(distance < bestDistance)) continue;
            bestPos = pos;
            bestDistance = distance;
        }
        return bestPos;
    }

    public boolean isDoubleHole(class_2338 pos) {
        class_2350 unHardFacing = this.is3Block(pos);
        if (unHardFacing != null) {
            return (unHardFacing = this.is3Block(pos = pos.method_10093(unHardFacing))) != null;
        }
        return false;
    }

    public class_2350 is3Block(class_2338 pos) {
        if (!this.isHard(pos.method_10074())) {
            return null;
        }
        if (HoleManager.mc.field_1687.method_22347(pos) && HoleManager.mc.field_1687.method_22347(pos.method_10084()) && HoleManager.mc.field_1687.method_22347(pos.method_10086(2))) {
            int progress = 0;
            class_2350 unHardFacing = null;
            for (class_2350 facing : class_2350.values()) {
                if (facing == class_2350.field_11036 || facing == class_2350.field_11033) continue;
                if (this.isHard(pos.method_10093(facing))) {
                    ++progress;
                    continue;
                }
                int progress2 = 0;
                for (class_2350 facing2 : class_2350.values()) {
                    if (facing2 == class_2350.field_11033 || facing2 == facing.method_10153() || !this.isHard(pos.method_10093(facing).method_10093(facing2))) continue;
                    ++progress2;
                }
                if (progress2 == 4) {
                    ++progress;
                    continue;
                }
                unHardFacing = facing;
            }
            return progress == 3 ? unHardFacing : null;
        }
        return null;
    }

    public boolean isHard(class_2338 pos) {
        class_2248 block = HoleManager.mc.field_1687.method_8320(pos).method_26204();
        return this.isHard(block);
    }

    public boolean isHard(class_2248 block) {
        return block == class_2246.field_9987 || AutoMine.hard.contains(block);
    }
}

