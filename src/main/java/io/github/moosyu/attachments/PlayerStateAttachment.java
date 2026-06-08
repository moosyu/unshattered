package io.github.moosyu.attachments;

import net.minecraft.world.item.ItemStack;

public class PlayerStateAttachment {
    private final double[] stats = new double[Stat.values().length];
    private boolean cancelKnockback = false;
    private int invulnerableTime = 0;
    private int lastUpdatedStat = -1;

    public enum Stat {
        HEALTH,
        MANA
    }

    public PlayerStateAttachment() {
        stats[Stat.HEALTH.ordinal()] = 0.0f;
        stats[Stat.MANA.ordinal()] = 0.0f;
    }

    public double[] getStats() {
        return stats;
    }

    public void setStats(double[] newStats) {
        System.arraycopy(newStats, 0, stats, 0, stats.length);
    }

    public double getCurrentStat(Stat currentStat) {
        return stats[currentStat.ordinal()];
    }

    public void setCurrentStat(Stat stat, double value) {
        int index = stat.ordinal();
        stats[index] = value;
        lastUpdatedStat = index;
    }

    public void removeCurrentStat(Stat currentStat, double amount) {stats[currentStat.ordinal()] -= amount;}

    public void addCurrentStat(Stat currentStat, double amount, double maxAmount) {
        // the Math.min should return the smaller of the two (so the value doesnt overflow max). very smart but very dangerous.
        stats[currentStat.ordinal()] = (float) Math.min(stats[currentStat.ordinal()] + amount, maxAmount);
    }

    public int getLastUpdatedStat() {
        return lastUpdatedStat;
    }

    public double getCurrentStatByIndex(int index) {
        return stats[index];
    }

    public void setCurrentStatByIndex(int index, double value) {
        stats[index] = value;
    }

    public boolean isKnockbackCancelled() {
        return cancelKnockback;
    }

    public void setCancelledKnockback(boolean cancelKnockback) {
        this.cancelKnockback = cancelKnockback;
    }

    public boolean isInvulnerable() {return invulnerableTime > 0;}

    public void decrementInvulnerableTime() {invulnerableTime = Math.max(0, invulnerableTime - 1);}

    public void setInvulnerableTime(int invulnerable) {this.invulnerableTime = invulnerable;}
}