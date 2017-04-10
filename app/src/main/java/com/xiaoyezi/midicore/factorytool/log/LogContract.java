package com.xiaoyezi.midicore.factorytool.log;

import com.xiaoyezi.midicore.factorytool.base.BasePresenter;
import com.xiaoyezi.midicore.factorytool.base.BaseView;

/**
 * Created by jim on 2017/4/10.
 */
public interface LogContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
    }
}
