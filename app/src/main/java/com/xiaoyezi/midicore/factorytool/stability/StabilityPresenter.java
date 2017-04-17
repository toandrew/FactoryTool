package com.xiaoyezi.midicore.factorytool.stability;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.xiaoyezi.midicore.factorytool.data.MiDiDataRepository;
import com.xiaoyezi.midicore.factorytool.data.MiDiDataRepository.TestData;
import com.xiaoyezi.midicore.factorytool.data.MiDiDataSource;
import com.xiaoyezi.midicore.factorytool.data.TestConfig;
import com.xiaoyezi.midicore.factorytool.utils.Tlog;
import com.xiaoyezi.midicore.factorytool.utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by jim on 2017/4/10.
 */
public class StabilityPresenter implements StabilityContract.Presenter {
    private static final String TAG = "StabilityPresenter";

    private StabilityFragment mStabilityFragment;

    private MiDiDataRepository mMiDiDataRepository;

    StabilityPresenter.TestMidiEventListener mMidiListener = new StabilityPresenter.TestMidiEventListener();

    Subscription mSubscription;

    // badly
    private int mCurrentTestIndex = 0;

    public StabilityPresenter(StabilityFragment fragment, MiDiDataRepository dataRepository) {
        mStabilityFragment = fragment;
        mMiDiDataRepository = dataRepository;
    }

    @Override
    public void start(@NonNull final Context context) {
        Tlog.e("startDevice!");

        mMiDiDataRepository.setMidiDevEventListener(mMidiListener);
        mMiDiDataRepository.startDevice();
    }

    @Override
    public void stop() {
        Tlog.e("stopDevice!");

        // stop test
        if (mSubscription != null) {
            mStabilityFragment.onTestFinished();

            stopTest();
        }

        mMiDiDataRepository.stopDevice();
    }

    @Override
    public void resume() {
        Tlog.e("resumeDevice!");
        mMiDiDataRepository.resumeDevice();
    }

    @Override
    public void pause() {
        Tlog.e("pauseDevice!");
        mMiDiDataRepository.pauseDevice();
    }

    @Override
    public void sendMidiEvent(int type, int data1, int data2) {
        mMiDiDataRepository.sendMidiEvent(type, data1, data2);
    }

    /**
     * Midi event listener
     */
    private final class TestMidiEventListener implements MiDiDataSource.EventListener {
        @Override
        public void onAttached() {
            mStabilityFragment.setDeviceConnectionState(true);
        }

        @Override
        public void onDetached() {
            mStabilityFragment.setDeviceConnectionState(false);
        }

        @Override
        public void onMidiData(final byte[] data) {
            mStabilityFragment.onMidiData(data);
        }
    }

    /**
     * Start/Stop test
     *
     * @param config
     */
    @Override
    public void startStopText(final TestConfig config) {
        if (mSubscription == null) {
            final ArrayList<TestData> testSet = (ArrayList) buildTests(config);

            Tlog.e("BEGIN");

            mCurrentTestIndex = 0;
            mSubscription = Observable.interval(config.getTestTimeGap() == 0 ? 1 : config.getTestTimeGap(), TimeUnit.MILLISECONDS)
                    .limit(config.getTestTimes() == -1 ? Integer.MAX_VALUE : testSet.size() * config.getTestTimes())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {

                        }
                    })
                    .doOnUnsubscribe(new Action0() {
                        @Override
                        public void call() {

                        }
                    })
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {
                            Tlog.e("END");

                            if (!mSubscription.isUnsubscribed()) {
                                mSubscription.unsubscribe();
                                mSubscription = null;
                            }
                            Log.w(TAG, "onCompleted!");
                            mStabilityFragment.onTestFinished();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Tlog.e("onError");

                            if (!mSubscription.isUnsubscribed()) {
                                mSubscription.unsubscribe();
                                mSubscription = null;
                            }

                            mStabilityFragment.onTestFinished();
                        }

                        @Override
                        public void onNext(Long aLong) {
                            if (mCurrentTestIndex < testSet.size()) {
                                TestData t = testSet.get(mCurrentTestIndex);
                                Log.w(TAG, "onNext!" + t.type);
                                if (t.type == MiDiDataSource.MIDI_COMMAND_TURN_ON || t.type == MiDiDataSource.MIDI_COMMAND_TURN_OFF) {
                                    t.data1 = (int) Utils.getRandom(22, 110);
                                    t.data2 = (int) Utils.getRandom(1, 3);
                                }
                                mStabilityFragment.onSendMidiData(mMiDiDataRepository.getMidiData(t.type, t.data1, t.data2));
                                sendMidiEvent(t.type, t.data1, t.data2);
                                mCurrentTestIndex++;
                            } else {
                                mCurrentTestIndex = 0;
                            }
                        }
                    });
        } else {
            Tlog.e("END");

            stopTest();
        }
    }

    /**
     * Create all tests
     *
     * @param config
     * @return
     */
    private ArrayList<TestData> buildTests(final TestConfig config) {
        ArrayList<TestData> tests = new ArrayList<>();

        if (config.getConnectTest()) {
            tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_CONNECT, 0, 0));
            tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_DISCONNECT, 0, 0));
        }

        if (config.getDevInfoTest()) {
            tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_CONNECT, 0, 0));
            tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_QUERY_INFO, 0, 0));
        }

        if (config.getLightAllTest()) {
            tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_CONNECT, 0, 0));
            int color = (int) Utils.getRandom(1, 3);
            tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_TURN_ON_ALL, color, 0));
            tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_TURN_OFF_ALL, color, 0));
        }

        if (config.getLightTest()) {
            tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_CONNECT, 0, 0));
            int lightNum = config.getTestLightNum();
            for (int i = 0; i < lightNum; i++) {
                int light = (int) Utils.getRandom(22, 110);
                int color = (int) Utils.getRandom(1, 3);
                tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_TURN_ON, light, color));
                tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_TURN_OFF, light, color));
            }
        }

        tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_CONNECT, 0, 0));
        tests.add(new TestData(MiDiDataSource.MIDI_COMMAND_TURN_OFF_ALL, 0, 0));

        return tests;
    }

    /**
     * Stop test
     */
    private void stopTest() {
        mSubscription.unsubscribe();
        mSubscription = null;
        mCurrentTestIndex = 0;
    }

    /**
     * Test config data
     */
    public static class StabilityTestConfig extends TestConfig {
    }
}
