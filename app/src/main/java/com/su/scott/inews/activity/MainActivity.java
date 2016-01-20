package com.su.scott.inews.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.su.scott.inews.R;
import com.su.scott.inews.adapter.NewsPagerAdapter;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.bean.MyEvent;
import com.su.scott.inews.fragment.MenuFragment;
import com.su.scott.inews.fragment.NewsListFragment;
import com.su.scott.inews.iamge.LruBitmapCache;
import com.su.scott.inews.manager.AppManager;
import com.su.scott.inews.util.Snack;
import com.su.scott.inews.util.Tools;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * @类名 MainActivity
 * @描述 主页Activity
 * @作者 Su
 * @时间
 */
public class MainActivity extends BaseActivity implements MenuFragment.IMenuItemClickListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private TabLayout mPagerTabLayout;
    private ViewPager mNewsViewPager;
    private FloatingActionButton mScrollToTopFb;
    private FragmentManager mFragmentManager;
    private MenuFragment mMenuFragment;
    private List<Fragment> mFragmentList;
    private List<String> mPageTitleList;
    private NewsPagerAdapter mPagerAdapter;
    private Animation mFbInAnim, mFbOutAnim;

    private static final int[] START_PAGES = new int[]{
            1, 5, 10, 15
    };/*区分4个新闻页面*/
    public static final String FLAG_SHOW_PB = "MainActivity_Show_Pb";
    public static final String FLAG_HIDE_PB = "MainActivity_Hide_Pb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPreData() {

    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_main);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mPagerTabLayout = (TabLayout) findViewById(R.id.tab_indicator_main);
        mNewsViewPager = (ViewPager) findViewById(R.id.view_pager_main);
        mScrollToTopFb = (FloatingActionButton) findViewById(R.id.fb_main);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void initData() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        mPageTitleList = new ArrayList<>();
        mMenuFragment = new MenuFragment();

        mFragmentList.add(new NewsListFragment().setmStartPage(START_PAGES[0]));
        mFragmentList.add(new NewsListFragment().setmStartPage(START_PAGES[1]));
        mFragmentList.add(new NewsListFragment().setmStartPage(START_PAGES[2]));
        mFragmentList.add(new NewsListFragment().setmStartPage(START_PAGES[3]));
        mPageTitleList.add("社会");
        mPageTitleList.add("国际");
        mPageTitleList.add("娱乐");
        mPageTitleList.add("体育");
        mPagerAdapter = new NewsPagerAdapter(mFragmentManager, mFragmentList, mPageTitleList);
        mNewsViewPager.setAdapter(mPagerAdapter);
//        mNewsViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        mMenuFragment.setMenuItemClickListener(this);
        mFragmentManager.beginTransaction().replace(R.id.fl_container_menu_main, mMenuFragment).commit();
        mPagerTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mPagerTabLayout.setupWithViewPager(mNewsViewPager);
//        mNewsViewPager.setOffscreenPageLimit(mPageTitleList.size());
        mFbInAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.in_south);
        mFbOutAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.out_south);
    }

    @Override
    protected void initListener() {
        mScrollToTopFb.setOnClickListener(this);
    }

    private long lastRecordTime;

    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (keyCoder == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
                return true;
            } else {
                if ((System.currentTimeMillis() - lastRecordTime) > 2000) {
                    Snack.showShort(mDrawerLayout, "再按一次退出应用", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            return;
                        }
                    });
                    lastRecordTime = System.currentTimeMillis();
                } else {
                    AppManager.getInstance().finishAll();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCoder, event);
    }

    @Override
    public void onMenuItemClick() {

    }

    public void onEventMainThread(MyEvent event) {
        if ("MainActivity".equals(event.getTag())) {
            String flag = event.getFlag();
            if (FLAG_SHOW_PB.equals(flag)) {
                showFb();
            } else if (FLAG_HIDE_PB.equals(flag)) {
                hideFb();
            }
        }
    }

    private void showFb() {
        if (mScrollToTopFb.getVisibility() == View.VISIBLE) {
            return;
        }
        mScrollToTopFb.setVisibility(View.VISIBLE);
        mScrollToTopFb.startAnimation(mFbInAnim);
    }

    private void hideFb() {
        if (mScrollToTopFb.getVisibility() != View.VISIBLE) {
            return;
        }
        mScrollToTopFb.startAnimation(mFbOutAnim);
        mScrollToTopFb.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (Tools.isFastClick()) {
            return;
        }

        int id = v.getId();

        if (R.id.fb_main == id) {
            EventBus.getDefault().post(new MyEvent(START_PAGES[mNewsViewPager.getCurrentItem()] + ""));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LruBitmapCache.getInstance().clearCache();
    }

}
