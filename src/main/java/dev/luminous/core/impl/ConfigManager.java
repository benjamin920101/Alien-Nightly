/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Splitter
 *  org.apache.commons.io.IOUtils
 */
package dev.luminous.core.impl;

import com.google.common.base.Splitter;
import dev.luminous.Alien;
import dev.luminous.core.Manager;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.client.HUD;
import dev.luminous.mod.modules.settings.Setting;
import dev.luminous.mod.modules.settings.impl.BindSetting;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.ColorSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import dev.luminous.mod.modules.settings.impl.SliderSetting;
import dev.luminous.mod.modules.settings.impl.StringSetting;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

public class ConfigManager
extends Manager {
    public static File options = ConfigManager.getFile("options.txt");
    private final Hashtable<String, String> settings = new Hashtable();

    public ConfigManager() {
        this.read();
    }

    public void load() {
        for (Module module : Alien.MODULE.getModules()) {
            for (Setting setting : module.getSettings()) {
                String line = module.getName() + "_" + setting.getName();
                if (setting instanceof BooleanSetting) {
                    BooleanSetting s = (BooleanSetting)setting;
                    s.setValueWithoutTask(Alien.CONFIG.getBoolean(line, s.getDefaultValue()));
                    continue;
                }
                if (setting instanceof SliderSetting) {
                    SliderSetting sx = (SliderSetting)setting;
                    sx.setValue(Alien.CONFIG.getFloat(line, (float)sx.getDefaultValue()));
                    continue;
                }
                if (setting instanceof BindSetting) {
                    BindSetting sxx = (BindSetting)setting;
                    sxx.setValue(Alien.CONFIG.getInt(line, sxx.getDefaultValue()));
                    sxx.setHoldEnable(Alien.CONFIG.getBoolean(line + "_hold"));
                    continue;
                }
                if (setting instanceof EnumSetting) {
                    EnumSetting sxxx = (EnumSetting)setting;
                    sxxx.loadSetting(Alien.CONFIG.getString(line));
                    continue;
                }
                if (setting instanceof ColorSetting) {
                    ColorSetting sxxxx = (ColorSetting)setting;
                    sxxxx.setValue(new Color(Alien.CONFIG.getInt(line, sxxxx.getDefaultValue().getRGB()), true));
                    sxxxx.setSync(Alien.CONFIG.getBoolean(line + "Sync", sxxxx.getDefaultSync()));
                    if (!sxxxx.injectBoolean) continue;
                    sxxxx.booleanValue = Alien.CONFIG.getBoolean(line + "Boolean", sxxxx.getDefaultBooleanValue());
                    continue;
                }
                if (!(setting instanceof StringSetting)) continue;
                StringSetting sxxxxx = (StringSetting)setting;
                sxxxxx.setValue(Alien.CONFIG.getString(line, sxxxxx.getDefaultValue()));
            }
            module.setState(Alien.CONFIG.getBoolean(module.getName() + "_state", module instanceof HUD));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void save() {
        PrintWriter printwriter = null;
        try {
            printwriter = new PrintWriter(new OutputStreamWriter((OutputStream)new FileOutputStream(options), StandardCharsets.UTF_8));
            for (Module module : Alien.MODULE.getModules()) {
                for (Setting setting : module.getSettings()) {
                    String line = module.getName() + "_" + setting.getName();
                    if (setting instanceof BooleanSetting) {
                        BooleanSetting s = (BooleanSetting)setting;
                        printwriter.println(line + ":" + s.getValue());
                        continue;
                    }
                    if (setting instanceof SliderSetting) {
                        SliderSetting sx = (SliderSetting)setting;
                        printwriter.println(line + ":" + sx.getValue());
                        continue;
                    }
                    if (setting instanceof BindSetting) {
                        BindSetting sxx = (BindSetting)setting;
                        printwriter.println(line + ":" + sxx.getValue());
                        printwriter.println(line + "_hold:" + sxx.isHoldEnable());
                        continue;
                    }
                    if (setting instanceof EnumSetting) {
                        EnumSetting sxxx = (EnumSetting)setting;
                        printwriter.println(line + ":" + ((Enum)sxxx.getValue()).name());
                        continue;
                    }
                    if (setting instanceof ColorSetting) {
                        ColorSetting sxxxx = (ColorSetting)setting;
                        printwriter.println(line + ":" + sxxxx.getValue().getRGB());
                        printwriter.println(line + "Sync:" + sxxxx.sync);
                        if (!sxxxx.injectBoolean) continue;
                        printwriter.println(line + "Boolean:" + sxxxx.booleanValue);
                        continue;
                    }
                    if (!(setting instanceof StringSetting)) continue;
                    StringSetting sxxxxx = (StringSetting)setting;
                    printwriter.println(line + ":" + sxxxxx.getValue());
                }
                printwriter.println(module.getName() + "_state:" + module.isOn());
            }
            IOUtils.closeQuietly((Writer)printwriter);
        }
        catch (Exception var18) {
            var18.printStackTrace();
            System.out.println("[Alien] Failed to save settings");
        }
        finally {
            IOUtils.closeQuietly(printwriter);
        }
    }

    public void read() {
        Splitter COLON_SPLITTER = Splitter.on((char)':');
        try {
            if (!options.exists()) {
                return;
            }
            for (String s : IOUtils.readLines((InputStream)new FileInputStream(options), (Charset)StandardCharsets.UTF_8)) {
                try {
                    Iterator iterator = COLON_SPLITTER.limit(2).split((CharSequence)s).iterator();
                    this.settings.put((String)iterator.next(), (String)iterator.next());
                }
                catch (Exception var6) {
                    System.out.println("Skipping bad option: " + s);
                }
            }
        }
        catch (Exception var7) {
            var7.printStackTrace();
            System.out.println("[Alien] Failed to load settings");
        }
    }

    public int getInt(String setting, int defaultValue) {
        String s = this.settings.get(setting);
        return s != null && this.isInteger(s) ? Integer.parseInt(s) : defaultValue;
    }

    public float getFloat(String setting, float defaultValue) {
        String s = this.settings.get(setting);
        return s != null && this.isFloat(s) ? Float.parseFloat(s) : defaultValue;
    }

    public boolean getBoolean(String setting) {
        String s = this.settings.get(setting);
        return Boolean.parseBoolean(s);
    }

    public boolean getBoolean(String setting, boolean defaultValue) {
        if (this.settings.get(setting) != null) {
            String s = this.settings.get(setting);
            return Boolean.parseBoolean(s);
        }
        return defaultValue;
    }

    public String getString(String setting) {
        return this.settings.get(setting);
    }

    public String getString(String setting, String defaultValue) {
        return this.settings.get(setting) == null ? defaultValue : this.settings.get(setting);
    }

    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public boolean isFloat(String str) {
        String pattern = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";
        return str.matches(pattern);
    }
}

