package io.github.moosyu.data.regions;

public enum TemperatureTypes {
    MAX_TEMP(74.0f),
    HIGH_TEMP(50.0f,'♨', 0xFFFF5555),
    BASE_TEMP(37.0f, '~', 0xFFFFFF),
    LOW_TEMP(0.0f, '❄', 0xFF55FFFF),
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
