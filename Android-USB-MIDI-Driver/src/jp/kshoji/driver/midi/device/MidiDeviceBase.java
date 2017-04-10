package jp.kshoji.driver.midi.device;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import jp.kshoji.driver.midi.util.UsbMidiDeviceUtils;
import jp.kshoji.driver.midi.device.MidiDeviceInterface;

public abstract class MidiDeviceBase implements MidiDeviceInterface {
    public final UsbDevice             usbDevice;
    final UsbInterface          usbInterface;
    final UsbDeviceConnection   usbDeviceConnection;
    final UsbEndpoint           usbEndpoint;

    public MidiDeviceBase( UsbDevice usbDevice,  UsbDeviceConnection usbDeviceConnection,
                           UsbInterface usbInterface,  UsbEndpoint usbEndpoint) throws IllegalArgumentException {
        this.usbDevice = usbDevice;
        this.usbDeviceConnection = usbDeviceConnection;
        this.usbInterface = usbInterface;
        this.usbEndpoint = usbEndpoint;

        usbDeviceConnection.claimInterface(usbInterface, true);
    }

    public void stop() {
        usbDeviceConnection.releaseInterface(usbInterface);
    }

    /**
     * Get the product name
     *
     * @return the product name. null if API Level < {@link android.os.Build.VERSION_CODES#HONEYCOMB_MR2 }, or the product name is truly null
     */

    public String getProductName() {
        return UsbMidiDeviceUtils.getProductName(usbDevice, usbDeviceConnection);
    }

    /**
     * Get the manufacturer name
     *
     * @return the manufacturer name. null if API Level < {@link android.os.Build.VERSION_CODES#HONEYCOMB_MR2 }, or the manufacturer name is truly null
     */

    public String getManufacturerName() {
        return UsbMidiDeviceUtils.getManufacturerName(usbDevice, usbDeviceConnection);
    }

    /**
     * Get the device name(linux device path)
     * @return the device name(linux device path)
     */

    public String getDeviceAddress() {
        return usbDevice.getDeviceName();
    }
}
