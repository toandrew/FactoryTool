package com.xiaoyezi.midicore.factorytool.basic;

import com.xiaoyezi.midicore.factorytool.base.BasePresenter;
import com.xiaoyezi.midicore.factorytool.base.BaseView;

/**
 * Created by jim on 2017/4/10.
 */
public interface BasicContract {
    interface View extends BaseView<Presenter> {
        void setDeviceConnectionState(final boolean b);

        void onMidiData(final byte[] data);
    }

    interface Presenter extends BasePresenter {
        void sendMidiEvent(int type, int data1, int data2);
    }
}
