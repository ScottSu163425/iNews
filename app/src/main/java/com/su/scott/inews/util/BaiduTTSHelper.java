package com.su.scott.inews.util;

import android.content.Context;
import android.media.AudioManager;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;


/**
 * Created by Administrator on 2015/12/31.
 */
public class BaiduTTSHelper {
    private static final String APPID = "7574404";
    private static final String APIKEY = "r7DZq3N6X5EY17jIGWLDycWM";
    private static final String SECRETKEY = "122e1541363398f434316eda8ba90214";

    public static SpeechSynthesizer getTtsClient(final Context context, SpeechSynthesizerListener listener) {
        SpeechSynthesizerListener speechSynthesizerListener;
        if (null != listener) {
            speechSynthesizerListener = listener;
        } else {
            speechSynthesizerListener = new SpeechSynthesizerListener() {
                @Override
                public void onStartWorking(SpeechSynthesizer speechSynthesizer) {

                }

                @Override
                public void onSpeechStart(SpeechSynthesizer speechSynthesizer) {

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

                }

                @Override
                public void onSpeechResume(SpeechSynthesizer speechSynthesizer) {

                }

                @Override
                public void onCancel(SpeechSynthesizer speechSynthesizer) {

                }

                @Override
                public void onSynthesizeFinish(SpeechSynthesizer speechSynthesizer) {

                }

                @Override
                public void onSpeechFinish(SpeechSynthesizer speechSynthesizer) {

                }

                @Override
                public void onError(SpeechSynthesizer speechSynthesizer, SpeechError speechError) {
                    T.showShortCenter(context, speechError.errorDescription);
                }
            };
        }
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(context, "", speechSynthesizerListener);
        // 此处需要将setApiKey方法的两个参数替换为你在百度开发者中心注册应用所得到的apiKey和secretKey
        speechSynthesizer.setApiKey(APIKEY, SECRETKEY);
        speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        setParams(speechSynthesizer);

        return speechSynthesizer;
    }

    private static void setParams(SpeechSynthesizer speechSynthesizer) {
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, SpeechSynthesizer.AUDIO_ENCODE_AMR);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85);
//        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_LANGUAGE, SpeechSynthesizer.LANGUAGE_ZH);
//        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_NUM_PRON, "0");
//        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_ENG_PRON, "0");
//        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PUNC, "0");
//        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_BACKGROUND, "0");
//        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_STYLE, "0");
//        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TERRITORY, "0");
    }

}
