package com.xiaoyezi.midicore.factorytool;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import com.xiaoyezi.midicore.factorytool.basic.BasicFragment;
import com.xiaoyezi.midicore.factorytool.basic.BasicPresenter;
import com.xiaoyezi.midicore.factorytool.data.Injection;
import com.xiaoyezi.midicore.factorytool.log.LogFragment;
import com.xiaoyezi.midicore.factorytool.log.LogPresenter;
import com.xiaoyezi.midicore.factorytool.stability.StabilityFragment;
import com.xiaoyezi.midicore.factorytool.stability.StabilityPresenter;

public class MainActivity extends AppCompatActivity {
    private static final int BASIC_POSITION     = 0;
    private static final int STABILITY_POSITION = 1;
    private static final int LOG_POSITION       = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
        tab.addView(LayoutInflater.from(this).inflate(R.layout.table_title, tab, false));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

        FragmentPagerItems pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of(getString(R.string.basic_tab), BasicFragment.class));
        pages.add(FragmentPagerItem.of(getString(R.string.stability_tab), StabilityFragment.class));
        pages.add(FragmentPagerItem.of(getString(R.string.log_tab), LogFragment.class));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        //buildRelationship(adapter);
    }

    /**
     * Build relationship between views, presenters & models
     */
    private void buildRelationship(FragmentPagerItemAdapter adapter) {
        BasicFragment basicFragment = (BasicFragment)adapter.getPage(BASIC_POSITION);
        basicFragment.setPresenter(new BasicPresenter(basicFragment, Injection.provideMiDiDataRepository()));

        StabilityFragment stabilityFragment = (StabilityFragment)adapter.getPage(STABILITY_POSITION);
        stabilityFragment.setPresenter(new StabilityPresenter(stabilityFragment, Injection.provideMiDiDataRepository()));

        LogFragment logFragment = (LogFragment)adapter.getPage(LOG_POSITION);
        logFragment.setPresenter(new LogPresenter(logFragment, Injection.provideMiDiDataRepository()));
    }
}
