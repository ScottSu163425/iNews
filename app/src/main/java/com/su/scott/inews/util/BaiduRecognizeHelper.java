package com.su.scott.inews.util;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

/**
 * 百度语音识别帮助类
 */
public class BaiduRecognizeHelper {
    public static final String API_KEY = "r7DZq3N6X5EY17jIGWLDycWM";

    public static final String SECRET_KEY = "122e1541363398f434316eda8ba90214";

    /**
     * 对话框样式
     */
    public static int DIALOG_THEME = BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG;

    /**
     * 当前垂直领域类型
     */
    public static int CURRENT_PROP = VoiceRecognitionConfig.PROP_INPUT;

    /**
     * 当前识别语言
     */
    public static String CURRENT_LANGUAGE = VoiceRecognitionConfig.LANGUAGE_CHINESE;


    public static void showRecognizeDialog(Activity context, DialogRecognitionListener listener) {
        Bundle params = new Bundle();
        params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, API_KEY);
        params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, SECRET_KEY);
        params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, DIALOG_THEME);
        BaiduASRDigitalDialog reconDialog = new BaiduASRDigitalDialog(context, params);
        reconDialog.getParams().putInt(BaiduASRDigitalDialog.PARAM_PROP, CURRENT_PROP);
        reconDialog.getParams().putString(BaiduASRDigitalDialog.PARAM_LANGUAGE, CURRENT_LANGUAGE);
        reconDialog.setDialogRecognitionListener(listener);
        reconDialog.show();
    }

    public static void showEngRecognizeDialog(Activity context, DialogRecognitionListener listener) {
        Bundle params = new Bundle();
        params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, API_KEY);
        params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, SECRET_KEY);
        params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, DIALOG_THEME);
        BaiduASRDigitalDialog reconDialog = new BaiduASRDigitalDialog(context, params);
        reconDialog.getParams().putInt(BaiduASRDigitalDialog.PARAM_PROP, CURRENT_PROP);
        reconDialog.getParams().putString(BaiduASRDigitalDialog.PARAM_LANGUAGE, VoiceRecognitionConfig.LANGUAGE_ENGLISH);
        reconDialog.setDialogRecognitionListener(listener);
        reconDialog.show();
    }

}
