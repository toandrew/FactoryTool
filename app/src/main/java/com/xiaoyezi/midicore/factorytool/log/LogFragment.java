package com.xiaoyezi.midicore.factorytool.log;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.xiaoyezi.midicore.factorytool.R;
import com.xiaoyezi.midicore.factorytool.base.BaseFragment;
import com.xiaoyezi.midicore.factorytool.data.FileModel;
import com.xiaoyezi.midicore.factorytool.data.Injection;
import com.xiaoyezi.midicore.factorytool.log.adapter.LogsAdapter;
import com.xiaoyezi.midicore.factorytool.log.adapter.itemtouchhelper.ItemTouchHelperCallback;

/**
 * Created by jianmin on 2017/4/10.
 */
public class LogFragment extends BaseFragment implements LogContract.View {
    private static final String TAG = "LogFragment";

    Unbinder mUnbinder;

    @BindView(R.id.log_recyclerview)
    RecyclerView mLogRecyclerView;

    @BindView(R.id.no_log_textview)
    TextView mNoLogTextView;

    private LogsAdapter mLogsAdapter;

    private ItemTouchHelper mItemTouchHelper;

    private LogContract.Presenter mLogPresenter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            Log.w(TAG, "visible!!");
            if (mLogPresenter != null && this.getActivity() != null) {
                Log.w(TAG, "visible!!mStabilityPresenter.start!!!");
                mLogPresenter.start(this.getActivity());
            }
        } else {
            Log.w(TAG, "invisible!!");
            if (mLogPresenter != null) {
                Log.w(TAG, "invisible!!mStabilityPresenter.stop!!!");
                mLogPresenter.stop();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        mLogPresenter = new LogPresenter(this, Injection.provideMiDiDataRepository(getActivity().getApplicationContext()));
        mLogPresenter.start(this.getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mLogPresenter.stop();

        mUnbinder.unbind();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        mLogPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mLogPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(LogContract.Presenter presenter) {
        mLogPresenter = presenter;
    }

    @Override
    public void showLogs(List<FileModel> logs) {
        if (logs.size() > 0) {
            mLogRecyclerView.setVisibility(View.VISIBLE);
            mNoLogTextView.setVisibility(View.GONE);
        } else {
            mLogRecyclerView.setVisibility(View.GONE);
            mNoLogTextView.setVisibility(View.VISIBLE);
        }

        if (mLogsAdapter == null) {
            mLogsAdapter = new LogsAdapter(logs);
            mLogRecyclerView.setAdapter(mLogsAdapter);

            ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(mLogsAdapter);
            mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
            mItemTouchHelper.attachToRecyclerView(mLogRecyclerView);
            mLogsAdapter.setTouchHelper(mItemTouchHelper);
        } else {
            mLogsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Init log related view
     */
    private void initView() {
        setupRecyclerView();
    }

    /**
     * Init log recyclerView
     */
    private void setupRecyclerView() {
        mLogRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLogRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), OrientationHelper.VERTICAL));
    }
}
