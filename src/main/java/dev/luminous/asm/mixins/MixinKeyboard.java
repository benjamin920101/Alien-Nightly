/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_309
 *  net.minecraft.class_333
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.luminous.asm.mixins;

import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.core.impl.CommandManager;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import net.minecraft.class_124;
import net.minecraft.class_309;
import net.minecraft.class_333;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_309.class})
public class MixinKeyboard
implements Wrapper {
    @Inject(method={"method_1466"}, at={@At(value="HEAD")})
    private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        block4: {
            try {
                if (action == 1) {
                    Alien.MODULE.onKeyPressed(key);
                }
                if (action == 0) {
                    Alien.MODULE.onKeyReleased(key);
                }
            }
            catch (Exception var9) {
                var9.printStackTrace();
                if (!ClientSetting.INSTANCE.debug.getValue()) break block4;
                CommandManager.sendMessage(String.valueOf(class_124.field_1079) + "[ERROR] onKey " + var9.getMessage());
            }
        }
    }

    @Redirect(method={"method_1466"}, at=@At(value="INVOKE", target="Lnet/minecraft/class_333;method_1791()Z"), require=0)
    public boolean hook(class_333 instance) {
        return false;
    }
}

