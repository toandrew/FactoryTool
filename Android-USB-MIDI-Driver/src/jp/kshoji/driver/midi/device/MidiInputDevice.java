package jp.kshoji.driver.midi.device;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.util.SparseIntArray;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import jp.kshoji.driver.midi.device.MidiDeviceBase;
import jp.kshoji.driver.midi.listener.OnMidiInputEventListener;
import jp.kshoji.driver.midi.util.ReusableByteArrayOutputStream;
import jp.kshoji.driver.midi.util.UsbMidiDeviceUtils;

/**
 * MIDI Input Device
 *
 * @author K.Shoji
 * @modifier 张宇飞(zhangyufei@xiaoyezi.com)
 */
public final class MidiInputDevice extends MidiDeviceBase {
    private static final int CABLE_COUNT = 16;
    private OnMidiInputEventListener midiEventListener = null;
    private final WaiterThread waiterThread;
    private static final byte[] CONNECTION_RESPONSE = { 0x4, (byte)0xf0, 0x0, 0x20, 0x4, 0x2b, 0x69, 0x40, 0x4, 0x0, 0x61, 0x33, 0x5, (byte)0xf7 };
    private static final byte[] TIMING_CLOCK_RESPONSE = { 0xf, (byte)0xf8, 0x0, 0x0 };
    private static final byte[] ACTIVE_SENSING_RESPONSE = { 0xf, (byte)0xfe, 0x0, 0x0 };


    public MidiInputDevice( UsbDevice usbDevice,  UsbDeviceConnection usbDeviceConnection,  UsbInterface usbInterface,  UsbEndpoint usbEndpoint) throws IllegalArgumentException {
        super(usbDevice, usbDeviceConnection, usbInterface, usbEndpoint);

        this.waiterThread = new WaiterThread();
        waiterThread.setName("[MidiInputDevice waiterThread " + usbDevice.getDeviceName() + "]");
        this.waiterThread.setPriority(8);
    }

    /**
     * Sets the OnMidiInputEventListener
     *
     * @param midiEventListener the OnMidiInputEventListener
     */
    public void setMidiEventListener(OnMidiInputEventListener midiEventListener) {
        this.midiEventListener = midiEventListener;
    }

    public void start() {
        if (!waiterThread.isAlive()) {
            waiterThread.start();
        }
    }

    @Override
    public void stop() {
        super.stop();
        midiEventListener = null;
        waiterThread.stopFlag = true;
        resume();

        // blocks while the thread will stop
        while (waiterThread.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    /**
     * Suspends event listening
     */
    public void suspend() {
        synchronized (waiterThread.suspendSignal) {
            waiterThread.suspendFlag = true;
        }
    }

    /**
     * Resumes event listening
     */
    public void resume() {
        synchronized (waiterThread.suspendSignal) {
            waiterThread.suspendFlag = false;
            waiterThread.suspendSignal.notifyAll();
        }
    }

    /**
     * 判定a是否包含b
     * @return b在a中的起始,-1表示不包含
     */
    static public int containArray(final byte[] a, int la, final byte[] b, int lb) {
        int j, k;
        if (lb > la) {
            return -1;
        }
        for (int i = 0; i < la; ++i) {
            j = 0;
            for (; j < lb; ++j) {
                k = i + j;
                if (k >= la || a[k] != b[j]) {
                    break;
                }
            }
            if (j == lb) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Polling thread for input data.
     * Loops infinitely while stopFlag == false.
     *
     * @author K.Shoji
     */
    final class WaiterThread extends Thread {
        volatile boolean stopFlag;
        final Object suspendSignal = new Object();
        volatile boolean suspendFlag;
        private int needAlign4BytesFlag; // 0 表示未初始化, 1 表示无需对齐, 2 表示需要对齐

        /**
         * Constructor
         */
        WaiterThread() {
            stopFlag = false;
            suspendFlag = false;
            needAlign4BytesFlag = 0;
        }

        private void handleMidiData( MidiInputDevice sender, int cable, final byte d[]) {
            final OnMidiInputEventListener midiEventListener = MidiInputDevice.this.midiEventListener;
            if (midiEventListener != null) {
                midiEventListener.onMidiData(sender, cable, d);
            }
        }

        private void handleMidiData( MidiInputDevice sender, int cable, int... v) {
            final OnMidiInputEventListener midiEventListener = MidiInputDevice.this.midiEventListener;
            if (midiEventListener == null) {
                return;
            }
            byte d[] = new byte[v.length];
            for (int i = 0; i < v.length; ++i) {
                d[i] = (byte)v[i];
            }
            midiEventListener.onMidiData(sender, cable, d);
        }

        @Override
        public void run() {
            final UsbDeviceConnection deviceConnection = usbDeviceConnection;
            final UsbEndpoint usbEndpoint = MidiInputDevice.this.usbEndpoint;
            final int maxPacketSize = usbEndpoint.getMaxPacketSize();
            final MidiInputDevice sender = MidiInputDevice.this;

            // prepare buffer variables 加4。 因为去掉了2倍缓冲区,这里加4来防止解析systemExt时,调整偏移量导致数组越界crash
            final byte[] bulkReadBuffer = new byte[maxPacketSize + 4];
            int length;
            int cable;
            int codeIndexNumber;
            int byte1;
            int byte2;
            int byte3;
            int i, j;

            // for SysEx
            final ReusableByteArrayOutputStream[] systemExclusive = new ReusableByteArrayOutputStream[CABLE_COUNT];

            for (i = 0; i < CABLE_COUNT; i++) {
                systemExclusive[i] = new ReusableByteArrayOutputStream();
            }

            // Don't allocate instances in the loop, as much as possible.
            while (!stopFlag) {
                // NOTE(zhangyufei): 如果 bulkTransferd的超时设置为0，那么直到有数据前会一直阻塞
                // 如果设置一个较小的超时时间例如10ms，又会出现某些设备，某些情况下离奇的完全阻塞到这里。具体原因不详，看上去和系统性能有关
                length = deviceConnection.bulkTransfer(usbEndpoint, bulkReadBuffer, maxPacketSize, 100);
                synchronized (suspendSignal) {
                    if (suspendFlag) {
                        try {
                            // check the deviceConnection to ignore events while suspending.
                            // Note: Events received within last sleeping(100msec) will be sent to the midiEventListener.
                            suspendSignal.wait(100);
                        } catch (InterruptedException e) {
                            // ignore exception
                        }
                        continue;
                    }
                }

                // NOTE(zhangyufei): 不会存在1个字节的USB-MIDI数据。由于部分硬件电路干扰产生的1字节消息,将被这里屏蔽掉
                if (length < 2) {
                    continue;
                }

                // NOTE(zhangyufei): 因为TAP的早期固件中存在缺陷,未严格按照USB-MIDI协议补齐4字节发送数据包
                // 导致正常地解析无法进行。 此处就需要判定是否需要进行4字节补齐操作
                // 1. 外部应当首先发送链接设备的消息,不发送的话,将忽略其它所有的消息
                // 2. 对比链接消息的响应。其中14个字节有效数据。正常固件为16字节对齐,后面补2个0的。异常为整好14个字节

                if (0 == needAlign4BytesFlag) {
                    i = containArray(bulkReadBuffer, length, MidiInputDevice.CONNECTION_RESPONSE, 14);
                    if (i == 0) {
                        needAlign4BytesFlag = (length == 16) ? 1 : 2;
                    } else {
                        // 非TAP会发送f f8 0 0和f fe 0 0这样两个包,所以如果有包含这个包就不是TAP
                        if (containArray(bulkReadBuffer, length, MidiInputDevice.TIMING_CLOCK_RESPONSE, 4) >= 0 ||
                                containArray(bulkReadBuffer, length, MidiInputDevice.ACTIVE_SENSING_RESPONSE, 4) >= 0) {

                            needAlign4BytesFlag = 1;
                        } else {
                            continue;
                        }
                    }
                    System.out.format("###### this deivce %s align 4 bytes\n", (needAlign4BytesFlag == 2) ? "need" : "does't need");
                }

                // NOTE(zhangyufei): codeIndexNumber 为4, 5, 6, 7的时候是SysEx消息。其中4, 7是刚好4字节的
                // 所以只需处理5, 6两种情况,将i值做适当的偏移
                for (i = 0; i < length; i += 4) {
                    cable = (bulkReadBuffer[i] >> 4) & 0xf;
                    codeIndexNumber = bulkReadBuffer[i] & 0xf;
                    byte1 = bulkReadBuffer[i + 1] & 0xff;
                    byte2 = bulkReadBuffer[i + 2] & 0xff;
                    byte3 = bulkReadBuffer[i + 3] & 0xff;

                    // NOTE(zhangyufei): 4, 5, 6, 7是systemEx命令,不是这种命令的时候,应当清空systemEx的buf。
                    // TOK, TOP 有时初次链接usb的时候会发送无用数据来导致练琴检测失败。通过这样的过滤来规避这样的情况
                    if (codeIndexNumber < 4 || codeIndexNumber > 7) {
                        for (j = 0; j < CABLE_COUNT; ++j) {
                            systemExclusive[j].reset();
                        }
                    }

                    switch (codeIndexNumber) {
                        case 0:
                        case 1:
                        case 3:
                        case 8:
                        case 10:
                        case 11:
                            handleMidiData(sender, cable, byte1, byte2, byte3);
                            break;
                        case 4:
                            // sysex starts, and has next
                            synchronized (systemExclusive[cable]) {
                                systemExclusive[cable].write(byte1);
                                systemExclusive[cable].write(byte2);
                                systemExclusive[cable].write(byte3);
                            }
                            break;
                        case 5:
                            // system common message with 1byte
                            // sysex end with 1 byte
                            synchronized (systemExclusive[cable]) {
                                // 我们只关心0xf7这个消息，其它自定义消息忽略掉
                                if (byte1 == 0xf7) {
                                    systemExclusive[cable].write(byte1);
                                    handleMidiData(sender, cable, systemExclusive[cable].toByteArray());
                                }
                                systemExclusive[cable].reset();
                                if (2 == needAlign4BytesFlag) {
                                    i -= 2;
                                }
                            }
                            break;
                        case 6:
                            // sysex end with 2 bytes
                            synchronized (systemExclusive[cable]) {
                                systemExclusive[cable].write(byte1);
                                systemExclusive[cable].write(byte2);
                                handleMidiData(sender, cable, systemExclusive[cable].toByteArray());
                                systemExclusive[cable].reset();
                                if (2 == needAlign4BytesFlag) {
                                    i -= 1;
                                }
                            }
                            break;
                        case 7:
                            // sysex end with 3 bytes
                            synchronized (systemExclusive[cable]) {
                                systemExclusive[cable].write(byte1);
                                systemExclusive[cable].write(byte2);
                                systemExclusive[cable].write(byte3);
                                handleMidiData(sender, cable, systemExclusive[cable].toByteArray());
                                systemExclusive[cable].reset();
                            }
                            break;
                        case 9:
                            handleMidiData(sender, cable, byte1, byte2, byte3);
                            break;
                        case 12:
                        case 13:
                        case 2:
                            handleMidiData(sender, cable, byte1, byte2);
                            break;
                        case 14:
                            handleMidiData(sender, cable, byte1, byte2 | (byte3 << 7));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
}
