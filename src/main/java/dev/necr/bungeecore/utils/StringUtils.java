package dev.necr.bungeecore.utils;

import dev.necr.bungeecore.callbacks.IsDoubleCallback;
import dev.necr.bungeecore.callbacks.IsIntegerCallback;
import dev.necr.bungeecore.enums.TrueFalseType;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
@SuppressWarnings("unused")
public class StringUtils {

    /**
     * Colorizes string
     *
     * @param text the string to colorize
     * @return colorized string
     */
    public static String colorize(String text) {
        return text == null ? " " : ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String[] colorize(String[] lines) {
        return colorize(Arrays.asList(lines.clone())).toArray(new String[0]);
    }

    public static List<String> colorize(List<String> s) {
        List<String> colorized = new ArrayList<>();
        s.forEach((st) -> colorized.add(colorize(st)));
        return colorized;
    }

    /**
     * Colorizes a string from {@link TrueFalseType} to green if true and to red if false
     *
     * @param state         the state
     * @param trueFalseType see {@link TrueFalseType}
     * @return a green {@link TrueFalseType#getIfTrue()} if true and a red {@link TrueFalseType#getIfFalse()} if false
     */
    public String colorizeTrueFalse(boolean state, TrueFalseType trueFalseType) {
        if (state) {
            return "&a" + trueFalseType.getIfTrue();
        }
        return "&c" + trueFalseType.getIfFalse();
    }

    /**
     * Colorizes a string from {@link TrueFalseType} to green if true and to red if false
     *
     * @param state         the state
     * @param trueFalseType see {@link TrueFalseType}
     * @return a green {@link TrueFalseType#getIfTrue()} if true and a red {@link TrueFalseType#getIfFalse()} if false
     */
    public String colorizeBoldTrueFalse(boolean state, TrueFalseType trueFalseType) {
        if (state) {
            return "&a&l" + trueFalseType.getIfTrue();
        }
        return "&c&l" + trueFalseType.getIfFalse();
    }

    /**
     * Strips the given message of all color codes
     *
     * @param text String to strip of color
     * @return colorized string
     */
    public static String strip(String text) {
        return text == null ? " " : ChatColor.stripColor(text);
    }

    /**
     * Uppercase the first letter of the given string
     *
     * @param text String to strip of color
     * @return colorized string
     */
    public static String toUppercaseFirstChar(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    /**
     * Checks if a string is parsable to an Integer
     *
     * @param s the string
     * @return {@link IsIntegerCallback}
     */
    public IsIntegerCallback isInteger(String s) {
        IsIntegerCallback callback = new IsIntegerCallback(false, 0);
        if (s == null) {
            return callback;
        }

        try {
            return callback.setInteger(true).setValue(Integer.parseInt(s));
        } catch (Exception ignored) {
            return callback;
        }
    }

    /**
     * Checks if a string is parsable to a double
     *
     * @param s the string
     * @return {@link IsDoubleCallback}
     */
    public IsDoubleCallback isDouble(String s) {
        IsDoubleCallback callback = new IsDoubleCallback(false, 0.0);
        if (s == null) {
            return callback;
        }

        try {
            return callback.setDouble(true).setValue(Double.parseDouble(s));
        } catch (Exception ignored) {
            return callback;
        }
    }
}
