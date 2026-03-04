/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$LookAndOnGround
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DoAttackEvent;
import dev.luminous.api.events.impl.FireworkShooterRotationEvent;
import dev.luminous.api.events.impl.InteractBlockEvent;
import dev.luminous.api.events.impl.InteractItemEvent;
import dev.luminous.api.events.impl.JumpEvent;
import dev.luminous.api.events.impl.KeyboardInputEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.RotationEvent;
import dev.luminous.api.events.impl.SendMovementPacketsEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.events.impl.TickMovementEvent;
import dev.luminous.api.events.impl.TravelEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.path.BaritoneUtil;
import dev.luminous.api.utils.rotation.Rotation;
import dev.luminous.asm.accessors.IClientPlayerEntity;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.movement.HoleSnap;
import dev.luminous.mod.modules.impl.player.Freecam;
import dev.luminous.mod.modules.settings.enums.SnapBack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;

public class RotationManager
implements Wrapper {
    public static final Timer ROTATE_TIMER = new Timer();
    public static class_243 directionVec = null;
    public static boolean snapBack = false;
    private static float renderPitch;
    private static float renderYawOffset;
    private static float prevRenderPitch;
    private static float prevRenderYawOffset;
    private static float prevRotationYawHead;
    private static float rotationYawHead;
    public float nextYaw;
    public float nextPitch;
    public float rotationYaw;
    public float rotationPitch;
    public float lastYaw;
    public float lastPitch;
    public class_243 crossHairUpdatePos;
    private int ticksExisted;
    public static float fixYaw;
    public static float fixPitch;
    private float prevYaw;
    private float prevPitch;
    private Rotation rotation;
    private float serverYaw;
    private float serverPitch;
    private float lastServerYaw;
    private float lastServerPitch;
    private float prevJumpYaw;

    public RotationManager() {
        Alien.EVENT_BUS.subscribe(this);
    }

    @EventListener
    public void onInteract(InteractItemEvent event) {
        if (AntiCheat.INSTANCE.interactRotation.getValue() && RotationManager.mc.field_1724 != null) {
            if (event.isPre()) {
                this.snapAt(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455());
            } else {
                this.snapBack();
            }
        }
    }

    @EventListener
    public void onInteract(InteractBlockEvent event) {
        if (AntiCheat.INSTANCE.interactRotation.getValue() && RotationManager.mc.field_1724 != null) {
            if (event.isPre()) {
                this.snapAt(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455());
            } else {
                this.snapBack();
            }
        }
    }

    @EventListener
    public void doAttack(DoAttackEvent event) {
        if (AntiCheat.INSTANCE.interactRotation.getValue() && RotationManager.mc.field_1724 != null) {
            if (event.isPre()) {
                this.snapAt(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455());
            } else {
                this.snapBack();
            }
        }
    }

    public void snapBack() {
        if (AntiCheat.INSTANCE.snapBackEnum.is(SnapBack.Force)) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2830(RotationManager.mc.field_1724.method_23317(), RotationManager.mc.field_1724.method_23318(), RotationManager.mc.field_1724.method_23321(), this.rotationYaw, this.rotationPitch, RotationManager.mc.field_1724.method_24828()));
        } else if (AntiCheat.INSTANCE.snapBackEnum.is(SnapBack.Tick)) {
            snapBack = true;
        }
    }

    public void lookAt(class_243 directionVec) {
        this.rotationTo(directionVec);
        this.snapAt(directionVec);
    }

    public void lookAt(class_2338 pos, class_2350 side) {
        class_243 hitVec = pos.method_46558().method_1019(new class_243((double)side.method_10163().method_10263() * 0.5, (double)side.method_10163().method_10264() * 0.5, (double)side.method_10163().method_10260() * 0.5));
        this.lookAt(hitVec);
    }

    public void snapAt(float yaw, float pitch) {
        this.setRenderRotation(yaw, pitch, true);
        if (AntiCheat.INSTANCE.grimRotation.getValue()) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2830(RotationManager.mc.field_1724.method_23317(), RotationManager.mc.field_1724.method_23318(), RotationManager.mc.field_1724.method_23321(), yaw, pitch, RotationManager.mc.field_1724.method_24828()));
        } else {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2831(yaw, pitch, RotationManager.mc.field_1724.method_24828()));
        }
    }

    public void snapAt(class_243 directionVec) {
        float[] angle = RotationManager.getRotation(directionVec);
        this.snapAt(angle[0], angle[1]);
    }

    public void rotationTo(class_243 vec3d) {
        ROTATE_TIMER.reset();
        directionVec = vec3d;
    }

    public boolean inFov(class_243 directionVec, float fov) {
        float[] angle = RotationManager.getRotation(this.crossHairUpdatePos != null ? this.crossHairUpdatePos : new class_243(RotationManager.mc.field_1724.method_23317(), RotationManager.mc.field_1724.method_23318() + (double)RotationManager.mc.field_1724.method_18381(RotationManager.mc.field_1724.method_18376()), RotationManager.mc.field_1724.method_23321()), directionVec);
        return this.inFov(angle[0], angle[1], fov);
    }

    public boolean inFov(float yaw, float pitch, float fov) {
        float pitchDifferent;
        float yawDifferent = class_3532.method_15356((float)yaw, (float)this.rotationYaw);
        return yawDifferent * yawDifferent + (pitchDifferent = Math.abs(pitch - this.rotationPitch)) * pitchDifferent <= fov * fov;
    }

    @EventListener
    public void onTickMovement(TickMovementEvent event) {
        if (RotationManager.mc.field_1724 != null) {
            this.crossHairUpdatePos = new class_243(RotationManager.mc.field_1724.method_23317(), RotationManager.mc.field_1724.method_23318() + (double)RotationManager.mc.field_1724.method_18381(RotationManager.mc.field_1724.method_18376()), RotationManager.mc.field_1724.method_23321());
        }
    }

    @EventListener
    public void update(SendMovementPacketsEvent event) {
        if (AntiCheat.INSTANCE.movementSync() && !BaritoneUtil.isActive()) {
            event.setYaw(this.nextYaw);
            event.setPitch(this.nextPitch);
        } else {
            UpdateRotateEvent updateRotateEvent = UpdateRotateEvent.get(event.getYaw(), event.getPitch());
            Alien.EVENT_BUS.post(updateRotateEvent);
            event.setYaw(updateRotateEvent.getYaw());
            event.setPitch(updateRotateEvent.getPitch());
        }
    }

    @EventListener(priority=999)
    public void update(TickMovementEvent event) {
        if (RotationManager.mc.field_1724 != null && AntiCheat.INSTANCE.movementSync() && !BaritoneUtil.isActive()) {
            UpdateRotateEvent updateRotateEvent = UpdateRotateEvent.get(RotationManager.mc.field_1724.method_36454(), RotationManager.mc.field_1724.method_36455());
            Alien.EVENT_BUS.post(updateRotateEvent);
            this.nextYaw = updateRotateEvent.getYaw();
            this.nextPitch = updateRotateEvent.getPitch();
            fixYaw = this.nextYaw;
            fixPitch = this.nextPitch;
        }
    }

    @EventListener(priority=-200)
    public void onLastRotation(UpdateRotateEvent event) {
        RotationEvent rotationEvent = RotationEvent.get();
        Alien.EVENT_BUS.post(rotationEvent);
        if (rotationEvent.getRotation()) {
            float[] newAngle = this.injectStep(new float[]{rotationEvent.getYaw(), rotationEvent.getPitch()}, rotationEvent.getSpeed());
            event.setYaw(newAngle[0]);
            event.setPitch(newAngle[1]);
        } else if (rotationEvent.getTarget() != null) {
            float[] newAngle = this.injectStep(rotationEvent.getTarget(), rotationEvent.getSpeed());
            event.setYaw(newAngle[0]);
            event.setPitch(newAngle[1]);
        } else if (!event.isModified() && AntiCheat.INSTANCE.look.getValue() && directionVec != null && !ROTATE_TIMER.passed((long)(AntiCheat.INSTANCE.rotateTime.getValue() * 1000.0))) {
            float[] newAngle = this.injectStep(directionVec, AntiCheat.INSTANCE.steps.getValueFloat());
            event.setYaw(newAngle[0]);
            event.setPitch(newAngle[1]);
        }
    }

    @EventListener
    public void travel(TravelEvent e) {
        if (AntiCheat.INSTANCE.movementSync() && !BaritoneUtil.isActive() && !RotationManager.mc.field_1724.method_3144()) {
            if (e.isPre()) {
                this.prevYaw = RotationManager.mc.field_1724.method_36454();
                this.prevPitch = RotationManager.mc.field_1724.method_36455();
                RotationManager.mc.field_1724.method_36456(fixYaw);
                RotationManager.mc.field_1724.method_36457(fixPitch);
            } else {
                RotationManager.mc.field_1724.method_36456(this.prevYaw);
                RotationManager.mc.field_1724.method_36457(this.prevPitch);
            }
        }
    }

    @EventListener
    public void onJump(JumpEvent e) {
        if (AntiCheat.INSTANCE.movementSync() && !BaritoneUtil.isActive() && !RotationManager.mc.field_1724.method_3144()) {
            if (e.isPre()) {
                this.prevYaw = RotationManager.mc.field_1724.method_36454();
                this.prevPitch = RotationManager.mc.field_1724.method_36455();
                RotationManager.mc.field_1724.method_36456(fixYaw);
                RotationManager.mc.field_1724.method_36457(fixPitch);
            } else {
                RotationManager.mc.field_1724.method_36456(this.prevYaw);
                RotationManager.mc.field_1724.method_36457(this.prevPitch);
            }
        }
    }

    @EventListener
    public void onFirework(FireworkShooterRotationEvent event) {
        if (AntiCheat.INSTANCE.movementSync() && !BaritoneUtil.isActive() && event.shooter == RotationManager.mc.field_1724) {
            event.yaw = fixYaw;
            event.pitch = fixPitch;
            event.cancel();
        }
    }

    @EventListener(priority=-999)
    public void onKeyInput(KeyboardInputEvent e) {
        if (!(!AntiCheat.INSTANCE.movementSync() || BaritoneUtil.isActive() || HoleSnap.INSTANCE.isOn() || RotationManager.mc.field_1724.method_3144() || Freecam.INSTANCE.isOn())) {
            float mF = RotationManager.mc.field_1724.field_3913.field_3905;
            float mS = RotationManager.mc.field_1724.field_3913.field_3907;
            float delta = (RotationManager.mc.field_1724.method_36454() - fixYaw) * ((float)Math.PI / 180);
            float cos = class_3532.method_15362((float)delta);
            float sin = class_3532.method_15374((float)delta);
            RotationManager.mc.field_1724.field_3913.field_3907 = Math.round(mS * cos - mF * sin);
            RotationManager.mc.field_1724.field_3913.field_3905 = Math.round(mF * cos + mS * sin);
        }
    }

    public float[] injectStep(class_243 vec, float steps) {
        float currentYaw = AntiCheat.INSTANCE.serverSide.getValue() ? this.getLastYaw() : this.rotationYaw;
        float currentPitch = AntiCheat.INSTANCE.serverSide.getValue() ? this.getLastPitch() : this.rotationPitch;
        float yawDelta = class_3532.method_15393((float)((float)class_3532.method_15338((double)(Math.toDegrees(Math.atan2(vec.field_1350 - RotationManager.mc.field_1724.method_23321(), vec.field_1352 - RotationManager.mc.field_1724.method_23317())) - 90.0)) - currentYaw));
        float pitchDelta = (float)(-Math.toDegrees(Math.atan2(vec.field_1351 - (RotationManager.mc.field_1724.method_19538().field_1351 + (double)RotationManager.mc.field_1724.method_18381(RotationManager.mc.field_1724.method_18376())), Math.sqrt(Math.pow(vec.field_1352 - RotationManager.mc.field_1724.method_23317(), 2.0) + Math.pow(vec.field_1350 - RotationManager.mc.field_1724.method_23321(), 2.0))))) - currentPitch;
        if (AntiCheat.INSTANCE.random.getValue()) {
            float angleToRad = (float)Math.toRadians(27 * (RotationManager.mc.field_1724.field_6012 % 30));
            yawDelta = (float)((double)yawDelta + Math.sin(angleToRad) * 3.0) + MathUtil.random(-1.0f, 1.0f);
            pitchDelta += MathUtil.random(-0.6f, 0.6f);
        }
        if (yawDelta > 180.0f) {
            yawDelta -= 180.0f;
        }
        float yawStepVal = 180.0f * steps;
        float clampedYawDelta = class_3532.method_15363((float)class_3532.method_15379((float)yawDelta), (float)(-yawStepVal), (float)yawStepVal);
        float clampedPitchDelta = class_3532.method_15363((float)pitchDelta, (float)-45.0f, (float)45.0f);
        float newYaw = currentYaw + (yawDelta > 0.0f ? clampedYawDelta : -clampedYawDelta);
        float newPitch = class_3532.method_15363((float)(currentPitch + clampedPitchDelta), (float)-90.0f, (float)90.0f);
        return new float[]{newYaw, newPitch};
    }

    public float[] injectStep(float[] angle, float steps) {
        float currentYaw = AntiCheat.INSTANCE.serverSide.getValue() ? this.getLastYaw() : this.rotationYaw;
        float currentPitch = AntiCheat.INSTANCE.serverSide.getValue() ? this.getLastPitch() : this.rotationPitch;
        float yawDelta = class_3532.method_15393((float)(angle[0] - currentYaw));
        float pitchDelta = angle[1] - currentPitch;
        if (AntiCheat.INSTANCE.random.getValue()) {
            float angleToRad = (float)Math.toRadians(27 * (RotationManager.mc.field_1724.field_6012 % 30));
            yawDelta = (float)((double)yawDelta + Math.sin(angleToRad) * 3.0) + MathUtil.random(-1.0f, 1.0f);
            pitchDelta += MathUtil.random(-0.6f, 0.6f);
        }
        if (yawDelta > 180.0f) {
            yawDelta -= 180.0f;
        }
        float yawStepVal = 180.0f * steps;
        float pitchStepVal = 90.0f * steps;
        float clampedYawDelta = class_3532.method_15363((float)class_3532.method_15379((float)yawDelta), (float)(-yawStepVal), (float)yawStepVal);
        float clampedPitchDelta = class_3532.method_15363((float)pitchDelta, (float)(-pitchStepVal), (float)pitchStepVal);
        float newYaw = currentYaw + (yawDelta > 0.0f ? clampedYawDelta : -clampedYawDelta);
        float newPitch = class_3532.method_15363((float)(currentPitch + clampedPitchDelta), (float)-90.0f, (float)90.0f);
        return new float[]{newYaw, newPitch};
    }

    @EventListener(priority=-999)
    public void onPacketSend(PacketEvent.Sent event) {
        class_2828 packet;
        class_2596<?> class_25962;
        if (RotationManager.mc.field_1724 != null && (class_25962 = event.getPacket()) instanceof class_2828 && (packet = (class_2828)class_25962).method_36172()) {
            this.setLastYaw(packet.method_12271(this.getLastYaw()));
            this.setLastPitch(packet.method_12270(this.getLastPitch()));
            this.setRenderRotation(this.getLastYaw(), this.getLastPitch(), ClientSetting.INSTANCE.sync.getValue());
        }
    }

    @EventListener
    public void onUpdateWalkingPost(TickEvent event) {
        if (event.isPost()) {
            this.setRenderRotation(this.getLastYaw(), this.getLastPitch(), false);
        }
    }

    public void setRenderRotation(float yaw, float pitch, boolean force) {
        if (RotationManager.mc.field_1724 != null && (RotationManager.mc.field_1724.field_6012 != this.ticksExisted || force)) {
            this.ticksExisted = RotationManager.mc.field_1724.field_6012;
            prevRenderPitch = renderPitch;
            prevRenderYawOffset = renderYawOffset;
            renderYawOffset = this.getRenderYawOffset(yaw, prevRenderYawOffset);
            prevRotationYawHead = rotationYawHead;
            rotationYawHead = yaw;
            renderPitch = pitch;
        }
    }

    private float getRenderYawOffset(float yaw, float offsetIn) {
        float offset;
        double zDif;
        float result = offsetIn;
        double xDif = RotationManager.mc.field_1724.method_23317() - RotationManager.mc.field_1724.field_6014;
        if (xDif * xDif + (zDif = RotationManager.mc.field_1724.method_23321() - RotationManager.mc.field_1724.field_5969) * zDif > 0.002500000176951289) {
            offset = (float)class_3532.method_15349((double)zDif, (double)xDif) * 57.295776f - 90.0f;
            float wrap = class_3532.method_15379((float)(class_3532.method_15393((float)yaw) - offset));
            result = 95.0f < wrap && wrap < 265.0f ? offset - 180.0f : offset;
        }
        if (RotationManager.mc.field_1724.field_6251 > 0.0f) {
            result = yaw;
        }
        if ((offset = class_3532.method_15393((float)(yaw - (result = offsetIn + class_3532.method_15393((float)(result - offsetIn)) * 0.3f)))) < -75.0f) {
            offset = -75.0f;
        } else if (offset >= 75.0f) {
            offset = 75.0f;
        }
        result = yaw - offset;
        if (offset * offset > 2500.0f) {
            result += offset * 0.2f;
        }
        return result;
    }

    public static float[] getRotation(class_243 eyesPos, class_243 vec) {
        double diffX = vec.field_1352 - eyesPos.field_1352;
        double diffY = vec.field_1351 - eyesPos.field_1351;
        double diffZ = vec.field_1350 - eyesPos.field_1350;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{class_3532.method_15393((float)yaw), class_3532.method_15393((float)pitch)};
    }

    public static float[] getRotation(class_243 vec) {
        class_243 eyesPos = RotationManager.mc.field_1724.method_33571();
        return RotationManager.getRotation(eyesPos, vec);
    }

    public static float getRenderPitch() {
        return renderPitch;
    }

    public static float getRotationYawHead() {
        return rotationYawHead;
    }

    public static float getRenderYawOffset() {
        return renderYawOffset;
    }

    public static float getPrevRenderPitch() {
        return prevRenderPitch;
    }

    public static float getPrevRotationYawHead() {
        return prevRotationYawHead;
    }

    public static float getPrevRenderYawOffset() {
        return prevRenderYawOffset;
    }

    public float getLastYaw() {
        return this.lastYaw;
    }

    public void setLastYaw(float lastYaw) {
        this.lastYaw = lastYaw;
        if (AntiCheat.INSTANCE.forceSync.getValue() && Alien.SERVER.playerNull.passedS(0.15)) {
            ((IClientPlayerEntity)RotationManager.mc.field_1724).setLastYaw(lastYaw);
        }
    }

    public float getLastPitch() {
        return this.lastPitch;
    }

    public void setLastPitch(float lastPitch) {
        this.lastPitch = lastPitch;
        if (AntiCheat.INSTANCE.forceSync.getValue() && Alien.SERVER.playerNull.passedS(0.15)) {
            ((IClientPlayerEntity)RotationManager.mc.field_1724).setLastPitch(lastPitch);
        }
    }

    public float getServerYaw() {
        return this.serverYaw;
    }

    public float getServerPitch() {
        return this.serverPitch;
    }

    public float getRotationYaw() {
        return this.rotation.getYaw();
    }

    public float getRotationPitch() {
        return this.rotation.getPitch();
    }

    public boolean isRotating() {
        return this.rotation != null;
    }
}

