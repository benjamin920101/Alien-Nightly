/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.hit.HitResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.network.packet.Packet
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.sound.SoundEvents
 *  net.minecraft.sound.SoundCategory
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket
 *  net.minecraft.network.packet.s2c.play.GameMessageS2CPacket
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.PacketEvent;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.math.Timer;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.api.utils.render.TextUtil;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.Packet;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class Punctuation
extends Module {
    private final BooleanSetting sound = this.add(new BooleanSetting("Sound", true));
    private final SliderSetting clearTime = this.add(new SliderSetting("ClearTime", 10.0, 0.0, 100.0, 0.1).setSuffix("s"));
    private final ColorSetting color = this.add(new ColorSetting("Color", new Color(255, 255, 255, 100)));
    private final BindSetting enemySpot = this.add(new BindSetting("EnemySpot", -1));
    private final StringSetting key = this.add(new StringSetting("EncryptKey", "IDKWTFTHIS"));
    private final ConcurrentHashMap<String, Spot> waypoint = new ConcurrentHashMap();
    private boolean pressed = false;

    public Punctuation() {
        super("Punctuation", Module.Category.Misc);
        this.setChinese("\u6807\u70b9");
    }

    public static SecretKeySpec getKey(String myKey) {
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        }
        catch (Exception var3) {
            return null;
        }
    }

    @Override
    public void onDisable() {
        this.waypoint.clear();
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.waypoint.values().removeIf(t -> t.timer.passedS(this.clearTime.getValue()));
        if (this.enemySpot.isPressed() && Punctuation.mc.field_1755 == null) {
            class_239 class_2392;
            if (!this.pressed && (class_2392 = mc.method_1560().method_5745(256.0, 0.0f, false)) instanceof class_3965) {
                class_3965 blockHitResult = (class_3965)class_2392;
                class_2338 pos = blockHitResult.method_17777();
                Punctuation.mc.field_1724.field_3944.method_45729(this.Encrypt("EnemyHere{" + pos.method_10263() + "," + pos.method_10264() + "," + pos.method_10260() + "," + this.color.getValue().getRGB() + "}"));
            }
            this.pressed = true;
        } else {
            this.pressed = false;
        }
    }

    @Override
    public void onRender2D(class_332 context, float tickDelta) {
        for (Spot spot : this.waypoint.values()) {
            class_243 vector = TextUtil.worldSpaceToScreenSpace(spot.pos.method_46558().method_1031(0.0, 1.0, 0.0));
            String text = "\u00a7a" + spot.name + " \u00a7f(" + spot.pos.method_10263() + ", " + spot.pos.method_10264() + ", " + spot.pos.method_10260() + ")";
            if (!(vector.field_1350 > 0.0) || !(vector.field_1350 < 1.0)) continue;
            double posX = vector.field_1352;
            double posY = vector.field_1351;
            double endPosX = Math.max(vector.field_1352, vector.field_1350);
            float diff = (float)(endPosX - posX) / 2.0f;
            float textWidth = Punctuation.mc.field_1772.method_1727(text);
            float tagX = (float)((posX + (double)diff - (double)(textWidth / 4.0f)) * 1.0);
            context.method_51448().method_22903();
            context.method_51448().method_22905(0.5f, 0.5f, 1.0f);
            context.method_51433(Punctuation.mc.field_1772, text, (int)tagX * 2, (int)(posY - 11.0 + 10.799999999999999) * 2, -1, true);
            context.method_51448().method_22909();
        }
    }

    @Override
    public void onRender3D(class_4587 matrixStack) {
        for (Spot spot : this.waypoint.values()) {
            Render3DUtil.drawFill(matrixStack, new class_238((double)spot.pos.method_10263() + 0.25, -60.0, (double)spot.pos.method_10260() + 0.25, (double)spot.pos.method_10263() + 0.75, 320.0, (double)spot.pos.method_10260() + 0.75), spot.color);
        }
    }

    @EventListener
    private void PacketReceive(PacketEvent.Receive event) {
        if (!Punctuation.nullCheck()) {
            class_7439 packet;
            class_2596<?> class_25962 = event.getPacket();
            if (class_25962 instanceof class_7439 && (packet = (class_7439)class_25962).comp_763() != null) {
                mc.execute(() -> this.receive(packet.comp_763().getString()));
            }
            if ((class_25962 = event.getPacket()) instanceof class_7438) {
                packet = (class_7438)class_25962;
                if (packet.comp_1103() != null) {
                    mc.execute(() -> this.lambda$PacketReceive$2((class_7438)packet));
                } else {
                    mc.execute(() -> this.lambda$PacketReceive$3((class_7438)packet));
                }
            }
        }
    }

    private void receive(String s) {
        try {
            Pattern pattern;
            Matcher matcher;
            if (s == null) {
                return;
            }
            String decrypt = this.Decrypt(s.replaceAll("\u00a7[a-zA-Z0-9]", "").replaceAll("<[^>]*> ", ""));
            if (decrypt == null) {
                return;
            }
            if (decrypt.contains("EnemyHere") && (matcher = (pattern = Pattern.compile("\\{(.*?)}")).matcher(decrypt)).find()) {
                String pos = matcher.group(1);
                String[] posSplit = pos.split(",");
                if (posSplit.length == 3) {
                    if (this.sound.getValue()) {
                        Punctuation.mc.field_1687.method_8396((class_1657)Punctuation.mc.field_1724, Punctuation.mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0f, 1.9f);
                    }
                    String xString = posSplit[0];
                    String yString = posSplit[1];
                    String zString = posSplit[2];
                    pattern = Pattern.compile("<(.*?)>");
                    matcher = pattern.matcher(s);
                    if (!this.isNumeric(xString)) {
                        return;
                    }
                    double x = Double.parseDouble(xString);
                    if (!this.isNumeric(yString)) {
                        return;
                    }
                    double y = Double.parseDouble(yString);
                    if (!this.isNumeric(zString)) {
                        return;
                    }
                    double z = Double.parseDouble(zString);
                    if (matcher.find()) {
                        String sender = matcher.group(1);
                        this.waypoint.put(sender, new Spot(sender, new BlockPosX(x, y, z), this.color.getValue(), new Timer()));
                        CommandManager.sendMessage(sender + " marked at \u00a7r(" + xString + ", " + yString + ", " + zString + ")");
                    } else {
                        this.waypoint.put("" + MathUtil.random(0.0f, 1.0E9f), new Spot("Unknown", new BlockPosX(x, y, z), this.color.getValue(), new Timer()));
                        CommandManager.sendMessage("Unknown marked at \u00a7r(" + xString + ", " + yString + ", " + zString + ")");
                    }
                } else if (posSplit.length == 4) {
                    if (this.sound.getValue()) {
                        Punctuation.mc.field_1687.method_8396((class_1657)Punctuation.mc.field_1724, Punctuation.mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0f, 1.9f);
                    }
                    String xStringx = posSplit[0];
                    String yStringx = posSplit[1];
                    String zStringx = posSplit[2];
                    String colorString = posSplit[3];
                    pattern = Pattern.compile("<(.*?)>");
                    matcher = pattern.matcher(s);
                    if (!this.isNumeric(xStringx)) {
                        return;
                    }
                    double xx = Double.parseDouble(xStringx);
                    if (!this.isNumeric(yStringx)) {
                        return;
                    }
                    double yx = Double.parseDouble(yStringx);
                    if (!this.isNumeric(zStringx)) {
                        return;
                    }
                    double z = Double.parseDouble(zStringx);
                    if (!this.isNumeric(colorString)) {
                        return;
                    }
                    double color = Double.parseDouble(colorString);
                    if (matcher.find()) {
                        String sender = matcher.group(1);
                        this.waypoint.put(sender, new Spot(sender, new BlockPosX(xx, yx, z), new Color((int)color, true), new Timer()));
                        CommandManager.sendMessage(sender + " marked at \u00a7r(" + xStringx + ", " + yStringx + ", " + zStringx + ")");
                    } else {
                        this.waypoint.put("" + MathUtil.random(0.0f, 1.0E9f), new Spot("Unknown", new BlockPosX(xx, yx, z), new Color((int)color, true), new Timer()));
                        CommandManager.sendMessage("Unknown marked at \u00a7r(" + xStringx + ", " + yStringx + ", " + zStringx + ")");
                    }
                }
            }
        }
        catch (Exception var20) {
            var20.printStackTrace();
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public String Decrypt(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKey = Punctuation.getKey(this.key.getValue());
            byte[] iv = new byte[16];
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(2, (Key)secretKey, ivParams);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
            return new String(original, StandardCharsets.UTF_8);
        }
        catch (Exception var7) {
            return null;
        }
    }

    public String Encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKey = Punctuation.getKey(this.key.getValue());
            byte[] iv = new byte[16];
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(1, (Key)secretKey, ivParams);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception var6) {
            return null;
        }
    }

    private /* synthetic */ void lambda$PacketReceive$3(class_7438 packet) {
        this.receive(packet.comp_1102().comp_1090());
    }

    private /* synthetic */ void lambda$PacketReceive$2(class_7438 packet) {
        this.receive(packet.comp_1103().getString());
    }

    private record Spot(String name, class_2338 pos, Color color, Timer timer) {
    }
}

