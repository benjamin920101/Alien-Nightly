/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.effect.StatusEffect
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.component.type.PotionContentsComponent
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
 *  net.minecraft.component.DataComponentTypes
 */
package dev.luminous.mod.modules.impl.player;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.util.Hand;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.component.DataComponentTypes;

public class AutoPot
extends Module {
    public static AutoPot INSTANCE;
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 5.0, 0.0, 10.0, 0.1).setSuffix("s"));
    private final BooleanSetting speed = this.add(new BooleanSetting("Speed", false));
    private final BooleanSetting resistance = this.add(new BooleanSetting("Resistance", false));
    private final BooleanSetting strength = this.add(new BooleanSetting("Strength", false));
    private final BooleanSetting slowFalling = this.add(new BooleanSetting("SlowFalling", false));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true));
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", false));
    private final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true));
    private final BindSetting speedKey = this.add(new BindSetting("SpeedKey", -1));
    private final BindSetting strengthKey = this.add(new BindSetting("StrengthKey", -1));
    private final BindSetting resistanceKey = this.add(new BindSetting("ResistanceKey", -1));
    private final Timer delayTimer = new Timer();
    private boolean throwing = false;
    private boolean turtlePress;
    private boolean speedPress;
    private boolean strengthPress;

    public AutoPot() {
        super("AutoPot", Module.Category.Player);
        this.setChinese("\u81ea\u52a8\u836f\u6c34");
        INSTANCE = this;
        Alien.EVENT_BUS.subscribe(new AutoPotTick());
    }

    public static int findPotionInventorySlot(class_1291 targetEffect) {
        for (int i = 35; i >= 0; --i) {
            class_1799 itemStack = AutoPot.mc.field_1724.method_31548().method_5438(i);
            if (class_1792.method_7880((class_1792)itemStack.method_7909()) != class_1792.method_7880((class_1792)class_1802.field_8436)) continue;
            class_1844 potionContentsComponent = (class_1844)itemStack.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
            for (class_1293 effect : potionContentsComponent.method_57397()) {
                if (effect.method_5579().comp_349() != targetEffect) continue;
                return i < 9 ? i + 36 : i;
            }
        }
        return -1;
    }

    public static int findPotion(class_1291 targetEffect) {
        for (int i = 0; i < 9; ++i) {
            class_1799 itemStack = AutoPot.mc.field_1724.method_31548().method_5438(i);
            if (class_1792.method_7880((class_1792)itemStack.method_7909()) != class_1792.method_7880((class_1792)class_1802.field_8436)) continue;
            class_1844 potionContentsComponent = (class_1844)itemStack.method_57825(class_9334.field_49651, (Object)class_1844.field_49274);
            for (class_1293 effect : potionContentsComponent.method_57397()) {
                if (effect.method_5579().comp_349() != targetEffect) continue;
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onDisable() {
        this.throwing = false;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (!(this.inventory.getValue() && !EntityUtil.inInventory() || !this.delayTimer.passedMs(this.delay.getValue() * 1000.0) || this.onlyGround.getValue() && (!AutoPot.mc.field_1724.method_24828() && !Alien.PLAYER.isInWeb((class_1657)AutoPot.mc.field_1724) || AutoPot.mc.field_1687.method_22347((class_2338)new BlockPosX(AutoPot.mc.field_1724.method_19538().method_1031(0.0, -1.0, 0.0)))))) {
            if (this.resistance.getValue() && (!AutoPot.mc.field_1724.method_6059(class_1294.field_5907) || AutoPot.mc.field_1724.method_6112(class_1294.field_5907).method_5578() < 2)) {
                this.throwing = this.checkThrow((class_1291)class_1294.field_5907.comp_349());
                if (this.isThrow()) {
                    this.throwPotion((class_1291)class_1294.field_5907.comp_349());
                    return;
                }
            }
            if (this.speed.getValue() && !AutoPot.mc.field_1724.method_6059(class_1294.field_5904)) {
                this.throwing = this.checkThrow((class_1291)class_1294.field_5904.comp_349());
                if (this.isThrow()) {
                    this.throwPotion((class_1291)class_1294.field_5904.comp_349());
                    return;
                }
            }
            if (this.strength.getValue() && !AutoPot.mc.field_1724.method_6059(class_1294.field_5910)) {
                this.throwing = this.checkThrow((class_1291)class_1294.field_5910.comp_349());
                if (this.isThrow()) {
                    this.throwPotion((class_1291)class_1294.field_5910.comp_349());
                    return;
                }
            }
            if (this.slowFalling.getValue() && !AutoPot.mc.field_1724.method_6059(class_1294.field_5906)) {
                this.throwing = this.checkThrow((class_1291)class_1294.field_5906.comp_349());
                if (this.isThrow()) {
                    this.throwPotion((class_1291)class_1294.field_5906.comp_349());
                }
            }
        }
    }

    public void throwPotion(class_1291 targetEffect) {
        int newSlot;
        int oldSlot = AutoPot.mc.field_1724.method_31548().field_7545;
        if (this.inventory.getValue() && (newSlot = AutoPot.findPotionInventorySlot(targetEffect)) != -1) {
            Alien.ROTATION.snapAt(Alien.ROTATION.rotationYaw, 90.0f);
            InventoryUtil.inventorySwap(newSlot, AutoPot.mc.field_1724.method_31548().field_7545);
            AutoPot.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
            InventoryUtil.inventorySwap(newSlot, AutoPot.mc.field_1724.method_31548().field_7545);
            EntityUtil.syncInventory();
            Alien.ROTATION.snapBack();
            this.delayTimer.reset();
        } else {
            newSlot = AutoPot.findPotion(targetEffect);
            if (newSlot != -1) {
                Alien.ROTATION.snapAt(Alien.ROTATION.rotationYaw, 90.0f);
                InventoryUtil.switchToSlot(newSlot);
                AutoPot.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                InventoryUtil.switchToSlot(oldSlot);
                Alien.ROTATION.snapBack();
                this.delayTimer.reset();
            }
        }
    }

    public boolean isThrow() {
        return this.throwing;
    }

    public boolean checkThrow(class_1291 targetEffect) {
        if (!EntityUtil.inInventory()) {
            return false;
        }
        return this.usingPause.getValue() && AutoPot.mc.field_1724.method_6115() ? false : AutoPot.findPotion(targetEffect) != -1 || this.inventory.getValue() && AutoPot.findPotionInventorySlot(targetEffect) != -1;
    }

    public class AutoPotTick {
        @EventListener
        public void onTick(ClientTickEvent event) {
            if (!(Module.nullCheck() || event.isPost() || AutoPot.this.inventory.getValue() && !EntityUtil.inInventory())) {
                if (Wrapper.mc.field_1755 == null) {
                    if (AutoPot.this.resistanceKey.isPressed()) {
                        if (!AutoPot.this.turtlePress && AutoPot.this.checkThrow((class_1291)class_1294.field_5907.comp_349())) {
                            AutoPot.this.throwPotion((class_1291)class_1294.field_5907.comp_349());
                            AutoPot.this.turtlePress = true;
                            return;
                        }
                    } else {
                        AutoPot.this.turtlePress = false;
                    }
                    if (AutoPot.this.strengthKey.isPressed()) {
                        if (!AutoPot.this.strengthPress && AutoPot.this.checkThrow((class_1291)class_1294.field_5910.comp_349())) {
                            AutoPot.this.throwPotion((class_1291)class_1294.field_5910.comp_349());
                            AutoPot.this.strengthPress = true;
                            return;
                        }
                    } else {
                        AutoPot.this.strengthPress = false;
                    }
                    if (AutoPot.this.speedKey.isPressed()) {
                        if (!AutoPot.this.speedPress && AutoPot.this.checkThrow((class_1291)class_1294.field_5904.comp_349())) {
                            AutoPot.this.throwPotion((class_1291)class_1294.field_5904.comp_349());
                            AutoPot.this.speedPress = true;
                        }
                    } else {
                        AutoPot.this.speedPress = false;
                    }
                } else {
                    AutoPot.this.speedPress = false;
                    AutoPot.this.turtlePress = false;
                    AutoPot.this.strengthPress = false;
                }
            }
        }
    }
}

