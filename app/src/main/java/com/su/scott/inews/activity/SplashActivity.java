package com.su.scott.inews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.su.scott.inews.R;
import com.su.scott.inews.util.AnimUtil;
import com.su.scott.inews.util.UIUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        overridePendingTransition(R.anim.in_scale_out, R.anim.out_alpha);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 500);

    }

    private void init() {
        TextView mAppNameTv, mCopyRightTv;
        mAppNameTv = (TextView) findViewById(R.id.tv_appname_splash);
        mCopyRightTv = (TextView) findViewById(R.id.tv_copyright_splash);
        UIUtil.setViewVisiable(mAppNameTv);
        UIUtil.setViewVisiable(mCopyRightTv);

        AnimUtil.rotateX(mAppNameTv, 90, 0, 900, new BounceInterpolator());
        Animation a = AnimUtil.inEastOvershot(this, mCopyRightTv, 500);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.in_east, R.anim.out_west);
                        finish();
                    }
                }, 1500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (keyCoder == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCoder, event);
    }

}