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
import com.jiusg.aggregation.domain.WeiBo;
import com.jiusg.aggregation.ui.WebViewActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/20.
 */
public class WeiBoAdapter extends RecyclerView.Adapter<WeiBoAdapter.MainViewHolder> {

    private ArrayList<WeiBo> weiBos;
    private Context context;

    public WeiBoAdapter(ArrayList<WeiBo> weiBos, Context context){
        this.context = context;
        this.weiBos = weiBos;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder((LayoutInflater.from(context)
                .inflate(R.layout.item_recycler_weibo, parent, false)));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.name.setText(weiBos.get(position).userName);
        holder.content.setText(Html.fromHtml(weiBos.get(position).content));
        holder.time.setText(weiBos.get(position).time);

        Glide.with(context)
                .load(weiBos.get(position).userPhoto)
                .asBitmap()
                .centerCrop()
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        if(weiBos != null)
            return weiBos.size();
        return 0;
    }

    class MainViewHolder extends RecyclerView.ViewHolder{

        ImageView photo;
        TextView name;
        TextView content;
        TextView time;

        public MainViewHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.weiBo_photo);
            name = (TextView) itemView.findViewById(R.id.weiBo_name);
            content = (TextView) itemView.findViewById(R.id.weiBo_content);
            time = (TextView) itemView.findViewById(R.id.weiBo_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("weiBo",weiBos.get(getAdapterPosition()));
                    intent.setClass(context, WebViewActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

}
