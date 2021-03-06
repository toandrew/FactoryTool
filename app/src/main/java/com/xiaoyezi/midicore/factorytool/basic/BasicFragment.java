package com.xiaoyezi.midicore.factorytool.basic;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xiaoyezi.midicore.factorytool.R;
import com.xiaoyezi.midicore.factorytool.base.BaseFragment;
import com.xiaoyezi.midicore.factorytool.data.Injection;
import com.xiaoyezi.midicore.factorytool.data.MiDiDataSource;

/**
 * Created by jianmin on 2017/4/10.
 */
public class BasicFragment extends BaseFragment implements BasicContract.View {
    private static final String TAG = "BasicFragment";

    private int mLightColor; // 当前亮灯的颜色
    private int mPitch;

    private BasicContract.Presenter mBasicPresenter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            Log.w(TAG, "visible!!");
            if (mBasicPresenter != null) {
                Log.w(TAG, "visible!! mBasicPresenter.start");
                mBasicPresenter.start(this.getActivity());
            }
        } else {
            Log.w(TAG, "invisible!!");

            if (mBasicPresenter != null) {
                Log.w(TAG, "invisible!! mBasicPresenter.stop");
                mBasicPresenter.stop();
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
        return inflater.inflate(R.layout.fragment_basic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.w(TAG, "onViewCreated!");
        setupButtons();
        setupPitchEditText();
        setupLightColorRadioButtons();

        mBasicPresenter = new BasicPresenter(this, Injection.provideMiDiDataRepository(this.getActivity().getApplicationContext()));
        mBasicPresenter.start(this.getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.w(TAG, "onResume!");
        mBasicPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.w(TAG, "onPause!");
        mBasicPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mBasicPresenter.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(BasicContract.Presenter presenter) {
        mBasicPresenter = presenter;
    }

    /**
     * Process button related events
     */
    private void setupButtons() {
        // 连接
        Button bt = (Button) this.getActivity().findViewById(R.id.btn_connect);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBasicPresenter.sendMidiEvent(MiDiDataSource.MIDI_COMMAND_CONNECT, 0, 0);
            }
        });

        // 静态信息查询
        bt = (Button) this.getActivity().findViewById(R.id.btn_query_si);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBasicPresenter.sendMidiEvent(MiDiDataSource.MIDI_COMMAND_QUERY_INFO, 0, 0);
            }
        });

        // 全灯亮
        bt = (Button) this.getActivity().findViewById(R.id.btn_all_light_on);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBasicPresenter.sendMidiEvent(MiDiDataSource.MIDI_COMMAND_TURN_ON_ALL, mLightColor, 0);
            }
        });

        // 全灯灭
        bt = (Button) this.getActivity().findViewById(R.id.btn_all_light_off);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBasicPresenter.sendMidiEvent(MiDiDataSource.MIDI_COMMAND_TURN_OFF_ALL, 0, 0);
            }
        });

        // 单灯亮
        bt = (Button) this.getActivity().findViewById(R.id.btn_light_on);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBasicPresenter.sendMidiEvent(MiDiDataSource.MIDI_COMMAND_TURN_ON, mPitch, mLightColor);
            }
        });

        // 单灯灭
        bt = (Button) this.getActivity().findViewById(R.id.btn_light_off);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBasicPresenter.sendMidiEvent(MiDiDataSource.MIDI_COMMAND_TURN_OFF, mPitch, 0);
            }
        });

        // 按键
        bt = (Button) this.getActivity().findViewById(R.id.btn_press_key);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBasicPresenter.sendMidiEvent(MiDiDataSource.MIDI_COMMAND_KEY_PRESS, mPitch, 75);
            }
        });
    }

    /**
     * Process light color
     */
    private void setupLightColorRadioButtons() {
        RadioGroup rg = (RadioGroup) this.getActivity().findViewById(R.id.rg_light_color);
        setLightColor(rg.getCheckedRadioButtonId());

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setLightColor(radioGroup.getCheckedRadioButtonId());
            }
        });
    }

    /**
     * Set light color
     *
     * @param radioBtnId
     */
    private void setLightColor(int radioBtnId) {
        switch (radioBtnId) {
            case R.id.rb_light_red:
                mLightColor = 1;
                return;
            case R.id.rb_light_blue:
                mLightColor = 2;
                return;
            case R.id.rb_light_purple:
                mLightColor = 3;
                return;
        }
    }

    /**
     * Process pitch which will be sent
     */
    private void setupPitchEditText() {
        EditText et = (EditText) this.getActivity().findViewById(R.id.et_pitch);
        setPitch(Integer.parseInt(et.getText().toString()));
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String s = editable.toString();
                    if (!s.isEmpty()) {
                        setPitch(Integer.parseInt(s));
                    }
                } catch (Exception e) {
                    editable.clear();
                    editable.append("" + mPitch);
                }
            }
        });
    }

    /**
     * Set pitch
     *
     * @param p
     */
    private void setPitch(int p) {
        if (p < 1) {
            mPitch = 1;
        } else {
            mPitch = p;
        }
    }

    /**
     * Set device connect status
     *
     * @param b
     */
    @Override
    public void setDeviceConnectionState(final boolean b) {
        final Activity activity = this.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView cb = (TextView) activity.findViewById(R.id.cb_connection);
                int okColor = activity.getResources().getColor(R.color.green_500);
                int badColor = activity.getResources().getColor(R.color.red);
                cb.setTextColor(b ? okColor : badColor);
                cb.setText(b ? "琴已连接" : "琴未连接");
            }
        });
    }

    /**
     * Midi data received
     *
     * @param data
     */
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
                TextView v = (TextView) activity.findViewById(R.id.tv_midi_data);
                v.setText(s);
            }
        });
    }
}
