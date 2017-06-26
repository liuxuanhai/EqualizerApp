package io.infernodz.equalizerapp.ui;


import android.support.annotation.NonNull;

import java.util.List;

import io.infernodz.equalizerapp.data.entities.FrequencyBand;
import io.infernodz.equalizerapp.data.entities.FrequencyBandLevelModel;
import io.infernodz.equalizerapp.domain.IEqualizerInteractor;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class EqualizerPresenter implements EqualizerContract.Presenter {

    private EqualizerContract.View view;
    private IEqualizerInteractor interactor;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
    public void listenBandLevelChange(Observable<FrequencyBandLevelChangeEvent> bandEvents) {
        compositeDisposable.add(interactor.controlFrequencyBandLevel(bandEvents)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FrequencyBandLevelModel>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull FrequencyBandLevelModel frequencyBandLevelModel) {
                        int bandNumber = frequencyBandLevelModel.getBandNumber();
                        int bandLevel = frequencyBandLevelModel.getLevel();
                        view.showBandLevel(bandNumber, bandLevel);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
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

        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable.dispose();
        }
    }
}
