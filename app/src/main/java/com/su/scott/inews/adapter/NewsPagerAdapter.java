package com.su.scott.inews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class NewsPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private List<String> pageTitleList;

    public NewsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
    }

    public NewsPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> pageTitleList) {
        super(fm);
        this.fragmentManager = fm;
        this.fragmentList = fragmentList;
        this.pageTitleList = pageTitleList;
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }


    public void setPageTitleList(List<String> pageTitleList) {
        this.pageTitleList = pageTitleList;
    }

    public List<String> getPageTitleList() {
        return pageTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitleList.get(position);
    }
}
