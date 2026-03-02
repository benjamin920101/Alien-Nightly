/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_2661
 *  net.minecraft.class_310
 *  net.minecraft.class_412
 *  net.minecraft.class_437
 *  net.minecraft.class_442
 *  net.minecraft.class_639
 *  net.minecraft.class_642
 *  net.minecraft.class_8705
 */
package dev.luminous.mod.commands.impl;

import dev.luminous.mod.commands.Command;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_2661;
import net.minecraft.class_310;
import net.minecraft.class_412;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_8705;

public class RejoinCommand
extends Command {
    public RejoinCommand() {
        super("rejoin", "");
    }

    @Override
    public void runCommand(String[] parameters) {
        mc.method_18859(() -> {
            if (RejoinCommand.mc.field_1687 != null && mc.method_1558() != null) {
                class_642 lastestServerEntry = mc.method_1558();
                new class_2661(class_2561.method_30163((String)"Self kick")).method_11467((class_8705)mc.method_1562());
                class_412.method_36877((class_437)new class_442(), (class_310)mc, (class_639)class_639.method_2950((String)lastestServerEntry.field_3761), (class_642)lastestServerEntry, (boolean)false, null);
            }
        });
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}

