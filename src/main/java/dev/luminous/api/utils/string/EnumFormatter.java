/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Direction
 *  net.minecraft.util.math.Direction$Axis
 */
package dev.luminous.api.utils.string;

import net.minecraft.util.math.Direction;

public class EnumFormatter {
    public static String formatEnum(Enum<?> in) {
        String name = in.name();
        if (!name.contains("_")) {
            char firstChar = name.charAt(0);
            String suffixChars = name.split(String.valueOf(firstChar), 2)[1];
            return String.valueOf(firstChar).toUpperCase() + suffixChars.toLowerCase();
        }
        String[] names = name.split("_");
        StringBuilder nameToReturn = new StringBuilder();
        for (String n : names) {
            char firstChar = n.charAt(0);
            String suffixChars = n.split(String.valueOf(firstChar), 2)[1];
            nameToReturn.append(String.valueOf(firstChar).toUpperCase()).append(suffixChars.toLowerCase());
        }
        return nameToReturn.toString();
    }

    public static String formatDirection(class_2350 direction) {
        return switch (direction) {
            default -> throw new MatchException(null, null);
            case class_2350.field_11036 -> "Up";
            case class_2350.field_11033 -> "Down";
            case class_2350.field_11043 -> "North";
            case class_2350.field_11035 -> "South";
            case class_2350.field_11034 -> "East";
            case class_2350.field_11039 -> "West";
        };
    }

    public static String formatAxis(class_2350.class_2351 axis) {
        return switch (axis) {
            default -> throw new MatchException(null, null);
            case class_2350.class_2351.field_11048 -> "X";
            case class_2350.class_2351.field_11052 -> "Y";
            case class_2350.class_2351.field_11051 -> "Z";
        };
    }
}

