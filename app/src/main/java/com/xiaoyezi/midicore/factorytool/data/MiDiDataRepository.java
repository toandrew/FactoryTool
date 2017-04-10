package com.xiaoyezi.midicore.factorytool.data;

/**
 * Created by jim on 2017/4/10.
 */
public class MiDiDataRepository implements MiDiDataSource {
    private static MiDiDataRepository sMiDiDataRepository = new MiDiDataRepository();

    private MiDiDataRepository() {
    }

    public static MiDiDataRepository getInstance() {
        return sMiDiDataRepository;
    }
}
