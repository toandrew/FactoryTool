package jp.kshoji.driver.midi.listener;

import jp.kshoji.driver.midi.device.MidiInputDevice;

/**
 * Listener for MIDI events
 * For events' details, @see <a href="http://www.usb.org/developers/devclass_docs/midi10.pdf">Universal Serial Bus Device Class Definition for MIDI Devices</a>
 *
 * @author K.Shoji
 */
public interface OnMidiInputEventListener {

	/**
	 * Miscellaneous function codes. Reserved for future extensions.
	 * Code Index Number : 0x0
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
	 * @param data raw midi data
	 */
	void onMidiData(MidiInputDevice sender, int cable, final byte data[]);
}
