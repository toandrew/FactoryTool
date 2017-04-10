/*******************************************
 * @简介: 用于管理UsbMidi设备
 * @作者: 张宇飞(zhangyufei@xiaoyezi.com
 * @日期: 2016-11-22
 * ****************************************/

package midicore;

import android.content.Context;
import android.hardware.usb.UsbDevice;

import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;
import jp.kshoji.driver.midi.util.UsbMidiDriver;

import midicore.MidiDeviceEventListener;

public final class MidiDeviceManager {
    private static final MidiDeviceManager  sInstance         = new MidiDeviceManager();
    private UsbMidiDriver                   mUsbMidiDriver    = null;
    private static final int                TARGET_VENDOR_ID  = 0x0a67;
    private boolean                         mIsValidated      = true;
    private int                             mTargetProductId  = 0;
    private MidiDeviceEventListener         mDevEventListener = null;

    public static MidiDeviceManager getInstance() {
        return sInstance;
    }

    public void     open() { mUsbMidiDriver.open(); }
    public void     close() { mUsbMidiDriver.close(); }
    public void     suspendDevices() { mUsbMidiDriver.suspend();}
    public void     resumeDevices() { mUsbMidiDriver.resume();}
    public boolean  isValidated() { return mIsValidated; }
    public void     setValidated(boolean mIsValidated) { this.mIsValidated = mIsValidated; }
    public int      getDeviceProductId() { return mTargetProductId; }
    public void     setMidiDevEventListener(MidiDeviceEventListener listener) { mDevEventListener = listener; }

    // NOTE(zhangyufei): 在后面的接口中，应当添加fd参数可用于指定向单个设备发送消息
    public void sendMsg(final byte data[]) {
        if (mUsbMidiDriver == null) {
            return;
        }
        // 根据data长度来判定是系统消息还是普通消息
        if (data.length > 6) {
            long c = ((long)(data[0] + 256) << 32) | (data[1] << 24) | (data[2] << 16) | (data[3] << 8) | data[4];
            if (c == 0xf000202b69L) {
                for (MidiOutputDevice midiOutputDevice : mUsbMidiDriver.getMidiOutputDevices()) {
                    midiOutputDevice.sendMidiSystemExclusive(0, data);
                }
                return;
            }
        }
        for (MidiOutputDevice midiOutputDevice : mUsbMidiDriver.getMidiOutputDevices()) {
            midiOutputDevice.sendMidiSystemCommonMessage(0, data);
        }
    }

    public void sendMsgEx(boolean isSys, int... v) {
        byte cmd[];
        int i;
        if (isSys) {
            cmd = new byte[v.length + 6];
            cmd[0] = (byte)(0xf0);
            cmd[1] = 0;
            cmd[2] = 0x20;
            cmd[3] = 0x2b;
            cmd[4] = 0x69;
            for (i = 0; i < v.length; ++i) {
                cmd[i + 5] = (byte)v[i];
            }
            cmd[i + 5] = (byte)0xf7;
            for (MidiOutputDevice midiOutputDevice : mUsbMidiDriver.getMidiOutputDevices()) {
                midiOutputDevice.sendMidiSystemExclusive(0, cmd);
            }
        } else {
            cmd = new byte[v.length];
            for (i = 0; i < v.length; ++i) {
                cmd[i] = (byte)v[i];
            }
            for (MidiOutputDevice midiOutputDevice : mUsbMidiDriver.getMidiOutputDevices()) {
                midiOutputDevice.sendMidiSystemCommonMessage(0, cmd);
            }
        }
    }

    public void init(final Context context) {
        mUsbMidiDriver = new UsbMidiDriver(context) {
            @Override
            public void onMidiOutputDeviceAttached( final MidiOutputDevice midiOutputDevice) {
                mTargetProductId = midiOutputDevice.usbDevice.getProductId();
                // 使用usb 的VendorID来断定是否是The One的设备。但是在这一次不使用这个属性做任何处理
                mIsValidated = midiOutputDevice.usbDevice.getVendorId() == MidiDeviceManager.TARGET_VENDOR_ID;

                if (mDevEventListener != null) {
                    mDevEventListener.onAttached();
                }
            }

            @Override
            public void onMidiOutputDeviceDetached( final MidiOutputDevice midiOutputDevice) {
                mIsValidated = true;
                mTargetProductId = 0;
                if (mDevEventListener != null) {
                    mDevEventListener.onDetached();
                }
            }

            @Override
            public void onMidiInputDeviceAttached( MidiInputDevice midiInputDevice) {
                midiInputDevice.setMidiEventListener(this);
            }

            @Override
            public void onMidiInputDeviceDetached( MidiInputDevice midiInputDevice) {
                midiInputDevice.setMidiEventListener(null);
            }

            @Override
            public void onMidiData( MidiInputDevice sender, int cable, final byte[] data) {
                if (mDevEventListener != null) {
                    mDevEventListener.onMidiData(data);
                }
            }
        };
    }
}
