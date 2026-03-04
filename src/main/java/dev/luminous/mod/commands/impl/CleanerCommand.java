/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Items
 */
package dev.luminous.mod.commands.impl;

import dev.luminous.Alien;
import dev.luminous.core.impl.PlayerManager;
import dev.luminous.mod.commands.Command;
import dev.luminous.mod.gui.windows.WindowsScreen;
import dev.luminous.mod.gui.windows.impl.ItemSelectWindow;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Items;

public class CleanerCommand
extends Command {
    public CleanerCommand() {
        super("cleaner", "[\"\"/name/reset/clear/list] | [add/remove] [name]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            PlayerManager.screenToOpen = new WindowsScreen(new ItemSelectWindow(Alien.CLEANER));
        } else {
            String var2;
            switch (var2 = parameters[0]) {
                case "reset": {
                    Alien.CLEANER.clear();
                    Alien.CLEANER.add(class_1802.field_22022.method_7876());
                    Alien.CLEANER.add(class_1802.field_22024.method_7876());
                    Alien.CLEANER.add(class_1802.field_22027.method_7876());
                    Alien.CLEANER.add(class_1802.field_22028.method_7876());
                    Alien.CLEANER.add(class_1802.field_22029.method_7876());
                    Alien.CLEANER.add(class_1802.field_22030.method_7876());
                    Alien.CLEANER.add(class_1802.field_8281.method_7876());
                    Alien.CLEANER.add(class_1802.field_8466.method_7876());
                    Alien.CLEANER.add(class_1802.field_8634.method_7876());
                    Alien.CLEANER.add(class_1802.field_8367.method_7876());
                    Alien.CLEANER.add(class_1802.field_8287.method_7876());
                    Alien.CLEANER.add(class_1802.field_8786.method_7876());
                    Alien.CLEANER.add(class_1802.field_8574.method_7876());
                    Alien.CLEANER.add(class_1802.field_8436.method_7876());
                    Alien.CLEANER.add(class_1802.field_8288.method_7876());
                    Alien.CLEANER.add(class_1802.field_8301.method_7876());
                    Alien.CLEANER.add(class_1802.field_8833.method_7876());
                    Alien.CLEANER.add(class_1802.field_8884.method_7876());
                    Alien.CLEANER.add(class_1802.field_8249.method_7876());
                    Alien.CLEANER.add(class_1802.field_8105.method_7876());
                    Alien.CLEANER.add(class_1802.field_8793.method_7876());
                    Alien.CLEANER.add(class_1802.field_8801.method_7876());
                    Alien.CLEANER.add(class_1802.field_23141.method_7876());
                    Alien.CLEANER.add(class_1802.field_8782.method_7876());
                    this.sendChatMessage("\u00a7fItems list got reset");
                    return;
                }
                case "clear": {
                    Alien.CLEANER.getList().clear();
                    this.sendChatMessage("\u00a7fItems list got clear");
                    return;
                }
                case "list": {
                    if (Alien.CLEANER.getList().isEmpty()) {
                        this.sendChatMessage("\u00a7fItems list is empty");
                        return;
                    }
                    for (String name : Alien.CLEANER.getList()) {
                        this.sendChatMessage("\u00a7a" + name);
                    }
                    return;
                }
                case "add": {
                    if (parameters.length == 2) {
                        Alien.CLEANER.add(parameters[1]);
                        this.sendChatMessage("\u00a7f" + parameters[1] + (Alien.CLEANER.inList(parameters[1]) ? " \u00a7ahas been added" : " \u00a7chas been removed"));
                        return;
                    }
                    this.sendUsage();
                    return;
                }
                case "remove": {
                    if (parameters.length == 2) {
                        Alien.CLEANER.remove(parameters[1]);
                        this.sendChatMessage("\u00a7f" + parameters[1] + (Alien.CLEANER.inList(parameters[1]) ? " \u00a7ahas been added" : " \u00a7chas been removed"));
                        return;
                    }
                    this.sendUsage();
                    return;
                }
            }
            if (parameters.length == 1) {
                this.sendChatMessage("\u00a7f" + parameters[0] + (Alien.CLEANER.inList(parameters[0]) ? " \u00a7ais in whitelist" : " \u00a7cisn't in whitelist"));
            } else {
                this.sendUsage();
            }
        }
    }

    @Override
    public String[] getAutocorrect(int count, List<String> seperated) {
        if (count != 1) {
            return null;
        }
        String input = seperated.getLast().toLowerCase();
        ArrayList<String> correct = new ArrayList<String>();
        for (String x : List.of("add", "remove", "list", "reset", "clear")) {
            if (!input.equalsIgnoreCase(Alien.getPrefix() + "cleaner") && !x.toLowerCase().startsWith(input)) continue;
            correct.add(x);
        }
        int numCmds = correct.size();
        String[] commands = new String[numCmds];
        int i = 0;
        for (String xx : correct) {
            commands[i++] = xx;
        }
        return commands;
    }
}

