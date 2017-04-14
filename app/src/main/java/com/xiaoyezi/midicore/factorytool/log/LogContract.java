package com.xiaoyezi.midicore.factorytool.log;

import com.xiaoyezi.midicore.factorytool.base.BasePresenter;
import com.xiaoyezi.midicore.factorytool.base.BaseView;
import com.xiaoyezi.midicore.factorytool.data.FileModel;

import java.util.List;

/**
 * Created by jim on 2017/4/10.
 */
public interface LogContract {
    interface View extends BaseView<Presenter> {
        void showLogs(List<FileModel> logs);
    }

    interface Presenter extends BasePresenter {
    }
}
