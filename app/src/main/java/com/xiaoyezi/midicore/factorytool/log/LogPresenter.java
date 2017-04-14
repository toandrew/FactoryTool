package com.xiaoyezi.midicore.factorytool.log;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xiaoyezi.midicore.factorytool.data.FileModel;
import com.xiaoyezi.midicore.factorytool.data.MiDiDataRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jim on 2017/4/10.
 */
public class LogPresenter implements LogContract.Presenter {
    LogFragment mLogFragment;

    MiDiDataRepository mMiDiDataRepository;

    private List<FileModel> mLogs = new ArrayList<FileModel>();

    public LogPresenter(LogFragment fragment, MiDiDataRepository dataRepository) {
        mLogFragment = fragment;
        mMiDiDataRepository = dataRepository;
    }

    @Override
    public void start(@NonNull final Context context) {
        mLogs.clear();
        mLogs.addAll(mMiDiDataRepository.getLogs());
        mLogFragment.showLogs(mLogs);
    }

    @Override
    public void stop() {
        if (mLogs != null) {
            mLogs.clear();
        }
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }
}
