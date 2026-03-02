/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1739
 *  net.minecraft.class_1799
 *  net.minecraft.class_1890
 *  net.minecraft.class_1893
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_2561
 *  net.minecraft.class_2680
 *  net.minecraft.class_4587
 *  net.minecraft.class_6880
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.player.InventoryUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.core.impl.BreakManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.class_1657;
import net.minecraft.class_1739;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_4587;
import net.minecraft.class_6880;

public class BreakESP
extends Module {
    public static BreakESP INSTANCE;
    private final BooleanSetting progress = this.add(new BooleanSetting("Progress", true));
    private final SliderSetting damage = this.add(new SliderSetting("Damage", 1.0, 0.0, 2.0, 0.01));
    private final ColorSetting box = this.add(new ColorSetting("Box", new Color(198, 176, 12, 255)).injectBoolean(true));
    private final ColorSetting fill = this.add(new ColorSetting("Fill", new Color(198, 176, 12, 78)).injectBoolean(true));
    private final ColorSetting boxFriend = this.add(new ColorSetting("FriendBox", new Color(30, 45, 169, 255)).injectBoolean(true));
    private final ColorSetting fillFriend = this.add(new ColorSetting("FriendFill", new Color(30, 45, 169, 78)).injectBoolean(true));
    private final EnumSetting<Easing> ease = this.add(new EnumSetting<Easing>("Ease", Easing.CubicInOut));
    private final BooleanSetting second = this.add(new BooleanSetting("Second", true));
    private final ColorSetting secondBox = this.add(new ColorSetting("SecondBox", new Color(255, 255, 255, 255)).injectBoolean(true));
    private final ColorSetting secondFill = this.add(new ColorSetting("SecondFill", new Color(255, 255, 255, 100)).injectBoolean(true));
    final DecimalFormat df = new DecimalFormat("0.0");
    final Color startColor = new Color(255, 6, 6);
    final Color endColor = new Color(0, 255, 12);
    final Color doubleColor = new Color(255, 179, 96);

    public BreakESP() {
        super("BreakESP", Module.Category.Render);
        this.setChinese("\u6316\u6398\u663e\u793a");
        INSTANCE = this;
    }

    private Color getFillColor(class_1657 player) {
        return Alien.FRIEND.isFriend(player) ? this.fillFriend.getValue() : this.fill.getValue();
    }

    private Color getBoxColor(class_1657 player) {
        return Alien.FRIEND.isFriend(player) ? this.boxFriend.getValue() : this.box.getValue();
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        for (BreakManager.BreakData breakData : Alien.BREAK.breakMap.values()) {
            if (breakData == null || breakData.getEntity() == null) continue;
            class_1657 player = (class_1657)breakData.getEntity();
            double size = 0.5 * (1.0 - breakData.fade.ease(this.ease.getValue()));
            class_238 cbox = new class_238(breakData.pos).method_1002(size, size, size).method_1002(-size, -size, -size);
            if (this.fill.booleanValue) {
                Render3DUtil.drawFill(matrixStack, cbox, this.getFillColor(player));
            }
            if (this.box.booleanValue) {
                Render3DUtil.drawBox(matrixStack, cbox, this.getBoxColor(player));
            }
            Render3DUtil.drawText3D(player.method_5477().getString(), breakData.pos.method_46558().method_1031(0.0, this.progress.getValue() ? 0.15 : 0.0, 0.0), -1);
            if (!this.progress.getValue()) continue;
            Render3DUtil.drawText3D(class_2561.method_30163((String)(breakData.failed ? "\u00a74Failed" : (breakData.complete ? "Broke" : this.df.format(Math.min(1.0, (double)breakData.timer.getMs() / breakData.breakTime) * 100.0)))), breakData.pos.method_46558().method_1031(0.0, -0.15, 0.0), 0.0, 0.0, 1.0, breakData.complete ? (BreakESP.mc.field_1687.method_22347(breakData.pos) ? this.endColor : this.startColor) : ColorUtil.fadeColor(this.startColor, this.endColor, (double)breakData.timer.getMs() / breakData.breakTime));
        }
        if (this.second.getValue()) {
            Iterator<BreakManager.BreakData> iterator = ((ConcurrentHashMap.KeySetView)Alien.BREAK.doubleMap.keySet()).iterator();
            while (iterator.hasNext()) {
                int i = (Integer)((Object)iterator.next());
                BreakManager.BreakData breakDatax = Alien.BREAK.doubleMap.get(i);
                if (breakDatax != null && breakDatax.getEntity() != null && !BreakESP.mc.field_1687.method_22347(breakDatax.pos)) {
                    BreakManager.BreakData singleBreakData = Alien.BREAK.breakMap.get(i);
                    if (singleBreakData != null && singleBreakData.pos.equals((Object)breakDatax.pos)) continue;
                    double sizex = 0.5 * (1.0 - breakDatax.fade.ease(this.ease.getValue()));
                    class_238 cboxx = new class_238(breakDatax.pos).method_1002(sizex, sizex, sizex).method_1002(-sizex, -sizex, -sizex);
                    if (this.secondFill.booleanValue) {
                        Render3DUtil.drawFill(matrixStack, cboxx, this.secondFill.getValue());
                    }
                    if (this.secondBox.booleanValue) {
                        Render3DUtil.drawBox(matrixStack, cboxx, this.secondBox.getValue());
                    }
                    Render3DUtil.drawText3D(breakDatax.getEntity().method_5477().getString(), breakDatax.pos.method_46558().method_1031(0.0, 0.15, 0.0), -1);
                    Render3DUtil.drawText3D("Double", breakDatax.pos.method_46558().method_1031(0.0, -0.15, 0.0), this.doubleColor.getRGB());
                    continue;
                }
                Alien.BREAK.doubleMap.remove(i);
            }
        }
    }

    public static double getBreakTime(class_2338 pos, boolean extraBreak) {
        int slot = BreakESP.getTool(pos);
        if (slot == -1) {
            slot = BreakESP.mc.field_1724.method_31548().field_7545;
        }
        return BreakESP.getBreakTime(pos, slot, extraBreak ? 1.0 : BreakESP.INSTANCE.damage.getValue());
    }

    static int getTool(class_2338 pos) {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        float CurrentFastest = 1.0f;
        for (Map.Entry<Integer, class_1799> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            float destroySpeed;
            float digSpeed;
            if (entry.getValue().method_7909() instanceof class_1739 || !((digSpeed = (float)class_1890.method_8225((class_6880)((class_6880)BreakESP.mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get()), (class_1799)entry.getValue())) + (destroySpeed = entry.getValue().method_7924(BreakESP.mc.field_1687.method_8320(pos))) > CurrentFastest)) continue;
            CurrentFastest = digSpeed + destroySpeed;
            slot.set(entry.getKey());
        }
        return slot.get();
    }

    static double getBreakTime(class_2338 pos, int slot, double damage) {
        return (double)(1.0f / BreakESP.getBlockStrength(pos, BreakESP.mc.field_1724.method_31548().method_5438(slot)) / 20.0f * 1000.0f) * damage;
    }

    static float getBlockStrength(class_2338 position, class_1799 itemStack) {
        class_2680 state = BreakESP.mc.field_1687.method_8320(position);
        float hardness = state.method_26214((class_1922)BreakESP.mc.field_1687, position);
        if (hardness < 0.0f) {
            return 0.0f;
        }
        float i = state.method_29291() && !itemStack.method_7951(state) ? 100.0f : 30.0f;
        return BreakESP.getDigSpeed(state, itemStack) / hardness / i;
    }

    static float getDigSpeed(class_2680 state, class_1799 itemStack) {
        int efficiencyModifier;
        float digSpeed = BreakESP.getDestroySpeed(state, itemStack);
        if (digSpeed > 1.0f && (efficiencyModifier = class_1890.method_8225((class_6880)((class_6880)BreakESP.mc.field_1687.method_30349().method_46762(class_1893.field_9131.method_58273()).method_46746(class_1893.field_9131).get()), (class_1799)itemStack)) > 0 && !itemStack.method_7960()) {
            digSpeed += (float)(StrictMath.pow(efficiencyModifier, 2.0) + 1.0);
        }
        return digSpeed < 0.0f ? 0.0f : digSpeed;
    }

    static float getDestroySpeed(class_2680 state, class_1799 itemStack) {
        float destroySpeed = 1.0f;
        if (itemStack != null && !itemStack.method_7960()) {
            destroySpeed *= itemStack.method_7924(state);
        }
        return destroySpeed;
    }
}

