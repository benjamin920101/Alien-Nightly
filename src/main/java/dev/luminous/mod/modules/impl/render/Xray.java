/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.BlockView
 *  net.minecraft.block.Block
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$Mutable
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.util.shape.VoxelShapes
 *  net.minecraft.block.BlockState
 *  net.minecraft.client.gui.screen.Screen
 */
package dev.luminous.mod.modules.impl.render;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.AmbientOcclusionEvent;
import dev.luminous.api.events.impl.ChunkOcclusionEvent;
import dev.luminous.api.events.impl.RenderBlockEntityEvent;
import dev.luminous.mod.gui.windows.WindowsScreen;
import dev.luminous.mod.gui.windows.impl.ItemSelectWindow;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import net.minecraft.world.BlockView;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;

public class Xray
extends Module {
    public static Xray INSTANCE;
    public final BooleanSetting edit = this.add(new BooleanSetting("Edit", false).injectTask(this::openGui));
    private static final ThreadLocal<class_2338.class_2339> EXPOSED_POS;

    public Xray() {
        super("Xray", Module.Category.Render);
        this.setChinese("\u77ff\u7269\u900f\u89c6");
        INSTANCE = this;
    }

    private void openGui() {
        this.edit.setValueWithoutTask(false);
        if (!Xray.nullCheck()) {
            mc.method_1507((class_437)new WindowsScreen(new ItemSelectWindow(Alien.XRAY)));
        }
    }

    @Override
    public boolean onEnable() {
        if (!Xray.nullCheck()) {
            Xray.mc.field_1769.method_3279();
        }
        return false;
    }

    @Override
    public void onDisable() {
        Xray.mc.field_1769.method_3279();
    }

    public boolean isBlocked(class_2248 block) {
        return !Alien.XRAY.inWhitelist(block.method_9539());
    }

    @EventListener
    private void onRenderBlockEntity(RenderBlockEntityEvent event) {
        if (this.isBlocked(event.blockEntity.method_11010().method_26204())) {
            event.cancel();
        }
    }

    @EventListener
    private void onChunkOcclusion(ChunkOcclusionEvent event) {
        event.cancel();
    }

    @EventListener
    private void onAmbientOcclusion(AmbientOcclusionEvent event) {
        event.lightLevel = 1.0f;
    }

    public static boolean shouldBlock(class_2680 state) {
        return INSTANCE.isOn() && INSTANCE.isBlocked(state.method_26204());
    }

    public boolean modifyDrawSide(class_2680 state, class_1922 view, class_2338 pos, class_2350 facing, boolean returns) {
        if (!returns && !this.isBlocked(state.method_26204())) {
            class_2338 adjPos = pos.method_10093(facing);
            class_2680 adjState = view.method_8320(adjPos);
            return adjState.method_26173(view, adjPos, facing.method_10153()) != class_259.method_1077() || adjState.method_26204() != state.method_26204() || Xray.isExposed(adjPos);
        }
        return returns;
    }

    public static boolean isExposed(class_2338 blockPos) {
        for (class_2350 direction : class_2350.values()) {
            if (Xray.mc.field_1687.method_8320((class_2338)EXPOSED_POS.get().method_25505((class_2382)blockPos, direction)).method_26225()) continue;
            return true;
        }
        return false;
    }

    static {
        EXPOSED_POS = ThreadLocal.withInitial(class_2338.class_2339::new);
    }
}

