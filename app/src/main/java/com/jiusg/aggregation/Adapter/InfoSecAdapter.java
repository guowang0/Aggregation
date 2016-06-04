package com.jiusg.aggregation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiusg.aggregation.R;
import com.jiusg.aggregation.domain.Message;
import com.jiusg.aggregation.ui.WebViewActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/20.
 */
public class InfoSecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Message> messages;
    private Context context;

    private final int TYPE_INFO = 0;
    private final int TYPE_WEIBO = 1;

    public InfoSecAdapter(ArrayList<Message> messages, Context context) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).type == Message.WEIBO) {
            return TYPE_WEIBO;
        } else if (messages.get(position).type == Message.INFO)
            return TYPE_INFO;
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1)
            return null;
        if (viewType == TYPE_INFO)
            return new MainViewHolder((LayoutInflater.from(context)
                    .inflate(R.layout.item_recycler_hot, parent, false)));
        return new WeiBoHolder((LayoutInflater.from(context)
                .inflate(R.layout.item_recycler_weibo, parent, false)));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_INFO) {
            if (messages.get(position).info != null) {
                MainViewHolder holder = (MainViewHolder) viewHolder;
                holder.title.setText(Html.fromHtml(messages.get(position).info.title + ""));
                holder.content.setText(Html.fromHtml(messages.get(position).info.content + ""));
                holder.time.setText(messages.get(position).info.source + "   " + messages.get(position).info.timeStr);
            }
        } else if (getItemViewType(position) == TYPE_WEIBO) {
            if(messages.get(position).weiBo != null) {
                WeiBoHolder holder = (WeiBoHolder) viewHolder;
                holder.name.setText(messages.get(position).weiBo.userName);
                holder.content.setText(Html.fromHtml(messages.get(position).weiBo.content));
                holder.time.setText(messages.get(position).weiBo.time);

                Glide.with(context)
                        .load(messages.get(position).weiBo.userPhoto)
                        .asBitmap()
                        .centerCrop()
                        .into(holder.photo);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (messages != null)
            return messages.size();
        return 0;
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView content;
        TextView time;

        public MainViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.hot_title);
            content = (TextView) itemView.findViewById(R.id.hot_content);
            time = (TextView) itemView.findViewById(R.id.hot_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("message", messages.get(getAdapterPosition()).info);
                    intent.setClass(context, WebViewActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    class WeiBoHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView name;
        TextView content;
        TextView time;

        public WeiBoHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.weiBo_photo);
            name = (TextView) itemView.findViewById(R.id.weiBo_name);
            content = (TextView) itemView.findViewById(R.id.weiBo_content);
            time = (TextView) itemView.findViewById(R.id.weiBo_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("weiBo", messages.get(getAdapterPosition()).weiBo);
                    intent.setClass(context, WebViewActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

}
