/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1667
 *  net.minecraft.class_1683
 *  net.minecraft.class_1684
 *  net.minecraft.class_1753
 *  net.minecraft.class_1764
 *  net.minecraft.class_1771
 *  net.minecraft.class_1776
 *  net.minecraft.class_1779
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1803
 *  net.minecraft.class_1823
 *  net.minecraft.class_1828
 *  net.minecraft.class_1835
 *  net.minecraft.class_1890
 *  net.minecraft.class_1893
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 *  net.minecraft.class_4587
 *  net.minecraft.class_6880
 */
package dev.luminous.mod.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.KeyPearl;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1667;
import net.minecraft.class_1683;
import net.minecraft.class_1684;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1771;
import net.minecraft.class_1776;
import net.minecraft.class_1779;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1803;
import net.minecraft.class_1823;
import net.minecraft.class_1828;
import net.minecraft.class_1835;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4587;
import net.minecraft.class_6880;

public class Trajectories
extends Module {
    static class_4587 matrixStack;
    private final ColorSetting hand = this.add(new ColorSetting("Hand", new Color(255, 255, 255, 255)).injectBoolean(true));
    private final ColorSetting pearl = this.add(new ColorSetting("Pearl", new Color(255, 255, 255, 255)).injectBoolean(true));
    private final ColorSetting arrow = this.add(new ColorSetting("Arrow", new Color(255, 255, 255, 255)).injectBoolean(true));
    private final ColorSetting xp = this.add(new ColorSetting("XP", new Color(255, 255, 255, 255)).injectBoolean(true));

    public Trajectories() {
        super("Trajectories", Module.Category.Render);
        this.setChinese("\u629b\u7269\u7ebf\u9884\u6d4b");
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        if (!Trajectories.nullCheck()) {
            Trajectories.matrixStack = matrixStack;
            if (this.pearl.booleanValue || this.arrow.booleanValue || this.xp.booleanValue) {
                RenderSystem.disableDepthTest();
                for (class_1297 en : Alien.THREAD.getEntities()) {
                    if (en instanceof class_1684 && this.pearl.booleanValue) {
                        this.calcTrajectory(en, this.pearl.getValue());
                    }
                    if (en instanceof class_1667 && this.arrow.booleanValue) {
                        this.calcTrajectory(en, this.arrow.getValue());
                    }
                    if (!(en instanceof class_1683) || !this.xp.booleanValue) continue;
                    this.calcTrajectory(en, this.xp.getValue());
                }
                RenderSystem.enableDepthTest();
            }
            if (this.hand.booleanValue) {
                class_1268 hand;
                if (!Trajectories.mc.field_1690.method_31044().method_31034()) {
                    return;
                }
                class_1799 mainHand = Trajectories.mc.field_1724.method_6047();
                class_1799 offHand = Trajectories.mc.field_1724.method_6079();
                if (!(mainHand.method_7909() instanceof class_1753 || mainHand.method_7909() instanceof class_1764 || this.isThrowable(mainHand.method_7909()) || KeyPearl.INSTANCE.isOn())) {
                    if (!(offHand.method_7909() instanceof class_1753 || offHand.method_7909() instanceof class_1764 || this.isThrowable(offHand.method_7909()))) {
                        return;
                    }
                    hand = class_1268.field_5810;
                } else {
                    hand = class_1268.field_5808;
                }
                RenderSystem.disableDepthTest();
                boolean prev_bob = (Boolean)Trajectories.mc.field_1690.method_42448().method_41753();
                Trajectories.mc.field_1690.method_42448().method_41748((Object)false);
                double x = MathUtil.interpolate(Trajectories.mc.field_1724.field_6014, Trajectories.mc.field_1724.method_23317(), (double)mc.method_60646().method_60637(true));
                double y = MathUtil.interpolate(Trajectories.mc.field_1724.field_6036, Trajectories.mc.field_1724.method_23318(), (double)mc.method_60646().method_60637(true));
                double z = MathUtil.interpolate(Trajectories.mc.field_1724.field_5969, Trajectories.mc.field_1724.method_23321(), (double)mc.method_60646().method_60637(true));
                if (offHand.method_7909() instanceof class_1764 && class_1890.method_8225((class_6880)((class_6880)Trajectories.mc.field_1687.method_30349().method_46762(class_1893.field_9108.method_58273()).method_46746(class_1893.field_9108).get()), (class_1799)offHand) != 0 || mainHand.method_7909() instanceof class_1764 && class_1890.method_8225((class_6880)((class_6880)Trajectories.mc.field_1687.method_30349().method_46762(class_1893.field_9108.method_58273()).method_46746(class_1893.field_9108).get()), (class_1799)mainHand) != 0) {
                    this.calcTrajectory(hand == class_1268.field_5810 ? offHand.method_7909() : mainHand.method_7909(), Trajectories.mc.field_1724.method_36454() - 10.0f, x, y, z);
                    this.calcTrajectory(hand == class_1268.field_5810 ? offHand.method_7909() : mainHand.method_7909(), Trajectories.mc.field_1724.method_36454(), x, y, z);
                    this.calcTrajectory(hand == class_1268.field_5810 ? offHand.method_7909() : mainHand.method_7909(), Trajectories.mc.field_1724.method_36454() + 10.0f, x, y, z);
                } else {
                    this.calcTrajectory(hand == class_1268.field_5810 ? offHand.method_7909() : mainHand.method_7909(), Trajectories.mc.field_1724.method_36454(), x, y, z);
                }
                Trajectories.mc.field_1690.method_42448().method_41748((Object)prev_bob);
                RenderSystem.enableDepthTest();
            }
        }
    }

    private void calcTrajectory(class_1297 e, Color color) {
        double motionX = e.method_18798().field_1352;
        double motionY = e.method_18798().field_1351;
        double motionZ = e.method_18798().field_1350;
        if (motionX != 0.0 || motionY != 0.0 || motionZ != 0.0) {
            double x = e.method_23317();
            double y = e.method_23318();
            double z = e.method_23321();
            for (int i = 0; i < 300; ++i) {
                class_243 lastPos = new class_243(x, y, z);
                if (Trajectories.mc.field_1687.method_8320(new class_2338((int)(x += motionX), (int)(y += motionY), (int)(z += motionZ))).method_26204() == class_2246.field_10382) {
                    motionX *= 0.8;
                    motionY *= 0.8;
                    motionZ *= 0.8;
                } else {
                    motionX *= 0.99;
                    motionY *= 0.99;
                    motionZ *= 0.99;
                }
                motionY = e instanceof class_1667 ? (motionY -= (double)0.05f) : (motionY -= (double)0.03f);
                class_243 pos = new class_243(x, y, z);
                if (Trajectories.mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)Trajectories.mc.field_1724)) != null && (Trajectories.mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)Trajectories.mc.field_1724)).method_17783() == class_239.class_240.field_1331 || Trajectories.mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)Trajectories.mc.field_1724)).method_17783() == class_239.class_240.field_1332) || y <= -65.0) break;
                int alpha = (int)MathUtil.clamp(255.0f * ((float)(i + 1) / 10.0f), 0.0f, 255.0f);
                Render3DUtil.drawLine(lastPos, pos, ColorUtil.injectAlpha(color, alpha));
            }
        }
    }

    private void calcTrajectory(class_1792 item, float yaw, double x, double y, double z) {
        y = y + (double)Trajectories.mc.field_1724.method_18381(Trajectories.mc.field_1724.method_18376()) - 0.1000000014901161;
        if (item == Trajectories.mc.field_1724.method_6047().method_7909()) {
            x -= (double)(class_3532.method_15362((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
            z -= (double)(class_3532.method_15374((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
        } else {
            x += (double)(class_3532.method_15362((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
            z += (double)(class_3532.method_15374((float)(yaw / 180.0f * (float)Math.PI)) * 0.16f);
        }
        float maxDist = this.getDistance(item);
        double motionX = -class_3532.method_15374((float)(yaw / 180.0f * (float)Math.PI)) * class_3532.method_15362((float)(Trajectories.mc.field_1724.method_36455() / 180.0f * (float)Math.PI)) * maxDist;
        double motionY = -class_3532.method_15374((float)((Trajectories.mc.field_1724.method_36455() - (float)this.getThrowPitch(item)) / 180.0f * 3.141593f)) * maxDist;
        double motionZ = class_3532.method_15362((float)(yaw / 180.0f * (float)Math.PI)) * class_3532.method_15362((float)(Trajectories.mc.field_1724.method_36455() / 180.0f * (float)Math.PI)) * maxDist;
        float power = (float)Trajectories.mc.field_1724.method_6048() / 20.0f;
        power = (power * power + power * 2.0f) / 3.0f;
        if (power > 1.0f) {
            power = 1.0f;
        }
        float distance = class_3532.method_15355((float)((float)(motionX * motionX + motionY * motionY + motionZ * motionZ)));
        motionX /= (double)distance;
        motionY /= (double)distance;
        motionZ /= (double)distance;
        float pow = (item instanceof class_1753 ? power * 2.0f : (item instanceof class_1764 ? 2.2f : 1.0f)) * this.getThrowVelocity(item);
        motionX *= (double)pow;
        motionY *= (double)pow;
        motionZ *= (double)pow;
        motionX += Trajectories.mc.field_1724.method_18798().method_10216();
        motionY += Trajectories.mc.field_1724.method_18798().method_10214();
        motionZ += Trajectories.mc.field_1724.method_18798().method_10215();
        for (int i = 0; i < 300; ++i) {
            class_3965 bhr;
            class_243 lastPos = new class_243(x, y, z);
            if (Trajectories.mc.field_1687.method_8320(new class_2338((int)(x += motionX), (int)(y += motionY), (int)(z += motionZ))).method_26204() == class_2246.field_10382) {
                motionX *= 0.8;
                motionY *= 0.8;
                motionZ *= 0.8;
            } else {
                motionX *= 0.99;
                motionY *= 0.99;
                motionZ *= 0.99;
            }
            motionY = item instanceof class_1753 ? (motionY -= (double)0.05f) : (Trajectories.mc.field_1724.method_6047().method_7909() instanceof class_1764 ? (motionY -= (double)0.05f) : (motionY -= (double)0.03f));
            class_243 pos = new class_243(x, y, z);
            for (class_1297 ent : Alien.THREAD.getEntities()) {
                if (ent instanceof class_1667 || ent.equals((Object)Trajectories.mc.field_1724) || !ent.method_5829().method_994(new class_238(x - 0.3, y - 0.3, z - 0.3, x + 0.3, y + 0.3, z + 0.3))) continue;
                Render3DUtil.drawBox(matrixStack, ent.method_5829(), this.hand.getValue());
                break;
            }
            if ((bhr = Trajectories.mc.field_1687.method_17742(new class_3959(lastPos, pos, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)Trajectories.mc.field_1724))) != null && bhr.method_17783() == class_239.class_240.field_1332) {
                Render3DUtil.drawBox(matrixStack, new class_238(bhr.method_17777()), this.hand.getValue());
                break;
            }
            if (y <= -65.0) break;
            if (motionX == 0.0 && motionY == 0.0 && motionZ == 0.0) continue;
            Render3DUtil.drawLine(lastPos, pos, this.hand.getValue());
        }
    }

    private boolean isThrowable(class_1792 item) {
        return item instanceof class_1776 || item instanceof class_1835 || item instanceof class_1779 || item instanceof class_1823 || item instanceof class_1771 || item instanceof class_1828 || item instanceof class_1803;
    }

    private float getDistance(class_1792 item) {
        return item instanceof class_1753 ? 1.0f : 0.4f;
    }

    private float getThrowVelocity(class_1792 item) {
        if (item instanceof class_1828 || item instanceof class_1803) {
            return 0.5f;
        }
        if (item instanceof class_1779) {
            return 0.59f;
        }
        return item instanceof class_1835 ? 2.0f : 1.5f;
    }

    private int getThrowPitch(class_1792 item) {
        return !(item instanceof class_1828) && !(item instanceof class_1803) && !(item instanceof class_1779) ? 0 : 20;
    }
}

