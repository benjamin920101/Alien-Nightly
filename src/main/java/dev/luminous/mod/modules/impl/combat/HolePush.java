/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2318
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_2665
 *  net.minecraft.class_2680
 *  net.minecraft.class_2769
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.core.impl.RotationManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.combat.AutoCrystal;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2318;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2665;
import net.minecraft.class_2680;
import net.minecraft.class_2769;

public class HolePush
extends Module {
    public static HolePush INSTANCE;
    private static final float[] OFFSETS;
    private static final class_2350[] HORIZONTAL_DIRECTIONS;
    private final EnumSetting<Page> page = this.add(new EnumSetting<Page>("Page", Page.General));
    private final BooleanSetting torch = this.add(new BooleanSetting("Torch", false, () -> this.page.is(Page.General)));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, () -> this.page.is(Page.Rotate)));
    private final BooleanSetting yawDeceive = this.add(new BooleanSetting("YawDeceive", true, () -> this.page.is(Page.Rotate)));
    private final BooleanSetting allowWeb = this.add(new BooleanSetting("AllowWeb", true, () -> this.page.is(Page.Check)));
    public final BindSetting allowKey = this.add(new BindSetting("AllowKey", -1, () -> !this.allowWeb.getValue() && this.page.is(Page.Check)));
    private final BooleanSetting noEating = this.add(new BooleanSetting("EatingPause", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting selfGround = this.add(new BooleanSetting("SelfGround", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", false, () -> this.page.is(Page.Check)));
    private final BooleanSetting onlyNoInside = this.add(new BooleanSetting("OnlyNoInside", false, () -> this.page.is(Page.Check)));
    private final BooleanSetting onlyInside = this.add(new BooleanSetting("OnlyInside", true, () -> this.page.is(Page.Check)));
    private final SliderSetting surroundCheck = this.add(new SliderSetting("SurroundCheck", 0, 0, 4, () -> this.page.is(Page.Check)));
    private final BooleanSetting syncCrystal = this.add(new BooleanSetting("SyncCrystal", true, () -> this.page.is(Page.Check)));
    private final BooleanSetting pistonPacket = this.add(new BooleanSetting("PistonPacket", true, () -> this.page.is(Page.General)));
    private final BooleanSetting powerPacket = this.add(new BooleanSetting("PowerPacket", true, () -> this.page.is(Page.General)));
    private final BooleanSetting mine = this.add(new BooleanSetting("Mine", true, () -> this.page.is(Page.General)));
    private final BooleanSetting pauseCity = this.add(new BooleanSetting("PauseCity", true, () -> this.mine.isOpen() && this.page.is(Page.General)));
    private final SliderSetting updateDelay = this.add(new SliderSetting("Delay", 200, 0, 1000, () -> this.page.is(Page.General)));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true, () -> this.page.is(Page.General)));
    private final SliderSetting range = this.add(new SliderSetting("Range", 4.7, 0.0, 6.0, () -> this.page.is(Page.General)));
    private final SliderSetting placeRange = this.add(new SliderSetting("PlaceRange", 4.7, 0.0, 6.0, () -> this.page.is(Page.General)));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true, () -> this.page.is(Page.General)));
    private final Timer timer = new Timer();

    public HolePush() {
        super("HolePush", Module.Category.Combat);
        this.setChinese("\u5751\u63a8");
        INSTANCE = this;
    }

    public static void pistonFacing(class_2350 i) {
        if (i == class_2350.field_11034) {
            Alien.ROTATION.snapAt(-90.0f, 5.0f);
        } else if (i == class_2350.field_11039) {
            Alien.ROTATION.snapAt(90.0f, 5.0f);
        } else if (i == class_2350.field_11043) {
            Alien.ROTATION.snapAt(180.0f, 5.0f);
        } else if (i == class_2350.field_11035) {
            Alien.ROTATION.snapAt(0.0f, 5.0f);
        }
    }

    @Override
    public boolean onEnable() {
        AutoCrystal.INSTANCE.lastBreakTimer.reset();
        return false;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (!this.timer.passedMs(this.updateDelay.getValue())) {
            return;
        }
        if (this.selfGround.getValue() && !HolePush.mc.field_1724.method_24828()) {
            return;
        }
        class_2248 blockType = this.getBlockType();
        int powerSlot = this.findBlock(blockType);
        int pistonSlot = this.findClass(class_2665.class);
        if (powerSlot == -1 || pistonSlot == -1) {
            if (this.autoDisable.getValue()) {
                this.disable();
            }
            return;
        }
        if (this.syncCrystal.getValue() && AutoCrystal.INSTANCE != null && AutoCrystal.INSTANCE.crystalPos != null) {
            return;
        }
        if (this.noEating.getValue() && HolePush.mc.field_1724.method_6115()) {
            return;
        }
        if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
            return;
        }
        for (class_1657 player : CombatUtil.getEnemies(this.range.getValue())) {
            class_2338 pos;
            BlockPosX playerPos;
            Object z;
            int n;
            Object pos2;
            if (!this.canPush(player).booleanValue()) continue;
            class_2338 basePos = EntityUtil.getEntityPos((class_1297)player);
            for (class_2350 i : HORIZONTAL_DIRECTIONS) {
                pos2 = basePos.method_10093(i);
                if (!this.isTargetHere((class_2338)pos2, (class_1297)player) || !HolePush.mc.field_1687.method_39454((class_1297)player, new class_238(pos2))) continue;
                if (this.tryPush(basePos.method_10093(i.method_10153()), i)) {
                    this.timer.reset();
                    return;
                }
                if (!this.tryPush(basePos.method_10093(i.method_10153()).method_10084(), i)) continue;
                this.timer.reset();
                return;
            }
            for (float x : OFFSETS) {
                pos2 = OFFSETS;
                int n2 = ((class_2338)pos2).length;
                for (n = 0; n < n2; ++n) {
                    z = pos2[n];
                    playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + 0.5, player.method_23321() + (double)z);
                    for (class_2350 i : HORIZONTAL_DIRECTIONS) {
                        pos = playerPos.method_10093(i);
                        if (!this.isTargetHere(pos, (class_1297)player) || !HolePush.mc.field_1687.method_39454((class_1297)player, new class_238(pos))) continue;
                        if (this.tryPush(playerPos.method_10093(i.method_10153()), i)) {
                            this.timer.reset();
                            return;
                        }
                        if (!this.tryPush(playerPos.method_10093(i.method_10153()).method_10084(), i)) continue;
                        this.timer.reset();
                        return;
                    }
                }
            }
            if (!HolePush.mc.field_1687.method_39454((class_1297)player, new class_238((class_2338)new BlockPosX(player.method_23317(), player.method_23318() + 2.5, player.method_23321())))) {
                for (class_2350 i : HORIZONTAL_DIRECTIONS) {
                    class_2338 pos3 = basePos.method_10093(i);
                    class_238 box = player.method_5829().method_997(new class_243((double)i.method_10148(), (double)i.method_10164(), (double)i.method_10165()));
                    if (this.getBlock(pos3.method_10084()) == class_2246.field_10379 || HolePush.mc.field_1687.method_39454((class_1297)player, box.method_989(0.0, 1.0, 0.0)) || this.isTargetHere(pos3, (class_1297)player)) continue;
                    if (this.tryPush(basePos.method_10093(i.method_10153()).method_10084(), i)) {
                        this.timer.reset();
                        return;
                    }
                    if (!this.tryPush(basePos.method_10093(i.method_10153()), i)) continue;
                    this.timer.reset();
                    return;
                }
            }
            for (float x : OFFSETS) {
                float[] fArray = OFFSETS;
                int n3 = fArray.length;
                for (n = 0; n < n3; ++n) {
                    z = fArray[n];
                    playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + 0.5, player.method_23321() + (double)z);
                    for (class_2350 i : HORIZONTAL_DIRECTIONS) {
                        pos = playerPos.method_10093(i);
                        if (!this.isTargetHere(pos, (class_1297)player)) continue;
                        if (this.tryPush(playerPos.method_10093(i.method_10153()).method_10084(), i)) {
                            this.timer.reset();
                            return;
                        }
                        if (!this.tryPush(playerPos.method_10093(i.method_10153()), i)) continue;
                        this.timer.reset();
                        return;
                    }
                }
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    private boolean tryPush(class_2338 piston, class_2350 direction) {
        class_2680 state;
        class_2248 blockType = this.getBlockType();
        if (!HolePush.mc.field_1687.method_22347(piston.method_10093(direction))) {
            return false;
        }
        if (this.isTrueFacing(piston, direction) && this.facingCheck(piston) && BlockUtil.clientCanPlace(piston, false)) {
            boolean canPower = false;
            if (BlockUtil.getPlaceSide(piston, this.placeRange.getValue()) != null) {
                CombatUtil.modifyPos = piston;
                CombatUtil.modifyBlockState = class_2246.field_10560.method_9564();
                for (class_2350 class_23502 : class_2350.values()) {
                    if (this.getBlock(piston.method_10093(class_23502)) != blockType) continue;
                    canPower = true;
                    break;
                }
                for (class_2350 class_23503 : class_2350.values()) {
                    if (canPower) break;
                    if (!BlockUtil.canPlace(piston.method_10093(class_23503), this.placeRange.getValue())) continue;
                    canPower = true;
                }
                CombatUtil.modifyPos = null;
                if (canPower) {
                    int pistonSlot = this.findClass(class_2665.class);
                    class_2350 side = BlockUtil.getPlaceSide(piston, this.placeRange.getValue());
                    if (side != null) {
                        if (this.rotate.getValue()) {
                            Alien.ROTATION.lookAt(piston.method_10093(side), side.method_10153());
                        }
                        if (this.yawDeceive.getValue()) {
                            HolePush.pistonFacing(direction.method_10153());
                        }
                        old = HolePush.mc.field_1724.method_31548().field_7545;
                        this.doSwap(pistonSlot);
                        BlockUtil.placeBlock(piston, false, this.pistonPacket.getValue());
                        if (this.inventory.getValue()) {
                            this.doSwap(pistonSlot);
                            EntityUtil.syncInventory();
                        } else {
                            this.doSwap(old);
                        }
                        if (this.rotate.getValue() && this.yawDeceive.getValue()) {
                            Alien.ROTATION.lookAt(piston.method_10093(side), side.method_10153());
                        }
                        if (this.rotate.getValue()) {
                            Alien.ROTATION.snapBack();
                        }
                        for (class_2350 i : class_2350.values()) {
                            if (this.getBlock(piston.method_10093(i)) != blockType) continue;
                            if (this.shouldMine()) {
                                SpeedMine.INSTANCE.mine(piston.method_10093(i));
                            }
                            if (this.autoDisable.getValue()) {
                                this.disable();
                            }
                            return true;
                        }
                        for (class_2350 i : class_2350.values()) {
                            if (i == class_2350.field_11036 && this.torch.getValue() || !BlockUtil.canPlace(piston.method_10093(i), this.placeRange.getValue())) continue;
                            int oldSlot = HolePush.mc.field_1724.method_31548().field_7545;
                            int powerSlot = this.findBlock(blockType);
                            this.doSwap(powerSlot);
                            BlockUtil.placeBlock(piston.method_10093(i), this.rotate.getValue(), this.powerPacket.getValue());
                            if (this.inventory.getValue()) {
                                this.doSwap(powerSlot);
                                EntityUtil.syncInventory();
                            } else {
                                this.doSwap(oldSlot);
                            }
                            if (this.shouldMine()) {
                                SpeedMine.INSTANCE.mine(piston.method_10093(i));
                            }
                            return true;
                        }
                        return true;
                    }
                }
            } else {
                int var9_24 = 0;
                class_2350 powerFacing = null;
                class_2350[] side = class_2350.values();
                old = side.length;
                boolean bl = false;
                while (var9_24 < old) {
                    class_2350 i = side[var9_24];
                    if (i != class_2350.field_11036 || !this.torch.getValue()) {
                        if (powerFacing != null) break;
                        CombatUtil.modifyPos = piston.method_10093(i);
                        CombatUtil.modifyBlockState = blockType.method_9564();
                        if (BlockUtil.getPlaceSide(piston, this.placeRange.getValue()) != null) {
                            powerFacing = i;
                        }
                        CombatUtil.modifyPos = null;
                        if (powerFacing != null && !BlockUtil.canPlace(piston.method_10093(powerFacing))) {
                            powerFacing = null;
                        }
                    }
                    ++var9_24;
                }
                if (powerFacing != null) {
                    int oldSlot = HolePush.mc.field_1724.method_31548().field_7545;
                    int powerSlot = this.findBlock(blockType);
                    this.doSwap(powerSlot);
                    BlockUtil.placeBlock(piston.method_10093(powerFacing), this.rotate.getValue(), this.powerPacket.getValue());
                    if (this.inventory.getValue()) {
                        this.doSwap(powerSlot);
                        EntityUtil.syncInventory();
                    } else {
                        this.doSwap(oldSlot);
                    }
                    CombatUtil.modifyPos = piston.method_10093(powerFacing);
                    CombatUtil.modifyBlockState = blockType.method_9564();
                    int n = this.findClass(class_2665.class);
                    class_2350 side2 = BlockUtil.getPlaceSide(piston, this.placeRange.getValue());
                    if (side2 != null) {
                        if (this.rotate.getValue()) {
                            Alien.ROTATION.lookAt(piston.method_10093(side2), side2.method_10153());
                        }
                        if (this.yawDeceive.getValue()) {
                            HolePush.pistonFacing(direction.method_10153());
                        }
                        int old2 = HolePush.mc.field_1724.method_31548().field_7545;
                        this.doSwap(n);
                        BlockUtil.placeBlock(piston, false, this.pistonPacket.getValue());
                        if (this.inventory.getValue()) {
                            this.doSwap(n);
                            EntityUtil.syncInventory();
                        } else {
                            this.doSwap(old2);
                        }
                        if (this.rotate.getValue() && this.yawDeceive.getValue()) {
                            Alien.ROTATION.lookAt(piston.method_10093(side2), side2.method_10153());
                        }
                        if (this.rotate.getValue()) {
                            Alien.ROTATION.snapBack();
                        }
                    }
                    CombatUtil.modifyPos = null;
                    return true;
                }
            }
        }
        if ((state = HolePush.mc.field_1687.method_8320(piston)).method_26204() instanceof class_2665 && this.getBlockState(piston).method_11654((class_2769)class_2318.field_10927) == direction) {
            for (class_2350 direction4 : class_2350.values()) {
                if (this.getBlock(piston.method_10093(direction4)) != blockType) continue;
                if (this.autoDisable.getValue()) {
                    this.disable();
                    return true;
                }
                return false;
            }
            for (class_2350 direction5 : class_2350.values()) {
                if (direction5 == class_2350.field_11036 && this.torch.getValue() || !BlockUtil.canPlace(piston.method_10093(direction5), this.placeRange.getValue())) continue;
                int n = HolePush.mc.field_1724.method_31548().field_7545;
                int powerSlot = this.findBlock(blockType);
                this.doSwap(powerSlot);
                BlockUtil.placeBlock(piston.method_10093(direction5), this.rotate.getValue(), this.powerPacket.getValue());
                if (this.inventory.getValue()) {
                    this.doSwap(powerSlot);
                    EntityUtil.syncInventory();
                } else {
                    this.doSwap(n);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String getInfo() {
        if (this.syncCrystal.getValue() && AutoCrystal.INSTANCE != null && AutoCrystal.INSTANCE.crystalPos != null) {
            return "WaitSync";
        }
        if (this.allowWeb.getValue() || this.allowKey.isPressed()) {
            return "CrazyDog";
        }
        return "SmartPush";
    }

    private boolean shouldMine() {
        if (!this.mine.getValue()) {
            return false;
        }
        if (!this.pauseCity.getValue()) {
            return true;
        }
        class_2338 breakPos = SpeedMine.getBreakPos();
        return breakPos != null && breakPos.equals((Object)SpeedMine.secondPos);
    }

    private boolean facingCheck(class_2338 pos) {
        if (ClientSetting.INSTANCE.lowVersion.getValue()) {
            class_2350 direction = MathUtil.getDirectionFromEntityLiving(pos, (class_1309)HolePush.mc.field_1724);
            return direction != class_2350.field_11036 && direction != class_2350.field_11033;
        }
        return true;
    }

    private boolean isTrueFacing(class_2338 pos, class_2350 facing) {
        if (this.yawDeceive.getValue()) {
            return true;
        }
        class_2350 side = BlockUtil.getPlaceSide(pos);
        if (side == null) {
            return false;
        }
        class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        float[] rotation = RotationManager.getRotation(directionVec);
        return MathUtil.getFacingOrder(rotation[0], rotation[1]).method_10153() == facing;
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, HolePush.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    public int findBlock(class_2248 blockIn) {
        if (this.inventory.getValue()) {
            return InventoryUtil.findBlockInventorySlot(blockIn);
        }
        return InventoryUtil.findBlock(blockIn);
    }

    public int findClass(Class<?> clazz) {
        if (this.inventory.getValue()) {
            return InventoryUtil.findClassInventorySlot(clazz);
        }
        return InventoryUtil.findClass(clazz);
    }

    private boolean burrowUpdate(class_1657 player) {
        for (float x : new float[]{0.0f, 0.3f, -0.3f}) {
            for (float z : new float[]{0.0f, 0.3f, -0.3f}) {
                BlockPosX pos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + 1.5, player.method_23321() + (double)z);
                class_2248 block = HolePush.mc.field_1687.method_8320((class_2338)pos).method_26204();
                if (!new class_238((class_2338)pos).method_994(player.method_5829()) || block != class_2246.field_10540 && block != class_2246.field_23152 && block != class_2246.field_9987 && block != class_2246.field_10443) continue;
                return true;
            }
        }
        return false;
    }

    private Boolean canPush(class_1657 player) {
        if (this.onlyGround.getValue() && !player.method_24828()) {
            return false;
        }
        if (!this.allowWeb.getValue() && Alien.PLAYER.isInWeb(player) && !this.allowKey.isPressed()) {
            return false;
        }
        if (this.onlyNoInside.getValue() && this.burrowUpdate(player)) {
            return false;
        }
        if (this.onlyInside.getValue() && !this.burrowUpdate(player)) {
            return false;
        }
        int progress = 0;
        if (HolePush.mc.field_1687.method_39454((class_1297)player, new class_238((class_2338)new BlockPosX(player.method_23317() + 1.0, player.method_23318() + 0.5, player.method_23321())))) {
            ++progress;
        }
        if (HolePush.mc.field_1687.method_39454((class_1297)player, new class_238((class_2338)new BlockPosX(player.method_23317() - 1.0, player.method_23318() + 0.5, player.method_23321())))) {
            ++progress;
        }
        if (HolePush.mc.field_1687.method_39454((class_1297)player, new class_238((class_2338)new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321() + 1.0)))) {
            ++progress;
        }
        if (HolePush.mc.field_1687.method_39454((class_1297)player, new class_238((class_2338)new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321() - 1.0)))) {
            ++progress;
        }
        class_238 baseBox = new class_238((class_2338)new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321()));
        for (float x : OFFSETS) {
            for (float z : OFFSETS) {
                BlockPosX playerPos = new BlockPosX(player.method_23317() + (double)x, player.method_23318() + 0.5, player.method_23321() + (double)z);
                for (class_2350 i : HORIZONTAL_DIRECTIONS) {
                    class_2338 pos = playerPos.method_10093(i);
                    if (!this.isTargetHere(pos, (class_1297)player)) continue;
                    if (HolePush.mc.field_1687.method_39454((class_1297)player, new class_238(pos))) {
                        return true;
                    }
                    if (!((double)progress > this.surroundCheck.getValue() - 1.0)) continue;
                    return true;
                }
            }
        }
        if (!HolePush.mc.field_1687.method_39454((class_1297)player, new class_238((class_2338)new BlockPosX(player.method_23317(), player.method_23318() + 2.5, player.method_23321())))) {
            class_2338 playerBasePos = EntityUtil.getEntityPos((class_1297)player);
            for (class_2350 i : HORIZONTAL_DIRECTIONS) {
                class_2338 pos = playerBasePos.method_10093(i);
                class_238 box = player.method_5829().method_997(new class_243((double)i.method_10148(), (double)i.method_10164(), (double)i.method_10165()));
                if (this.getBlock(pos.method_10084()) == class_2246.field_10379 || HolePush.mc.field_1687.method_39454((class_1297)player, box.method_989(0.0, 1.0, 0.0)) || this.isTargetHere(pos, (class_1297)player) || !HolePush.mc.field_1687.method_39454((class_1297)player, baseBox)) continue;
                return true;
            }
        }
        return (double)progress > this.surroundCheck.getValue() - 1.0 || Alien.HOLE.isHard(new BlockPosX(player.method_23317(), player.method_23318() + 0.5, player.method_23321()));
    }

    private boolean isTargetHere(class_2338 pos, class_1297 target) {
        return new class_238(pos).method_994(target.method_5829());
    }

    private class_2248 getBlock(class_2338 pos) {
        return HolePush.mc.field_1687.method_8320(pos).method_26204();
    }

    private class_2248 getBlockType() {
        if (this.torch.getValue()) {
            return class_2246.field_10523;
        }
        return class_2246.field_10002;
    }

    private class_2680 getBlockState(class_2338 pos) {
        return HolePush.mc.field_1687.method_8320(pos);
    }

    static {
        OFFSETS = new float[]{-0.25f, 0.0f, 0.25f};
        HORIZONTAL_DIRECTIONS = new class_2350[]{class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034};
    }

    public static enum Page {
        General,
        Rotate,
        Check;

    }
}

