package io.infernodz.equalizerapp.ui;

public class FrequencyBandLevelChangeEvent {

    private int bandNumber;
    private int updatedBandLevel;

    public FrequencyBandLevelChangeEvent(int bandNumber, int updatedBandLevel) {
        this.bandNumber = bandNumber;
        this.updatedBandLevel = updatedBandLevel;
    }

    public int getBandNumber() {
        return bandNumber;
    }

    public int getUpdatedBandLevel() {
        return updatedBandLevel;
    }
}
