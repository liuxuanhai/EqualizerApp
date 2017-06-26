package io.infernodz.equalizerapp.data.entities;

public class FrequencyBandLevelModel {

    private int bandNumber;
    private int level;

    public FrequencyBandLevelModel(int bandNumber, int level) {
        this.bandNumber = bandNumber;
        this.level = level;
    }

    public int getBandNumber() {
        return bandNumber;
    }

    public int getLevel() {
        return level;
    }
}
