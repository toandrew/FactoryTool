package com.xiaoyezi.midicore.factorytool.basic;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xiaoyezi.midicore.factorytool.data.MiDiDataRepository;
import com.xiaoyezi.midicore.factorytool.data.MiDiDataSource;

/**
 * Created by jim on 2017/4/10.
 */
public class BasicPresenter implements BasicContract.Presenter {
    private BasicContract.View mBasicView;

    private MiDiDataRepository mMiDiDataRepository;

    BasicPresenter.TestMidiEventListener mMidiListener = new BasicPresenter.TestMidiEventListener();

    public BasicPresenter(BasicContract.View view, MiDiDataRepository dataRepository) {
        mBasicView = view;
        mMiDiDataRepository = dataRepository;
    }

    @Override
    public void start(@NonNull final Context context) {
        mMiDiDataRepository.setMidiDevEventListener(mMidiListener);
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
            mBasicView.setDeviceConnectionState(true);
        }

        @Override
        public void onDetached() {
            mBasicView.setDeviceConnectionState(false);
        }

        @Override
        public void onMidiData(final byte[] data) {
            mBasicView.onMidiData(data);
        }
    }
}

