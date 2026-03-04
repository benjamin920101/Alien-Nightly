/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 */
package dev.luminous.mod.commands.impl;

import dev.luminous.api.utils.path.TPUtils;
import dev.luminous.mod.commands.Command;
import java.text.DecimalFormat;
import java.util.List;
import net.minecraft.util.math.Vec3d;

public class TeleportCommand
extends Command {
    public TeleportCommand() {
        super("tp", "[x] [y] [z]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length != 3) {
            this.sendUsage();
        } else {
            double z;
            double y;
            double x;
            if (this.isNumeric(parameters[0])) {
                x = Double.parseDouble(parameters[0]);
            } else {
                if (!parameters[0].startsWith("~")) {
                    this.sendUsage();
                    return;
                }
                if (this.isNumeric(parameters[0].replace("~", ""))) {
                    x = TeleportCommand.mc.field_1724.method_23317() + Double.parseDouble(parameters[0].replace("~", ""));
                } else {
                    if (!parameters[0].replace("~", "").isEmpty()) {
                        this.sendUsage();
                        return;
                    }
                    x = TeleportCommand.mc.field_1724.method_23317();
                }
            }
            if (this.isNumeric(parameters[1])) {
                y = Double.parseDouble(parameters[1]);
            } else {
                if (!parameters[1].startsWith("~")) {
                    this.sendUsage();
                    return;
                }
                if (this.isNumeric(parameters[1].replace("~", ""))) {
                    y = TeleportCommand.mc.field_1724.method_23318() + Double.parseDouble(parameters[1].replace("~", ""));
                } else {
                    if (!parameters[1].replace("~", "").isEmpty()) {
                        this.sendUsage();
                        return;
                    }
                    y = TeleportCommand.mc.field_1724.method_23318();
                }
            }
            if (this.isNumeric(parameters[2])) {
                z = Double.parseDouble(parameters[2]);
            } else {
                if (!parameters[2].startsWith("~")) {
                    this.sendUsage();
                    return;
                }
                if (this.isNumeric(parameters[2].replace("~", ""))) {
                    z = TeleportCommand.mc.field_1724.method_23321() + Double.parseDouble(parameters[2].replace("~", ""));
                } else {
                    if (!parameters[2].replace("~", "").isEmpty()) {
                        this.sendUsage();
                        return;
                    }
                    z = TeleportCommand.mc.field_1724.method_23321();
                }
            }
            TPUtils.newTeleport(new class_243(x, y, z));
            TeleportCommand.mc.field_1724.method_5814(x, y, z);
            DecimalFormat df = new DecimalFormat("0.0");
            this.sendChatMessage("\u00a7fTeleported to \u00a7e" + df.format(x) + ", " + df.format(y) + ", " + df.format(z));
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        return new String[]{"~ "};
    }
}

