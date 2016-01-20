package com.su.scott.inews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.su.scott.inews.R;
import com.su.scott.inews.activity.MainActivity;
import com.su.scott.inews.activity.NewsDetailActivity;
import com.su.scott.inews.adapter.NewsListAdapter;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.base.BaseFragment;
import com.su.scott.inews.bean.MyEvent;
import com.su.scott.inews.bean.NewsBean;
import com.su.scott.inews.constant.Constant;
import com.su.scott.inews.http.CustomRequest;
import com.su.scott.inews.util.AnimUtil;
import com.su.scott.inews.util.NetworkUtil;
import com.su.scott.inews.util.Snack;
import com.su.scott.inews.util.Tools;
import com.su.scott.inews.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @类名 NewsListFragment
 * @描述 新闻列表Fragment
 * @作者 Su
 * @时间 2015-12-16
 */
public class NewsListFragment extends BaseFragment {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mNewsRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<NewsBean> mNewsBeanList;
    private NewsListAdapter mNewsAdapter;
    private View mEmptyLayout;
    private int mStartPage = 1;
    private int mCurrPage;
    private boolean hasGetData = false;
    private static final int PAGE_STEP = 10;


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initPreData() {
    }

    @Override
    protected void initView() {
        mEmptyLayout = rootLayout.findViewById(R.id.rl_empty_content);
        mNewsRecyclerView = (RecyclerView) rootLayout.findViewById(R.id.rv_news_fragment_1);
        mNewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootLayout.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorScheme(R.color.colorPrimary, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        mCurrPage = mStartPage;
        mNewsBeanList = new ArrayList<>();
        mNewsAdapter = new NewsListAdapter(activity);
        mLinearLayoutManager = new LinearLayoutManager(activity);
        mNewsAdapter.setNewsBeanList(mNewsBeanList);
        mNewsRecyclerView.setHasFixedSize(true);
        mNewsRecyclerView.setAdapter(mNewsAdapter);
        mNewsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mNewsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        requestNewsList(true, false, mStartPage);
    }


    @Override
    protected void initListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestNewsList(false, false, mStartPage);
            }
        });

        mNewsAdapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Tools.isFastClick()) {
                    return;
                }
                AnimUtil.shrink(view);

                NewsBean news = mNewsAdapter.getNewsBeanList().get(position);
                Intent intent = new Intent(activity, NewsDetailActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("NewsBean", news);
                intent.putExtras(data);
                activity.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                AnimUtil.pulseBounce(view);
            }
        });


        mNewsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isSlidingToLast;
            private int currRecyclerState;//静止、滚动

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                currRecyclerState = newState;

                //上拉自动加载更多
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    mNewsAdapter.getRequestQueue().start();
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向下滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多
                        if (NetworkUtil.isNetworkConnected(activity)) {
                            requestNewsList(false, true, ++mCurrPage);
                        } else {
                            Snack.showShort(mNewsRecyclerView, "加载失败，请检查网络");
                        }
                    }
                }/* else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    mNewsAdapter.getRequestQueue().stop();
                }*/
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (currRecyclerState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (dy > 0) {
                        //上拉下滚
                        isSlidingToLast = true;
                        EventBus.getDefault().post(new MyEvent("MainActivity", MainActivity.FLAG_HIDE_PB));
                    } else if (dy < 0) {
                        //下拉上滚
                        isSlidingToLast = false;
                        EventBus.getDefault().post(new MyEvent("MainActivity", MainActivity.FLAG_SHOW_PB));
                    }
                }
            }
        });

    }


    private void requestNewsList(final boolean needShowPd, final boolean isLoadMore, int pageStart) {
        int num = PAGE_STEP;

        if (needShowPd) {
            showPd();
        }

        if (isLoadMore) {
            num = PAGE_STEP * 2;
        }

        CustomRequest.getInstance(activity).getStringBeanList(BaseActivity.requestQueue, BaseActivity.TAG, Constant.URL_NEWS, new CustomRequest.CustomParam().put("apikey", Constant.API_KEY_APISTORE), new CustomRequest.CustomParam().put("page", pageStart + "").put("num", num + ""), NewsBean.class, new CustomRequest.BeanListener<NewsBean>() {
            @Override
            public void onSuccess(final List<NewsBean> response) {
                if (null == response) {
                    Snackbar.make(rootLayout, "获取数据失败", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (!isLoadMore) {
                    //首次获取或刷新
                    mNewsBeanList.clear();

                 /*   new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                if (null != dbUtils.findAll(NewsBean.class)) {
                                    dbUtils.deleteAll(NewsBean.class);
                                }
                                dbUtils.saveAll(response);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();*/
                }

                mNewsBeanList.addAll(response);
                mNewsAdapter.notifyDataSetChanged();
                hasGetData = true;
                stopRefresh();
                dismissPd();

                if (0 == response.size()) {
                    UIUtil.setViewVisiable(mEmptyLayout);
                } else {
                    UIUtil.setViewGone(mEmptyLayout);
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                Snack.showLong(mNewsRecyclerView, "请检查网络");

                if ((null != mNewsBeanList) && (0 == mNewsBeanList.size())) {
                /*    try {
                        List<NewsBean> newsCache = dbUtils.findAll(NewsBean.class);
                        if (null != newsCache) {
                            mNewsBeanList.addAll(newsCache);
                            mLastLoadList = newsCache;
                            mNewsAdapter.notifyDataSetChanged();
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }*/

//                    showEmpty();
                    UIUtil.setViewVisiable(mEmptyLayout);
                }

                dismissPd();
                stopRefresh();
            }
        });
    }

    public void onEventMainThread(MyEvent event) {
        if (((mStartPage + "").equals(event.getTag())) && hasGetData) {
            mNewsRecyclerView.smoothScrollToPosition(0);
//            EventBus.getDefault().post(new MyEvent(MainActivity.FLAG_EVENT_HIDE_FB));
        }
    }

    public int getmStartPage() {
        return mStartPage;
    }

    public NewsListFragment setmStartPage(int mStartPage) {
        this.mStartPage = mStartPage;
        return this;
    }

    private void stopRefresh() {
        if (null == mSwipeRefreshLayout) {
            return;
        }

        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
