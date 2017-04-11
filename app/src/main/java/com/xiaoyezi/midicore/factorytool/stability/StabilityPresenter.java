package com.xiaoyezi.midicore.factorytool.stability;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xiaoyezi.midicore.factorytool.data.MiDiDataRepository;
import com.xiaoyezi.midicore.factorytool.data.MiDiDataSource;

/**
 * Created by jim on 2017/4/10.
 */
public class StabilityPresenter implements StabilityContract.Presenter {
    private StabilityFragment mStabilityFragment;

    private MiDiDataRepository mMiDiDataRepository;

    public StabilityPresenter(StabilityFragment fragment, MiDiDataRepository dataRepository) {
        mStabilityFragment = fragment;
        mMiDiDataRepository = dataRepository;
    }

    @Override
    public void start(@NonNull final Context context) {
        mMiDiDataRepository.setMidiDevEventListener(new StabilityPresenter.TestMidiEventListener());
        mMiDiDataRepository.startDevice();
    }

    @Override
    public void stop() {
        mMiDiDataRepository.stopDevice();
    }

    @Override
    public void resume() {
        mMiDiDataRepository.resumeDevice();
    }

    @Override
    public void pause() {
        mMiDiDataRepository.pauseDevice();
    }

    @Override
    public void sendMidiEvent(int type, int data1, int data2) {
        mMiDiDataRepository.sendMidiEvent(type, data1, data2);
    }

    /**
     * Midi event listener
     */
    private final class TestMidiEventListener implements MiDiDataSource.EventListener {
        @Override
        public void onAttached() {
            mStabilityFragment.setDeviceConnectionState(true);
        }

        @Override
        public void onDetached() {
            mStabilityFragment.setDeviceConnectionState(false);
        }

        @Override
        public void onMidiData(final byte[] data) {
            mStabilityFragment.onMidiData(data);
        }
    }

    /**
     * Start/Stop test
     *
     * @param config
     */
    @Override
    public void startStopText(TestConfig config) {
    }


    /**
     * config data
     */
    public static class StabilityTestConfig extends TestConfig {
    }
}
