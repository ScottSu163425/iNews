package com.su.scott.inews.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.su.scott.inews.R;
import com.su.scott.inews.adapter.FilmRankPagerAdapter;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.fragment.FilmRankFragment;
import com.su.scott.inews.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * @类名 FilmRankActivity
 * @描述 影片排行Activity
 * @作者 Su
 * @时间
 */
public class FilmRankActivity extends BaseActivity {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mRankViewPager;
    private FilmRankPagerAdapter mPagerAdapter;

    private List<Fragment> mFragmentList;
    private List<String> mTabTitleList;
    private static final String[] TYPE_RANK = new String[]{"CN", "US", "HK"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_film_rank;
    }

    @Override
    protected void initPreData() {

    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_filmrank);
        mToolbar.setTitle("票房排行");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_indicator_filmrank);
        mRankViewPager = (ViewPager) findViewById(R.id.vp_rank_filmrank);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new FilmRankPagerAdapter(getSupportFragmentManager());
        mFragmentList = new ArrayList<>();
        mTabTitleList = new ArrayList<>();

        mFragmentList.add(new FilmRankFragment().setType(TYPE_RANK[0]));
        mFragmentList.add(new FilmRankFragment().setType(TYPE_RANK[1]));
        mFragmentList.add(new FilmRankFragment().setType(TYPE_RANK[2]));
        mTabTitleList.add("国内");
        mTabTitleList.add("北美");
        mTabTitleList.add("香港");

        mPagerAdapter.setFragmentList(mFragmentList);
        mPagerAdapter.setPageTitleList(mTabTitleList);

        mRankViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mRankViewPager);
    }


    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Tools.isFastClick()) {
            return true;
        }

        int id = item.getItemId();
        if (R.id.action_search_single == id) {
            goTo(FilmSearchActivity.class);
            overridePendingTransition(R.anim.in_north, R.anim.out_alpha);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}
