package com.xiaoyezi.midicore.factorytool.data;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by jim on 2017/4/10.
 */
public class Injection {
    public static MiDiDataRepository provideMiDiDataRepository(@NonNull Context context) {
        return MiDiDataRepository.getInstance(context);
    }
}
