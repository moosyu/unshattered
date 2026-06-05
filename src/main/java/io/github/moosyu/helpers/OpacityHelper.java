package io.github.moosyu.helpers;

public final class OpacityHelper {
    public static int getOpacityColor(int color, float opacity) {
        return ((int)(opacity * 255) << 24) | (color & 0xFFFFFF);
    }
}
