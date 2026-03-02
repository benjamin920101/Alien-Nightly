/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_239
 *  net.minecraft.class_3966
 */
package dev.luminous.mod.modules.impl.misc;

import dev.luminous.Alien;
import dev.luminous.mod.modules.Module;
import net.minecraft.class_1657;
import net.minecraft.class_239;
import net.minecraft.class_3966;

public class Friend
extends Module {
    public static Friend INSTANCE;

    public Friend() {
        super("Friend", Module.Category.Misc);
        this.setChinese("\u597d\u53cb");
        INSTANCE = this;
    }

    @Override
    public boolean onEnable() {
        if (Friend.nullCheck()) {
            this.disable();
        } else {
            class_3966 entityHitResult;
            class_239 class_2392 = Friend.mc.field_1765;
            if (class_2392 instanceof class_3966 && (class_2392 = (entityHitResult = (class_3966)class_2392).method_17782()) instanceof class_1657) {
                class_1657 player = (class_1657)class_2392;
                Alien.FRIEND.friend(player);
            }
            this.disable();
        }
        return false;
    }
}

