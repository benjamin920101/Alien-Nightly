/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1268
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1304
 *  net.minecraft.class_1313
 *  net.minecraft.class_1657
 *  net.minecraft.class_1671
 *  net.minecraft.class_1713
 *  net.minecraft.class_1770
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2708
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 *  net.minecraft.class_2886
 *  net.minecraft.class_3532
 *  net.minecraft.class_746
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.MoveEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.TravelEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.events.impl.UpdateRotateEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.asm.accessors.IFireworkRocketEntity;
import dev.luminous.asm.accessors.ILivingEntity;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.AntiCheat;
import dev.luminous.mod.modules.impl.movement.Sprint;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1313;
import net.minecraft.class_1657;
import net.minecraft.class_1671;
import net.minecraft.class_1713;
import net.minecraft.class_1770;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2848;
import net.minecraft.class_2886;
import net.minecraft.class_3532;
import net.minecraft.class_746;

public class ElytraFly
extends Module {
    public static ElytraFly INSTANCE;
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Control));
    public final BooleanSetting infiniteDura = this.add(new BooleanSetting("InfiniteDura", false));
    public final BooleanSetting packet = this.add(new BooleanSetting("Packet", false).setParent());
    private final SliderSetting packetDelay = this.add(new SliderSetting("PacketDelay", 0.0, 0.0, 20.0, 1.0, this.packet::isOpen));
    private final BooleanSetting setFlag = this.add(new BooleanSetting("SetFlag", false, () -> !this.mode.is(Mode.Bounce)));
    private final BooleanSetting firework = this.add(new BooleanSetting("Firework", false).setParent());
    public final BindSetting fireWork = this.add(new BindSetting("FireWorkBind", -1, this.firework::isOpen));
    public final BooleanSetting packetInteract = this.add(new BooleanSetting("PacketInteract", true, this.firework::isOpen));
    public final BooleanSetting inventory = this.add(new BooleanSetting("InventorySwap", true, this.firework::isOpen));
    public final BooleanSetting onlyOne = this.add(new BooleanSetting("OnlyOne", true, this.firework::isOpen));
    private final BooleanSetting usingPause = this.add(new BooleanSetting("UsingPause", true, this.firework::isOpen));
    public final BooleanSetting autoJump = this.add(new BooleanSetting("AutoJump", true, () -> this.mode.is(Mode.Bounce)));
    public final SliderSetting upPitch = this.add(new SliderSetting("UpPitch", 0.0, 0.0, 90.0, () -> this.mode.getValue() == Mode.Control));
    public final SliderSetting upFactor = this.add(new SliderSetting("UpFactor", 1.0, 0.0, 10.0, () -> this.mode.getValue() == Mode.Control));
    public final SliderSetting downFactor = this.add(new SliderSetting("FallSpeed", 1.0, 0.0, 10.0, () -> this.mode.getValue() == Mode.Control));
    public final SliderSetting speed = this.add(new SliderSetting("Speed", 1.0, (double)0.1f, 10.0, () -> this.mode.getValue() == Mode.Control));
    public final BooleanSetting speedLimit = this.add(new BooleanSetting("SpeedLimit", true, () -> this.mode.getValue() == Mode.Control));
    public final SliderSetting maxSpeed = this.add(new SliderSetting("MaxSpeed", 2.5, (double)0.1f, 10.0, () -> this.speedLimit.getValue() && this.mode.getValue() == Mode.Control));
    public final BooleanSetting noDrag = this.add(new BooleanSetting("NoDrag", false, () -> this.mode.getValue() == Mode.Control));
    public final Timer fireworkTimer = new Timer();
    private final BooleanSetting autoStop = this.add(new BooleanSetting("AutoStop", true));
    private final BooleanSetting sprint = this.add(new BooleanSetting("Sprint", true, () -> this.mode.is(Mode.Bounce)));
    private final SliderSetting pitch = this.add(new SliderSetting("Pitch", 88.0, -90.0, 90.0, 0.1, () -> this.mode.is(Mode.Bounce)));
    private final BooleanSetting instantFly = this.add(new BooleanSetting("AutoStart", true, () -> !this.mode.is(Mode.Bounce)));
    private final BooleanSetting checkSpeed = this.add(new BooleanSetting("CheckSpeed", false, () -> !this.mode.is(Mode.Bounce)));
    public final SliderSetting minSpeed = this.add(new SliderSetting("MinSpeed", 70.0, 0.1, 200.0, () -> !this.mode.is(Mode.Bounce)));
    private final SliderSetting delay = this.add(new SliderSetting("Delay", 1000.0, 0.0, 20000.0, 50.0, () -> !this.mode.is(Mode.Bounce)));
    private final SliderSetting timeout = this.add(new SliderSetting("Timeout", 0.0, 0.1, 1.0, 0.1, () -> !this.mode.is(Mode.Bounce)));
    private final SliderSetting sneakDownSpeed = this.add(new SliderSetting("DownSpeed", 1.0, 0.1, 10.0, () -> this.mode.getValue() == Mode.Control));
    private final SliderSetting boost = this.add(new SliderSetting("Boost", 1.0, 0.1, 4.0, () -> this.mode.getValue() == Mode.Boost));
    private final BooleanSetting freeze = this.add(new BooleanSetting("Freeze", false, () -> this.mode.is(Mode.Rotation)));
    private final BooleanSetting motionStop = this.add(new BooleanSetting("MotionStop", false, () -> this.mode.is(Mode.Rotation)));
    private final SliderSetting infiniteMaxSpeed = this.add(new SliderSetting("InfiniteMaxSpeed", 150.0, 50.0, 170.0, () -> this.mode.getValue() == Mode.Pitch));
    private final SliderSetting infiniteMinSpeed = this.add(new SliderSetting("InfiniteMinSpeed", 25.0, 10.0, 70.0, () -> this.mode.getValue() == Mode.Pitch));
    private final SliderSetting infiniteMaxHeight = this.add(new SliderSetting("InfiniteMaxHeight", 200, -50, 360, () -> this.mode.getValue() == Mode.Pitch));
    public final BooleanSetting releaseSneak = this.add(new BooleanSetting("ReleaseSneak", false));
    private final Timer instantFlyTimer = new Timer();
    boolean prev;
    float prePitch;
    private boolean hasElytra = false;
    float yaw = 0.0f;
    float rotationPitch = 0.0f;
    boolean flying = false;
    int packetDelayInt = 0;
    private boolean down;
    private float lastInfinitePitch;
    private float infinitePitch;

    public ElytraFly() {
        super("ElytraFly", Module.Category.Movement);
        this.setChinese("\u9798\u7fc5\u98de\u884c");
        INSTANCE = this;
        Alien.EVENT_BUS.subscribe(new FireWorkTweak());
    }

    public void off() {
        if (!this.inventory.getValue() || EntityUtil.inInventory()) {
            int firework;
            if (this.onlyOne.getValue()) {
                for (class_1297 entity : Alien.THREAD.getEntities()) {
                    class_1671 fireworkRocketEntity;
                    if (!(entity instanceof class_1671) || ((IFireworkRocketEntity)(fireworkRocketEntity = (class_1671)entity)).getShooter() != ElytraFly.mc.field_1724) continue;
                    return;
                }
            }
            ElytraFly.INSTANCE.fireworkTimer.reset();
            if (ElytraFly.mc.field_1724.method_6047().method_7909() == class_1802.field_8639) {
                if (this.packetInteract.getValue()) {
                    ElytraFly.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                } else {
                    ElytraFly.mc.field_1761.method_2919((class_1657)ElytraFly.mc.field_1724, class_1268.field_5808);
                }
            } else if (this.inventory.getValue() && (firework = InventoryUtil.findItemInventorySlot(class_1802.field_8639)) != -1) {
                InventoryUtil.inventorySwap(firework, ElytraFly.mc.field_1724.method_31548().field_7545);
                if (this.packetInteract.getValue()) {
                    ElytraFly.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                } else {
                    ElytraFly.mc.field_1761.method_2919((class_1657)ElytraFly.mc.field_1724, class_1268.field_5808);
                }
                InventoryUtil.inventorySwap(firework, ElytraFly.mc.field_1724.method_31548().field_7545);
                EntityUtil.syncInventory();
            } else {
                firework = InventoryUtil.findItem(class_1802.field_8639);
                if (firework != -1) {
                    int old = ElytraFly.mc.field_1724.method_31548().field_7545;
                    InventoryUtil.switchToSlot(firework);
                    if (this.packetInteract.getValue()) {
                        ElytraFly.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                    } else {
                        ElytraFly.mc.field_1761.method_2919((class_1657)ElytraFly.mc.field_1724, class_1268.field_5808);
                    }
                    InventoryUtil.switchToSlot(old);
                }
            }
        }
    }

    public static boolean recastElytra(class_746 player) {
        if (ElytraFly.checkConditions(player) && ElytraFly.ignoreGround(player)) {
            player.field_3944.method_52787((class_2596)new class_2848((class_1297)player, class_2848.class_2849.field_12982));
            if (ElytraFly.INSTANCE.setFlag.getValue()) {
                ElytraFly.mc.field_1724.method_23669();
            }
            return true;
        }
        return false;
    }

    public static boolean checkConditions(class_746 player) {
        class_1799 itemStack = player.method_6118(class_1304.field_6174);
        return !player.method_31549().field_7479 && !player.method_5765() && !player.method_6101() && itemStack.method_31574(class_1802.field_8833) && class_1770.method_7804((class_1799)itemStack);
    }

    private static boolean ignoreGround(class_746 player) {
        if (!player.method_5799() && !player.method_6059(class_1294.field_5902)) {
            class_1799 itemStack = player.method_6118(class_1304.field_6174);
            if (itemStack.method_31574(class_1802.field_8833) && class_1770.method_7804((class_1799)itemStack)) {
                player.method_23669();
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @Override
    public boolean onEnable() {
        if (!ElytraFly.nullCheck()) {
            this.hasElytra = false;
            this.yaw = ElytraFly.mc.field_1724.method_36454();
            this.rotationPitch = ElytraFly.mc.field_1724.method_36455();
        }
        return false;
    }

    @Override
    public void onDisable() {
        if (!ElytraFly.nullCheck() && this.releaseSneak.getValue()) {
            mc.method_1562().method_52787((class_2596)new class_2848((class_1297)ElytraFly.mc.field_1724, class_2848.class_2849.field_12984));
        }
    }

    private void boost() {
        if (this.hasElytra) {
            if (!this.isFallFlying()) {
                return;
            }
            float yaw = (float)Math.toRadians(ElytraFly.mc.field_1724.method_36454());
            if (ElytraFly.mc.field_1690.field_1894.method_1434()) {
                ElytraFly.mc.field_1724.method_5762((double)(-class_3532.method_15374((float)yaw) * this.boost.getValueFloat() / 10.0f), 0.0, (double)(class_3532.method_15362((float)yaw) * this.boost.getValueFloat() / 10.0f));
            }
        }
    }

    @EventListener(priority=-9999)
    public void onRotation(UpdateRotateEvent event) {
        if (!ElytraFly.nullCheck()) {
            if (this.mode.is(Mode.Rotation)) {
                if (this.isFallFlying()) {
                    if (MovementUtil.isMoving()) {
                        if (ElytraFly.mc.field_1690.field_1903.method_1434()) {
                            this.rotationPitch = -45.0f;
                        } else if (ElytraFly.mc.field_1690.field_1832.method_1434()) {
                            this.rotationPitch = 45.0f;
                        } else {
                            this.rotationPitch = -1.9f;
                            if (this.motionStop.getValue()) {
                                this.setY(0.0);
                            }
                        }
                    } else if (ElytraFly.mc.field_1690.field_1903.method_1434()) {
                        this.rotationPitch = -89.0f;
                    } else if (ElytraFly.mc.field_1690.field_1832.method_1434()) {
                        this.rotationPitch = 89.0f;
                    } else if (this.motionStop.getValue()) {
                        this.setY(0.0);
                    }
                    if (ElytraFly.mc.field_1690.field_1894.method_1434() || ElytraFly.mc.field_1690.field_1881.method_1434() || ElytraFly.mc.field_1690.field_1913.method_1434() || ElytraFly.mc.field_1690.field_1849.method_1434()) {
                        this.yaw = Sprint.getSprintYaw(ElytraFly.mc.field_1724.method_36454());
                    } else if (this.motionStop.getValue()) {
                        this.setX(0.0);
                        this.setZ(0.0);
                    }
                    event.setYaw(this.yaw);
                    event.setPitch(this.rotationPitch);
                }
            } else if (this.mode.is(Mode.Pitch)) {
                if (this.isFallFlying()) {
                    event.setPitch(this.infinitePitch);
                }
            } else if (this.mode.is(Mode.Bounce) && this.isFallFlying()) {
                event.setPitch(this.pitch.getValueFloat());
            }
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.getInfinitePitch();
        this.flying = false;
        if (this.packet.getValue()) {
            this.hasElytra = InventoryUtil.findItemInventorySlot(class_1802.field_8833) != -1;
        } else {
            this.hasElytra = false;
            for (class_1799 is : ElytraFly.mc.field_1724.method_5661()) {
                if (!(is.method_7909() instanceof class_1770)) continue;
                this.hasElytra = true;
                break;
            }
            if (this.infiniteDura.getValue() && !ElytraFly.mc.field_1724.method_24828() && this.hasElytra) {
                this.flying = true;
                ElytraFly.mc.field_1761.method_2906(ElytraFly.mc.field_1724.field_7512.field_7763, 6, 0, class_1713.field_7790, (class_1657)ElytraFly.mc.field_1724);
                ElytraFly.mc.field_1761.method_2906(ElytraFly.mc.field_1724.field_7512.field_7763, 6, 0, class_1713.field_7790, (class_1657)ElytraFly.mc.field_1724);
                mc.method_1562().method_52787((class_2596)new class_2848((class_1297)ElytraFly.mc.field_1724, class_2848.class_2849.field_12982));
                if (this.setFlag.getValue()) {
                    ElytraFly.mc.field_1724.method_23669();
                }
            }
            if (this.mode.is(Mode.Bounce)) {
                ((ILivingEntity)ElytraFly.mc.field_1724).setLastJumpCooldown(0);
                return;
            }
        }
        double x = ElytraFly.mc.field_1724.method_23317() - ElytraFly.mc.field_1724.field_6014;
        double y = ElytraFly.mc.field_1724.method_23318() - ElytraFly.mc.field_1724.field_6036;
        double z = ElytraFly.mc.field_1724.method_23321() - ElytraFly.mc.field_1724.field_5969;
        double dist = Math.sqrt(x * x + z * z + y * y) / 1000.0;
        double div = 1.388888888888889E-5;
        float timer = Alien.TIMER.get();
        double speed = dist / div * (double)timer;
        if (this.mode.getValue() == Mode.Boost) {
            this.boost();
        }
        if (this.packet.getValue()) {
            if (!ElytraFly.mc.field_1724.method_24828()) {
                ++this.packetDelayInt;
                if (!((double)this.packetDelayInt <= this.packetDelay.getValue())) {
                    int elytra = InventoryUtil.findItem(class_1802.field_8833);
                    if (elytra != -1) {
                        ElytraFly.mc.field_1761.method_2906(ElytraFly.mc.field_1724.field_7512.field_7763, 6, elytra, class_1713.field_7791, (class_1657)ElytraFly.mc.field_1724);
                        mc.method_1562().method_52787((class_2596)new class_2848((class_1297)ElytraFly.mc.field_1724, class_2848.class_2849.field_12982));
                        ElytraFly.mc.field_1724.method_23669();
                        if ((!this.checkSpeed.getValue() || speed <= this.minSpeed.getValue()) && this.firework.getValue() && this.fireworkTimer.passed(this.delay.getValueInt()) && (MovementUtil.isMoving() || this.mode.is(Mode.Rotation) && ElytraFly.mc.field_1690.field_1903.method_1434()) && (!ElytraFly.mc.field_1724.method_6115() || !this.usingPause.getValue()) && this.isFallFlying()) {
                            this.off();
                            this.fireworkTimer.reset();
                        }
                        ElytraFly.mc.field_1761.method_2906(ElytraFly.mc.field_1724.field_7512.field_7763, 6, elytra, class_1713.field_7791, (class_1657)ElytraFly.mc.field_1724);
                        this.packetDelayInt = 0;
                    } else {
                        elytra = InventoryUtil.findItemInventorySlot(class_1802.field_8833);
                        if (elytra != -1) {
                            ElytraFly.mc.field_1761.method_2906(ElytraFly.mc.field_1724.field_7512.field_7763, elytra, 0, class_1713.field_7790, (class_1657)ElytraFly.mc.field_1724);
                            ElytraFly.mc.field_1761.method_2906(ElytraFly.mc.field_1724.field_7512.field_7763, 6, 0, class_1713.field_7790, (class_1657)ElytraFly.mc.field_1724);
                            mc.method_1562().method_52787((class_2596)new class_2848((class_1297)ElytraFly.mc.field_1724, class_2848.class_2849.field_12982));
                            ElytraFly.mc.field_1724.method_23669();
                            if ((!this.checkSpeed.getValue() || speed <= this.minSpeed.getValue()) && this.firework.getValue() && this.fireworkTimer.passed(this.delay.getValueInt()) && (MovementUtil.isMoving() || this.mode.is(Mode.Rotation) && ElytraFly.mc.field_1690.field_1903.method_1434()) && (!ElytraFly.mc.field_1724.method_6115() || !this.usingPause.getValue()) && this.isFallFlying()) {
                                this.off();
                                this.fireworkTimer.reset();
                            }
                            ElytraFly.mc.field_1761.method_2906(ElytraFly.mc.field_1724.field_7512.field_7763, 6, 0, class_1713.field_7790, (class_1657)ElytraFly.mc.field_1724);
                            ElytraFly.mc.field_1761.method_2906(ElytraFly.mc.field_1724.field_7512.field_7763, elytra, 0, class_1713.field_7790, (class_1657)ElytraFly.mc.field_1724);
                            this.packetDelayInt = 0;
                        }
                    }
                }
            }
        } else {
            if ((!this.checkSpeed.getValue() || speed <= this.minSpeed.getValue()) && this.firework.getValue() && this.fireworkTimer.passed(this.delay.getValueInt()) && (MovementUtil.isMoving() || this.mode.is(Mode.Rotation) && ElytraFly.mc.field_1690.field_1903.method_1434()) && (!ElytraFly.mc.field_1724.method_6115() || !this.usingPause.getValue()) && this.isFallFlying()) {
                this.off();
                this.fireworkTimer.reset();
            }
            if (!this.isFallFlying() && this.hasElytra) {
                this.fireworkTimer.setMs(99999999L);
                if (!ElytraFly.mc.field_1724.method_24828() && this.instantFly.getValue() && ElytraFly.mc.field_1724.method_18798().method_10214() < 0.0 && !this.infiniteDura.getValue()) {
                    if (!this.instantFlyTimer.passed((long)(1000.0 * this.timeout.getValue()))) {
                        return;
                    }
                    this.instantFlyTimer.reset();
                    mc.method_1562().method_52787((class_2596)new class_2848((class_1297)ElytraFly.mc.field_1724, class_2848.class_2849.field_12982));
                    if (this.setFlag.getValue()) {
                        ElytraFly.mc.field_1724.method_23669();
                    }
                }
            }
        }
    }

    protected final class_243 getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = class_3532.method_15362((float)g);
        float i = class_3532.method_15374((float)g);
        float j = class_3532.method_15362((float)f);
        float k = class_3532.method_15374((float)f);
        return new class_243((double)(i * j), (double)(-k), (double)(h * j));
    }

    public class_243 getRotationVec(float tickDelta) {
        return this.getRotationVector(-this.upPitch.getValueFloat(), ElytraFly.mc.field_1724.method_5705(tickDelta));
    }

    @EventListener
    private void onPlayerMove(MoveEvent event) {
        if (this.autoStop.getValue() && this.isFallFlying()) {
            int chunkX = (int)(ElytraFly.mc.field_1724.method_23317() / 16.0);
            int chunkZ = (int)(ElytraFly.mc.field_1724.method_23321() / 16.0);
            if (!ElytraFly.mc.field_1687.method_2935().method_12123(chunkX, chunkZ)) {
                event.cancel();
            }
        }
    }

    @EventListener
    private void onTick(ClientTickEvent event) {
        if (!ElytraFly.nullCheck() && this.mode.is(Mode.Bounce) && this.hasElytra) {
            if (this.autoJump.getValue()) {
                ElytraFly.mc.field_1690.field_1903.method_23481(true);
            }
            if (event.isPost()) {
                if (!this.isFallFlying()) {
                    mc.method_1562().method_52787((class_2596)new class_2848((class_1297)ElytraFly.mc.field_1724, class_2848.class_2849.field_12982));
                }
                if (ElytraFly.checkConditions(ElytraFly.mc.field_1724) && !this.sprint.getValue()) {
                    if (this.isFallFlying()) {
                        ElytraFly.mc.field_1724.method_5728(ElytraFly.mc.field_1724.method_24828());
                    } else {
                        ElytraFly.mc.field_1724.method_5728(true);
                    }
                }
            } else if (ElytraFly.checkConditions(ElytraFly.mc.field_1724) && this.sprint.getValue()) {
                ElytraFly.mc.field_1724.method_5728(true);
            }
        }
    }

    @EventListener
    private void onPacketSend(PacketEvent.Send event) {
        if (!ElytraFly.nullCheck() && this.mode.is(Mode.Bounce) && this.hasElytra && event.getPacket() instanceof class_2848 && ((class_2848)event.getPacket()).method_12365().equals((Object)class_2848.class_2849.field_12982) && !this.sprint.getValue()) {
            ElytraFly.mc.field_1724.method_5728(true);
        }
    }

    @EventListener
    private void onPacketReceive(PacketEvent.Receive event) {
        if (!ElytraFly.nullCheck() && this.mode.is(Mode.Bounce) && this.hasElytra && event.getPacket() instanceof class_2708) {
            ElytraFly.mc.field_1724.method_23670();
        }
    }

    @EventListener
    public void travel(TravelEvent event) {
        if (!ElytraFly.nullCheck() && !AntiCheat.INSTANCE.movementSync()) {
            if (this.mode.is(Mode.Bounce) && this.hasElytra) {
                if (event.isPre()) {
                    this.prev = true;
                    this.prePitch = ElytraFly.mc.field_1724.method_36455();
                    ElytraFly.mc.field_1724.method_36457(this.pitch.getValueFloat());
                } else if (this.prev) {
                    this.prev = false;
                    ElytraFly.mc.field_1724.method_36457(this.prePitch);
                }
            } else if (this.mode.is(Mode.Pitch) && this.isFallFlying()) {
                if (event.isPre()) {
                    this.prev = true;
                    this.prePitch = ElytraFly.mc.field_1724.method_36455();
                    ElytraFly.mc.field_1724.method_36457(this.lastInfinitePitch);
                } else if (this.prev) {
                    this.prev = false;
                    ElytraFly.mc.field_1724.method_36457(this.prePitch);
                }
            }
        }
    }

    @EventListener
    public void onMove(TravelEvent event) {
        if (!ElytraFly.nullCheck() && this.hasElytra && this.isFallFlying() && !event.isPost()) {
            if ((this.mode.is(Mode.Freeze) || this.mode.is(Mode.Rotation) && this.freeze.getValue()) && !MovementUtil.isMoving() && !ElytraFly.mc.field_1690.field_1903.method_1434() && !ElytraFly.mc.field_1690.field_1832.method_1434()) {
                event.cancel();
            } else if (this.mode.getValue() == Mode.Control) {
                if (this.firework.getValue()) {
                    if (ElytraFly.mc.field_1690.field_1832.method_1434() && ElytraFly.mc.field_1724.field_3913.field_3904) {
                        this.setY(0.0);
                    } else if (ElytraFly.mc.field_1690.field_1832.method_1434()) {
                        this.setY(-this.sneakDownSpeed.getValue());
                    } else if (ElytraFly.mc.field_1724.field_3913.field_3904) {
                        this.setY(this.upFactor.getValue());
                    } else {
                        this.setY(-3.0E-11 * this.downFactor.getValue());
                    }
                    double[] dir = MovementUtil.directionSpeed(this.speed.getValue());
                    this.setX(dir[0]);
                    this.setZ(dir[1]);
                } else {
                    class_243 lookVec = this.getRotationVec(mc.method_60646().method_60637(true));
                    double lookDist = Math.sqrt(lookVec.field_1352 * lookVec.field_1352 + lookVec.field_1350 * lookVec.field_1350);
                    double motionDist = Math.sqrt(this.getX() * this.getX() + this.getZ() * this.getZ());
                    if (ElytraFly.mc.field_1724.field_3913.field_3903) {
                        this.setY(-this.sneakDownSpeed.getValue());
                    } else if (!ElytraFly.mc.field_1724.field_3913.field_3904) {
                        this.setY(-3.0E-11 * this.downFactor.getValue());
                    }
                    if (ElytraFly.mc.field_1724.field_3913.field_3904) {
                        if (motionDist > this.upFactor.getValue() / this.upFactor.getMax()) {
                            double rawUpSpeed = motionDist * 0.01325;
                            this.setY(this.getY() + rawUpSpeed * 3.2);
                            this.setX(this.getX() - lookVec.field_1352 * rawUpSpeed / lookDist);
                            this.setZ(this.getZ() - lookVec.field_1350 * rawUpSpeed / lookDist);
                        } else {
                            double[] dir = MovementUtil.directionSpeed(this.speed.getValue());
                            this.setX(dir[0]);
                            this.setZ(dir[1]);
                        }
                    }
                    if (lookDist > 0.0) {
                        this.setX(this.getX() + (lookVec.field_1352 / lookDist * motionDist - this.getX()) * 0.1);
                        this.setZ(this.getZ() + (lookVec.field_1350 / lookDist * motionDist - this.getZ()) * 0.1);
                    }
                    if (!ElytraFly.mc.field_1724.field_3913.field_3904) {
                        double[] dir = MovementUtil.directionSpeed(this.speed.getValue());
                        this.setX(dir[0]);
                        this.setZ(dir[1]);
                    }
                    if (!this.noDrag.getValue()) {
                        this.setY(this.getY() * (double)0.99f);
                        this.setX(this.getX() * (double)0.98f);
                        this.setZ(this.getZ() * (double)0.99f);
                    }
                    double finalDist = Math.sqrt(this.getX() * this.getX() + this.getZ() * this.getZ());
                    if (this.speedLimit.getValue() && finalDist > this.maxSpeed.getValue()) {
                        this.setX(this.getX() * this.maxSpeed.getValue() / finalDist);
                        this.setZ(this.getZ() * this.maxSpeed.getValue() / finalDist);
                    }
                    event.cancel();
                    ElytraFly.mc.field_1724.method_5784(class_1313.field_6308, ElytraFly.mc.field_1724.method_18798());
                }
            }
        }
    }

    private double getX() {
        return MovementUtil.getMotionX();
    }

    private void setX(double f) {
        MovementUtil.setMotionX(f);
    }

    private double getY() {
        return MovementUtil.getMotionY();
    }

    private void setY(double f) {
        MovementUtil.setMotionY(f);
    }

    private double getZ() {
        return MovementUtil.getMotionZ();
    }

    private void setZ(double f) {
        MovementUtil.setMotionZ(f);
    }

    private void getInfinitePitch() {
        this.lastInfinitePitch = this.infinitePitch;
        double currentPlayerSpeed = Math.hypot(ElytraFly.mc.field_1724.method_23317() - ElytraFly.mc.field_1724.field_6014, ElytraFly.mc.field_1724.method_23321() - ElytraFly.mc.field_1724.field_5969);
        if (ElytraFly.mc.field_1724.method_23318() < this.infiniteMaxHeight.getValue()) {
            if (currentPlayerSpeed * 72.0 < this.infiniteMinSpeed.getValue() && !this.down) {
                this.down = true;
            }
            if (currentPlayerSpeed * 72.0 > this.infiniteMaxSpeed.getValue() && this.down) {
                this.down = false;
            }
        } else {
            this.down = true;
        }
        this.infinitePitch = this.down ? (this.infinitePitch += 3.0f) : (this.infinitePitch -= 3.0f);
        this.infinitePitch = MathUtil.clamp(this.infinitePitch, -40.0f, 40.0f);
    }

    public boolean isFallFlying() {
        return ElytraFly.mc.field_1724.method_6128() || this.packet.getValue() && this.hasElytra && !ElytraFly.mc.field_1724.method_24828() || this.flying;
    }

    public static enum Mode {
        Control,
        Boost,
        Bounce,
        Freeze,
        None,
        Rotation,
        Pitch;

    }

    public class FireWorkTweak {
        boolean press;

        @EventListener
        public void onTick(ClientTickEvent event) {
            if (!(Module.nullCheck() || event.isPost() || ElytraFly.this.inventory.getValue() && !EntityUtil.inInventory())) {
                if (Wrapper.mc.field_1755 == null) {
                    if (ElytraFly.this.fireWork.isPressed()) {
                        if (!(this.press || !ElytraFly.this.fireworkTimer.passed(ElytraFly.this.delay.getValueInt()) || Wrapper.mc.field_1724.method_6115() && ElytraFly.this.usingPause.getValue() || !ElytraFly.this.isFallFlying())) {
                            ElytraFly.this.off();
                            ElytraFly.this.fireworkTimer.reset();
                        }
                        this.press = true;
                    } else {
                        this.press = false;
                    }
                } else {
                    this.press = false;
                }
            }
        }
    }
}

