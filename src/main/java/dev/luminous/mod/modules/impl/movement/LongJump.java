/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1713
 *  net.minecraft.class_1770
 *  net.minecraft.class_1799
 *  net.minecraft.class_238
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2708
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 *  net.minecraft.class_434
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.Event;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ElytraTransformEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.PlayerMoveEvent;
import dev.luminous.api.events.impl.PlayerUpdateEvent;
import dev.luminous.api.events.impl.TickEvent;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.string.EnumFormatter;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.DisablerModule;
import dev.luminous.mod.modules.impl.movement.Fly;
import dev.luminous.mod.modules.impl.movement.PacketFly;
import dev.luminous.mod.modules.impl.movement.Speed;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1770;
import net.minecraft.class_1799;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_434;

public class LongJump
extends Module {
    private static LongJump INSTANCE;
    EnumSetting<JumpMode> modeConfig = this.add(new EnumSetting<JumpMode>("Mode", JumpMode.NORMAL));
    SliderSetting boostConfig = this.add(new SliderSetting("Boost", (double)0.1f, 4.5, 10.0, () -> this.modeConfig.getValue() == JumpMode.NORMAL));
    BooleanSetting elytraBoostConfig = this.add(new BooleanSetting("VanillaBoost", false, () -> this.modeConfig.getValue() == JumpMode.GRIM));
    BooleanSetting autoDisableConfig = this.add(new BooleanSetting("AutoDisable", true));
    private int stage;
    private double distance;
    private double speed;
    private int airTicks;
    private int groundTicks;

    public LongJump() {
        super("LongJump", Module.Category.Movement);
        this.setChinese("\u8fdc\u8df3");
        INSTANCE = this;
    }

    public static LongJump getInstance() {
        return INSTANCE;
    }

    public String getModuleData() {
        return EnumFormatter.formatEnum(this.modeConfig.getValue());
    }

    @Override
    public boolean onEnable() {
        this.groundTicks = 0;
        return false;
    }

    @Override
    public void onDisable() {
        this.stage = 0;
        this.distance = 0.0;
    }

    @EventListener
    public void onTravel(TickEvent event) {
        if (LongJump.nullCheck()) {
            return;
        }
        double dx = LongJump.mc.field_1724.method_23317() - LongJump.mc.field_1724.field_6014;
        double dz = LongJump.mc.field_1724.method_23321() - LongJump.mc.field_1724.field_5969;
        this.distance = Math.sqrt(dx * dx + dz * dz);
        if (this.modeConfig.getValue() == JumpMode.GRIM) {
            class_238 bb = LongJump.mc.field_1724.method_5829();
            boolean shouldFall = false;
            for (double i = 0.0; i < 2.0; i += 0.01) {
                if (LongJump.mc.field_1687.method_8587((class_1297)LongJump.mc.field_1724, bb.method_989(0.0, -i, 0.0))) continue;
                shouldFall = true;
                break;
            }
            if (!shouldFall) {
                return;
            }
            int elytraSlot = -1;
            for (int i = 0; i < 36; ++i) {
                class_1799 stack = LongJump.mc.field_1724.method_31548().method_5438(i);
                if (!(stack.method_7909() instanceof class_1770)) continue;
                elytraSlot = i;
                break;
            }
            if (elytraSlot == -1) {
                return;
            }
            if (LongJump.mc.field_1724.method_24828()) {
                LongJump.mc.field_1724.method_6043();
            } else if (LongJump.mc.field_1724.method_18798().field_1351 < 0.0 && !LongJump.mc.field_1724.method_6128()) {
                Alien.INVENTORY.click(elytraSlot < 9 ? elytraSlot + 36 : elytraSlot, 0, class_1713.field_7790);
                Alien.INVENTORY.click(6, 0, class_1713.field_7790);
                Alien.INVENTORY.click(elytraSlot < 9 ? elytraSlot + 36 : elytraSlot, 0, class_1713.field_7790);
                Alien.NETWORK.sendPacket((class_2596<?>)new class_2848((class_1297)LongJump.mc.field_1724, class_2848.class_2849.field_12982));
                LongJump.mc.field_1724.method_23669();
                Alien.INVENTORY.click(6, 0, class_1713.field_7790);
                Alien.INVENTORY.click(elytraSlot < 9 ? elytraSlot + 36 : elytraSlot, 0, class_1713.field_7790);
                Alien.INVENTORY.click(6, 0, class_1713.field_7790);
            }
            if (this.elytraBoostConfig.getValue()) {
                if (!LongJump.mc.field_1724.method_6128() || LongJump.mc.field_1724.method_5799() || LongJump.mc.field_1724.method_5771() || (float)LongJump.mc.field_1724.method_7344().method_7586() <= 6.0f) {
                    return;
                }
                if (LongJump.mc.field_1724.method_18798().field_1351 < 0.0) {
                    double d4 = (double)0.014f * Math.cos(Math.toRadians(LongJump.mc.field_1724.method_36454() + 90.0f));
                    double d5 = (double)0.014f * Math.sin(Math.toRadians(LongJump.mc.field_1724.method_36454() + 90.0f));
                    class_243 glide = new class_243(d4, 0.0, d5);
                    class_243 motion = LongJump.mc.field_1724.method_18798();
                    Alien.MOVEMENT.setMotionXZ(motion.field_1352 + glide.field_1352, motion.field_1350 + glide.field_1350);
                }
            }
        }
    }

    @EventListener
    public void onElytraTransform(ElytraTransformEvent event) {
        if (this.modeConfig.getValue() == JumpMode.GRIM && event.getEntity() == LongJump.mc.field_1724) {
            event.cancel();
        }
    }

    @EventListener
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.modeConfig.getValue() == JumpMode.NORMAL) {
            double amplifier;
            if (LongJump.mc.field_1724 == null || LongJump.mc.field_1687 == null || Fly.getInstance().onEnable() || DisablerModule.getInstance().grimFireworkCheck() || PacketFly.INSTANCE.onEnable() || !MovementUtil.isInputtingMovement()) {
                return;
            }
            double speedEffect = 1.0;
            double slowEffect = 1.0;
            if (LongJump.mc.field_1724.method_6059(class_1294.field_5904)) {
                amplifier = LongJump.mc.field_1724.method_6112(class_1294.field_5904).method_5578();
                speedEffect = 1.0 + 0.2 * (amplifier + 1.0);
            }
            if (LongJump.mc.field_1724.method_6059(class_1294.field_5909)) {
                amplifier = LongJump.mc.field_1724.method_6112(class_1294.field_5909).method_5578();
                slowEffect = 1.0 + 0.2 * (amplifier + 1.0);
            }
            double base = (double)0.2873f * speedEffect / slowEffect;
            if (this.stage == 0) {
                this.stage = 1;
                this.speed = this.boostConfig.getValue() * base - 0.01;
            } else if (this.stage == 1) {
                this.stage = 2;
                Alien.MOVEMENT.setMotionY(0.42);
                event.setY(0.42);
                this.speed *= 2.149;
            } else if (this.stage == 2) {
                this.stage = 3;
                double moveSpeed = 0.66 * (this.distance - base);
                this.speed = this.distance - moveSpeed;
            } else {
                if (!LongJump.mc.field_1687.method_8587((class_1297)LongJump.mc.field_1724, LongJump.mc.field_1724.method_5829().method_989(0.0, LongJump.mc.field_1724.method_18798().method_10214(), 0.0)) || LongJump.mc.field_1724.field_5992) {
                    this.stage = 0;
                }
                this.speed = this.distance - this.distance / 159.0;
            }
            this.speed = Math.max(this.speed, base);
            event.cancel();
            class_241 motion = Speed.INSTANCE.handleStrafeMotion((float)this.speed);
            event.setX(motion.field_1343);
            event.setZ(motion.field_1342);
        }
    }

    @EventListener
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        if (Event.Stage.Pre == Event.Stage.Pre && this.modeConfig.getValue() == JumpMode.GLIDE) {
            if (Fly.getInstance().onEnable() || LongJump.mc.field_1724.method_6128() || LongJump.mc.field_1724.method_21754() || LongJump.mc.field_1724.method_5799()) {
                return;
            }
            if (LongJump.mc.field_1724.method_24828()) {
                this.distance = 0.0;
            }
            float direction = LongJump.mc.field_1724.method_36454() + (float)(LongJump.mc.field_1724.field_6250 < 0.0f ? 180 : 0) + (LongJump.mc.field_1724.field_6212 > 0.0f ? -90.0f * (LongJump.mc.field_1724.field_6250 < 0.0f ? -0.5f : (LongJump.mc.field_1724.field_6250 > 0.0f ? 0.5f : 1.0f)) : 0.0f) - (LongJump.mc.field_1724.field_6212 < 0.0f ? -90.0f * (LongJump.mc.field_1724.field_6250 < 0.0f ? -0.5f : (LongJump.mc.field_1724.field_6250 > 0.0f ? 0.5f : 1.0f)) : 0.0f);
            float dx = (float)Math.cos((double)(direction + 90.0f) * Math.PI / 180.0);
            float dz = (float)Math.sin((double)(direction + 90.0f) * Math.PI / 180.0);
            if (!LongJump.mc.field_1724.field_5992) {
                ++this.airTicks;
                if (LongJump.mc.field_1724.field_3913.field_3903) {
                    LongJump.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(0.0, 2.147483647E9, 0.0, false));
                }
                this.groundTicks = 0;
                if (!LongJump.mc.field_1724.field_5992) {
                    if (LongJump.mc.field_1724.method_18798().field_1351 == -0.07190068807140403) {
                        Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.35f);
                    }
                    if (LongJump.mc.field_1724.method_18798().field_1351 == -0.10306193759436909) {
                        Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.55f);
                    }
                    if (LongJump.mc.field_1724.method_18798().field_1351 == -0.13395038817442878) {
                        Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.67f);
                    }
                    if (LongJump.mc.field_1724.method_18798().field_1351 == -0.16635183030382) {
                        Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.69f);
                    }
                    if (LongJump.mc.field_1724.method_18798().field_1351 == -0.19088711097794803) {
                        Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.71f);
                    }
                    if (LongJump.mc.field_1724.method_18798().field_1351 == -0.21121925191528862) {
                        Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.2f);
                    }
                    if (LongJump.mc.field_1724.method_18798().field_1351 == -0.11979897632390576) {
                        Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.93f);
                    }
                    if (LongJump.mc.field_1724.method_18798().field_1351 == -0.18758479151225355) {
                        Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.72f);
                    }
                    if (LongJump.mc.field_1724.method_18798().field_1351 == -0.21075983825251726) {
                        Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.76f);
                    }
                    if (this.getJumpCollisions((class_1657)LongJump.mc.field_1724, 70.0) < 0.5) {
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.23537393014173347) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.03f);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.08531999505205401) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * -0.5);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.03659320313669756) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)-0.1f);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.07481386749524899) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)-0.07f);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.0732677700939672) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)-0.05f);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.07480988066790395) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)-0.04f);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.0784000015258789) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.1f);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.08608320193943977) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.1f);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.08683615560584318) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.05f);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.08265497329678266) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.05f);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.08245009535659828) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * (double)0.05f);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.08244005633718426) {
                            Alien.MOVEMENT.setMotionY(-0.08243956442521608);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 == -0.08243956442521608) {
                            Alien.MOVEMENT.setMotionY(-0.08244005590677261);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 > -0.1 && LongJump.mc.field_1724.method_18798().field_1351 < -0.08 && !LongJump.mc.field_1724.method_24828() && LongJump.mc.field_1724.field_3913.field_3910) {
                            Alien.MOVEMENT.setMotionY(-1.0E-4f);
                        }
                    } else {
                        if (LongJump.mc.field_1724.method_18798().field_1351 < -0.2 && LongJump.mc.field_1724.method_18798().field_1351 > -0.24) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * 0.7);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 < -0.25 && LongJump.mc.field_1724.method_18798().field_1351 > -0.32) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * 0.8);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 < -0.35 && LongJump.mc.field_1724.method_18798().field_1351 > -0.8) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * 0.98);
                        }
                        if (LongJump.mc.field_1724.method_18798().field_1351 < -0.8 && LongJump.mc.field_1724.method_18798().field_1351 > -1.6) {
                            Alien.MOVEMENT.setMotionY(LongJump.mc.field_1724.method_18798().field_1351 * 0.99);
                        }
                    }
                }
                Alien.TICK.setClientTick(0.85f);
                double[] jumpFactor = new double[]{0.420606, 0.417924, 0.415258, 0.412609, 0.409977, 0.407361, 0.404761, 0.402178, 0.399611, 0.39706, 0.394525, 0.392, 0.3894, 0.38644, 0.383655, 0.381105, 0.37867, 0.37625, 0.37384, 0.37145, 0.369, 0.3666, 0.3642, 0.3618, 0.35945, 0.357, 0.354, 0.351, 0.348, 0.345, 0.342, 0.339, 0.336, 0.333, 0.33, 0.327, 0.324, 0.321, 0.318, 0.315, 0.312, 0.309, 0.307, 0.305, 0.303, 0.3, 0.297, 0.295, 0.293, 0.291, 0.289, 0.287, 0.285, 0.283, 0.281, 0.279, 0.277, 0.275, 0.273, 0.271, 0.269, 0.267, 0.265, 0.263, 0.261, 0.259, 0.257, 0.255, 0.253, 0.251, 0.249, 0.247, 0.245, 0.243, 0.241, 0.239, 0.237};
                if (LongJump.mc.field_1724.field_3913.field_3910) {
                    try {
                        Alien.MOVEMENT.setMotionXZ((double)dx * jumpFactor[this.airTicks - 1] * 3.0, (double)dz * jumpFactor[this.airTicks - 1] * 3.0);
                    }
                    catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                        // empty catch block
                    }
                    return;
                }
                Alien.MOVEMENT.setMotionXZ(0.0, 0.0);
                return;
            }
            Alien.TICK.setClientTick(1.0f);
            this.airTicks = 0;
            ++this.groundTicks;
            Alien.MOVEMENT.setMotionXZ(LongJump.mc.field_1724.method_18798().field_1352 / 13.0, LongJump.mc.field_1724.method_18798().field_1350 / 13.0);
            if (this.groundTicks == 1) {
                LongJump.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(LongJump.mc.field_1724.method_23317(), LongJump.mc.field_1724.method_23318(), LongJump.mc.field_1724.method_23321(), LongJump.mc.field_1724.method_24828()));
                LongJump.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(LongJump.mc.field_1724.method_23317() + 0.0624, LongJump.mc.field_1724.method_23318(), LongJump.mc.field_1724.method_23321(), LongJump.mc.field_1724.method_24828()));
                LongJump.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(LongJump.mc.field_1724.method_23317(), LongJump.mc.field_1724.method_23318() + 0.419, LongJump.mc.field_1724.method_23321(), LongJump.mc.field_1724.method_24828()));
                LongJump.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(LongJump.mc.field_1724.method_23317() + 0.0624, LongJump.mc.field_1724.method_23318(), LongJump.mc.field_1724.method_23321(), LongJump.mc.field_1724.method_24828()));
                LongJump.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(LongJump.mc.field_1724.method_23317(), LongJump.mc.field_1724.method_23318() + 0.419, LongJump.mc.field_1724.method_23321(), LongJump.mc.field_1724.method_24828()));
            }
            if (this.groundTicks > 2) {
                this.groundTicks = 0;
                Alien.MOVEMENT.setMotionXZ((double)dx * 0.3, (double)dz * 0.3);
                Alien.MOVEMENT.setMotionY(0.424f);
            }
        }
    }

    @EventListener
    public void onPacketInbound(PacketEvent event) {
        if (LongJump.mc.field_1724 == null || LongJump.mc.field_1687 == null || LongJump.mc.field_1755 instanceof class_434) {
            return;
        }
        if (event.getPacket() instanceof class_2708 && this.autoDisableConfig.getValue()) {
            this.disable();
        }
    }

    private double getJumpCollisions(class_1657 player, double d) {
        return 1.0;
    }

    public static enum JumpMode {
        NORMAL,
        GLIDE,
        GRIM;

    }
}

