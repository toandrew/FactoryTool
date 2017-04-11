package com.xiaoyezi.midicore.factorytool.stability;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xiaoyezi.midicore.factorytool.R;
import com.xiaoyezi.midicore.factorytool.base.BaseFragment;
import com.xiaoyezi.midicore.factorytool.data.Injection;

/**
 * Created by jianmin on 2017/4/10.
 */
public class StabilityFragment extends BaseFragment implements StabilityContract.View {
    private static final String TAG = "StabilityFragment";

    private StabilityContract.Presenter mStabilityPresenter;

    private TextView mLogInfoTextView;

    private TextView mMidiDataInfoTextView;

    private ToggleButton mStartStopBtn;

    private TextView mDeviceStatusTextView;

    private Spinner mTimesSpinner;

    private Spinner mTimeGapSpinner;

    private CheckBox mConnectDisconnectCb;

    private CheckBox mDeviceInfoCb;

    private CheckBox mLightAllOnOffCb;

    private CheckBox mLightOnOffCb;

    private  StabilityContract.Presenter.TestConfig mTestConfg = new StabilityPresenter.StabilityTestConfig();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stability, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupButtons();

        mStabilityPresenter = new StabilityPresenter(this, Injection.provideMiDiDataRepository(this.getActivity().getApplicationContext()));
        mStabilityPresenter.start(this.getContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                int badColor = activity.getResources().getColor(R.color.text_disabled);
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
                String s = String.format("收到的MIDI数据长度为 %d\n", data.length);
                for (int i = 0; i < data.length; ++i) {
                    s += String.format("0x%x ", data[i]);
                }
                mMidiDataInfoTextView.setText(s);
            }
        });
    }

    @Override
    public void updateLogInfo(String fullPath, int size) {
    }

    /**
     * Process button related events
     */
    private void setupButtons() {
        final Activity activty = this.getActivity();

        // Start & stop
        mStartStopBtn = (ToggleButton)activty.findViewById(R.id.start_stop_btn);
        mStartStopBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startStopTest();
            }
        });

        mDeviceStatusTextView = (TextView)activty.findViewById(R.id.device_status_textview);

        // Test send times
        mTimesSpinner = (Spinner)activty.findViewById(R.id.times_spinner);
        mTimesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int[] times = getResources().getIntArray(R.array.times);

                mTestConfg.setTestTimes(times[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Test time gap
        mTimeGapSpinner =  (Spinner)activty.findViewById(R.id.time_gap_spinner);
        mTimeGapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int[] timeGap = getResources().getIntArray(R.array.time_gap);

                mTestConfg.setTestTimeGap(timeGap[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Connect & Disconnect test
        mConnectDisconnectCb = (CheckBox)activty.findViewById(R.id.connect_disconnect_cb);
        mConnectDisconnectCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mTestConfg.setConnectTest(b);
            }
        });

        // Get device info test
        mDeviceInfoCb = (CheckBox)activty.findViewById(R.id.device_info_cb);
        mDeviceInfoCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mTestConfg.setDevInfoTest(b);
            }
        });

        // Light all on/off
        mLightAllOnOffCb = (CheckBox)activty.findViewById(R.id.light_all_on_off_cb);
        mLightAllOnOffCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mTestConfg.setLightAllTest(b);
            }
        });

        mLightOnOffCb = (CheckBox)activty.findViewById(R.id.light_on_off_cb);
        mLightOnOffCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mTestConfg.setLightTest(b);
            }
        });

        // Log info
        mLogInfoTextView = (TextView)activty.findViewById(R.id.log_info_textview);

        // Send/Receive midi data
        mMidiDataInfoTextView = (TextView)activty.findViewById(R.id.midi_info_textview);

        initDefaultConfig();
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
    }

    /**
     * start/stop test
     */
    private void startStopTest() {
        mStabilityPresenter.startStopText(mTestConfg);
    }
}
