/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_332
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 *  net.minecraft.class_4587
 */
package dev.luminous.mod.modules.impl.client;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.DeathEvent;
import dev.luminous.api.events.impl.EntitySpawnEvent;
import dev.luminous.api.events.impl.RemoveEntityEvent;
import dev.luminous.api.events.impl.TotemEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.Easing;
import dev.luminous.api.utils.math.FadeUtils;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.api.utils.render.TextUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.ColorsModule;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.LinkedList;
import net.minecraft.class_1657;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_4587;

public class Notification
extends Module {
    public static Notification INSTANCE;
    public final EnumSetting<Mode> mode = this.add(new EnumSetting<Mode>("Mode", Mode.Screen));
    public final EnumSetting<TextStyle> textStyle = this.add(new EnumSetting<TextStyle>("TextStyle", TextStyle.Mio));
    private final ColorSetting textColor = this.add(new ColorSetting("TextColor", new Color(255, 255, 255, 255)));
    private final SliderSetting stayTime = this.add(new SliderSetting("StayTime", 1200, 0, 5000).setSuffix("ms"));
    private final BooleanSetting font = this.add(new BooleanSetting("Font", true));
    private final BooleanSetting shadow = this.add(new BooleanSetting("Shadow", true));
    public final BooleanSetting visualRange = this.add(new BooleanSetting("VisualRange", false).setParent());
    public final BooleanSetting showfriend = this.add(new BooleanSetting("ShowFriends", false, this.visualRange::isOpen));
    public final BooleanSetting popCounter = this.add(new BooleanSetting("PopCounter", true));
    private final SliderSetting baseY = this.add(new SliderSetting("BaseY", 300, 0, 1000, () -> this.mode.is(Mode.Screen)));
    private final SliderSetting rangeY = this.add(new SliderSetting("RangeY", 120, 20, 600, () -> this.mode.is(Mode.Screen)));
    private final SliderSetting rightOffset = this.add(new SliderSetting("RightOffset", 0, -100, 100, () -> this.mode.is(Mode.Screen)));
    private final SliderSetting xTime = this.add(new SliderSetting("XTime", 500, 0, 5000, () -> this.mode.is(Mode.Screen)).setSuffix("ms"));
    private final EnumSetting<Easing> xEase = this.add(new EnumSetting<Easing>("XEase", Easing.Linear, () -> this.mode.is(Mode.Screen)));
    private final SliderSetting yTime = this.add(new SliderSetting("YTime", 1000, 0, 5000, () -> this.mode.is(Mode.Screen)).setSuffix("ms"));
    private final EnumSetting<Easing> yEase = this.add(new EnumSetting<Easing>("YEase", Easing.Linear, () -> this.mode.is(Mode.Screen)));
    private final SliderSetting notificationWidth = this.add(new SliderSetting("Width", 200, 100, 400, () -> this.mode.is(Mode.Screen)));
    private final SliderSetting notificationHeight = this.add(new SliderSetting("Height", 20, 5, 40, () -> this.mode.is(Mode.Screen)));
    private final SliderSetting outlineWidth = this.add(new SliderSetting("OutlineWidth", 0.5, 0.0, 5.0, 0.1f, () -> this.mode.is(Mode.Screen)));
    private final ColorSetting fillColor = this.add(new ColorSetting("FillColor", new Color(30, 30, 30, 180), () -> this.mode.is(Mode.Screen)));
    private final ColorSetting outlineColor = this.add(new ColorSetting("OutlineColor", new Color(255, 255, 255, 255), () -> this.mode.is(Mode.Screen)));
    private final BooleanSetting blur = this.add(new BooleanSetting("Blur", false, () -> this.mode.is(Mode.Screen)).setParent());
    private final SliderSetting blurRadius = this.add(new SliderSetting("BlurRadius", 10.0, 0.0, 100.0, 1.0, () -> this.mode.is(Mode.Screen) && this.blur.isOpen()));
    private final EnumSetting<TextPosition> textPosition = this.add(new EnumSetting<TextPosition>("TextPosition", TextPosition.Center, () -> this.mode.is(Mode.Screen)));
    private final BooleanSetting showProgress = this.add(new BooleanSetting("ShowProgress", true, () -> this.mode.is(Mode.Screen)).setParent());
    private final SliderSetting Height = this.add(new SliderSetting("PHeight", 1, 1, 8, () -> this.mode.is(Mode.Screen) && this.showProgress.isOpen()));
    private final ColorSetting Color = this.add(new ColorSetting("PColor", new Color(255, 255, 255, 255), () -> this.mode.is(Mode.Screen) && this.showProgress.isOpen()));
    private final EnumSetting<ProgressPosition> Position = this.add(new EnumSetting<ProgressPosition>("PPosition", ProgressPosition.Bottom, () -> this.mode.is(Mode.Screen) && this.showProgress.isOpen()));
    private final SliderSetting maxCount = this.add(new SliderSetting("MaxCount", 5, 1, 10, () -> this.mode.is(Mode.TopCenter)));
    private final EnumSetting<Easing> ease = this.add(new EnumSetting<Easing>("Ease", Easing.Linear, () -> this.mode.is(Mode.TopCenter)));
    private final LinkedList<NotificationBar> notifications = new LinkedList();

    public Notification() {
        super("Notification", Module.Category.Client);
        this.setChinese("\u901a\u77e5");
        INSTANCE = this;
    }

    public void push(String text) {
        String formattedText = this.formatText(text);
        this.notifications.addFirst(new NotificationBar(formattedText));
    }

    public void push(String text, Color color) {
        String formattedText = this.formatText(text);
        this.notifications.addFirst(new NotificationBar(formattedText, color));
    }

    private String formatText(String text) {
        String lowerText = text.toLowerCase();
        if (lowerText.contains("error") || lowerText.contains("failed") || lowerText.contains("disabled")) {
            return "\u00a74[-] \u00a7f" + text;
        }
        if (lowerText.contains("enabled") || lowerText.contains("success") || lowerText.contains("complete")) {
            return "\u00a72[+] \u00a7f" + text;
        }
        return text;
    }

    @Override
    public void onRender2D(class_332 ctx, float tickDelta) {
        if (this.mode.is(Mode.Screen)) {
            this.renderScreenMode(ctx);
        } else if (this.mode.is(Mode.TopCenter)) {
            this.renderTopCenterMode(ctx);
        }
    }

    @EventListener
    public void onAddEntity(EntitySpawnEvent event) {
        String playerName;
        boolean isFriend;
        if (this.visualRange.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null && (!(isFriend = Alien.FRIEND.isFriend(playerName = event.getEntity().method_5476().getString())) || this.showfriend.getValue()) && event.getEntity() != Notification.mc.field_1724) {
            String message = (isFriend ? playerName : playerName) + "\u00a7f entered your visual range.";
            if (isFriend) {
                this.push(message, ColorsModule.INSTANCE.FriendColor.getValue());
            } else {
                this.push(message);
            }
            Notification.mc.field_1687.method_8396((class_1657)Notification.mc.field_1724, Notification.mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0f, 1.9f);
        }
    }

    @EventListener
    public void onRemoveEntity(RemoveEntityEvent event) {
        String playerName;
        boolean isFriend;
        if (this.visualRange.getValue() && event.getEntity() instanceof class_1657 && event.getEntity().method_5476() != null && (!(isFriend = Alien.FRIEND.isFriend(playerName = event.getEntity().method_5476().getString())) || this.showfriend.getValue()) && event.getEntity() != Notification.mc.field_1724) {
            String message = (isFriend ? playerName : playerName) + "\u00a7f left your visual range.";
            if (isFriend) {
                this.push(message, ColorsModule.INSTANCE.FriendColor.getValue());
            } else {
                this.push(message);
            }
            Notification.mc.field_1687.method_8396((class_1657)Notification.mc.field_1724, Notification.mc.field_1724.method_24515(), class_3417.field_14627, class_3419.field_15248, 100.0f, 1.9f);
        }
    }

    @EventListener
    public void onPlayerDeath(DeathEvent event) {
        class_1657 player = event.getPlayer();
        if (this.popCounter.getValue()) {
            if (Alien.POP.popContainer.containsKey(player.method_5477().getString())) {
                int l_Count = Alien.POP.popContainer.get(player.method_5477().getString());
                if (l_Count == 1) {
                    if (player.equals((Object)Notification.mc.field_1724)) {
                        this.push("\u00a7fYou\u00a7r died after popping \u00a7f" + l_Count + "\u00a7r totem.");
                    } else {
                        this.push("\u00a7f" + player.method_5477().getString() + "\u00a7r died after popping \u00a7f" + l_Count + "\u00a7r totem.");
                    }
                } else if (player.equals((Object)Notification.mc.field_1724)) {
                    this.push("\u00a7fYou\u00a7r died after popping \u00a7f" + l_Count + "\u00a7r totems.");
                } else {
                    this.push("\u00a7f" + player.method_5477().getString() + "\u00a7r died after popping \u00a7f" + l_Count + "\u00a7r totems.");
                }
            } else if (player.equals((Object)Notification.mc.field_1724)) {
                this.push("\u00a7fYou\u00a7r died.");
            } else {
                this.push("\u00a7f" + player.method_5477().getString() + "\u00a7r died.");
            }
        }
    }

    @EventListener
    public void onTotem(TotemEvent event) {
        if (this.popCounter.getValue()) {
            class_1657 player = event.getPlayer();
            int l_Count = 1;
            if (Alien.POP.popContainer.containsKey(player.method_5477().getString())) {
                l_Count = Alien.POP.popContainer.get(player.method_5477().getString());
            }
            if (l_Count == 1) {
                if (player.equals((Object)Notification.mc.field_1724)) {
                    this.push("\u00a7fYou\u00a7r popped \u00a7f" + l_Count + "\u00a7r totem.");
                } else {
                    this.push("\u00a7f" + player.method_5477().getString() + " \u00a7rpopped \u00a7f" + l_Count + "\u00a7r totems.");
                }
            } else if (player.equals((Object)Notification.mc.field_1724)) {
                this.push("\u00a7fYou\u00a7r popped \u00a7f" + l_Count + "\u00a7r totem.");
            } else {
                this.push("\u00a7f" + player.method_5477().getString() + " \u00a7rhas popped \u00a7f" + l_Count + "\u00a7r totems.");
            }
        }
    }

    private void renderScreenMode(class_332 ctx) {
        int spacing = 4;
        int clipTop = this.baseY.getValueInt() - this.rangeY.getValueInt();
        int clipBottom = this.baseY.getValueInt() + 200;
        ctx.method_44379(0, clipTop, mc.method_22683().method_4486(), clipBottom);
        this.updateTargetPositions();
        for (NotificationBar notification : this.notifications) {
            notification.update();
            notification.drawScreen(ctx);
        }
        ctx.method_44380();
        this.notifications.removeIf(n -> n.stage == Stage.END && n.slideFade.isEnd());
    }

    private void renderTopCenterMode(class_332 ctx) {
        int max = this.maxCount.getValueInt();
        while (this.notifications.size() > max) {
            this.notifications.removeLast();
        }
        int spacing = 2;
        int startY = 50;
        int screenWidth = mc.method_22683().method_4486();
        for (int i = 0; i < this.notifications.size(); ++i) {
            NotificationBar notification = this.notifications.get(i);
            if (notification.textWidth == null && mc != null) {
                notification.textWidth = (int)(this.font.getValue() ? FontManager.ui.getWidth(notification.text) : TextUtil.getWidth(notification.text)) + 20;
            }
            int actualWidth = notification.textWidth != null ? notification.textWidth : 200;
            notification.targetY = startY + i * ((int)(this.font.getValue() ? FontManager.ui.getFontHeight() : TextUtil.getHeight()) + spacing);
            notification.targetX = (screenWidth - actualWidth) / 2;
        }
        for (NotificationBar notification : this.notifications) {
            notification.update();
            notification.drawTopCenter(ctx);
        }
        this.notifications.removeIf(n -> n.stage == Stage.END && n.fadeProgress <= 0.0f);
    }

    private void updateTargetPositions() {
        int spacing = 4;
        int screenWidth = mc.method_22683().method_4486();
        for (int i = this.notifications.size() - 1; i >= 0; --i) {
            NotificationBar notification = this.notifications.get(i);
            if (i == this.notifications.size() - 1) {
                notification.targetY = this.baseY.getValueInt();
                notification.targetX = screenWidth - notification.width + this.rightOffset.getValueInt();
                continue;
            }
            NotificationBar below = this.notifications.get(i + 1);
            notification.targetY = below.currentY - notification.height - spacing;
            notification.targetX = screenWidth - notification.width + this.rightOffset.getValueInt();
        }
    }

    public static enum Mode {
        Screen,
        TopCenter;

    }

    public static enum TextStyle {
        Mio,
        HookCross;

    }

    public static enum TextPosition {
        Left,
        Center,
        Right;

    }

    public static enum ProgressPosition {
        Top,
        Bottom;

    }

    private class NotificationBar {
        final FadeUtils slideFade;
        final FadeUtils stayFade;
        final FadeUtils fadeOutFade;
        final String text;
        final Color customColor;
        int currentX;
        int targetX;
        int currentY;
        int targetY;
        final int width;
        final int height;
        Integer textWidth = null;
        Stage stage = Stage.START;
        float fadeProgress = 0.0f;
        private float progressWidth = 0.0f;
        private float lastTargetWidth = 0.0f;

        NotificationBar(String text) {
            this(text, null);
        }

        NotificationBar(String text, Color color) {
            this.text = text;
            this.customColor = color;
            if (Notification.this.mode.is(Mode.Screen)) {
                this.width = Notification.this.notificationWidth.getValueInt();
                this.height = Notification.this.notificationHeight.getValueInt();
            } else {
                this.width = 200;
                this.height = 30;
            }
            if (Notification.this.mode.is(Mode.Screen)) {
                this.slideFade = new FadeUtils(Notification.this.xTime.getValueInt());
                this.fadeOutFade = new FadeUtils(500L);
            } else {
                this.slideFade = new FadeUtils(800L);
                this.fadeOutFade = new FadeUtils(1000L);
            }
            this.stayFade = new FadeUtils(Notification.this.stayTime.getValueInt());
            this.targetX = 0;
            this.currentX = 0;
            this.currentY = this.targetY = -this.height;
            this.fadeProgress = 0.0f;
            this.textWidth = null;
            this.slideFade.reset();
        }

        void update() {
            Easing easing;
            Easing easing2 = easing = Notification.this.mode.is(Mode.Screen) ? Notification.this.xEase.getValue() : Notification.this.ease.getValue();
            if (Notification.this.mode.is(Mode.Screen)) {
                this.updateScreenMode(easing);
            } else {
                this.updateTopCenterMode(easing);
            }
        }

        private void updateScreenMode(Easing easing) {
            this.currentY += (int)((double)(this.targetY - this.currentY) * this.slideFade.ease(easing));
            switch (this.stage.ordinal()) {
                case 0: {
                    this.currentX += (int)((double)(this.targetX - this.currentX) * this.slideFade.ease(easing));
                    if (!this.slideFade.isEnd()) break;
                    this.stage = Stage.STAY;
                    this.stayFade.reset();
                    break;
                }
                case 1: {
                    if (!this.stayFade.isEnd()) break;
                    this.stage = Stage.END;
                    this.slideFade.reset();
                    break;
                }
                case 2: {
                    int offscreen = Wrapper.mc.method_22683().method_4486() + this.width;
                    this.currentX += (int)((double)(offscreen - this.currentX) * this.slideFade.ease(easing));
                }
            }
        }

        private void updateTopCenterMode(Easing easing) {
            switch (this.stage.ordinal()) {
                case 0: {
                    this.currentY += (int)((double)(this.targetY - this.currentY) * this.slideFade.ease(easing));
                    this.fadeProgress = Math.min(1.0f, this.fadeProgress + 0.05f);
                    if (!this.slideFade.isEnd()) break;
                    this.stage = Stage.STAY;
                    this.stayFade.reset();
                    this.fadeProgress = 1.0f;
                    break;
                }
                case 1: {
                    this.currentY += (int)((float)(this.targetY - this.currentY) * 0.2f);
                    if (!this.stayFade.isEnd()) break;
                    this.stage = Stage.END;
                    this.fadeOutFade.reset();
                    break;
                }
                case 2: {
                    this.fadeProgress = Math.max(0.0f, 1.0f - (float)this.fadeOutFade.ease(easing));
                }
            }
        }

        void drawScreen(class_332 ctx) {
            class_4587 matrices = ctx.method_51448();
            float borderWidth = Notification.this.outlineWidth.getValueFloat();
            if (borderWidth > 0.0f) {
                this.drawOutline(matrices, borderWidth);
            }
            ctx.method_44379(this.currentX, this.currentY, this.currentX + this.width, this.currentY + this.height);
            if (Notification.this.blur.getValue()) {
                Alien.BLUR.applyBlur(Notification.this.blurRadius.getValueFloat(), this.currentX, this.currentY, this.width, this.height);
            }
            Render2DUtil.drawRect(matrices, (float)this.currentX, (float)this.currentY, (float)this.width, (float)this.height, Notification.this.fillColor.getValue());
            int textX = this.getTextPosition();
            int textY = (int)((float)this.currentY + ((float)this.height - TextUtil.getHeight()) / 2.0f);
            int drawColor = this.customColor != null ? this.customColor.getRGB() : Notification.this.textColor.getValue().getRGB();
            TextUtil.drawString(ctx, this.text, textX, textY, drawColor, Notification.this.font.getValue(), Notification.this.shadow.getValue());
            if (this.stage == Stage.STAY && Notification.this.showProgress.getValue()) {
                this.drawProgressBar(ctx);
            }
            ctx.method_44380();
        }

        void drawTopCenter(class_332 ctx) {
            Color baseColor = this.customColor != null ? this.customColor : Notification.this.textColor.getValue();
            Color color = new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), (int)(255.0f * this.fadeProgress));
            TextUtil.drawString(ctx, this.text, this.targetX, this.targetY, color.getRGB(), Notification.this.font.getValue(), Notification.this.shadow.getValue());
        }

        private void drawOutline(class_4587 matrices, float borderWidth) {
            Color color = Notification.this.outlineColor.getValue();
            float x = this.currentX;
            float y = this.currentY;
            float w = this.width;
            float h = this.height;
            Render2DUtil.drawRect(matrices, x - borderWidth, y - borderWidth, w + borderWidth * 2.0f, borderWidth, color);
            Render2DUtil.drawRect(matrices, x - borderWidth, y + h, w + borderWidth * 2.0f, borderWidth, color);
            Render2DUtil.drawRect(matrices, x - borderWidth, y, borderWidth, h, color);
            Render2DUtil.drawRect(matrices, x + w, y, borderWidth, h, color);
        }

        private int getTextPosition() {
            int textWidth = (int)(Notification.this.font.getValue() ? FontManager.ui.getWidth(this.text) : TextUtil.getWidth(this.text));
            switch (Notification.this.textPosition.getValue().ordinal()) {
                case 0: {
                    return this.currentX + 5;
                }
                case 2: {
                    return this.currentX + this.width - textWidth - 5;
                }
            }
            return this.currentX + (this.width - textWidth) / 2;
        }

        private void drawProgressBar(class_332 ctx) {
            float remain = (float)(1.0 - this.stayFade.getFadeOne());
            remain = Math.max(0.0f, Math.min(1.0f, remain));
            int barHeight = Notification.this.Height.getValueInt();
            Color barColor = Notification.this.Color.getValue();
            int barY = Notification.this.Position.getValue() == ProgressPosition.Top ? this.currentY + 2 : this.currentY + this.height - barHeight - 2;
            int barX = this.currentX + 4;
            int maxBarW = this.width - 8;
            float targetWidth = (float)maxBarW * remain;
            float currentWidth = this.smoothProgressWidth(targetWidth, maxBarW);
            int barW = (int)currentWidth;
            float alpha = Math.min(1.0f, (float)(this.stayFade.getFadeOne() * 2.0));
            Color bgColor = new Color(barColor.getRed(), barColor.getGreen(), barColor.getBlue(), (int)((float)barColor.getAlpha() * 0.15f * alpha));
            Render2DUtil.drawRect(ctx, (float)barX, (float)barY, (float)maxBarW, (float)barHeight, bgColor);
            if (barW > 0) {
                Color mainColor = new Color(barColor.getRed(), barColor.getGreen(), barColor.getBlue(), (int)((float)barColor.getAlpha() * alpha));
                Render2DUtil.drawRect(ctx, (float)barX, (float)barY, (float)barW, (float)barHeight, mainColor);
                if (remain > 0.1f) {
                    int glossWidth = Math.min(20, (int)((float)maxBarW * 0.15f));
                    int glossX = barX + Math.min(barW - glossWidth, (int)((float)barW * 0.7f));
                    Color glossColor = new Color(255, 255, 255, (int)(30.0f * alpha * (1.0f - remain)));
                    Render2DUtil.drawRect(ctx, (float)glossX, (float)barY, (float)glossWidth, (float)(barHeight / 2), glossColor);
                }
            }
        }

        private float smoothProgressWidth(float targetWidth, float maxWidth) {
            if (Math.abs(targetWidth - this.lastTargetWidth) > maxWidth * 0.3f) {
                this.progressWidth = targetWidth;
                this.lastTargetWidth = targetWidth;
                return this.progressWidth;
            }
            float smoothing = 0.15f;
            this.progressWidth += (targetWidth - this.progressWidth) * smoothing;
            this.lastTargetWidth = targetWidth;
            return Math.max(0.0f, Math.min(maxWidth, this.progressWidth));
        }
    }

    public static enum Stage {
        START,
        STAY,
        END;

    }
}

