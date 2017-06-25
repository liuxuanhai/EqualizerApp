package io.infernodz.equalizerapp.ui;


import android.support.annotation.NonNull;

import java.util.List;

import io.infernodz.equalizerapp.data.entities.FrequencyBand;
import io.infernodz.equalizerapp.data.entities.FrequencyBandLevelModel;
import io.infernodz.equalizerapp.domain.IEqualizerInteractor;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EqualizerPresenter implements EqualizerContract.Presenter {

    private EqualizerContract.View view;
    private IEqualizerInteractor interactor;

    public EqualizerPresenter(@NonNull IEqualizerInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void initializePlayer() {
        view.initializePlayer();
    }

    @Override
    public void releasePlayer() {
        view.releasePlayer();
    }

    @Override
    public void initializeEqualizer(int priority, int audioSessionId) {
        interactor.initializeEqualizer(priority, audioSessionId);
    }

    @Override
    public void releaseEqualizer() {
        interactor.releaseEqualizer();
    }

    @Override
    public void createEqualizerControls() {
        interactor.getFrequencyBands()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<FrequencyBand>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<FrequencyBand> frequencyBands) {
                        view.initializeFrequencyBandsUI(frequencyBands);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void onFrequencyBandLevelChange(int bandNumber, int level) {
        interactor.updateFrequencyBandLevel(bandNumber, level)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<FrequencyBandLevelModel>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull FrequencyBandLevelModel frequencyBandLevelModel) {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void bindView(EqualizerContract.View view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    @Override
    public void onStop() {
        this.releasePlayer();
        this.releaseEqualizer();
    }
}
