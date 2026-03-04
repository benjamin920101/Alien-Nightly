/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.util.Window
 *  net.minecraft.client.resource.language.I18n
 *  net.minecraft.server.integrated.IntegratedServer
 *  net.minecraft.util.Hand
 *  net.minecraft.SharedConstants
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.hit.HitResult
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.client.MinecraftClient
 *  net.minecraft.client.gui.hud.InGameHud
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.util.thread.ReentrantThreadExecutor
 *  net.minecraft.client.gui.screen.multiplayer.ConnectScreen
 *  net.minecraft.client.gui.screen.DownloadingTerrainScreen
 *  net.minecraft.client.gui.screen.ProgressScreen
 *  net.minecraft.client.gui.screen.Screen
 *  net.minecraft.client.gui.screen.TitleScreen
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.RunArgs
 *  net.minecraft.client.network.ClientPlayNetworkHandler
 *  net.minecraft.client.network.ClientPlayerInteractionManager
 *  net.minecraft.client.world.ClientWorld
 *  net.minecraft.client.network.ServerInfo
 *  net.minecraft.client.particle.ParticleManager
 *  net.minecraft.client.network.ClientPlayerEntity
 *  org.ladysnake.satin.api.managed.ManagedShaderEffect
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.luminous.asm.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.Alien;
import dev.luminous.api.events.Event;
import dev.luminous.api.events.impl.ClientTickEvent;
import dev.luminous.api.events.impl.DoAttackEvent;
import dev.luminous.api.events.impl.GameLeftEvent;
import dev.luminous.api.events.impl.OpenScreenEvent;
import dev.luminous.api.events.impl.ResizeEvent;
import dev.luminous.api.utils.render.Render2DUtil;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.core.impl.FontManager;
import dev.luminous.core.impl.ShaderManager;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.player.InteractTweaks;
import java.awt.Color;
import net.minecraft.client.util.Window;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Hand;
import net.minecraft.SharedConstants;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.hit.HitResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.RunArgs;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.network.ClientPlayerEntity;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_310.class})
public abstract class MixinMinecraftClient
extends class_4093<Runnable> {
    @Shadow
    @Final
    public class_329 field_1705;
    @Shadow
    public int field_1771;
    @Shadow
    public class_746 field_1724;
    @Shadow
    public class_239 field_1765;
    @Shadow
    public class_636 field_1761;
    @Final
    @Shadow
    public class_702 field_1713;
    @Shadow
    public class_638 field_1687;
    @Shadow
    private class_1132 field_1766;
    @Shadow
    public class_437 field_1755;
    @Shadow
    @Final
    private class_1041 field_1704;
    @Unique
    private static long alienStartTs = 0L;

    public MixinMinecraftClient(String string) {
        super(string);
    }

    @Inject(method={"method_15993"}, at={@At(value="TAIL")})
    private void captureResize(CallbackInfo ci) {
        Alien.EVENT_BUS.post(new ResizeEvent(this.field_1704));
    }

    @Redirect(method={"method_1523"}, at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/systems/RenderSystem;limitDisplayFPS(I)V"), require=0)
    public void fpsHook(int fps) {
        if (!ClientSetting.INSTANCE.fuckFPSLimit.getValue()) {
            RenderSystem.limitDisplayFPS((int)fps);
        }
    }

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    void postWindowInit(class_542 args, CallbackInfo ci) {
        FontManager.init();
        alienStartTs = System.currentTimeMillis();
    }

    @Inject(method={"method_1507"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSetScreen(class_437 screen, CallbackInfo info) {
        OpenScreenEvent event = OpenScreenEvent.get(screen);
        Alien.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method={"method_1523"}, at={@At(value="TAIL")})
    private void vitalityMagentaOverlay(CallbackInfo ci) {
        class_437 screen = this.field_1755;
        if (screen != null) {
            boolean isLoading;
            boolean isMainMenu = screen instanceof class_442;
            boolean bl = isLoading = screen instanceof class_435 || screen instanceof class_434 || screen instanceof class_412;
            if ((isMainMenu || isLoading) && !Alien.SHADER.fullNullCheck()) {
                float w = this.field_1704.method_4486();
                float h = this.field_1704.method_4502();
                float t = (float)(System.currentTimeMillis() % 100000L) / 1000.0f;
                ManagedShaderEffect gradient = Alien.SHADER.getShader(ShaderManager.Shader.Gradient);
                gradient.setUniformValue("alpha2", 0.85f);
                gradient.setUniformValue("rgb", 0.78f, 0.05f, 0.59f);
                gradient.setUniformValue("rgb1", 0.56f, 0.06f, 0.68f);
                gradient.setUniformValue("rgb2", 0.93f, 0.12f, 0.63f);
                gradient.setUniformValue("rgb3", 0.64f, 0.0f, 0.64f);
                gradient.setUniformValue("step", 180.0f);
                gradient.setUniformValue("radius", 2.0f);
                gradient.setUniformValue("quality", 1.0f);
                gradient.setUniformValue("divider", 150.0f);
                gradient.setUniformValue("maxSample", 10.0f);
                gradient.setUniformValue("resolution", w, h);
                gradient.setUniformValue("time", t * 300.0f);
                gradient.render(((class_310)this).method_60646().method_60637(true));
                ManagedShaderEffect pulse = Alien.SHADER.getShader(ShaderManager.Shader.Pulse);
                pulse.setUniformValue("mixFactor", 0.65f);
                pulse.setUniformValue("minAlpha", 0.35f);
                pulse.setUniformValue("radius", 2.0f);
                pulse.setUniformValue("quality", 1.0f);
                pulse.setUniformValue("divider", 150.0f);
                pulse.setUniformValue("maxSample", 10.0f);
                pulse.setUniformValue("color", 0.85f, 0.05f, 0.66f);
                pulse.setUniformValue("color2", 0.56f, 0.06f, 0.68f);
                pulse.setUniformValue("time", t);
                pulse.setUniformValue("size", 12.0f);
                pulse.setUniformValue("resolution", w, h);
                pulse.render(((class_310)this).method_60646().method_60637(true));
                class_4587 m = new class_4587();
                w = this.field_1704.method_4486();
                h = this.field_1704.method_4502();
                long now = System.currentTimeMillis();
                long elapsed = now - alienStartTs;
                if (elapsed < 2400L) {
                    float p = Math.max(0.0f, Math.min(1.0f, (float)elapsed / 2400.0f));
                    int aTop = (int)(180.0 * Math.sin((double)p * Math.PI));
                    Color c1 = new Color(28, 60, 110, aTop);
                    Color c2 = new Color(190, 50, 160, aTop);
                    Render2DUtil.verticalGradient(m, 0.0f, 0.0f, w, h, c1, c2);
                    float r = Math.min(w, h) * (0.12f + 0.18f * p);
                    Render2DUtil.drawCircle(m, w / 2.0f, h / 2.0f, r, new Color(255, 255, 255, (int)(70.0f * p)), 80);
                    Render2DUtil.drawCircle(m, w / 2.0f, h / 2.0f, r * 1.2f, new Color(120, 220, 255, (int)(50.0f * p)), 80);
                } else {
                    float ny;
                    float nx;
                    float x0;
                    float i;
                    float phase = (float)(now % 6000L) / 6000.0f;
                    float angle = 0.523599f;
                    float dx = (float)Math.tan(angle) * h;
                    float base = -h;
                    float spacing = 28.0f;
                    float shift = phase * spacing * 5.2f;
                    int cA1 = new Color(230, 60, 170, 64).getRGB();
                    int cA2 = new Color(160, 40, 130, 48).getRGB();
                    int nodeC = new Color(255, 255, 255, 42).getRGB();
                    for (i = base; i < w; i += spacing) {
                        x0 = i + shift;
                        Render2DUtil.drawLine(m, x0, 0.0f, x0 + dx, h, cA1);
                        nx = x0 + dx * 0.25f;
                        ny = h * 0.25f;
                        Render2DUtil.drawCircle(m, nx, ny, 2.5f, new Color(nodeC, true), 32);
                    }
                    for (i = base + spacing / 2.0f; i < w; i += spacing) {
                        x0 = i + shift * 0.85f;
                        Render2DUtil.drawLine(m, x0, 0.0f, x0 + dx, h, cA2);
                        nx = x0 + dx * 0.65f;
                        ny = h * 0.6f;
                        Render2DUtil.drawCircle(m, nx, ny, 2.0f, new Color(nodeC, true), 28);
                    }
                }
            }
        }
    }

    @Inject(method={"method_1536"}, at={@At(value="INVOKE", target="Lnet/minecraft/class_239;method_17783()Lnet/minecraft/class_239$class_240;", shift=At.Shift.BEFORE)})
    public void onAttack(CallbackInfoReturnable<Boolean> cir) {
        Alien.EVENT_BUS.post(DoAttackEvent.getPre());
    }

    @Inject(method={"method_1536"}, at={@At(value="INVOKE", target="Lnet/minecraft/class_746;method_6104(Lnet/minecraft/class_1268;)V", shift=At.Shift.AFTER)})
    public void onAttackPost(CallbackInfoReturnable<Boolean> cir) {
        Alien.EVENT_BUS.post(DoAttackEvent.getPost());
    }

    @Inject(method={"method_56134(Lnet/minecraft/class_437;)V"}, at={@At(value="HEAD")})
    private void onDisconnect(class_437 screen, CallbackInfo info) {
        if (this.field_1687 != null) {
            Alien.EVENT_BUS.post(GameLeftEvent.INSTANCE);
        }
    }

    @Inject(method={"method_56134(Lnet/minecraft/class_437;)V"}, at={@At(value="HEAD")})
    private void clearTitleMixin(class_437 screen, CallbackInfo info) {
        if (ClientSetting.INSTANCE.titleFix.getValue()) {
            this.field_1705.method_34003();
            this.field_1705.method_1742();
        }
    }

    @Inject(method={"method_1590"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleBlockBreaking(boolean breaking, CallbackInfo ci) {
        if (this.field_1771 <= 0 && this.field_1724.method_6115() && InteractTweaks.INSTANCE.multiTask()) {
            if (breaking && this.field_1765 != null && this.field_1765.method_17783() == class_239.class_240.field_1332) {
                class_2350 direction;
                class_3965 blockHitResult = (class_3965)this.field_1765;
                class_2338 blockPos = blockHitResult.method_17777();
                if (!this.field_1687.method_8320(blockPos).method_26215() && this.field_1761.method_2902(blockPos, direction = blockHitResult.method_17780())) {
                    this.field_1713.method_3054(blockPos, direction);
                    this.field_1724.method_6104(class_1268.field_5808);
                }
            } else {
                this.field_1761.method_2925();
            }
            ci.cancel();
        }
    }

    @Inject(at={@At(value="HEAD")}, method={"method_1574()V"})
    public void tickHead(CallbackInfo info) {
        block2: {
            try {
                Alien.EVENT_BUS.post(ClientTickEvent.get(Event.Stage.Pre));
            }
            catch (Exception var3) {
                var3.printStackTrace();
                if (!ClientSetting.INSTANCE.debug.getValue()) break block2;
                CommandManager.sendMessage("\u00a74An error has occurred (MinecraftClient.tick() [HEAD]) Message: [" + var3.getMessage() + "]");
            }
        }
    }

    @Inject(at={@At(value="TAIL")}, method={"method_1574()V"})
    public void tickTail(CallbackInfo info) {
        block2: {
            try {
                Alien.EVENT_BUS.post(ClientTickEvent.get(Event.Stage.Post));
            }
            catch (Exception var3) {
                var3.printStackTrace();
                if (!ClientSetting.INSTANCE.debug.getValue()) break block2;
                CommandManager.sendMessage("\u00a74An error has occurred (MinecraftClient.tick() [TAIL]) Message: [" + var3.getMessage() + "]");
            }
        }
    }

    @Overwrite
    private String method_24287() {
        if (ClientSetting.INSTANCE == null) {
            return "Alien: Loading..";
        }
        if (ClientSetting.INSTANCE.titleOverride.getValue()) {
            return ClientSetting.INSTANCE.windowTitle.getValue();
        }
        StringBuilder stringBuilder = new StringBuilder(ClientSetting.INSTANCE.windowTitle.getValue());
        stringBuilder.append(" ");
        stringBuilder.append(class_155.method_16673().method_48019());
        class_634 clientPlayNetworkHandler = this.method_1562();
        if (clientPlayNetworkHandler != null && clientPlayNetworkHandler.method_48296().method_10758()) {
            stringBuilder.append(" - ");
            class_642 serverInfo = this.method_1558();
            if (this.field_1766 != null && !this.field_1766.method_3860()) {
                stringBuilder.append(class_1074.method_4662((String)"title.singleplayer", (Object[])new Object[0]));
            } else if (serverInfo != null && serverInfo.method_52811()) {
                stringBuilder.append(class_1074.method_4662((String)"title.multiplayer.realms", (Object[])new Object[0]));
            } else if (this.field_1766 != null || serverInfo != null && serverInfo.method_2994()) {
                stringBuilder.append(class_1074.method_4662((String)"title.multiplayer.lan", (Object[])new Object[0]));
            } else {
                stringBuilder.append(class_1074.method_4662((String)"title.multiplayer.other", (Object[])new Object[0]));
            }
        }
        return stringBuilder.toString();
    }

    @Shadow
    public class_634 method_1562() {
        return null;
    }

    @Shadow
    public class_642 method_1558() {
        return null;
    }
}

