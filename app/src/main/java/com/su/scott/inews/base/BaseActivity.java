package com.su.scott.inews.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.DbUtils;
import com.su.scott.inews.R;
import com.su.scott.inews.constant.Constant;
import com.su.scott.inews.manager.AppManager;
import com.su.scott.inews.util.T;

/**
 * Activity公共基类
 * onCreate中方法执行顺序：initPreData -> initToolbar -> initView -> initData -> initListener
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public static RequestQueue requestQueue;

    protected static DbUtils dbUtils;

    private ProgressDialog progressDialog;

    private static final String MSG_LOADING_DEFAULT = "加载中...";
    public static final String TAG = "request_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentLayout());
        AppManager.getInstance().pushActivity(this);

        initBase();
        initPreData();
        initToolbar();
        initView();
        initData();
        initListener();
    }

    private void initBase() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(MSG_LOADING_DEFAULT);

        if (null == dbUtils) {
            dbUtils = DbUtils.create(this);
        }

        if (null == requestQueue) {
            requestQueue = MyApplication.requestQueue;
        }
    }

    /**
     * 子类不可再调用setContentView(int)
     *
     * @return
     */
    protected abstract int getContentLayout();

    protected abstract void initPreData();

    protected abstract void initToolbar();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();


    protected void showPd() {
        if ((null != progressDialog) && (!progressDialog.isShowing())) {
            progressDialog.show();
        }
    }

    protected void showPd(CharSequence msg) {
        if ((null != progressDialog) && (!progressDialog.isShowing())) {
            progressDialog.setMessage(msg);
            progressDialog.show();
        }
    }

    protected void dismissPd() {
        if ((null != progressDialog) && (progressDialog.isShowing())) {
            progressDialog.dismiss();
        }
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static DbUtils getDbUtils() {
        return dbUtils;
    }

    protected void goTo(Activity from, Class<?> to) {
        startActivity(new Intent(from, to));
    }

    protected void goTo(Class<?> to) {
        startActivity(new Intent(this, to));
    }

    protected void goTo(Intent intent) {
        startActivity(intent);
    }

    protected void goToAndFinish(Activity from, Class<?> to) {
        startActivity(new Intent(from, to));
        from.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().popActivity(this);
        if (null != requestQueue) {
            requestQueue.cancelAll(TAG);
        }
    }

}
