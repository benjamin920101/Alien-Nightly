/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 */
package dev.luminous.api.utils.path;

import dev.luminous.api.utils.Wrapper;
import dev.luminous.mod.modules.impl.client.BaritoneModule;
import net.minecraft.class_2248;
import net.minecraft.class_2338;

public class BaritoneUtil
implements Wrapper {
    public static boolean loaded;

    public static void gotoPos(class_2338 pos) {
        if (loaded) {
            BaritoneModule.gotoPos(pos);
        }
    }

    public static void forward() {
        if (loaded) {
            BaritoneModule.forward();
        }
    }

    public static void mine(class_2248 block) {
        if (loaded) {
            BaritoneModule.mine(block);
        }
    }

    public static boolean isPathing() {
        return loaded ? BaritoneModule.isPathing() : false;
    }

    public static void cancelEverything() {
        if (loaded) {
            BaritoneModule.cancelEverything();
        }
    }

    public static boolean isActive() {
        return loaded ? BaritoneModule.isActive() : false;
    }

    static {
        Package[] packages;
        for (Package pkg : packages = Package.getPackages()) {
            if (!pkg.getName().contains("baritone.api")) continue;
            loaded = true;
            break;
        }
    }
}

