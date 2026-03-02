/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1481
 *  net.minecraft.class_1501
 *  net.minecraft.class_1533
 *  net.minecraft.class_1542
 *  net.minecraft.class_1646
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1707
 *  net.minecraft.class_1713
 *  net.minecraft.class_1714
 *  net.minecraft.class_1724
 *  net.minecraft.class_1728
 *  net.minecraft.class_1733
 *  net.minecraft.class_1735
 *  net.minecraft.class_1747
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1821
 *  net.minecraft.class_1829
 *  net.minecraft.class_1914
 *  net.minecraft.class_1922
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_243
 *  net.minecraft.class_2480
 *  net.minecraft.class_2482
 *  net.minecraft.class_2596
 *  net.minecraft.class_2627
 *  net.minecraft.class_2813
 *  net.minecraft.class_2815
 *  net.minecraft.class_2824
 *  net.minecraft.class_2828
 *  net.minecraft.class_2846
 *  net.minecraft.class_2846$class_2847
 *  net.minecraft.class_2851
 *  net.minecraft.class_2863
 *  net.minecraft.class_2886
 *  net.minecraft.class_3532
 *  net.minecraft.class_3545
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  net.minecraft.class_490
 *  net.minecraft.class_516
 *  net.minecraft.class_7204
 *  net.minecraft.class_7225$class_7874
 *  net.minecraft.class_7439
 *  net.minecraft.class_8786
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.path.BaritoneUtil;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.asm.accessors.ILivingEntity;
import dev.luminous.core.impl.RotationManager;
import dev.luminous.mod.gui.windows.WindowsScreen;
import dev.luminous.mod.gui.windows.impl.ItemSelectWindow;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.combat.Aura;
import dev.luminous.mod.modules.impl.misc.ShulkerViewer;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.impl.render.PlaceRender;
import dev.luminous.mod.modules.settings.enums.SwingSide;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1481;
import net.minecraft.class_1501;
import net.minecraft.class_1533;
import net.minecraft.class_1542;
import net.minecraft.class_1646;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1714;
import net.minecraft.class_1724;
import net.minecraft.class_1728;
import net.minecraft.class_1733;
import net.minecraft.class_1735;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1821;
import net.minecraft.class_1829;
import net.minecraft.class_1914;
import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2482;
import net.minecraft.class_2596;
import net.minecraft.class_2627;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2824;
import net.minecraft.class_2828;
import net.minecraft.class_2846;
import net.minecraft.class_2851;
import net.minecraft.class_2863;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import net.minecraft.class_3545;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_490;
import net.minecraft.class_516;
import net.minecraft.class_7204;
import net.minecraft.class_7225;
import net.minecraft.class_7439;
import net.minecraft.class_8786;

public class Bot
extends Module {
    public static Bot INSTANCE;
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.AutoTrade));
    private final BooleanSetting autoEat = this.add(new BooleanSetting("AutoEat", true).setParent());
    private final SliderSetting hunger = this.add(new SliderSetting("Hunger", 10.0, 0.0, 20.0, 1.0, this.autoEat::isOpen));
    private final SliderSetting health = this.add(new SliderSetting("Health", 20.0, 0.0, 36.0, 0.1, this.autoEat::isOpen));
    private final BooleanSetting anyFood = this.add(new BooleanSetting("AnyFood", false, this.autoEat::isOpen));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 5, 0, 100, () -> this.mode.is(Mode.TridentDupe)));
    private final BooleanSetting dropTridents = this.add(new BooleanSetting("DropTridents", true, () -> this.mode.is(Mode.TridentDupe)));
    private final BooleanSetting durabilityManagement = this.add(new BooleanSetting("DurabilityManagement", true, () -> this.mode.is(Mode.TridentDupe)));
    public final BooleanSetting edit = this.add(new BooleanSetting("Edit", false, () -> this.mode.is(Mode.AutoTrade)).injectTask(this::openAutoTradeEdit));
    public final SliderSetting repeatSetting = this.add(new SliderSetting("Repeat", 2.0, 1.0, 15.0, 1.0, () -> this.mode.is(Mode.AutoTrade)));
    public final BooleanSetting autoCloseSetting = this.add(new BooleanSetting("AutoClose", true, () -> this.mode.is(Mode.AutoTrade)));
    public final BooleanSetting timeoutCloseSetting = this.add(new BooleanSetting("TimeoutClose", true, () -> this.mode.is(Mode.AutoTrade)));
    public final SliderSetting timeOutSetting = this.add(new SliderSetting("Timeout", 1.0, 0.0, 15.0, 0.1, () -> this.mode.is(Mode.AutoTrade)));
    public final BooleanSetting autoOpenSetting = this.add(new BooleanSetting("AutoOpen", true, () -> this.mode.is(Mode.AutoTrade)));
    private final SliderSetting range = this.add(new SliderSetting("Range", 4.0, 0.0, 8.0, 0.1, () -> this.mode.is(Mode.SlabPlacer) || this.mode.is(Mode.AutoTrade)));
    private final BooleanSetting inventory = this.add(new BooleanSetting("Inventory", true, () -> this.mode.is(Mode.NPlusOneDupe)));
    private final BooleanSetting ai = this.add(new BooleanSetting("AI", true, () -> this.mode.is(Mode.SandMiner)));
    private final BooleanSetting nuker = this.add(new BooleanSetting("Nuker", true, () -> this.mode.is(Mode.SandMiner)));
    private final BooleanSetting redSand = this.add(new BooleanSetting("RedSand", false, () -> this.mode.is(Mode.SandMiner)));
    private final SliderSetting breaks = this.add(new SliderSetting("Breaks", 10, 0, 20, () -> this.mode.is(Mode.SandMiner)));
    private final SliderSetting maxTime = this.add(new SliderSetting("MaxTime", 60, 0, 100, () -> this.mode.is(Mode.Ominous)));
    public final SliderSetting placeDelay = this.add(new SliderSetting("PlaceDelay", 50, 0, 500, () -> this.mode.is(Mode.SlabPlacer)));
    private final SliderSetting blocksPer = this.add(new SliderSetting("BlocksPer", 1, 1, 8, () -> this.mode.is(Mode.SlabPlacer)));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, () -> this.mode.is(Mode.SlabPlacer)));
    private final StringSetting name = this.add(new StringSetting("Name", "KizuatoResu", () -> this.mode.is(Mode.ScoreFarmer)));
    private final BooleanSetting getScore = this.add(new BooleanSetting("GetScore", false, () -> this.mode.is(Mode.ScoreFarmer)));
    private final Timer commandTimer = new Timer();
    private final Timer duelItemTimer = new Timer();
    final List<class_2338> emptyBox = new ArrayList<class_2338>();
    final List<class_1481> inLove = new ArrayList<class_1481>();
    final Timer timeOut = new Timer();
    final Timer closeScreen = new Timer();
    final Timer openTimeOut = new Timer();
    final Timer putTimer = new Timer();
    final Timer ominousTimer = new Timer();
    Stage stage = Stage.Open;
    class_2338 boxPos;
    boolean closeToBox;
    boolean putIn;
    class_1501 llama;
    boolean storageSand = false;
    boolean endEat = false;
    final Timer craftTimer = new Timer();
    final Timer screenTimeout = new Timer();
    int lastSlot = -1;
    int tick = 0;
    private final List<class_1646> tradedVillager = new ArrayList<class_1646>();
    private final Timer timeoutTimer = new Timer();
    int placeProgress = 0;
    private final Timer slabPlacerDelay = new Timer();
    private boolean cancel = true;
    private final List<class_3545<Long, Runnable>> scheduledTasks = new ArrayList<class_3545<Long, Runnable>>();
    private final List<class_3545<Long, Runnable>> scheduledTasks2 = new ArrayList<class_3545<Long, Runnable>>();

    public Bot() {
        super("Bot", Module.Category.Misc);
        this.setChinese("\u673a\u5668\u4eba");
        INSTANCE = this;
    }

    private void openAutoTradeEdit() {
        this.edit.setValueWithoutTask(false);
        if (!Bot.nullCheck()) {
            mc.method_1507((class_437)new WindowsScreen(new ItemSelectWindow(Alien.TRADE)));
        }
    }

    private void placeBlock(class_2338 pos) {
        int block = this.getBlock();
        if (block != -1) {
            int oldSlot = Bot.mc.field_1724.method_31548().field_7545;
            if (BlockUtil.canPlace(pos)) {
                if (BlockUtil.allowAirPlace()) {
                    this.doSwap(block);
                    BlockUtil.placedPos.add(pos);
                    BlockUtil.airPlace(pos, this.rotate.getValue());
                    if (this.inventory.getValue()) {
                        this.doSwap(block);
                        EntityUtil.syncInventory();
                    } else {
                        this.doSwap(oldSlot);
                    }
                    return;
                }
                class_2350 side = BlockUtil.getPlaceSide(pos);
                if (side == null) {
                    return;
                }
                this.doSwap(block);
                BlockUtil.placedPos.add(pos);
                BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), this.rotate.getValue());
                if (this.inventory.getValue()) {
                    this.doSwap(block);
                    EntityUtil.syncInventory();
                } else {
                    this.doSwap(oldSlot);
                }
            }
        }
    }

    private void tryPlaceBlock(class_2338 pos) {
        class_2350 side;
        int block;
        if (pos != null && (double)this.placeProgress < this.blocksPer.getValue() && (block = this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(class_2482.class) : InventoryUtil.findClass(class_2482.class)) != -1 && (side = BlockUtil.getPlaceSide(pos)) != null) {
            class_243 directionVec = new class_243((double)pos.method_10263() + 0.5 + (double)side.method_10163().method_10263() * 0.5, (double)pos.method_10264() + 0.5 + (double)side.method_10163().method_10264() * 0.5, (double)pos.method_10260() + 0.5 + (double)side.method_10163().method_10260() * 0.5);
            if (BlockUtil.canPlace(pos, 6.0, true)) {
                if (this.rotate.getValue()) {
                    Alien.ROTATION.lookAt(directionVec);
                }
                if (!BlockUtil.hasEntity(pos, false)) {
                    int old = Bot.mc.field_1724.method_31548().field_7545;
                    this.doSwap(block);
                    BlockUtil.placedPos.add(pos);
                    if (BlockUtil.allowAirPlace()) {
                        BlockUtil.airPlace(pos, false, class_1268.field_5808, true);
                    } else {
                        BlockUtil.clickBlock(pos.method_10093(side), side.method_10153(), false, class_1268.field_5808);
                    }
                    if (this.inventory.getValue()) {
                        this.doSwap(block);
                        EntityUtil.syncInventory();
                    } else {
                        this.doSwap(old);
                    }
                    if (this.rotate.getValue()) {
                        Alien.ROTATION.snapBack();
                    }
                    ++this.placeProgress;
                    this.slabPlacerDelay.reset();
                }
            }
        }
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, Bot.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getBlock() {
        return this.inventory.getValue() ? InventoryUtil.findClassInventorySlot(class_2480.class) : InventoryUtil.findClass(class_2480.class);
    }

    @Override
    public String getInfo() {
        return this.mode.is(Mode.XinDupe) ? "Stage:" + this.stage.name() + ", Riding:" + Bot.mc.field_1724.method_5765() : this.mode.getValue().name();
    }

    @Override
    public boolean onEnable() {
        this.emptyBox.clear();
        this.storageSand = false;
        this.stage = Stage.Summon;
        this.boxPos = null;
        this.closeToBox = false;
        this.llama = null;
        this.putIn = false;
        this.tick = 0;
        this.scheduledTasks.clear();
        this.scheduledTasks2.clear();
        if (this.mode.is(Mode.TridentDupe)) {
            this.tridentDupe();
        }
        return false;
    }

    @EventListener(priority=201)
    private void onSendPacket(PacketEvent.Send event) {
        if (this.mode.is(Mode.TridentDupe)) {
            if (!this.cancel) {
                return;
            }
            if (event.getPacket() instanceof class_2828 || event.getPacket() instanceof class_2815) {
                return;
            }
            if (!(event.getPacket() instanceof class_2813) && !(event.getPacket() instanceof class_2846)) {
                return;
            }
            event.cancel();
        }
    }

    private void tridentDupe() {
        int delayInt = this.delay.getValueInt() * 100;
        int lowestHotbarSlot = 0;
        int lowestHotbarDamage = 1000;
        for (int i = 0; i < 9; ++i) {
            int currentHotbarDamage;
            if (Bot.mc.field_1724.method_31548().method_5438(i).method_7909() != class_1802.field_8547 || lowestHotbarDamage <= (currentHotbarDamage = Bot.mc.field_1724.method_31548().method_5438(i).method_7919())) continue;
            lowestHotbarSlot = i;
            lowestHotbarDamage = currentHotbarDamage;
        }
        Bot.mc.field_1761.method_2919((class_1657)Bot.mc.field_1724, class_1268.field_5808);
        this.cancel = true;
        int finalLowestHotbarSlot = lowestHotbarSlot;
        this.scheduleTask(() -> {
            this.cancel = false;
            if (this.durabilityManagement.getValue() && finalLowestHotbarSlot != 0) {
                Bot.mc.field_1761.method_2906(Bot.mc.field_1724.field_7512.field_7763, 44, 0, class_1713.field_7791, (class_1657)Bot.mc.field_1724);
                if (this.dropTridents.getValue()) {
                    Bot.mc.field_1761.method_2906(Bot.mc.field_1724.field_7512.field_7763, 44, 0, class_1713.field_7795, (class_1657)Bot.mc.field_1724);
                }
                Bot.mc.field_1761.method_2906(Bot.mc.field_1724.field_7512.field_7763, 36 + finalLowestHotbarSlot, 0, class_1713.field_7791, (class_1657)Bot.mc.field_1724);
            }
            Bot.mc.field_1761.method_2906(Bot.mc.field_1724.field_7512.field_7763, 3, 0, class_1713.field_7791, (class_1657)Bot.mc.field_1724);
            class_2846 packet2 = new class_2846(class_2846.class_2847.field_12974, class_2338.field_10980, class_2350.field_11033, 0);
            mc.method_1562().method_52787((class_2596)packet2);
            if (this.dropTridents.getValue()) {
                Bot.mc.field_1761.method_2906(Bot.mc.field_1724.field_7512.field_7763, 44, 0, class_1713.field_7795, (class_1657)Bot.mc.field_1724);
            }
            this.cancel = true;
            this.scheduleTask2(this::tridentDupe, delayInt);
        }, delayInt);
    }

    public void scheduleTask(Runnable task, long delayMillis) {
        long executeTime = System.currentTimeMillis() + delayMillis;
        this.scheduledTasks.add((class_3545<Long, Runnable>)new class_3545((Object)executeTime, (Object)task));
    }

    public void scheduleTask2(Runnable task, long delayMillis) {
        long executeTime = System.currentTimeMillis() + delayMillis;
        this.scheduledTasks2.add((class_3545<Long, Runnable>)new class_3545((Object)executeTime, (Object)task));
    }

    @Override
    public void onDisable() {
        BaritoneUtil.cancelEverything();
    }

    /*
     * Could not resolve type clashes
     * Unable to fully structure code
     */
    @EventListener
    public void onUpdate(UpdateEvent event) {
        block197: {
            block198: {
                block196: {
                    if (!Bot.nullCheck()) break block196;
                    this.emptyBox.clear();
                    this.stage = Stage.Summon;
                    this.boxPos = null;
                    this.closeToBox = false;
                    this.llama = null;
                    this.putIn = false;
                    break block197;
                }
                if (this.autoEat.getValue() && (Bot.mc.field_1724.method_6032() + Bot.mc.field_1724.method_6067() < this.health.getValueFloat() || Bot.mc.field_1724.method_7344().method_7586() < this.hunger.getValueInt())) break block198;
                if (this.endEat) {
                    this.endEat = false;
                    Bot.mc.field_1690.field_1904.method_23481(false);
                }
                switch (this.mode.getValue().ordinal()) {
                    case 0: {
                        var3_2 = Bot.mc.field_1724.field_7512;
                        if (var3_2 instanceof class_1728) {
                            handler = (class_1728)var3_2;
                            if (this.timeoutCloseSetting.getValue() && this.timeoutTimer.passedS(this.timeOutSetting.getValue())) {
                                Bot.mc.method_1562().method_52787((class_2596)new class_2815(Bot.mc.field_1724.field_7512.field_7763));
                                Bot.mc.field_1755.method_25419();
                                return;
                            }
                            i = 0;
                            list = handler.method_17438();
                            block20: for (size = 0; size < list.size(); ++size) {
                                if ((double)i >= this.repeatSetting.getValue()) {
                                    return;
                                }
                                tradeOffer = (class_1914)list.get(size);
                                if (tradeOffer.method_8255() || !Alien.TRADE.inWhitelist(tradeOffer.method_8250().method_7909().method_7876())) continue;
                                while ((double)i < this.repeatSetting.getValue()) {
                                    if (!tradeOffer.method_19272().method_7960()) {
                                        count = InventoryUtil.getItemCount(tradeOffer.method_19272().method_7909());
                                        if (handler.method_7611(0).method_7677().method_7909() == tradeOffer.method_19272().method_7909()) {
                                            count += handler.method_7611(0).method_7677().method_7947();
                                        }
                                        if (count < tradeOffer.method_19272().method_7947()) continue block20;
                                    }
                                    if (!tradeOffer.method_8247().method_7960()) {
                                        countx = InventoryUtil.getItemCount(tradeOffer.method_8247().method_7909());
                                        if (handler.method_7611(1).method_7677().method_7909() == tradeOffer.method_8247().method_7909()) {
                                            countx += handler.method_7611(1).method_7677().method_7947();
                                        }
                                        if (countx < tradeOffer.method_8247().method_7947()) continue block20;
                                    }
                                    Bot.mc.method_1562().method_52787((class_2596)new class_2863(size));
                                    Bot.mc.field_1761.method_2906(Bot.mc.field_1724.field_7512.field_7763, 2, 1, class_1713.field_7794, (class_1657)Bot.mc.field_1724);
                                    ++i;
                                }
                            }
                            if (this.autoCloseSetting.getValue() && (double)i < this.repeatSetting.getValue()) {
                                Bot.mc.method_1562().method_52787((class_2596)new class_2815(Bot.mc.field_1724.field_7512.field_7763));
                                Bot.mc.field_1755.method_25419();
                                break;
                            }
                        } else {
                            this.timeoutTimer.reset();
                            if (!this.autoOpenSetting.getValue()) break;
                            for (class_1297 entityxxxx : Alien.THREAD.getEntities()) {
                                if (!(entityxxxx instanceof class_1646)) continue;
                                villager = (class_1646)entityxxxx;
                                if ((double)Bot.mc.field_1724.method_5739((class_1297)villager) <= this.range.getValue()) {
                                    if (this.tradedVillager.contains(villager)) continue;
                                    this.tradedVillager.add(villager);
                                    if (this.rotate.getValue()) {
                                        Alien.ROTATION.snapAt(villager.method_33571());
                                    }
                                    Bot.mc.method_1562().method_52787((class_2596)class_2824.method_34207((class_1297)villager, (boolean)Bot.mc.field_1724.method_5715(), (class_1268)class_1268.field_5808));
                                    if (this.rotate.getValue()) {
                                        Alien.ROTATION.snapBack();
                                    }
                                    return;
                                }
                                this.tradedVillager.remove(villager);
                            }
                        }
                        break block197;
                    }
                    case 1: {
                        ++this.tick;
                        shulkerx = InventoryUtil.findClass(class_2480.class);
                        if (shulkerx != -1) {
                            for (class_1297 entityxxx : Alien.THREAD.getEntities()) {
                                if (!(entityxxx instanceof class_1533)) continue;
                                itemFrameEntity = (class_1533)entityxxx;
                                if (entityxxx.method_5739((class_1297)Bot.mc.field_1724) > 3.0f) continue;
                                InventoryUtil.switchToSlot(shulkerx);
                                Bot.mc.method_1562().method_52787((class_2596)class_2824.method_34207((class_1297)itemFrameEntity, (boolean)false, (class_1268)class_1268.field_5808));
                                if (this.tick >= 2) {
                                    Bot.mc.method_1562().method_52787((class_2596)class_2824.method_34206((class_1297)itemFrameEntity, (boolean)false));
                                    Bot.mc.field_1724.method_6104(class_1268.field_5808);
                                    this.tick = 0;
                                }
                                return;
                            }
                        }
                        this.tick = 0;
                        break;
                    }
                    case 2: {
                        if (this.llama != null && (this.llama.method_29504() || this.llama.method_5739((class_1297)Bot.mc.field_1724) > 20.0f)) {
                            this.llama = null;
                        }
                        chestSlot = InventoryUtil.findBlockInventorySlot(class_2246.field_10034);
                        swordSlot = InventoryUtil.findClass(class_1829.class);
                        if (chestSlot == -1) {
                            this.emptyBox.clear();
                            this.stage = Stage.Open;
                            this.boxPos = null;
                            this.closeToBox = false;
                            this.llama = null;
                            this.putIn = false;
                            return;
                        }
                        shulkers = 0;
                        for (Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                            var8_36 = entry.getValue().method_7909();
                            if (!(var8_36 instanceof class_1747) || !((blockItem = (class_1747)var8_36).method_7711() instanceof class_2480)) continue;
                            ++shulkers;
                        }
                        if (shulkers > 18) {
                            if (Bot.mc.field_1755 != null) {
                                Bot.mc.field_1755.method_25419();
                            }
                            for (slot1x = 9; slot1x < 36; ++slot1x) {
                                stack = Bot.mc.field_1724.method_31548().method_5438(slot1x);
                                if (stack.method_7960() || !((var8_36 = stack.method_7909()) instanceof class_1747) || !((blockItem = (class_1747)var8_36).method_7711() instanceof class_2480)) continue;
                                Bot.mc.field_1761.method_2906(Bot.mc.field_1724.field_7498.field_7763, slot1x, 1, class_1713.field_7795, (class_1657)Bot.mc.field_1724);
                                if (--shulkers > 18) continue;
                                return;
                            }
                            return;
                        }
                        if (this.closeToBox && this.boxPos != null) {
                            this.closeTo(this.boxPos);
                        }
                        if (!this.openTimeOut.passed(100L)) {
                            return;
                        }
                        switch (this.stage.ordinal()) {
                            case 0: {
                                if (!this.closeScreen.passed(250L)) {
                                    if (Bot.mc.field_1755 != null) {
                                        Bot.mc.field_1755.method_25419();
                                    }
                                    return;
                                }
                                if (Bot.mc.field_1724.method_5765()) {
                                    Bot.mc.method_1562().method_52787((class_2596)new class_2851(0.0f, 0.0f, false, true));
                                    return;
                                }
                                if (this.boxPos == null || this.emptyBox.contains(this.boxPos)) {
                                    for (class_1703 posxxxx : BlockUtil.getSphere(3.0f)) {
                                        if (this.emptyBox.contains(posxxxx) || !(Bot.mc.field_1687.method_8321((class_2338)posxxxx) instanceof class_2627) || BlockUtil.getClickSideStrict((class_2338)posxxxx) == null) continue;
                                        this.closeToBox = false;
                                        this.boxPos = posxxxx;
                                        break;
                                    }
                                }
                                if (this.boxPos != null && !this.emptyBox.contains(this.boxPos)) {
                                    if (Bot.mc.field_1724.method_33571().method_1022(this.boxPos.method_46558()) < 4.0) {
                                        if (this.openTimeOut.passedS(1.0)) {
                                            this.closeToBox = false;
                                            this.openTimeOut.reset();
                                            BlockUtil.clickBlock(this.boxPos, BlockUtil.getClickSide(this.boxPos), true);
                                            this.stage = Stage.Take;
                                            return;
                                        }
                                    } else {
                                        this.closeToBox = true;
                                    }
                                    return;
                                }
                                return;
                            }
                            case 1: {
                                if (Bot.mc.field_1724.method_5765()) {
                                    Bot.mc.method_1562().method_52787((class_2596)new class_2851(0.0f, 0.0f, false, true));
                                    return;
                                }
                                if (this.boxPos == null || this.emptyBox.contains(this.boxPos)) {
                                    this.closeScreen.reset();
                                    this.stage = Stage.Open;
                                    return;
                                }
                                posxxxx = Bot.mc.field_1724.field_7512;
                                if (posxxxx instanceof class_1733) {
                                    shulkerx = (class_1733)posxxxx;
                                    egg = false;
                                    hay = false;
                                    for (class_1735 slotxxx : shulkerx.field_7761) {
                                        if (slotxxx.field_7874 >= 27 || slotxxx.method_7677().method_7960()) continue;
                                        if (slotxxx.method_7677().method_7909() == class_1802.field_8803) {
                                            egg = true;
                                        }
                                        if (slotxxx.method_7677().method_7909() != class_2246.field_10359.method_8389()) continue;
                                        hay = true;
                                    }
                                    if (egg && hay) {
                                        eggs = 0;
                                        hays = 0;
                                        for (Map.Entry<Integer, class_1799> entryx : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                                            if (entryx.getValue().method_7909() == class_1802.field_8803) {
                                                ++eggs;
                                            }
                                            if (entryx.getValue().method_7909() != class_2246.field_10359.method_8389()) continue;
                                            ++hays;
                                        }
                                        for (class_1735 slotxxxx : shulkerx.field_7761) {
                                            if (slotxxxx.method_7677().method_7960()) continue;
                                            if (slotxxxx.field_7874 < 27) {
                                                if (slotxxxx.method_7677().method_7909() == class_1802.field_8803 && eggs < 2) {
                                                    ++eggs;
                                                    Bot.mc.field_1761.method_2906(shulkerx.field_7763, slotxxxx.field_7874, 0, class_1713.field_7794, (class_1657)Bot.mc.field_1724);
                                                }
                                                if (slotxxxx.method_7677().method_7909() != class_2246.field_10359.method_8389() || hays >= 2) continue;
                                                ++hays;
                                                Bot.mc.field_1761.method_2906(shulkerx.field_7763, slotxxxx.field_7874, 0, class_1713.field_7794, (class_1657)Bot.mc.field_1724);
                                                continue;
                                            }
                                            if (slotxxxx.method_7677().method_7909() != class_1802.field_8745) continue;
                                            Bot.mc.field_1761.method_2906(shulkerx.field_7763, slotxxxx.field_7874, 0, class_1713.field_7794, (class_1657)Bot.mc.field_1724);
                                        }
                                        if (hays < 1 || eggs < 1) {
                                            this.emptyBox.add(this.boxPos);
                                        }
                                        if (Bot.mc.field_1755 != null) {
                                            Bot.mc.field_1755.method_25419();
                                        }
                                        this.stage = Stage.Summon;
                                    } else {
                                        this.closeScreen.reset();
                                        this.emptyBox.add(this.boxPos);
                                        this.stage = Stage.Open;
                                    }
                                    return;
                                }
                                if (this.openTimeOut.passedS(1.0)) {
                                    this.closeScreen.reset();
                                    this.stage = Stage.Open;
                                    return;
                                }
                                return;
                            }
                            case 2: {
                                eggs = 0;
                                hays = 0;
                                for (Map.Entry<Integer, class_1799> entryx : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                                    if (entryx.getValue().method_7909() == class_1802.field_8803) {
                                        ++eggs;
                                    }
                                    if (entryx.getValue().method_7909() != class_2246.field_10359.method_8389()) continue;
                                    ++hays;
                                }
                                if (eggs <= 1 || hays <= 1) {
                                    this.closeScreen.reset();
                                    this.stage = Stage.Open;
                                    return;
                                }
                                for (class_1297 entityxxxxx : Alien.THREAD.getEntities()) {
                                    if (!(entityxxxxx instanceof class_1501)) continue;
                                    llamaEntity = (class_1501)entityxxxxx;
                                    if (!(Bot.mc.field_1724.method_33571().method_1022(entityxxxxx.method_19538()) < 10.0) || !entityxxxxx.method_5805()) continue;
                                    if (Bot.mc.field_1724.method_33571().method_1022(entityxxxxx.method_19538()) < 5.0) {
                                        this.llama = llamaEntity;
                                        this.stage = Stage.Tame;
                                    } else {
                                        this.closeTo(entityxxxxx.method_24515());
                                    }
                                    return;
                                }
                                if (Bot.mc.field_1755 != null) {
                                    Bot.mc.field_1755.method_25419();
                                }
                                slotxxx = InventoryUtil.findItemInventorySlot(class_1802.field_8803);
                                InventoryUtil.inventorySwap(slotxxx, Bot.mc.field_1724.method_31548().field_7545);
                                Alien.ROTATION.snapAt(Alien.ROTATION.getLastYaw(), 89.0f);
                                Bot.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                                InventoryUtil.inventorySwap(slotxxx, Bot.mc.field_1724.method_31548().field_7545);
                                return;
                            }
                            case 3: {
                                if (this.llama == null || this.llama.method_29504()) {
                                    this.stage = Stage.Summon;
                                    return;
                                }
                                eggs = 0;
                                hays = 0;
                                for (Map.Entry<Integer, class_1799> entryx : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
                                    if (entryx.getValue().method_7909() == class_1802.field_8803) {
                                        ++eggs;
                                    }
                                    if (entryx.getValue().method_7909() != class_2246.field_10359.method_8389()) continue;
                                    ++hays;
                                }
                                if (eggs <= 1 || hays <= 1) {
                                    this.closeScreen.reset();
                                    this.stage = Stage.Open;
                                    return;
                                }
                                if (Bot.mc.field_1724.method_5765()) {
                                    if (this.llama.method_6727()) {
                                        if (this.llama.method_6703()) {
                                            moves = 0;
                                            llamaEntity = Bot.mc.field_1724.field_7512;
                                            if (llamaEntity instanceof class_1724) {
                                                shulkerx = (class_1724)llamaEntity;
                                                if (this.putTimer.passed(250L)) {
                                                    if (!this.putIn) {
                                                        for (class_1735 slotxxxxx : shulkerx.field_7761) {
                                                            var12_63 = slotxxxxx.method_7677().method_7909();
                                                            if (!(var12_63 instanceof class_1747) || !((blockItemx = (class_1747)var12_63).method_7711() instanceof class_2480)) continue;
                                                            Bot.mc.field_1761.method_2906(shulkerx.field_7763, slotxxxxx.field_7874, 0, class_1713.field_7794, (class_1657)Bot.mc.field_1724);
                                                            this.putTimer.reset();
                                                            if (++moves < 15) continue;
                                                            break;
                                                        }
                                                        this.putIn = true;
                                                    } else {
                                                        this.stage = Stage.Kill;
                                                        Bot.mc.method_1562().method_52787((class_2596)new class_2851(0.0f, 0.0f, false, true));
                                                    }
                                                    return;
                                                }
                                            } else {
                                                this.putIn = false;
                                                this.putTimer.reset();
                                                Bot.mc.field_1724.method_3132();
                                            }
                                            return;
                                        }
                                        Bot.mc.method_1562().method_52787((class_2596)new class_2851(0.0f, 0.0f, false, true));
                                        return;
                                    }
                                    return;
                                }
                                if (Bot.mc.field_1724.method_33571().method_1022(this.llama.method_19538()) > 5.0) {
                                    this.closeTo(this.llama.method_24515());
                                    return;
                                }
                                if (this.llama.method_6109()) {
                                    if (Bot.mc.field_1755 != null) {
                                        Bot.mc.field_1755.method_25419();
                                    }
                                    slotxxxxxx = InventoryUtil.findBlockInventorySlot(class_2246.field_10359);
                                    InventoryUtil.inventorySwap(slotxxxxxx, Bot.mc.field_1724.method_31548().field_7545);
                                    Alien.ROTATION.lookAt(this.llama.method_19538());
                                    Bot.mc.field_1761.method_2905((class_1657)Bot.mc.field_1724, (class_1297)this.llama, class_1268.field_5808);
                                    InventoryUtil.inventorySwap(slotxxxxxx, Bot.mc.field_1724.method_31548().field_7545);
                                } else if (this.llama.method_6727()) {
                                    if (this.llama.method_6703()) {
                                        for (i = 0; i < 9; ++i) {
                                            if (!Bot.mc.field_1724.method_31548().method_5438(i).method_7960()) continue;
                                            InventoryUtil.switchToSlot(i);
                                            Bot.mc.field_1761.method_2905((class_1657)Bot.mc.field_1724, (class_1297)this.llama, class_1268.field_5808);
                                            return;
                                        }
                                        for (ix = 0; ix < 9; ++ix) {
                                            llamaEntity = Bot.mc.field_1724.method_31548().method_5438(ix).method_7909();
                                            if ((!(llamaEntity instanceof class_1747) || !((blockItemxx = (class_1747)llamaEntity).method_7711() instanceof class_2480)) && Bot.mc.field_1724.method_31548().method_5438(ix).method_7909() != class_1802.field_8745) continue;
                                            InventoryUtil.switchToSlot(ix);
                                            Bot.mc.field_1724.method_7290(true);
                                            Bot.mc.field_1761.method_2905((class_1657)Bot.mc.field_1724, (class_1297)this.llama, class_1268.field_5808);
                                            return;
                                        }
                                        return;
                                    }
                                    this.putTimer.reset();
                                    this.putIn = false;
                                    if (Bot.mc.field_1755 != null) {
                                        Bot.mc.field_1755.method_25419();
                                    }
                                    InventoryUtil.inventorySwap(chestSlot, Bot.mc.field_1724.method_31548().field_7545);
                                    Alien.ROTATION.lookAt(this.llama.method_19538());
                                    Bot.mc.field_1761.method_2905((class_1657)Bot.mc.field_1724, (class_1297)this.llama, class_1268.field_5808);
                                    InventoryUtil.inventorySwap(chestSlot, Bot.mc.field_1724.method_31548().field_7545);
                                } else {
                                    if (Bot.mc.field_1755 != null) {
                                        Bot.mc.field_1755.method_25419();
                                    }
                                    for (ixx = 0; ixx < 9; ++ixx) {
                                        if (!Bot.mc.field_1724.method_31548().method_5438(ixx).method_7960()) continue;
                                        InventoryUtil.switchToSlot(ixx);
                                        Bot.mc.field_1761.method_2905((class_1657)Bot.mc.field_1724, (class_1297)this.llama, class_1268.field_5808);
                                        return;
                                    }
                                    for (ixxx = 0; ixxx < 9; ++ixxx) {
                                        llamaEntity = Bot.mc.field_1724.method_31548().method_5438(ixxx).method_7909();
                                        if (!(llamaEntity instanceof class_1747) || !((blockItemxx = (class_1747)llamaEntity).method_7711() instanceof class_2480)) continue;
                                        InventoryUtil.switchToSlot(ixxx);
                                        Bot.mc.field_1724.method_7290(true);
                                        Bot.mc.field_1761.method_2905((class_1657)Bot.mc.field_1724, (class_1297)this.llama, class_1268.field_5808);
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            case 4: {
                                if (this.llama == null || this.llama.method_29504()) {
                                    this.llama = null;
                                    this.stage = Stage.Summon;
                                    return;
                                }
                                if (Bot.mc.field_1755 != null) {
                                    Bot.mc.field_1755.method_25419();
                                }
                                if (Bot.mc.field_1724.method_5765()) {
                                    Bot.mc.method_1562().method_52787((class_2596)new class_2851(0.0f, 0.0f, false, true));
                                    return;
                                }
                                if (Bot.mc.field_1724.method_19538().method_1022(this.llama.method_19538()) > 1.0) {
                                    this.closeTo(this.llama.method_24515());
                                }
                                if (Bot.mc.field_1724.method_19538().method_1022(this.llama.method_19538()) > 2.0) {
                                    return;
                                }
                                InventoryUtil.switchToSlot(swordSlot);
                                if (this.check()) {
                                    Alien.ROTATION.lookAt(this.llama.method_33571());
                                    Bot.mc.field_1761.method_2918((class_1657)Bot.mc.field_1724, (class_1297)this.llama);
                                    EntityUtil.swingHand(class_1268.field_5808, SwingSide.All);
                                }
                                return;
                            }
                        }
                        return;
                    }
                    case 3: {
                        placePos = SpeedMine.getBreakPos();
                        if (placePos == null || !BlockUtil.canPlace(placePos)) break;
                        this.placeBlock(placePos);
                        break;
                    }
                    case 4: {
                        currentTime = System.currentTimeMillis();
                        iterator = this.scheduledTasks.iterator();
                        while (iterator.hasNext()) {
                            entry = iterator.next();
                            if ((Long)entry.method_15442() > currentTime) continue;
                            ((Runnable)entry.method_15441()).run();
                            iterator.remove();
                        }
                        iterator = this.scheduledTasks2.iterator();
                        while (iterator.hasNext()) {
                            entry = iterator.next();
                            if ((Long)entry.method_15442() > currentTime) continue;
                            ((Runnable)entry.method_15441()).run();
                            iterator.remove();
                        }
                        break;
                    }
                    case 5: {
                        seagrass = InventoryUtil.findItem(class_1802.field_8158);
                        if (seagrass == -1) {
                            if (BaritoneUtil.isActive()) {
                                BaritoneUtil.cancelEverything();
                            }
                            return;
                        }
                        if (this.timeOut.passedS(300.0)) {
                            this.inLove.clear();
                            this.timeOut.reset();
                        }
                        distance = 0.0;
                        target = null;
                        for (class_1297 entity : Alien.THREAD.getEntities()) {
                            if (!(entity instanceof class_1481) || (turtle = (class_1481)entity).method_6109() || this.inLove.contains(turtle) || Math.abs(Bot.mc.field_1724.method_23318() - turtle.method_23318()) > 3.0) continue;
                            dis = Bot.mc.field_1724.method_5739((class_1297)turtle);
                            if (target != null && !(dis < distance)) continue;
                            distance = dis;
                            target = turtle;
                        }
                        if (target == null) {
                            if (this.timeOut.passedS(20.0)) {
                                this.inLove.clear();
                                this.timeOut.reset();
                            }
                            if (BaritoneUtil.isActive()) {
                                BaritoneUtil.cancelEverything();
                            }
                            return;
                        }
                        if (Bot.mc.field_1724.method_5739(target) < 3.0f) {
                            BaritoneUtil.cancelEverything();
                            InventoryUtil.switchToSlot(seagrass);
                            Alien.ROTATION.snapAt(target.method_19538());
                            Bot.mc.method_1562().method_52787((class_2596)class_2824.method_34207(target, (boolean)Bot.mc.field_1724.method_5715(), (class_1268)class_1268.field_5808));
                            EntityUtil.swingHand(class_1268.field_5808, AntiCheat.INSTANCE.interactSwing.getValue());
                            Alien.ROTATION.snapBack();
                            this.inLove.add(target);
                            break;
                        }
                        BaritoneUtil.gotoPos(target.method_24515());
                        break;
                    }
                    case 6: {
                        seagrassx = InventoryUtil.findItem(class_1802.field_8158);
                        if (seagrassx == -1) {
                            return;
                        }
                        if (this.timeOut.passedS(300.0)) {
                            this.inLove.clear();
                            this.timeOut.reset();
                        }
                        distance = 0.0;
                        target = null;
                        for (class_1297 entityx : Alien.THREAD.getEntities()) {
                            if (!(entityx instanceof class_1481) || (turtlex = (class_1481)entityx).method_6109() || this.inLove.contains(turtlex) || Bot.mc.field_1724.method_5739((class_1297)turtlex) > 3.0f) continue;
                            dis = Bot.mc.field_1724.method_5739((class_1297)turtlex);
                            if (target != null && !(dis < distance)) continue;
                            distance = dis;
                            target = turtlex;
                        }
                        if (target == null) {
                            if (this.timeOut.passedS(20.0)) {
                                this.inLove.clear();
                                this.timeOut.reset();
                            }
                            return;
                        }
                        InventoryUtil.switchToSlot(seagrassx);
                        Alien.ROTATION.snapAt(target.method_19538());
                        Bot.mc.method_1562().method_52787((class_2596)class_2824.method_34207(target, (boolean)Bot.mc.field_1724.method_5715(), (class_1268)class_1268.field_5808));
                        EntityUtil.swingHand(class_1268.field_5808, AntiCheat.INSTANCE.interactSwing.getValue());
                        Alien.ROTATION.snapBack();
                        this.inLove.add(target);
                        break;
                    }
                    case 7: {
                        sandBlock = class_2246.field_10102;
                        if (this.redSand.getValue()) {
                            sandBlock = class_2246.field_10534;
                        }
                        if (Aura.INSTANCE.isOn() && Aura.INSTANCE.getTarget(Aura.INSTANCE.range.getValue()) != null) {
                            if (Bot.mc.field_1755 != null) {
                                Bot.mc.field_1755.method_25419();
                            }
                            if (Bot.mc.field_1724.method_31548().field_7545 != (slot = InventoryUtil.findClass(class_1829.class))) {
                                InventoryUtil.switchToSlot(slot);
                            }
                            BaritoneUtil.cancelEverything();
                            return;
                        }
                        if (Bot.mc.field_1755 == null) {
                            this.screenTimeout.reset();
                        } else if (this.screenTimeout.passedS(5.0)) {
                            Bot.mc.field_1755.method_25419();
                        }
                        if (InventoryUtil.findClassInventorySlot(class_1821.class) == -1) {
                            BaritoneUtil.cancelEverything();
                            if (Bot.mc.field_1724.field_7512 instanceof class_1714) {
                                if (this.craftTimer.passedS(1.0)) {
                                    for (class_516 recipeResult : Bot.mc.field_1724.method_3130().method_1393()) {
                                        for (class_8786 recipe : recipeResult.method_2648(true)) {
                                            if (!(recipe.comp_1933().method_8110((class_7225.class_7874)Bot.mc.field_1687.method_30349()).method_7909() instanceof class_1821)) continue;
                                            this.craftTimer.reset();
                                            Bot.mc.field_1761.method_2912(Bot.mc.field_1724.field_7512.field_7763, recipe, false);
                                            Bot.mc.field_1761.method_2906(Bot.mc.field_1724.field_7512.field_7763, 0, 1, class_1713.field_7794, (class_1657)Bot.mc.field_1724);
                                            return;
                                        }
                                    }
                                }
                            } else {
                                bestPos = null;
                                distance = 100.0;
                                for (class_2338 pos : BlockUtil.getSphere(3.0f)) {
                                    if (Bot.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_9980 && BlockUtil.getClickSideStrict(pos) != null) {
                                        BlockUtil.clickBlock(pos, BlockUtil.getClickSide(pos), this.rotate.getValue());
                                        return;
                                    }
                                    if (!BlockUtil.canPlace(pos) || bestPos != null && !((double)class_3532.method_15355((float)((float)Bot.mc.field_1724.method_5707(pos.method_46558()))) < distance)) continue;
                                    bestPos = pos;
                                    distance = class_3532.method_15355((float)((float)Bot.mc.field_1724.method_5707(pos.method_46558())));
                                }
                                if (bestPos != null) {
                                    craftTable = InventoryUtil.findItemInventorySlot(class_1802.field_8465);
                                    if (craftTable == -1) {
                                        return;
                                    }
                                    InventoryUtil.inventorySwap(craftTable, Bot.mc.field_1724.method_31548().field_7545);
                                    BlockUtil.placeBlock(bestPos, this.rotate.getValue());
                                    InventoryUtil.inventorySwap(craftTable, Bot.mc.field_1724.method_31548().field_7545);
                                }
                            }
                            return;
                        }
                        hasShulkerItemEntity = null;
                        for (class_1297 entityxx : Alien.THREAD.getEntities()) {
                            if (!(entityxx instanceof class_1542) || !((turtlex = (itemEntity = (class_1542)entityxx).method_6983().method_7909()) instanceof class_1747) || !((item = (class_1747)turtlex).method_7711() instanceof class_2480) && item.method_7711() != class_2246.field_10443) continue;
                            hasShulkerItemEntity = itemEntity;
                            break;
                        }
                        if (Bot.mc.field_1724.field_7512 instanceof class_1714 && Bot.mc.field_1755 != null) {
                            Bot.mc.field_1755.method_25419();
                        }
                        lbl571: {
                        if ((sands = InventoryUtil.getItemCount(class_1802.field_8858)) < 1728) break lbl571;
                        for (slot1 = 9; slot1 < 36; ++slot1) {
                            stack = Bot.mc.field_1724.method_31548().method_5438(slot1);
                            if (stack.method_7960() || !((turtlex = stack.method_7909()) instanceof class_1747) || (blockItem = (class_1747)turtlex).method_7711() != sandBlock || stack.method_7947() >= stack.method_7914()) continue;
                            Bot.mc.field_1761.method_2906(Bot.mc.field_1724.field_7498.field_7763, slot1, 1, class_1713.field_7795, (class_1657)Bot.mc.field_1724);
                        }
                        if (Bot.mc.field_1755 instanceof class_490) {
                            Bot.mc.field_1755.method_25419();
                            return;
                        }
                        shulkerSlot = InventoryUtil.findClassInventorySlot(class_2480.class);
                        shulker = BlockUtil.getBlock(class_2480.class, 3.0f);
                        if (Bot.mc.field_1755 instanceof class_465 && (var104 = Bot.mc.field_1724.field_7512) instanceof class_1703) {
                            if (!(Bot.mc.field_1724.field_7512 instanceof class_1733)) {
                                BaritoneUtil.cancelEverything();
                                if (shulkerSlot != -1) {
                                    Bot.mc.field_1755.method_25419();
                                    break;
                                }
                                var106 = var104.field_7761.iterator();
                                do {
                                    if (!var106.hasNext()) {
                                        return;
                                    }
                                    slot = (class_1735)var106.next();
                                } while (slot.field_7874 >= 27 || !((var18_102 = slot.method_7677().method_7909()) instanceof class_1747) || !((blockItem = (class_1747)var18_102).method_7711() instanceof class_2480) || ShulkerViewer.hasItems(slot.method_7677()));
                                Bot.mc.field_1761.method_2906(var104.field_7763, slot.field_7874, 0, class_1713.field_7794, (class_1657)Bot.mc.field_1724);
                                break;
                            }
                            BaritoneUtil.cancelEverything();
                            this.storageSand = true;
                            slot = Bot.mc.field_1724.field_7512;
                            if (slot instanceof class_1733) {
                                shulkerBoxScreenHandler = (class_1733)slot;
                                for (class_1735 slot : shulkerBoxScreenHandler.field_7761) {
                                    if (slot.field_7874 >= 27 || !slot.method_7677().method_7960()) continue;
                                    for (class_1735 slot2 : shulkerBoxScreenHandler.field_7761) {
                                        if (slot2.field_7874 < 27 || slot2.method_7677().method_7909() != class_1802.field_8858 || slot2.method_7677().method_7947() != slot2.method_7677().method_7914()) continue;
                                        Bot.mc.field_1761.method_2906(shulkerBoxScreenHandler.field_7763, slot2.field_7874, 0, class_1713.field_7794, (class_1657)Bot.mc.field_1724);
                                        return;
                                    }
                                }
                            }
                        } else if (shulker == null) {
                            distance = 100.0;
                            bestPos = null;
                            for (class_2338 pos : BlockUtil.getSphere(3.0f, Bot.mc.field_1724.method_33571())) {
                                if (!Bot.mc.field_1687.method_22347(pos.method_10084()) || !BlockUtil.clientCanPlace(pos, false) || !BlockUtil.isStrictDirection(pos.method_10093(class_2350.field_11033), class_2350.field_11036) || !BlockUtil.canClick(pos.method_10093(class_2350.field_11033)) || bestPos != null && !((double)class_3532.method_15355((float)((float)Bot.mc.field_1724.method_5707(pos.method_46558()))) < distance)) continue;
                                distance = class_3532.method_15355((float)((float)Bot.mc.field_1724.method_5707(pos.method_46558())));
                                bestPos = pos;
                            }
                            if (bestPos != null) {
                                BaritoneUtil.cancelEverything();
                                if (shulkerSlot != -1) {
                                    InventoryUtil.inventorySwap(shulkerSlot, Bot.mc.field_1724.method_31548().field_7545);
                                    BlockUtil.clickBlock(bestPos.method_10093(class_2350.field_11033), class_2350.field_11036, this.rotate.getValue());
                                    InventoryUtil.inventorySwap(shulkerSlot, Bot.mc.field_1724.method_31548().field_7545);
                                    break;
                                }
                                ec = BlockUtil.getBlock(class_2246.field_10443, 3.0f);
                                if (ec != null) {
                                    BlockUtil.clickBlock(ec, BlockUtil.getClickSide(ec), this.rotate.getValue());
                                    break;
                                }
                                enderChest = InventoryUtil.findBlockInventorySlot(class_2246.field_10443);
                                if (enderChest != -1) {
                                    InventoryUtil.inventorySwap(enderChest, Bot.mc.field_1724.method_31548().field_7545);
                                    BlockUtil.placeBlock(bestPos, true);
                                    InventoryUtil.inventorySwap(enderChest, Bot.mc.field_1724.method_31548().field_7545);
                                    break;
                                }
                            }
                        } else {
                            BlockUtil.clickBlock((class_2338)shulker, BlockUtil.getClickSide((class_2338)shulker), this.rotate.getValue());
                            break;
                        }
                        }
                            if (this.storageSand) {
                                shulker = Bot.mc.field_1724.field_7512;
                                if (shulker instanceof class_1733) {
                                    shulkerBoxScreenHandler = (class_1733)shulker;
                                    for (class_1735 slotx : shulkerBoxScreenHandler.field_7761) {
                                        if (slotx.field_7874 >= 27 || !slotx.method_7677().method_7960()) continue;
                                        for (class_1735 slot2x : shulkerBoxScreenHandler.field_7761) {
                                            if (slot2x.field_7874 < 27 || slot2x.method_7677().method_7909() != class_1802.field_8858 || slot2x.method_7677().method_7947() != slot2x.method_7677().method_7914()) continue;
                                            Bot.mc.field_1761.method_2906(shulkerBoxScreenHandler.field_7763, slot2x.field_7874, 0, class_1713.field_7794, (class_1657)Bot.mc.field_1724);
                                            return;
                                        }
                                    }
                                    Bot.mc.field_1755.method_25419();
                                    this.storageSand = false;
                                    break;
                                }
                            } else {
                                shulkerx = BlockUtil.getBlock(class_2480.class, 3.0f);
                                if (shulkerx != null) {
                                    BaritoneUtil.mine(Bot.mc.field_1687.method_8320(shulkerx).method_26204());
                                    break;
                                }
                                fillShulker = InventoryUtil.findClassInventorySlot(class_2480.class);
                                if (fillShulker != -1) {
                                    BaritoneUtil.cancelEverything();
                                    var110 = Bot.mc.field_1724.field_7512;
                                    if (var110 instanceof class_1703) {
                                        for (class_1735 slotxx : var110.field_7761) {
                                            if (slotxx.field_7874 < 27 || !((enderChest = slotxx.method_7677().method_7909()) instanceof class_1747) || !((blockItem = (class_1747)enderChest).method_7711() instanceof class_2480)) continue;
                                            Bot.mc.field_1761.method_2906(var110.field_7763, slotxx.field_7874, 0, class_1713.field_7794, (class_1657)Bot.mc.field_1724);
                                            return;
                                        }
                                    }
                                    distance = 100.0;
                                    bestPos = null;
                                    for (class_2338 posx : BlockUtil.getSphere(3.0f)) {
                                        if (!Bot.mc.field_1687.method_22347(posx.method_10084()) || !BlockUtil.clientCanPlace(posx, false) || !BlockUtil.isStrictDirection(posx.method_10093(class_2350.field_11033), class_2350.field_11036) || !BlockUtil.canClick(posx.method_10093(class_2350.field_11033)) || bestPos != null && !((double)class_3532.method_15355((float)((float)Bot.mc.field_1724.method_5707(posx.method_46558()))) < distance)) continue;
                                        distance = class_3532.method_15355((float)((float)Bot.mc.field_1724.method_5707(posx.method_46558())));
                                        bestPos = posx;
                                    }
                                    if (bestPos != null) {
                                        ec = BlockUtil.getBlock(class_2246.field_10443, 3.0f);
                                        if (ec != null) {
                                            BlockUtil.clickBlock(ec, BlockUtil.getClickSide(ec), this.rotate.getValue());
                                        } else {
                                            enderChest = InventoryUtil.findBlockInventorySlot(class_2246.field_10443);
                                            if (enderChest != -1) {
                                                InventoryUtil.inventorySwap(enderChest, Bot.mc.field_1724.method_31548().field_7545);
                                                BlockUtil.placeBlock(bestPos, true);
                                                InventoryUtil.inventorySwap(enderChest, Bot.mc.field_1724.method_31548().field_7545);
                                            }
                                        }
                                    }
                                    return;
                                }
                                if (hasShulkerItemEntity != null) {
                                    BaritoneUtil.gotoPos(hasShulkerItemEntity.method_24515());
                                }
                                if ((posxx = BlockUtil.getBlock(class_2246.field_10443, 5.0f)) != null) {
                                    BaritoneUtil.mine(class_2246.field_10443);
                                    if (Bot.mc.field_1755 != null) {
                                        Bot.mc.field_1755.method_25419();
                                    }
                                    return;
                                }
                                if (this.ai.getValue()) {
                                    BaritoneUtil.mine(sandBlock);
                                }
                                if (this.nuker.getValue()) {
                                    if (!Bot.mc.field_1724.method_24828()) {
                                        return;
                                    }
                                    b = 0;
                                    for (class_2338 sand : BlockUtil.getSphere(3.0f, Bot.mc.field_1724.method_33571())) {
                                        if (sandBlock != Bot.mc.field_1687.method_8320(sand).method_26204() || (side = BlockUtil.getClickSideStrict(sand)) == null) continue;
                                        PlaceRender.INSTANCE.create(sand);
                                        Alien.ROTATION.snapAt(sand.method_46558());
                                        Bot.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12973, sand, side, id));
                                        Bot.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12968, sand, side, id));
                                        Bot.sendSequencedPacket(id -> new class_2846(class_2846.class_2847.field_12973, sand, side, id));
                                        Alien.ROTATION.snapBack();
                                        if (!((double)(++b) >= this.breaks.getValue())) continue;
                                        return;
                                    }
                                }
                            }
                        break block197;
                    }
                    case 8: {
                        if (this.getScore.getValue() && this.commandTimer.passedS(4.0)) {
                            Bot.mc.field_1724.field_3944.method_45731("duel " + this.name.getValue());
                            this.commandTimer.reset();
                        }
                        if (!(Bot.mc.field_1724.field_7512 instanceof class_1707) || !this.duelItemTimer.passedS(1.0)) break;
                        Bot.mc.field_1761.method_2906(Bot.mc.field_1724.field_7512.field_7763, 0, 0, class_1713.field_7790, (class_1657)Bot.mc.field_1724);
                        this.duelItemTimer.reset();
                        break;
                    }
                    case 9: {
                        if (this.inventory.getValue() && !EntityUtil.inInventory()) {
                            return;
                        }
                        this.placeProgress = 0;
                        if (!this.slabPlacerDelay.passed((long)this.placeDelay.getValue())) {
                            return;
                        }
                        if (Bot.mc.field_1724.method_6115() && this.usingPause.getValue()) {
                            return;
                        }
                        for (class_2338 posxxx : BlockUtil.getSphere(this.range.getValueFloat(), Bot.mc.field_1724.method_19538())) {
                            if (!Bot.mc.field_1687.method_8320(posxxx).method_26234((class_1922)Bot.mc.field_1687, posxxx) && Bot.mc.field_1687.method_8320(posxxx).method_26204() instanceof class_2482 || Bot.mc.field_1687.method_22347(posxxx) || !BlockUtil.canReplace(posxxx.method_10084())) continue;
                            this.tryPlaceBlock(posxxx.method_10084());
                        }
                        break block197;
                    }
                    case 10: {
                        ominousSlot = InventoryUtil.findItem(class_1802.field_50140);
                        if (ominousSlot == -1) break;
                        if (!Bot.mc.field_1724.method_6059(class_1294.field_16595) && !Bot.mc.field_1724.method_6059(class_1294.field_50117)) {
                            if (!this.ominousTimer.passedS(this.maxTime.getValue())) break;
                            if (Bot.mc.field_1724.method_6047().method_7909() != class_1802.field_50140) {
                                this.lastSlot = Bot.mc.field_1724.method_31548().field_7545;
                                InventoryUtil.switchToSlot(ominousSlot);
                                break;
                            }
                            Bot.mc.field_1690.field_1904.method_23481(true);
                            this.ominousTimer.reset();
                            break;
                        }
                        if (Bot.mc.field_1724.method_6047().method_7909() != class_1802.field_50140) break;
                        Bot.mc.field_1690.field_1904.method_23481(false);
                        if (this.lastSlot == -1) break;
                        InventoryUtil.switchToSlot(this.lastSlot);
                        this.lastSlot = -1;
                    }
                }
                break block197;
            }
            food = InventoryUtil.findItem(class_1802.field_8367);
            if (food == -1) {
                food = InventoryUtil.findItem(class_1802.field_8463);
            }
            if (food == -1) {
                food = InventoryUtil.findItem(class_1802.field_8071);
            }
            if (food == -1 && this.anyFood.getValue()) {
                food = InventoryUtil.getFood();
            }
            if (food != -1) {
                if (Bot.mc.field_1755 != null && this.mode.is(Mode.XinDupe)) {
                    Bot.mc.field_1755.method_25419();
                }
                if (Bot.mc.field_1724.method_31548().field_7545 != food) {
                    InventoryUtil.switchToSlot(food);
                }
                Bot.mc.field_1690.field_1904.method_23481(true);
                this.endEat = true;
                Bot.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
            }
            BaritoneUtil.cancelEverything();
        }
    }

    @EventListener
    private void PacketReceive(PacketEvent.Receive event) {
        class_7439 packet;
        class_2596<?> class_25962;
        if (!Bot.nullCheck() && this.mode.is(Mode.ScoreFarmer) && (class_25962 = event.getPacket()) instanceof class_7439 && (packet = (class_7439)class_25962).comp_763() != null) {
            String received = packet.comp_763().getString().replaceAll("\u00a7[a-zA-Z0-9]", "");
            if (!this.getScore.getValue() && received.contains("\u4f60\u6536\u5230\u4e00\u4e2a\u51b3\u6597\u7533\u8bf7")) {
                Bot.mc.field_1724.field_3944.method_45731("duel accept " + this.name.getValue());
            } else if (!received.contains("<")) {
                if (this.getScore.getValue() && received.contains("Starting in 3 seconds")) {
                    Bot.mc.field_1724.field_3944.method_45731("suicide");
                } else if (!this.getScore.getValue() && received.contains("Starting in 4 seconds")) {
                    Bot.mc.field_1724.field_3944.method_45731("suicide");
                }
            }
        }
    }

    @EventListener
    public void onRotate(UpdateRotateEvent event) {
        if (this.mode.is(Mode.XinDupe)) {
            event.setPitch(88.0f);
        }
    }

    private boolean check() {
        int at = ((ILivingEntity)Bot.mc.field_1724).getLastAttackedTicks();
        return (double)Math.max((float)at / Aura.getAttackCooldownProgressPerTick(), 0.0f) >= 1.3;
    }

    private void closeTo(class_2338 pos) {
        double speed = 0.19153333333333333;
        float forward = 1.0f;
        float side = 0.0f;
        float yaw = RotationManager.getRotation(pos.method_46558())[0];
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        MovementUtil.setMotionX(posX);
        MovementUtil.setMotionZ(posZ);
    }

    private static /* synthetic */ class_2596 lambda$onUpdate$26(int id) {
        return new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch());
    }

    private static /* synthetic */ class_2596 lambda$onUpdate$25(class_2338 sand, class_2350 side, int id) {
        return new class_2846(class_2846.class_2847.field_12973, sand, side, id);
    }

    private static /* synthetic */ class_2596 lambda$onUpdate$24(class_2338 sand, class_2350 side, int id) {
        return new class_2846(class_2846.class_2847.field_12968, sand, side, id);
    }

    private static /* synthetic */ class_2596 lambda$onUpdate$23(class_2338 sand, class_2350 side, int id) {
        return new class_2846(class_2846.class_2847.field_12973, sand, side, id);
    }

    private static /* synthetic */ class_2596 lambda$onUpdate$22(int id) {
        return new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch());
    }

    public static enum Mode {
        AutoTrade,
        ItemFrameDupe,
        XinDupe,
        NPlusOneDupe,
        TridentDupe,
        TurtlePath,
        Turtle,
        SandMiner,
        ScoreFarmer,
        SlabPlacer,
        Ominous,
        None;

    }

    public static enum Stage {
        Open,
        Take,
        Summon,
        Tame,
        Kill;

    }
}

