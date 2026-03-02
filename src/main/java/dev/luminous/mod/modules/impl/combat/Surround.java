/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_742
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.impl.movement.Velocity;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.enums.Timing;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.Objects;
import net.minecraft.class_1268;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_742;

public class Surround
extends Module {
    public static Surround INSTANCE;
    public final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    public final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, () -> this.page.is(Page.General)));
    private final BooleanSetting mineDownward = this.add(new BooleanSetting("MineDownward", false, () -> this.page.is(Page.General)));
    public final BooleanSetting extend = this.add(new BooleanSetting("Extend", true, () -> this.page.is(Page.General))).setParent();
    public final BooleanSetting onlySelf = this.add(new BooleanSetting("OnlySelf", false, () -> this.page.is(Page.General) && this.extend.isOpen()));
    public final BooleanSetting inAir = this.add(new BooleanSetting("InAir", true, () -> this.page.is(Page.Check)));
    private final Timer timer = new Timer();
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8, () -> this.page.is(Page.General)));
    private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true, () -> this.page.is(Page.General)));
    private final EnumSetting<Timing> timing = this.add(new EnumSetting<Timing>("Timing", Timing.All, () -> this.page.is(Page.General)));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Break", true, () -> this.page.is(Page.General)).setParent());
    private final BooleanSetting eatPause = this.add(new BooleanSetting("EatingPause", true, () -> this.page.is(Page.General) && this.breakCrystal.isOpen()));
    private final BooleanSetting center = this.add(new BooleanSetting("Center", true, () -> this.page.is(Page.General)));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.is(Page.General)));
    private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true, () -> this.page.is(Page.General)));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.getValue() == Page.Rotate));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false, () -> this.rotate.isOpen() && this.page.getValue() == Page.Rotate).setParent());
    private final BooleanSetting whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == Page.Rotate));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, () -> this.page.getValue() == Page.Rotate && this.yawStep.isOpen()));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.page.getValue() == Page.Rotate && this.yawStep.isOpen()).setParent());
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 20.0, 0.0, 360.0, 0.1, () -> this.checkFov.isOpen() && this.page.getValue() == Page.Rotate && this.yawStep.isOpen()));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.page.getValue() == Page.Rotate && this.yawStep.isOpen()));
    private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", false, () -> this.page.is(Page.Check)));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting moveDisable = this.add(new BooleanSetting("MoveDisable", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting jumpDisable = this.add(new BooleanSetting("JumpDisable", true, () -> this.page.is(Page.Check)));
    public class_243 directionVec = null;
    double startX = 0.0;
    double startY = 0.0;
    double startZ = 0.0;
    int progress = 0;
    private boolean shouldCenter = true;

    public Surround() {
        super("Surround", "Surrounds you with Obsidian", Module.Category.Combat);
        this.setChinese("\u56f4\u811a");
        INSTANCE = this;
    }

    public static boolean selfIntersectPos(class_2338 pos) {
        return Surround.mc.field_1724.method_5829().method_994(new class_238(pos));
    }

    public static boolean otherIntersectPos(class_2338 pos) {
        for (class_742 player : Alien.THREAD.getPlayers()) {
            if (!player.method_5829().method_994(new class_238(pos))) continue;
            return true;
        }
        return false;
    }

    public static class_241 getRotationTo(class_243 posFrom, class_243 posTo) {
        class_243 vec3d = posTo.method_1020(posFrom);
        return Surround.getRotationFromVec(vec3d);
    }

    private static class_241 getRotationFromVec(class_243 vec) {
        double d = vec.field_1352;
        double d2 = vec.field_1350;
        double xz = Math.hypot(d, d2);
        d2 = vec.field_1350;
        double d3 = vec.field_1352;
        double yaw = Surround.normalizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - 90.0);
        double pitch = Surround.normalizeAngle(Math.toDegrees(-Math.atan2(vec.field_1351, xz)));
        return new class_241((float)yaw, (float)pitch);
    }

    private static double normalizeAngle(double angleIn) {
        double d;
        double angle = angleIn % 360.0;
        if (d >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    @EventListener
    public void onRotate(RotationEvent event) {
        if (this.directionVec != null && this.rotate.getValue() && this.shouldYawStep()) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @EventListener
    public void onTick(ClientTickEvent event) {
        if (!(Surround.nullCheck() || this.inventory.getValue() && !EntityUtil.inInventory() || this.timing.is(Timing.Pre) && event.isPost() || this.timing.is(Timing.Post) && event.isPre() || !this.timer.passed((long)this.placeDelay.getValue()))) {
            this.directionVec = null;
            this.progress = 0;
            if (!MovementUtil.isMoving() && !Surround.mc.field_1690.field_1903.method_1434()) {
                this.startX = Surround.mc.field_1724.method_23317();
                this.startY = Surround.mc.field_1724.method_23318();
                this.startZ = Surround.mc.field_1724.method_23321();
            }
            double distanceToStart = class_3532.method_15355((float)((float)Surround.mc.field_1724.method_5649(this.startX, this.startY, this.startZ)));
            if (this.getBlock() == -1) {
                CommandManager.sendMessageId("\u00a74No block found", this.hashCode() - 1);
                this.disable();
            } else if (!(this.moveDisable.getValue() && distanceToStart > 1.0 || this.jumpDisable.getValue() && Surround.mc.field_1724.field_3913.field_3904)) {
                if (!(Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue() || this.usingPause.getValue() && Surround.mc.field_1724.method_6115() || !this.inAir.getValue() && !Surround.mc.field_1724.method_24828())) {
                    this.doSurround(new BlockPosX(Surround.mc.field_1724.method_23317(), Surround.mc.field_1724.method_23318(), Surround.mc.field_1724.method_23321()));
                    this.doSurround(new BlockPosX(Surround.mc.field_1724.method_23317(), Surround.mc.field_1724.method_23318() + 0.8, Surround.mc.field_1724.method_23321()));
                }
            } else {
                this.disable();
            }
        }
    }

    public void doSurround(class_2338 pos) {
        for (class_2350 i : class_2350.values()) {
            if (i == class_2350.field_11036) continue;
            class_2338 offsetPos = pos.method_10093(i);
            if (BlockUtil.getPlaceSide(offsetPos) != null) {
                this.tryPlaceBlock(offsetPos);
            } else if (BlockUtil.canReplace(offsetPos)) {
                this.tryPlaceBlock(this.getHelperPos(offsetPos));
            }
            if (!Surround.selfIntersectPos(offsetPos) && (this.onlySelf.getValue() || !Surround.otherIntersectPos(offsetPos)) || !this.extend.getValue()) continue;
            for (class_2350 i2 : class_2350.values()) {
                if (i2 == class_2350.field_11036) continue;
                class_2338 offsetPos2 = offsetPos.method_10093(i2);
                if (Surround.selfIntersectPos(offsetPos2) || !this.onlySelf.getValue() && Surround.otherIntersectPos(offsetPos2)) {
                    for (class_2350 i3 : class_2350.values()) {
                        if (i3 == class_2350.field_11036) continue;
                        this.tryPlaceBlock(offsetPos2);
                        class_2338 offsetPos3 = offsetPos2.method_10093(i3);
                        this.tryPlaceBlock(BlockUtil.getPlaceSide(offsetPos3) == null && BlockUtil.canReplace(offsetPos3) ? this.getHelperPos(offsetPos3) : offsetPos3);
                    }
                }
                this.tryPlaceBlock(BlockUtil.getPlaceSide(offsetPos2) == null && BlockUtil.canReplace(offsetPos2) ? this.getHelperPos(offsetPos2) : offsetPos2);
            }
        }
    }

    @Override
    public boolean onEnable() {
        if (!Surround.nullCheck()) {
            this.startX = Surround.mc.field_1724.method_23317();
            this.startY = Surround.mc.field_1724.method_23318();
            this.startZ = Surround.mc.field_1724.method_23321();
            this.shouldCenter = true;
        } else if (this.moveDisable.getValue() || this.jumpDisable.getValue()) {
            this.disable();
        }
        return false;
    }

    private boolean shouldYawStep() {
        return this.whenElytra.getValue() || !Surround.mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() && !Velocity.INSTANCE.noRotation() : false;
    }

    @EventListener(priority=-1)
    public void onMove(MoveEvent event) {
        if (!Surround.nullCheck() && this.center.getValue() && !Surround.mc.field_1724.method_6128()) {
            class_2338 blockPos = EntityUtil.getPlayerPos(true);
            if (Surround.mc.field_1724.method_23317() - (double)blockPos.method_10263() - 0.5 <= 0.2 && Surround.mc.field_1724.method_23317() - (double)blockPos.method_10263() - 0.5 >= -0.2 && Surround.mc.field_1724.method_23321() - (double)blockPos.method_10260() - 0.5 <= 0.2 && Surround.mc.field_1724.method_23321() - 0.5 - (double)blockPos.method_10260() >= -0.2) {
                if (this.shouldCenter && (Surround.mc.field_1724.method_24828() || MovementUtil.isMoving())) {
                    event.setX(0.0);
                    event.setZ(0.0);
                    this.shouldCenter = false;
                }
            } else if (this.shouldCenter) {
                class_243 centerPos = EntityUtil.getPlayerPos(true).method_46558();
                float rotation = Surround.getRotationTo((class_243)Surround.mc.field_1724.method_19538(), (class_243)centerPos).field_1343;
                float yawRad = rotation / 180.0f * (float)Math.PI;
                double dist = Surround.mc.field_1724.method_19538().method_1022(new class_243(centerPos.field_1352, Surround.mc.field_1724.method_23318(), centerPos.field_1350));
                double cappedSpeed = Math.min(0.2873, dist);
                double x = (double)(-((float)Math.sin(yawRad))) * cappedSpeed;
                double z = (double)((float)Math.cos(yawRad)) * cappedSpeed;
                event.setX(x);
                event.setZ(z);
            }
        }
    }

    private void tryPlaceBlock(class_2338 pos) {
        if (!(pos == null || this.detectMining.getValue() && Alien.BREAK.isMining(pos) || !((double)this.progress < this.blocksPer.getValue()))) {
            class_2350 side;
            int block;
            class_2338 self = EntityUtil.getPlayerPos(true);
            if (!(this.mineDownward.getValue() && Objects.equals(SpeedMine.getBreakPos(), self.method_10074()) && Objects.equals(SpeedMine.getBreakPos(), pos) || (block = this.getBlock()) == -1 || (side = BlockUtil.getPlaceSide(pos)) == null)) {
                class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
                if (BlockUtil.canPlace(pos, 6.0, true) && (!this.rotate.getValue() || this.faceVector(directionVec))) {
                    if (this.breakCrystal.getValue()) {
                        CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.eatPause.getValue());
                    } else if (BlockUtil.hasEntity(pos, false)) {
                        return;
                    }
                    int old = Surround.mc.field_1724.method_31548().field_7545;
                    this.doSwap(block);
                    BlockUtil.placedPos.add(pos);
                    if (BlockUtil.allowAirPlace()) {
                        BlockUtil.airPlace(pos, false, class_1268.field_5808, this.packetPlace.getValue());
                    } else {
                        BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), false, class_1268.field_5808, this.packetPlace.getValue());
                    }
                    this.timer.reset();
                    if (this.inventory.getValue()) {
                        this.doSwap(block);
                        EntityUtil.syncInventory();
                    } else {
                        this.doSwap(old);
                    }
                    if (this.rotate.getValue() && !this.shouldYawStep()) {
                        Alien.ROTATION.snapBack();
                    }
                    ++this.progress;
                }
            }
        }
    }

    private boolean faceVector(class_243 directionVec) {
        if (!this.shouldYawStep()) {
            Alien.ROTATION.lookAt(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        return Alien.ROTATION.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkFov.getValue();
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, Surround.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getBlock() {
        if (this.inventory.getValue()) {
            return InventoryUtil.findBlockInventorySlot(class_2246.field_10540) == -1 && this.enderChest.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10443) : InventoryUtil.findBlockInventorySlot(class_2246.field_10540);
        }
        return InventoryUtil.findBlock(class_2246.field_10540) == -1 && this.enderChest.getValue() ? InventoryUtil.findBlock(class_2246.field_10443) : InventoryUtil.findBlock(class_2246.field_10540);
    }

    public class_2338 getHelperPos(class_2338 pos) {
        for (class_2350 i : class_2350.values()) {
            if (this.detectMining.getValue() && Alien.BREAK.isMining(pos.method_10093(i)) || !BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) || !BlockUtil.canPlace(pos.method_10093(i))) continue;
            return pos.method_10093(i);
        }
        return null;
    }

    public static enum Page {
        General,
        Rotate,
        Check;

    }
}

