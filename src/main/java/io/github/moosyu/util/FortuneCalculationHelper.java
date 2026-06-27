package io.github.moosyu.util;

import java.util.Random;

public class FortuneCalculationHelper {

    /**
     *
     * @param fortuneAmount the fortune of whatever type is being used
     * @param baseDropAmount base drop amount dropped when you break the block (usually itll be one but you never know)
     * @return amount of whatever the player should be given
     */
    public static int getItemsCount(double fortuneAmount, int baseDropAmount) {
        double prevHundred = (Math.floor(fortuneAmount / 100.0));
        int guaranteedDrops = (int) prevHundred + baseDropAmount;
        double nextHundredDiff = fortuneAmount - (prevHundred * 100);

        if (new Random().nextDouble(100.0d) < nextHundredDiff) {
            return guaranteedDrops + 1;
        } else {
            return guaranteedDrops;
        }
    }
}
