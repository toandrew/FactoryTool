package com.xiaoyezi.midicore.factorytool.log;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xiaoyezi.midicore.factorytool.data.MiDiDataRepository;

/**
 * Created by jim on 2017/4/10.
 */
public class LogPresenter implements LogContract.Presenter {
    LogFragment mLogFragment;

    MiDiDataRepository mMiDiDataRepository;

    public LogPresenter(LogFragment fragment, MiDiDataRepository dataRepository) {
        mLogFragment = fragment;
        mMiDiDataRepository = dataRepository;
    }

    @Override
    public void start(@NonNull final Context context) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
