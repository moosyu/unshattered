package io.github.moosyu.helpers;

// https://www.geeksforgeeks.org/dsa/converting-decimal-number-lying-between-1-to-3999-to-roman-numerals/ 💋
public final class RomanNumeralHelper {
    public static String toRoman(int x) {
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
