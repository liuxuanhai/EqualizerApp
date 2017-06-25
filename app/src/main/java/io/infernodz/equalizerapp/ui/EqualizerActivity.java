package io.infernodz.equalizerapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.List;

import javax.inject.Inject;

import io.infernodz.equalizerapp.EqualizerApplication;
import io.infernodz.equalizerapp.R;
import io.infernodz.equalizerapp.data.entities.FrequencyBand;


public class EqualizerActivity extends AppCompatActivity
        implements EqualizerContract.View, AudioRendererEventListener {

    /* Constants */

    private static final int DEFAULT_PRIORITY = 100;
    private static final int MIN_SEEK_BAR_VALUE = 0;
    private static final int HERTZ_IN_KILOHERTZ = 1000;

    private static final String KILOHERTZ_SYMBOL = "kHz";
    private static final String HERTZ_SYMBOL = "Hz";
    private static final String DECIBEL_SYMBOL = "dB";
    private static final String PLUS_SYMBOL = "+";

    /* Fields */

    // Смещение значений прогресса SeekBar отночительно значений уровня FrequencyBand
    private int seekBarValueOffset;

    private SimpleExoPlayer player;
    private SimpleExoPlayerView playerView;
    private LinearLayout equalizerBandsWrapper;

    @Inject EqualizerContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        playerView = (SimpleExoPlayerView) findViewById(R.id.player);
        equalizerBandsWrapper = (LinearLayout) findViewById(R.id.bands_wrapper);

        //presenter = new EqualizerPresenter(new EqualizerInteractor(new EqualizerModel()));
        EqualizerApplication.getAppComponent().inject(this);
        presenter.bindView(this);
        presenter.initializePlayer();
    }

    // ExoPlayer section

    @Override
    public void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(true);
        player.setAudioDebugListener(this);

        MediaSource mediaSource = buildMediaSource(Uri.parse(getString(R.string.media_url)));
        player.prepare(mediaSource, true, false);
    }

    @Override
    public void releasePlayer() {
        player.stop();
        player.release();
        player = null;
    }

    // Audio renderer listener

    @Override
    public void onAudioSessionId(int audioSessionId) {
        presenter.initializeEqualizer(DEFAULT_PRIORITY, audioSessionId);
        presenter.createEqualizerControls();
    }

    @Override
    public void onAudioEnabled(DecoderCounters counters) {

    }

    @Override
    public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onAudioInputFormatChanged(Format format) {

    }

    @Override
    public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

    }

    @Override
    public void onAudioDisabled(DecoderCounters counters) {

    }

    @Override
    protected void onDestroy() {
        presenter.onStop();
        super.onDestroy();
    }

    @Override
    public void initializeFrequencyBandsUI(List<FrequencyBand> bands) {
        LayoutInflater inflater = getLayoutInflater();
        for (FrequencyBand frequencyBand : bands) {
            View bandRow = inflater.inflate(R.layout.frequency_band_item_layout, equalizerBandsWrapper, false);

            TextView bandFrequency = (TextView) bandRow.findViewById(R.id.band_frequency);
            TextView bandMinLevel = (TextView) bandRow.findViewById(R.id.band_min_level);
            TextView bandMaxLevel = (TextView) bandRow.findViewById(R.id.band_max_level);
            SeekBar bandLevelController = (SeekBar) bandRow.findViewById(R.id.band_level_controller);

            bandFrequency.setText(convertFrequencyToString(frequencyBand.getFrequency()));
            bandMinLevel.setText(convertBandLevelToString(frequencyBand.getMinLevel()));
            bandMaxLevel.setText(convertBandLevelToString(frequencyBand.getMaxLevel()));

            // Соотносим значения band с seek bar
            seekBarValueOffset = MIN_SEEK_BAR_VALUE - frequencyBand.getMinLevel();
            int maxSeekBarValue = getValueWithOffset(frequencyBand.getMaxLevel(), seekBarValueOffset);
            int seekBarProgressValue = getValueWithOffset(frequencyBand.getLevel(), seekBarValueOffset);
            bandLevelController.setMax(maxSeekBarValue);
            bandLevelController.setProgress(seekBarProgressValue);

            final int bandNumber = frequencyBand.getBandNumber();

            bandLevelController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    presenter.onFrequencyBandLevelChange(bandNumber,
                            getValueWithOffset(seekBar.getProgress(), -seekBarValueOffset));
                }
            });
            equalizerBandsWrapper.addView(bandRow);
        }
    }

    /* Private helper methods */

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    /* Смещает значение на величину offset */
    private int getValueWithOffset(int value, int offset) {
        return value + offset;
    }

    private String convertFrequencyToString(int frequency) {
        if(frequency > HERTZ_IN_KILOHERTZ) {
            frequency /= HERTZ_IN_KILOHERTZ;
            return String.valueOf(frequency) + KILOHERTZ_SYMBOL;
        } else {
            return String.valueOf(frequency) + HERTZ_SYMBOL;
        }
    }

    private String convertBandLevelToString(int bandLevel) {
        String formatedBandLevel = String.valueOf(bandLevel) +  DECIBEL_SYMBOL;
        if (bandLevel >= 0) {
            formatedBandLevel = PLUS_SYMBOL + formatedBandLevel;
        }
        return formatedBandLevel;
    }
}
