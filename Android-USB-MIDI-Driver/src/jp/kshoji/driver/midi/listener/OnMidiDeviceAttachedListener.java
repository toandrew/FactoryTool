package jp.kshoji.driver.midi.listener;

import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;

/**
 * Listener for MIDI attached events
 *
 * @author K.Shoji
 */
public interface OnMidiDeviceAttachedListener {
    /**
     * MIDI input device has been attached
     *
     * @param midiInputDevice attached MIDI Input device
     */
    void onMidiInputDeviceAttached(MidiInputDevice midiInputDevice);

    /**
     * MIDI output device has been attached
     *
     * @param midiOutputDevice attached MIDI Output device
     */
    void onMidiOutputDeviceAttached(MidiOutputDevice midiOutputDevice);
}
