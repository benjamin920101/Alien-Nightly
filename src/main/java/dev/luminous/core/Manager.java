/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 */
package dev.luminous.core;

import java.io.File;
import net.minecraft.class_310;

public class Manager {
    public static final class_310 mc = class_310.method_1551();

    public static File getFile(String s) {
        File folder = Manager.getFolder();
        return new File(folder, s);
    }

    public static File getFolder() {
        File folder = new File(Manager.mc.field_1697.getPath() + File.separator + "Alien".toLowerCase());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
}

