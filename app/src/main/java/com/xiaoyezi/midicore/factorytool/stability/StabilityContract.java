package com.xiaoyezi.midicore.factorytool.stability;

import com.xiaoyezi.midicore.factorytool.base.BasePresenter;
import com.xiaoyezi.midicore.factorytool.base.BaseView;
import com.xiaoyezi.midicore.factorytool.data.TestConfig;

/**
 * Created by jim on 2017/4/10.
 */
public interface StabilityContract {
    interface View extends BaseView<Presenter> {
        /**
         * Set device connect status
         *
         * @param b
         */
        void setDeviceConnectionState(final boolean b);

        /**
         * Midi data received
         *
         * @param data
         */
        void onMidiData(final byte[] data);

        /**
         * When midi data sent
         *
         * @param data
         */
        void onSendMidiData(final byte[] data);

        /**
         * Update log info
         *
         * @param fullPath
         * @param size
         */
        void updateLogInfo(String fullPath, int size);

        /**
         * Test finished
         */
        void onTestFinished();
    }

    interface Presenter extends BasePresenter {
        void startStopText(TestConfig config);

        void sendMidiEvent(int type, int data1, int data2);
    }
}
