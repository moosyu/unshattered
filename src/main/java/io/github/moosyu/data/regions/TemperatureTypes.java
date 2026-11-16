package io.github.moosyu.data.regions;

import io.github.moosyu.util.UnshatteredUtils;

public enum TemperatureTypes {
    MAX_TEMP(74.0f),
    HIGH_TEMP(50.0f,'♨', UnshatteredUtils.RED),
    BASE_TEMP(37.0f, '~', UnshatteredUtils.WHITE),
    LOW_TEMP(0.0f, '❄', UnshatteredUtils.CYAN),
    MIN_TEMP(-37.0f);

    private final float value;
    private final char symbol;
    private final int colour;

    TemperatureTypes(float value, char symbol, int colour) {
        this.value = value;
        this.symbol = symbol;
        this.colour = colour;
    }

    TemperatureTypes(float value) {
        this.value = value;
        this.symbol = ' ';
        this.colour = 0xFFFFFFFF;
    }


    public float getValue() {
        return value;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getColour() {
        return colour;
    }
}
