package com.xiaoyezi.midicore.factorytool.stability;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xiaoyezi.midicore.factorytool.data.MiDiDataRepository;

/**
 * Created by jim on 2017/4/10.
 */
public class StabilityPresenter implements StabilityContract.Presenter {
    private StabilityFragment mStabilityFragment;

    private MiDiDataRepository mMiDiDataRepository;

    public StabilityPresenter(StabilityFragment fragment, MiDiDataRepository dataRepository) {
        mStabilityFragment = fragment;
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
