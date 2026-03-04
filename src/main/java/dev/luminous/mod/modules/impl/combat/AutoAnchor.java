/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket$Action
 *  net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.client.util.math.MatrixStack
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.Render3DEvent;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.entity.PlayerEntityPredict;
import dev.luminous.api.utils.math.AnimateUtil;
import dev.luminous.api.utils.math.ExplosionUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.Aura;
import dev.luminous.mod.modules.impl.combat.AutoCrystal;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.impl.movement.Velocity;
import dev.luminous.mod.modules.impl.player.AirPlace;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import dev.luminous.mod.modules.settings.enums.Timing;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.client.util.math.MatrixStack;

public class AutoAnchor
extends Module {
    public static AutoAnchor INSTANCE;
    static class_243 placeVec3d;
    static class_243 curVec3d;
    public final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    public final SliderSetting range = this.add(new SliderSetting("Range", 5.0, 0.0, 6.0, 0.1, () -> this.page.getValue() == Page.General).setSuffix("m"));
    public final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 8.0, 0.1, 12.0, 0.1, () -> this.page.getValue() == Page.General).setSuffix("m"));
    public final SliderSetting minDamage = this.add(new SliderSetting("Min", 4.0, 0.0, 36.0, 0.1, () -> this.page.getValue() == Page.Interact).setSuffix("dmg"));
    public final SliderSetting breakMin = this.add(new SliderSetting("ExplosionMin", 4.0, 0.0, 36.0, 0.1, () -> this.page.getValue() == Page.Interact).setSuffix("dmg"));
    public final SliderSetting headDamage = this.add(new SliderSetting("ForceHead", 7.0, 0.0, 36.0, 0.1, () -> this.page.getValue() == Page.Interact).setSuffix("dmg"));
    private final SliderSetting selfPredict = this.add(new SliderSetting("SelfPredict", 4, 0, 10, () -> this.page.getValue() == Page.Predict).setSuffix("ticks"));
    private final SliderSetting predictTicks = this.add(new SliderSetting("Predict", 4, 0, 10, () -> this.page.getValue() == Page.Predict).setSuffix("ticks"));
    private final SliderSetting simulation = this.add(new SliderSetting("Simulation", 5.0, 0.0, 20.0, 1.0, () -> this.page.getValue() == Page.Predict));
    private final SliderSetting maxMotionY = this.add(new SliderSetting("MaxMotionY", 0.34, 0.0, 2.0, 0.01, () -> this.page.getValue() == Page.Predict));
    private final BooleanSetting step = this.add(new BooleanSetting("Step", false, () -> this.page.getValue() == Page.Predict));
    private final BooleanSetting doubleStep = this.add(new BooleanSetting("DoubleStep", false, () -> this.page.getValue() == Page.Predict));
    private final BooleanSetting jump = this.add(new BooleanSetting("Jump", false, () -> this.page.getValue() == Page.Predict));
    private final BooleanSetting inBlockPause = this.add(new BooleanSetting("InBlockPause", true, () -> this.page.getValue() == Page.Predict));
    final ArrayList<class_2338> chargeList = new ArrayList();
    private final BooleanSetting assist = this.add(new BooleanSetting("Assist", true, () -> this.page.getValue() == Page.Assist));
    private final BooleanSetting obsidian = this.add(new BooleanSetting("Obsidian", true, () -> this.page.getValue() == Page.Assist));
    private final BooleanSetting checkMine = this.add(new BooleanSetting("DetectMining", false, () -> this.page.getValue() == Page.Assist));
    private final SliderSetting assistRange = this.add(new SliderSetting("AssistRange", 5.0, 0.0, 6.0, 0.1, () -> this.page.getValue() == Page.Assist).setSuffix("m"));
    private final SliderSetting assistDamage = this.add(new SliderSetting("AssistDamage", 6.0, 0.0, 36.0, 0.1, () -> this.page.getValue() == Page.Assist).setSuffix("h"));
    private final SliderSetting delay = this.add(new SliderSetting("AssistDelay", 0.1, 0.0, 1.0, 0.01, () -> this.page.getValue() == Page.Assist).setSuffix("s"));
    private final BooleanSetting preferCrystal = this.add(new BooleanSetting("PreferCrystal", false, () -> this.page.getValue() == Page.General));
    private final BooleanSetting thread = this.add(new BooleanSetting("Thread", false, () -> this.page.getValue() == Page.General));
    private final BooleanSetting light = this.add(new BooleanSetting("LessCPU", true, () -> this.page.getValue() == Page.General));
    private final BooleanSetting inventorySwap = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.getValue() == Page.General));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("BreakCrystal", true, () -> this.page.getValue() == Page.General).setParent());
    private final BooleanSetting spam = this.add(new BooleanSetting("Spam", true, () -> this.page.getValue() == Page.General).setParent());
    private final BooleanSetting mineSpam = this.add(new BooleanSetting("OnlyMining", true, () -> this.page.getValue() == Page.General && this.spam.isOpen()));
    private final BooleanSetting spamPlace = this.add(new BooleanSetting("Fast", true, () -> this.page.getValue() == Page.General).setParent());
    private final BooleanSetting inSpam = this.add(new BooleanSetting("WhenSpamming", true, () -> this.page.getValue() == Page.General && this.spamPlace.isOpen()));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, () -> this.page.getValue() == Page.General));
    private final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("Swing", SwingSide.All, () -> this.page.getValue() == Page.General));
    private final EnumSetting<Timing> timing = this.add(new EnumSetting<Timing>("Timing", Timing.All, () -> this.page.getValue() == Page.General));
    private final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 100.0, 0.0, 500.0, 1.0, () -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final SliderSetting fillDelay = this.add(new SliderSetting("FillDelay", 100.0, 0.0, 500.0, 1.0, () -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final SliderSetting breakDelay = this.add(new SliderSetting("BreakDelay", 100.0, 0.0, 500.0, 1.0, () -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final SliderSetting spamDelay = this.add(new SliderSetting("SpamDelay", 200.0, 0.0, 1000.0, 1.0, () -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final SliderSetting updateDelay = this.add(new SliderSetting("UpdateDelay", 200.0, 0.0, 1000.0, 1.0, () -> this.page.getValue() == Page.General).setSuffix("ms"));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.getValue() == Page.Rotate).setParent());
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", true, () -> this.rotate.isOpen() && this.page.getValue() == Page.Rotate).setParent());
    private final BooleanSetting whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == Page.Rotate));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == Page.Rotate));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == Page.Rotate));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 20.0, 0.0, 360.0, 0.1, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.checkFov.getValue() && this.page.getValue() == Page.Rotate));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.rotate.isOpen() && this.yawStep.isOpen() && this.page.getValue() == Page.Rotate));
    private final BooleanSetting noSuicide = this.add(new BooleanSetting("NoSuicide", true, () -> this.page.getValue() == Page.Interact));
    private final BooleanSetting smart = this.add(new BooleanSetting("Smart", true, () -> this.page.getValue() == Page.Interact));
    private final BooleanSetting terrainIgnore = this.add(new BooleanSetting("TerrainIgnore", true, () -> this.page.getValue() == Page.Interact));
    private final SliderSetting minPrefer = this.add(new SliderSetting("Prefer", 7.0, 0.0, 36.0, 0.1, () -> this.page.getValue() == Page.Interact).setSuffix("dmg"));
    private final SliderSetting maxSelfDamage = this.add(new SliderSetting("MaxSelf", 8.0, 0.0, 36.0, 0.1, () -> this.page.getValue() == Page.Interact).setSuffix("dmg"));
    private final EnumSetting<Aura.TargetESP> mode = this.add(new EnumSetting<Aura.TargetESP>("TargetESP", Aura.TargetESP.Jello, () -> this.page.getValue() == Page.Render));
    private final ColorSetting color = this.add(new ColorSetting("TargetColor", new Color(255, 255, 255, 250), () -> this.page.getValue() == Page.Render));
    private final ColorSetting outlineColor = this.add(new ColorSetting("TargetOutlineColor", new Color(255, 255, 255, 250), () -> this.page.getValue() == Page.Render));
    private final BooleanSetting render = this.add(new BooleanSetting("Render", true, () -> this.page.getValue() == Page.Render));
    private final BooleanSetting shrink = this.add(new BooleanSetting("Shrink", true, () -> this.page.getValue() == Page.Render && this.render.getValue()));
    private final ColorSetting box = this.add(new ColorSetting("Box", new Color(255, 255, 255, 255), () -> this.page.getValue() == Page.Render && this.render.getValue()).injectBoolean(true));
    private final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(255, 255, 255, 100), () -> this.page.getValue() == Page.Render && this.render.getValue()).injectBoolean(true));
    private final SliderSetting sliderSpeed = this.add(new SliderSetting("SliderSpeed", 0.2, 0.0, 1.0, 0.01, () -> this.page.getValue() == Page.Render && this.render.getValue()));
    private final SliderSetting startFadeTime = this.add(new SliderSetting("StartFade", 0.3, 0.0, 2.0, 0.01, () -> this.page.getValue() == Page.Render && this.render.getValue()).setSuffix("s"));
    private final SliderSetting fadeSpeed = this.add(new SliderSetting("FadeSpeed", 0.2, 0.01, 1.0, 0.01, () -> this.page.getValue() == Page.Render && this.render.getValue()));
    private final Timer delayTimer = new Timer();
    private final Timer calcTimer = new Timer();
    private final Timer noPosTimer = new Timer();
    private final Timer assistTimer = new Timer();
    public class_243 directionVec = null;
    public class_1657 displayTarget;
    public class_2338 currentPos;
    public class_2338 tempPos;
    public double lastDamage;
    double fade = 0.0;
    class_2338 assistPos;

    public AutoAnchor() {
        super("AutoAnchor", Module.Category.Combat);
        this.setChinese("\u91cd\u751f\u951a\u5149\u73af");
        INSTANCE = this;
        Alien.EVENT_BUS.subscribe(new AnchorRender());
    }

    public static boolean canSee(class_243 from, class_243 to) {
        class_3965 result = AutoAnchor.mc.field_1687.method_17742(new class_3959(from, to, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, (class_1297)AutoAnchor.mc.field_1724));
        return result == null || result.method_17783() == class_239.class_240.field_1333;
    }

    @Override
    public String getInfo() {
        return this.displayTarget != null && this.currentPos != null ? this.displayTarget.method_5477().getString() : null;
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        if (this.displayTarget != null && this.currentPos != null) {
            Aura.doRender(matrixStack, mc.method_60646().method_60637(true), (class_1297)this.displayTarget, this.color.getValue(), this.outlineColor.getValue(), this.mode.getValue());
        }
    }

    @EventListener
    public void onRotate(RotationEvent event) {
        if (this.currentPos != null && this.rotate.getValue() && this.shouldYawStep() && this.directionVec != null) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @Override
    public void onDisable() {
        this.tempPos = null;
        this.currentPos = null;
    }

    public void onThread() {
        if (!this.isOff() && !AutoAnchor.nullCheck() && this.thread.getValue()) {
            int unBlock;
            if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
                this.currentPos = null;
                return;
            }
            if (AutoCrystal.INSTANCE.isOn() && AutoCrystal.INSTANCE.crystalPos != null && this.preferCrystal.getValue()) {
                this.currentPos = null;
                return;
            }
            int anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_23152) : InventoryUtil.findBlock(class_2246.field_23152);
            int glowstone = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10171) : InventoryUtil.findBlock(class_2246.field_10171);
            int n = unBlock = this.inventorySwap.getValue() ? anchor : InventoryUtil.findUnBlock();
            if (anchor == -1) {
                this.currentPos = null;
                return;
            }
            if (glowstone == -1) {
                this.currentPos = null;
                return;
            }
            if (unBlock == -1) {
                this.currentPos = null;
                return;
            }
            if (AutoAnchor.mc.field_1724.method_5715()) {
                this.currentPos = null;
                return;
            }
            if (this.usingPause.getValue() && AutoAnchor.mc.field_1724.method_6115()) {
                this.currentPos = null;
                return;
            }
            this.calc();
        }
    }

    private boolean shouldYawStep() {
        return this.whenElytra.getValue() || !AutoAnchor.mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() && !Velocity.INSTANCE.noRotation() : false;
    }

    @EventListener
    public void onTick(ClientTickEvent event) {
        if (!(AutoAnchor.nullCheck() || this.timing.is(Timing.Pre) && event.isPost() || this.timing.is(Timing.Post) && event.isPre())) {
            int anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_23152) : InventoryUtil.findBlock(class_2246.field_23152);
            int glowstone = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10171) : InventoryUtil.findBlock(class_2246.field_10171);
            int unBlock = this.inventorySwap.getValue() ? anchor : InventoryUtil.findUnBlock();
            int old = AutoAnchor.mc.field_1724.method_31548().field_7545;
            if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
                this.currentPos = null;
            } else if (AutoCrystal.INSTANCE.isOn() && AutoCrystal.INSTANCE.crystalPos != null) {
                this.currentPos = null;
            } else if (anchor == -1) {
                this.currentPos = null;
            } else if (glowstone == -1) {
                this.currentPos = null;
            } else if (unBlock == -1) {
                this.currentPos = null;
            } else if (AutoAnchor.mc.field_1724.method_5715()) {
                this.currentPos = null;
            } else if (this.usingPause.getValue() && AutoAnchor.mc.field_1724.method_6115()) {
                this.currentPos = null;
            } else if (!this.inventorySwap.getValue() || EntityUtil.inInventory()) {
                class_2338 pos;
                if (this.assist.getValue()) {
                    this.onAssist();
                }
                if (!this.thread.getValue()) {
                    this.calc();
                }
                if ((pos = this.currentPos) != null) {
                    boolean shouldSpam;
                    if (this.breakCrystal.getValue()) {
                        CombatUtil.attackCrystal(new class_2338((class_2382)pos), this.rotate.getValue(), false);
                    }
                    boolean bl = shouldSpam = this.spam.getValue() && (!this.mineSpam.getValue() || Alien.BREAK.isMining(pos));
                    if (shouldSpam) {
                        if (!this.delayTimer.passed((long)this.spamDelay.getValueFloat())) {
                            return;
                        }
                        this.delayTimer.reset();
                        if (BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue())) {
                            this.placeBlock(pos, this.rotate.getValue(), anchor);
                        }
                        if (!this.chargeList.contains(pos)) {
                            this.delayTimer.reset();
                            this.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue(), glowstone);
                            this.chargeList.add(pos);
                        }
                        this.chargeList.remove(pos);
                        this.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue(), unBlock);
                        if (this.spamPlace.getValue() && this.inSpam.getValue()) {
                            if (this.shouldYawStep() && this.checkFov.getValue()) {
                                class_2350 side = BlockUtil.getClickSide(pos);
                                class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
                                if (Alien.ROTATION.inFov(directionVec, this.fov.getValueFloat())) {
                                    CombatUtil.modifyPos = pos;
                                    CombatUtil.modifyBlockState = class_2246.field_10124.method_9564();
                                    this.placeBlock(pos, this.rotate.getValue(), anchor);
                                    CombatUtil.modifyPos = null;
                                }
                            } else {
                                CombatUtil.modifyPos = pos;
                                CombatUtil.modifyBlockState = class_2246.field_10124.method_9564();
                                this.placeBlock(pos, this.rotate.getValue(), anchor);
                                CombatUtil.modifyPos = null;
                            }
                        }
                    } else if (BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue())) {
                        if (!this.delayTimer.passed((long)this.placeDelay.getValueFloat())) {
                            return;
                        }
                        this.delayTimer.reset();
                        this.placeBlock(pos, this.rotate.getValue(), anchor);
                    } else if (BlockUtil.getBlock(pos) == class_2246.field_23152) {
                        if (!this.chargeList.contains(pos)) {
                            if (!this.delayTimer.passed((long)this.fillDelay.getValueFloat())) {
                                return;
                            }
                            this.delayTimer.reset();
                            this.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue(), glowstone);
                            this.chargeList.add(pos);
                        } else {
                            if (!this.delayTimer.passed((long)this.breakDelay.getValueFloat())) {
                                return;
                            }
                            this.delayTimer.reset();
                            this.chargeList.remove(pos);
                            this.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue(), unBlock);
                            if (this.spamPlace.getValue()) {
                                if (this.shouldYawStep() && this.checkFov.getValue()) {
                                    class_2350 side = BlockUtil.getClickSide(pos);
                                    class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
                                    if (Alien.ROTATION.inFov(directionVec, this.fov.getValueFloat())) {
                                        CombatUtil.modifyPos = pos;
                                        CombatUtil.modifyBlockState = class_2246.field_10124.method_9564();
                                        this.placeBlock(pos, this.rotate.getValue(), anchor);
                                        CombatUtil.modifyPos = null;
                                    }
                                } else {
                                    CombatUtil.modifyPos = pos;
                                    CombatUtil.modifyBlockState = class_2246.field_10124.method_9564();
                                    this.placeBlock(pos, this.rotate.getValue(), anchor);
                                    CombatUtil.modifyPos = null;
                                }
                            }
                        }
                    }
                    if (!this.inventorySwap.getValue()) {
                        this.doSwap(old);
                    }
                }
            }
        }
    }

    private void calc() {
        if (!AutoAnchor.nullCheck()) {
            if (this.calcTimer.passed((long)this.updateDelay.getValueFloat())) {
                double damage;
                this.calcTimer.reset();
                PlayerEntityPredict selfPredict = new PlayerEntityPredict((class_1657)AutoAnchor.mc.field_1724, this.maxMotionY.getValue(), this.selfPredict.getValueInt(), this.simulation.getValueInt(), this.step.getValue(), this.doubleStep.getValue(), this.jump.getValue(), this.inBlockPause.getValue());
                this.tempPos = null;
                double placeDamage = this.minDamage.getValue();
                double breakDamage = this.breakMin.getValue();
                boolean anchorFound = false;
                List<class_1657> enemies = CombatUtil.getEnemies(this.targetRange.getValue());
                ArrayList<PlayerEntityPredict> list = new ArrayList<PlayerEntityPredict>();
                for (class_1657 player : enemies) {
                    list.add(new PlayerEntityPredict(player, this.maxMotionY.getValue(), this.predictTicks.getValueInt(), this.simulation.getValueInt(), this.step.getValue(), this.doubleStep.getValue(), this.jump.getValue(), this.inBlockPause.getValue()));
                }
                for (PlayerEntityPredict pap : list) {
                    double d;
                    double selfDamage;
                    class_2338 pos = EntityUtil.getEntityPos((class_1297)pap.player, true).method_10086(2);
                    if (!BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue()) && (BlockUtil.getBlock(pos) != class_2246.field_23152 || BlockUtil.getClickSideStrict(pos) == null) || (selfDamage = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue() || this.noSuicide.getValue() && selfDamage > (double)(AutoAnchor.mc.field_1724.method_6032() + AutoAnchor.mc.field_1724.method_6067())) continue;
                    damage = this.getAnchorDamage(pos, pap.player, pap.predict);
                    if (!(d > (double)this.headDamage.getValueFloat()) || this.smart.getValue() && selfDamage > damage) continue;
                    this.lastDamage = damage;
                    this.displayTarget = pap.player;
                    this.tempPos = pos;
                    break;
                }
                if (this.tempPos == null) {
                    for (class_2338 pos : BlockUtil.getSphere(this.range.getValueFloat() + 1.0f, AutoAnchor.mc.field_1724.method_33571())) {
                        for (PlayerEntityPredict papx : list) {
                            double selfDamage;
                            boolean skip;
                            if (this.light.getValue()) {
                                CombatUtil.modifyPos = pos;
                                CombatUtil.modifyBlockState = class_2246.field_10124.method_9564();
                                skip = !AutoAnchor.canSee(pos.method_46558(), papx.predict.method_19538());
                                CombatUtil.modifyPos = null;
                                if (skip) continue;
                            }
                            if (BlockUtil.getBlock(pos) != class_2246.field_23152) {
                                double selfDamage2;
                                if (anchorFound || !BlockUtil.canPlace(pos, this.range.getValue(), this.breakCrystal.getValue())) continue;
                                CombatUtil.modifyPos = pos;
                                CombatUtil.modifyBlockState = class_2246.field_10540.method_9564();
                                skip = BlockUtil.getClickSideStrict(pos) == null;
                                CombatUtil.modifyPos = null;
                                if (skip || !((damage = this.getAnchorDamage(pos, papx.player, papx.predict)) >= placeDamage) || AutoCrystal.INSTANCE.crystalPos != null && !AutoCrystal.INSTANCE.isOff() && !((double)AutoCrystal.INSTANCE.lastDamage < damage) || (selfDamage2 = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue() || this.noSuicide.getValue() && selfDamage2 > (double)(AutoAnchor.mc.field_1724.method_6032() + AutoAnchor.mc.field_1724.method_6067()) || this.smart.getValue() && selfDamage2 > damage) continue;
                                this.lastDamage = damage;
                                this.displayTarget = papx.player;
                                placeDamage = damage;
                                this.tempPos = pos;
                                continue;
                            }
                            double damage2 = this.getAnchorDamage(pos, papx.player, papx.predict);
                            if (BlockUtil.getClickSideStrict(pos) == null || !(damage2 >= breakDamage)) continue;
                            if (damage2 >= this.minPrefer.getValue()) {
                                anchorFound = true;
                            }
                            if (!anchorFound && damage2 < placeDamage || AutoCrystal.INSTANCE.crystalPos != null && !AutoCrystal.INSTANCE.isOff() && !((double)AutoCrystal.INSTANCE.lastDamage < damage2) || (selfDamage = this.getAnchorDamage(pos, selfPredict.player, selfPredict.predict)) > this.maxSelfDamage.getValue() || this.noSuicide.getValue() && selfDamage > (double)(AutoAnchor.mc.field_1724.method_6032() + AutoAnchor.mc.field_1724.method_6067()) || this.smart.getValue() && selfDamage > damage2) continue;
                            this.lastDamage = damage2;
                            this.displayTarget = papx.player;
                            breakDamage = damage2;
                            this.tempPos = pos;
                        }
                    }
                }
            }
            this.currentPos = this.tempPos;
        }
    }

    public double getAnchorDamage(class_2338 anchorPos, class_1657 target, class_1657 predict) {
        if (this.terrainIgnore.getValue()) {
            CombatUtil.terrainIgnore = true;
        }
        double damage = ExplosionUtil.anchorDamage(anchorPos, (class_1309)target, (class_1309)predict);
        CombatUtil.terrainIgnore = false;
        return damage;
    }

    public void placeBlock(class_2338 pos, boolean rotate, int slot) {
        if (BlockUtil.allowAirPlace()) {
            this.airPlace(pos, rotate, slot);
        } else {
            class_2350 side = BlockUtil.getPlaceSide(pos);
            if (side != null) {
                this.clickBlock(pos.method_10093(side), side.method_10153(), rotate, slot);
            }
        }
    }

    public void clickBlock(class_2338 pos, class_2350 side, boolean rotate, int slot) {
        if (pos != null) {
            class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
            if (!rotate || this.faceVector(directionVec)) {
                this.doSwap(slot);
                EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                class_3965 result = new class_3965(directionVec, side, pos, false);
                Module.sendSequencedPacket(id -> new class_2885(class_1268.field_5808, result, id));
                if (this.inventorySwap.getValue()) {
                    this.doSwap(slot);
                }
                if (rotate && !this.shouldYawStep()) {
                    Alien.ROTATION.snapBack();
                }
            }
        }
    }

    public void airPlace(class_2338 pos, boolean rotate, int slot) {
        if (pos != null) {
            class_2350 side = BlockUtil.getClickSide(pos);
            class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
            if (!rotate || this.faceVector(directionVec)) {
                this.doSwap(slot);
                boolean bypass = AirPlace.INSTANCE.grimBypass.getValue();
                if (bypass) {
                    mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12969, new class_2338(0, 0, 0), class_2350.field_11033));
                }
                EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                class_3965 result = new class_3965(directionVec, side, pos, false);
                Module.sendSequencedPacket(id -> new class_2885(bypass ? class_1268.field_5810 : class_1268.field_5808, result, id));
                if (bypass) {
                    mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12969, new class_2338(0, 0, 0), class_2350.field_11033));
                }
                if (this.inventorySwap.getValue()) {
                    this.doSwap(slot);
                }
                if (rotate && !this.shouldYawStep()) {
                    Alien.ROTATION.snapBack();
                }
            }
        }
    }

    private void doSwap(int slot) {
        if (this.inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, AutoAnchor.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public boolean faceVector(class_243 directionVec) {
        if (!this.shouldYawStep()) {
            Alien.ROTATION.lookAt(directionVec);
            return true;
        }
        this.directionVec = directionVec;
        return Alien.ROTATION.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkFov.getValue();
    }

    public void onAssist() {
        this.assistPos = null;
        int anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_23152) : InventoryUtil.findBlock(class_2246.field_23152);
        int glowstone = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10171) : InventoryUtil.findBlock(class_2246.field_10171);
        int old = AutoAnchor.mc.field_1724.method_31548().field_7545;
        if (anchor != -1) {
            if (this.obsidian.getValue()) {
                int n = anchor = this.inventorySwap.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
                if (anchor == -1) {
                    return;
                }
            }
            if (!(glowstone == -1 || AutoAnchor.mc.field_1724.method_5715() || this.usingPause.getValue() && AutoAnchor.mc.field_1724.method_6115() || Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue() || !this.assistTimer.passed((long)(this.delay.getValueFloat() * 1000.0f)))) {
                class_2338 placePos;
                this.assistTimer.reset();
                ArrayList<PlayerEntityPredict> list = new ArrayList<PlayerEntityPredict>();
                for (class_1657 player : CombatUtil.getEnemies(this.assistRange.getValue())) {
                    list.add(new PlayerEntityPredict(player, this.maxMotionY.getValue(), this.predictTicks.getValueInt(), this.simulation.getValueInt(), this.step.getValue(), this.doubleStep.getValue(), this.jump.getValue(), this.inBlockPause.getValue()));
                }
                double bestDamage = this.assistDamage.getValue();
                for (PlayerEntityPredict pap : list) {
                    double damage;
                    class_2338 pos = EntityUtil.getEntityPos((class_1297)pap.player, true).method_10086(2);
                    if (AutoAnchor.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_23152) {
                        return;
                    }
                    if (BlockUtil.clientCanPlace(pos, false) && (damage = this.getAnchorDamage(pos, pap.player, pap.predict)) >= bestDamage) {
                        bestDamage = damage;
                        this.assistPos = pos;
                    }
                    for (class_2350 i : class_2350.values()) {
                        double damage2;
                        if (i == class_2350.field_11036 || i == class_2350.field_11033 || !BlockUtil.clientCanPlace(pos.method_10093(i), false) || !((damage2 = this.getAnchorDamage(pos.method_10093(i), pap.player, pap.predict)) >= bestDamage)) continue;
                        bestDamage = damage2;
                        this.assistPos = pos.method_10093(i);
                    }
                }
                if (this.assistPos != null && BlockUtil.getPlaceSide(this.assistPos, this.range.getValue()) == null && (placePos = this.getHelper(this.assistPos)) != null) {
                    this.doSwap(anchor);
                    BlockUtil.placeBlock(placePos, this.rotate.getValue());
                    if (this.inventorySwap.getValue()) {
                        this.doSwap(anchor);
                    } else {
                        this.doSwap(old);
                    }
                }
            }
        }
    }

    public class_2338 getHelper(class_2338 pos) {
        for (class_2350 i : class_2350.values()) {
            if (this.checkMine.getValue() && Alien.BREAK.isMining(pos.method_10093(i)) || !BlockUtil.isStrictDirection(pos.method_10093(i), i.method_10153()) || !BlockUtil.canPlace(pos.method_10093(i))) continue;
            return pos.method_10093(i);
        }
        return null;
    }

    public static enum Page {
        General,
        Interact,
        Predict,
        Rotate,
        Assist,
        Render;

    }

    public class AnchorRender {
        @EventListener
        public void onRender3D(Render3DEvent event) {
            class_2338 currentPos = AutoAnchor.INSTANCE.currentPos;
            if (currentPos != null) {
                AutoAnchor.this.noPosTimer.reset();
                placeVec3d = currentPos.method_46558();
            }
            if (placeVec3d != null) {
                AutoAnchor.this.fade = AutoAnchor.this.fadeSpeed.getValue() >= 1.0 ? (AutoAnchor.this.noPosTimer.passed((long)(AutoAnchor.this.startFadeTime.getValue() * 1000.0)) ? 0.0 : 0.5) : AnimateUtil.animate(AutoAnchor.this.fade, AutoAnchor.this.noPosTimer.passed((long)(AutoAnchor.this.startFadeTime.getValue() * 1000.0)) ? 0.0 : 0.5, AutoAnchor.this.fadeSpeed.getValue() / 10.0);
                if (AutoAnchor.this.fade == 0.0) {
                    curVec3d = null;
                } else {
                    curVec3d = curVec3d != null && !(AutoAnchor.this.sliderSpeed.getValue() >= 1.0) ? new class_243(AnimateUtil.animate(AutoAnchor.curVec3d.field_1352, AutoAnchor.placeVec3d.field_1352, AutoAnchor.this.sliderSpeed.getValue() / 10.0), AnimateUtil.animate(AutoAnchor.curVec3d.field_1351, AutoAnchor.placeVec3d.field_1351, AutoAnchor.this.sliderSpeed.getValue() / 10.0), AnimateUtil.animate(AutoAnchor.curVec3d.field_1350, AutoAnchor.placeVec3d.field_1350, AutoAnchor.this.sliderSpeed.getValue() / 10.0)) : placeVec3d;
                    if (AutoAnchor.this.render.getValue()) {
                        class_238 cbox = new class_238(curVec3d, curVec3d);
                        cbox = AutoAnchor.this.shrink.getValue() ? cbox.method_1014(AutoAnchor.this.fade) : cbox.method_1014(0.5);
                        if (AutoAnchor.this.fill.booleanValue) {
                            event.drawFill(cbox, ColorUtil.injectAlpha(AutoAnchor.this.fill.getValue(), (int)((double)AutoAnchor.this.fill.getValue().getAlpha() * AutoAnchor.this.fade * 2.0)));
                        }
                        if (AutoAnchor.this.box.booleanValue) {
                            event.drawBox(cbox, ColorUtil.injectAlpha(AutoAnchor.this.box.getValue(), (int)((double)AutoAnchor.this.box.getValue().getAlpha() * AutoAnchor.this.fade * 2.0)));
                        }
                    }
                }
            }
        }
    }
}

