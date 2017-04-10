package jp.kshoji.driver.midi.device;

public interface MidiDeviceInterface {
    void start();
    void stop();
    void resume();
    void suspend();
}
