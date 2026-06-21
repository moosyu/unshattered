package io.github.moosyu.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHelpers {
    public static Component parseStyledText(String input, int baseColor) {
        MutableComponent result = Component.empty();
        Matcher matcher = Pattern.compile("\\[color=(0x[0-9A-Fa-f]+)](.*?)\\[/color]|\\[i](.*?)\\[/i]").matcher(input);
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                String before = input.substring(lastEnd, matcher.start());
                result.append(Component.literal(before).withColor(baseColor));
            }
            if (matcher.group(1) != null) {
                String colorHex = matcher.group(1);
                String text = matcher.group(2);
                int color = (int) Long.parseLong(colorHex.substring(2), 16);

                result.append(Component.literal(text).withColor(color));
            } else if (matcher.group(3) != null) {
                String text = matcher.group(3);
                result.append(Component.literal(text).withColor(baseColor).withStyle(ChatFormatting.ITALIC));
            }
            lastEnd = matcher.end();
        }

        if (lastEnd < input.length()) {
            String remaining = input.substring(lastEnd);
            result.append(Component.literal(remaining).withColor(baseColor));
        }

        return result;
    }

    // https://www.geeksforgeeks.org/dsa/converting-decimal-number-lying-between-1-to-3999-to-roman-numerals/ 💋
    public static String convertTextToRomanNumeral(int x) {
        int[] base = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
        String[] sym = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};

        // to store result
        StringBuilder res = new StringBuilder();

        // Loop from the right side to find
        // the largest smaller base value
        int i = base.length - 1;
        while (x > 0) {
            int div = x / base[i];
            while (div > 0) {
                res.append(sym[i]);
                div--;
            }

            // Repeat the process for remainder
            x = x % base[i];
            i--;
        }

        return res.toString();
    }
}
