package com.su.scott.inews.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.su.scott.inews.R;
import com.su.scott.inews.adapter.CollectionListAdapter;
import com.su.scott.inews.adapter.NewsListAdapter;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.bean.CollectionBean;
import com.su.scott.inews.bean.NewsBean;
import com.su.scott.inews.util.AnimUtil;
import com.su.scott.inews.util.DialogUtil;
import com.su.scott.inews.util.Snack;
import com.su.scott.inews.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * @类名 CollectionActivity
 * @描述 “我的收藏”Activity
 * @作者 Su
 * @时间
 */
public class CollectionActivity extends BaseActivity {
    private Toolbar mToolbar;
    private List<CollectionBean> mCollectionList;
    private RecyclerView mNewsRecyclerView;
    private CollectionListAdapter mCollectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initPreData() {

    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_collection);
        mToolbar.setTitle("收藏夹");
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
        mNewsRecyclerView = (RecyclerView) findViewById(R.id.rv_collection);
    }

    @Override
    protected void initData() {
        mCollectionList = new ArrayList<>();
        mCollectionAdapter = new CollectionListAdapter(this);
        mCollectionAdapter.setCollectionList(mCollectionList);
        mNewsRecyclerView.setHasFixedSize(true);
        mNewsRecyclerView.setAdapter(mCollectionAdapter);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        getCollection();
    }

    private void getCollection() {
        new AsyncTask<Void, Void, List<CollectionBean>>() {

            @Override
            protected void onPreExecute() {
                showPd();
                super.onPreExecute();
            }

            @Override
            protected List<CollectionBean> doInBackground(Void... params) {
                try {
                    List<CollectionBean> collectionBeanList = dbUtils.findAll(CollectionBean.class);
                    if (null != collectionBeanList) {
                        return (Tools.getReverseList(collectionBeanList));
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<CollectionBean> result) {
                dismissPd();
                super.onPostExecute(result);
                if (null != result) {
                    mCollectionList.clear();
                    mCollectionList.addAll(result);
                    mCollectionAdapter.notifyDataSetChanged();
                } else {
                    Snack.showShort(mNewsRecyclerView, "收藏夹为空");
                }
            }
        }.execute();
    }

    @Override
    protected void initListener() {
        mCollectionAdapter.setOnItemClickListener(new CollectionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Tools.isFastClick()) {
                    return;
                }
                AnimUtil.shrink(view);

                CollectionBean collectionBean = mCollectionAdapter.getCollectionList().get(position);
                NewsBean news = NewsListAdapter.CollectionToNews(collectionBean);
                Intent intent = new Intent(CollectionActivity.this, NewsDetailActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("NewsBean", news);
                intent.putExtras(data);
                goTo(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                if (Tools.isFastClick()) {
                    return;
                }

                AnimUtil.pulseBounce(view);
                Snack.showShort(view, "是否移除该条收藏？", "OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeCollection(position);

                    }
                });
            }
        });
    }

    private void removeCollection(int position) {
        CollectionBean beanToRemove = mCollectionAdapter.getCollectionList().get(position);
        try {
            dbUtils.delete(CollectionBean.class, WhereBuilder.b("url", "=", beanToRemove.getUrl()));
//            mCollectionList.remove(position);
            mCollectionAdapter.removeItem(position);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_clear, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Tools.isFastClick()) {
            return true;
        }
        int id = item.getItemId();

        if (R.id.action_clear_single == id) {
            popClearDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void popClearDialog() {
        DialogUtil.showComplex(this, "提示", "是否清空收藏夹？", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearCollection();
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void clearCollection() {
        if (0 == mCollectionList.size()) {
            Snack.showShort(mNewsRecyclerView, "您收藏夹中并没有记录");
            return;
        }

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                mCollectionList.clear();
                try {
                    dbUtils.deleteAll(CollectionBean.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mCollectionAdapter.notifyDataSetChanged();
                Snack.showShort(mNewsRecyclerView, "收藏夹已清空");
                super.onPostExecute(aVoid);
            }
        }.execute();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mCollectionAdapter.clearImageCache();
    }

    @Override
    public void onClick(View v) {

    }
}
