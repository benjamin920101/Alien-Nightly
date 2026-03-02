/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_2960
 *  net.minecraft.class_308
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 *  net.minecraft.class_640
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.Render2DEvent;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_308;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_640;

public class PlayerInfoHUD
extends Module {
    public final SliderSetting cardWidth = this.add(new SliderSetting("CardWidth", 200.0, 120.0, 400.0, 1.0));
    public final SliderSetting cardHeight = this.add(new SliderSetting("CardHeight", 80.0, 60.0, 150.0, 1.0));
    public final SliderSetting firstCardX = this.add(new SliderSetting("FirstCardX", 10.0, 0.0, 1920.0, 1.0));
    public final SliderSetting firstCardY = this.add(new SliderSetting("FirstCardY", 10.0, 0.0, 1080.0, 1.0));
    public final SliderSetting cardSpacing = this.add(new SliderSetting("CardSpacing", 5.0, 0.0, 30.0, 0.5));
    public final EnumSetting<CardLayout> layout = this.add(new EnumSetting<CardLayout>("Layout", CardLayout.VERTICAL));
    public final BooleanSetting enableBackgroundImage = this.add(new BooleanSetting("EnableBGImage", true));
    private static final class_2960 BACKGROUND_TEXTURE = class_2960.method_60655((String)"alienclient", (String)"textures/xingye.png");
    public final SliderSetting imageAlpha = this.add(new SliderSetting("ImageAlpha", 150.0, 0.0, 255.0, 1.0));
    public final EnumSetting<ImageMode> imageMode = this.add(new EnumSetting<ImageMode>("ImageMode", ImageMode.RELAY));
    private static final float FIXED_IMAGE_WIDTH = 200.0f;
    private static final float FIXED_IMAGE_HEIGHT = 240.0f;
    private static final float FIXED_IMAGE_POS_X = 0.0f;
    private static final float FIXED_IMAGE_POS_Y = -19.0f;
    public final BooleanSetting enableCardBackground = this.add(new BooleanSetting("EnableCardBG", true));
    public final ColorSetting cardBgColor = this.add(new ColorSetting("CardBG", new Color(Integer.MIN_VALUE, true)));
    public final ColorSetting cardBorderColor = this.add(new ColorSetting("CardBorder", new Color(-16711936, true)));
    public final BooleanSetting cardRoundedCorners = this.add(new BooleanSetting("CardRounded", true));
    public final SliderSetting cardCornerRadius = this.add(new SliderSetting("CardCornerRadius", 8.0, 0.0, 20.0, 0.5));
    public final SliderSetting cardBorderThickness = this.add(new SliderSetting("CardBorderThickness", 2.0, 0.0, 5.0, 0.5));
    public final BooleanSetting enableBlur = this.add(new BooleanSetting("EnableBlur", true));
    public final SliderSetting blurStrength = this.add(new SliderSetting("BlurStrength", 5.0, 1.0, 20.0, 0.5));
    public final SliderSetting blurRadius = this.add(new SliderSetting("BlurRadius", 8.0, 0.0, 20.0, 0.5));
    public final ColorSetting blurOverlay = this.add(new ColorSetting("BlurOverlay", new Color(0x60000000, true)));
    public final SliderSetting maxPlayers = this.add(new SliderSetting("MaxPlayers", 3.0, 1.0, 10.0, 1.0));
    public final SliderSetting maxDistance = this.add(new SliderSetting("MaxDistance", 50.0, 10.0, 200.0, 1.0));
    public final BooleanSetting showSkin = this.add(new BooleanSetting("ShowSkin", true));
    public final BooleanSetting showName = this.add(new BooleanSetting("ShowName", true));
    public final BooleanSetting showHealth = this.add(new BooleanSetting("ShowHealth", true));
    public final BooleanSetting showHealthBar = this.add(new BooleanSetting("HealthBar", true));
    public final BooleanSetting showHealthAnimation = this.add(new BooleanSetting("HealthAnimation", true));
    public final BooleanSetting showItems = this.add(new BooleanSetting("ShowItems", true));
    public final BooleanSetting showArmorItems = this.add(new BooleanSetting("ShowArmor", true));
    public final BooleanSetting showMainHand = this.add(new BooleanSetting("ShowMainHand", true));
    public final BooleanSetting showOffHand = this.add(new BooleanSetting("ShowOffHand", true));
    public final BooleanSetting showDistance = this.add(new BooleanSetting("ShowDistance", true));
    public final BooleanSetting showPing = this.add(new BooleanSetting("ShowPing", false));
    public final EnumSetting<FontMode> fontMode = this.add(new EnumSetting<FontMode>("Font", FontMode.VANILLA));
    public final SliderSetting nameScale = this.add(new SliderSetting("NameScale", 1.0, 0.5, 2.0, 0.1));
    public final SliderSetting infoScale = this.add(new SliderSetting("InfoScale", 0.8, 0.5, 2.0, 0.1));
    public final ColorSetting nameColor = this.add(new ColorSetting("NameColor", new Color(-1, true)));
    public final ColorSetting friendColor = this.add(new ColorSetting("FriendColor", new Color(-16711936, true)));
    public final ColorSetting healthColor = this.add(new ColorSetting("HealthColor", new Color(-65536, true)));
    public final ColorSetting healthBarBgColor = this.add(new ColorSetting("HealthBarBG", new Color(-2144128205, true)));
    public final ColorSetting distanceColor = this.add(new ColorSetting("DistanceColor", new Color(-256, true)));
    public final SliderSetting skinSize = this.add(new SliderSetting("SkinSize", 40.0, 20.0, 60.0, 1.0));
    public final SliderSetting skinPadding = this.add(new SliderSetting("SkinPadding", 5.0, 0.0, 20.0, 1.0));
    public final SliderSetting healthBarHeight = this.add(new SliderSetting("HealthBarHeight", 6.0, 2.0, 10.0, 0.5));
    public final SliderSetting healthBarPadding = this.add(new SliderSetting("HealthBarPadding", 3.0, 0.0, 10.0, 0.5));
    public final SliderSetting itemSize = this.add(new SliderSetting("ItemSize", 16.0, 12.0, 24.0, 1.0));
    public final SliderSetting itemSpacing = this.add(new SliderSetting("ItemSpacing", 2.0, 0.0, 10.0, 0.5));
    public final SliderSetting animationSpeed = this.add(new SliderSetting("AnimSpeed", 5.0, 1.0, 20.0, 0.5));
    public final SliderSetting particleCount = this.add(new SliderSetting("ParticleCount", 8.0, 2.0, 20.0, 1.0));
    public final SliderSetting particleSize = this.add(new SliderSetting("ParticleSize", 2.0, 1.0, 5.0, 0.5));
    private final List<PlayerData> visiblePlayers = new ArrayList<PlayerData>();

    public PlayerInfoHUD() {
        super("PlayerInfoHUD", Module.Category.Render);
        this.setChinese("\u73a9\u5bb6\u4fe1\u606fHUD");
    }

    @EventListener
    public void onRender2D(Render2DEvent event) {
        class_332 context = event.drawContext;
        class_4587 matrices = context.method_51448();
        this.updateVisiblePlayers();
        if (this.visiblePlayers.isEmpty()) {
            return;
        }
        this.calculateCardPositions();
        if (this.enableBackgroundImage.getValue()) {
            this.drawBackgroundImage(context, matrices);
        }
        for (PlayerData playerData : this.visiblePlayers) {
            if (playerData.cardPos == null) continue;
            this.enableCardScissor(context, playerData.cardPos);
            this.drawPlayerCard(context, matrices, playerData);
            context.method_44380();
        }
    }

    private void enableCardScissor(class_332 context, CardPosition cardPos) {
        int x = (int)cardPos.x;
        int y = (int)cardPos.y;
        int width = (int)cardPos.width;
        int height = (int)cardPos.height;
        context.method_44379(x, y, x + width, y + height);
    }

    private void drawBackgroundImage(class_332 context, class_4587 matrices) {
        try {
            int alpha = (int)this.imageAlpha.getValueFloat();
            float baseX = this.firstCardX.getValueFloat();
            float baseY = this.firstCardY.getValueFloat();
            float imageX = baseX + 0.0f;
            float imageY = baseY + -19.0f;
            context.method_51422(1.0f, 1.0f, 1.0f, (float)alpha / 255.0f);
            if (this.imageMode.getValue() == ImageMode.RELAY) {
                float displayHeight;
                int playerCount = this.visiblePlayers.size();
                float textureHeight = 240.0f;
                float textureStartY = 0.0f;
                if (playerCount == 1) {
                    displayHeight = textureHeight / 3.0f;
                    textureStartY = 0.0f;
                } else if (playerCount == 2) {
                    displayHeight = textureHeight / 3.0f;
                    textureStartY = textureHeight / 3.0f;
                } else {
                    displayHeight = textureHeight;
                    textureStartY = 0.0f;
                }
                context.method_25290(BACKGROUND_TEXTURE, (int)imageX, (int)imageY, 0.0f, (float)((int)textureStartY), 200, (int)displayHeight, 200, (int)textureHeight);
            } else {
                context.method_25290(BACKGROUND_TEXTURE, (int)imageX, (int)imageY, 0.0f, 0.0f, 200, 240, 200, 240);
            }
            context.method_51422(1.0f, 1.0f, 1.0f, 1.0f);
        }
        catch (Exception e) {
            this.drawFallbackBackground(matrices);
        }
    }

    private void drawFallbackBackground(class_4587 matrices) {
        float baseX = this.firstCardX.getValueFloat();
        float baseY = this.firstCardY.getValueFloat();
        float imageX = baseX + 0.0f;
        float imageY = baseY + -19.0f;
        Color color1 = new Color(0x40000000, true);
        if (this.imageMode.getValue() == ImageMode.RELAY) {
            float displayHeight;
            int playerCount = this.visiblePlayers.size();
            float currentImageY = imageY;
            if (playerCount == 1) {
                displayHeight = 80.0f;
            } else if (playerCount == 2) {
                displayHeight = 80.0f;
                currentImageY = imageY + 80.0f;
            } else {
                displayHeight = 240.0f;
            }
            Render2DUtil.drawRect(matrices, imageX, currentImageY, 200.0f, displayHeight, color1);
        } else {
            Render2DUtil.drawRect(matrices, imageX, imageY, 200.0f, 240.0f, color1);
        }
    }

    private void calculateCardPositions() {
        float startX = this.firstCardX.getValueFloat();
        float startY = this.firstCardY.getValueFloat();
        float cardWidthValue = this.cardWidth.getValueFloat();
        float cardHeightValue = this.cardHeight.getValueFloat();
        float spacing = this.cardSpacing.getValueFloat();
        CardLayout layoutMode = this.layout.getValue();
        for (int i = 0; i < this.visiblePlayers.size(); ++i) {
            float cardY;
            float cardX;
            PlayerData playerData = this.visiblePlayers.get(i);
            if (layoutMode == CardLayout.VERTICAL) {
                cardX = startX;
                cardY = startY + (float)i * (cardHeightValue + spacing);
            } else {
                cardX = startX + (float)i * (cardWidthValue + spacing);
                cardY = startY;
            }
            playerData.cardPos = new CardPosition(cardX, cardY, cardWidthValue, cardHeightValue);
        }
    }

    private void drawPlayerCard(class_332 context, class_4587 matrices, PlayerData playerData) {
        if (playerData.cardPos == null) {
            return;
        }
        float x = playerData.cardPos.x;
        float y = playerData.cardPos.y;
        float w = playerData.cardPos.width;
        float h = playerData.cardPos.height;
        if (this.enableCardBackground.getValue()) {
            if (this.enableBlur.getValue()) {
                this.drawCardBlurBackground(matrices, x, y, w, h);
            }
            if (!this.enableBackgroundImage.getValue()) {
                this.drawCardRoundedBackground(matrices, x, y, w, h);
            }
        }
        this.drawPlayerInCard(context, matrices, x, y, w, h, playerData);
        this.drawCardParticlesOnTop(matrices, x, y, w, h, playerData);
    }

    private void drawCardBlurBackground(class_4587 matrices, float x, float y, float w, float h) {
        float radius = this.cardCornerRadius.getValueFloat();
        float blurStrengthValue = this.blurStrength.getValueFloat();
        int i = 0;
        while ((float)i < blurStrengthValue) {
            float offset = (float)i * 0.5f;
            float alpha = (float)((double)this.blurOverlay.getValue().getAlpha() / 255.0 * (1.0 - (double)((float)i / blurStrengthValue)));
            Color blurColor = new Color(this.blurOverlay.getValue().getRed(), this.blurOverlay.getValue().getGreen(), this.blurOverlay.getValue().getBlue(), (int)(alpha * 255.0f));
            Render2DUtil.drawRoundedRect(matrices, x - offset, y - offset, w + offset * 2.0f, h + offset * 2.0f, radius + offset, blurColor);
            ++i;
        }
    }

    private void drawCardRoundedBackground(class_4587 matrices, float x, float y, float w, float h) {
        float radius = this.cardCornerRadius.getValueFloat();
        Render2DUtil.drawRoundedRect(matrices, x, y, w, h, radius, this.cardBgColor.getValue());
        if (this.cardBorderThickness.getValueFloat() > 0.0f) {
            Render2DUtil.drawRoundedStroke(matrices, x, y, w, h, radius, this.cardBorderColor.getValue(), 32);
        }
    }

    private void drawPlayerInCard(class_332 context, class_4587 matrices, float x, float y, float w, float h, PlayerData playerData) {
        class_1657 player = playerData.player;
        float padding = this.skinPadding.getValueFloat();
        float skinX = x + padding;
        float skinY = y + (h - this.skinSize.getValueFloat()) / 2.0f;
        if (this.showSkin.getValue()) {
            this.drawPlayerSkin(context, matrices, skinX, skinY, this.skinSize.getValueFloat(), player);
        }
        if (this.cardBorderThickness.getValueFloat() > 0.0f) {
            float radius = Math.min(this.cardCornerRadius.getValueFloat(), this.skinSize.getValueFloat() / 2.0f);
            Render2DUtil.drawRoundedStroke(matrices, skinX, skinY, this.skinSize.getValueFloat(), this.skinSize.getValueFloat(), radius, this.cardBorderColor.getValue(), 32);
        }
        float contentX = skinX + this.skinSize.getValueFloat() + padding;
        float contentWidth = w - (this.skinSize.getValueFloat() + padding * 2.0f);
        float contentStartY = y + padding;
        this.drawPlayerInfo(context, matrices, contentX, contentStartY, contentWidth, player, playerData);
    }

    private void drawPlayerInfo(class_332 context, class_4587 matrices, float x, float y, float maxWidth, class_1657 player, PlayerData playerData) {
        float currentY = y;
        if (this.showName.getValue()) {
            float f;
            String name = player.method_5477().getString();
            Color nameColorValue = Alien.FRIEND.isFriend(player) ? this.friendColor.getValue() : this.nameColor.getValue();
            matrices.method_22903();
            matrices.method_46416(x, currentY, 0.0f);
            matrices.method_22905(this.nameScale.getValueFloat(), this.nameScale.getValueFloat(), 1.0f);
            if (this.fontMode.getValue() == FontMode.VANILLA) {
                context.method_51433(PlayerInfoHUD.mc.field_1772, name, 0, 0, nameColorValue.getRGB(), true);
            } else {
                FontManager.ui.drawString(matrices, name, 0.0, 0.0, nameColorValue.getRGB());
            }
            matrices.method_22909();
            if (this.fontMode.getValue() == FontMode.VANILLA) {
                Objects.requireNonNull(PlayerInfoHUD.mc.field_1772);
                f = 9.0f;
            } else {
                f = FontManager.ui.getFontHeight();
            }
            float textHeight = f * this.nameScale.getValueFloat();
            currentY += textHeight + 2.0f;
        }
        if (this.showHealth.getValue() && this.showHealthBar.getValue()) {
            this.drawHealthBar(matrices, x, currentY, maxWidth, player);
            float maxHealth = player.method_6063();
            float currentHealth = Math.min(player.method_6032() + player.method_6067(), maxHealth);
            float healthPercent = Math.min(currentHealth / maxHealth, 1.0f);
            float filledWidth = maxWidth * healthPercent;
            playerData.currentHealthBarX = x + filledWidth;
            playerData.currentHealthBarY = currentY + this.healthBarHeight.getValueFloat() / 2.0f;
            if (currentHealth < playerData.lastHealth) {
                this.createParticlesAtCurrentHealthBar(playerData);
            }
            currentY += this.healthBarHeight.getValueFloat() + this.healthBarPadding.getValueFloat();
            playerData.lastHealth = currentHealth;
        }
        if (this.showItems.getValue()) {
            this.drawPlayerItems(context, matrices, x, currentY, maxWidth, player);
            currentY += this.itemSize.getValueFloat() + 2.0f;
        }
        this.drawBottomInfo(context, matrices, x, currentY, maxWidth, player);
    }

    private void drawBottomInfo(class_332 context, class_4587 matrices, float x, float y, float maxWidth, class_1657 player) {
        int ping;
        matrices.method_22903();
        matrices.method_46416(x, y, 0.0f);
        matrices.method_22905(this.infoScale.getValueFloat(), this.infoScale.getValueFloat(), 1.0f);
        float infoY = 0.0f;
        float currentX = 0.0f;
        if (this.showDistance.getValue()) {
            float textWidth;
            float distance = PlayerInfoHUD.mc.field_1724.method_5739((class_1297)player);
            String distanceText = String.format("%.1fm", Float.valueOf(distance));
            if (this.fontMode.getValue() == FontMode.VANILLA) {
                textWidth = PlayerInfoHUD.mc.field_1772.method_1727(distanceText);
                context.method_51433(PlayerInfoHUD.mc.field_1772, distanceText, (int)currentX, (int)infoY, this.distanceColor.getValue().getRGB(), true);
            } else {
                textWidth = FontManager.ui.getWidth(distanceText);
                FontManager.ui.drawString(matrices, distanceText, (double)currentX, (double)infoY, this.distanceColor.getValue().getRGB());
            }
            currentX += textWidth + 8.0f;
        }
        if (this.showPing.getValue() && (ping = this.getPlayerPing(player)) >= 0) {
            String pingText = ping + "ms";
            Color pingColor = this.getPingColor(ping);
            if (this.fontMode.getValue() == FontMode.VANILLA) {
                float textWidth = PlayerInfoHUD.mc.field_1772.method_1727(pingText);
                float pingX = maxWidth / this.infoScale.getValueFloat() - textWidth;
                context.method_51433(PlayerInfoHUD.mc.field_1772, pingText, (int)pingX, (int)infoY, pingColor.getRGB(), true);
            } else {
                float textWidth = FontManager.ui.getWidth(pingText);
                float pingX = maxWidth / this.infoScale.getValueFloat() - textWidth;
                FontManager.ui.drawString(matrices, pingText, (double)pingX, (double)infoY, pingColor.getRGB());
            }
        }
        matrices.method_22909();
    }

    private void drawCardParticlesOnTop(class_4587 matrices, float cardX, float cardY, float cardW, float cardH, PlayerData playerData) {
        if (!this.showHealthAnimation.getValue()) {
            return;
        }
        List<HealthParticle> particles = playerData.particles;
        for (int i = particles.size() - 1; i >= 0; --i) {
            HealthParticle particle = particles.get(i);
            particle.update(playerData.currentHealthBarX, playerData.currentHealthBarY);
            if (!particle.isAlive()) {
                particles.remove(i);
                continue;
            }
            if (!(particle.x >= cardX && particle.x <= cardX + cardW && particle.y >= cardY && particle.y <= cardY + cardH)) continue;
            float size = particle.getSize();
            Color particleColor = new Color(particle.color.getRed(), particle.color.getGreen(), particle.color.getBlue(), (int)((float)particle.color.getAlpha() * particle.life));
            Render2DUtil.drawRect(matrices, particle.x, particle.y, size, size, particleColor);
        }
    }

    private void createParticlesAtCurrentHealthBar(PlayerData playerData) {
        if (!this.showHealthAnimation.getValue()) {
            return;
        }
        class_1657 player = playerData.player;
        int particleCountValue = (int)this.particleCount.getValueFloat();
        for (int i = 0; i < particleCountValue; ++i) {
            float particleX = playerData.currentHealthBarX + (float)(Math.random() * 10.0) - 5.0f;
            float particleY = playerData.currentHealthBarY + (float)(Math.random() * 10.0) - 5.0f;
            float currentHealth = player.method_6032() + player.method_6067();
            float maxHealth = player.method_6063();
            float healthPercent = Math.min(currentHealth / maxHealth, 1.0f);
            Color particleColor = this.getHealthBarColor(healthPercent);
            playerData.particles.add(new HealthParticle(this, this, this, particleX, particleY, particleColor));
        }
    }

    private void updateVisiblePlayers() {
        if (PlayerInfoHUD.mc.field_1687 == null || PlayerInfoHUD.mc.field_1724 == null) {
            this.visiblePlayers.clear();
            return;
        }
        List allPlayers = PlayerInfoHUD.mc.field_1687.method_18456().stream().filter(player -> player != PlayerInfoHUD.mc.field_1724 && !player.method_5767() && player.method_5805()).filter(player -> !Alien.FRIEND.isFriend((class_1657)player)).filter(player -> PlayerInfoHUD.mc.field_1724.method_5739((class_1297)player) <= this.maxDistance.getValueFloat()).sorted(Comparator.comparingDouble(p -> PlayerInfoHUD.mc.field_1724.method_5739((class_1297)p))).limit((int)this.maxPlayers.getValueFloat()).collect(Collectors.toList());
        this.visiblePlayers.removeIf(playerData -> !allPlayers.contains(playerData.player));
        for (Object player2 : allPlayers) {
            boolean exists = this.visiblePlayers.stream().anyMatch(pd -> pd.player == player2);
            if (exists) continue;
            PlayerData newData = new PlayerData();
            newData.player = (class_1657)player2;
            newData.distance = PlayerInfoHUD.mc.field_1724.method_5739((class_1297)player2);
            newData.lastHealth = ((class_1657)player2).method_6063() + ((class_1657)player2).method_6067();
            this.visiblePlayers.add(newData);
        }
        this.visiblePlayers.sort(Comparator.comparingDouble(pd -> pd.distance));
    }

    private void drawPlayerItems(class_332 context, class_4587 matrices, float x, float y, float maxWidth, class_1657 player) {
        class_1799 mainHand;
        class_1799 offHand;
        float itemSizeValue = this.itemSize.getValueFloat();
        float itemSpacingValue = this.itemSpacing.getValueFloat();
        ArrayList<class_1799> itemsToShow = new ArrayList<class_1799>();
        if (this.showOffHand.getValue() && !(offHand = player.method_6079()).method_7960()) {
            itemsToShow.add(offHand);
        }
        if (this.showArmorItems.getValue()) {
            for (int i = 3; i >= 0; --i) {
                class_1799 armorItem = (class_1799)player.method_31548().field_7548.get(i);
                if (armorItem.method_7960()) continue;
                itemsToShow.add(armorItem);
            }
        }
        if (this.showMainHand.getValue() && !(mainHand = player.method_6047()).method_7960()) {
            itemsToShow.add(mainHand);
        }
        if (itemsToShow.isEmpty()) {
            return;
        }
        float totalWidth = (float)itemsToShow.size() * itemSizeValue + (float)(itemsToShow.size() - 1) * itemSpacingValue;
        float startX = x + (maxWidth - totalWidth) / 2.0f;
        float itemY = y;
        for (int i = 0; i < itemsToShow.size(); ++i) {
            float itemX = startX + (float)i * (itemSizeValue + itemSpacingValue);
            this.drawItem(context, matrices, (class_1799)itemsToShow.get(i), itemX, itemY, itemSizeValue);
        }
    }

    private void drawItem(class_332 context, class_4587 matrices, class_1799 itemStack, float x, float y, float size) {
        if (itemStack.method_7960()) {
            return;
        }
        matrices.method_22903();
        matrices.method_46416(x, y, 0.0f);
        matrices.method_22905(size / 16.0f, size / 16.0f, 1.0f);
        class_308.method_24210();
        context.method_51427(itemStack, 0, 0);
        context.method_51431(PlayerInfoHUD.mc.field_1772, itemStack, 0, 0);
        matrices.method_22909();
        if (this.cardBorderThickness.getValueFloat() > 0.0f) {
            float thickness = Math.max(1.0f, this.cardBorderThickness.getValueFloat());
            Color borderColorValue = this.cardBorderColor.getValue();
            Render2DUtil.drawRect(matrices, x - thickness, y - thickness, size + thickness * 2.0f, thickness, borderColorValue);
            Render2DUtil.drawRect(matrices, x - thickness, y + size, size + thickness * 2.0f, thickness, borderColorValue);
            Render2DUtil.drawRect(matrices, x - thickness, y, thickness, size, borderColorValue);
            Render2DUtil.drawRect(matrices, x + size, y, thickness, size, borderColorValue);
        }
    }

    private boolean drawPlayerSkin(class_332 context, class_4587 matrices, float x, float y, float size, class_1657 player) {
        try {
            float radius = Math.min(this.cardCornerRadius.getValueFloat(), size / 2.0f);
            Render2DUtil.drawRoundedRect(matrices, x, y, size, size, radius, new Color(-13421773));
            float innerSize = size - 4.0f;
            float innerX = x + 2.0f;
            float innerY = y + 2.0f;
            float innerRadius = Math.max(0.0f, radius - 2.0f);
            Render2DUtil.drawRoundedRect(matrices, innerX, innerY, innerSize, innerSize, innerRadius, new Color(-11184811));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    private void drawHealthBar(class_4587 matrices, float x, float y, float maxWidth, class_1657 player) {
        float maxHealth = player.method_6063();
        float currentHealth = Math.min(player.method_6032() + player.method_6067(), maxHealth);
        float healthPercent = Math.min(currentHealth / maxHealth, 1.0f);
        float barHeight = this.healthBarHeight.getValueFloat();
        float barWidth = maxWidth;
        float barRadius = Math.min(barHeight / 2.0f, this.cardCornerRadius.getValueFloat());
        Render2DUtil.drawRoundedRect(matrices, x, y, barWidth, barHeight, barRadius, this.healthBarBgColor.getValue());
        float filledWidth = barWidth * healthPercent;
        if (filledWidth > 0.0f) {
            Color healthColorValue = this.getHealthBarColor(healthPercent);
            if (filledWidth >= barWidth - 0.5f) {
                Render2DUtil.drawRoundedRect(matrices, x, y, barWidth, barHeight, barRadius, healthColorValue);
            } else if (filledWidth > barRadius * 2.0f) {
                Render2DUtil.drawRect(matrices, x + barRadius, y, filledWidth - barRadius * 2.0f, barHeight, healthColorValue);
                Render2DUtil.drawRoundedRect(matrices, x, y, barRadius * 2.0f, barHeight, barRadius, healthColorValue);
            } else {
                Render2DUtil.drawRoundedRect(matrices, x, y, filledWidth, barHeight, barRadius, healthColorValue);
            }
        }
    }

    private Color getHealthBarColor(float percent) {
        if (percent > 0.7f) {
            return new Color(-16711936);
        }
        if (percent > 0.4f) {
            return new Color(-256);
        }
        if (percent > 0.2f) {
            return new Color(-39424);
        }
        return new Color(-65536);
    }

    private Color getPingColor(int ping) {
        if (ping < 100) {
            return new Color(-16711936);
        }
        if (ping < 200) {
            return new Color(-256);
        }
        if (ping < 300) {
            return new Color(-39424);
        }
        return new Color(-65536);
    }

    private int getPlayerPing(class_1657 player) {
        if (mc.method_1562() == null) {
            return -1;
        }
        class_640 entry = mc.method_1562().method_2871(player.method_5667());
        return entry != null ? entry.method_2959() : -1;
    }

    @Override
    public boolean onEnable() {
        super.onEnable();
        this.visiblePlayers.clear();
        return false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.visiblePlayers.clear();
    }

    public static enum CardLayout {
        VERTICAL("Vertical"),
        HORIZONTAL("Horizontal");

        private final String name;

        private CardLayout(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    public static enum ImageMode {
        NORMAL("Normal"),
        RELAY("Relay");

        private final String name;

        private ImageMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    public static enum FontMode {
        VANILLA("Vanilla"),
        CUSTOM("Custom");

        private final String name;

        private FontMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    private static class PlayerData {
        class_1657 player;
        float distance;
        float lastHealth;
        List<HealthParticle> particles = new ArrayList<HealthParticle>();
        float currentHealthBarX = 0.0f;
        float currentHealthBarY = 0.0f;
        CardPosition cardPos;

        private PlayerData() {
        }
    }

    private static class CardPosition {
        float x;
        float y;
        float width;
        float height;

        CardPosition(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    private class HealthParticle {
        float x;
        float y;
        float velocityX;
        float velocityY;
        float life;
        Color color;
        PlayerInfoHUD parent;

        HealthParticle(PlayerInfoHUD playerInfoHUD, PlayerInfoHUD playerInfoHUD2, PlayerInfoHUD parent, float x, float y, Color color) {
            this.parent = parent;
            this.x = x;
            this.y = y;
            this.color = color;
            this.velocityX = (float)((Math.random() - 0.5) * 4.0);
            this.velocityY = (float)((Math.random() - 0.5) * 4.0 - 2.0);
            this.life = 1.0f;
        }

        void update(float healthX, float healthY) {
            float targetX = healthX;
            float dx = targetX - this.x;
            float targetY = healthY;
            float dy = targetY - this.y;
            float distance = (float)Math.sqrt(dx * dx + dy * dy);
            if (distance > 0.0f) {
                float force = 0.2f;
                this.velocityX += dx / distance * force;
                this.velocityY += dy / distance * force;
            }
            this.x += this.velocityX;
            this.y += this.velocityY;
            this.velocityY += 0.1f;
            this.velocityX *= 0.95f;
            this.velocityY *= 0.95f;
            this.life -= 0.02f * this.parent.animationSpeed.getValueFloat();
        }

        boolean isAlive() {
            return this.life > 0.0f;
        }

        float getSize() {
            return this.parent.particleSize.getValueFloat();
        }
    }
}

