package com.su.scott.inews.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.su.scott.inews.R;
import com.su.scott.inews.adapter.NewsListAdapter;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.bean.CollectionBean;
import com.su.scott.inews.bean.NewsBean;
import com.su.scott.inews.util.BaiduTTSHelper;
import com.su.scott.inews.util.NetworkUtil;
import com.su.scott.inews.util.Snack;
import com.su.scott.inews.util.Tools;

/**
 * @类名 NewsDetailActivity
 * @描述 新闻详情Activity
 * @作者 Su
 * @时间
 */
public class NewsDetailActivity extends BaseActivity {
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private FloatingActionButton mReadFb;
    private NewsBean mNewsBean;
    private SpeechSynthesizer mTtsClient;
    private boolean isReadingOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWebView();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initData() {
        mTtsClient = BaiduTTSHelper.getTtsClient(this, mTtsListener);
    }

    @Override
    protected void initPreData() {
        mNewsBean = getIntent().getParcelableExtra("NewsBean");
    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar_news_detail);
        mToolbar.setTitle("iNews");
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
        mWebView = (WebView) findViewById(R.id.web_view_news_detail);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progress_newsdetail);
        mReadFb = (FloatingActionButton) findViewById(R.id.fb_read_news_detail);
    }

    private void initWebView() {
        mWebView.loadUrl(mNewsBean.getUrl());

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
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                stopRefresh();

                if (NetworkUtil.isNetworkConnected(NewsDetailActivity.this)) {
                    mReadFb.setVisibility(View.VISIBLE);
                } else {
                    mReadFb.setVisibility(View.GONE);
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
//                mToolbar.setTitle(title);
            }

        });
    }

    @Override
    protected void initListener() {
        mReadFb.setOnClickListener(this);
    }

    private SpeechSynthesizerListener mTtsListener = new SpeechSynthesizerListener() {
        @Override
        public void onStartWorking(SpeechSynthesizer speechSynthesizer) {

        }

        @Override
        public void onSpeechStart(SpeechSynthesizer speechSynthesizer) {
            isReadingOn = true;
        }

        @Override
        public void onNewDataArrive(SpeechSynthesizer speechSynthesizer, byte[] bytes, boolean b) {

        }

        @Override
        public void onBufferProgressChanged(SpeechSynthesizer speechSynthesizer, int i) {

        }

        @Override
        public void onSpeechProgressChanged(SpeechSynthesizer speechSynthesizer, int i) {

        }

        @Override
        public void onSpeechPause(SpeechSynthesizer speechSynthesizer) {
            isReadingOn = false;
        }

        @Override
        public void onSpeechResume(SpeechSynthesizer speechSynthesizer) {
            isReadingOn = true;
        }

        @Override
        public void onCancel(SpeechSynthesizer speechSynthesizer) {
            isReadingOn = false;
        }

        @Override
        public void onSynthesizeFinish(SpeechSynthesizer speechSynthesizer) {

        }

        @Override
        public void onSpeechFinish(SpeechSynthesizer speechSynthesizer) {
            isReadingOn = false;
        }

        @Override
        public void onError(SpeechSynthesizer speechSynthesizer, SpeechError speechError) {
            isReadingOn = false;
            stopReading();
            Snack.showShort(mReadFb, speechError.errorDescription);
        }
    };

    private void startReading(String text) {
        mTtsClient.speak(text);
        isReadingOn = true;
        mReadFb.setImageResource(R.drawable.ic_volume_up_white_24dp);
    }

    private void stopReading() {
        if (null != mTtsClient) {
            mTtsClient.cancel();
            mReadFb.setImageResource(R.drawable.ic_volume_off_white_24dp);
            isReadingOn = false;
        }
    }

    private void saveNewsToDb() {
        try {
            CollectionBean collectionBean = dbUtils.findFirst(Selector.from(CollectionBean.class).where("url", "=", mNewsBean.getUrl()));
            if (null == collectionBean) {
                collectionBean = NewsListAdapter.newsToCollection(mNewsBean);
                dbUtils.save(collectionBean);
                Snack.showShort(mReadFb, "已添加至收藏夹");
            } else {
                Snack.showShort(mReadFb, "收藏夹中已存在");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_favorite_newsdetail == id) {
            saveNewsToDb();
            return true;
        }

        if (R.id.action_refresh_newsdetail == id) {
            mWebView.reload();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (Tools.isFastClick()) {
            return;
        }
//
//        int id = v.getId();
//
//        if (R.id.fb_read_news_detail == id) {
//            if (!isReadingOn) {
//                startReading(mNewsBean.getTitle() + "    " + mNewsBean.getDescription());
//            } else {
//                stopReading();
//            }
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


    @Override
    protected void onPause() {
        super.onPause();
        mTtsClient.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTtsClient.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTtsClient.cancel();
    }

}
