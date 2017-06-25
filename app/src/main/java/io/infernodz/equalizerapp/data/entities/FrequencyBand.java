package io.infernodz.equalizerapp.data.entities;

public class FrequencyBand {

    private int bandNumber;
    private int level;
    private int frequency;
    private int minLevel;
    private int maxLevel;

    public FrequencyBand(int bandNumber, int level, int frequency, int minLevel, int maxLevel) {
        this.bandNumber = bandNumber;
        this.level = level;
        this.frequency = frequency;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public int getBandNumber() { return bandNumber;
    }

    public int getLevel() {
        return level;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getMaxLevel() { return maxLevel;
    }

    public int getMinLevel() {
        return minLevel;
    }
}
