package com.su.scott.inews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.su.scott.inews.R;
import com.su.scott.inews.bean.CollectionBean;
import com.su.scott.inews.bean.NewsBean;
import com.su.scott.inews.iamge.BitmapCache;
import com.su.scott.inews.util.StringUtil;

import java.util.List;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private Context context;
    private List<NewsBean> newsBeanList;
    private ImageLoader imageLoader;
    private OnItemClickListener onItemClickListener;
    private RequestQueue requestQueue;
    private BitmapCache bitmapCache;

    public NewsListAdapter(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        bitmapCache = new BitmapCache();
        imageLoader = new ImageLoader(requestQueue, bitmapCache);
    }

    public NewsListAdapter(Context context, List<NewsBean> newsBeanList) {
        this.context = context;
        this.newsBeanList = newsBeanList;
        requestQueue = Volley.newRequestQueue(context);
        bitmapCache = new BitmapCache();
        imageLoader = new ImageLoader(requestQueue, bitmapCache);
    }

    public List<NewsBean> getNewsBeanList() {
        return newsBeanList;
    }

    public void setNewsBeanList(List<NewsBean> newsBeanList) {
        this.newsBeanList = newsBeanList;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lv_news, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NewsBean news = newsBeanList.get(position);
        setText(holder.titleTv, news.getTitle());
        setText(holder.descriptionTv, StringUtil.toDBC(news.getDescription()));
        setText(holder.timeTv, news.getCtime());
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
        return newsBeanList == null ? 0 : newsBeanList.size();
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


    private void setText(TextView textView, String msg) {
        if (null != msg) {
            textView.setText(msg);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public static CollectionBean newsToCollection(NewsBean news) {
        CollectionBean collectionBean = new CollectionBean();
        collectionBean.setTitle(news.getTitle());
        collectionBean.setUrl(news.getUrl());
        collectionBean.setPicUrl(news.getPicUrl());
        collectionBean.setDescription(news.getDescription());
        collectionBean.setCtime(news.getCtime());
        return collectionBean;
    }

    public static NewsBean CollectionToNews(CollectionBean collectionBean) {
        NewsBean newsBean = new NewsBean();
        newsBean.setCtime(collectionBean.getCtime());
        newsBean.setDescription(collectionBean.getDescription());
        newsBean.setUrl(collectionBean.getUrl());
        newsBean.setPicUrl(collectionBean.getPicUrl());
        newsBean.setTitle(collectionBean.getTitle());
        return newsBean;
    }

}

