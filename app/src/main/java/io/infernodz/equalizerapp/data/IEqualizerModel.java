package io.infernodz.equalizerapp.data;

/**
 * Main entry point for accessing equalizer
 */
public interface IEqualizerModel {

    /**
     * Initialize a new equalizer, attach it to audioSession and turns it on.
     */
    void initialize(int priority, int sessionId);

    /**
     * Turns the equaliser off and releases its resourses.
     */
    void release();

    /**
     * Sets the given equalizer bands to the given gain value.
     * @param band frequency band that will have new gain.
     *             The numbering starts from 0 and ends at (number of bands - 1).
     * @param level new gain in decibels. getMinBandLevel() and getMaxBandLevel() will define
     *              the minimum and maximum numbers.
     */
    void setBandLevel(int band, int level);

    /**
     * Get the gain to the given equalizer band.
     * @param band frequencu band whose gain is required.
     *             The numbering starts from 0 and ends at (number of bands - 1).
     * @return the gain in decibels of the given band.
     */
    int getBandLevel(int band);

    /**
     * Get the gain to the given equalizer band.
     * @param band frequencu band whose gain is required.
     *             The numbering starts from 0 and ends at (number of bands - 1).
     * @return the center frequency in hertz
     */
    int getBandCenterFrequency(int band);

    /**
     * Gets the number of bands supported the equalizer model
     */
    int getNumberOfBands();

    /**
     * Gets the minimum gain level in decibels
     */
    int getMinBandLevel();

    /**
     * Gets the maximum gain level in decibels
     */
    int getMaxBandLevel();

}
