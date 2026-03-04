/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.block.Blocks
 *  net.minecraft.block.Block
 *  net.minecraft.block.PlantBlock
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.block.WallSignBlock
 */
package dev.luminous.api.utils.path;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.path.AStarCustomPathFinder;
import dev.luminous.api.utils.path.Vec3;
import dev.luminous.api.utils.world.BlockPosX;
import java.util.ArrayList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.WallSignBlock;

public class PathUtils
implements Wrapper {
    public static final double range = 9.0;

    private static boolean canPassThrough(class_2338 pos) {
        class_2248 block = PathUtils.mc.field_1687.method_8320(new class_2338(pos.method_10263(), pos.method_10264(), pos.method_10260())).method_26204();
        return block == class_2246.field_10124 || block instanceof class_2261 || block == class_2246.field_10597 || block == class_2246.field_9983 || block == class_2246.field_10382 || block == class_2246.field_27097 || block instanceof class_2551;
    }

    public static ArrayList<Vec3> computePath(class_1309 fromEntity, class_1309 toEntity) {
        return PathUtils.computePath(new Vec3(fromEntity.method_23317(), fromEntity.method_23318(), fromEntity.method_23321()), new Vec3(toEntity.method_23317(), toEntity.method_23318(), toEntity.method_23321()));
    }

    public static ArrayList<Vec3> computePath(class_243 vec3d) {
        return PathUtils.computePath(new Vec3(PathUtils.mc.field_1724.method_23317(), PathUtils.mc.field_1724.method_23318(), PathUtils.mc.field_1724.method_23321()), new Vec3(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350));
    }

    public static ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
        if (!PathUtils.canPassThrough(new BlockPosX(topFrom.mc()))) {
            topFrom = topFrom.addVector(0.0, 1.0, 0.0);
        }
        AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();
        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<Vec3>();
        ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (Vec3 pathElm : pathFinderPath) {
            if (i != 0 && i != pathFinderPath.size() - 1) {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > 9.0) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.x(), pathElm.x());
                    double smallY = Math.min(lastDashLoc.y(), pathElm.y());
                    double smallZ = Math.min(lastDashLoc.z(), pathElm.z());
                    double bigX = Math.max(lastDashLoc.x(), pathElm.x());
                    double bigY = Math.max(lastDashLoc.y(), pathElm.y());
                    double bigZ = Math.max(lastDashLoc.z(), pathElm.z());
                    int x = (int)smallX;
                    block1: while ((double)x <= bigX) {
                        int y = (int)smallY;
                        while ((double)y <= bigY) {
                            int z = (int)smallZ;
                            while ((double)z <= bigZ) {
                                if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false;
                                    break block1;
                                }
                                ++z;
                            }
                            ++y;
                        }
                        ++x;
                    }
                }
                if (!canContinue) {
                    if (!path.contains(lastLoc.addVector(0.5, 0.0, 0.5))) {
                        path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    }
                    lastDashLoc = lastLoc;
                }
            } else {
                if (lastLoc != null && !path.contains(lastLoc.addVector(0.5, 0.0, 0.5))) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                if (!path.contains(pathElm.addVector(0.5, 0.0, 0.5))) {
                    path.add(pathElm.addVector(0.5, 0.0, 0.5));
                }
                lastDashLoc = pathElm;
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }
}

