/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.CactusBlock
 *  net.minecraft.block.ChestBlock
 *  net.minecraft.block.EnderChestBlock
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.block.FenceBlock
 *  net.minecraft.block.PaneBlock
 *  net.minecraft.block.SlabBlock
 *  net.minecraft.block.SkullBlock
 *  net.minecraft.block.StainedGlassBlock
 *  net.minecraft.block.StairsBlock
 *  net.minecraft.block.TrapdoorBlock
 *  net.minecraft.block.WallBlock
 *  net.minecraft.block.PistonBlock
 *  net.minecraft.block.PistonExtensionBlock
 *  net.minecraft.block.PistonHeadBlock
 */
package dev.luminous.api.utils.path;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.path.Vec3;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.StainedGlassBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.PistonHeadBlock;

public class AStarCustomPathFinder
implements Wrapper {
    private static final Vec3[] flatCardinalDirections = new Vec3[]{new Vec3(1.0, 0.0, 0.0), new Vec3(-1.0, 0.0, 0.0), new Vec3(0.0, 0.0, 1.0), new Vec3(0.0, 0.0, -1.0)};
    private final Vec3 startVec3;
    private final Vec3 endVec3;
    private final ArrayList<Hub> hubs = new ArrayList();
    private final ArrayList<Hub> hubsToWork = new ArrayList();
    private ArrayList<Vec3> path = new ArrayList();

    public AStarCustomPathFinder(Vec3 startVec3, Vec3 endVec3) {
        this.startVec3 = startVec3.addVector(0.0, 0.0, 0.0).floor();
        this.endVec3 = endVec3.addVector(0.0, 0.0, 0.0).floor();
    }

    public static boolean checkPositionValidity(Vec3 loc, boolean checkGround) {
        return AStarCustomPathFinder.checkPositionValidity((int)loc.x(), (int)loc.y(), (int)loc.z(), checkGround);
    }

    public static boolean checkPositionValidity(int x, int y, int z, boolean checkGround) {
        class_2338 block1 = new class_2338(x, y, z);
        class_2338 block2 = new class_2338(x, y + 1, z);
        class_2338 block3 = new class_2338(x, y - 1, z);
        return !AStarCustomPathFinder.isBlockSolid(block1) && !AStarCustomPathFinder.isBlockSolid(block2) && (AStarCustomPathFinder.isBlockSolid(block3) || !checkGround) && AStarCustomPathFinder.isSafeToWalkOn(block3);
    }

    private static boolean isBlockSolid(class_2338 block) {
        return AStarCustomPathFinder.mc.field_1687.method_8320((class_2338)block).field_23166 != null && AStarCustomPathFinder.mc.field_1687.method_8320((class_2338)block).field_23166.field_20337 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2482 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2510 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2266 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2281 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2336 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2484 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2389 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2354 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2544 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2506 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2665 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2667 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2671 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2506 || AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2533;
    }

    private static boolean isSafeToWalkOn(class_2338 block) {
        return !(AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2354) && !(AStarCustomPathFinder.mc.field_1687.method_8320(block).method_26204() instanceof class_2544);
    }

    public ArrayList<Vec3> getPath() {
        return this.path;
    }

    public void compute() {
        this.compute(1000, 4);
    }

    public void compute(int loops, int depth) {
        this.path.clear();
        this.hubsToWork.clear();
        ArrayList<Vec3> initPath = new ArrayList<Vec3>();
        initPath.add(this.startVec3);
        this.hubsToWork.add(new Hub(this.startVec3, null, initPath, this.startVec3.squareDistanceTo(this.endVec3), 0.0, 0.0));
        block0: for (int i = 0; i < loops; ++i) {
            this.hubsToWork.sort(new CompareHub());
            int j = 0;
            if (this.hubsToWork.isEmpty()) break;
            for (Hub o : new ArrayList<Hub>(this.hubsToWork)) {
                Vec3 loc2;
                if (++j > depth) continue;
                this.hubsToWork.remove(o);
                this.hubs.add(o);
                for (Vec3 direction : flatCardinalDirections) {
                    Vec3 loc = o.getLoc().add(direction).floor();
                    if (AStarCustomPathFinder.checkPositionValidity(loc, false) && this.addHub(o, loc, 0.0)) break block0;
                }
                Vec3 loc1 = o.getLoc().addVector(0.0, 1.0, 0.0).floor();
                if ((!AStarCustomPathFinder.checkPositionValidity(loc1, false) || !this.addHub(o, loc1, 0.0)) && (!AStarCustomPathFinder.checkPositionValidity(loc2 = o.getLoc().addVector(0.0, -1.0, 0.0).floor(), false) || !this.addHub(o, loc2, 0.0))) continue;
                break block0;
            }
        }
        this.hubs.sort(new CompareHub());
        this.path = this.hubs.getFirst().getPath();
    }

    public Hub isHubExisting(Vec3 loc) {
        for (Hub hub : this.hubs) {
            if (hub.getLoc().x() != loc.x() || hub.getLoc().y() != loc.y() || hub.getLoc().z() != loc.z()) continue;
            return hub;
        }
        for (Hub hubx : this.hubsToWork) {
            if (hubx.getLoc().x() != loc.x() || hubx.getLoc().y() != loc.y() || hubx.getLoc().z() != loc.z()) continue;
            return hubx;
        }
        return null;
    }

    public boolean addHub(Hub parent, Vec3 loc, double cost) {
        Hub existingHub = this.isHubExisting(loc);
        double totalCost = cost;
        if (parent != null) {
            totalCost = cost + parent.getTotalCost();
        }
        if (existingHub == null) {
            double minDistanceSquared = 9.0;
            if (loc.x() == this.endVec3.x() && loc.y() == this.endVec3.y() && loc.z() == this.endVec3.z() || loc.squareDistanceTo(this.endVec3) <= minDistanceSquared) {
                this.path.clear();
                this.path = parent.getPath();
                this.path.add(loc);
                return true;
            }
            ArrayList<Vec3> path = new ArrayList<Vec3>(parent.getPath());
            path.add(loc);
            this.hubsToWork.add(new Hub(loc, parent, path, loc.squareDistanceTo(this.endVec3), cost, totalCost));
        } else if (existingHub.getCost() > cost) {
            ArrayList<Vec3> path = new ArrayList<Vec3>(parent.getPath());
            path.add(loc);
            existingHub.setLoc(loc);
            existingHub.setParent(parent);
            existingHub.setPath(path);
            existingHub.setSquareDistanceToFromTarget(loc.squareDistanceTo(this.endVec3));
            existingHub.setCost(cost);
            existingHub.setTotalCost(totalCost);
        }
        return false;
    }

    public static class Hub {
        private Vec3 loc;
        private Hub parent;
        private ArrayList<Vec3> path;
        private double squareDistanceToFromTarget;
        private double cost;
        private double totalCost;

        public Hub(Vec3 loc, Hub parent, ArrayList<Vec3> path, double squareDistanceToFromTarget, double cost, double totalCost) {
            this.loc = loc;
            this.parent = parent;
            this.path = path;
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
            this.cost = cost;
            this.totalCost = totalCost;
        }

        public Vec3 getLoc() {
            return this.loc;
        }

        public void setLoc(Vec3 loc) {
            this.loc = loc;
        }

        public Hub getParent() {
            return this.parent;
        }

        public void setParent(Hub parent) {
            this.parent = parent;
        }

        public ArrayList<Vec3> getPath() {
            return this.path;
        }

        public void setPath(ArrayList<Vec3> path) {
            this.path = path;
        }

        public double getSquareDistanceToFromTarget() {
            return this.squareDistanceToFromTarget;
        }

        public void setSquareDistanceToFromTarget(double squareDistanceToFromTarget) {
            this.squareDistanceToFromTarget = squareDistanceToFromTarget;
        }

        public double getCost() {
            return this.cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double getTotalCost() {
            return this.totalCost;
        }

        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }
    }

    public static class CompareHub
    implements Comparator<Hub> {
        @Override
        public int compare(Hub o1, Hub o2) {
            return (int)(o1.getSquareDistanceToFromTarget() + o1.getTotalCost() - (o2.getSquareDistanceToFromTarget() + o2.getTotalCost()));
        }
    }
}

