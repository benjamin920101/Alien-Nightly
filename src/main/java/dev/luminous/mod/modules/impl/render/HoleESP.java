/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_4587
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_4587;

public class HoleESP
extends Module {
    public static HoleESP INSTANCE;
    public final SliderSetting startFade = this.add(new SliderSetting("StartFade", 5.0, 1.0, 20.0));
    public final SliderSetting distance = this.add(new SliderSetting("Distance", 6.0, 1.0, 20.0));
    public final SliderSetting airHeight = this.add(new SliderSetting("AirHeight", 1.0, -3.0, 3.0, 0.01));
    public final BooleanSetting airYCheck = this.add(new BooleanSetting("AirYCheck", true));
    public final SliderSetting height = this.add(new SliderSetting("Height", 1.0, -3.0, 3.0, 0.1));
    public final SliderSetting wallHeight = this.add(new SliderSetting("WallHeight", 3.0, -3.0, 3.0, 0.1));
    public final BooleanSetting sideCheck = this.add(new BooleanSetting("SideCheck", true));
    private final ColorSetting airFill = this.add(new ColorSetting("AirFill", new Color(148, 0, 0, 100)).injectBoolean(true));
    private final ColorSetting airBox = this.add(new ColorSetting("AirBox", new Color(148, 0, 0, 100)).injectBoolean(true));
    private final ColorSetting airFade = this.add(new ColorSetting("AirFade", new Color(148, 0, 0, 0)).injectBoolean(true));
    private final ColorSetting normalFill = this.add(new ColorSetting("UnsafeFill", new Color(255, 0, 0, 50)).injectBoolean(true));
    private final ColorSetting normalBox = this.add(new ColorSetting("UnsafeBox", new Color(255, 0, 0, 100)).injectBoolean(true));
    private final ColorSetting normalFade = this.add(new ColorSetting("UnsafeFade", new Color(255, 0, 0, 0)).injectBoolean(true));
    private final ColorSetting bedrockFill = this.add(new ColorSetting("SafeFill", new Color(8, 255, 79, 50)).injectBoolean(true));
    private final ColorSetting bedrockBox = this.add(new ColorSetting("SafeBox", new Color(8, 255, 79, 100)).injectBoolean(true));
    private final ColorSetting bedrockFade = this.add(new ColorSetting("SafeFade", new Color(8, 255, 79, 100)).injectBoolean(true));
    private final ColorSetting wallFill = this.add(new ColorSetting("WallFill", new Color(0, 255, 255, 128)).injectBoolean(true));
    private final ColorSetting wallBox = this.add(new ColorSetting("WallBox", new Color(0, 225, 255, 255)).injectBoolean(true));
    private final ColorSetting wallFade = this.add(new ColorSetting("WallFade", new Color(0, 255, 255, 64)).injectBoolean(true));
    private final ColorSetting wallSideFill = this.add(new ColorSetting("WallSideFill", new Color(0, 255, 255, 128)).injectBoolean(true));
    private final ColorSetting wallSideBox = this.add(new ColorSetting("WallSideBox", new Color(0, 225, 255, 255)).injectBoolean(true));
    private final ColorSetting wallSideFade = this.add(new ColorSetting("WallSideFade", new Color(0, 255, 255, 64)).injectBoolean(true));
    private final SliderSetting updateDelay = this.add(new SliderSetting("UpdateDelay", 50, 0, 1000));
    private final List<class_2338> tempNormalList = new ArrayList<class_2338>();
    private final List<class_2338> tempBedrockList = new ArrayList<class_2338>();
    private final List<class_2338> tempAirList = new ArrayList<class_2338>();
    private final List<class_2338> tempWallList = new ArrayList<class_2338>();
    private final List<class_2338> tempWallSideList = new ArrayList<class_2338>();
    private final Timer timer = new Timer();
    boolean drawing = false;
    private List<class_2338> normalList = new ArrayList<class_2338>();
    private List<class_2338> bedrockList = new ArrayList<class_2338>();
    private List<class_2338> airList = new ArrayList<class_2338>();
    private List<class_2338> wallList = new ArrayList<class_2338>();
    private List<class_2338> wallSideList = new ArrayList<class_2338>();

    public HoleESP() {
        super("HoleESP", Module.Category.Render);
        this.setChinese("\u5751\u900f\u89c6");
        INSTANCE = this;
    }

    public void onThread() {
        if (!HoleESP.nullCheck() && !this.isOff() && !this.drawing && this.timer.passedMs(this.updateDelay.getValue())) {
            this.normalList = new ArrayList<class_2338>(this.tempNormalList);
            this.bedrockList = new ArrayList<class_2338>(this.tempBedrockList);
            this.airList = new ArrayList<class_2338>(this.tempAirList);
            this.wallList = new ArrayList<class_2338>(this.tempWallList);
            this.wallSideList = new ArrayList<class_2338>(this.tempWallSideList);
            this.timer.reset();
            this.tempBedrockList.clear();
            this.tempNormalList.clear();
            this.tempAirList.clear();
            this.tempWallList.clear();
            this.tempWallSideList.clear();
            for (class_2338 pos : BlockUtil.getSphere(this.distance.getValueFloat(), HoleESP.mc.field_1724.method_19538())) {
                Type type;
                if (this.isBedrock(pos) && this.isBedrock(pos.method_10086(2)) && this.isBedrock(pos.method_10074())) {
                    class_2350 side = this.getWallSide(pos);
                    if (side != null || !this.sideCheck.getValue()) {
                        this.tempWallList.add(pos);
                    }
                    if (side != null) {
                        this.tempWallSideList.add(pos.method_10093(side));
                    }
                }
                if ((type = this.isHole(pos)) == Type.Bedrock) {
                    this.tempBedrockList.add(pos);
                    continue;
                }
                if (type == Type.Normal) {
                    this.tempNormalList.add(pos);
                    continue;
                }
                if (type != Type.Air) continue;
                this.tempAirList.add(pos);
            }
        }
    }

    private class_2350 getWallSide(class_2338 pos) {
        double distance = Double.MAX_VALUE;
        class_2350 side = null;
        for (class_2350 direction : class_2350.values()) {
            class_2338 offsetPos;
            if (direction == class_2350.field_11036 || direction == class_2350.field_11033 || !BlockUtil.canCollide(new class_238((offsetPos = pos.method_10093(direction)).method_10074())) || BlockUtil.canCollide(new class_238(offsetPos)) || BlockUtil.canCollide(new class_238(offsetPos.method_10084()))) continue;
            if (side == null) {
                side = direction;
                distance = HoleESP.mc.field_1724.method_33571().method_1022(offsetPos.method_46558());
                continue;
            }
            if (!(HoleESP.mc.field_1724.method_33571().method_1022(offsetPos.method_46558()) < distance)) continue;
            side = direction;
            distance = HoleESP.mc.field_1724.method_33571().method_1022(offsetPos.method_46558());
        }
        return side;
    }

    private boolean isBedrock(class_2338 pos) {
        return HoleESP.mc.field_1687.method_8320(pos).method_26204() == class_2246.field_9987;
    }

    Type isHole(class_2338 pos) {
        if (HoleESP.mc.field_1687.method_22347(pos) && (!this.airYCheck.getValue() || pos.method_10264() == HoleESP.mc.field_1724.method_31478() - 1 || pos.method_10264() == HoleESP.mc.field_1724.method_31478()) && Alien.HOLE.isHard(pos.method_10084())) {
            return Type.Air;
        }
        int blockProgress = 0;
        boolean bedRock = true;
        for (class_2350 i : class_2350.values()) {
            if (i == class_2350.field_11036 || i == class_2350.field_11033 || !Alien.HOLE.isHard(pos.method_10093(i))) continue;
            if (HoleESP.mc.field_1687.method_8320(pos.method_10093(i)).method_26204() != class_2246.field_9987) {
                bedRock = false;
            }
            ++blockProgress;
        }
        if (HoleESP.mc.field_1687.method_22347(pos) && HoleESP.mc.field_1687.method_22347(pos.method_10084()) && HoleESP.mc.field_1687.method_22347(pos.method_10086(2)) && blockProgress > 3 && BlockUtil.canCollide((class_1297)HoleESP.mc.field_1724, new class_238(pos.method_10074()))) {
            return bedRock ? Type.Bedrock : Type.Normal;
        }
        return Alien.HOLE.isDoubleHole(pos) ? Type.Normal : Type.None;
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        this.drawing = true;
        this.draw(matrixStack, this.bedrockList, this.bedrockFill, this.bedrockFade, this.bedrockBox, this.height.getValue());
        this.draw(matrixStack, this.airList, this.airFill, this.airFade, this.airBox, this.airHeight.getValue());
        this.draw(matrixStack, this.normalList, this.normalFill, this.normalFade, this.normalBox, this.height.getValue());
        this.draw(matrixStack, this.wallList, this.wallFill, this.wallFade, this.wallBox, this.wallHeight.getValue());
        this.draw(matrixStack, this.wallSideList, this.wallSideFill, this.wallSideFade, this.wallSideBox, this.height.getValue());
        this.drawing = false;
    }

    private void draw(class_4587 matrixStack, List<class_2338> list, ColorSetting fill, ColorSetting fade, ColorSetting box, double height) {
        for (class_2338 pos : list) {
            double distance = HoleESP.mc.field_1724.method_19538().method_1022(pos.method_46558());
            double alpha = distance > this.startFade.getValue() ? Math.max(Math.min(1.0, 1.0 - (distance - this.startFade.getValue()) / (this.distance.getValue() - this.startFade.getValue())), 0.0) : 1.0;
            class_238 espBox = new class_238((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260(), (double)(pos.method_10263() + 1), (double)pos.method_10264() + height, (double)(pos.method_10260() + 1));
            if (fill.booleanValue) {
                if (fade.booleanValue) {
                    Render3DUtil.drawFadeFill(matrixStack, espBox, ColorUtil.injectAlpha(fill.getValue(), (int)((double)fill.getValue().getAlpha() * alpha)), ColorUtil.injectAlpha(fade.getValue(), (int)((double)fade.getValue().getAlpha() * alpha)));
                } else {
                    Render3DUtil.drawFill(matrixStack, espBox, ColorUtil.injectAlpha(fill.getValue(), (int)((double)fill.getValue().getAlpha() * alpha)));
                }
            }
            if (!box.booleanValue) continue;
            Render3DUtil.drawBox(matrixStack, espBox, ColorUtil.injectAlpha(box.getValue(), (int)((double)box.getValue().getAlpha() * alpha)));
        }
    }

    public static enum Type {
        None,
        Air,
        Normal,
        Bedrock;

    }
}

