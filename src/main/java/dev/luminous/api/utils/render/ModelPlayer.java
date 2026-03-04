/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.client.render.BufferBuilder
 *  net.minecraft.client.render.Tessellator
 *  net.minecraft.client.render.VertexFormats
 *  net.minecraft.client.render.VertexFormat$DrawMode
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.client.util.math.MatrixStack
 *  net.minecraft.client.render.entity.model.EntityModelLayers
 *  net.minecraft.client.render.entity.EntityRendererFactory$Context
 *  net.minecraft.client.render.entity.model.PlayerEntityModel
 *  net.minecraft.client.model.ModelPart
 *  net.minecraft.client.model.ModelPart$Quad
 *  net.minecraft.client.model.ModelPart$Cuboid
 *  net.minecraft.client.render.GameRenderer
 *  net.minecraft.util.math.RotationAxis
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Vector3f
 *  org.joml.Vector4f
 */
package dev.luminous.api.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.math.MathUtil;
import dev.luminous.api.utils.render.Render3DUtil;
import dev.luminous.asm.accessors.ILivingEntity;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import java.awt.Color;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ModelPlayer
extends class_591<class_1657> {
    public final class_1657 player;
    private static final Vector4f pos1 = new Vector4f();
    private static final Vector4f pos2 = new Vector4f();
    private static final Vector4f pos3 = new Vector4f();
    private static final Vector4f pos4 = new Vector4f();

    public ModelPlayer(class_1657 player) {
        super(new class_5617.class_5618(Wrapper.mc.method_1561(), Wrapper.mc.method_1480(), Wrapper.mc.method_1541(), Wrapper.mc.method_1561().method_43336(), Wrapper.mc.method_1478(), Wrapper.mc.method_31974(), Wrapper.mc.field_1772).method_32167(class_5602.field_27577), false);
        this.player = player;
        this.field_3482.field_3665 = false;
        this.field_3479.field_3665 = false;
        this.field_3484.field_3665 = false;
        this.field_3486.field_3665 = false;
        this.field_3483.field_3665 = false;
        this.field_3394.field_3665 = false;
        this.method_2838().method_41924(new Vector3f(-0.05f, -0.05f, -0.05f));
        this.field_3400 = player.method_18276();
    }

    public void render(class_4587 matrices, ColorSetting fill, ColorSetting line) {
        this.render(matrices, fill, line, 1.0, 0.0, 1.0, 0.0, false, false);
    }

    public void render(class_4587 matrices, ColorSetting fill, ColorSetting line, double alpha, double yOffset, double scale, double yaw, boolean noLimb, boolean forceSneaking) {
        if (forceSneaking) {
            this.field_3400 = true;
        }
        double x = this.player.method_23317() - Wrapper.mc.method_1561().field_4686.method_19326().method_10216();
        double y = this.player.method_23318() - Wrapper.mc.method_1561().field_4686.method_19326().method_10214() + yOffset;
        double z = this.player.method_23321() - Wrapper.mc.method_1561().field_4686.method_19326().method_10215();
        matrices.method_22903();
        matrices.method_46416((float)x, (float)y, (float)z);
        matrices.method_22907(class_7833.field_40716.rotation(MathUtil.rad(180.0f - this.player.field_6283 + (float)yaw)));
        this.field_3447 = this.player.method_6055(1.0f);
        float j = ((ILivingEntity)this.player).getLeaningPitch();
        if (this.player.method_6128()) {
            float k = this.player.method_36455();
            float l = (float)this.player.method_6003() + this.player.field_6283 + (float)yaw;
            float m = class_3532.method_15363((float)(l * l / 100.0f), (float)0.0f, (float)1.0f);
            if (!this.player.method_6123()) {
                matrices.method_22907(class_7833.field_40714.rotationDegrees(m * (-90.0f - k)));
            }
            class_243 vec3d = this.player.method_5828(1.0f);
            class_243 vec3d2 = this.player.method_18798();
            double d = vec3d2.method_37268();
            double e = vec3d.method_37268();
            if (d > 0.0 && e > 0.0) {
                double n = (vec3d2.field_1352 * vec3d.field_1352 + vec3d2.field_1350 * vec3d.field_1350) / Math.sqrt(d * e);
                double o = vec3d2.field_1352 * vec3d.field_1350 - vec3d2.field_1350 * vec3d.field_1352;
                matrices.method_22907(class_7833.field_40716.rotation((float)(Math.signum(o) * Math.acos(n))));
            }
        } else if (j > 0.0f) {
            float kx = this.player.method_36455();
            float lx = this.player.method_5799() ? -90.0f - kx : -90.0f;
            float mx = class_3532.method_16439((float)j, (float)0.0f, (float)lx);
            matrices.method_22907(class_7833.field_40714.rotationDegrees(mx));
            if (this.player.method_20232()) {
                matrices.method_46416(0.0f, -1.0f, 0.3f);
            }
        }
        matrices.method_22905(-1.0f, -1.0f, 1.0f);
        matrices.method_46416(0.0f, -1.401f, 0.0f);
        matrices.method_22905((float)scale * 0.93f, (float)scale * 0.93f, (float)scale * 0.93f);
        this.method_17086((class_1309)this.player, noLimb ? 0.0f : this.player.field_42108.method_48569(), noLimb ? 0.0f : this.player.field_42108.method_48566(), Wrapper.mc.method_60646().method_60637(true));
        this.method_17087((class_1309)this.player, noLimb ? 0.0f : this.player.field_42108.method_48569(), noLimb ? 0.0f : this.player.field_42108.method_48566(), this.player.field_6012, this.player.field_6241 - this.player.field_6283, this.player.method_36455());
        this.field_3449 = this.player.method_5765();
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(class_757::method_34540);
        this.method_22946().forEach(modelPart -> ModelPlayer.render(matrices, modelPart, fill, line, alpha, false));
        this.method_22948().forEach(modelPart -> ModelPlayer.render(matrices, modelPart, fill, line, alpha, false));
        matrices.method_22909();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    public static void render(class_4587 matrices, class_630 part, ColorSetting fill, ColorSetting line, double alpha, boolean texture) {
        if (!(!part.field_3665 || part.field_3663.isEmpty() && part.field_3661.isEmpty())) {
            matrices.method_22903();
            part.method_22703(matrices);
            for (class_630.class_628 cuboid : part.field_3663) {
                ModelPlayer.render(matrices, cuboid, fill, line, alpha, texture);
            }
            for (class_630 child : part.field_3661.values()) {
                ModelPlayer.render(matrices, child, fill, line, alpha, texture);
            }
            matrices.method_22909();
        }
    }

    public static void render(class_4587 matrices, class_630.class_628 cuboid, ColorSetting fill, ColorSetting line, double alpha, boolean texture) {
        Matrix4f matrix = matrices.method_23760().method_23761();
        for (class_630.class_593 quad : cuboid.field_3649) {
            class_287 buffer;
            float b;
            float g;
            float r;
            float a;
            Color color;
            pos1.set(quad.field_3502[0].field_3605.x / 16.0f, quad.field_3502[0].field_3605.y / 16.0f, quad.field_3502[0].field_3605.z / 16.0f, 1.0f);
            pos1.mul((Matrix4fc)matrix);
            pos2.set(quad.field_3502[1].field_3605.x / 16.0f, quad.field_3502[1].field_3605.y / 16.0f, quad.field_3502[1].field_3605.z / 16.0f, 1.0f);
            pos2.mul((Matrix4fc)matrix);
            pos3.set(quad.field_3502[2].field_3605.x / 16.0f, quad.field_3502[2].field_3605.y / 16.0f, quad.field_3502[2].field_3605.z / 16.0f, 1.0f);
            pos3.mul((Matrix4fc)matrix);
            pos4.set(quad.field_3502[3].field_3605.x / 16.0f, quad.field_3502[3].field_3605.y / 16.0f, quad.field_3502[3].field_3605.z / 16.0f, 1.0f);
            pos4.mul((Matrix4fc)matrix);
            if (fill.booleanValue) {
                color = fill.getValue();
                a = (float)((double)((float)color.getAlpha() / 255.0f) * alpha);
                r = (float)color.getRed() / 255.0f;
                g = (float)color.getGreen() / 255.0f;
                b = (float)color.getBlue() / 255.0f;
                buffer = class_289.method_1348().method_60827(class_293.class_5596.field_27382, texture ? class_290.field_1575 : class_290.field_1576);
                buffer.method_22912(ModelPlayer.pos1.x, ModelPlayer.pos1.y, ModelPlayer.pos1.z).method_22913(quad.field_3502[0].field_3604, quad.field_3502[0].field_3603).method_22915(r, g, b, a);
                buffer.method_22912(ModelPlayer.pos2.x, ModelPlayer.pos2.y, ModelPlayer.pos2.z).method_22913(quad.field_3502[1].field_3604, quad.field_3502[1].field_3603).method_22915(r, g, b, a);
                buffer.method_22912(ModelPlayer.pos2.x, ModelPlayer.pos2.y, ModelPlayer.pos2.z).method_22913(quad.field_3502[1].field_3604, quad.field_3502[1].field_3603).method_22915(r, g, b, a);
                buffer.method_22912(ModelPlayer.pos3.x, ModelPlayer.pos3.y, ModelPlayer.pos3.z).method_22913(quad.field_3502[2].field_3604, quad.field_3502[2].field_3603).method_22915(r, g, b, a);
                buffer.method_22912(ModelPlayer.pos3.x, ModelPlayer.pos3.y, ModelPlayer.pos3.z).method_22913(quad.field_3502[2].field_3604, quad.field_3502[2].field_3603).method_22915(r, g, b, a);
                buffer.method_22912(ModelPlayer.pos4.x, ModelPlayer.pos4.y, ModelPlayer.pos4.z).method_22913(quad.field_3502[3].field_3604, quad.field_3502[3].field_3603).method_22915(r, g, b, a);
                buffer.method_22912(ModelPlayer.pos1.x, ModelPlayer.pos1.y, ModelPlayer.pos1.z).method_22913(quad.field_3502[0].field_3604, quad.field_3502[0].field_3603).method_22915(r, g, b, a);
                buffer.method_22912(ModelPlayer.pos1.x, ModelPlayer.pos1.y, ModelPlayer.pos1.z).method_22913(quad.field_3502[0].field_3604, quad.field_3502[0].field_3603).method_22915(r, g, b, a);
                Render3DUtil.endBuilding(buffer);
            }
            if (!line.booleanValue) continue;
            color = line.getValue();
            a = (float)((double)((float)color.getAlpha() / 255.0f) * alpha);
            r = (float)color.getRed() / 255.0f;
            g = (float)color.getGreen() / 255.0f;
            b = (float)color.getBlue() / 255.0f;
            buffer = class_289.method_1348().method_60827(class_293.class_5596.field_29344, texture ? class_290.field_1575 : class_290.field_1576);
            buffer.method_22912(ModelPlayer.pos1.x, ModelPlayer.pos1.y, ModelPlayer.pos1.z).method_22913(quad.field_3502[0].field_3604, quad.field_3502[0].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(ModelPlayer.pos2.x, ModelPlayer.pos2.y, ModelPlayer.pos2.z).method_22913(quad.field_3502[1].field_3604, quad.field_3502[1].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(ModelPlayer.pos2.x, ModelPlayer.pos2.y, ModelPlayer.pos2.z).method_22913(quad.field_3502[1].field_3604, quad.field_3502[1].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(ModelPlayer.pos3.x, ModelPlayer.pos3.y, ModelPlayer.pos3.z).method_22913(quad.field_3502[2].field_3604, quad.field_3502[2].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(ModelPlayer.pos3.x, ModelPlayer.pos3.y, ModelPlayer.pos3.z).method_22913(quad.field_3502[2].field_3604, quad.field_3502[2].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(ModelPlayer.pos4.x, ModelPlayer.pos4.y, ModelPlayer.pos4.z).method_22913(quad.field_3502[3].field_3604, quad.field_3502[3].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(ModelPlayer.pos4.x, ModelPlayer.pos4.y, ModelPlayer.pos4.z).method_22913(quad.field_3502[3].field_3604, quad.field_3502[3].field_3603).method_22915(r, g, b, a);
            buffer.method_22912(ModelPlayer.pos1.x, ModelPlayer.pos1.y, ModelPlayer.pos1.z).method_22913(quad.field_3502[0].field_3604, quad.field_3502[0].field_3603).method_22915(r, g, b, a);
            Render3DUtil.endBuilding(buffer);
        }
    }
}

