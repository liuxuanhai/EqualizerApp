package io.infernodz.equalizerapp.domain;

import java.util.ArrayList;
import java.util.List;

import io.infernodz.equalizerapp.data.entities.FrequencyBand;
import io.infernodz.equalizerapp.data.entities.FrequencyBandLevelModel;
import io.infernodz.equalizerapp.data.IEqualizerModel;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class EqualizerInteractor implements IEqualizerInteractor {

    private IEqualizerModel equalizerModel;

    public EqualizerInteractor(@NonNull IEqualizerModel equalizerModel) {
        this.equalizerModel = equalizerModel;
    }

    @Override
    public Single<List<FrequencyBand>> getFrequencyBands() {
        return Single.create(new SingleOnSubscribe<List<FrequencyBand>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<FrequencyBand>> e) throws Exception {
                int numBands = equalizerModel.getNumberOfBands();
                List<FrequencyBand> bands = new ArrayList<>();
                for(int i = 0; i < numBands; i++) {
                    int bandNumber = i;
                    int bandLevel = equalizerModel.getBandLevel((short) bandNumber);
                    int frequency = equalizerModel.getBandCenterFrequency(bandNumber);
                    int minLevel = equalizerModel.getMinBandLevel();
                    int maxLevel = equalizerModel.getMaxBandLevel();
                    FrequencyBand band = new FrequencyBand(bandNumber, bandLevel, frequency, minLevel, maxLevel);
                    bands.add(band);
                }
                e.onSuccess(bands);
            }
        });
    }

    @Override
    public Single<FrequencyBandLevelModel> updateFrequencyBandLevel(final int bandNumber, final int level) {
        return Single.create(new SingleOnSubscribe<FrequencyBandLevelModel>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<FrequencyBandLevelModel> e) throws Exception {
                equalizerModel.setBandLevel(bandNumber, level);
                int currentBandLevel = equalizerModel.getBandLevel(bandNumber);
                FrequencyBandLevelModel band =  new FrequencyBandLevelModel(bandNumber, currentBandLevel);
                e.onSuccess(band);
            }
        });
    }

    @Override
    public void initializeEqualizer(int priority, int audioSessionId) {
        equalizerModel.initialize(priority, audioSessionId);
    }

    @Override
    public void releaseEqualizer() {
        equalizerModel.release();
    }
}
