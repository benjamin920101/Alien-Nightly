/*
 * Decompiled with CFR 0.152.
 */
package dev.luminous.mod.commands.impl;

import dev.luminous.Alien;
import dev.luminous.mod.commands.Command;
import dev.luminous.mod.modules.impl.misc.FakePlayer;
import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import java.util.List;

public class FakePlayerCommand
extends Command {
    public FakePlayerCommand() {
        super("fakeplayer", "[record/play]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            FakePlayer.INSTANCE.toggle();
        } else {
            String string = parameters[0];
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{"record", "play"}, (Object)string, n)) {
                case 0: {
                    FakePlayer.INSTANCE.record.setValue(!FakePlayer.INSTANCE.record.getValue());
                    break;
                }
                case 1: {
                    FakePlayer.INSTANCE.play.setValue(!FakePlayer.INSTANCE.play.getValue());
                    break;
                }
                default: {
                    this.sendUsage();
                }
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
        for (String x : List.of("record", "play")) {
            if (!input.equalsIgnoreCase(Alien.getPrefix() + "fakeplayer") && !x.toLowerCase().startsWith(input)) continue;
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

