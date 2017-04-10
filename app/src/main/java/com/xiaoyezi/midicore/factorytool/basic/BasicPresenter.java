package com.xiaoyezi.midicore.factorytool.basic;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xiaoyezi.midicore.factorytool.data.MiDiDataRepository;

import midicore.MidiDeviceEventListener;
import midicore.MidiDeviceManager;

/**
 * Created by jim on 2017/4/10.
 */
public class BasicPresenter implements BasicContract.Presenter {
    private BasicContract.View mBasicView;

    private MiDiDataRepository mMiDiDataRepository;

    public BasicPresenter(BasicContract.View view, MiDiDataRepository dataRepository) {
        mBasicView = view;
        mMiDiDataRepository = dataRepository;
    }

    @Override
    public void start(@NonNull final Context context) {
        MidiDeviceManager.getInstance().init(context.getApplicationContext());
        MidiDeviceManager.getInstance().setMidiDevEventListener(new TestMidiEventListener());
        MidiDeviceManager.getInstance().open();
    }

    @Override
    public void stop() {
        MidiDeviceManager.getInstance().close();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void sendMidiEvent(int type, int data1, int data2) {
        switch (type) {
            case MIDI_COMMAND_CONNECT:
                byte cmd[] = {(byte)0xf0, 0, 0x20, 0x2b, 0x69, 0, 0, 0x55, 0x79, (byte)0xf7};
                MidiDeviceManager.getInstance().sendMsg(cmd);
                break;
            case MIDI_COMMAND_QUERY_INFO:
                MidiDeviceManager.getInstance().sendMsgEx(true, 0x1, 0, 0);
                break;
            case MIDI_COMMAND_TURN_ON_ALL:
                MidiDeviceManager.getInstance().sendMsgEx(true, 0x16, 0x2, data1);
                break;
            case MIDI_COMMAND_TURN_OFF_ALL:
                MidiDeviceManager.getInstance().sendMsgEx(true, 0x16, 0x2, 0);
                break;
            case MIDI_COMMAND_TURN_ON:
                byte cmdOn[] = {(byte)0xa2, (byte)pitch2Light(data1), (byte)data2};
                MidiDeviceManager.getInstance().sendMsg(cmdOn);
                break;
            case MIDI_COMMAND_TURN_OFF:
                MidiDeviceManager.getInstance().sendMsgEx(false, 0xa2, (byte)pitch2Light(data1), 0);
                break;
            case MIDI_COMMAND_KEY_PRESS:
                MidiDeviceManager.getInstance().sendMsgEx(false, 0x90, (byte)data1, 75);
                break;
        }
    }

    /**
     * Convert pitch to led light number
     *
     * @param pitch
     * @return
     */
    private int pitch2Light(int pitch) {
        return pitch - 21;
    }

    private final class TestMidiEventListener implements MidiDeviceEventListener {
        public void onAttached() {
            mBasicView.setDeviceConnectionState(true);
        }

        public void onDetached() {
            mBasicView.setDeviceConnectionState(false);
        }

        public void onMidiData(final byte[] data) {
            mBasicView.onMidiData(data);
        }
    }
}
