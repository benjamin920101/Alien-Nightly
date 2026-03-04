/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.projectile.thrown.EnderPearlEntity
 *  net.minecraft.item.Items
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Position
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.util.math.Vec3d
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.EntitySpawnedEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.core.impl.RotationManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;

public class PearlBlocker
extends Module {
    public static PearlBlocker INSTANCE;
    private final BooleanSetting ignoreFriends = this.add(new BooleanSetting("IgnoreFriends", true));
    private final SliderSetting range = this.add(new SliderSetting("Range", 10.0, 1.0, 30.0));
    private final BooleanSetting obsidianOnly = this.add(new BooleanSetting("ObsidianOnly", true));
    private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting predictive = this.add(new BooleanSetting("Predictive", true));
    private final BooleanSetting debug = this.add(new BooleanSetting("Debug", false));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private final BooleanSetting silentRotation = this.add(new BooleanSetting("SilentRotation", false));
    private final EnumSetting wallSize = this.add(new EnumSetting<WallSize>("WallSize", WallSize.SMALL));
    private final SliderSetting placeCooldown = this.add(new SliderSetting("PlaceCooldown", 80.0, 50.0, 1000.0));
    private final SliderSetting rotateCooldown = this.add(new SliderSetting("RotateCooldown", 60.0, 30.0, 1000.0));
    private final Map<String, Long> lastPlaceTime = new HashMap<String, Long>();
    private long lastRotateTime = 0L;
    private float[] lastRotation = null;

    public PearlBlocker() {
        super("PearlBlocker", Module.Category.Combat);
        this.setChinese("\u73cd\u73e0\u963b\u6321");
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.lastPlaceTime.clear();
        this.lastRotateTime = 0L;
        this.lastRotation = null;
    }

    @EventListener
    public void onEntitySpawn(EntitySpawnedEvent event) {
        if (PearlBlocker.nullCheck()) {
            return;
        }
        class_1297 e = event.getEntity();
        if (!(e instanceof class_1684)) {
            return;
        }
        class_1684 pearl = (class_1684)e;
        class_1297 owner = pearl.method_24921();
        if (!(owner instanceof class_1657)) {
            return;
        }
        class_1657 player = (class_1657)owner;
        if (player == PearlBlocker.mc.field_1724) {
            return;
        }
        if (this.ignoreFriends.getValue()) {
            try {
                if (Alien.FRIEND != null && Alien.FRIEND.getClass().getMethod("contains", String.class).invoke((Object)Alien.FRIEND, player.method_5477().getString()).equals(Boolean.TRUE)) {
                    return;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        double maxRange = this.range.getValue();
        if (PearlBlocker.mc.field_1724.method_5858((class_1297)pearl) > maxRange * maxRange) {
            return;
        }
        String name = player.method_5477().getString();
        long now = System.currentTimeMillis();
        if ((double)(now - this.lastPlaceTime.getOrDefault(name, 0L)) < this.placeCooldown.getValue()) {
            return;
        }
        if (this.debug.getValue()) {
            this.sendMessage("Blocking pearl in flight from: " + name);
        }
        if (this.blockPearlPath(pearl)) {
            this.lastPlaceTime.put(name, now);
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (PearlBlocker.nullCheck() || !this.predictive.getValue()) {
            return;
        }
        long now = System.currentTimeMillis();
        double maxRange = this.range.getValue();
        for (class_1657 player : PearlBlocker.mc.field_1687.method_18456()) {
            String name;
            if (player == PearlBlocker.mc.field_1724 || PearlBlocker.mc.field_1724.method_5858((class_1297)player) > maxRange * maxRange) continue;
            if (this.ignoreFriends.getValue()) {
                try {
                    if (Alien.FRIEND != null && Alien.FRIEND.getClass().getMethod("contains", String.class).invoke((Object)Alien.FRIEND, player.method_5477().getString()).equals(Boolean.TRUE)) {
                        continue;
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (!this.isHoldingPearl(player) || !this.isLikelyToThrow(player) || (double)(now - this.lastPlaceTime.getOrDefault(name = player.method_5477().getString(), 0L)) < this.placeCooldown.getValue()) continue;
            if (this.debug.getValue()) {
                this.sendMessage("Predictive block: " + name);
            }
            if (!this.blockPredictive(player)) continue;
            this.lastPlaceTime.put(name, now);
        }
    }

    private boolean isHoldingPearl(class_1657 player) {
        return player.method_6047().method_7909() == class_1802.field_8634 || player.method_6079().method_7909() == class_1802.field_8634;
    }

    private boolean isLikelyToThrow(class_1657 player) {
        return player.method_36455() < -30.0f || !player.method_24828() || player.method_18798().method_1027() > 0.2;
    }

    private boolean blockPredictive(class_1657 player) {
        boolean placed = false;
        class_243 pos = player.method_19538();
        class_243 look = player.method_5720().method_1029();
        int radius = this.isWallBig() ? 2 : 1;
        for (int d = 2; d <= 4; ++d) {
            class_243 centerVec = pos.method_1019(look.method_1021((double)d));
            class_2338 center = class_2338.method_49638((class_2374)centerVec);
            if (this.placeWallAt(center, radius)) {
                placed = true;
            }
            if (placed) break;
        }
        return placed;
    }

    private boolean blockPearlPath(class_1684 pearl) {
        class_243 vel = pearl.method_18798();
        if (vel.method_1027() < 0.01) {
            return false;
        }
        class_243 dir = vel.method_1029();
        class_243 pos = pearl.method_19538();
        int radius = this.isWallBig() ? 2 : 1;
        for (int d = 3; d <= 7; ++d) {
            class_243 future = pos.method_1019(dir.method_1021((double)d));
            class_2338 center = class_2338.method_49638((class_2374)future);
            if (!this.placeWallAt(center, radius)) continue;
            return true;
        }
        return false;
    }

    private boolean placeWallAt(class_2338 center, int radius) {
        boolean any = false;
        for (int x = -radius; x <= radius; ++x) {
            for (int z = -radius; z <= radius; ++z) {
                for (int y = 0; y <= 1; ++y) {
                    class_2338 target = center.method_10069(x, y, z);
                    if (!this.placeBlockSmart(target)) continue;
                    any = true;
                }
            }
        }
        return any;
    }

    private boolean isWallBig() {
        Object v = this.wallSize.getValue();
        if (v == null) {
            return false;
        }
        String s = v.toString().toLowerCase();
        return s.equals("big") || s.contains("5x5") || s.equals("large");
    }

    private boolean isWallSmall() {
        Object v = this.wallSize.getValue();
        if (v == null) {
            return false;
        }
        String s = v.toString().toLowerCase();
        return s.equals("Small") || s.contains("3x3") || s.equals("Smaller");
    }

    private boolean placeBlockSmart(class_2338 pos) {
        int slot;
        if (PearlBlocker.mc.field_1687 == null || PearlBlocker.mc.field_1724 == null) {
            return false;
        }
        class_2338 me = PearlBlocker.mc.field_1724.method_24515();
        if (me.equals((Object)pos) || me.method_10084().equals((Object)pos)) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        if (this.obsidianOnly.getValue()) {
            int n = slot = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
            if (slot == -1) {
                if (this.debug.getValue()) {
                    this.sendMessage("No obsidian found for PearlBlocker");
                }
                return false;
            }
        } else {
            int n = slot = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
            if (slot == -1 && (slot = InventoryUtil.findItem(class_1802.field_8634)) == -1) {
                if (this.debug.getValue()) {
                    this.sendMessage("No suitable block found for PearlBlocker");
                }
                return false;
            }
        }
        int oldSlot = PearlBlocker.mc.field_1724.method_31548().field_7545;
        if (this.inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, oldSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
        if (this.rotate.getValue()) {
            class_243 center = class_243.method_24953((class_2382)pos);
            float[] rotations = RotationManager.getRotation(center);
            long now = System.currentTimeMillis();
            if (this.silentRotation.getValue()) {
                try {
                    if (this.lastRotation == null || Math.abs(rotations[0] - this.lastRotation[0]) > 2.0f || Math.abs(rotations[1] - this.lastRotation[1]) > 2.0f || (double)(now - this.lastRotateTime) > this.rotateCooldown.getValue()) {
                        Alien.ROTATION.snapAt(rotations[0], rotations[1]);
                        this.lastRotateTime = now;
                        this.lastRotation = rotations;
                    }
                }
                catch (Exception ex) {
                    Alien.ROTATION.lookAt(center);
                }
            } else {
                Alien.ROTATION.lookAt(center);
            }
        }
        try {
            BlockUtil.placeBlock(pos, this.rotate.getValue());
        }
        catch (Exception ex) {
            if (this.inventorySwap.getValue()) {
                InventoryUtil.inventorySwap(slot, oldSlot);
            } else {
                InventoryUtil.switchToSlot(oldSlot);
            }
            return false;
        }
        if (this.inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, oldSlot);
            try {
                Thread.sleep(1L);
            }
            catch (InterruptedException interruptedException) {}
        } else {
            InventoryUtil.switchToSlot(oldSlot);
        }
        return true;
    }

    public static boolean nullCheck() {
        return PearlBlocker.mc.field_1724 == null || PearlBlocker.mc.field_1687 == null;
    }

    private static enum WallSize {
        SMALL,
        BIG;

    }
}

