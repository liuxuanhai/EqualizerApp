package io.infernodz.equalizerapp.domain;

import java.util.ArrayList;
import java.util.List;

import io.infernodz.equalizerapp.data.entities.FrequencyBand;
import io.infernodz.equalizerapp.data.entities.FrequencyBandLevelModel;
import io.infernodz.equalizerapp.data.IEqualizerModel;
import io.infernodz.equalizerapp.ui.FrequencyBandLevelChangeEvent;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

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
    public Observable<FrequencyBandLevelModel> controlFrequencyBandLevel(Observable<FrequencyBandLevelChangeEvent> bandEvents) {
        return bandEvents
                .flatMap(new Function<FrequencyBandLevelChangeEvent, ObservableSource<FrequencyBandLevelModel>>() {
                    @Override
                    public ObservableSource<FrequencyBandLevelModel> apply(@NonNull final FrequencyBandLevelChangeEvent frequencyBandLevelChangeEvent) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<FrequencyBandLevelModel>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<FrequencyBandLevelModel> e) throws Exception {
                                int bandNumber = frequencyBandLevelChangeEvent.getBandNumber();
                                int bandLevel = frequencyBandLevelChangeEvent.getUpdatedBandLevel();

                                equalizerModel.setBandLevel(bandNumber, bandLevel);

                                int currentBandLevel = equalizerModel.getBandLevel(bandNumber);
                                FrequencyBandLevelModel band =  new FrequencyBandLevelModel(bandNumber, currentBandLevel);

                                e.onNext(band);
                            }
                        });
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
