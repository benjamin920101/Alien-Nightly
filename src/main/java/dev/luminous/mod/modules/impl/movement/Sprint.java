/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
 *  net.minecraft.util.math.MathHelper
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.SprintEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.events.impl.TickMovementEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.path.BaritoneUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.movement.HoleSnap;
import dev.luminous.mod.modules.impl.player.Freecam;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;

public class Sprint
extends Module {
    public static Sprint INSTANCE;
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Legit));
    public final BooleanSetting inWaterPause = this.add(new BooleanSetting("InWaterPause", true));
    public final BooleanSetting inWebPause = this.add(new BooleanSetting("InWebPause", true));
    public final BooleanSetting sneakingPause = this.add(new BooleanSetting("SneakingPause", false));
    public final BooleanSetting blindnessPause = this.add(new BooleanSetting("BlindnessPause", false));
    public final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", false));
    public final BooleanSetting lagPause = this.add(new BooleanSetting("LagPause", true));
    boolean pause = false;

    public Sprint() {
        super("Sprint", "Permanently keeps player in sprinting mode.", Module.Category.Movement);
        this.setChinese("\u5f3a\u5236\u75be\u8dd1");
        INSTANCE = this;
    }

    public static float getSprintYaw(float yaw) {
        if (Sprint.mc.field_1690.field_1894.method_1434() && !Sprint.mc.field_1690.field_1881.method_1434()) {
            if (Sprint.mc.field_1690.field_1913.method_1434() && !Sprint.mc.field_1690.field_1849.method_1434()) {
                yaw -= 45.0f;
            } else if (Sprint.mc.field_1690.field_1849.method_1434() && !Sprint.mc.field_1690.field_1913.method_1434()) {
                yaw += 45.0f;
            }
        } else if (Sprint.mc.field_1690.field_1881.method_1434() && !Sprint.mc.field_1690.field_1894.method_1434()) {
            yaw += 180.0f;
            if (Sprint.mc.field_1690.field_1913.method_1434() && !Sprint.mc.field_1690.field_1849.method_1434()) {
                yaw += 45.0f;
            } else if (Sprint.mc.field_1690.field_1849.method_1434() && !Sprint.mc.field_1690.field_1913.method_1434()) {
                yaw -= 45.0f;
            }
        } else if (Sprint.mc.field_1690.field_1913.method_1434() && !Sprint.mc.field_1690.field_1849.method_1434()) {
            yaw -= 90.0f;
        } else if (Sprint.mc.field_1690.field_1849.method_1434() && !Sprint.mc.field_1690.field_1913.method_1434()) {
            yaw += 90.0f;
        }
        return class_3532.method_15393((float)yaw);
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @EventListener
    public void onPacket(PacketEvent.Receive event) {
        if (this.lagPause.getValue() && event.getPacket() instanceof class_2708) {
            this.pause = true;
        }
    }

    public boolean inWater() {
        return this.inWaterPause.getValue() && Sprint.mc.field_1724.method_52535();
    }

    @EventListener
    public void onMove(TickMovementEvent event) {
        if (!BaritoneUtil.isPathing() && !this.inWater()) {
            if (this.mode.getValue() == Mode.PressKey) {
                Sprint.mc.field_1690.field_1867.method_23481(true);
            } else {
                Sprint.mc.field_1724.method_5728(this.shouldSprint());
            }
        }
    }

    @EventListener
    public void tick(TickEvent event) {
        if (event.isPost()) {
            this.pause = false;
        }
    }

    @EventListener
    public void sprint(SprintEvent event) {
        if (!(BaritoneUtil.isPathing() || this.mode.is(Mode.PressKey) || this.inWater())) {
            event.cancel();
            event.setSprint(this.shouldSprint());
        }
    }

    private boolean shouldSprint() {
        if (!(Sprint.mc.field_1724.method_7344().method_7586() <= 6 && !Sprint.mc.field_1724.method_7337() || !MovementUtil.isMoving() || this.pause || Sprint.mc.field_1724.method_5715() && this.sneakingPause.getValue() || Alien.PLAYER.isInWeb((class_1657)Sprint.mc.field_1724) && this.inWebPause.getValue() || Sprint.mc.field_1724.method_6115() && this.usingPause.getValue() || Sprint.mc.field_1724.method_3144() || Sprint.mc.field_1724.method_6059(class_1294.field_5919) && this.blindnessPause.getValue())) {
            switch (this.mode.getValue().ordinal()) {
                case 1: {
                    if (AntiCheat.INSTANCE.movementSync()) {
                        return Sprint.mc.field_1724.field_3913.field_3905 > 0.0f;
                    }
                    return HoleSnap.INSTANCE.isOn() || Sprint.mc.field_1690.field_1894.method_1434() && class_3532.method_15356((float)Sprint.mc.field_1724.method_36454(), (float)Alien.ROTATION.rotationYaw) < 40.0f;
                }
                case 2: {
                    return true;
                }
                case 3: {
                    if (AntiCheat.INSTANCE.movementSync()) {
                        return Sprint.mc.field_1724.field_3913.field_3905 > 0.0f;
                    }
                    return HoleSnap.INSTANCE.isOn() || class_3532.method_15356((float)Sprint.getSprintYaw(Sprint.mc.field_1724.method_36454()), (float)Alien.ROTATION.rotationYaw) < 40.0f;
                }
            }
        }
        return false;
    }

    @EventListener(priority=-100)
    public void rotate(UpdateRotateEvent event) {
        if (!(BaritoneUtil.isPathing() || (Sprint.mc.field_1724.method_7344().method_7586() <= 6 && !Sprint.mc.field_1724.method_7337() || !MovementUtil.isMoving() || Freecam.INSTANCE.isOn() || Sprint.mc.field_1724.method_6128() || Alien.PLAYER.isInWeb((class_1657)Sprint.mc.field_1724) && this.inWebPause.getValue() || Sprint.mc.field_1724.method_5715() && this.sneakingPause.getValue() || Sprint.mc.field_1724.method_3144() || Sprint.mc.field_1724.method_6115() && this.usingPause.getValue() || Sprint.mc.field_1724.method_52535() || !Freecam.INSTANCE.isOff() || Sprint.mc.field_1724.method_6059(class_1294.field_5919)) && this.blindnessPause.getValue() || !this.mode.is(Mode.Rotation) || event.isModified())) {
            event.setYaw(Sprint.getSprintYaw(Sprint.mc.field_1724.method_36454()));
        }
    }

    public static enum Mode {
        PressKey,
        Legit,
        Rage,
        Rotation;

    }
}

