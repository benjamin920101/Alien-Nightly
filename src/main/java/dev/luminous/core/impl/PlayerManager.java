/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1263
 *  net.minecraft.class_1657
 *  net.minecraft.class_1707
 *  net.minecraft.class_1799
 *  net.minecraft.class_2246
 *  net.minecraft.class_2336
 *  net.minecraft.class_2338
 *  net.minecraft.class_2371
 *  net.minecraft.class_238
 *  net.minecraft.class_437
 *  net.minecraft.class_476
 *  net.minecraft.class_5134
 */
package dev.luminous.core.impl;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.BlockActivateEvent;
import dev.luminous.api.events.impl.GameLeftEvent;
import dev.luminous.api.events.impl.OpenScreenEvent;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.mod.modules.Module;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.class_1263;
import net.minecraft.class_1657;
import net.minecraft.class_1707;
import net.minecraft.class_1799;
import net.minecraft.class_2246;
import net.minecraft.class_2336;
import net.minecraft.class_2338;
import net.minecraft.class_2371;
import net.minecraft.class_238;
import net.minecraft.class_437;
import net.minecraft.class_476;
import net.minecraft.class_5134;

public class PlayerManager
implements Wrapper {
    public static class_437 screenToOpen;
    public final class_2371<class_1799> ENDERCHEST_ITEM = class_2371.method_10213((int)27, (Object)class_1799.field_8037);
    public final Map<class_1657, EntityAttribute> map = new ConcurrentHashMap<class_1657, EntityAttribute>();
    public final CopyOnWriteArrayList<class_1657> inWebPlayers = new CopyOnWriteArrayList();
    public boolean known = false;
    private int echestOpenedState;

    public PlayerManager() {
        Alien.EVENT_BUS.subscribe(this);
    }

    @EventListener
    public void onLogout(GameLeftEvent event) {
        this.inWebPlayers.clear();
        this.map.clear();
        this.ENDERCHEST_ITEM.clear();
        this.known = false;
    }

    @EventListener
    private void onBlockActivate(BlockActivateEvent event) {
        if (event.blockState.method_26204() instanceof class_2336 && this.echestOpenedState == 0) {
            this.echestOpenedState = 1;
        }
    }

    @EventListener
    private void onOpenScreenEvent(OpenScreenEvent event) {
        class_1707 container;
        if (this.echestOpenedState == 1 && event.screen instanceof class_476) {
            this.echestOpenedState = 2;
        } else if (this.echestOpenedState != 0 && PlayerManager.mc.field_1755 instanceof class_476 && (container = (class_1707)((class_476)PlayerManager.mc.field_1755).method_17577()) != null) {
            class_1263 inv = container.method_7629();
            for (int i = 0; i < 27; ++i) {
                this.ENDERCHEST_ITEM.set(i, (Object)inv.method_5438(i));
            }
            this.known = true;
            this.echestOpenedState = 0;
        }
    }

    public void onUpdate() {
        if (!Module.nullCheck()) {
            if (screenToOpen != null && PlayerManager.mc.field_1755 == null) {
                mc.method_1507(screenToOpen);
                screenToOpen = null;
            }
            this.inWebPlayers.clear();
            for (class_1657 class_16572 : Alien.THREAD.getPlayers()) {
                this.map.put(class_16572, new EntityAttribute(class_16572.method_6096(), class_16572.method_45325(class_5134.field_23725)));
                this.webUpdate(class_16572);
            }
        }
    }

    public boolean isInWeb(class_1657 player) {
        return this.inWebPlayers.contains(player);
    }

    private void webUpdate(class_1657 player) {
        for (float x : new float[]{0.0f, 0.3f, -0.3f}) {
            for (float z : new float[]{0.0f, 0.3f, -0.3f}) {
                for (int y : new int[]{-1, 0, 1, 2}) {
                    class_2338 pos = new BlockPosX(player.method_23317() + (double)x, player.method_23318(), player.method_23321() + (double)z).method_10086(y);
                    if (!new class_238(pos).method_994(player.method_5829()) || PlayerManager.mc.field_1687.method_8320(pos).method_26204() != class_2246.field_10343) continue;
                    this.inWebPlayers.add(player);
                    return;
                }
            }
        }
    }

    public record EntityAttribute(int armor, double toughness) {
    }
}

