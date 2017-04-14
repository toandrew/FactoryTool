package com.xiaoyezi.midicore.factorytool.data;

/**
 * Created by jim on 2017/4/13.
 */
public abstract class TestConfig {
    private boolean connectTest;
    private boolean devInfoTest;
    private boolean lightAllTest;
    private boolean lightTest;

    private int times;
    private int timeGap;

    private int lightNum = 0;

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

    public int getTestTimeGap() {
        return timeGap;
    }

    public int getTestLightNum() {
        return lightNum;
    }

    public void setTestLightNum(int num) {
        lightNum = num;
    }
}