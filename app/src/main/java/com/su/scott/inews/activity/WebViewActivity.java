package com.su.scott.inews.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.su.scott.inews.R;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.util.NetworkUtil;
import com.su.scott.inews.util.Tools;

/**
 * @类名 WebViewActivity
 * @描述 WebViewActivity
 * @作者 Su
 * @时间
 */
public class WebViewActivity extends BaseActivity {
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private FloatingActionButton mCollectFb;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void initPreData() {
        mUrl = getIntent().getStringExtra("URL");
    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_webview_activity);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();// 返回上一页面;
                } else {
                    finish();
                }
            }
        });
    }


    @Override
    protected void initView() {
        mWebView = (WebView) findViewById(R.id.web_view_webview_activity);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress_webview_activity);
        mCollectFb = (FloatingActionButton) findViewById(R.id.fb_webview_activity);

    }

    @Override
    protected void initData() {
        initWebView();
    }

    private void initWebView() {
        mWebView.loadUrl(mUrl);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);


        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                stopRefresh();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                stopRefresh();
                if (NetworkUtil.isNetworkConnected(WebViewActivity.this)) {
                    mCollectFb.setVisibility(View.VISIBLE);
                } else {
                    mCollectFb.setVisibility(View.GONE);
                }
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                showMiddleTv(title);
                mToolbar.setTitle(title);
            }

        });
    }

    @Override
    protected void initListener() {
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mWebView.reload();
//            }
//        });

        mCollectFb.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (R.id.action_refresh_single == id) {
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        mWebView.reload();
    }

    @Override
    public void onClick(View v) {
        if (Tools.isFastClick()) {
            return;
        }
    }


    private void stopRefresh() {
//        if (null == mSwipeRefreshLayout) {
//            return;
//        }
//
//        if (mSwipeRefreshLayout.isRefreshing()) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
    }

    // 改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();// 返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
