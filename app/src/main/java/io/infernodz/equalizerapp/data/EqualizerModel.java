package io.infernodz.equalizerapp.data;


import android.media.audiofx.Equalizer;

/**
 * Реализация модели управления эквалайзером
 */
public class EqualizerModel implements IEqualizerModel {

    private static final int MILIHERTZ_IN_HERTZ = 1000;
    private static final int MILBELS_IN_DECIBEL = 100;

    private Equalizer equalizer;

    @Override
    public void initialize(int priority, int sessionId) {
        equalizer = new Equalizer(priority, sessionId);
        equalizer.setEnabled(true);
    }

    @Override
    public void release() {
        // Освобождаем ресурсы и ссылку. Иначе nullpointer при перезапуске
        equalizer.release();
        equalizer = null;
    }

    @Override
    public void setBandLevel(int band, int level) {
        equalizer.setBandLevel((short) band, (short) (level * MILBELS_IN_DECIBEL));
    }

    @Override
    public int getBandLevel(int band) {
        return equalizer.getBandLevel((short) band) / MILBELS_IN_DECIBEL;
    }

    @Override
    public int getBandCenterFrequency(int band) {
        return equalizer.getCenterFreq((short) band) / MILIHERTZ_IN_HERTZ;
    }

    @Override
    public int getNumberOfBands() {
        return (int) equalizer.getNumberOfBands();
    }

    @Override
    public int getMinBandLevel() {
        return (int) equalizer.getBandLevelRange()[0] / MILBELS_IN_DECIBEL;
    }

    @Override
    public int getMaxBandLevel() {
        return (int) equalizer.getBandLevelRange()[1] / MILBELS_IN_DECIBEL;
    }

}
