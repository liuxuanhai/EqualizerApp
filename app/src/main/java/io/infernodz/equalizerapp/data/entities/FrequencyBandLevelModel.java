package io.infernodz.equalizerapp.data.entities;

public class FrequencyBandLevelModel {

    private int bandNumber;
    private int currentBandLevel;

    public FrequencyBandLevelModel(int bandNumber, int currentBandLevel) {
        this.bandNumber = bandNumber;
        this.currentBandLevel = currentBandLevel;
    }

    public int getBandNumber() {
        return bandNumber;
    }

    public int getCurrentBandLevel() {
        return currentBandLevel;
    }
}
