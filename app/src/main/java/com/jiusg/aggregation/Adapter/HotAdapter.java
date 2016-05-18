package com.jiusg.aggregation.Adapter;

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
import com.jiusg.aggregation.domain.WeiBo;
import com.jiusg.aggregation.ui.WebViewActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/20.
 */
public class HotAdapter extends RecyclerView.Adapter<HotAdapter.MainViewHolder> {

    private ArrayList<Message> messages;
    private Context context;

    public HotAdapter(ArrayList<Message> messages, Context context){
        this.context = context;
        this.messages = messages;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder((LayoutInflater.from(context)
                .inflate(R.layout.item_recycler_hot, parent, false)));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.title.setText(Html.fromHtml(messages.get(position).title+""));
        holder.content.setText(Html.fromHtml(messages.get(position).content+""));
        holder.time.setText(messages.get(position).source+"   "+messages.get(position).timeStr);

    }

    @Override
    public int getItemCount() {
        if(messages != null)
            return messages.size();
        return 0;
    }

    class MainViewHolder extends RecyclerView.ViewHolder{

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
                    intent.putExtra("message",messages.get(getAdapterPosition()));
                    intent.setClass(context, WebViewActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

}
