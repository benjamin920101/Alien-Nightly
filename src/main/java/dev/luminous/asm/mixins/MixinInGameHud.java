/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.hud.InGameHud
 *  net.minecraft.client.gui.DrawContext
 *  net.minecraft.registry.tag.FluidTags
 *  net.minecraft.client.render.RenderTickCounter
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.events.impl.PreRender2DEvent;
import dev.luminous.api.utils.player.EntityUtil;
import dev.luminous.api.utils.render.ColorUtil;
import dev.luminous.api.utils.render.TextUtil;
import dev.luminous.core.impl.FontManager;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.client.HUD;
import dev.luminous.mod.modules.impl.render.Crosshair;
import dev.luminous.mod.modules.impl.render.NoRender;
import java.awt.Color;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_329.class})
public abstract class MixinInGameHud {
    @Final
    @Shadow
    private class_310 field_2035;
    @Unique
    final Color minColor = new Color(196, 0, 0);
    @Unique
    final Color maxColor = new Color(0, 227, 0);

    @Inject(method={"method_1746"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderPortalOverlay(class_332 context, float nauseaStrength, CallbackInfo ci) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.portal.getValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"method_1765"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderStatusEffectOverlay(CallbackInfo info) {
        if (NoRender.INSTANCE.isOn() && NoRender.INSTANCE.potionsIcon.getValue()) {
            info.cancel();
        }
    }

    @Inject(method={"method_55805"}, at={@At(value="TAIL")})
    private void onRenderMainHud(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        class_1657 player;
        if (HUD.INSTANCE.isOn() && HUD.INSTANCE.armor.getValue() && (player = this.method_1737()) != null) {
            int t;
            int x = context.method_51421() / 2 + 91;
            int y = context.method_51443() - 28 - HUD.INSTANCE.armorOffset.getValueInt();
            if (this.field_2035.field_1761.method_2908()) {
                y -= 16;
            }
            if ((t = this.method_1744(this.method_1734())) == 0) {
                y -= 10;
            }
            int maxAir = player.method_5748();
            int air = Math.min(player.method_5669(), maxAir);
            if (player.method_5777(class_3486.field_15517) || air < maxAir) {
                int w = this.method_1733(t) - 1;
                y += w * 10;
            }
            for (class_1799 armor : this.field_2035.field_1724.method_31548().field_7548) {
                x -= 20;
                if (armor.method_7960()) continue;
                context.method_51448().method_22903();
                int damage = EntityUtil.getDamagePercent(armor);
                context.method_51427(armor, x, y);
                context.method_51431(this.field_2035.field_1772, armor, x, y);
                if (HUD.INSTANCE.durability.getValue()) {
                    if (HUD.INSTANCE.font.getValue()) {
                        FontManager.small.drawString(context.method_51448(), damage + "%", (double)(x + 1), (double)((float)y - FontManager.small.getFontHeight() / 2.0f), ColorUtil.fadeColor(this.minColor, this.maxColor, (float)damage / 100.0f).getRGB(), HUD.INSTANCE.shadow.getValue());
                    } else {
                        TextUtil.drawStringScale(context, damage + "%", x + 2, (float)y - 2.25f, ColorUtil.fadeColor(this.minColor, this.maxColor, (float)damage / 100.0f).getRGB(), 0.5f, HUD.INSTANCE.shadow.getValue());
                    }
                }
                context.method_51448().method_22909();
            }
        }
    }

    @Shadow
    private int method_1733(int t) {
        return 0;
    }

    @Shadow
    private int method_1744(class_1309 livingEntity) {
        return 0;
    }

    @Shadow
    private class_1657 method_1737() {
        return null;
    }

    @Shadow
    private class_1309 method_1734() {
        return null;
    }

    @Shadow
    public abstract void method_1753(class_332 var1, class_9779 var2);

    @Inject(at={@At(value="HEAD")}, method={"method_1753"})
    public void renderStart(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        Alien.EVENT_BUS.post(PreRender2DEvent.get(context));
    }

    @Inject(at={@At(value="TAIL")}, method={"method_1753"})
    public void renderHook(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        Alien.MODULE.onRender2D(context);
    }

    @Inject(method={"method_1747"}, at={@At(value="INVOKE", target="Lnet/minecraft/class_338;method_1808(Z)V")}, cancellable=true)
    private void onClear(CallbackInfo info) {
        if (ClientSetting.INSTANCE.isOn() && ClientSetting.INSTANCE.keepHistory.getValue()) {
            info.cancel();
        }
    }

    @ModifyArg(method={"method_1759"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_332;method_52706(Lnet/minecraft/class_2960;IIII)V", ordinal=1), index=1)
    private int selectedSlotX(int x) {
        if (ClientSetting.INSTANCE.hotbar()) {
            double hotbarX = ClientSetting.animation.get(x, ClientSetting.INSTANCE.hotbarTime.getValueInt(), ClientSetting.INSTANCE.animEase.getValue());
            return (int)hotbarX;
        }
        return x;
    }

    @Inject(method={"method_1736"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderCrosshairBegin(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        if (Crosshair.INSTANCE.isOn()) {
            Crosshair.INSTANCE.draw(context);
            ci.cancel();
        }
    }
}

