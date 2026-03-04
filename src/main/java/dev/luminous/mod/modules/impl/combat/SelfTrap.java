/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.block.Blocks
 *  net.minecraft.block.Block
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec2f
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.core.impl.BreakManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.impl.movement.Velocity;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.enums.Timing;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.util.Hand;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;

public class SelfTrap
extends Module {
    public static SelfTrap INSTANCE;
    private final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    private final BooleanSetting godMode = this.add(new BooleanSetting("GodMode", true, () -> this.page.is(Page.GodMode)));
    private final SliderSetting breakTime = this.add(new SliderSetting("BreakTime", 2.0, 0.0, 3.0, () -> this.page.is(Page.GodMode)));
    private final BooleanSetting failedSkip = this.add(new BooleanSetting("FailedSkip", true, () -> this.page.is(Page.GodMode)));
    private final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, () -> this.page.is(Page.General)));
    private final BooleanSetting mineDownward = this.add(new BooleanSetting("MineDownward", false, () -> this.page.is(Page.General)));
    private final BooleanSetting extend = this.add(new BooleanSetting("Extend", true, () -> this.page.is(Page.General)));
    private final Timer timer = new Timer();
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8, () -> this.page.is(Page.General)));
    private final EnumSetting<Timing> timing = this.add(new EnumSetting<Timing>("Timing", Timing.All, () -> this.page.is(Page.General)));
    private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true, () -> this.page.is(Page.General)));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Break", true, () -> this.page.is(Page.General)).setParent());
    private final BooleanSetting eatPause = this.add(new BooleanSetting("EatingPause", true, () -> this.page.is(Page.General) && this.breakCrystal.isOpen()));
    private final BooleanSetting center = this.add(new BooleanSetting("Center", true, () -> this.page.is(Page.General)));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.is(Page.General)));
    private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true, () -> this.page.is(Page.General)));
    private final BooleanSetting head = this.add(new BooleanSetting("Head", true, () -> this.page.is(Page.General)));
    private final BooleanSetting feet = this.add(new BooleanSetting("Feet", true, () -> this.page.is(Page.General)));
    private final BooleanSetting chest = this.add(new BooleanSetting("Chest", true, () -> this.page.is(Page.General)));
    private final BindSetting headKey = this.add(new BindSetting("HeadKey", -1, () -> this.page.is(Page.General)));
    private final BooleanSetting inAir = this.add(new BooleanSetting("InAir", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting detectMining = this.add(new BooleanSetting("DetectMining", false, () -> this.page.is(Page.Check)));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting noBlockDisable = this.add(new BooleanSetting("NoBlockDisable", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting moveDisable = this.add(new BooleanSetting("MoveDisable", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting jumpDisable = this.add(new BooleanSetting("JumpDisable", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.getValue() == Page.Rotate));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false, () -> this.rotate.isOpen() && this.page.getValue() == Page.Rotate).setParent());
    private final BooleanSetting whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == Page.Rotate));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, () -> this.page.getValue() == Page.Rotate && this.yawStep.isOpen()));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.page.getValue() == Page.Rotate && this.yawStep.isOpen()).setParent());
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 20.0, 0.0, 360.0, 0.1, () -> this.checkFov.isOpen() && this.page.getValue() == Page.Rotate && this.yawStep.isOpen()));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.page.getValue() == Page.Rotate && this.yawStep.isOpen()));
    public class_243 directionVec = null;
    double startX = 0.0;
    double startY = 0.0;
    double startZ = 0.0;
    int progress = 0;
    private boolean shouldCenter = true;
    public static final List<class_2338> airList;

    public SelfTrap() {
        super("SelfTrap", Module.Category.Combat);
        this.setChinese("\u81ea\u6211\u56f0\u4f4f");
        INSTANCE = this;
        Alien.EVENT_BUS.subscribe(new SelfTrapTick());
    }

    public static boolean selfIntersectPos(class_2338 pos) {
        return SelfTrap.mc.field_1724.method_5829().method_994(new class_238(pos));
    }

    public static class_241 getRotationTo(class_243 posFrom, class_243 posTo) {
        class_243 vec3d = posTo.method_1020(posFrom);
        return SelfTrap.getRotationFromVec(vec3d);
    }

    private static class_241 getRotationFromVec(class_243 vec) {
        double d = vec.field_1352;
        double d2 = vec.field_1350;
        double xz = Math.hypot(d, d2);
        d2 = vec.field_1350;
        double d3 = vec.field_1352;
        double yaw = SelfTrap.normalizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - 90.0);
        double pitch = SelfTrap.normalizeAngle(Math.toDegrees(-Math.atan2(vec.field_1351, xz)));
        return new class_241((float)yaw, (float)pitch);
    }

    @EventListener
    public void onRotate(RotationEvent event) {
        if (this.directionVec != null && this.rotate.getValue() && this.shouldYawStep()) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
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

    @Override
    public boolean onEnable() {
        if (!SelfTrap.nullCheck()) {
            this.startX = SelfTrap.mc.field_1724.method_23317();
            this.startY = SelfTrap.mc.field_1724.method_23318();
            this.startZ = SelfTrap.mc.field_1724.method_23321();
            this.shouldCenter = true;
        } else if (this.moveDisable.getValue() || this.jumpDisable.getValue()) {
            this.disable();
        }
        return false;
    }

    @EventListener
    public void onTick(ClientTickEvent event) {
        if (!(SelfTrap.nullCheck() || this.inventory.getValue() && !EntityUtil.inInventory() || this.timing.is(Timing.Pre) && event.isPost() || this.timing.is(Timing.Post) && event.isPre() || !this.timer.passed((long)this.placeDelay.getValue()))) {
            this.directionVec = null;
            this.progress = 0;
            if (!MovementUtil.isMoving() && !SelfTrap.mc.field_1690.field_1903.method_1434()) {
                this.startX = SelfTrap.mc.field_1724.method_23317();
                this.startY = SelfTrap.mc.field_1724.method_23318();
                this.startZ = SelfTrap.mc.field_1724.method_23321();
            }
            class_2338 pos = EntityUtil.getPlayerPos(true);
            double distanceToStart = class_3532.method_15355((float)((float)SelfTrap.mc.field_1724.method_5649(this.startX, this.startY, this.startZ)));
            if (this.getBlock() == -1) {
                if (this.noBlockDisable.getValue()) {
                    this.disable();
                }
            } else if (!(this.moveDisable.getValue() && distanceToStart > 1.0 || this.jumpDisable.getValue() && SelfTrap.mc.field_1724.field_3913.field_3904)) {
                if (!(Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue() || this.usingPause.getValue() && SelfTrap.mc.field_1724.method_6115() || !this.inAir.getValue() && !SelfTrap.mc.field_1724.method_24828())) {
                    if (this.head.getValue()) {
                        class_2248 block = BlockUtil.getBlock(pos.method_10084());
                        if (block != class_2246.field_10540 && block != class_2246.field_9987) {
                            this.tryPlaceBlock(pos.method_10086(2));
                        } else {
                            this.clickDown(pos.method_10086(2));
                        }
                    }
                    if (this.feet.getValue()) {
                        this.doSurround(pos);
                    }
                    if (this.chest.getValue()) {
                        this.doSurround(pos.method_10084());
                    }
                    airList.clear();
                }
            } else {
                this.disable();
            }
        }
    }

    private boolean shouldYawStep() {
        return this.whenElytra.getValue() || !SelfTrap.mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() && !Velocity.INSTANCE.noRotation() : false;
    }

    @EventListener(priority=-1)
    public void onMove(MoveEvent event) {
        if (!SelfTrap.nullCheck() && this.center.getValue() && !SelfTrap.mc.field_1724.method_6128()) {
            class_2338 blockPos = EntityUtil.getPlayerPos(true);
            if (SelfTrap.mc.field_1724.method_23317() - (double)blockPos.method_10263() - 0.5 <= 0.2 && SelfTrap.mc.field_1724.method_23317() - (double)blockPos.method_10263() - 0.5 >= -0.2 && SelfTrap.mc.field_1724.method_23321() - (double)blockPos.method_10260() - 0.5 <= 0.2 && SelfTrap.mc.field_1724.method_23321() - 0.5 - (double)blockPos.method_10260() >= -0.2) {
                if (this.shouldCenter && (SelfTrap.mc.field_1724.method_24828() || MovementUtil.isMoving())) {
                    event.setX(0.0);
                    event.setZ(0.0);
                    this.shouldCenter = false;
                }
            } else if (this.shouldCenter) {
                class_243 centerPos = EntityUtil.getPlayerPos(true).method_46558();
                float rotation = SelfTrap.getRotationTo((class_243)SelfTrap.mc.field_1724.method_19538(), (class_243)centerPos).field_1343;
                float yawRad = rotation / 180.0f * (float)Math.PI;
                double dist = SelfTrap.mc.field_1724.method_19538().method_1022(new class_243(centerPos.field_1352, SelfTrap.mc.field_1724.method_23318(), centerPos.field_1350));
                double cappedSpeed = Math.min(0.2873, dist);
                double x = (double)(-((float)Math.sin(yawRad))) * cappedSpeed;
                double z = (double)((float)Math.cos(yawRad)) * cappedSpeed;
                event.setX(x);
                event.setZ(z);
            }
        }
    }

    private void doSurround(class_2338 pos) {
        for (class_2350 i : class_2350.values()) {
            if (i == class_2350.field_11036) continue;
            class_2338 offsetPos = pos.method_10093(i);
            if (this.godMode.getValue()) {
                for (BreakManager.BreakData breakData : Alien.BREAK.breakMap.values()) {
                    if (breakData.getEntity() == null || this.failedSkip.getValue() && breakData.failed || !breakData.pos.equals((Object)offsetPos)) continue;
                    if (!((double)breakData.timer.getMs() >= this.breakTime.getValue() * 1000.0)) break;
                    airList.add(offsetPos);
                    break;
                }
            }
            if (BlockUtil.getPlaceSide(offsetPos) != null) {
                this.tryPlaceBlock(offsetPos);
            } else if (BlockUtil.canReplace(offsetPos)) {
                this.tryPlaceBlock(this.getHelperPos(offsetPos));
            }
            if (!SelfTrap.selfIntersectPos(offsetPos) || !this.extend.getValue()) continue;
            for (class_2350 i2 : class_2350.values()) {
                if (i2 == class_2350.field_11036) continue;
                class_2338 offsetPos2 = offsetPos.method_10093(i2);
                if (this.godMode.getValue()) {
                    for (BreakManager.BreakData breakDatax : Alien.BREAK.breakMap.values()) {
                        if (breakDatax.getEntity() == null || this.failedSkip.getValue() && breakDatax.failed || !breakDatax.pos.equals((Object)offsetPos2)) continue;
                        if (!((double)breakDatax.timer.getMs() >= this.breakTime.getValue() * 1000.0)) break;
                        airList.add(offsetPos2);
                        break;
                    }
                }
                if (SelfTrap.selfIntersectPos(offsetPos2)) {
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

    private boolean faceVector(class_243 directionVec) {
        if (!this.shouldYawStep()) {
            Alien.ROTATION.lookAt(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        return Alien.ROTATION.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkFov.getValue();
    }

    private void clickDown(class_2338 pos) {
        int block;
        if (!(pos == null || this.detectMining.getValue() && Alien.BREAK.isMining(pos) || (block = this.getBlock()) == -1)) {
            class_2350 side = class_2350.field_11033;
            class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
            if (BlockUtil.clientCanPlace(pos, true) && airList.contains(pos) && (!this.rotate.getValue() || this.faceVector(directionVec))) {
                if (this.breakCrystal.getValue()) {
                    CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.eatPause.getValue());
                } else if (BlockUtil.hasEntity(pos, false)) {
                    return;
                }
                int old = SelfTrap.mc.field_1724.method_31548().field_7545;
                this.doSwap(block);
                BlockUtil.placedPos.add(pos);
                if (BlockUtil.allowAirPlace()) {
                    BlockUtil.airPlace(pos, false, class_1268.field_5808, this.packetPlace.getValue());
                } else {
                    BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), false, class_1268.field_5808, this.packetPlace.getValue());
                }
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
                this.timer.reset();
            }
        }
    }

    private void tryPlaceBlock(class_2338 pos) {
        if (!(pos == null || this.detectMining.getValue() && Alien.BREAK.isMining(pos))) {
            class_2350 side;
            int block;
            class_2338 self = EntityUtil.getPlayerPos(true);
            if (!(this.mineDownward.getValue() && Objects.equals(SpeedMine.getBreakPos(), self.method_10074()) && Objects.equals(SpeedMine.getBreakPos(), pos) || !((double)this.progress < this.blocksPer.getValue()) || (block = this.getBlock()) == -1 || (side = BlockUtil.getPlaceSide(pos)) == null)) {
                class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
                if (BlockUtil.canPlace(pos, 6.0, true) && (!this.rotate.getValue() || this.faceVector(directionVec))) {
                    if (this.breakCrystal.getValue()) {
                        CombatUtil.attackCrystal(pos, this.rotate.getValue(), this.eatPause.getValue());
                    } else if (BlockUtil.hasEntity(pos, false)) {
                        return;
                    }
                    int old = SelfTrap.mc.field_1724.method_31548().field_7545;
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

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, SelfTrap.mc.field_1724.method_31548().field_7545);
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

    static {
        airList = new ArrayList<class_2338>();
    }

    public static enum Page {
        General,
        Rotate,
        Check,
        GodMode;

    }

    public class SelfTrapTick {
        boolean pressed = false;

        @EventListener
        public void onTick(ClientTickEvent event) {
            if (!(Module.nullCheck() || SelfTrap.this.timing.is(Timing.Pre) && event.isPost() || SelfTrap.this.timing.is(Timing.Post) && event.isPre())) {
                if (!SelfTrap.this.headKey.isPressed()) {
                    this.pressed = false;
                } else if (!this.pressed) {
                    this.pressed = true;
                    SelfTrap.this.directionVec = null;
                    SelfTrap.this.progress = 0;
                    class_2338 pos = EntityUtil.getPlayerPos(true);
                    if (SelfTrap.this.getBlock() != -1 && (SelfTrap.this.inAir.getValue() || Wrapper.mc.field_1724.method_24828())) {
                        class_2248 block = BlockUtil.getBlock(pos.method_10084());
                        if (block != class_2246.field_10540 && block != class_2246.field_9987) {
                            SelfTrap.this.tryPlaceBlock(pos.method_10086(2));
                        } else {
                            SelfTrap.this.clickDown(pos.method_10086(2));
                        }
                    }
                }
            }
        }
    }
}

