package io.github.moosyu.attachments;

public class PlayerStatsAttachment {
    private final double[] stats = new double[Stat.values().length];
    public enum Stat {
        HEALTH,
        MANA
    }

    public PlayerStatsAttachment() {
        stats[Stat.HEALTH.ordinal()] = 0.0f;
        stats[Stat.MANA.ordinal()] = 0.0f;
    }

    public double getCurrentStat(Stat currentStat) {
        return stats[currentStat.ordinal()];
    }

    public void setCurrentStat(Stat currentStat, double value) {
        stats[currentStat.ordinal()] = value;
    }

    public void removeCurrentStat(Stat currentStat, double amount) {
        stats[currentStat.ordinal()] -= amount;

    }

    public void addCurrentStat(Stat currentStat, double amount, double maxAmount) {
        // the Math.min should return the smaller of the two (so the value doesnt overflow max health
        // very smart but very dangerous
        stats[currentStat.ordinal()] = (float) Math.min(stats[currentStat.ordinal()] + amount, maxAmount);
    }
}