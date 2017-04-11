package com.xiaoyezi.midicore.factorytool.stability;

import com.xiaoyezi.midicore.factorytool.base.BasePresenter;
import com.xiaoyezi.midicore.factorytool.base.BaseView;

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
         * Update log info
         *
         * @param fullPath
         * @param size
         */
        void updateLogInfo(String fullPath, int size);
    }


    interface Presenter extends BasePresenter {
        void startStopText(TestConfig config);

        void sendMidiEvent(int type, int data1, int data2);

        abstract class TestConfig {
            private boolean connectTest;
            private boolean devInfoTest;
            private boolean lightAllTest;
            private boolean lightTest;

            private int times;
            private int timeGap;

            public void setConnectTest(boolean enable) {
                connectTest = enable;
            }

            public boolean getConnectTest() {
                return connectTest;
            }

            public void setDevInfoTest(boolean enable) {
                devInfoTest = enable;
            }

            public boolean getDevInfoTest() {
                return devInfoTest;
            }

            public void setLightAllTest(boolean enable) {
                lightAllTest = enable;
            }

            public boolean getLightAllTest() {
                return lightAllTest;
            }

            public void setLightTest(boolean enable) {
                lightTest = enable;
            }

            public boolean getLightTest() {
                return lightTest;
            }

            public void setTestTimes(int t) {
                times = t;
            }

            public int getTestTimes() {
                return times;
            }

            public void setTestTimeGap(int tg) {
                timeGap = tg;
            }

            public int getTesetTimeGap() {
                return timeGap;
            }
        }
    }
}
