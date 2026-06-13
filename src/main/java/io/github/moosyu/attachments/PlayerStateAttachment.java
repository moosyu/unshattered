package io.github.moosyu.attachments;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

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

    public double[] getStats() {return stats;}

    public void setStats(double[] newStats) {System.arraycopy(newStats, 0, stats, 0, stats.length);}

    public double getCurrentStat(Stat currentStat) {return stats[currentStat.ordinal()];}

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

    public int getLastUpdatedStat() {return lastUpdatedStat;}

    public double getCurrentStatByIndex(int index) {return stats[index];}

    public void setCurrentStatByIndex(int index, double value) {stats[index] = value;}

    public boolean isKnockbackCancelled() {return cancelKnockback;}

    public void setCancelledKnockback(boolean cancelKnockback) {this.cancelKnockback = cancelKnockback;}

    public boolean isInvulnerable() {return invulnerableTime > 0;}

    public void decrementInvulnerableTime() {invulnerableTime = Math.max(0, invulnerableTime - 1);}

    public void setInvulnerableTime(int invulnerable) {this.invulnerableTime = invulnerable;}

    public void writeSync(RegistryFriendlyByteBuf buf, boolean initialSync) {
        boolean fullSync = initialSync || lastUpdatedStat < 0;
        buf.writeBoolean(fullSync);
        if (fullSync) {
            buf.writeInt(stats.length);
            for (double stat : stats) {
                buf.writeDouble(stat);
            }
        } else {
            buf.writeInt(lastUpdatedStat);
            buf.writeDouble(stats[lastUpdatedStat]);
        }
    }

    public static PlayerStateAttachment readSync(RegistryFriendlyByteBuf buf, @Nullable PlayerStateAttachment existing) {
        boolean fullSync = buf.readBoolean();
        PlayerStateAttachment attachment = existing != null ? existing : new PlayerStateAttachment();
        if (fullSync) {
            int length = buf.readInt();
            double[] stats = new double[length];

            for (int i = 0; i < length; i++) {
                stats[i] = buf.readDouble();
            }
            attachment.setStats(stats);
        } else {
            int statIndex = buf.readInt();
            double value = buf.readDouble();

            attachment.setCurrentStatByIndex(statIndex, value);
        }
        return attachment;
    }
}