/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 */
package dev.luminous.mod.commands.impl;

import dev.luminous.mod.commands.Command;
import dev.luminous.mod.modules.impl.misc.ShulkerViewer;
import java.util.List;
import net.minecraft.class_1799;

public class PeekCommand
extends Command {
    private static final class_1799[] ITEMS = new class_1799[27];

    public PeekCommand() {
        super("peek", "");
    }

    @Override
    public void runCommand(String[] parameters) {
        ShulkerViewer.openContainer(PeekCommand.mc.field_1724.method_6047(), ITEMS, true);
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return null;
    }
}

