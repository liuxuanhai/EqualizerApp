package io.infernodz.equalizerapp.ui;


import java.util.List;

import io.infernodz.equalizerapp.data.entities.FrequencyBand;
import io.infernodz.equalizerapp.data.entities.FrequencyBandLevelModel;
import io.reactivex.Observable;

public interface EqualizerContract {

    interface Presenter {

        /**
         * Initialize the player and turns it on.
         */
        void initializePlayer();

        /**
         * Turns the player off and releases its resourses.
         */
        void releasePlayer();

        /**
         * Initialize a new equalizer, attach it to audioSession and turns it on.
         */
        void initializeEqualizer(int priority, int audioSessionId);

        /**
         * Turns the equaliser off and releases its resourses.
         */
        void releaseEqualizer();

        /**
         * Creates controls for managing equalizer.
         */
        void createEqualizerControls();

        /**
         * Add a data source from frequency band change level events and
         * subscribe to its changes.
         */
        void listenBandLevelChange(Observable<FrequencyBandLevelChangeEvent> bandEvents);

        void bindView(View view);

        void unbindView();

        void onStop();

    }

    interface View {

        void initializePlayer();

        void releasePlayer();

        void initializeFrequencyBandsUI(List<FrequencyBand> bands);

        void showBandLevel(int bandNumber, int bandLevel);
    }
}
