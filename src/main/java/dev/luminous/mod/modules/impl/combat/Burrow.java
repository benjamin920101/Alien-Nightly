/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.ExperienceOrbEntity
 *  net.minecraft.entity.decoration.EndCrystalEntity
 *  net.minecraft.entity.ItemEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.entity.projectile.ArrowEntity
 *  net.minecraft.entity.projectile.thrown.ExperienceBottleEntity
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$PositionAndOnGround
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$LookAndOnGround
 *  net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.hit.BlockHitResult
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.core.impl.RotationManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.BlockHitResult;

public class Burrow
extends Module {
    public static Burrow INSTANCE;
    private final EnumSetting<RotateMode> rotate = this.add(new EnumSetting<RotateMode>("RotateMode", RotateMode.Bypass));
    private final EnumSetting<LagBackMode> lagMode = this.add(new EnumSetting<LagBackMode>("LagMode", LagBackMode.TrollHack));
    private final EnumSetting<LagBackMode> aboveLagMode = this.add(new EnumSetting<LagBackMode>("MoveLagMode", LagBackMode.Smart));
    private final List<class_2338> placePos = new ArrayList<class_2338>();
    private final Timer timer = new Timer();
    private final Timer webTimer = new Timer();
    private final BooleanSetting disable = this.add(new BooleanSetting("Disable", true));
    private final BooleanSetting jumpDisable = this.add(new BooleanSetting("JumpDisable", true, () -> !this.disable.getValue()));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 500, 0, 1000, () -> !this.disable.getValue()));
    private final SliderSetting webTime = this.add(new SliderSetting("WebTime", 0, 0, 500));
    private final BooleanSetting enderChest = this.add(new BooleanSetting("EnderChest", true));
    private final BooleanSetting antiLag = this.add(new BooleanSetting("AntiLag", false));
    private final BooleanSetting single = this.add(new BooleanSetting("SingleBlock", false));
    private final BooleanSetting detectMine = this.add(new BooleanSetting("DetectMining", false));
    private final BooleanSetting headFill = this.add(new BooleanSetting("HeadFill", false));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false));
    private final BooleanSetting down = this.add(new BooleanSetting("Down", true));
    private final BooleanSetting noSelfPos = this.add(new BooleanSetting("NoSelfPos", false));
    private final BooleanSetting packetPlace = this.add(new BooleanSetting("PacketPlace", true));
    private final BooleanSetting sound = this.add(new BooleanSetting("Sound", true));
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 4.0, 1.0, 4.0, 1.0));
    private final BooleanSetting breakCrystal = this.add(new BooleanSetting("Break", true));
    private final BooleanSetting wait = this.add(new BooleanSetting("Wait", true, this.disable::getValue));
    private final BooleanSetting fakeMove = this.add(new BooleanSetting("FakeMove", true).setParent());
    private final BooleanSetting center = this.add(new BooleanSetting("AllowCenter", true, this.fakeMove::isOpen));
    private final SliderSetting preCorrect = this.add(new SliderSetting("PreCorrect", 0.25, 0.0, 1.0, 0.001, this.fakeMove::isOpen));
    private final SliderSetting moveDis = this.add(new SliderSetting("MoveDis", 0.25, 0.0, 1.0, 0.001, this.fakeMove::isOpen));
    private final SliderSetting moveDis2 = this.add(new SliderSetting("MoveDis2", 0.25, 0.0, 1.0, 0.001, this.fakeMove::isOpen));
    private final SliderSetting moveCorrect2 = this.add(new SliderSetting("Correct", 0.25, 0.0, 1.0, 0.001, this.fakeMove::isOpen));
    private final SliderSetting yOffset = this.add(new SliderSetting("YOffset", 0.01, 0.0, 1.0, 0.001, this.fakeMove::isOpen));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final SliderSetting smartX = this.add(new SliderSetting("SmartXZ", 3.0, 0.0, 10.0, 0.1, () -> this.lagMode.getValue() == LagBackMode.Smart || this.aboveLagMode.getValue() == LagBackMode.Smart));
    private final SliderSetting smartUp = this.add(new SliderSetting("SmartUp", 3.0, 0.0, 10.0, 0.1, () -> this.lagMode.getValue() == LagBackMode.Smart || this.aboveLagMode.getValue() == LagBackMode.Smart));
    private final SliderSetting smartDown = this.add(new SliderSetting("SmartDown", 3.0, 0.0, 10.0, 0.1, () -> this.lagMode.getValue() == LagBackMode.Smart || this.aboveLagMode.getValue() == LagBackMode.Smart));
    private final SliderSetting smartDistance = this.add(new SliderSetting("SmartDistance", 2.0, 0.0, 10.0, 0.1, () -> this.lagMode.getValue() == LagBackMode.Smart || this.aboveLagMode.getValue() == LagBackMode.Smart));
    private int progress = 0;
    private class_243 currentPos;

    public Burrow() {
        super("Burrow", Module.Category.Combat);
        this.setChinese("\u5361\u9ed1\u66dc\u77f3");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(ClientTickEvent event) {
        if ((!this.inventory.getValue() || EntityUtil.inInventory()) && event.isPost()) {
            if (!this.disable.getValue() && this.jumpDisable.getValue() && Burrow.mc.field_1724.field_3913.field_3904) {
                this.disable();
                return;
            }
            if (Alien.PLAYER.isInWeb((class_1657)Burrow.mc.field_1724)) {
                this.webTimer.reset();
                return;
            }
            if (this.usingPause.getValue() && Burrow.mc.field_1724.method_6115()) {
                return;
            }
            if (!this.webTimer.passedMs(this.webTime.getValue())) {
                return;
            }
            if (!this.disable.getValue() && !this.timer.passedMs(this.delay.getValue())) {
                return;
            }
            if (!Burrow.mc.field_1724.method_24828()) {
                return;
            }
            if (this.antiLag.getValue() && !BlockUtil.canCollide((class_1297)Burrow.mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10074()))) {
                return;
            }
            if (this.single.getValue() && EntityUtil.isInsideBlock()) {
                if (this.disable.getValue()) {
                    this.disable();
                }
                return;
            }
            if (Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue()) {
                return;
            }
            int oldSlot = Burrow.mc.field_1724.method_31548().field_7545;
            int block = this.getBlock();
            if (block == -1) {
                CommandManager.sendMessageId("\u00a74No block found.", this.hashCode() - 1);
                this.disable();
                return;
            }
            this.progress = 0;
            this.placePos.clear();
            double offset = this.single.getValue() ? 0.0 : AntiCheat.getOffset();
            BlockPosX pos1 = new BlockPosX(Burrow.mc.field_1724.method_23317() + offset, Burrow.mc.field_1724.method_23318() + 0.5, Burrow.mc.field_1724.method_23321() + offset);
            BlockPosX pos2 = new BlockPosX(Burrow.mc.field_1724.method_23317() - offset, Burrow.mc.field_1724.method_23318() + 0.5, Burrow.mc.field_1724.method_23321() + offset);
            BlockPosX pos3 = new BlockPosX(Burrow.mc.field_1724.method_23317() + offset, Burrow.mc.field_1724.method_23318() + 0.5, Burrow.mc.field_1724.method_23321() - offset);
            BlockPosX pos4 = new BlockPosX(Burrow.mc.field_1724.method_23317() - offset, Burrow.mc.field_1724.method_23318() + 0.5, Burrow.mc.field_1724.method_23321() - offset);
            BlockPosX pos5 = new BlockPosX(Burrow.mc.field_1724.method_23317() + offset, Burrow.mc.field_1724.method_23318() + 1.5, Burrow.mc.field_1724.method_23321() + offset);
            BlockPosX pos6 = new BlockPosX(Burrow.mc.field_1724.method_23317() - offset, Burrow.mc.field_1724.method_23318() + 1.5, Burrow.mc.field_1724.method_23321() + offset);
            BlockPosX pos7 = new BlockPosX(Burrow.mc.field_1724.method_23317() + offset, Burrow.mc.field_1724.method_23318() + 1.5, Burrow.mc.field_1724.method_23321() - offset);
            BlockPosX pos8 = new BlockPosX(Burrow.mc.field_1724.method_23317() - offset, Burrow.mc.field_1724.method_23318() + 1.5, Burrow.mc.field_1724.method_23321() - offset);
            BlockPosX pos9 = new BlockPosX(Burrow.mc.field_1724.method_23317() + offset, Burrow.mc.field_1724.method_23318() - 1.0, Burrow.mc.field_1724.method_23321() + offset);
            BlockPosX pos10 = new BlockPosX(Burrow.mc.field_1724.method_23317() - offset, Burrow.mc.field_1724.method_23318() - 1.0, Burrow.mc.field_1724.method_23321() + offset);
            BlockPosX pos11 = new BlockPosX(Burrow.mc.field_1724.method_23317() + offset, Burrow.mc.field_1724.method_23318() - 1.0, Burrow.mc.field_1724.method_23321() - offset);
            BlockPosX pos12 = new BlockPosX(Burrow.mc.field_1724.method_23317() - offset, Burrow.mc.field_1724.method_23318() - 1.0, Burrow.mc.field_1724.method_23321() - offset);
            class_2338 playerPos = EntityUtil.getPlayerPos(true);
            boolean headFill = false;
            if (!(this.canPlace(pos1) || this.canPlace(pos2) || this.canPlace(pos3) || this.canPlace(pos4))) {
                boolean cantDown;
                boolean cantHeadFill = !this.headFill.getValue() || !this.canPlace(pos5) && !this.canPlace(pos6) && !this.canPlace(pos7) && !this.canPlace(pos8);
                boolean bl = cantDown = !this.down.getValue() || !this.canPlace(pos9) && !this.canPlace(pos10) && !this.canPlace(pos11) && !this.canPlace(pos12);
                if (cantHeadFill) {
                    if (cantDown) {
                        if (!this.wait.getValue() && this.disable.getValue()) {
                            this.disable();
                        }
                        return;
                    }
                } else {
                    headFill = true;
                }
            }
            boolean above = false;
            class_2338 headPos = EntityUtil.getPlayerPos(true).method_10086(2);
            boolean rotate = this.rotate.getValue() == RotateMode.Normal;
            CombatUtil.attackCrystal(pos1, rotate, false);
            CombatUtil.attackCrystal(pos2, rotate, false);
            CombatUtil.attackCrystal(pos3, rotate, false);
            CombatUtil.attackCrystal(pos4, rotate, false);
            if (!(headFill || Burrow.mc.field_1724.method_20448() || this.trapped(headPos) || this.trapped(headPos.method_10069(1, 0, 0)) || this.trapped(headPos.method_10069(-1, 0, 0)) || this.trapped(headPos.method_10069(0, 0, 1)) || this.trapped(headPos.method_10069(0, 0, -1)) || this.trapped(headPos.method_10069(1, 0, -1)) || this.trapped(headPos.method_10069(-1, 0, -1)) || this.trapped(headPos.method_10069(1, 0, 1)) || this.trapped(headPos.method_10069(-1, 0, 1)))) {
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 0.4199999868869781, Burrow.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 0.7531999805212017, Burrow.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 0.9999957640154541, Burrow.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1.1661092609382138, Burrow.mc.field_1724.method_23321(), false));
                this.currentPos = new class_243(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1.1661092609382138, Burrow.mc.field_1724.method_23321());
            } else {
                above = true;
                if (!this.fakeMove.getValue()) {
                    if (!this.wait.getValue() && this.disable.getValue()) {
                        this.disable();
                    }
                    return;
                }
                if (!this.fakeMove()) {
                    return;
                }
            }
            this.timer.reset();
            this.doSwap(block);
            if (this.rotate.getValue() == RotateMode.Bypass) {
                if (above) {
                    float[] angle = RotationManager.getRotation(this.currentPos.method_1031(0.0, (double)Burrow.mc.field_1724.method_18381(Burrow.mc.field_1724.method_18376()), 0.0), Burrow.mc.field_1724.method_19538());
                    Alien.ROTATION.snapAt(angle[0], angle[1]);
                } else {
                    Alien.ROTATION.snapAt(Alien.ROTATION.rotationYaw, 90.0f);
                }
            }
            this.placeBlock(playerPos, rotate);
            this.placeBlock(pos1, rotate);
            this.placeBlock(pos2, rotate);
            this.placeBlock(pos3, rotate);
            this.placeBlock(pos4, rotate);
            if (this.down.getValue()) {
                this.placeBlock(pos9, rotate);
                this.placeBlock(pos10, rotate);
                this.placeBlock(pos11, rotate);
                this.placeBlock(pos12, rotate);
            }
            if (this.headFill.getValue() && above) {
                this.placeBlock(pos5, rotate);
                this.placeBlock(pos6, rotate);
                this.placeBlock(pos7, rotate);
                this.placeBlock(pos8, rotate);
            }
            if (this.inventory.getValue()) {
                this.doSwap(block);
                EntityUtil.syncInventory();
            } else {
                this.doSwap(oldSlot);
            }
            switch ((above ? this.aboveLagMode.getValue() : this.lagMode.getValue()).ordinal()) {
                case 0: {
                    ArrayList<BlockPosX> list = new ArrayList<BlockPosX>();
                    for (double x = Burrow.mc.field_1724.method_19538().method_10216() - this.smartX.getValue(); x < Burrow.mc.field_1724.method_19538().method_10216() + this.smartX.getValue(); x += 1.0) {
                        for (double z = Burrow.mc.field_1724.method_19538().method_10215() - this.smartX.getValue(); z < Burrow.mc.field_1724.method_19538().method_10215() + this.smartX.getValue(); z += 1.0) {
                            for (double d = Burrow.mc.field_1724.method_19538().method_10214() - this.smartDown.getValue(); d < Burrow.mc.field_1724.method_19538().method_10214() + this.smartUp.getValue(); d += 1.0) {
                                list.add(new BlockPosX(x, d, z));
                            }
                        }
                    }
                    double distance = 0.0;
                    class_2338 bestPos = null;
                    for (class_2338 class_23382 : list) {
                        if (!this.canMove(class_23382) || (double)class_3532.method_15355((float)((float)Burrow.mc.field_1724.method_5707(class_23382.method_46558().method_1031(0.0, -0.5, 0.0)))) < this.smartDistance.getValue() || bestPos != null && !(Burrow.mc.field_1724.method_5707(class_23382.method_46558()) < distance)) continue;
                        bestPos = class_23382;
                        distance = Burrow.mc.field_1724.method_5707(class_23382.method_46558());
                    }
                    if (bestPos == null) break;
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829((double)bestPos.method_10263() + 0.5, (double)bestPos.method_10264(), (double)bestPos.method_10260() + 0.5, false));
                    break;
                }
                case 1: {
                    for (int i = 0; i < 20; ++i) {
                        mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1337.0, Burrow.mc.field_1724.method_23321(), false));
                    }
                    break;
                }
                case 2: {
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 2.3400880035762786, Burrow.mc.field_1724.method_23321(), false));
                    break;
                }
                case 3: {
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), -70.0, Burrow.mc.field_1724.method_23321(), false));
                    break;
                }
                case 4: {
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), -7.0, Burrow.mc.field_1724.method_23321(), false));
                    break;
                }
                case 5: {
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1.9, Burrow.mc.field_1724.method_23321(), false));
                    break;
                }
                case 6: {
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2831(-180.0f, -90.0f, false));
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2831(180.0f, 90.0f, false));
                    break;
                }
                case 7: {
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1.16610926093821, Burrow.mc.field_1724.method_23321(), false));
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1.170005801788139, Burrow.mc.field_1724.method_23321(), false));
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1.2426308013947485, Burrow.mc.field_1724.method_23321(), false));
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 2.3400880035762786, Burrow.mc.field_1724.method_23321(), false));
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 2.640088003576279, Burrow.mc.field_1724.method_23321(), false));
                    break;
                }
                case 8: {
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1.1001, Burrow.mc.field_1724.method_23321(), false));
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1.0605, Burrow.mc.field_1724.method_23321(), false));
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1.0802, Burrow.mc.field_1724.method_23321(), false));
                    mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318() + 1.1127, Burrow.mc.field_1724.method_23321(), false));
                }
            }
            if (this.disable.getValue()) {
                this.disable();
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    private boolean fakeMove() {
        void var6_11;
        double[] offsets = new double[]{1.0, 0.0, -1.0};
        ArrayList<BlockPosX> offList = new ArrayList<BlockPosX>();
        class_2338 playerPos = Burrow.mc.field_1724.method_24515();
        Object object = offsets;
        int n = ((double[])object).length;
        boolean bl = false;
        while (var6_11 < n) {
            double x = object[var6_11];
            for (double z : offsets) {
                offList.add(new BlockPosX(Burrow.mc.field_1724.method_23317() + x, Burrow.mc.field_1724.method_23318(), Burrow.mc.field_1724.method_23321() + z));
            }
            ++var6_11;
        }
        object = offList.iterator();
        while (object.hasNext()) {
            class_2338 offPos = (class_2338)object.next();
            if (!this.checkSelf(offPos) || BlockUtil.canReplace(offPos) || this.headFill.getValue() && BlockUtil.canReplace(offPos.method_10084())) continue;
            this.gotoPos(offPos);
            return true;
        }
        ArrayList<class_2338> pos = new ArrayList<class_2338>();
        for (class_2338 class_23382 : offList) {
            if (playerPos.equals((Object)class_23382) || !this.checkSelf(class_23382) || !this.canMove(class_23382)) continue;
            pos.add(class_23382);
        }
        if (!pos.isEmpty()) {
            double dis = 10.0;
            class_2338 target = null;
            for (class_2338 p : pos) {
                double distance = Burrow.mc.field_1724.method_19538().method_1022(p.method_46558().method_1031(0.0, -0.5, 0.0));
                if (!(distance < dis) && target != null) continue;
                target = p;
                dis = distance;
            }
            this.gotoPos(target);
            return true;
        }
        for (class_2338 class_23383 : offList) {
            if (playerPos.equals((Object)class_23383) || !this.checkSelf(class_23383)) continue;
            pos.add(class_23383);
        }
        if (!pos.isEmpty()) {
            double dis = 10.0;
            class_2338 target = null;
            for (class_2338 px : pos) {
                double distance = Burrow.mc.field_1724.method_19538().method_1022(px.method_46558().method_1031(0.0, -0.5, 0.0));
                if (!(distance < dis) && target != null) continue;
                target = px;
                dis = distance;
            }
            this.gotoPos(target);
            return true;
        }
        if (!this.center.getValue()) {
            return false;
        }
        offList.clear();
        offList.add(new BlockPosX(Burrow.mc.field_1724.method_23317() + 1.0, Burrow.mc.field_1724.method_23318(), Burrow.mc.field_1724.method_23321()));
        offList.add(new BlockPosX(Burrow.mc.field_1724.method_23317() - 1.0, Burrow.mc.field_1724.method_23318(), Burrow.mc.field_1724.method_23321()));
        offList.add(new BlockPosX(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318(), Burrow.mc.field_1724.method_23321() - 1.0));
        offList.add(new BlockPosX(Burrow.mc.field_1724.method_23317(), Burrow.mc.field_1724.method_23318(), Burrow.mc.field_1724.method_23321() + 1.0));
        for (class_2338 class_23384 : offList) {
            if (!this.canMove(class_23384)) continue;
            this.gotoPos(class_23384);
            return true;
        }
        if (!this.wait.getValue() && this.disable.getValue()) {
            this.disable();
        }
        return false;
    }

    private void placeBlock(class_2338 pos, boolean rotate) {
        if (this.canPlace(pos) && !this.placePos.contains(pos) && this.progress < this.blocksPer.getValueInt()) {
            class_2350 side;
            this.placePos.add(pos);
            if (BlockUtil.allowAirPlace()) {
                ++this.progress;
                BlockUtil.placedPos.add(pos);
                if (this.sound.getValue()) {
                    Burrow.mc.field_1687.method_8396((class_1657)Burrow.mc.field_1724, pos, class_3417.field_14574, class_3419.field_15245, 1.0f, 0.8f);
                }
                this.clickBlock(pos, class_2350.field_11033, rotate, class_1268.field_5808, this.packetPlace.getValue());
            }
            if ((side = BlockUtil.getPlaceSide(pos)) == null) {
                return;
            }
            ++this.progress;
            BlockUtil.placedPos.add(pos);
            if (this.sound.getValue()) {
                Burrow.mc.field_1687.method_8396((class_1657)Burrow.mc.field_1724, pos, class_3417.field_14574, class_3419.field_15245, 1.0f, 0.8f);
            }
            this.clickBlock(pos.method_10093(side), side.method_10153(), rotate, class_1268.field_5808, this.packetPlace.getValue());
        }
    }

    public void clickBlock(class_2338 pos, class_2350 side, boolean rotate, class_1268 hand, boolean packet) {
        class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
        if (rotate) {
            float[] angle = RotationManager.getRotation(this.currentPos.method_1031(0.0, (double)Burrow.mc.field_1724.method_18381(Burrow.mc.field_1724.method_18376()), 0.0), directionVec);
            Alien.ROTATION.snapAt(angle[0], angle[1]);
        }
        EntityUtil.swingHand(hand, AntiCheat.INSTANCE.interactSwing.getValue());
        class_3965 result = new class_3965(directionVec, side, pos, false);
        if (packet) {
            Module.sendSequencedPacket(id -> new class_2885(hand, result, id));
        } else {
            Burrow.mc.field_1761.method_2896(Burrow.mc.field_1724, hand, result);
        }
        if (rotate) {
            Alien.ROTATION.snapBack();
        }
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, Burrow.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private void gotoPos(class_2338 offPos) {
        double targetX = (double)offPos.method_10263() + 0.5;
        double targetZ = (double)offPos.method_10260() + 0.5;
        double x = Burrow.mc.field_1724.method_23317();
        double z = Burrow.mc.field_1724.method_23321();
        double y = Burrow.mc.field_1724.method_23318() + this.yOffset.getValue();
        double xDiff = Math.abs(x - targetX);
        double zDiff = Math.abs(z - targetZ);
        double moveDis = this.preCorrect.getValue();
        if (moveDis > 0.0) {
            if (xDiff >= moveDis) {
                x = x > targetX ? (x -= moveDis) : (x += moveDis);
                Burrow.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y, z, false));
            }
            if (zDiff >= moveDis) {
                z = z > targetZ ? (z -= moveDis) : (z += moveDis);
                Burrow.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y, z, false));
            }
        }
        xDiff = Math.abs(x - targetX);
        zDiff = Math.abs(z - targetZ);
        moveDis = this.moveDis.getValue();
        if (moveDis > 0.0) {
            while (xDiff > moveDis) {
                x = x > targetX ? (x -= moveDis) : (x += moveDis);
                Burrow.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y, z, false));
                xDiff = Math.abs(x - targetX);
            }
            while (zDiff > moveDis) {
                z = z > targetZ ? (z -= moveDis) : (z += moveDis);
                Burrow.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y, z, false));
                zDiff = Math.abs(z - targetZ);
            }
        }
        if ((moveDis = this.moveDis2.getValue()) > 0.0) {
            while (xDiff > moveDis) {
                x = x > targetX ? (x -= moveDis) : (x += moveDis);
                Burrow.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y, z, false));
                xDiff = Math.abs(x - targetX);
            }
            while (zDiff > moveDis) {
                z = z > targetZ ? (z -= moveDis) : (z += moveDis);
                Burrow.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y, z, false));
                zDiff = Math.abs(z - targetZ);
            }
        }
        if ((moveDis = this.moveCorrect2.getValue()) > 0.0) {
            if (xDiff >= moveDis) {
                x = x > targetX ? (x -= moveDis) : (x += moveDis);
                Burrow.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y, z, false));
            }
            if (zDiff >= moveDis) {
                z = z > targetZ ? (z -= moveDis) : (z += moveDis);
                Burrow.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(x, y, z, false));
            }
        }
        this.currentPos = new class_243(x, y, z);
    }

    private boolean canMove(class_2338 pos) {
        return Burrow.mc.field_1687.method_22347(pos) && Burrow.mc.field_1687.method_22347(pos.method_10084());
    }

    private boolean canPlace(class_2338 pos) {
        if (this.noSelfPos.getValue() && pos.equals((Object)EntityUtil.getPlayerPos(true))) {
            return false;
        }
        if (!BlockUtil.allowAirPlace() && BlockUtil.getPlaceSide(pos) == null) {
            return false;
        }
        if (!BlockUtil.canReplace(pos)) {
            return false;
        }
        return this.detectMine.getValue() && Alien.BREAK.isMining(pos) ? false : !this.hasEntity(pos);
    }

    private boolean hasEntity(class_2338 pos) {
        for (class_1297 entity : BlockUtil.getEntities(new class_238(pos))) {
            if (entity == Burrow.mc.field_1724 || !entity.method_5805() || entity instanceof class_1542 || entity instanceof class_1303 || entity instanceof class_1683 || entity instanceof class_1667 || entity instanceof class_1511 && this.breakCrystal.getValue()) continue;
            return true;
        }
        return false;
    }

    private boolean checkSelf(class_2338 pos) {
        return Burrow.mc.field_1724.method_5829().method_994(new class_238(pos));
    }

    private boolean trapped(class_2338 pos) {
        return (BlockUtil.canCollide((class_1297)Burrow.mc.field_1724, new class_238(pos)) || BlockUtil.getBlock(pos) == class_2246.field_10343) && this.checkSelf(pos.method_10087(2));
    }

    private int getBlock() {
        if (this.inventory.getValue()) {
            return InventoryUtil.findBlockInventorySlot(class_2246.field_10540) == -1 && this.enderChest.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10443) : InventoryUtil.findBlockInventorySlot(class_2246.field_10540);
        }
        return InventoryUtil.findBlock(class_2246.field_10540) == -1 && this.enderChest.getValue() ? InventoryUtil.findBlock(class_2246.field_10443) : InventoryUtil.findBlock(class_2246.field_10540);
    }

    private static enum RotateMode {
        Bypass,
        Normal,
        None;

    }

    private static enum LagBackMode {
        Smart,
        Invalid,
        TrollHack,
        ToVoid,
        ToVoid2,
        Normal,
        Rotation,
        Fly,
        Glide;

    }
}

