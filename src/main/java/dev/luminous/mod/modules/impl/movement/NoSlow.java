/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Hand
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.screen.slot.SlotActionType
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket
 *  net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket$PositionAndOnGround
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket$Action
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
 *  net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket$Mode
 *  net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
 *  net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
 *  net.minecraft.client.option.KeyBinding
 *  net.minecraft.client.util.InputUtil
 *  net.minecraft.client.gui.screen.ChatScreen
 *  net.minecraft.component.DataComponentTypes
 */
package dev.luminous.mod.modules.impl.movement;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.InteractItemEvent;
import dev.luminous.api.events.impl.KeyboardInputEvent;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.player.MovementUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.movement.AutoWalk;
import dev.luminous.mod.modules.impl.movement.ElytraFly;
import dev.luminous.mod.modules.impl.movement.Sprint;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.component.DataComponentTypes;

public class NoSlow
extends Module {
    public static NoSlow INSTANCE;
    final Queue<class_2813> storedClicks = new LinkedList<class_2813>();
    final AtomicBoolean pause = new AtomicBoolean();
    private final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Vanilla));
    private final BooleanSetting soulSand = this.add(new BooleanSetting("SoulSand", true));
    private final BooleanSetting sneak = this.add(new BooleanSetting("Sneak", false));
    private final BooleanSetting climb = this.add(new BooleanSetting("Climb", false));
    private final BooleanSetting gui = this.add(new BooleanSetting("Gui", true));
    private final BooleanSetting allowSneak = this.add(new BooleanSetting("AllowSneak", false, this.gui::getValue));
    private final EnumSetting<Bypass> clickBypass = this.add(new EnumSetting<Bypass>("GuiMoveBypass", Bypass.None));
    boolean using = false;
    int delay = 0;

    public NoSlow() {
        super("NoSlow", Module.Category.Movement);
        this.setChinese("\u65e0\u51cf\u901f");
        INSTANCE = this;
    }

    private static float getMovementMultiplier(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0f;
        }
        return positive ? 1.0f : -1.0f;
    }

    @Override
    public String getInfo() {
        return this.mode.getValue().name();
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.using = NoSlow.mc.field_1724.method_6115();
        --this.delay;
        if (this.using) {
            this.delay = 2;
        }
        if (this.using && !NoSlow.mc.field_1724.method_3144() && !NoSlow.mc.field_1724.method_6128()) {
            switch (this.mode.getValue().ordinal()) {
                case 1: {
                    mc.method_1562().method_52787((class_2596)new class_2868(NoSlow.mc.field_1724.method_31548().field_7545));
                    break;
                }
                case 2: {
                    if (NoSlow.mc.field_1724.method_6058() == class_1268.field_5810) {
                        NoSlow.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                        break;
                    }
                    NoSlow.sendSequencedPacket(id -> new class_2886(class_1268.field_5810, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                    break;
                }
                case 3: {
                    NoSlow.mc.field_1761.method_2906(NoSlow.mc.field_1724.field_7512.field_7763, 1, 0, class_1713.field_7790, (class_1657)NoSlow.mc.field_1724);
                    if (NoSlow.mc.field_1724.method_6058() == class_1268.field_5810) {
                        NoSlow.sendSequencedPacket(id -> new class_2886(class_1268.field_5808, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                        break;
                    }
                    NoSlow.sendSequencedPacket(id -> new class_2886(class_1268.field_5810, id, Alien.ROTATION.getLastYaw(), Alien.ROTATION.getLastPitch()));
                }
            }
        }
        if (this.gui.getValue() && !(NoSlow.mc.field_1755 instanceof class_408)) {
            for (class_304 k : new class_304[]{NoSlow.mc.field_1690.field_1881, NoSlow.mc.field_1690.field_1913, NoSlow.mc.field_1690.field_1849}) {
                k.method_23481(class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)k.method_1428()).method_1444()));
            }
            NoSlow.mc.field_1690.field_1903.method_23481(ElytraFly.INSTANCE.isOn() && ElytraFly.INSTANCE.mode.is(ElytraFly.Mode.Bounce) && ElytraFly.INSTANCE.autoJump.getValue() || class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)NoSlow.mc.field_1690.field_1903.method_1428()).method_1444()));
            NoSlow.mc.field_1690.field_1894.method_23481(AutoWalk.INSTANCE.forward() || class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)NoSlow.mc.field_1690.field_1894.method_1428()).method_1444()));
            NoSlow.mc.field_1690.field_1867.method_23481(Sprint.INSTANCE.isOn() && !Sprint.INSTANCE.inWater() || class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)NoSlow.mc.field_1690.field_1867.method_1428()).method_1444()));
            if (this.allowSneak.getValue()) {
                NoSlow.mc.field_1690.field_1832.method_23481(class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)NoSlow.mc.field_1690.field_1832.method_1428()).method_1444()));
            }
        }
    }

    @EventListener(priority=100)
    public void keyboard(KeyboardInputEvent event) {
        if (this.sneak.getValue()) {
            event.cancel();
        }
        if (this.gui.getValue() && !(NoSlow.mc.field_1755 instanceof class_408)) {
            for (class_304 k : new class_304[]{NoSlow.mc.field_1690.field_1881, NoSlow.mc.field_1690.field_1913, NoSlow.mc.field_1690.field_1849}) {
                k.method_23481(class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)k.method_1428()).method_1444()));
            }
            NoSlow.mc.field_1690.field_1903.method_23481(ElytraFly.INSTANCE.isOn() && ElytraFly.INSTANCE.mode.is(ElytraFly.Mode.Bounce) && ElytraFly.INSTANCE.autoJump.getValue() || class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)NoSlow.mc.field_1690.field_1903.method_1428()).method_1444()));
            NoSlow.mc.field_1690.field_1894.method_23481(AutoWalk.INSTANCE.forward() || class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)NoSlow.mc.field_1690.field_1894.method_1428()).method_1444()));
            NoSlow.mc.field_1690.field_1867.method_23481(Sprint.INSTANCE.isOn() && !Sprint.INSTANCE.inWater() || class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)NoSlow.mc.field_1690.field_1867.method_1428()).method_1444()));
            if (this.allowSneak.getValue()) {
                NoSlow.mc.field_1690.field_1832.method_23481(class_3675.method_15987((long)mc.method_22683().method_4490(), (int)class_3675.method_15981((String)NoSlow.mc.field_1690.field_1832.method_1428()).method_1444()));
            }
            NoSlow.mc.field_1724.field_3913.field_3910 = NoSlow.mc.field_1690.field_1894.method_1434();
            NoSlow.mc.field_1724.field_3913.field_3909 = NoSlow.mc.field_1690.field_1881.method_1434();
            NoSlow.mc.field_1724.field_3913.field_3908 = NoSlow.mc.field_1690.field_1913.method_1434();
            NoSlow.mc.field_1724.field_3913.field_3906 = NoSlow.mc.field_1690.field_1849.method_1434();
            NoSlow.mc.field_1724.field_3913.field_3905 = NoSlow.getMovementMultiplier(NoSlow.mc.field_1724.field_3913.field_3910, NoSlow.mc.field_1724.field_3913.field_3909);
            NoSlow.mc.field_1724.field_3913.field_3907 = NoSlow.getMovementMultiplier(NoSlow.mc.field_1724.field_3913.field_3908, NoSlow.mc.field_1724.field_3913.field_3906);
            NoSlow.mc.field_1724.field_3913.field_3904 = NoSlow.mc.field_1690.field_1903.method_1434();
            NoSlow.mc.field_1724.field_3913.field_3903 = NoSlow.mc.field_1690.field_1832.method_1434();
        }
    }

    @EventListener
    public void onUse(InteractItemEvent event) {
        if (event.isPre()) {
            if (this.delay > 0) {
                NoSlow.mc.field_1752 = 0;
                event.cancel();
            } else if (this.mode.is(Mode.GrimPacket) && NoSlow.mc.field_1724 != null && NoSlow.mc.field_1724.method_5998(event.hand).method_7909().method_57347().method_57832(class_9334.field_50075)) {
                NoSlow.mc.field_1761.method_2906(NoSlow.mc.field_1724.field_7512.field_7763, 1, 0, class_1713.field_7790, (class_1657)NoSlow.mc.field_1724);
            }
        }
    }

    @EventListener
    public void onPacketSend(PacketEvent.Send e) {
        if (!NoSlow.nullCheck()) {
            class_2886 packet;
            class_2596<?> class_25962;
            if (this.mode.is(Mode.Drop) && (class_25962 = e.getPacket()) instanceof class_2886 && (packet = (class_2886)class_25962).method_12551() == class_1268.field_5808 && NoSlow.mc.field_1724.method_6047().method_7909().method_57347().method_57832(class_9334.field_50075)) {
                mc.method_1562().method_52787((class_2596)new class_2846(class_2846.class_2847.field_12975, class_2338.field_10980, class_2350.field_11033));
            } else if (MovementUtil.isMoving() && !this.pause.get()) {
                class_2596<?> class_25963 = e.getPacket();
                if (class_25963 instanceof class_2813) {
                    class_2813 click = (class_2813)class_25963;
                    switch (this.clickBypass.getValue().ordinal()) {
                        case 1: {
                            if (!NoSlow.mc.field_1724.method_24828() || NoSlow.mc.field_1687.method_20812((class_1297)NoSlow.mc.field_1724, NoSlow.mc.field_1724.method_5829().method_989(0.0, 0.0656, 0.0)).iterator().hasNext()) break;
                            if (NoSlow.mc.field_1724.method_5624()) {
                                mc.method_1562().method_52787((class_2596)new class_2848((class_1297)NoSlow.mc.field_1724, class_2848.class_2849.field_12985));
                            }
                            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(NoSlow.mc.field_1724.method_23317(), NoSlow.mc.field_1724.method_23318() + 0.0656, NoSlow.mc.field_1724.method_23321(), false));
                            break;
                        }
                        case 2: {
                            if (!NoSlow.mc.field_1724.method_24828() || NoSlow.mc.field_1687.method_20812((class_1297)NoSlow.mc.field_1724, NoSlow.mc.field_1724.method_5829().method_989(0.0, 2.71875E-7, 0.0)).iterator().hasNext()) break;
                            if (NoSlow.mc.field_1724.method_5624()) {
                                mc.method_1562().method_52787((class_2596)new class_2848((class_1297)NoSlow.mc.field_1724, class_2848.class_2849.field_12985));
                            }
                            mc.method_1562().method_52787((class_2596)new class_2828.class_2829(NoSlow.mc.field_1724.method_23317(), NoSlow.mc.field_1724.method_23318() + 2.71875E-7, NoSlow.mc.field_1724.method_23321(), false));
                            break;
                        }
                        case 3: {
                            if (click.method_12195() == class_1713.field_7790 || click.method_12195() == class_1713.field_7793) break;
                            mc.method_1562().method_52787((class_2596)new class_2815(0));
                            break;
                        }
                        case 4: {
                            this.storedClicks.add(click);
                            e.cancel();
                        }
                    }
                }
                if (e.getPacket() instanceof class_2815 && this.clickBypass.is(Bypass.Delay)) {
                    this.pause.set(true);
                    while (!this.storedClicks.isEmpty()) {
                        mc.method_1562().method_52787((class_2596)this.storedClicks.poll());
                    }
                    this.pause.set(false);
                }
            }
        }
    }

    @EventListener
    public void onPacketSendPost(PacketEvent.Sent e) {
        if (e.getPacket() instanceof class_2813 && NoSlow.mc.field_1724.method_5624() && this.clickBypass.is(Bypass.NCP)) {
            mc.method_1562().method_52787((class_2596)new class_2848((class_1297)NoSlow.mc.field_1724, class_2848.class_2849.field_12981));
        }
    }

    public boolean noSlow() {
        return this.isOn() && this.mode.getValue() != Mode.None && (this.mode.getValue() != Mode.Drop && this.mode.getValue() != Mode.GrimPacket || this.using);
    }

    public boolean soulSand() {
        return this.isOn() && this.soulSand.getValue();
    }

    public boolean climb() {
        return this.isOn() && this.climb.getValue();
    }

    public static enum Mode {
        Vanilla,
        NCP,
        Grim,
        GrimPacket,
        Drop,
        None;

    }

    private static enum Bypass {
        None,
        NCP,
        NCP2,
        Grim,
        Delay;

    }
}

