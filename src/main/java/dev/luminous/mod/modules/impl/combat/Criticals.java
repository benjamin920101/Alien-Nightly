/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1511
 *  net.minecraft.class_238
 *  net.minecraft.class_2596
 *  net.minecraft.class_2824
 *  net.minecraft.class_2824$class_5907
 *  net.minecraft.class_2828
 *  net.minecraft.class_2828$class_2829
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.asm.accessors.IPlayerMoveC2SPacket;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.exploit.Blink;
import dev.luminous.mod.modules.impl.exploit.BowBomb;
import dev.luminous.mod.modules.impl.exploit.Phase;
import dev.luminous.mod.modules.impl.player.KeyPearl;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_238;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2828;

public class Criticals
extends Module {
    public static Criticals INSTANCE;
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.OldNCP));
    public final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true, () -> !this.mode.is(Mode.Ground)));
    private final BooleanSetting setOnGround = this.add(new BooleanSetting("SetNoGround", false, () -> this.mode.is(Mode.Ground)));
    private final BooleanSetting blockCheck = this.add(new BooleanSetting("BlockCheck", true, () -> this.mode.is(Mode.Ground)));
    private final BooleanSetting autoJump = this.add(new BooleanSetting("AutoJump", true, () -> this.mode.is(Mode.Ground)).setParent());
    private final BooleanSetting mini = this.add(new BooleanSetting("Mini", true, () -> this.mode.is(Mode.Ground) && this.autoJump.isOpen()));
    private final SliderSetting y = this.add(new SliderSetting("MotionY", 0.05, 0.0, 1.0, 1.0E-10, () -> this.mode.is(Mode.Ground) && this.autoJump.isOpen()));
    private final BooleanSetting autoDisable = this.add(new BooleanSetting("AutoDisable", true, () -> this.mode.is(Mode.Ground)));
    private final BooleanSetting crawlingDisable = this.add(new BooleanSetting("CrawlingDisable", true, () -> this.mode.is(Mode.Ground)));
    private final BooleanSetting flight = this.add(new BooleanSetting("Flight", false, () -> this.mode.is(Mode.Ground)));
    boolean requireJump = false;

    public Criticals() {
        super("Criticals", Module.Category.Combat);
        this.setChinese("\u5200\u5200\u66b4\u51fb");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @EventListener
    public void onPacketSend(PacketEvent.Send event) {
        if (!(event.isCancelled() || Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue())) {
            if (this.mode.is(Mode.Ground)) {
                if (!BowBomb.send && !KeyPearl.throwing && !Phase.INSTANCE.isOn() && this.setOnGround.getValue() && event.getPacket() instanceof class_2828) {
                    ((IPlayerMoveC2SPacket)event.getPacket()).setOnGround(false);
                }
            } else {
                class_1297 entity;
                class_2824 packet;
                class_2596<?> class_25962 = event.getPacket();
                if (!(!(class_25962 instanceof class_2824) || Criticals.getInteractType(packet = (class_2824)class_25962) != class_2824.class_5907.field_29172 || (entity = Criticals.getEntity(packet)) instanceof class_1511 || this.onlyGround.getValue() && !Criticals.mc.field_1724.method_24828() && !Criticals.mc.field_1724.method_31549().field_7479 || Criticals.mc.field_1724.method_5771() || Criticals.mc.field_1724.method_5799() || entity == null)) {
                    this.doCrit(entity);
                }
            }
        }
    }

    @Override
    public void onLogout() {
        if (this.mode.is(Mode.Ground) && this.autoDisable.getValue()) {
            this.disable();
        }
    }

    @Override
    public boolean onEnable() {
        if (!Blink.INSTANCE.isOn() || !Blink.INSTANCE.pauseModule.getValue()) {
            this.requireJump = true;
            if (this.mode.is(Mode.Ground)) {
                if (Criticals.nullCheck()) {
                    if (this.autoDisable.getValue()) {
                        this.disable();
                    }
                } else if (MovementUtil.isMoving() && this.autoDisable.getValue()) {
                    this.disable();
                } else if (this.crawlingDisable.getValue() && Criticals.mc.field_1724.method_20448()) {
                    this.disable();
                } else if (Criticals.mc.field_1724.method_24828() && this.autoJump.getValue() && (!this.blockCheck.getValue() || BlockUtil.canCollide((class_1297)Criticals.mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10086(2))))) {
                    this.jump();
                }
            }
        }
        return false;
    }

    public void jump() {
        if (this.mini.getValue()) {
            MovementUtil.setMotionY(this.y.getValue());
        } else {
            Criticals.mc.field_1724.method_6043();
        }
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (!(Blink.INSTANCE.isOn() && Blink.INSTANCE.pauseModule.getValue() || !this.mode.is(Mode.Ground))) {
            if (this.crawlingDisable.getValue() && Criticals.mc.field_1724.method_20448()) {
                this.disable();
            } else if (MovementUtil.isMoving() && this.autoDisable.getValue()) {
                this.disable();
            } else if (this.flight.getValue() && Criticals.mc.field_1724.field_6017 > 0.0f) {
                MovementUtil.setMotionY(0.0);
                MovementUtil.setMotionX(0.0);
                MovementUtil.setMotionZ(0.0);
                this.requireJump = false;
            } else if (this.blockCheck.getValue() && !BlockUtil.canCollide((class_1297)Criticals.mc.field_1724, new class_238(EntityUtil.getPlayerPos(true).method_10086(2)))) {
                this.requireJump = true;
            } else if (Criticals.mc.field_1724.method_24828() && this.autoJump.getValue() && (this.flight.getValue() || this.requireJump)) {
                this.jump();
                this.requireJump = false;
            }
        }
    }

    public void doCrit(class_1297 entity) {
        switch (this.mode.getValue().ordinal()) {
            case 0: {
                Criticals.mc.field_1724.method_7277(entity);
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 2.71875E-7, Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318(), Criticals.mc.field_1724.method_23321(), false));
                break;
            }
            case 1: {
                Criticals.mc.field_1724.method_7277(entity);
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 0.062600301692775, Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 0.07260029960661, Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318(), Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318(), Criticals.mc.field_1724.method_23321(), false));
                break;
            }
            case 2: {
                Criticals.mc.field_1724.method_7277(entity);
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 0.0625, Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318(), Criticals.mc.field_1724.method_23321(), false));
                break;
            }
            case 3: {
                Criticals.mc.field_1724.method_7277(entity);
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 1.058293536E-5, Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 9.16580235E-6, Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 1.0371854E-7, Criticals.mc.field_1724.method_23321(), false));
                break;
            }
            case 4: {
                Criticals.mc.field_1724.method_7277(entity);
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 0.0045, Criticals.mc.field_1724.method_23321(), true));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 1.52121E-4, Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 0.3, Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 0.025, Criticals.mc.field_1724.method_23321(), false));
                break;
            }
            case 5: {
                Criticals.mc.field_1724.method_7277(entity);
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 5.0E-4, Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 1.0E-4, Criticals.mc.field_1724.method_23321(), false));
            }
            default: {
                break;
            }
            case 7: {
                if (MovementUtil.isMoving() || !MovementUtil.isStatic()) {
                    return;
                }
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318(), Criticals.mc.field_1724.method_23321(), true));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 0.0625, Criticals.mc.field_1724.method_23321(), false));
                mc.method_1562().method_52787((class_2596)new class_2828.class_2829(Criticals.mc.field_1724.method_23317(), Criticals.mc.field_1724.method_23318() + 0.045, Criticals.mc.field_1724.method_23321(), false));
            }
        }
    }

    public static class_1297 getEntity(class_2824 packet) {
        return Criticals.mc.field_1687 == null ? null : Criticals.mc.field_1687.method_8469(packet.field_12870);
    }

    public static class_2824.class_5907 getInteractType(class_2824 packet) {
        return packet.field_12871.method_34211();
    }

    public static enum Mode {
        UpdatedNCP,
        Strict,
        NCP,
        OldNCP,
        Hypixel2K22,
        Packet,
        Ground,
        BBTT;

    }
}

