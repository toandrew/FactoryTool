package midicore;

public interface MidiDeviceEventListener {
    void onAttached();
    void onDetached();
    void onMidiData(final byte data[]);
}
