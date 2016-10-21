package com.su.scott.inews.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.su.scott.inews.R;
import com.su.scott.inews.activity.FilmInfoActivity;
import com.su.scott.inews.adapter.FilmRankListAdapter;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.base.BaseFragment;
import com.su.scott.inews.bean.FilmRankBean;
import com.su.scott.inews.bean.HistoryTodayBean;
import com.su.scott.inews.bean.SearchFilmResultBean;
import com.su.scott.inews.constant.Constant;
import com.su.scott.inews.http.CustomRequest;
import com.su.scott.inews.util.AnimUtil;
import com.su.scott.inews.util.JsonUtil;
import com.su.scott.inews.util.Snack;
import com.su.scott.inews.util.StringUtil;
import com.su.scott.inews.util.T;
import com.su.scott.inews.util.Tools;
import com.su.scott.inews.util.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @类名 FilmRankFragment
 * @描述 票房排行Fragment
 * @作者 Su
 * @时间 2015-12-28
 */
public class FilmRankFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private View mEmptyLayout;
    private SwipeRefreshLayout mRefreshLayout;
    private List<FilmRankBean> mDataList;
    private FilmRankListAdapter mAdapter;

    private String type;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_film_rank;
    }

    @Override
    protected void initPreData() {

    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) rootLayout.findViewById(R.id.rv_filmrank_fragment);
        mEmptyLayout = rootLayout.findViewById(R.id.rl_empty_content);
        mRefreshLayout = (SwipeRefreshLayout) rootLayout.findViewById(R.id.refresh_layout_fragment_filmrank);
        mRefreshLayout.setColorScheme(R.color.colorPrimary, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }

    @Override
    protected void initData() {
        mDataList = new ArrayList<>();
        mAdapter = new FilmRankListAdapter(activity);
        mAdapter.setBeanList(mDataList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        requestRankList(true, type);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener(new FilmRankListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Tools.isFastClick()) {
                    return;
                }
                requestFilmInfo(mAdapter.getBeanList().get(position).getName());
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestRankList(false, type);
            }
        });
    }


    public String getType() {
        return type;
    }

    public FilmRankFragment setType(String type) {
        this.type = type;
        return this;
    }

    private void requestRankList(boolean needShowPd, final String type) {
        if (needShowPd) {
            showPd();
        }
        CustomRequest.CustomParam param = new CustomRequest.CustomParam();
        param.put("area", type);
        param.put("key", Constant.API_KEY_FILM_RANK);

        CustomRequest.getInstance(activity).getString(BaseActivity.requestQueue, BaseActivity.TAG, Constant.URL_FILM_RANK, null, param, new CustomRequest.SimpleListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final List<FilmRankBean> temp = JsonUtil.jsonToList(jsonObject.getJSONArray("result").toString(), FilmRankBean.class);
                    if (null != temp) {
                        for (int i = 0; i < temp.size(); i++) {
                            FilmRankBean bean = temp.get(i);
                            bean.setType(type);
                        }

                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    final List<FilmRankBean> cacheList = dbUtils.findAll(Selector.from(FilmRankBean.class).where("type", "=", type));

                                    if (null != cacheList) {
                                        dbUtils.delete(FilmRankBean.class, WhereBuilder.b("type", "=", type));
                                    }
                                    dbUtils.saveAll(temp);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        }.execute();

                        mDataList.clear();
                        mDataList.addAll(temp);
                        mAdapter.notifyDataSetChanged();

                        if (0 == temp.size()) {
                            UIUtil.setViewVisiable(mEmptyLayout);
                        } else {
                            UIUtil.setViewGone(mEmptyLayout);
                        }
                    }
                    dismissPd();
                    stopRefresh();
                } catch (Exception e) {
                    T.showShort(activity, "数据处理异常");
                    e.printStackTrace();
                    dismissPd();
                    stopRefresh();
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                Snack.showShort(mRecyclerView, getString(R.string.tip_error_check_connection));
                stopRefresh();
                dismissPd();
                if ((null != mDataList) && (0 == mDataList.size())) {
                    try {
                        List<FilmRankBean> cacheList = dbUtils.findAll(Selector.from(FilmRankBean.class).where("type", "=", type));
                        if ((null == cacheList) || (cacheList.size() == 0)) {
                            UIUtil.setViewVisiable(mEmptyLayout);
                        } else {
                            mDataList.addAll(cacheList);
                            mAdapter.notifyDataSetChanged();
                        }

                    } catch (DbException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    private void stopRefresh() {
        UIUtil.stopSwipeRefresh(mRefreshLayout);
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

        CustomRequest.getInstance(activity).getString(BaseActivity.requestQueue, BaseActivity.TAG, Constant.URL_SEARCH_FILM_INFO, null, param, new CustomRequest.SimpleListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if ("0".equals(object.getString("error_code"))) {
                        SearchFilmResultBean bean = JsonUtil.jsonToBean(object.getJSONObject("result").toString(), SearchFilmResultBean.class);
                        Intent intent = new Intent(activity, FilmInfoActivity.class);
                        Bundle data = new Bundle();
                        data.putSerializable("FILM_BEAN", bean);
                        intent.putExtras(data);
                        goTo(intent);
                    } else {
                        Snack.showShort(mRecyclerView, "未检索到相关影片");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismissPd();
            }

            @Override
            public void onFailed(String errorMsg) {
                Snack.showShort(mRecyclerView, getString(R.string.tip_error_check_connection));

                dismissPd();
            }
        });
    }
}
