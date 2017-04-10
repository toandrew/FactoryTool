package com.xiaoyezi.midicore.factorytool.data;

/**
 * Created by jim on 2017/4/10.
 */
public class Injection {
    public static MiDiDataRepository provideMiDiDataRepository() {
        return MiDiDataRepository.getInstance();
    }
}
