/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1934
 */
package dev.luminous.mod.commands.impl;

import dev.luminous.mod.commands.Command;
import java.util.List;
import net.minecraft.class_1934;

public class GamemodeCommand
extends Command {
    public GamemodeCommand() {
        super("gamemode", "[gamemode]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            this.sendUsage();
        } else {
            String moduleName = parameters[0];
            if (moduleName.equalsIgnoreCase("survival")) {
                GamemodeCommand.mc.field_1761.method_2907(class_1934.field_9215);
            } else if (moduleName.equalsIgnoreCase("creative")) {
                GamemodeCommand.mc.field_1761.method_2907(class_1934.field_9220);
            } else if (moduleName.equalsIgnoreCase("adventure")) {
                GamemodeCommand.mc.field_1761.method_2907(class_1934.field_9216);
            } else if (moduleName.equalsIgnoreCase("spectator")) {
                GamemodeCommand.mc.field_1761.method_2907(class_1934.field_9219);
            }
        }
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        String[] stringArray;
        if (count == 1) {
            String[] stringArray2 = new String[4];
            stringArray2[0] = "survival";
            stringArray2[1] = "creative";
            stringArray2[2] = "adventure";
            stringArray = stringArray2;
            stringArray2[3] = "spectator";
        } else {
            stringArray = null;
        }
        return stringArray;
    }
}

