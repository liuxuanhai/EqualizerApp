package io.infernodz.equalizerapp.di;
;import dagger.Component;
import io.infernodz.equalizerapp.ui.EqualizerActivity;

@Component(modules={EqualizerModule.class})
public interface AppComponent {
    void inject(EqualizerActivity equalizerActivity);
}
