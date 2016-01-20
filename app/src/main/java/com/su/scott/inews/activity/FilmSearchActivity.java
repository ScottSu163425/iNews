package com.su.scott.inews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.su.scott.inews.R;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.bean.SearchFilmResultBean;
import com.su.scott.inews.constant.Constant;
import com.su.scott.inews.http.CustomRequest;
import com.su.scott.inews.util.AnimUtil;
import com.su.scott.inews.util.BaiduRecognizeHelper;
import com.su.scott.inews.util.JsonUtil;
import com.su.scott.inews.util.Snack;
import com.su.scott.inews.util.StringUtil;
import com.su.scott.inews.util.Tools;
import com.su.scott.inews.widget.KeywordsFlow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * @类名 FilmSearchActivity
 * @描述 影片检索Activity
 * @作者 Su
 * @时间
 */
public class FilmSearchActivity extends BaseActivity {
    private Toolbar mToolbar;
    private EditText mInputEt;
    private FloatingActionButton mChangeFb;
    private CardView mKeywordFlowLayout;
    private KeywordsFlow mKeywordsFlow;
    private ImageButton mSpeakImgBtn, mSearchImgBtn;


    private static final String[] KEY_WORDS = {"老炮儿", "寻龙诀", "万万没想到",
            "赤壁", "钢铁侠", "大侦探福尔摩斯", "泰坦尼克号", "名侦探柯南", "盗梦空间", "珍珠港",
            "忠犬八公", "神话", "捉妖记", "速度与激情7", "碟中谍5", "侏罗纪公园", "肖申克的救赎",
            "致命ID", "我是传奇", "夏洛特烦恼", "银魂", "天空之城", "风之谷", "火星救援",
            "地心引力", "我的少女时代", "复仇者联盟", "黑客帝国", "金刚", "教父", "放牛班的春天",
            "神探夏洛克", "蜘蛛侠"};

    @Override
    protected int getContentLayout() {
        return R.layout.activity_film_search;
    }

    @Override
    protected void initPreData() {
    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_filmsearch);
        mToolbar.setTitle("影视检索");
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
        mKeywordFlowLayout = (CardView) findViewById(R.id.card_key_word_flow_filmsearch);
        mKeywordsFlow = (KeywordsFlow) findViewById(R.id.key_words_flow_filmsearch);
        mInputEt = (EditText) findViewById(R.id.et_input_filmsearch);
        mSearchImgBtn = (ImageButton) findViewById(R.id.imgbtn_search_filmsearch);
        mChangeFb = (FloatingActionButton) findViewById(R.id.fb_change_filmsearch);
        mSpeakImgBtn = (ImageButton) findViewById(R.id.imgbtn_speak_filmsearch);
        mKeywordsFlow.setDuration(600l);
    }

    @Override
    protected void initData() {
        feedKeywordsFlow(mKeywordsFlow, KEY_WORDS);
        mKeywordsFlow.postDelayed(new Runnable() {
            @Override
            public void run() {
                mKeywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
            }
        }, 300);
    }

    @Override
    protected void initListener() {
        mChangeFb.setOnClickListener(this);
        mSearchImgBtn.setOnClickListener(this);
        mSpeakImgBtn.setOnClickListener(this);
        mKeywordsFlow.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tools.isFastClick()) {
                    return;
                }

                if (v instanceof TextView) {
                    String word = ((TextView) v).getText().toString().trim();
                    mInputEt.setText(word);
                    requestFilmInfo(word);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (Tools.isFastClick()) {
            return;
        }

        int id = v.getId();
        if (R.id.fb_change_filmsearch == id) {
            mKeywordsFlow.rubKeywords();
            feedKeywordsFlow(mKeywordsFlow, KEY_WORDS);
            mKeywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
        } else if (R.id.imgbtn_speak_filmsearch == id) {
            speak();
        } else if (R.id.imgbtn_search_filmsearch == id) {
            search(mInputEt);
        }

    }

    private void speak() {
        BaiduRecognizeHelper.showRecognizeDialog(FilmSearchActivity.this, new DialogRecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> rs = results != null ? results
                        .getStringArrayList(RESULTS_RECOGNITION) : null;
                if (rs != null && rs.size() > 0) {
                    String res = rs.get(0).replace("。", "").trim();
                    mInputEt.setText(res);
                    requestFilmInfo(res);
                }
            }
        });
    }


    private void search(EditText et) {
        String input = et.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            AnimUtil.shakeLeft(et);
            Snack.showShort(et, "检索条件不能为空");
            return;
        }
        requestFilmInfo(input);
    }

    /**
     * 检索请求
     *
     * @param keyword
     */
    private void requestFilmInfo(String keyword) {
        showPd(getString(R.string.tips_resquesting));

        CustomRequest.CustomParam param = new CustomRequest.CustomParam();
        param.put("key", Constant.API_KEY_SEARCH_FILM_INFO);
        param.put("q", StringUtil.toURLEncoded(keyword));

        CustomRequest.getInstance(this).getString(requestQueue, TAG, Constant.URL_SEARCH_FILM_INFO, null, param, new CustomRequest.SimpleListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if ("0".equals(object.getString("error_code"))) {
                        SearchFilmResultBean bean = JsonUtil.jsonToBean(object.getJSONObject("result").toString(), SearchFilmResultBean.class);
                        Intent intent = new Intent(FilmSearchActivity.this, FilmInfoActivity.class);
                        Bundle data = new Bundle();
                        data.putSerializable("FILM_BEAN", bean);
                        intent.putExtras(data);
                        goTo(intent);
                    } else {
                        Snack.showShort(mChangeFb, "未检索到相关影片");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissPd();
            }

            @Override
            public void onFailed(String errorMsg) {
                Snack.showShort(mChangeFb, getString(R.string.tip_error_check_connection));
                dismissPd();
            }
        });
    }

    private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
        Random random = new Random();
        for (int i = 0; i < KeywordsFlow.MAX; i++) {
            int ran = random.nextInt(arr.length);
            String tmp = arr[ran];
            keywordsFlow.feedKeyword(tmp);
        }
    }

}
