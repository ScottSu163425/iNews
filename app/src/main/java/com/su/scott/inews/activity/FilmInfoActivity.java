package com.su.scott.inews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.su.scott.inews.R;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.bean.SearchFilmResultBean;
import com.su.scott.inews.constant.Constant;
import com.su.scott.inews.http.CustomRequest;
import com.su.scott.inews.iamge.BitmapCache;
import com.su.scott.inews.util.AnimUtil;
import com.su.scott.inews.util.JsonUtil;
import com.su.scott.inews.util.Snack;
import com.su.scott.inews.util.StringUtil;
import com.su.scott.inews.util.Tools;
import com.su.scott.inews.util.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * @类名 FilmInfoActivity
 * @描述 影片详情Activity
 * @作者 Su
 * @时间
 */
public class FilmInfoActivity extends BaseActivity {
    private Toolbar mToolbar;
    private SearchFilmResultBean mFilmBean;
    private ImageView mCoverIv;
    private FloatingActionButton mCollecteFb;
    private TextView mFilmNameTv, mDirectorTv, mActorsTv, mTypeTv, mYearTv, mAreaTv, mRatingTv, mDescTv;
    private LinearLayout mActorsLayout, mRecommendLayout;
    private View mActorsView, mRecommendView;
    private ImageView mTopAreaIv;

    private List<ActorHolder> mActorHolders;
    private List<RecommendHolder> mRecommendHolders;
    private BitmapCache mBitmapCache;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_film_info;
    }

    @Override
    protected void initPreData() {
        mFilmBean = (SearchFilmResultBean) getIntent().getSerializableExtra("FILM_BEAN");
    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_filminfo);
        mToolbar.setTitle(mFilmBean.getTitle());
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
        mCoverIv = (ImageView) findViewById(R.id.iv_cover_filminfo);
        mCollecteFb = (FloatingActionButton) findViewById(R.id.fb_collecte_film_info);
        mTopAreaIv = (ImageView) findViewById(R.id.iv_top_area_filminfo);
        mFilmNameTv = (TextView) findViewById(R.id.tv_name_filminfo);
        mDirectorTv = (TextView) findViewById(R.id.tv_director_filminfo);
        mActorsTv = (TextView) findViewById(R.id.tv_actors_filminfo);
        mTypeTv = (TextView) findViewById(R.id.tv_type_filminfo);
        mYearTv = (TextView) findViewById(R.id.tv_year_filminfo);
        mAreaTv = (TextView) findViewById(R.id.tv_area_filminfo);
        mRatingTv = (TextView) findViewById(R.id.tv_rating_filminfo);
        mDescTv = (TextView) findViewById(R.id.tv_desc_filminfo);
        mActorsLayout = (LinearLayout) findViewById(R.id.ll_layout_actors_filminfo);
        mRecommendLayout = (LinearLayout) findViewById(R.id.ll_layout_recommends_filminfo);
        mActorsView = findViewById(R.id.card_layout_actors_filminfo);
        mRecommendView = findViewById(R.id.card_layout_recommends_filminfo);
        setContent();
    }

    private void setContent() {
        if (null == mFilmBean) {
            return;
        }

        setDescInfo();
    }


    /**
     * 简介信息填充
     */
    private void setDescInfo() {
        UIUtil.setText(mFilmNameTv, mFilmBean.getTitle());
        UIUtil.setText(mDirectorTv, mFilmBean.getDir(), "暂无信息");
        UIUtil.setText(mActorsTv, mFilmBean.getAct(), "暂无信息");
        UIUtil.setText(mTypeTv, mFilmBean.getTag(), "暂无信息");
        UIUtil.setText(mYearTv, mFilmBean.getYear(), "暂无信息");
        UIUtil.setText(mAreaTv, mFilmBean.getArea(), "暂无信息");
        UIUtil.setText(mRatingTv, mFilmBean.getRating(), "暂无评分");
        UIUtil.setText(mDescTv, StringUtil.addBeginSpace(mFilmBean.getDesc(), 4), "暂无简介");
    }

    @Override
    protected void initData() {
        if (null == mFilmBean) {
            return;
        }
        mBitmapCache = new BitmapCache();
        mImageLoader = new ImageLoader(Volley.newRequestQueue(this), mBitmapCache);

        setUpCover();
        setUpActorsInfo();
        setUpRecommendInfo();
    }

    private void setUpCover() {
        loadImage(mCoverIv, mFilmBean.getCover());
        loadImage(mTopAreaIv, mFilmBean.getCover());
    }

    private void loadImage(ImageView iv, String url) {
        if (null != iv) {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, R.color.grey_light, R.drawable.ic_load_error);
            if (null != url) {
                mImageLoader.get(url, listener);
            }
        }
    }

    private void setUpActorsInfo() {
        List<SearchFilmResultBean.Act_s> actorList = mFilmBean.getAct_s();
        if ((null == actorList) || (actorList.size() == 0)) {
            mActorsView.setVisibility(View.INVISIBLE);
        } else {
            mActorsView.setVisibility(View.VISIBLE);
            (findViewById(R.id.tv_tip1_filminfo)).setVisibility(View.VISIBLE);
            mActorHolders = new ArrayList<>();

            for (int i = 0; i < actorList.size(); i++) {
                SearchFilmResultBean.Act_s actor = actorList.get(i);
                ActorHolder holder = new ActorHolder();

                View layout = getLayoutInflater().inflate(R.layout.layout_actor, null);
                holder.layout = layout;
                holder.headerIv = (ImageView) layout.findViewById(R.id.iv_header_actior_layout);
                holder.nameTv = (TextView) layout.findViewById(R.id.tv_name_actor_layout);
                holder.name = actor.getName();
                holder.headerUrl = actor.getImage();
                holder.detailUrl = actor.getUrl();
                holder.nameTv.setText(holder.name);
                mActorHolders.add(holder);
                /*动态加入界面布局*/
                mActorsLayout.addView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                /*异步加载头像*/
                loadImage(holder.headerIv, holder.headerUrl);
            }
        }
    }

    private void setUpRecommendInfo() {
        List<SearchFilmResultBean.Video_rec> recList = mFilmBean.getVideo_rec();
        if ((null == recList) || (recList.size() == 0)) {
            mRecommendView.setVisibility(View.INVISIBLE);
        } else {
            mRecommendView.setVisibility(View.VISIBLE);
            (findViewById(R.id.tv_tip2_filminfo)).setVisibility(View.VISIBLE);
            mRecommendHolders = new ArrayList<>();

            for (int i = 0; i < recList.size(); i++) {
                SearchFilmResultBean.Video_rec video_rec = recList.get(i);
                RecommendHolder holder = new RecommendHolder();

                View layout = getLayoutInflater().inflate(R.layout.layout_recommend_film, null);
                holder.layout = layout;
                holder.coverIv = (ImageView) layout.findViewById(R.id.iv_cover_recommend_film_layout);
                holder.nameTv = (TextView) layout.findViewById(R.id.tv_name_recommend_film_layout);
                holder.name = video_rec.getTitle();
                holder.coverUrl = video_rec.getCover();
                holder.detailUrl = video_rec.getDetail_url();
                holder.nameTv.setText(holder.name);
                mRecommendHolders.add(holder);
                /*动态加入界面布局*/
                mRecommendLayout.addView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                /*异步加载头像*/
                loadImage(holder.coverIv, holder.coverUrl);
            }
        }
    }

    @Override
    protected void initListener() {
        mCollecteFb.setOnClickListener(this);

        if (null != mActorHolders) {
            for (int i = 0; i < mActorHolders.size(); i++) {
                final ActorHolder holder = mActorHolders.get(i);
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Tools.isFastClick()) {
                            return;
                        }

                        if (null != holder.detailUrl) {
                            Intent intent = new Intent(FilmInfoActivity.this, WebViewActivity.class);
                            intent.putExtra("URL", holder.detailUrl);
                            goTo(intent);
                        } else {
                            /*演员详情URL为空*/
                            Snack.showShort(v, "演员详情页连接不存在");
                        }
                    }
                });
            }
        }

        if (null != mRecommendHolders) {
            for (int i = 0; i < mRecommendHolders.size(); i++) {
                final RecommendHolder holder = mRecommendHolders.get(i);
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* if (null != holder.detailUrl) {
                            Intent intent = new Intent(FilmInfoActivity.this, WebViewActivity.class);
                            intent.putExtra("URL", holder.detailUrl);
                            goTo(intent);
                        } else {
                            *//*演员详情URL为空*//*
                            Snack.showShort(v, "演员详情页连接不存在");
                        }*/
                        if (null != holder.name) {
                            requestFilmInfo(holder.name);
                        }

                    }
                });
            }
        }

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
                        Intent intent = new Intent(FilmInfoActivity.this, FilmInfoActivity.class);
                        Bundle data = new Bundle();
                        data.putSerializable("FILM_BEAN", bean);
                        intent.putExtras(data);
                        goTo(intent);
                    } else {
                        Snack.showShort(mActorsLayout, "未检索到相关影片");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissPd();
            }

            @Override
            public void onFailed(String errorMsg) {
                Snack.showShort(mActorsLayout, getString(R.string.tip_error_check_connection));
                dismissPd();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (R.id.fb_collecte_film_info == id) {
//            AnimUtil.transformColor(mCollecteFb, getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.md_red_500), 800);
            AnimUtil.pulse(mCollecteFb);
        }
    }

    private class ActorHolder {
        public View layout;
        public ImageView headerIv;
        public TextView nameTv;
        public String name;
        public String headerUrl;
        public String detailUrl;
    }

    private class RecommendHolder {
        public View layout;
        public ImageView coverIv;
        public TextView nameTv;
        public String name;
        public String coverUrl;
        public String detailUrl;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
