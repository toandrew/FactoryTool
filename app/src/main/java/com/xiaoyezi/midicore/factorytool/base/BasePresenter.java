package com.xiaoyezi.midicore.factorytool.base;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by jim on 2017/4/10.
 */

public interface BasePresenter {
    void start(@NonNull final Context context);
    void stop();
    void resume();
    void pause();
}
