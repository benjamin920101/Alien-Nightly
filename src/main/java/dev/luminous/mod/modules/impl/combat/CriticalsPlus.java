/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.decoration.EndCrystalEntity
 *  net.minecraft.block.Blocks
 *  net.minecraft.network.PacketByteBuf
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$PositionAndOnGround
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$Full
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.api.utils.rotation.RotationUtils;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class CriticalsPlus
extends Module {
    public static CriticalsPlus INSTANCE;
    private final BooleanSetting onlyGround = this.add(new BooleanSetting("OnlyGround", true));
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.OldNCP));
    boolean canGrimCrit = false;

    public CriticalsPlus() {
        super("Criticals+", Module.Category.Combat);
        this.setChinese("\u5200\u7206+");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @EventListener
    public void onPacketSend(PacketEvent.Send event) {
        class_1297 entity;
        class_2824 packet;
        class_2596<?> class_25962 = event.getPacket();
        if (!(!(class_25962 instanceof class_2824) || CriticalsPlus.getInteractType(packet = (class_2824)class_25962) != InteractType.ATTACK || (entity = CriticalsPlus.getEntity(packet)) instanceof class_1511 || this.onlyGround.getValue() && !CriticalsPlus.mc.field_1724.method_24828() && !CriticalsPlus.mc.field_1724.method_31549().field_7479 || CriticalsPlus.mc.field_1724.method_5771() || CriticalsPlus.mc.field_1724.method_5869() || entity == null)) {
            CriticalsPlus.mc.field_1724.method_7277(entity);
            this.doCrit();
        }
    }

    public void doCrit() {
        if (this.mode.getValue() == Mode.Strict && CriticalsPlus.mc.field_1687.method_8320(CriticalsPlus.mc.field_1724.method_24515()).method_26204() != class_2246.field_10343) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.062600301692775, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.07260029960661, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318(), CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318(), CriticalsPlus.mc.field_1724.method_23321(), false));
        } else if (this.mode.getValue() == Mode.NCP) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.0625, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318(), CriticalsPlus.mc.field_1724.method_23321(), false));
        } else if (this.mode.getValue() == Mode.OldNCP) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 1.058293536E-5, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 9.16580235E-6, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 1.0371854E-7, CriticalsPlus.mc.field_1724.method_23321(), false));
        } else if (this.mode.getValue() == Mode.NewNCP) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 2.71875E-7, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318(), CriticalsPlus.mc.field_1724.method_23321(), false));
        } else if (this.mode.is(Mode.Hypixel2K22)) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.0045, CriticalsPlus.mc.field_1724.method_23321(), true));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 1.52121E-4, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.3, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.025, CriticalsPlus.mc.field_1724.method_23321(), false));
        } else if (this.mode.is(Mode.Packet)) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 5.0E-4, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 1.0E-4, CriticalsPlus.mc.field_1724.method_23321(), false));
        } else if (this.mode.is(Mode.BBTT)) {
            if (MovementUtil.isMoving() || !MovementUtil.isStatic()) {
                return;
            }
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318(), CriticalsPlus.mc.field_1724.method_23321(), true));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.0625, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.045, CriticalsPlus.mc.field_1724.method_23321(), false));
        } else if (this.mode.getValue() == Mode.LowPacket) {
            CriticalsPlus.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 2.71875E-7, CriticalsPlus.mc.field_1724.method_23321(), false));
            CriticalsPlus.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318(), CriticalsPlus.mc.field_1724.method_23321(), false));
        } else if (this.mode.is(Mode.Grim)) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.0625, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.04535, CriticalsPlus.mc.field_1724.method_23321(), false));
        } else if (this.mode.is(Mode.GrimV2)) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2830(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() - (double)1.0E-4f, CriticalsPlus.mc.field_1724.method_23321(), RotationUtils.getActualYaw(), RotationUtils.getActualPitch(), false));
        } else if (this.mode.is(Mode.GrimCC)) {
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.0625, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 0.0625013579, CriticalsPlus.mc.field_1724.method_23321(), false));
            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 1.3579E-6, CriticalsPlus.mc.field_1724.method_23321(), false));
        } else if (this.mode.getValue() == Mode.New2b2t) {
            CriticalsPlus.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318() + 2.71875E-7, CriticalsPlus.mc.field_1724.method_23321(), false));
            CriticalsPlus.mc.field_1724.field_3944.method_52787((class_2596)new class_2828.class_2829(CriticalsPlus.mc.field_1724.method_23317(), CriticalsPlus.mc.field_1724.method_23318(), CriticalsPlus.mc.field_1724.method_23321(), false));
        }
    }

    public static class_1297 getEntity(class_2824 packet) {
        class_2540 packetBuf = new class_2540(Unpooled.buffer());
        packet.method_55976(packetBuf);
        return CriticalsPlus.mc.field_1687 == null ? null : CriticalsPlus.mc.field_1687.method_8469(packetBuf.method_10816());
    }

    public static InteractType getInteractType(class_2824 packet) {
        class_2540 packetBuf = new class_2540(Unpooled.buffer());
        packet.method_55976(packetBuf);
        packetBuf.method_10816();
        return (InteractType)packetBuf.method_10818(InteractType.class);
    }

    public static enum Mode {
        NewNCP,
        Strict,
        NCP,
        OldNCP,
        Hypixel2K22,
        Packet,
        BBTT,
        LowPacket,
        GrimCC,
        GrimV2,
        Grim,
        New2b2t,
        Grimv3;

    }

    public static enum InteractType {
        INTERACT,
        ATTACK,
        INTERACT_AT;

    }
}

