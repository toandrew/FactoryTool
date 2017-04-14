package com.xiaoyezi.midicore.factorytool.data;

import android.content.Context;
import android.support.annotation.NonNull;

import midicore.MidiDeviceEventListener;

/**
 * Created by jim on 2017/4/10.
 */
public interface MiDiDataSource {
    int MIDI_COMMAND_CONNECT = 0;
    int MIDI_COMMAND_DISCONNECT = 1;
    int MIDI_COMMAND_QUERY_INFO = 2;
    int MIDI_COMMAND_TURN_ON_ALL = 3;
    int MIDI_COMMAND_TURN_OFF_ALL = 4;
    int MIDI_COMMAND_TURN_ON = 5;
    int MIDI_COMMAND_TURN_OFF = 6;
    int MIDI_COMMAND_KEY_PRESS = 7;

    boolean startDevice();

    boolean stopDevice();

    boolean resumeDevice();

    boolean pauseDevice();

    void setMidiDevEventListener(EventListener listener);

    void sendMidiEvent(int type, int data1, int data2);

    byte[] getMidiData(int type, int data1, int data2);

    interface EventListener extends MidiDeviceEventListener {
        @Override
        void onAttached();

        @Override
        void onDetached();

        @Override
        void onMidiData(final byte[] data);
    }
}
