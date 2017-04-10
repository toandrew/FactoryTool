package com.xiaoyezi.midicore.factorytool.basic;

import com.xiaoyezi.midicore.factorytool.base.BasePresenter;
import com.xiaoyezi.midicore.factorytool.base.BaseView;

/**
 * Created by jim on 2017/4/10.
 */
public interface BasicContract {
    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        int MIDI_COMMAND_CONNECT        = 0;
        int MIDI_COMMAND_QUERY_INFO     = 1;
        int MIDI_COMMAND_TURN_ON_ALL    = 2;
        int MIDI_COMMAND_TURN_OFF_ALL   = 3;
        int MIDI_COMMAND_TURN_ON        = 4;
        int MIDI_COMMAND_TURN_OFF       = 5;
        int MIDI_COMMAND_KEY_PRESS      = 6;

        void sendMidiEvent(int type, int data1, int data2);
    }
}
