package com.su.scott.inews.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.su.scott.inews.R;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.bean.TranslationResultBean;
import com.su.scott.inews.constant.Constant;
import com.su.scott.inews.http.CustomRequest;
import com.su.scott.inews.util.AnimUtil;
import com.su.scott.inews.util.BaiduRecognizeHelper;
import com.su.scott.inews.util.BaiduTTSHelper;
import com.su.scott.inews.util.JsonUtil;
import com.su.scott.inews.util.Snack;
import com.su.scott.inews.util.StringUtil;
import com.su.scott.inews.util.T;
import com.su.scott.inews.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @类名 TranslationActivity
 * @描述 文本互译Activity
 * @作者 Su
 * @时间
 */
public class TranslationActivity extends BaseActivity {
    private Toolbar mToolbar;
    private AppCompatSpinner mFromSpinner, mToSpinner;
    private View mOutputLayout;
    private EditText mInputEt, mOutputEt;
    private FloatingActionButton mTranslateFb;
    private ImageButton mSpeakImgBtn, mReadImgBtn;
    private SpeechSynthesizer mTtsClient;
    private MyDialogRecognitionListener mDialogRecognitionListener;
    private static final String[] LANGUAGE_NAME = new String[]{"中文", "英语", "日语", "韩语", "西班牙语", "法语",
            "德语", "意大利语", "荷兰语", "希腊语", "泰语", "阿拉伯语", "俄罗斯语", "葡萄牙语", "白话文", "文言文", "粤语"};

    private static final String[] LANGUAGE_FLAG = new String[]{"zh", "en", "jp", "kor", "spa", "fra",
            "de", "it", "nl", "el", "th", "ara", "ru", "p", "zh", "wyw", "yue"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_translation;
    }

    @Override
    protected void initPreData() {

    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_translation);
        mToolbar.setTitle("文本互译");
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
        mFromSpinner = (AppCompatSpinner) findViewById(R.id.spinner_from_translation);
        mToSpinner = (AppCompatSpinner) findViewById(R.id.spinner_to_translation);
        mOutputLayout = findViewById(R.id.card_output_translation);
        mInputEt = (EditText) findViewById(R.id.et_input_translation);
        mOutputEt = (EditText) findViewById(R.id.et_output_translation);
        mTranslateFb = (FloatingActionButton) findViewById(R.id.fb_translate_translation);
        mReadImgBtn = (ImageButton) findViewById(R.id.imgbtn_read_translation);
        mSpeakImgBtn = (ImageButton) findViewById(R.id.imgbtn_speak_transltaion);
    }

    @Override
    protected void initData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, LANGUAGE_NAME);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFromSpinner.setAdapter(adapter);
        mToSpinner.setAdapter(adapter);
        mToSpinner.setSelection(1);
        mTtsClient = BaiduTTSHelper.getTtsClient(this,null);
        mDialogRecognitionListener = new MyDialogRecognitionListener();
    }


    @Override
    protected void initListener() {
        /*与外层ScrollView滚动并存*/
        mInputEt.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        mTranslateFb.setOnClickListener(this);
        mSpeakImgBtn.setOnClickListener(this);
        mReadImgBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (Tools.isFastClick()) {
            return;
        }

        int id = v.getId();

        if (R.id.fb_translate_translation == id) {
            translate(mInputEt, LANGUAGE_FLAG[mFromSpinner.getSelectedItemPosition()], LANGUAGE_FLAG[mToSpinner.getSelectedItemPosition()]);
        } else if (R.id.imgbtn_speak_transltaion == id) {
            speak();
        } else if (R.id.imgbtn_read_translation == id) {
            String targetLang = LANGUAGE_NAME[mToSpinner.getSelectedItemPosition()];
            if (!("中文".equals(targetLang) || ("英语".equals(targetLang)))) {
                Snack.showShort(v, targetLang + "不支持语音朗读");
                return;
            }
            read(mOutputEt.getText().toString().trim());
        }
    }

    private void speak() {
        String fromType = LANGUAGE_FLAG[mFromSpinner.getSelectedItemPosition()];
        if ("en".equals(fromType)) {
            //英语识别
            BaiduRecognizeHelper.showEngRecognizeDialog(TranslationActivity.this, mDialogRecognitionListener);
        } else {
            BaiduRecognizeHelper.showRecognizeDialog(TranslationActivity.this, mDialogRecognitionListener);
        }
    }

    private class MyDialogRecognitionListener implements DialogRecognitionListener {

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> rs = results != null ? results
                    .getStringArrayList(RESULTS_RECOGNITION) : null;
            if (rs != null && rs.size() > 0) {
                /*屏蔽末尾结束符号*/
                String res = rs.get(0).replace("。", "").replace(".", "").trim();
                mInputEt.setText(res);
                translate(mInputEt, LANGUAGE_FLAG[mFromSpinner.getSelectedItemPosition()], LANGUAGE_FLAG[mToSpinner.getSelectedItemPosition()]);
            }
        }
    }

    private void read(String text) {
        int res = mTtsClient.speak(text);
        if (res < 0) {
            T.showShort(this, "朗读失败");
        }
    }

    private void stopReading() {
        if (null != mTtsClient) {
            mTtsClient.cancel();
        }
    }

    private void translate(EditText inputEt, String from, String to) {
        String input = inputEt.getText().toString().trim();

        if (TextUtils.isEmpty(input)) {
            Snack.showShort(inputEt, "输入不能为空");
            AnimUtil.shakeLeft(inputEt);
            return;
        }
        translateRequest(StringUtil.toURLEncoded(input), from, to);//Url编码
    }

    private void translateRequest(String input, String fromFlag, String toFlag) {
//        showPd();
        CustomRequest.getInstance(this).getString(requestQueue,TAG,Constant.URL_TRANSLATION, new CustomRequest.CustomParam().put("apikey", Constant.API_KEY_APISTORE), new CustomRequest.CustomParam().put("query", input).put("from", fromFlag).put("to", toFlag), new CustomRequest.SimpleListener() {
            @Override
            public void onSuccess(String response) {
                dismissPd();
                try {
                    JSONObject result = new JSONObject(response).getJSONObject("retData").getJSONArray("trans_result").getJSONObject(0);
                    TranslationResultBean resultBean = JsonUtil.jsonToBean(result.toString(), TranslationResultBean.class);

                    if (View.INVISIBLE == mOutputLayout.getVisibility()) {
                        mOutputLayout.setVisibility(View.VISIBLE);
//                        AnimUtil.rotateX(mOutputLayout, 90, 0, 600, new OvershootInterpolator(5.0f));
                        AnimUtil.inWestOvershot(getApplicationContext(), mOutputLayout,800);
                        mOutputLayout.setVisibility(View.VISIBLE);
                    } else {
//                        AnimUtil.rotateX(mOutputLayout, 0, -360, 600, new DecelerateInterpolator());
                        AnimUtil.pulseBounce(mOutputLayout);
                    }
                    mOutputEt.setText(resultBean.getDst());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                Snack.showShort(mInputEt, getString(R.string.tip_error_check_connection));

                dismissPd();
            }
        });

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
