package com.xiaoyezi.midicore.factorytool.data;

import android.content.Context;
import android.support.annotation.NonNull;

import midicore.MidiDeviceManager;

/**
 * Created by jim on 2017/4/10.
 */
public class MiDiDataRepository implements MiDiDataSource {
    private static MiDiDataRepository sMiDiDataRepository = null;

    private MiDiDataRepository() {
    }

    public static MiDiDataRepository getInstance(@NonNull Context context) {
        if (sMiDiDataRepository == null) {
            init(context);
        }

        return sMiDiDataRepository;
    }

    @Override
    public boolean startDevice() {
        MidiDeviceManager.getInstance().open();

        return true;
    }

    @Override
    public boolean stopDevice() {
        MidiDeviceManager.getInstance().close();

        return true;
    }

    @Override
    public boolean resumeDevice() {
        MidiDeviceManager.getInstance().resumeDevices();

        return true;
    }

    @Override
    public boolean pauseDevice() {
        MidiDeviceManager.getInstance().suspendDevices();

        return true;
    }

    @Override
    public void setMidiDevEventListener(EventListener listener) {
        MidiDeviceManager.getInstance().setMidiDevEventListener(listener);
    }

    @Override
    public void sendMidiEvent(int type, int data1, int data2) {
        switch (type) {
            case MIDI_COMMAND_CONNECT:
                byte cmd[] = {(byte) 0xf0, 0, 0x20, 0x2b, 0x69, 0, 0, 0x55, 0x79, (byte) 0xf7};
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
                byte cmdOn[] = {(byte) 0xa2, (byte) pitch2Light(data1), (byte) data2};
                MidiDeviceManager.getInstance().sendMsg(cmdOn);
                break;
            case MIDI_COMMAND_TURN_OFF:
                MidiDeviceManager.getInstance().sendMsgEx(false, 0xa2, (byte) pitch2Light(data1), 0);
                break;
            case MIDI_COMMAND_KEY_PRESS:
                MidiDeviceManager.getInstance().sendMsgEx(false, 0x90, (byte) data1, 75);
                break;
        }
    }

    /**
     * nit repo
     *
     * @param context
     */
    private synchronized static void init(@NonNull Context context) {
        if (sMiDiDataRepository == null) {
            sMiDiDataRepository = new MiDiDataRepository();
            sMiDiDataRepository.initDevice(context);
        }
    }

    /**
     * Init midi device
     *
     * @param context
     */
    private void initDevice(@NonNull Context context) {
        MidiDeviceManager.getInstance().init(context.getApplicationContext());
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
}
