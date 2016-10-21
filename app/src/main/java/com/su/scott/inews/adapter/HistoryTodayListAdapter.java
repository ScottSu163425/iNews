package com.su.scott.inews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.su.scott.inews.R;
import com.su.scott.inews.bean.HistoryTodayBean;
import com.su.scott.inews.iamge.BitmapCache;
import com.su.scott.inews.util.StringUtil;
import com.su.scott.inews.util.UIUtil;

import java.util.List;


public class HistoryTodayListAdapter extends RecyclerView.Adapter<HistoryTodayListAdapter.ViewHolder> {
    private Context context;
    private List<HistoryTodayBean> beanList;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;
    private BitmapCache bitmapCache;

    public HistoryTodayListAdapter(Context context) {
        this.context = context;
        bitmapCache = new BitmapCache();
        imageLoader = new ImageLoader(Volley.newRequestQueue(context), bitmapCache);
    }

    public HistoryTodayListAdapter(Context context, List<HistoryTodayBean> newsBeanList) {
        this.context = context;
        this.beanList = newsBeanList;
        bitmapCache = new BitmapCache();
        imageLoader = new ImageLoader(Volley.newRequestQueue(context), bitmapCache);
    }

    public List<HistoryTodayBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<HistoryTodayBean> beanList) {
        this.beanList = beanList;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_history_today, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HistoryTodayBean bean = beanList.get(position);
        UIUtil.setText(holder.titleTv, StringUtil.toDBC(bean.getTitle()));
        UIUtil.setText(holder.descriptionTv, StringUtil.toDBC(bean.getDes()));
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.thumbIv, R.color.grey_light, R.drawable.ic_load_error);
        imageLoader.get(bean.getPic(), listener);

        // 如果设置了回调，则设置点击事件
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    onItemClickListener.onItemLongClick(holder.itemView, pos);
//                    return true;
//                }
//            });
        }

        holder.itemView.setTag(bean);
    }

    @Override
    public int getItemCount() {
        return beanList == null ? 0 : beanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbIv;
        public TextView titleTv;
        public TextView descriptionTv;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbIv = (ImageView) itemView.findViewById(R.id.iv_pic_item_history_today);
            titleTv = (TextView) itemView.findViewById(R.id.tv_title_item_history_today);
            descriptionTv = (TextView) itemView.findViewById(R.id.tv_description_item_history_today);
        }
    }

    public void removeItem(int position) {
        beanList.remove(position);
        this.notifyItemRemoved(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }


}

