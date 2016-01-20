package com.su.scott.inews.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.DbUtils;
import com.su.scott.inews.R;
import com.su.scott.inews.util.L;

/**
 * Created by Su on 2015/12/16.
 */
public abstract class BaseFragment extends Fragment {
    protected BaseActivity activity;
    protected View rootLayout;
    protected static DbUtils dbUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseActivity) activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null == rootLayout) {
            rootLayout = inflater.inflate(getContentLayout(), container, false);
            initBase();
            initPreData();
            initView();
            initData();
            initListener();
        }

        return rootLayout;
    }

    private void initBase() {
        dbUtils = activity.getDbUtils();
    }

    protected abstract int getContentLayout();

    protected abstract void initPreData();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();


    protected void showPd() {
        activity.showPd();
    }

    protected void showPd(CharSequence msg) {
        activity.showPd(msg);
    }

    protected void dismissPd() {
        activity.dismissPd();
    }


    protected void goTo(Class target) {
        if (null != activity) {
            activity.startActivity(new Intent(activity, target));
        }
    }

    protected void goTo(Intent intent) {
        if (null != activity) {
            activity.startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        //解决Viewpager切换重复加载
        if (null != rootLayout) {
            ((ViewGroup) rootLayout).removeView(rootLayout);
        }

        super.onDestroy();
    }

}
