/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;

public class CevBreaker
extends Module {
    public static CevBreaker INSTANCE;
    private final SliderSetting targetRange = this.add(new SliderSetting("TargetRange", 5.0, 0.0, 8.0, 0.1));
    private final SliderSetting breakRange = this.add(new SliderSetting("BreakRange", 5.0, 0.0, 8.0, 0.1));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 100, 0, 500).setSuffix("ms"));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true));
    private final BooleanSetting ground = this.add(new BooleanSetting("Ground", true));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BooleanSetting top = this.add(new BooleanSetting("Top", false));
    private final BooleanSetting bevel = this.add(new BooleanSetting("Bevel", true));
    private final Timer timer = new Timer();
    private class_1657 target = null;

    public CevBreaker() {
        super("CevBreaker", Module.Category.Combat);
        this.setChinese("\u81ea\u52a8\u70b8\u5934");
        INSTANCE = this;
    }

    public static boolean canPlaceCrystal(class_2338 pos) {
        return CevBreaker.mc.field_1687.method_22347(pos) && BlockUtil.noEntityBlockCrystal(pos, false) && BlockUtil.noEntityBlockCrystal(pos.method_10084(), false) && (!ClientSetting.INSTANCE.lowVersion.getValue() || CevBreaker.mc.field_1687.method_22347(pos.method_10084()));
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (!(this.inventory.getValue() && !EntityUtil.inInventory() || this.ground.getValue() && !CevBreaker.mc.field_1724.method_24828())) {
            SpeedMine.INSTANCE.crystal.setValue(true);
            this.target = CombatUtil.getClosestEnemy(this.targetRange.getValue());
            if (this.target != null) {
                class_2338 pos;
                class_2338 targetPos = EntityUtil.getEntityPos((class_1297)this.target);
                if (SpeedMine.getBreakPos() != null) {
                    for (class_2350 facing : class_2350.values()) {
                        if (facing == class_2350.field_11033 || !(facing == class_2350.field_11036 ? this.top.getValue() : this.bevel.getValue()) || (pos = targetPos.method_10086(1).method_10093(facing)).method_10084().method_46558().method_1022(CevBreaker.mc.field_1724.method_19538()) > this.breakRange.getValue() || !SpeedMine.getBreakPos().equals((Object)targetPos.method_10086(1).method_10093(facing))) continue;
                        if (CevBreaker.canPlaceCrystal(targetPos.method_10086(2).method_10093(facing))) {
                            if (CevBreaker.mc.field_1687.method_22347(pos)) {
                                if (!BlockUtil.canPlace(pos)) continue;
                                if (!this.timer.passedMs(this.delay.getValue())) {
                                    return;
                                }
                                this.placeBlock(pos);
                                this.timer.reset();
                                return;
                            }
                            if (this.getBlock(pos) != class_2246.field_10540) continue;
                            SpeedMine.INSTANCE.mine(pos);
                            this.timer.reset();
                            return;
                        }
                        if (!BlockUtil.hasCrystal(targetPos.method_10086(2).method_10093(facing))) continue;
                        if (CevBreaker.mc.field_1687.method_22347(pos)) {
                            return;
                        }
                        if (this.getBlock(pos) != class_2246.field_10540) continue;
                        SpeedMine.INSTANCE.mine(pos);
                        this.timer.reset();
                        return;
                    }
                }
                for (class_2350 facingx : class_2350.values()) {
                    if (facingx == class_2350.field_11033 || !(facingx == class_2350.field_11036 ? this.top.getValue() : this.bevel.getValue()) || (pos = targetPos.method_10086(1).method_10093(facingx)).method_10084().method_46558().method_1022(CevBreaker.mc.field_1724.method_19538()) > this.breakRange.getValue()) continue;
                    if (CevBreaker.canPlaceCrystal(targetPos.method_10086(2).method_10093(facingx))) {
                        if (CevBreaker.mc.field_1687.method_22347(pos)) {
                            if (!BlockUtil.canPlace(pos)) continue;
                            if (!this.timer.passedMs(this.delay.getValue())) {
                                return;
                            }
                            this.placeBlock(pos);
                            this.timer.reset();
                            break;
                        }
                        if (this.getBlock(pos) != class_2246.field_10540) continue;
                        SpeedMine.INSTANCE.mine(pos);
                        this.timer.reset();
                        break;
                    }
                    if (!BlockUtil.hasCrystal(targetPos.method_10086(2).method_10093(facingx))) continue;
                    if (CevBreaker.mc.field_1687.method_22347(pos)) break;
                    if (this.getBlock(pos) != class_2246.field_10540) continue;
                    SpeedMine.INSTANCE.mine(pos);
                    this.timer.reset();
                    break;
                }
            }
        }
    }

    private void doSwap(int slot) {
        if (this.inventory.getValue()) {
            InventoryUtil.inventorySwap(slot, CevBreaker.mc.field_1724.method_31548().field_7545);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getBlock() {
        return this.inventory.getValue() ? InventoryUtil.findBlockInventorySlot(class_2246.field_10540) : InventoryUtil.findBlock(class_2246.field_10540);
    }

    private void placeBlock(class_2338 pos) {
        int block = this.getBlock();
        if (block != -1) {
            int oldSlot = CevBreaker.mc.field_1724.method_31548().field_7545;
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

    @Override
    public String getInfo() {
        return this.target != null ? this.target.method_5477().getString() : null;
    }

    private class_2248 getBlock(class_2338 pos) {
        return CevBreaker.mc.field_1687.method_8320(pos).method_26204();
    }
}

