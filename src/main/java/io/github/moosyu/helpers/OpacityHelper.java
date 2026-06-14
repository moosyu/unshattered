package io.github.moosyu.helpers;

public final class OpacityHelper {
    // https://stackoverflow.com/a/28483738 my goodness
    public static int getOpacityColor(int color, float opacity) {
        return ((int)(opacity * 255) << 24) | (color & 0xFFFFFF);
    }
}
