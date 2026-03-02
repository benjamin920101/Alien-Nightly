/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1671
 *  net.minecraft.class_1802
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2680
 *  net.minecraft.class_2708
 *  net.minecraft.class_2709
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 *  net.minecraft.class_4050
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.api.events.eventbus.EventHandler;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.RotateEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.utils.entity.PauseUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.Sprint;
import dev.luminous.mod.modules.impl.player.OffFirework;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1671;
import net.minecraft.class_1802;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_2708;
import net.minecraft.class_2709;
import net.minecraft.class_2848;
import net.minecraft.class_4050;

public class FakeFly
extends Module {
    public static FakeFly INSTANCE;
    private final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Control));
    public final BooleanSetting armor = this.add(new BooleanSetting("Armor", true));
    private final BooleanSetting stand = this.add(new BooleanSetting("Stand", false));
    private final SliderSetting timeout = this.add(new SliderSetting("Timeout", 0.5, 0.1, 1.0, 0.01));
    private final BooleanSetting key = this.add(new BooleanSetting("OnlyKeyRocket", false));
    private final BooleanSetting rotate = this.add(new BooleanSetting("Rotate", true, () -> this.mode.is(Mode.Control)));
    private final BooleanSetting rocket = this.add(new BooleanSetting("Firework", true));
    public final SliderSetting fireworkDelay = this.add(new SliderSetting("FireworkDelay", 1.2, 0.0, 5.0, 0.1, this.rocket::getValue));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false, this.rocket::getValue).setParent());
    private final BooleanSetting same = this.add(new BooleanSetting("SameHand", false, () -> this.rocket.getValue() && this.usingPause.isOpen()));
    public final SliderSetting horizontalSpeed = this.add(new SliderSetting("HorizontalSpeed", 25, 0, 100, () -> this.mode.is(Mode.Control)));
    public final SliderSetting verticalSpeed = this.add(new SliderSetting("VerticalSpeed", 25.0, 0.0, 100.0, () -> this.mode.is(Mode.Control)));
    public final SliderSetting pitch = this.add(new SliderSetting("Pitch", 90.0, 0.0, 90.0, 0.1, () -> this.mode.is(Mode.Control) && this.rotate.getValue()));
    public final SliderSetting accelTime = this.add(new SliderSetting("AccelerationTime", 0.0, 0.01, 2.0, 0.01, () -> this.mode.is(Mode.Control)));
    public final BooleanSetting sprintToBoost = this.add(new BooleanSetting("SprintToBoost", true, () -> this.mode.is(Mode.Control)));
    public final SliderSetting sprintToBoostMaxSpeed = this.add(new SliderSetting("BoostMaxSpeed", 100.0, 50.0, 300.0, () -> this.mode.is(Mode.Control)));
    public final SliderSetting boostAccelTime = this.add(new SliderSetting("BoostAccelTime", 0.5, 0.01, 2.0, 0.01, () -> this.mode.is(Mode.Control)));
    private class_243 lastMovement = class_243.field_1353;
    private class_243 currentVelocity = class_243.field_1353;
    private long timeOfLastRubberband = 0L;
    private class_243 lastRubberband = class_243.field_1353;
    public boolean fly = false;
    private final Timer instantFlyTimer = new Timer();
    private final Timer rocketTimer = new Timer();
    private final Timer spoofTimer = new Timer();

    public FakeFly() {
        super("FakeFly", Module.Category.Movement);
        this.setChinese("\u865a\u5047\u98de\u884c");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @Override
    public boolean onEnable() {
        if (FakeFly.nullCheck()) {
            return false;
        }
        this.currentVelocity = FakeFly.mc.field_1724.method_18798();
        this.fly = false;
        if (!FakeFly.mc.field_1724.method_7337()) {
            FakeFly.mc.field_1724.method_31549().field_7478 = false;
        }
        FakeFly.mc.field_1724.method_31549().field_7479 = false;
        this.spoofTimer.reset();
        return false;
    }

    @Override
    public void onDisable() {
        if (FakeFly.nullCheck()) {
            return;
        }
        InventoryUtil.silentSwapEquipChestplate();
        EntityUtil.syncInventory();
        if (!FakeFly.mc.field_1724.method_7337()) {
            FakeFly.mc.field_1724.method_31549().field_7478 = false;
        }
        FakeFly.mc.field_1724.method_31549().field_7479 = false;
        this.sync();
    }

    private void sync() {
        mc.method_1562().method_52787((class_2596)new class_2848((class_1297)FakeFly.mc.field_1724, class_2848.class_2849.field_12984));
        FakeFly.mc.field_1724.method_5660(false);
    }

    @EventHandler
    public void onUpdate() {
        if (FakeFly.nullCheck()) {
            return;
        }
        if (FakeFly.mc.field_1724.method_24828()) {
            this.fly = false;
        }
        if (!FakeFly.mc.field_1724.method_6128()) {
            if (!FakeFly.mc.field_1724.method_24828() && FakeFly.mc.field_1724.method_18798().method_10214() < 0.0) {
                if (!this.instantFlyTimer.passedMs((long)(1000.0 * this.timeout.getValue()))) {
                    return;
                }
                this.instantFlyTimer.reset();
                this.fly = true;
            }
        } else {
            this.fly = true;
        }
    }

    @EventHandler(priority=-100)
    private void onRotate(RotateEvent event) {
        if (FakeFly.nullCheck()) {
            return;
        }
        if (!this.fly) {
            return;
        }
        if (this.mode.is(Mode.Legit)) {
            return;
        }
        if (!this.rotate.getValue()) {
            return;
        }
        event.setYaw(Sprint.getSprintYaw(FakeFly.mc.field_1724.method_36454()));
        if (FakeFly.mc.field_1690.field_1903.method_1434()) {
            if (MovementUtil.isMoving()) {
                event.setPitch(-(this.pitch.getValueFloat() / 2.0f));
            } else {
                event.setPitch(-this.pitch.getValueFloat());
            }
        } else if (FakeFly.mc.field_1690.field_1832.method_1434()) {
            if (MovementUtil.isMoving()) {
                event.setPitch(this.pitch.getValueFloat() / 2.0f);
            } else {
                event.setPitch(this.pitch.getValueFloat());
            }
        }
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (event.isPre()) {
            return;
        }
        if (FakeFly.nullCheck()) {
            return;
        }
        if (!this.fly) {
            return;
        }
        boolean isUsingFirework = this.getIsUsingFirework();
        if (isUsingFirework || InventoryUtil.findItemInventorySlot(class_1802.field_8639) != -1) {
            class_243 desiredVelocity = new class_243(0.0, 0.0, 0.0);
            double yaw = Math.toRadians(FakeFly.mc.field_1724.method_36454());
            double pitch = Math.toRadians(FakeFly.mc.field_1724.method_36455());
            class_243 direction = new class_243(-Math.sin(yaw) * Math.cos(pitch), -Math.sin(pitch), Math.cos(yaw) * Math.cos(pitch)).method_1029();
            if (FakeFly.mc.field_1690.field_1894.method_1434()) {
                desiredVelocity = desiredVelocity.method_1019(direction.method_18805(this.getHorizontalSpeed() / 20.0, 0.0, this.getHorizontalSpeed() / 20.0));
            }
            if (FakeFly.mc.field_1690.field_1881.method_1434()) {
                desiredVelocity = desiredVelocity.method_1019(direction.method_18805(-this.getHorizontalSpeed() / 20.0, 0.0, -this.getHorizontalSpeed() / 20.0));
            }
            if (FakeFly.mc.field_1690.field_1913.method_1434()) {
                desiredVelocity = desiredVelocity.method_1019(direction.method_18805(this.getHorizontalSpeed() / 20.0, 0.0, this.getHorizontalSpeed() / 20.0).method_1024(1.5707964f));
            }
            if (FakeFly.mc.field_1690.field_1849.method_1434()) {
                desiredVelocity = desiredVelocity.method_1019(direction.method_18805(this.getHorizontalSpeed() / 20.0, 0.0, this.getHorizontalSpeed() / 20.0).method_1024(-1.5707964f));
            }
            if (FakeFly.mc.field_1690.field_1903.method_1434()) {
                desiredVelocity = desiredVelocity.method_1031(0.0, (double)this.verticalSpeed.getValueFloat() / 20.0, 0.0);
            }
            if (FakeFly.mc.field_1690.field_1832.method_1434()) {
                desiredVelocity = desiredVelocity.method_1031(0.0, (double)(-this.verticalSpeed.getValueFloat()) / 20.0, 0.0);
            }
            this.currentVelocity = new class_243(FakeFly.mc.field_1724.method_18798().field_1352, this.currentVelocity.field_1351, FakeFly.mc.field_1724.method_18798().field_1350);
            class_243 velocityDifference = desiredVelocity.method_1020(this.currentVelocity);
            double maxDelta = this.getHorizontalSpeed() / 20.0 / (this.getHorizontalAccelTime() * 20.0);
            if (velocityDifference.method_1027() > maxDelta * maxDelta) {
                velocityDifference = velocityDifference.method_1029().method_1021(maxDelta);
            }
            this.currentVelocity = this.currentVelocity.method_1019(velocityDifference);
            class_238 boundingBox = FakeFly.mc.field_1724.method_5829();
            double playerFeetY = boundingBox.field_1322;
            class_238 groundBox = new class_238(boundingBox.field_1323, playerFeetY - 0.1, boundingBox.field_1321, boundingBox.field_1320, playerFeetY, boundingBox.field_1324);
            for (class_2338 pos : class_2338.method_10094((int)((int)Math.floor(groundBox.field_1323)), (int)((int)Math.floor(groundBox.field_1322)), (int)((int)Math.floor(groundBox.field_1321)), (int)((int)Math.floor(groundBox.field_1320)), (int)((int)Math.floor(groundBox.field_1325)), (int)((int)Math.floor(groundBox.field_1324)))) {
                double blockTopY;
                double distanceToBlock;
                class_2680 blockState = FakeFly.mc.field_1687.method_8320(pos);
                if (!blockState.method_26212((class_1922)FakeFly.mc.field_1687, pos) || !((distanceToBlock = playerFeetY - (blockTopY = (double)pos.method_10264() + 1.0)) >= 0.0) || !(distanceToBlock < 0.1) || !(this.currentVelocity.field_1351 < 0.0)) continue;
                this.currentVelocity = new class_243(this.currentVelocity.field_1352, 0.1, this.currentVelocity.field_1350);
            }
            if (this.armor.getValue()) {
                InventoryUtil.silentSwapEquipElytra();
                this.sync();
            }
            if (!FakeFly.mc.field_1724.method_6128() || this.armor.getValue()) {
                mc.method_1562().method_52787((class_2596)new class_2848((class_1297)FakeFly.mc.field_1724, class_2848.class_2849.field_12982));
            }
            if (!(!this.rocketTimer.passedS(this.fireworkDelay.getValueFloat()) || !this.rocket.getValue() || !MovementUtil.isMoving() && this.key.getValue() || this.usingPause.getValue() && PauseUtil.checkPause(this.same.getValue()))) {
                OffFirework.INSTANCE.off();
                this.rocketTimer.reset();
            }
            if (this.stand.getValue()) {
                FakeFly.mc.field_1724.method_23670();
                FakeFly.mc.field_1724.method_18380(class_4050.field_18076);
            }
            if (this.armor.getValue()) {
                InventoryUtil.silentSwapEquipChestplate();
                this.sync();
            }
        }
    }

    @EventHandler
    private void onPlayerMove(MoveEvent event) {
        if (this.mode.is(Mode.Legit)) {
            return;
        }
        if (!this.fly) {
            return;
        }
        if (this.getIsUsingFirework() || InventoryUtil.findItemInventorySlot(class_1802.field_8639) != -1) {
            if (this.lastMovement == null) {
                this.lastMovement = new class_243(event.getX(), event.getY(), event.getZ());
            }
            class_243 newMovement = this.currentVelocity;
            FakeFly.mc.field_1724.method_18799(newMovement);
            event.setX(newMovement.field_1352);
            event.setY(newMovement.field_1351);
            event.setZ(newMovement.field_1350);
            this.lastMovement = newMovement;
        }
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        if (!this.fly) {
            return;
        }
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2708) {
            class_2708 packet = (class_2708)class_25962;
            if (packet.method_11733().contains(class_2709.field_12400)) {
                this.currentVelocity = new class_243(packet.method_11734(), this.currentVelocity.field_1351, this.currentVelocity.field_1350);
            }
            if (packet.method_11733().contains(class_2709.field_12398)) {
                this.currentVelocity = new class_243(this.currentVelocity.field_1352, packet.method_11735(), this.currentVelocity.field_1350);
            }
            if (packet.method_11733().contains(class_2709.field_12403)) {
                this.currentVelocity = new class_243(this.currentVelocity.field_1352, this.currentVelocity.field_1351, packet.method_11738());
            }
            if (!(packet.method_11733().contains(class_2709.field_12400) || packet.method_11733().contains(class_2709.field_12398) || packet.method_11733().contains(class_2709.field_12403))) {
                if (System.currentTimeMillis() - this.timeOfLastRubberband < 100L) {
                    this.currentVelocity = new class_243(packet.method_11734(), packet.method_11735(), packet.method_11738()).method_1020(this.lastRubberband);
                }
                this.timeOfLastRubberband = System.currentTimeMillis();
                this.lastRubberband = new class_243(packet.method_11734(), packet.method_11735(), packet.method_11738());
            }
        }
    }

    private boolean getIsUsingFirework() {
        boolean usingFirework = false;
        for (class_1297 entity : FakeFly.mc.field_1687.method_18112()) {
            class_1671 firework;
            if (!(entity instanceof class_1671) || (firework = (class_1671)entity).method_24921() == null || !firework.method_24921().equals((Object)FakeFly.mc.field_1724)) continue;
            usingFirework = true;
        }
        return usingFirework;
    }

    private double getHorizontalSpeed() {
        if (FakeFly.mc.field_1690.field_1867.method_1434() && this.sprintToBoost.getValue()) {
            double horizontalVelocity = this.currentVelocity.method_37267();
            return Math.clamp(horizontalVelocity * 1.3 * 20.0, this.horizontalSpeed.getValue(), this.sprintToBoostMaxSpeed.getValue());
        }
        return this.horizontalSpeed.getValue();
    }

    private double getHorizontalAccelTime() {
        return this.currentVelocity.method_37267() > this.horizontalSpeed.getValue() ? this.boostAccelTime.getValue() : this.accelTime.getValue();
    }

    public static enum Mode {
        Control,
        Legit;

    }
}

