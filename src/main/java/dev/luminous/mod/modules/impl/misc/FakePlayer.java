/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.class_1268
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1297$class_5529
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1935
 *  net.minecraft.class_2246
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2602
 *  net.minecraft.class_2663
 *  net.minecraft.class_2664
 *  net.minecraft.class_2824
 *  net.minecraft.class_2824$class_5907
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 *  net.minecraft.class_745
 */
package dev.luminous.mod.modules.impl.misc;

import com.mojang.authlib.GameProfile;
import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.DamageUtils;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.asm.accessors.ILivingEntity;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.combat.AutoAnchor;
import dev.luminous.mod.modules.impl.combat.AutoCrystal;
import dev.luminous.mod.modules.impl.combat.Criticals;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1935;
import net.minecraft.class_2246;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2602;
import net.minecraft.class_2663;
import net.minecraft.class_2664;
import net.minecraft.class_2824;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_745;

public class FakePlayer
extends Module {
    public static FakePlayer INSTANCE;
    public static FakePlayerEntity fakePlayer;
    final StringSetting name = this.add(new StringSetting("Name", "FakePlayer"));
    private final BooleanSetting damage = this.add(new BooleanSetting("Damage", true));
    private final BooleanSetting autoTotem = this.add(new BooleanSetting("AutoTotem", true));
    public final BooleanSetting record = this.add(new BooleanSetting("Record", false));
    public final BooleanSetting play = this.add(new BooleanSetting("Play", false));
    final List<PlayerState> positions = new ArrayList<PlayerState>();
    int movementTick;
    boolean lastRecordValue = false;

    public FakePlayer() {
        super("FakePlayer", Module.Category.Misc);
        this.setChinese("\u5047\u4eba");
        INSTANCE = this;
    }

    @Override
    public String getInfo() {
        return this.name.getValue();
    }

    @Override
    public boolean onEnable() {
        if (FakePlayer.nullCheck()) {
            this.disable();
        } else {
            fakePlayer = new FakePlayerEntity((class_1657)FakePlayer.mc.field_1724, this.name.getValue());
            FakePlayer.mc.field_1687.method_53875((class_1297)fakePlayer);
        }
        return false;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        if (fakePlayer != null && FakePlayer.fakePlayer.field_17892 == FakePlayer.mc.field_1687) {
            if (this.autoTotem.getValue()) {
                if (fakePlayer.method_6079().method_7909() != class_1802.field_8288) {
                    fakePlayer.method_6122(class_1268.field_5810, new class_1799((class_1935)class_1802.field_8288));
                }
                if (fakePlayer.method_6047().method_7909() != class_1802.field_8288) {
                    fakePlayer.method_6122(class_1268.field_5808, new class_1799((class_1935)class_1802.field_8288));
                }
            }
            if (this.record.getValue() != this.lastRecordValue && this.record.getValue()) {
                this.positions.clear();
            }
            this.lastRecordValue = this.record.getValue();
            if (this.record.getValue()) {
                this.positions.add(new PlayerState(FakePlayer.mc.field_1724.method_23317(), FakePlayer.mc.field_1724.method_23318(), FakePlayer.mc.field_1724.method_23321(), FakePlayer.mc.field_1724.method_36454(), FakePlayer.mc.field_1724.method_36455()));
            }
            if (this.play.getValue() && !this.positions.isEmpty()) {
                ++this.movementTick;
                if (this.movementTick >= this.positions.size()) {
                    this.movementTick = 0;
                }
                PlayerState p = this.positions.get(this.movementTick);
                fakePlayer.method_36456(p.yaw);
                fakePlayer.method_36457(p.pitch);
                fakePlayer.method_5847(p.yaw);
                fakePlayer.method_43391(p.x, p.y, p.z);
                fakePlayer.method_5759(p.x, p.y, p.z, p.yaw, p.pitch, 3);
            }
        } else {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        if (fakePlayer != null) {
            fakePlayer.method_5768();
            fakePlayer.method_31745(class_1297.class_5529.field_26998);
            fakePlayer.method_36209();
            fakePlayer = null;
        }
    }

    @EventListener
    public void onAttack(PacketEvent.Send event) {
        class_2824 packet;
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2824 && Criticals.getInteractType(packet = (class_2824)class_25962) == class_2824.class_5907.field_29172 && Criticals.getEntity(packet) == fakePlayer) {
            FakePlayer.mc.field_1687.method_43128((class_1657)FakePlayer.mc.field_1724, fakePlayer.method_23317(), fakePlayer.method_23318(), fakePlayer.method_23321(), class_3417.field_15115, class_3419.field_15248, 1.0f, 1.0f);
            float damage = DamageUtils.getAttackDamage((class_1309)FakePlayer.mc.field_1724, (class_1309)fakePlayer);
            if (!(!(FakePlayer.mc.field_1724.field_6017 > 0.0f) && (!Criticals.INSTANCE.isOn() || Criticals.INSTANCE.mode.is(Criticals.Mode.Ground) || !FakePlayer.mc.field_1724.method_24828() && Criticals.INSTANCE.onlyGround.getValue()) || FakePlayer.mc.field_1724.method_24828() && (!Criticals.INSTANCE.isOn() || Criticals.INSTANCE.mode.is(Criticals.Mode.Ground)) || FakePlayer.mc.field_1724.method_6101() || FakePlayer.mc.field_1724.method_5799() || FakePlayer.mc.field_1724.method_6059(class_1294.field_5919) || FakePlayer.mc.field_1724.method_5765())) {
                FakePlayer.mc.field_1687.method_43128((class_1657)FakePlayer.mc.field_1724, fakePlayer.method_23317(), fakePlayer.method_23318(), fakePlayer.method_23321(), class_3417.field_15016, class_3419.field_15248, 1.0f, 1.0f);
                FakePlayer.mc.field_1724.method_7277((class_1297)fakePlayer);
            }
            if (FakePlayer.fakePlayer.field_6235 <= 0) {
                fakePlayer.method_48922(FakePlayer.mc.field_1687.method_48963().method_48830());
                if (fakePlayer.method_6067() >= damage) {
                    fakePlayer.method_6073(fakePlayer.method_6067() - damage);
                } else {
                    float damage2 = damage - fakePlayer.method_6067();
                    fakePlayer.method_6073(0.0f);
                    fakePlayer.method_6033(fakePlayer.method_6032() - damage2);
                }
                if (fakePlayer.method_29504()) {
                    Alien.POP.onTotemPop((class_1657)fakePlayer);
                    if (fakePlayer.method_6095(FakePlayer.mc.field_1687.method_48963().method_48830())) {
                        fakePlayer.method_6033(10.0f);
                        new class_2663((class_1297)fakePlayer, 35).method_11471((class_2602)mc.method_1562());
                    }
                }
            }
        }
    }

    @EventListener
    public void onPacketReceive(PacketEvent.Receive event) {
        class_2596<?> class_25962;
        if (this.damage.getValue() && fakePlayer != null && FakePlayer.fakePlayer.field_6235 <= 0 && (class_25962 = event.getPacket()) instanceof class_2664) {
            class_2664 explosion = (class_2664)class_25962;
            class_243 class_2432 = new class_243(explosion.method_11475(), explosion.method_11477(), explosion.method_11478());
            if (Math.sqrt(class_2432.method_1025(fakePlayer.method_19538())) > 10.0) {
                return;
            }
            float damage = BlockUtil.getBlock(new BlockPosX(explosion.method_11475(), explosion.method_11477(), explosion.method_11478())) == class_2246.field_23152 ? (float)AutoAnchor.INSTANCE.getAnchorDamage(new BlockPosX(explosion.method_11475(), explosion.method_11477(), explosion.method_11478()), (class_1657)fakePlayer, (class_1657)fakePlayer) : AutoCrystal.INSTANCE.calculateDamage(new class_243(explosion.method_11475(), explosion.method_11477(), explosion.method_11478()), (class_1657)fakePlayer, (class_1657)fakePlayer);
            fakePlayer.method_48922(FakePlayer.mc.field_1687.method_48963().method_48830());
            if (fakePlayer.method_6067() >= damage) {
                fakePlayer.method_6073(fakePlayer.method_6067() - damage);
            } else {
                float damage2 = damage - fakePlayer.method_6067();
                fakePlayer.method_6073(0.0f);
                fakePlayer.method_6033(fakePlayer.method_6032() - damage2);
            }
            if (fakePlayer.method_29504()) {
                Alien.POP.onTotemPop((class_1657)fakePlayer);
                if (fakePlayer.method_6095(FakePlayer.mc.field_1687.method_48963().method_48830())) {
                    fakePlayer.method_6033(10.0f);
                    new class_2663((class_1297)fakePlayer, 35).method_11471((class_2602)mc.method_1562());
                }
            }
        }
    }

    public static class FakePlayerEntity
    extends class_745 {
        private final boolean onGround;

        public FakePlayerEntity(class_1657 player, String name) {
            super(Wrapper.mc.field_1687, new GameProfile(UUID.fromString("66666666-6666-6666-6666-666666666666"), name));
            this.method_5719((class_1297)player);
            this.field_6014 = player.field_6014;
            this.field_5969 = player.field_5969;
            this.field_6036 = player.field_6036;
            this.field_6283 = player.field_6283;
            this.field_6241 = player.field_6241;
            this.field_6251 = player.field_6251;
            this.field_6279 = player.field_6279;
            this.field_42108.method_48567(player.field_42108.method_48566());
            this.field_42108.field_42111 = player.field_42108.method_48569();
            ((ILivingEntity)((Object)this)).setLeaningPitch(((ILivingEntity)player).getLeaningPitch());
            ((ILivingEntity)((Object)this)).setLastLeaningPitch(((ILivingEntity)player).getLeaningPitch());
            this.field_5957 = player.method_5799();
            this.method_5660(player.method_5715());
            this.method_18380(player.method_18376());
            this.method_5729(7, player.method_6128());
            this.onGround = player.method_24828();
            this.method_24830(this.onGround);
            this.method_31548().method_7377(player.method_31548());
            this.method_52544(player.method_6067());
            this.method_6033(player.method_6032());
            this.method_5857(player.method_5829());
        }

        public boolean method_24828() {
            return this.onGround;
        }

        public boolean method_7325() {
            return false;
        }

        public boolean method_7337() {
            return false;
        }
    }

    private record PlayerState(double x, double y, double z, float yaw, float pitch) {
    }
}

