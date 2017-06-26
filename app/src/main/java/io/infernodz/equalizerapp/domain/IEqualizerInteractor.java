package io.infernodz.equalizerapp.domain;


import java.util.List;

import io.infernodz.equalizerapp.data.entities.FrequencyBand;
import io.infernodz.equalizerapp.data.entities.FrequencyBandLevelModel;
import io.infernodz.equalizerapp.ui.FrequencyBandLevelChangeEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;

public interface IEqualizerInteractor {

    /**
     * Gets frequency bands from equalizer model.
     */
    Single<List<FrequencyBand>> getFrequencyBands();

    /**
     * Sets the given equalizer band to the given gain value and returns this new value.
     */
    Observable<FrequencyBandLevelModel> controlFrequencyBandLevel(
            Observable<FrequencyBandLevelChangeEvent> bandEvents);

    /**
     * Initialize a new equalizer, attach it to audioSession and turns it on.
     */
    void initializeEqualizer(int priority, int audioSessionId);

    /**
     * Turns the equaliser off and releases its resourses.
     */
    void releaseEqualizer();
}
