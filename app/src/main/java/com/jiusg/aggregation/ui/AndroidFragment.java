package com.jiusg.aggregation.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jiusg.aggregation.adapter.InfoSecAdapter;
import com.jiusg.aggregation.R;
import com.jiusg.aggregation.common.CallBackMessage;
import com.jiusg.aggregation.common.LoadMessage;
import com.jiusg.aggregation.common.LoadWeiBo;
import com.jiusg.aggregation.utils.ACache;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/7.
 */
public class AndroidFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private InfoSecAdapter adapter;

    private ArrayList<com.jiusg.aggregation.domain.Message> messages;

    private WeiBoHandler handler;

    private ACache aCache = ACache.get(getActivity());

    private final int MSG_LOAD_DATA_FINISH = 0;

    private final String TAG = AndroidFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(swipeRefreshLayout == null){
            swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_weibo, container, false);
            initView(swipeRefreshLayout);

            messages = new ArrayList<>();

            handler = new WeiBoHandler();

            try{
                messages = (ArrayList<com.jiusg.aggregation.domain.Message>) aCache.getAsObject("Android");

                if(null != messages){
                    handler.sendEmptyMessage(0);
                }else {
                    messages = new ArrayList<>();
                }

            }catch (Exception e){

            }

            adapter = new InfoSecAdapter(messages,getActivity());

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);

            swipeRefreshLayout.setOnRefreshListener(this);


            handler.sendEmptyMessageDelayed(1,300);

        }
        return swipeRefreshLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView(SwipeRefreshLayout swipeRefreshLayout){
        recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.recycler_main);
    }

    private  void loadDataFromNetWork(final Handler handler){

        LoadWeiBo loadWeiBo = new LoadWeiBo(getActivity());

        LoadMessage loadMessage = new LoadMessage(getActivity());

        loadMessage.getAndroid(new CallBackMessage() {
            @Override
            public void done(ArrayList<com.jiusg.aggregation.domain.Message> list) {

                messages.clear();
                messages.addAll(list);
                handler.sendEmptyMessage(MSG_LOAD_DATA_FINISH);
                aCache.put("Android",messages);
            }

            @Override
            public void error(Throwable throwable) {
                Toast.makeText(getActivity(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRefresh() {
        loadDataFromNetWork(handler);
    }


    class WeiBoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case MSG_LOAD_DATA_FINISH:
                    swipeRefreshLayout.setRefreshing(false);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    break;
                case 1:
                    swipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                    break;
                default:
                    break;
            }
        }
    }

}
