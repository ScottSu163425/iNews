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
import com.su.scott.inews.bean.CollectionBean;
import com.su.scott.inews.iamge.BitmapCache;
import com.su.scott.inews.util.StringUtil;
import com.su.scott.inews.util.UIUtil;

import java.util.List;


public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.ViewHolder> {
    private Context context;
    private List<CollectionBean> collectionList;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;
    private BitmapCache bitmapCache;

    public CollectionListAdapter(Context context) {
        this.context = context;
        bitmapCache = new BitmapCache();
        imageLoader = new ImageLoader(Volley.newRequestQueue(context), bitmapCache);
    }

    public CollectionListAdapter(Context context, List<CollectionBean> newsBeanList) {
        this.context = context;
        this.collectionList = newsBeanList;
        bitmapCache = new BitmapCache();
        imageLoader = new ImageLoader(Volley.newRequestQueue(context), bitmapCache);    }

    public List<CollectionBean> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(List<CollectionBean> collectionList) {
        this.collectionList = collectionList;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lv_news, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CollectionBean news = collectionList.get(position);
        UIUtil.setText(holder.titleTv, news.getTitle());
        UIUtil.setText(holder.descriptionTv, StringUtil.toDBC(news.getDescription()));
        UIUtil.setText(holder.timeTv, news.getCtime());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.thumbIv, R.color.grey_light, R.drawable.ic_load_error);
        imageLoader.get(news.getPicUrl(), listener);

        // 如果设置了回调，则设置点击事件
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }

        holder.itemView.setTag(news);
    }

    @Override
    public int getItemCount() {
        return collectionList == null ? 0 : collectionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbIv;
        public TextView titleTv;
        public TextView descriptionTv;
        public TextView timeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbIv = (ImageView) itemView.findViewById(R.id.iv_pic_item_news);
            titleTv = (TextView) itemView.findViewById(R.id.tv_title_item_news);
            descriptionTv = (TextView) itemView.findViewById(R.id.tv_description_item_news);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time_item_news);
        }
    }

    public void removeItem(int position) {
        collectionList.remove(position);
        this.notifyItemRemoved(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }


}

