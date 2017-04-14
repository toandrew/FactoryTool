package com.xiaoyezi.midicore.factorytool.data;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;

import com.xiaoyezi.midicore.factorytool.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import midicore.MidiDeviceManager;

/**
 * Created by jim on 2017/4/10.
 */
public class MiDiDataRepository implements MiDiDataSource {
    private static final String LOG_PATH = "/sdcard/TheOne/";

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
                byte cmdConnect[] = {(byte) 0xf0, 0, 0x20, 0x2b, 0x69, 0, 0, 0x55, 0x79, (byte) 0xf7};
                MidiDeviceManager.getInstance().sendMsg(cmdConnect);
                break;
            case MIDI_COMMAND_DISCONNECT:
                byte cmdDisconnect[] = {(byte) 0xf0, 0, 0x20, 0x2b, 0x69, 0, 1, 0x55, 0x79, (byte) 0xf7};
                MidiDeviceManager.getInstance().sendMsg(cmdDisconnect);
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

    @Override
    public byte[] getMidiData(int type, int data1, int data2) {
        switch (type) {
            case MIDI_COMMAND_CONNECT:
                byte cmd[] = {(byte) 0xf0, 0, 0x20, 0x2b, 0x69, 0, 0, 0x55, 0x79, (byte) 0xf7};
                return cmd;
            case MIDI_COMMAND_QUERY_INFO:
                byte cmdQ[] = {(byte) 0xf0, 0, 0x20, 0x2b, 0x69, 0x1, 0, 0, (byte) 0xf7};
                return cmdQ;
            case MIDI_COMMAND_TURN_ON_ALL:
                byte cmdAllOn[] = {(byte) 0xf0, 0, 0x20, 0x2b, 0x69, 0x16, 0x2, (byte) data1, (byte) 0xf7};
                return cmdAllOn;
            case MIDI_COMMAND_TURN_OFF_ALL:
                byte cmdAllOff[] = {(byte) 0xf0, 0, 0x20, 0x2b, 0x69, 0x16, 0x2, 0, (byte) 0xf7};
                return cmdAllOff;
            case MIDI_COMMAND_TURN_ON:
                byte cmdOn[] = {(byte) 0xa2, (byte) pitch2Light(data1), (byte) data2};
                return cmdOn;
            case MIDI_COMMAND_TURN_OFF:
                byte cmdOff[] = {(byte) 0xa2, (byte) pitch2Light(data1), 0};
                return cmdOff;
            case MIDI_COMMAND_KEY_PRESS:
                byte cmdKeyPress[] = {(byte) 0x90, (byte) data1, 75};
                return cmdKeyPress;
        }

        return new byte[0];
    }

    /**
     * Test Data
     */
    public static class TestData {
        public int type;
        public int data1;
        public int data2;

        public TestData() {
            this.type = MIDI_COMMAND_CONNECT;
            this.data1 = 0;
            this.data2 = 0;
        }

        public TestData(int type, int data1, int data2) {
            this.type = type;
            this.data1 = data1;
            this.data2 = data2;
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

    /**
     * Get all logs
     *
     * @return
     */
    public List<FileModel> getLogs() {
        List<FileModel> logs = new ArrayList<>();

        File[] files = new File(LOG_PATH).listFiles();
        for (File file : files) {
            FileModel log = new LogFileModel(file.getName(), file.length(), Utils.getTime(file.lastModified()));
            logs.add(log);
        }

        return logs;
    }

    /**
     * Log data
     */
    private static class LogFileModel implements FileModel {
        String fileName;
        String filePath;
        long fileSize;
        String fileTime;

        public LogFileModel(String fileName, long fileSize, String fileTime) {
            this.fileName = fileName;
            this.filePath = LOG_PATH;
            this.fileSize = fileSize;
            this.fileTime = fileTime;
        }

        @Override
        public String getName() {
            return fileName;
        }

        @Override
        public String getPath() {
            return filePath;
        }

        @Override
        public long getSize() {
            return fileSize;
        }

        @Override
        public String getCreatedTime() {
            return fileTime;
        }

        @Override
        public Image getFileIcon() {
            return null;
        }
    }

}
