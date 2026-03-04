/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.decoration.EndCrystalEntity
 *  net.minecraft.entity.decoration.ArmorStandEntity
 *  net.minecraft.entity.ItemEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.item.AirBlockItem
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.enchantment.Enchantments
 *  net.minecraft.world.BlockView
 *  net.minecraft.world.World
 *  net.minecraft.block.AirBlock
 *  net.minecraft.block.Blocks
 *  net.minecraft.block.Block
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.block.BlockState
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket$Action
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.block.RespawnAnchorBlock
 *  net.minecraft.entity.attribute.EntityAttributes
 *  net.minecraft.registry.entry.RegistryEntry
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClickBlockEvent;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.math.FadeUtils;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.asm.accessors.IPlayerMoveC2SPacket;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.combat.AutoAnchor;
import dev.luminous.mod.modules.impl.combat.AutoCrystal;
import dev.luminous.mod.modules.impl.combat.CevBreaker;
import dev.luminous.mod.modules.impl.combat.Criticals;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.impl.movement.Velocity;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import dev.luminous.mod.modules.settings.enums.Timing;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.util.Hand;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;

public class SpeedMine
extends Module {
    public static SpeedMine INSTANCE;
    public static class_2338 secondPos;
    public static double progress;
    private final FadeUtils animationTime = new FadeUtils(1000L);
    private final FadeUtils secondAnim = new FadeUtils(1000L);
    private final DecimalFormat df = new DecimalFormat("0.0");
    private final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    private final SliderSetting stopDelay = this.add(new SliderSetting("StopDelay", 50.0, 0.0, 500.0, 1.0, () -> this.page.is(Page.General)));
    private final SliderSetting startDelay = this.add(new SliderSetting("StartDelay", 200.0, 0.0, 500.0, 1.0, () -> this.page.is(Page.General)));
    private final SliderSetting damage = this.add(new SliderSetting("Damage", 0.7f, 0.0, 2.0, 0.01, () -> this.page.is(Page.General)));
    private final SliderSetting maxBreak = this.add(new SliderSetting("MaxBreak", 3.0, 0.0, 20.0, 1.0, () -> this.page.is(Page.General)));
    public final BooleanSetting noGhostHand = this.add(new BooleanSetting("1.21", false, () -> this.page.is(Page.General)));
    public final BooleanSetting noCollide = this.add(new BooleanSetting("NoCollide", true, () -> this.page.is(Page.General)));
    private final EnumSetting<Timing> timing = this.add(new EnumSetting<Timing>("Timing", Timing.All, () -> this.page.getValue() == Page.General));
    private final BooleanSetting grimDisabler = this.add(new BooleanSetting("GrimDisabler", false, () -> this.page.is(Page.General)));
    private final BooleanSetting instant = this.add(new BooleanSetting("Instant", false, () -> this.page.is(Page.General)));
    private final BooleanSetting wait = this.add(new BooleanSetting("Wait", true, () -> !this.instant.getValue() && this.page.is(Page.General)));
    private final BooleanSetting mineAir = this.add(new BooleanSetting("MineAir", true, () -> this.wait.getValue() && !this.instant.getValue() && this.page.is(Page.General)));
    private final BooleanSetting hotBar = this.add(new BooleanSetting("HotbarSwap", false, () -> this.page.is(Page.General)));
    private final BooleanSetting doubleBreak = this.add(new BooleanSetting("DoubleBreak", true, () -> this.page.is(Page.General))).setParent();
    public final BooleanSetting autoSwitch = this.add(new BooleanSetting("AutoSwitch", true, () -> this.page.is(Page.General) && this.doubleBreak.isOpen()));
    private final SliderSetting start = this.add(new SliderSetting("Start", 0.9f, 0.0, 2.0, 0.01, () -> this.page.is(Page.General) && this.doubleBreak.isOpen()));
    private final SliderSetting timeOut = this.add(new SliderSetting("TimeOut", 1.2f, 0.0, 2.0, 0.01, () -> this.page.is(Page.General) && this.doubleBreak.isOpen()));
    private final BooleanSetting setAir = this.add(new BooleanSetting("SetAir", false, () -> this.page.is(Page.General)));
    private final BooleanSetting swing = this.add(new BooleanSetting("Swing", true, () -> this.page.is(Page.General)));
    private final BooleanSetting endSwing = this.add(new BooleanSetting("EndSwing", false, () -> this.page.is(Page.General)));
    public final SliderSetting range = this.add(new SliderSetting("Range", 6.0, 3.0, 10.0, 0.1, () -> this.page.is(Page.General)));
    private final EnumSetting<SwingSide> swingMode = this.add(new EnumSetting<SwingSide>("SwingSide", SwingSide.All, () -> this.page.is(Page.General)));
    private final BooleanSetting unbreakableCancel = this.add(new BooleanSetting("UnbreakableCancel", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting switchReset = this.add(new BooleanSetting("SwitchReset", false, () -> this.page.is(Page.Check)));
    private final BooleanSetting preferWeb = this.add(new BooleanSetting("PreferWeb", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting preferHead = this.add(new BooleanSetting("PreferHead", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting farCancel = this.add(new BooleanSetting("FarCancel", false, () -> this.page.is(Page.Check)));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting checkWeb = this.add(new BooleanSetting("CheckWeb", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting checkGround = this.add(new BooleanSetting("CheckGround", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting smart = this.add(new BooleanSetting("Smart", true, () -> this.page.is(Page.Check) && this.checkGround.getValue()));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false, () -> this.page.is(Page.Check)).setParent());
    private final BooleanSetting allowOffhand = this.add(new BooleanSetting("AllowOffhand", true, () -> this.page.is(Page.Check) && this.usingPause.isOpen()));
    private final BooleanSetting bypassGround = this.add(new BooleanSetting("BypassGround", true, () -> this.page.is(Page.Check)));
    private final SliderSetting bypassTime = this.add(new SliderSetting("BypassTime", 400, 0, 2000, () -> this.bypassGround.getValue() && this.page.is(Page.Check)));
    private final BindSetting pause = this.add(new BindSetting("Pause", -1, () -> this.page.is(Page.Check)));
    private final BooleanSetting rotate = this.add(new BooleanSetting("StartRotate", true, () -> this.page.is(Page.Rotation)));
    private final BooleanSetting endRotate = this.add(new BooleanSetting("EndRotate", false, () -> this.page.is(Page.Rotation)));
    private final SliderSetting syncTime = this.add(new SliderSetting("Sync", 300, 0, 1000, () -> this.page.is(Page.Rotation)));
    private final BooleanSetting yawStep = this.add(new BooleanSetting("YawStep", false, () -> this.page.is(Page.Rotation)).setParent());
    private final BooleanSetting whenElytra = this.add(new BooleanSetting("FallFlying", true, () -> this.page.is(Page.Rotation) && this.yawStep.isOpen()));
    private final SliderSetting steps = this.add(new SliderSetting("Steps", 0.05, 0.0, 1.0, 0.01, () -> this.page.is(Page.Rotation) && this.yawStep.isOpen()));
    private final BooleanSetting checkFov = this.add(new BooleanSetting("OnlyLooking", true, () -> this.page.is(Page.Rotation) && this.yawStep.isOpen()));
    private final SliderSetting fov = this.add(new SliderSetting("Fov", 20.0, 0.0, 360.0, 0.1, () -> this.page.is(Page.Rotation) && this.yawStep.isOpen()));
    private final SliderSetting priority = this.add(new SliderSetting("Priority", 10, 0, 100, () -> this.page.is(Page.Rotation) && this.yawStep.isOpen()));
    public final BooleanSetting crystal = this.add(new BooleanSetting("Crystal", false, () -> this.page.is(Page.Place)).setParent());
    private final BooleanSetting onlyHeadBomber = this.add(new BooleanSetting("OnlyCev", true, () -> this.page.is(Page.Place) && this.crystal.isOpen()));
    private final BooleanSetting waitPlace = this.add(new BooleanSetting("WaitPlace", true, () -> this.page.is(Page.Place) && this.crystal.isOpen()));
    private final BooleanSetting spamPlace = this.add(new BooleanSetting("SpamPlace", false, () -> this.page.is(Page.Place) && this.crystal.isOpen()));
    private final BooleanSetting afterBreak = this.add(new BooleanSetting("AfterBreak", true, () -> this.page.is(Page.Place) && this.crystal.isOpen()));
    private final BooleanSetting checkDamage = this.add(new BooleanSetting("DetectProgress", true, () -> this.page.is(Page.Place) && this.crystal.isOpen()));
    private final SliderSetting crystalDamage = this.add(new SliderSetting("Progress", 0.9f, 0.0, 1.0, 0.01, () -> this.page.is(Page.Place) && this.crystal.isOpen() && this.checkDamage.getValue()));
    public final BindSetting obsidian = this.add(new BindSetting("Obsidian", -1, () -> this.page.is(Page.Place)));
    private final BindSetting enderChest = this.add(new BindSetting("EnderChest", -1, () -> this.page.is(Page.Place)));
    private final BooleanSetting placeRotate = this.add(new BooleanSetting("PlaceRotate", true, () -> this.page.is(Page.Place)));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.is(Page.Place)));
    private final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 100, 0, 1000, () -> this.page.is(Page.Place)));
    private final BooleanSetting checkDouble = this.add(new BooleanSetting("CheckDouble", false, () -> this.page.is(Page.Render)));
    private final EnumSetting<Animation> animation = this.add(new EnumSetting<Animation>("Animation", Animation.Up, () -> this.page.is(Page.Render)));
    private final EnumSetting<Easing> ease = this.add(new EnumSetting<Easing>("Ease", Easing.CubicInOut, () -> this.page.is(Page.Render)));
    private final EnumSetting<Easing> fadeEase = this.add(new EnumSetting<Easing>("FadeEase", Easing.CubicInOut, () -> this.page.is(Page.Render)));
    private final SliderSetting expandLine = this.add(new SliderSetting("ExpandLine", 0.0, 0.0, 1.0, () -> this.page.is(Page.Render)));
    private final ColorSetting startColor = this.add(new ColorSetting("StartFill", new Color(255, 255, 255, 100), () -> this.page.is(Page.Render)));
    private final ColorSetting startOutlineColor = this.add(new ColorSetting("StartOutline", new Color(255, 255, 255, 100), () -> this.page.is(Page.Render)));
    private final ColorSetting endColor = this.add(new ColorSetting("EndFill", new Color(255, 255, 255, 100), () -> this.page.is(Page.Render)));
    private final ColorSetting endOutlineColor = this.add(new ColorSetting("EndOutline", new Color(255, 255, 255, 100), () -> this.page.is(Page.Render)));
    private final ColorSetting doubleColor = this.add(new ColorSetting("DoubleFill", new Color(88, 94, 255, 100), () -> this.doubleBreak.getValue() && this.page.is(Page.Render)));
    private final ColorSetting doubleOutlineColor = this.add(new ColorSetting("DoubleOutline", new Color(88, 94, 255, 100), () -> this.doubleBreak.getValue() && this.page.is(Page.Render)));
    private final BooleanSetting text = this.add(new BooleanSetting("Text", true, () -> this.page.is(Page.Render)));
    private final BooleanSetting box = this.add(new BooleanSetting("Box", true, () -> this.page.is(Page.Render)));
    private final BooleanSetting outline = this.add(new BooleanSetting("Outline", true, () -> this.page.is(Page.Render)));
    private final Timer mineTimer = new Timer();
    private final Timer sync = new Timer();
    private final Timer secondTimer = new Timer();
    private final Timer delayTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer startTime = new Timer();
    public static boolean ghost;
    public static boolean complete;
    int lastSlot = -1;
    class_243 directionVec = null;
    Runnable switchBack;
    class_2338 breakPos;
    boolean startPacket = false;
    int breakNumber = 0;
    double breakFinalTime;
    double secondFinalTime;
    boolean sendGroundPacket = false;
    boolean swapped = false;
    int mainSlot = 0;

    public SpeedMine() {
        super("SpeedMine", Module.Category.Player);
        this.setChinese("\u53d1\u5305\u6316\u6398");
        INSTANCE = this;
    }

    public static class_2338 getBreakPos() {
        return INSTANCE.isOn() ? SpeedMine.INSTANCE.breakPos : null;
    }

    @Override
    public String getInfo() {
        return progress >= 1.0 ? "Done" : this.df.format(progress * 100.0) + "%";
    }

    @EventListener
    public void onRotate(RotationEvent event) {
        if (this.rotate.getValue() && this.shouldYawStep() && this.directionVec != null && !this.sync.passedMs(this.syncTime.getValue())) {
            event.setTarget(this.directionVec, this.steps.getValueFloat(), this.priority.getValueFloat());
        }
    }

    @Override
    public void onLogin() {
        this.startPacket = false;
        ghost = false;
        complete = false;
        this.breakPos = null;
        secondPos = null;
    }

    @Override
    public void onDisable() {
        this.startPacket = false;
        ghost = false;
        complete = false;
        this.breakPos = null;
    }

    private void autoSwitch() {
        if (this.autoSwitch.getValue() && this.doubleBreak.getValue()) {
            int index = -1;
            if (secondPos != null) {
                float CurrentFastest = 1.0f;
                for (int i = 0; i < 9; ++i) {
                    float destroySpeed;
                    float digSpeed;
                    class_1799 stack = SpeedMine.mc.field_1724.method_31548().method_5438(i);
                    if (stack == class_1799.field_8037 || !((digSpeed = (float)class_1890.method_8225((class_6880)((class_6880)SpeedMine.mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get()), (class_1799)stack)) + (destroySpeed = stack.method_7924(SpeedMine.mc.field_1687.method_8320(secondPos))) > CurrentFastest)) continue;
                    CurrentFastest = digSpeed + destroySpeed;
                    index = i;
                }
            }
            if (index != -1 && !SpeedMine.mc.field_1690.field_1904.method_1434() && !SpeedMine.mc.field_1690.field_1886.method_1434() && !SpeedMine.mc.field_1724.method_6115() && this.secondTimer.passedMs(this.getBreakTime(secondPos, index, this.start.getValue()))) {
                if (index != SpeedMine.mc.field_1724.method_31548().field_7545) {
                    this.mainSlot = SpeedMine.mc.field_1724.method_31548().field_7545;
                    InventoryUtil.switchToSlot(index);
                    this.swapped = true;
                }
            } else if (this.swapped) {
                InventoryUtil.switchToSlot(this.mainSlot);
                this.swapped = false;
            }
        }
    }

    @EventListener
    public void onTick(ClientTickEvent event) {
        if (!SpeedMine.nullCheck()) {
            if (this.breakPos != null && SpeedMine.mc.field_1687.method_22347(this.breakPos)) {
                complete = true;
            }
            if (secondPos != null) {
                int secondSlot = this.getTool(secondPos);
                if (secondSlot == -1) {
                    secondSlot = SpeedMine.mc.field_1724.method_31548().field_7545;
                }
                this.secondFinalTime = this.getBreakTime(secondPos, secondSlot, 1.0);
                if (!this.isAir(secondPos) && !SpeedMine.unbreakable(secondPos)) {
                    double time = this.getBreakTime(secondPos, SpeedMine.mc.field_1724.method_31548().field_7545, 1.0);
                    if (this.secondTimer.passedMs(time * this.timeOut.getValue())) {
                        secondPos = null;
                    }
                } else {
                    secondPos = null;
                }
            }
            if (this.switchBack != null && event.isPre()) {
                this.switchBack.run();
                this.switchBack = null;
            }
            if (!(this.timing.is(Timing.Pre) && event.isPost() || this.timing.is(Timing.Post) && event.isPre())) {
                if (SpeedMine.mc.field_1724.method_29504()) {
                    secondPos = null;
                }
                this.autoSwitch();
                if (SpeedMine.mc.field_1724.method_7337()) {
                    this.startPacket = false;
                    ghost = false;
                    complete = false;
                    this.breakNumber = 0;
                    this.breakPos = null;
                    progress = 0.0;
                } else if (this.breakPos == null) {
                    this.breakNumber = 0;
                    this.startPacket = false;
                    ghost = false;
                    complete = false;
                    progress = 0.0;
                } else {
                    int slot = this.getTool(this.breakPos);
                    if (slot == -1) {
                        slot = SpeedMine.mc.field_1724.method_31548().field_7545;
                    }
                    this.breakFinalTime = this.getBreakTime(this.breakPos, slot);
                    progress = (double)this.mineTimer.getMs() / this.breakFinalTime;
                    if (this.isAir(this.breakPos)) {
                        this.breakNumber = 0;
                    }
                    if (!((double)this.breakNumber > this.maxBreak.getValue() - 1.0 && this.maxBreak.getValue() > 0.0 && !complete || !this.wait.getValue() && this.isAir(this.breakPos) && !this.instant.getValue())) {
                        if (SpeedMine.unbreakable(this.breakPos)) {
                            if (this.unbreakableCancel.getValue()) {
                                this.breakPos = null;
                                this.startPacket = false;
                                ghost = false;
                                complete = false;
                            }
                            this.breakNumber = 0;
                        } else if ((double)class_3532.method_15355((float)((float)SpeedMine.mc.field_1724.method_33571().method_1025(this.breakPos.method_46558()))) > this.range.getValue()) {
                            if (this.farCancel.getValue()) {
                                this.startPacket = false;
                                ghost = false;
                                complete = false;
                                this.breakNumber = 0;
                                this.breakPos = null;
                            }
                        } else if (!(this.usingPause.getValue() && SpeedMine.mc.field_1724.method_6115() && (!this.allowOffhand.getValue() || SpeedMine.mc.field_1724.method_6058() == class_1268.field_5808) || this.pause.isPressed() || Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue() || this.breakPos.equals((Object)AutoAnchor.INSTANCE.currentPos) && BlockUtil.getBlock(SpeedMine.getBreakPos()) instanceof class_4969 || !this.hotBar.getValue() && !EntityUtil.inInventory())) {
                            if (this.isAir(this.breakPos)) {
                                if (this.shouldCrystal()) {
                                    for (class_2350 facing : class_2350.values()) {
                                        CombatUtil.attackCrystal(this.breakPos.method_10093(facing), this.placeRotate.getValue(), true);
                                    }
                                }
                                if (this.placeTimer.passedMs(this.placeDelay.getValue()) && BlockUtil.canPlace(this.breakPos) && SpeedMine.mc.field_1755 == null) {
                                    int obsidian;
                                    if (this.enderChest.isPressed()) {
                                        int eChest = this.findBlock(class_2246.field_10443);
                                        if (eChest != -1) {
                                            int oldSlot = SpeedMine.mc.field_1724.method_31548().field_7545;
                                            this.doSwap(eChest, eChest);
                                            BlockUtil.placeBlock(this.breakPos, this.placeRotate.getValue(), true);
                                            this.doSwap(oldSlot, eChest);
                                            this.placeTimer.reset();
                                        }
                                    } else if (this.obsidian.isPressed() && (obsidian = this.findBlock(class_2246.field_10540)) != -1) {
                                        int hasCrystal = 0;
                                        if (this.shouldCrystal()) {
                                            for (class_1297 entity : BlockUtil.getEntities(new class_238(this.breakPos.method_10084()))) {
                                                if (!(entity instanceof class_1511)) continue;
                                                hasCrystal = 1;
                                                break;
                                            }
                                        }
                                        if (hasCrystal == 0 || this.spamPlace.getValue()) {
                                            int oldSlot = SpeedMine.mc.field_1724.method_31548().field_7545;
                                            this.doSwap(obsidian, obsidian);
                                            BlockUtil.placeBlock(this.breakPos, this.placeRotate.getValue(), true);
                                            this.doSwap(oldSlot, obsidian);
                                            this.placeTimer.reset();
                                        }
                                    }
                                }
                                this.breakNumber = 0;
                            } else if (this.canPlaceCrystal(this.breakPos.method_10084()) && this.shouldCrystal() && (this.placeTimer.passedMs(this.placeDelay.getValue()) ? (this.checkDamage.getValue() ? (double)this.mineTimer.getMs() / this.breakFinalTime >= this.crystalDamage.getValue() && !this.placeCrystal() : !this.placeCrystal()) : this.startPacket)) {
                                return;
                            }
                            if (this.waitPlace.getValue()) {
                                for (class_2350 i : class_2350.values()) {
                                    if (!this.breakPos.method_10093(i).equals((Object)AutoCrystal.INSTANCE.crystalPos)) continue;
                                    if (!AutoCrystal.INSTANCE.canPlaceCrystal(this.breakPos, false, false)) break;
                                    return;
                                }
                            }
                            if (this.delayTimer.passed((long)this.stopDelay.getValue())) {
                                if (this.startPacket) {
                                    if (this.isAir(this.breakPos)) {
                                        return;
                                    }
                                    if (this.onlyGround.getValue() && !SpeedMine.mc.field_1724.method_24828()) {
                                        return;
                                    }
                                    if (this.mineTimer.passed((long)this.breakFinalTime)) {
                                        boolean shouldSwitch;
                                        if (this.endRotate.getValue() && this.shouldYawStep() && !this.faceVector(this.breakPos.method_46558().method_43206(BlockUtil.getClickSide(this.breakPos), 0.5))) {
                                            return;
                                        }
                                        int old = SpeedMine.mc.field_1724.method_31548().field_7545;
                                        if (this.hotBar.getValue()) {
                                            shouldSwitch = slot != old;
                                        } else {
                                            if (slot < 9) {
                                                slot += 36;
                                            }
                                            boolean bl = shouldSwitch = old + 36 != slot;
                                        }
                                        if (shouldSwitch) {
                                            if (this.hotBar.getValue()) {
                                                InventoryUtil.switchToSlot(slot);
                                            } else {
                                                SpeedMine.mc.field_1761.method_2906(SpeedMine.mc.field_1724.field_7512.field_7763, slot, old, class_1713.field_7791, (class_1657)SpeedMine.mc.field_1724);
                                            }
                                        }
                                        int finalSlot = slot;
                                        this.switchBack = () -> {
                                            if (this.endRotate.getValue() && !this.faceVector(this.breakPos.method_46558().method_43206(BlockUtil.getClickSide(this.breakPos), 0.5))) {
                                                if (shouldSwitch) {
                                                    if (this.hotBar.getValue()) {
                                                        InventoryUtil.switchToSlot(old);
                                                    } else {
                                                        SpeedMine.mc.field_1761.method_2906(SpeedMine.mc.field_1724.field_7512.field_7763, finalSlot, old, class_1713.field_7791, (class_1657)SpeedMine.mc.field_1724);
                                                        EntityUtil.syncInventory();
                                                    }
                                                }
                                            } else {
                                                SpeedMine.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12973, this.breakPos, BlockUtil.getClickSide(this.breakPos), id));
                                                if (this.endSwing.getValue()) {
                                                    EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                                                }
                                                if (shouldSwitch) {
                                                    if (this.hotBar.getValue()) {
                                                        InventoryUtil.switchToSlot(old);
                                                    } else {
                                                        SpeedMine.mc.field_1761.method_2906(SpeedMine.mc.field_1724.field_7512.field_7763, finalSlot, old, class_1713.field_7791, (class_1657)SpeedMine.mc.field_1724);
                                                        EntityUtil.syncInventory();
                                                    }
                                                }
                                                ++this.breakNumber;
                                                this.delayTimer.reset();
                                                this.startTime.reset();
                                                if (this.afterBreak.getValue() && this.shouldCrystal()) {
                                                    for (class_2350 facing : class_2350.values()) {
                                                        CombatUtil.attackCrystal(this.breakPos.method_10093(facing), this.placeRotate.getValue(), true);
                                                    }
                                                }
                                                if (this.setAir.getValue()) {
                                                    SpeedMine.mc.field_1687.method_8501(this.breakPos, class_2246.field_10124.method_9564());
                                                }
                                                if (this.endRotate.getValue() && !this.shouldYawStep()) {
                                                    Alien.ROTATION.snapBack();
                                                }
                                                ghost = true;
                                            }
                                        };
                                        if (!this.noGhostHand.getValue()) {
                                            this.switchBack.run();
                                            this.switchBack = null;
                                        }
                                    }
                                } else {
                                    if (!this.startTime.passed(this.startDelay.getValueInt())) {
                                        return;
                                    }
                                    if (!this.mineAir.getValue() && this.isAir(this.breakPos)) {
                                        return;
                                    }
                                    class_2350 side = BlockUtil.getClickSide(this.breakPos);
                                    if (this.rotate.getValue()) {
                                        class_2382 vec3i = side.method_10163();
                                        if (!this.faceVector(this.breakPos.method_46558().method_1019(new class_243((double)vec3i.method_10263() * 0.5, (double)vec3i.method_10264() * 0.5, (double)vec3i.method_10260() * 0.5)))) {
                                            return;
                                        }
                                    }
                                    this.mineTimer.reset();
                                    this.animationTime.reset();
                                    if (this.swing.getValue()) {
                                        EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                                    }
                                    if (this.doubleBreak.getValue()) {
                                        if (secondPos == null || this.isAir(secondPos)) {
                                            double breakTime = this.getBreakTime(this.breakPos, slot, 1.0);
                                            this.secondAnim.reset();
                                            this.secondAnim.setLength((long)breakTime);
                                            this.secondTimer.reset();
                                            secondPos = this.breakPos;
                                        }
                                        this.doDoubleBreak(side);
                                    }
                                    SpeedMine.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12968, this.breakPos, side, id));
                                    if (this.rotate.getValue() && !this.shouldYawStep()) {
                                        Alien.ROTATION.snapBack();
                                    }
                                    this.startTime.reset();
                                }
                            }
                        }
                    } else {
                        if (this.breakPos.equals((Object)secondPos)) {
                            secondPos = null;
                        }
                        this.startPacket = false;
                        ghost = false;
                        complete = false;
                        this.breakNumber = 0;
                        this.breakPos = null;
                    }
                }
            }
        }
    }

    private void breakBlock(class_2338 breakPos) {
        SpeedMine.mc.field_1687.method_8320(breakPos).method_26204().method_9576((class_1937)SpeedMine.mc.field_1687, breakPos, SpeedMine.mc.field_1687.method_8320(breakPos), (class_1657)SpeedMine.mc.field_1724);
    }

    void doDoubleBreak(class_2350 side) {
        SpeedMine.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12968, this.breakPos, side, id));
        SpeedMine.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12973, this.breakPos, side, id));
    }

    boolean placeCrystal() {
        int crystal = this.findCrystal();
        if (crystal != -1) {
            int oldSlot = SpeedMine.mc.field_1724.method_31548().field_7545;
            this.doSwap(crystal, crystal);
            BlockUtil.placeCrystal(this.breakPos.method_10084(), this.placeRotate.getValue());
            this.doSwap(oldSlot, crystal);
            this.placeTimer.reset();
            return !this.waitPlace.getValue();
        }
        return true;
    }

    @EventListener
    public void onAttackBlock(ClickBlockEvent event) {
        if (!SpeedMine.nullCheck() && !SpeedMine.mc.field_1724.method_7337()) {
            event.cancel();
            class_2338 pos = event.getPos();
            if (!(pos.equals((Object)this.breakPos) || SpeedMine.unbreakable(pos) || this.breakPos != null && this.preferWeb.getValue() && BlockUtil.getBlock(this.breakPos) == class_2246.field_10343 || this.breakPos != null && this.preferHead.getValue() && SpeedMine.mc.field_1724.method_20448() && EntityUtil.getPlayerPos(true).method_10084().equals((Object)this.breakPos) || BlockUtil.getClickSideStrict(pos) == null || (double)class_3532.method_15355((float)((float)SpeedMine.mc.field_1724.method_33571().method_1025(pos.method_46558()))) > this.range.getValue())) {
                this.breakPos = pos;
                this.breakNumber = 0;
                this.startPacket = false;
                ghost = false;
                complete = false;
                this.mineTimer.reset();
                this.animationTime.reset();
                if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
                    class_2350 side = BlockUtil.getClickSide(this.breakPos);
                    if (this.rotate.getValue()) {
                        class_2382 vec3i = side.method_10163();
                        if (!this.faceVector(this.breakPos.method_46558().method_1019(new class_243((double)vec3i.method_10263() * 0.5, (double)vec3i.method_10264() * 0.5, (double)vec3i.method_10260() * 0.5)))) {
                            return;
                        }
                    }
                    if (this.startTime.passed(this.startDelay.getValueInt())) {
                        int slot;
                        if (this.swing.getValue()) {
                            EntityUtil.swingHand(class_1268.field_5808, this.swingMode.getValue());
                        }
                        if (this.doubleBreak.getValue()) {
                            if (secondPos == null || this.isAir(secondPos)) {
                                int slot2 = this.getTool(this.breakPos);
                                if (slot2 == -1) {
                                    slot2 = SpeedMine.mc.field_1724.method_31548().field_7545;
                                }
                                this.secondFinalTime = this.getBreakTime(this.breakPos, slot2, 1.0);
                                this.secondAnim.reset();
                                this.secondAnim.setLength((long)this.secondFinalTime);
                                this.secondTimer.reset();
                                secondPos = this.breakPos;
                            }
                            this.doDoubleBreak(side);
                        }
                        if ((slot = this.getTool(this.breakPos)) == -1) {
                            slot = SpeedMine.mc.field_1724.method_31548().field_7545;
                        }
                        this.breakFinalTime = this.getBreakTime(this.breakPos, slot);
                        SpeedMine.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12968, this.breakPos, side, id));
                        if (this.rotate.getValue() && !this.shouldYawStep()) {
                            Alien.ROTATION.snapBack();
                        }
                        this.startTime.reset();
                    }
                }
            }
        }
    }

    public void mine(class_2338 pos) {
        if (!SpeedMine.nullCheck()) {
            if (SpeedMine.mc.field_1724.method_7337()) {
                SpeedMine.mc.field_1761.method_2910(pos, BlockUtil.getClickSide(pos));
            } else if (this.isOff()) {
                SpeedMine.mc.field_1761.method_2910(pos, BlockUtil.getClickSide(pos));
            } else if (!(pos.equals((Object)this.breakPos) || SpeedMine.unbreakable(pos) || this.breakPos != null && this.preferWeb.getValue() && BlockUtil.getBlock(this.breakPos) == class_2246.field_10343 || this.breakPos != null && this.preferHead.getValue() && SpeedMine.mc.field_1724.method_20448() && EntityUtil.getPlayerPos(true).method_10084().equals((Object)this.breakPos) || BlockUtil.getClickSideStrict(pos) == null || (double)class_3532.method_15355((float)((float)SpeedMine.mc.field_1724.method_33571().method_1025(pos.method_46558()))) > this.range.getValue())) {
                this.breakPos = pos;
                this.breakNumber = 0;
                this.startPacket = false;
                ghost = false;
                complete = false;
                this.mineTimer.reset();
                this.animationTime.reset();
            }
        }
    }

    boolean faceVector(class_243 directionVec) {
        if (!this.shouldYawStep()) {
            Alien.ROTATION.lookAt(directionVec);
            return true;
        }
        this.sync.reset();
        this.directionVec = directionVec;
        return Alien.ROTATION.inFov(directionVec, this.fov.getValueFloat()) ? true : !this.checkFov.getValue();
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        if (this.breakPos != null && SpeedMine.mc.field_1687.method_22347(this.breakPos)) {
            complete = true;
        }
        if (!SpeedMine.mc.field_1724.method_7337()) {
            if (secondPos != null) {
                if (this.isAir(secondPos)) {
                    secondPos = null;
                    return;
                }
                if (!this.checkDouble.getValue() || !secondPos.equals((Object)this.breakPos)) {
                    this.secondAnim.setLength((long)this.secondFinalTime);
                    double ease = this.secondAnim.ease(this.ease.getValue());
                    if (this.box.getValue()) {
                        Render3DUtil.drawFill(matrixStack, this.getFillBox(secondPos, ease), this.doubleColor.getValue());
                    }
                    if (this.outline.getValue()) {
                        Render3DUtil.drawBox(matrixStack, this.getOutlineBox(secondPos, ease), this.doubleOutlineColor.getValue());
                    }
                }
            }
            if (this.breakPos != null) {
                progress = (double)this.mineTimer.getMs() / this.breakFinalTime;
                this.animationTime.setLength((long)this.breakFinalTime);
                double easex = this.animationTime.ease(this.ease.getValue());
                if (SpeedMine.unbreakable(this.breakPos)) {
                    if (this.box.getValue()) {
                        Render3DUtil.drawFill(matrixStack, new class_238(this.breakPos), this.startColor.getValue());
                    }
                    if (this.outline.getValue()) {
                        Render3DUtil.drawBox(matrixStack, new class_238(this.breakPos), this.startOutlineColor.getValue());
                    }
                    return;
                }
                if (this.box.getValue()) {
                    Render3DUtil.drawFill(matrixStack, this.getFillBox(this.breakPos, easex), this.getColor(this.animationTime.ease(this.fadeEase.getValue())));
                }
                if (this.outline.getValue()) {
                    Render3DUtil.drawBox(matrixStack, this.getOutlineBox(this.breakPos, easex), this.getOutlineColor(this.animationTime.ease(this.fadeEase.getValue())));
                }
                if (this.text.getValue()) {
                    if (this.isAir(this.breakPos)) {
                        Render3DUtil.drawText3D("Waiting", this.breakPos.method_46558(), -1);
                    } else if ((double)((int)this.mineTimer.getMs()) < this.breakFinalTime) {
                        Render3DUtil.drawText3D(this.df.format(progress * 100.0) + "%", this.breakPos.method_46558(), -1);
                    } else {
                        Render3DUtil.drawText3D("100.0%", this.breakPos.method_46558(), -1);
                    }
                }
            } else {
                progress = 0.0;
            }
        } else {
            progress = 0.0;
        }
    }

    private class_238 getFillBox(class_2338 pos, double ease) {
        switch (this.animation.getValue().ordinal()) {
            case 0: {
                ease = (1.0 - ease) / 2.0;
                return new class_238(pos).method_1002(ease, ease, ease).method_1002(-ease, -ease, -ease);
            }
            case 1: {
                ease = (1.0 - ease) / 2.0;
                return new class_238(pos).method_1002(ease, 0.0, ease).method_1002(-ease, 0.0, -ease);
            }
            case 2: {
                return new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)pos.method_10264() + ease, (double)(pos.method_10260() + 1));
            }
            case 3: {
                return new class_238((double)pos.method_10263(), (double)(pos.method_10264() + 1) - ease, (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)(pos.method_10264() + 1), (double)(pos.method_10260() + 1));
            }
            case 4: {
                return new class_238(pos).method_1002(ease, ease, ease).method_1002(-ease, -ease, -ease);
            }
        }
        return new class_238(pos);
    }

    private class_238 getOutlineBox(class_2338 pos, double ease) {
        ease = Math.min(ease + this.expandLine.getValue(), 1.0);
        switch (this.animation.getValue().ordinal()) {
            case 0: {
                ease = (1.0 - ease) / 2.0;
                return new class_238(pos).method_1002(ease, ease, ease).method_1002(-ease, -ease, -ease);
            }
            case 1: {
                ease = (1.0 - ease) / 2.0;
                return new class_238(pos).method_1002(ease, 0.0, ease).method_1002(-ease, 0.0, -ease);
            }
            case 2: {
                return new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)pos.method_10264() + ease, (double)(pos.method_10260() + 1));
            }
            case 3: {
                return new class_238((double)pos.method_10263(), (double)(pos.method_10264() + 1) - ease, (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)(pos.method_10264() + 1), (double)(pos.method_10260() + 1));
            }
            case 4: {
                return new class_238(pos).method_1002(ease, ease, ease).method_1002(-ease, -ease, -ease);
            }
        }
        return new class_238(pos);
    }

    @EventListener(priority=-200)
    public void onPacketSend(PacketEvent.Send event) {
        if (!SpeedMine.nullCheck() && !SpeedMine.mc.field_1724.method_7337()) {
            if (event.getPacket() instanceof class_2828) {
                if (this.bypassGround.getValue() && !SpeedMine.mc.field_1724.method_6128() && this.breakPos != null && !this.isAir(this.breakPos) && this.bypassTime.getValue() > 0.0 && class_3532.method_15355((float)((float)this.breakPos.method_46558().method_1025(SpeedMine.mc.field_1724.method_33571()))) <= this.range.getValueFloat() + 2.0f) {
                    double breakTime = this.breakFinalTime - this.bypassTime.getValue();
                    if (breakTime <= 0.0 || this.mineTimer.passed((long)breakTime)) {
                        this.sendGroundPacket = true;
                        ((IPlayerMoveC2SPacket)event.getPacket()).setOnGround(true);
                    }
                } else {
                    this.sendGroundPacket = false;
                }
            } else {
                class_2596<?> class_25962 = event.getPacket();
                if (class_25962 instanceof class_2868) {
                    class_2868 packet = (class_2868)class_25962;
                    if (packet.method_12442() != this.lastSlot) {
                        this.lastSlot = packet.method_12442();
                        if (this.switchReset.getValue()) {
                            this.startPacket = false;
                            ghost = false;
                            complete = false;
                            this.mineTimer.reset();
                            this.animationTime.reset();
                        }
                    }
                } else {
                    class_25962 = event.getPacket();
                    if (class_25962 instanceof class_2846) {
                        class_2846 packetx = (class_2846)class_25962;
                        if (packetx.method_12363() == class_2846.class_2847.field_12968) {
                            if (this.breakPos == null || !packetx.method_12362().equals((Object)this.breakPos)) {
                                return;
                            }
                            if (this.grimDisabler.getValue()) {
                                mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12973, packetx.method_12362(), packetx.method_12360()));
                            }
                            this.startPacket = true;
                        } else if (packetx.method_12363() == class_2846.class_2847.field_12973) {
                            if (this.breakPos == null || !packetx.method_12362().equals((Object)this.breakPos)) {
                                return;
                            }
                            if (!this.instant.getValue()) {
                                this.startPacket = false;
                                ghost = false;
                                complete = false;
                            }
                        }
                    }
                }
            }
        }
    }

    boolean canPlaceCrystal(class_2338 pos) {
        class_2338 obsPos = pos.method_10074();
        class_2338 boost = obsPos.method_10084();
        return !(BlockUtil.getBlock(obsPos) != class_2246.field_9987 && BlockUtil.getBlock(obsPos) != class_2246.field_10540 || BlockUtil.getClickSideStrict(obsPos) == null || !this.noEntity(boost) || !this.noEntity(boost.method_10084()) || ClientSetting.INSTANCE.lowVersion.getValue() && !SpeedMine.mc.field_1687.method_22347(boost.method_10084()));
    }

    boolean noEntity(class_2338 pos) {
        for (class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
            if (entity instanceof class_1542 || entity instanceof class_1531 && AntiCheat.INSTANCE.ignoreArmorStand.getValue()) continue;
            return false;
        }
        return true;
    }

    void doSwap(int slot, int inv) {
        if (!this.inventory.getValue()) {
            InventoryUtil.switchToSlot(slot);
        } else {
            InventoryUtil.inventorySwap(inv, SpeedMine.mc.field_1724.method_31548().field_7545);
        }
    }

    int findCrystal() {
        return this.inventory.getValue() ? InventoryUtil.findItemInventorySlot(class_1802.field_8301) : InventoryUtil.findItem(class_1802.field_8301);
    }

    int findBlock(class_2248 block) {
        return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(block) : InventoryUtil.findBlock(block);
    }

    boolean shouldCrystal() {
        return this.crystal.getValue() && (!this.onlyHeadBomber.getValue() || this.obsidian.isPressed()) || CevBreaker.INSTANCE.isOn();
    }

    public static double getBreakTime(class_2338 pos) {
        int slot = INSTANCE.getTool(pos);
        if (slot == -1) {
            slot = SpeedMine.mc.field_1724.method_31548().field_7545;
        }
        return INSTANCE.getBreakTime(pos, slot);
    }

    double getBreakTime(class_2338 pos, int slot) {
        return this.getBreakTime(pos, slot, this.damage.getValue());
    }

    double getBreakTime(class_2338 pos, int slot, double damage) {
        return (double)(1.0f / this.getBlockStrength(pos, SpeedMine.mc.field_1724.method_31548().method_5438(slot)) / 20.0f * 1000.0f) * damage;
    }

    float getBlockStrength(class_2338 position, class_1799 itemStack) {
        class_2680 state = SpeedMine.mc.field_1687.method_8320(position);
        float hardness = state.method_26214((class_1922)SpeedMine.mc.field_1687, position);
        if (hardness < 0.0f) {
            return 0.0f;
        }
        float i = state.method_29291() && !itemStack.method_7951(state) ? 100.0f : 30.0f;
        return this.getDigSpeed(state, itemStack) / hardness / i;
    }

    float getDigSpeed(class_2680 state, class_1799 itemStack) {
        boolean inWeb;
        int efficiencyModifier;
        float digSpeed = this.getDestroySpeed(state, itemStack);
        if (digSpeed > 1.0f && (efficiencyModifier = class_1890.method_8225((class_6880)((class_6880)SpeedMine.mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get()), (class_1799)itemStack)) > 0 && !itemStack.method_7960()) {
            digSpeed += (float)(StrictMath.pow(efficiencyModifier, 2.0) + 1.0);
        }
        if (SpeedMine.mc.field_1724.method_6059(class_1294.field_5917)) {
            digSpeed *= 1.0f + (float)(SpeedMine.mc.field_1724.method_6112(class_1294.field_5917).method_5578() + 1) * 0.2f;
        }
        if (SpeedMine.mc.field_1724.method_6059(class_1294.field_5901)) {
            digSpeed *= (switch (SpeedMine.mc.field_1724.method_6112(class_1294.field_5901).method_5578()) {
                case 0 -> 0.3f;
                case 1 -> 0.09f;
                case 2 -> 0.0027f;
                default -> 8.1E-4f;
            });
        }
        if (SpeedMine.mc.field_1724.method_5869()) {
            digSpeed *= (float)SpeedMine.mc.field_1724.method_5996(class_5134.field_51576).method_6194();
        }
        boolean bl = inWeb = this.checkWeb.getValue() && Alien.PLAYER.isInWeb((class_1657)SpeedMine.mc.field_1724) && SpeedMine.mc.field_1687.method_8320(this.breakPos).method_26204() == class_2246.field_10343;
        if ((!SpeedMine.mc.field_1724.method_24828() || inWeb) && SpeedMine.INSTANCE.checkGround.getValue() && (!this.smart.getValue() || Criticals.INSTANCE.mode.is(Criticals.Mode.Ground) && Criticals.INSTANCE.isOn() || SpeedMine.mc.field_1724.method_6128() || inWeb)) {
            digSpeed /= 5.0f;
        }
        return digSpeed < 0.0f ? 0.0f : digSpeed;
    }

    float getDestroySpeed(class_2680 state, class_1799 itemStack) {
        float destroySpeed = 1.0f;
        if (itemStack != null && !itemStack.method_7960()) {
            destroySpeed *= itemStack.method_7924(state);
        }
        return destroySpeed;
    }

    Color getColor(double quad) {
        int sR = this.startColor.getValue().getRed();
        int sG = this.startColor.getValue().getGreen();
        int sB = this.startColor.getValue().getBlue();
        int sA = this.startColor.getValue().getAlpha();
        int eR = this.endColor.getValue().getRed();
        int eG = this.endColor.getValue().getGreen();
        int eB = this.endColor.getValue().getBlue();
        int eA = this.endColor.getValue().getAlpha();
        return new Color((int)((double)sR + (double)(eR - sR) * quad), (int)((double)sG + (double)(eG - sG) * quad), (int)((double)sB + (double)(eB - sB) * quad), (int)((double)sA + (double)(eA - sA) * quad));
    }

    Color getOutlineColor(double quad) {
        int sR = this.startOutlineColor.getValue().getRed();
        int sG = this.startOutlineColor.getValue().getGreen();
        int sB = this.startOutlineColor.getValue().getBlue();
        int sA = this.startOutlineColor.getValue().getAlpha();
        int eR = this.endOutlineColor.getValue().getRed();
        int eG = this.endOutlineColor.getValue().getGreen();
        int eB = this.endOutlineColor.getValue().getBlue();
        int eA = this.endOutlineColor.getValue().getAlpha();
        return new Color((int)((double)sR + (double)(eR - sR) * quad), (int)((double)sG + (double)(eG - sG) * quad), (int)((double)sB + (double)(eB - sB) * quad), (int)((double)sA + (double)(eA - sA) * quad));
    }

    int getTool(class_2338 pos) {
        if (this.hotBar.getValue()) {
            int index = -1;
            float CurrentFastest = 1.0f;
            for (int i = 0; i < 9; ++i) {
                float destroySpeed;
                float digSpeed;
                class_1799 stack = SpeedMine.mc.field_1724.method_31548().method_5438(i);
                if (stack == class_1799.field_8037 || !((digSpeed = (float)class_1890.method_8225((class_6880)((class_6880)SpeedMine.mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get()), (class_1799)stack)) + (destroySpeed = stack.method_7924(SpeedMine.mc.field_1687.method_8320(pos))) > CurrentFastest)) continue;
                CurrentFastest = digSpeed + destroySpeed;
                index = i;
            }
            return index;
        }
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        float CurrentFastest = 1.0f;
        for (Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            float destroySpeed;
            float digSpeed;
            if (entry.getValue().method_7909() instanceof class_1739 || !((digSpeed = (float)class_1890.method_8225((class_6880)((class_6880)SpeedMine.mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get()), (class_1799)entry.getValue())) + (destroySpeed = entry.getValue().method_7924(SpeedMine.mc.field_1687.method_8320(pos))) > CurrentFastest)) continue;
            CurrentFastest = digSpeed + destroySpeed;
            slot.set(entry.getKey());
        }
        return slot.get();
    }

    private boolean shouldYawStep() {
        return this.whenElytra.getValue() || !SpeedMine.mc.field_1724.method_6128() && (!ElytraFly.INSTANCE.isOn() || !ElytraFly.INSTANCE.isFallFlying()) ? this.yawStep.getValue() && !Velocity.INSTANCE.noRotation() : false;
    }

    boolean isAir(class_2338 breakPos) {
        return SpeedMine.mc.field_1687.method_22347(breakPos) || BlockUtil.getBlock(breakPos) == class_2246.field_10036 && BlockUtil.hasCrystal(breakPos);
    }

    public static boolean unbreakable(class_2338 blockPos) {
        class_2248 block = SpeedMine.mc.field_1687.method_8320(blockPos).method_26204();
        return !(block instanceof class_2189) && (block.method_36555() == -1.0f || block.method_36555() == 100.0f);
    }

    static {
        progress = 0.0;
        ghost = false;
        complete = false;
    }

    public static enum Page {
        General,
        Check,
        Place,
        Rotation,
        Render;

    }

    private static enum Animation {
        Center,
        Grow,
        Up,
        Down,
        Oscillation,
        None;

    }
}

