package com.su.scott.inews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.su.scott.inews.R;
import com.su.scott.inews.bean.FilmRankBean;
import com.su.scott.inews.iamge.BitmapCache;
import com.su.scott.inews.util.UIUtil;

import java.util.List;


public class FilmRankListAdapter extends RecyclerView.Adapter<FilmRankListAdapter.ViewHolder> {
    private Context context;
    private List<FilmRankBean> beanList;
    private OnItemClickListener onItemClickListener;

    public FilmRankListAdapter(Context context) {
        this.context = context;
    }

    public FilmRankListAdapter(Context context, List<FilmRankBean> beanList) {
        this.context = context;
        this.beanList = beanList;
    }

    public List<FilmRankBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<FilmRankBean> beanList) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_film_rank, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FilmRankBean bean = beanList.get(position);

        UIUtil.setText(holder.rankTv, bean.getRid());
        UIUtil.setText(holder.nameTv, bean.getName());
        UIUtil.setText(holder.weekBoxTv, bean.getWboxoffice());
        UIUtil.setText(holder.totalBoxTv, bean.getTboxoffice());
        UIUtil.setText(holder.rankWeekTv, bean.getWk());

        if (0 == position) {
            holder.rankTv.setTextColor(Color.WHITE);
            holder.rankTv.setBackgroundResource(R.drawable.shape_circle_red);
        } else if (1 == position) {
            holder.rankTv.setTextColor(Color.WHITE);
            holder.rankTv.setBackgroundResource(R.drawable.shape_circle_blue);
        } else if (2 == position) {
            holder.rankTv.setTextColor(Color.WHITE);
            holder.rankTv.setBackgroundResource(R.drawable.shape_circle_green);
        } else {
            holder.rankTv.setTextColor(context.getResources().getColor(R.color.text_dark_2));
            holder.rankTv.setBackgroundColor(Color.TRANSPARENT);
        }

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

        holder.itemView.setTag(bean);
    }

    @Override
    public int getItemCount() {
        return beanList == null ? 0 : beanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rankTv, nameTv, weekBoxTv, totalBoxTv, rankWeekTv;

        public ViewHolder(View itemView) {
            super(itemView);
            rankTv = (TextView) itemView.findViewById(R.id.tv_rankid_item_film_fank);
            nameTv = (TextView) itemView.findViewById(R.id.tv_filmname_item_filmrank);
            weekBoxTv = (TextView) itemView.findViewById(R.id.tv_week_box_filmrank);
            totalBoxTv = (TextView) itemView.findViewById(R.id.tv_total_box_filmrank);
            rankWeekTv = (TextView) itemView.findViewById(R.id.tv_rank_week_filmrank);
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

