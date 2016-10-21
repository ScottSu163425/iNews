package com.su.scott.inews.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.exception.DbException;
import com.su.scott.inews.R;
import com.su.scott.inews.adapter.HistoryTodayListAdapter;
import com.su.scott.inews.base.BaseActivity;
import com.su.scott.inews.bean.HistoryTodayBean;
import com.su.scott.inews.constant.Constant;
import com.su.scott.inews.http.CustomRequest;
import com.su.scott.inews.iamge.BitmapCache;
import com.su.scott.inews.util.AnimUtil;
import com.su.scott.inews.util.JsonUtil;
import com.su.scott.inews.util.Snack;
import com.su.scott.inews.util.StringUtil;
import com.su.scott.inews.util.Tools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.nlmartian.silkcal.DatePickerController;
import me.nlmartian.silkcal.DayPickerView;
import me.nlmartian.silkcal.SimpleMonthAdapter;

/**
 * @类名 HistoryTodayActivit
 * @描述 “历史上的今天”Activity
 * @作者 Su
 * @时间
 */
public class HistoryTodayActivity extends BaseActivity implements DatePickerController {
    private Toolbar mToolbar;
    private List<HistoryTodayBean> mDataList;
    private RecyclerView mRecyclerView;
    private HistoryTodayListAdapter mListAdapter;
    private FloatingActionButton mSelectFb, mScrollToTopFb;
    private ImageView mTopAreaIv;
    private TextView mTopDesTv;
    private ImageLoader mImageLoader;
    private int mSelectedMonth;
    private int mSelectedDay;
    private Animation mFbInAnim, mFbOutAnim;
    private static final String API_KEY = "b0169118a6cbe93fa6e94f27dff4c048";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        request(mSelectedMonth, mSelectedDay);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_history_today;
    }

    @Override
    protected void initPreData() {

    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_history_today);
        mToolbar.setTitle("历史上的今天");
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
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_history_today);
        mSelectFb = (FloatingActionButton) findViewById(R.id.fb_select_date_history_today);
        mScrollToTopFb = (FloatingActionButton) findViewById(R.id.fb_scroll_to_top_history_today);
        mTopAreaIv = (ImageView) findViewById(R.id.iv_top_area_history_today);
        mTopDesTv = (TextView) findViewById(R.id.tv_des_top_history_today);
    }

    @Override
    protected void initData() {
        mImageLoader = new ImageLoader(Volley.newRequestQueue(this), new BitmapCache());
        mSelectedMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
        mSelectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mDataList = new ArrayList<>();
        mListAdapter = new HistoryTodayListAdapter(this);
        mListAdapter.setBeanList(mDataList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mFbInAnim = AnimationUtils.loadAnimation(HistoryTodayActivity.this, R.anim.in_south);
        mFbOutAnim = AnimationUtils.loadAnimation(HistoryTodayActivity.this, R.anim.out_south);
    }


    @Override
    protected void initListener() {
        mSelectFb.setOnClickListener(this);
        mScrollToTopFb.setOnClickListener(this);
        mListAdapter.setOnItemClickListener(new HistoryTodayListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                loadImage(mTopAreaIv, mListAdapter.getBeanList().get(position).getPic());
                mTopDesTv.setText(StringUtil.addBeginSpace(mListAdapter.getBeanList().get(position).getDes(), 4));
            }

            @Override
            public void onItemLongClick(View view, final int position) {
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int currRecyclerState;//静止、滚动

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                currRecyclerState = newState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (currRecyclerState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (dy > 0) {
                        //上拉下滚
                        hideFb();
                    } else if (dy < 0) {
                        //下拉上滚
                        showFb();
                    }
                }
            }
        });

    }

    private void showFb() {
        if (mScrollToTopFb.getVisibility() == View.VISIBLE) {
            return;
        }
        mScrollToTopFb.setVisibility(View.VISIBLE);
        mScrollToTopFb.startAnimation(mFbInAnim);
    }

    private void hideFb() {
        if (mScrollToTopFb.getVisibility() != View.VISIBLE) {
            return;
        }
        mScrollToTopFb.startAnimation(mFbOutAnim);
        mScrollToTopFb.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_refresh, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (R.id.action_refresh_single == id) {
            /*刷新*/
            mSelectedMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1);
            mSelectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            request(mSelectedMonth, mSelectedDay);
        }

        return super.onOptionsItemSelected(item);
    }

    private void popCalendar() {
        View contentView = getLayoutInflater().inflate(R.layout.layout_pick_date, null);
        final DayPickerView dayPickerView = (DayPickerView) contentView.findViewById(R.id.calendar_view);
        dayPickerView.setController(this);
        new AlertDialog.Builder(this).setView(contentView).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request(mSelectedMonth, mSelectedDay);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    private void request(int month, int day) {
        showPd();
        CustomRequest.CustomParam param = new CustomRequest.CustomParam();
        param.put("key", API_KEY).put("v", "1.0").put("month", month + "").put("day", day + "");

        CustomRequest.getInstance(this).getString(requestQueue, TAG, Constant.URL_HISTORY_TODAY, null, param, new CustomRequest.SimpleListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (0 == (object.getInt("error_code"))) {
                        JSONArray list = object.getJSONArray("result");
                        final List<HistoryTodayBean> temp = JsonUtil.jsonToList(list.toString(), HistoryTodayBean.class);

                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    final List<HistoryTodayBean> historyTodayBeanList = dbUtils.findAll(HistoryTodayBean.class);

                                    if (null != historyTodayBeanList) {
                                        dbUtils.deleteAll(HistoryTodayBean.class);
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
                        mListAdapter.notifyDataSetChanged();

                        loadImage(mTopAreaIv, temp.get(0).getPic());
                        mTopDesTv.setText(StringUtil.addBeginSpace(temp.get(0).getDes(), 4));
                        mRecyclerView.smoothScrollToPosition(0);
                        hideFb();
                    } else {
                        Snack.showShort(mRecyclerView, "访问服务器失败");
                    }

                    dismissPd();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String errorMsg) {
                Snack.showShort(mRecyclerView, getString(R.string.tip_error_check_connection));

                if (0 == mDataList.size()) {
                    try {
                        List<HistoryTodayBean> newsCache = dbUtils.findAll(HistoryTodayBean.class);
                        if (null != newsCache) {
                            mDataList.addAll(newsCache);
                            mListAdapter.notifyDataSetChanged();
                            loadImage(mTopAreaIv, mListAdapter.getBeanList().get(0).getPic());
                            mTopDesTv.setText(StringUtil.addBeginSpace(mListAdapter.getBeanList().get(0).getDes(), 4));
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                dismissPd();
            }
        });
    }

    private void loadImage(ImageView iv, String url) {
        if (null != iv) {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.bg_head_history_today, R.drawable.bg_head_history_today);
            if (null != url) {
                mImageLoader.get(url, listener);
            }
        }
    }

    @Override
    public int getMaxYear() {
        return 0;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        mSelectedMonth = month + 1;
        mSelectedDay = day;
    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {

    }


    @Override
    public void onClick(View v) {
        if (Tools.isFastClick()) {
            return;
        }

        int id = v.getId();

        if (R.id.fb_select_date_history_today == id) {
            popCalendar();
        } else if (R.id.fb_scroll_to_top_history_today == id) {
            //滚动到顶部
            if (null != mRecyclerView) {
                mRecyclerView.smoothScrollToPosition(0);
                hideFb();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
