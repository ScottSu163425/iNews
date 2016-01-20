package com.su.scott.inews.fragment;

import android.content.Intent;
import android.view.View;

import com.su.scott.inews.R;
import com.su.scott.inews.activity.CollectionActivity;
import com.su.scott.inews.activity.FilmRankActivity;
import com.su.scott.inews.activity.HistoryTodayActivity;
import com.su.scott.inews.activity.TranslationActivity;
import com.su.scott.inews.base.BaseFragment;
import com.su.scott.inews.util.AnimUtil;
import com.su.scott.inews.util.Tools;

/**
 * @类名 MenuFragment
 * @描述 左侧拉菜单Fragment
 * @作者 Su
 * @时间 2015-12-16
 */
public class MenuFragment extends BaseFragment implements View.OnClickListener {
    private View[] mItems;
    private IMenuItemClickListener menuItemClickListener;

    private static final int[] ITEM_IDS = new int[]{
            R.id.rl_item_collection_menu_fragment,
            R.id.rl_item_filminfo_menu_fragment,
            R.id.rl_item_history_today_menu_fragment,
            R.id.rl_item_translation_menu_fragment,

    };

    private static final Class[] TARGET_CLASS = new Class[]{
            CollectionActivity.class,
            FilmRankActivity.class,
            HistoryTodayActivity.class,
            TranslationActivity.class,
    };
    private static final int ITEM_COUNT = TARGET_CLASS.length;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_menu;
    }

    @Override
    protected void initPreData() {

    }

    @Override
    protected void initView() {
        mItems = new View[ITEM_COUNT];
        for (int i = 0; i < ITEM_COUNT; i++) {
            mItems[i] = rootLayout.findViewById(ITEM_IDS[i]);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        for (int i = 0; i < ITEM_COUNT; i++) {
            mItems[i].setOnClickListener(this);
            mItems[i].setOnLongClickListener(mLongClickListener);
        }
    }


    public IMenuItemClickListener getMenuItemClickListener() {
        return menuItemClickListener;
    }

    public void setMenuItemClickListener(IMenuItemClickListener menuItemClickListener) {
        this.menuItemClickListener = menuItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (Tools.isFastClick()) {
            return;
        }

        int id = v.getId();
        Class target = null;

        for (int i = 0; i < ITEM_COUNT; i++) {
            if (id == ITEM_IDS[i]) {
                target = TARGET_CLASS[i];
                break;
            }
        }
        startActivity(new Intent(activity, target));
        menuItemClickListener.onMenuItemClick();
    }

    private View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            AnimUtil.shrink(v);
            return true;
        }
    };

    public interface IMenuItemClickListener {
        public void onMenuItemClick();
    }
}
