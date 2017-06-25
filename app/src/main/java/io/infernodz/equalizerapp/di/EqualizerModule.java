package io.infernodz.equalizerapp.di;


import dagger.Module;
import dagger.Provides;
import io.infernodz.equalizerapp.data.EqualizerModel;
import io.infernodz.equalizerapp.data.IEqualizerModel;
import io.infernodz.equalizerapp.domain.EqualizerInteractor;
import io.infernodz.equalizerapp.domain.IEqualizerInteractor;
import io.infernodz.equalizerapp.ui.EqualizerContract;
import io.infernodz.equalizerapp.ui.EqualizerPresenter;

@Module
public class EqualizerModule {

    @Provides
    IEqualizerModel provideEqualizerModel() {
        return new EqualizerModel();
    }

    @Provides
    IEqualizerInteractor provideEqualizerInteractor(IEqualizerModel equalizerModel) {
        return new EqualizerInteractor(equalizerModel);
    }

    @Provides
    EqualizerContract.Presenter provideEqualizerPresenter(IEqualizerInteractor equalizerInteractor) {
        return new EqualizerPresenter(equalizerInteractor);
    }
}
