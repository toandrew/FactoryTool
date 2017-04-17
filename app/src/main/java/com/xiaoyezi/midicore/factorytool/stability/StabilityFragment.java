package com.xiaoyezi.midicore.factorytool.stability;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xiaoyezi.midicore.factorytool.R;
import com.xiaoyezi.midicore.factorytool.base.BaseFragment;
import com.xiaoyezi.midicore.factorytool.data.Injection;
import com.xiaoyezi.midicore.factorytool.data.TestConfig;
import com.xiaoyezi.midicore.factorytool.utils.Tlog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * Created by jianmin on 2017/4/10.
 */
public class StabilityFragment extends BaseFragment implements StabilityContract.View {
    private static final String TAG = "StabilityFragment";

    private Unbinder unbinder;

    private StabilityContract.Presenter mStabilityPresenter;

    @BindView(R.id.log_info_textview)
    TextView mLogInfoTextView;

    // Receive midi data
    @BindView(R.id.midi_info_textview)
    TextView mReceivedMidiDataInfoTextView;

    // Send midi data
    @BindView(R.id.send_midi_info_textview)
    TextView mSendMidiDataInfoTextView;

    @BindView(R.id.start_stop_btn)
    ToggleButton mStartStopBtn;

    @BindView(R.id.device_status_textview)
    TextView mDeviceStatusTextView;

    @BindView(R.id.times_spinner)
    Spinner mTimesSpinner;

    @BindView(R.id.time_gap_spinner)
    Spinner mTimeGapSpinner;

    @BindView(R.id.connect_disconnect_cb)
    CheckBox mConnectDisconnectCb;

    @BindView(R.id.device_info_cb)
    CheckBox mDeviceInfoCb;

    @BindView(R.id.light_all_on_off_cb)
    CheckBox mLightAllOnOffCb;

    @BindView(R.id.light_on_off_cb)
    CheckBox mLightOnOffCb;

    @BindView(R.id.light_num_textview)
    TextView mLightNumTextView;

    private int mFinishedTestNum = 0;

    private TestConfig mTestConfg = new StabilityPresenter.StabilityTestConfig();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            Log.w(TAG, "visible!!");
            if (mStabilityPresenter != null && this.getActivity() != null) {
                Log.w(TAG, "visible!!mStabilityPresenter.start!!!");
                mStabilityPresenter.start(this.getActivity());
            }
        } else {
            Log.w(TAG, "invisible!!");
            if (mStabilityPresenter != null) {
                Log.w(TAG, "invisible!!mStabilityPresenter.stop!!!");
                mStabilityPresenter.stop();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stability, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.w(TAG, "onViewCreated!");

        initDefaultConfig();

        mStabilityPresenter = new StabilityPresenter(this, Injection.provideMiDiDataRepository(this.getActivity().getApplicationContext()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.w(TAG, "onResume!");
        mStabilityPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.w(TAG, "onPause!");
        mStabilityPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mStabilityPresenter.stop();

        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(StabilityContract.Presenter presenter) {
        mStabilityPresenter = presenter;
    }

    @Override
    public void setDeviceConnectionState(final boolean b) {
        final Activity activity = this.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusTextView.setText(b ? "琴已连接" : "琴未连接");

                int okColor = activity.getResources().getColor(R.color.green_500);
                int badColor = activity.getResources().getColor(R.color.red);
                mDeviceStatusTextView.setTextColor(b ? okColor : badColor);
            }
        });
    }

    @Override
    public void onMidiData(final byte[] data) {
        final Activity activity = this.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String s = "";
                for (int i = 0; i < data.length; ++i) {
                    s += String.format("0x%x ", data[i]);
                }

                Tlog.d("<<<<<<<<[" + data.length + "]");
                Tlog.d(s);

                String info = String.format("收到的MIDI数据长度为 %d\n", data.length);
                mReceivedMidiDataInfoTextView.setText(info + s);
            }
        });
    }

    @Override
    public void onSendMidiData(final byte[] data) {
        final Activity activity = this.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFinishedTestNum++;
                String s = "";
                for (int i = 0; i < data.length; ++i) {
                    s += String.format("0x%x ", data[i]);
                }

                Tlog.d(">>>>>>>[" + data.length + "]");
                Tlog.d(s);

                String info = String.format("已发送: %d, 发送的MIDI数据长度为 %d\n", mFinishedTestNum, data.length);
                mSendMidiDataInfoTextView.setText(info + s);
            }
        });
    }

    @Override
    public void updateLogInfo(String fullPath, int size) {
    }

    @Override
    public void onTestFinished() {
        mStartStopBtn.setChecked(!mStartStopBtn.isChecked());
        resetDefaultStatus();
    }

    /**
     * Init default test config
     */
    private void initDefaultConfig() {
        mTestConfg.setTestTimes(Integer.valueOf(mTimesSpinner.getSelectedItem().toString()));
        mTestConfg.setTestTimeGap(Integer.valueOf(mTimeGapSpinner.getSelectedItem().toString()));
        mTestConfg.setConnectTest(mConnectDisconnectCb.isChecked());
        mTestConfg.setDevInfoTest(mDeviceInfoCb.isChecked());
        mTestConfg.setLightAllTest(mLightAllOnOffCb.isChecked());
        mTestConfg.setLightTest(mLightOnOffCb.isChecked());
        mTestConfg.setTestLightNum(Integer.valueOf(mLightNumTextView.getText().toString()));
    }

    /**
     * Ret midi sent/received info
     */
    private void resetDefaultStatus() {
        mLogInfoTextView.setText(Tlog.getLogName());
        mSendMidiDataInfoTextView.setText("");
        mReceivedMidiDataInfoTextView.setText("");
        mFinishedTestNum = 0;
    }

    /**
     * Start/Stop test
     */
    @OnClick(R.id.start_stop_btn)
    void onStartStopTest() {
        resetDefaultStatus();

        mStabilityPresenter.startStopText(mTestConfg);
    }

    /**
     * Test light number is changed
     *
     * @param editable
     */
    @OnTextChanged(value = R.id.light_num_textview, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onLightNumTextChanged(Editable editable) {
        try {
            String s = editable.toString();
            if (!s.isEmpty()) {
                mTestConfg.setTestLightNum(Integer.parseInt(s));
            }
        } catch (Exception e) {
            editable.clear();
            editable.append("" + mTestConfg.getTestLightNum());
        }
    }

    /**
     * Light all on/off test
     *
     * @param enable
     */
    @OnCheckedChanged(R.id.light_all_on_off_cb)
    void onEnableLightAllOnOffTest(boolean enable) {
        mTestConfg.setLightAllTest(enable);
    }

    /**
     * Multi lights on/off test
     *
     * @param enable
     */
    @OnCheckedChanged(R.id.light_on_off_cb)
    void onEnableLightOnOffTest(boolean enable) {
        mTestConfg.setLightTest(enable);
    }

    /**
     * Get device info test
     *
     * @param enable
     */
    @OnCheckedChanged(R.id.device_info_cb)
    void onEnableDevInfoTest(boolean enable) {
        mTestConfg.setDevInfoTest(enable);
    }

    /**
     * Connect & Disconnect test
     *
     * @param enable
     */
    @OnCheckedChanged(R.id.connect_disconnect_cb)
    void onEnableConnectTest(boolean enable) {
        mTestConfg.setConnectTest(enable);
    }

    /**
     * Test send times
     *
     * @param position
     */
    @OnItemSelected(R.id.times_spinner)
    void onTimesItemSelected(int position) {
        String[] times = getResources().getStringArray(R.array.times);

        mTestConfg.setTestTimes(Integer.valueOf(times[position]));
    }

    /**
     * Test time gap
     *
     * @param position
     */
    @OnItemSelected(R.id.time_gap_spinner)
    void onTimeGapItemSelected(int position) {
        String[] timeGap = getResources().getStringArray(R.array.time_gap);

        mTestConfg.setTestTimeGap(Integer.valueOf(timeGap[position]));
    }
}
