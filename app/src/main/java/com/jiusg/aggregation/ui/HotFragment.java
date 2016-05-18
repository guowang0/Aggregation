package com.jiusg.aggregation.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiusg.aggregation.Adapter.HotAdapter;
import com.jiusg.aggregation.Adapter.WeiBoAdapter;
import com.jiusg.aggregation.R;
import com.jiusg.aggregation.common.CallBackMessage;
import com.jiusg.aggregation.common.CallBackWeiBo;
import com.jiusg.aggregation.common.LoadBaiDu;
import com.jiusg.aggregation.common.LoadMessage;
import com.jiusg.aggregation.common.LoadWeiBo;
import com.jiusg.aggregation.common.LoadWooYun;
import com.jiusg.aggregation.domain.Message;
import com.jiusg.aggregation.domain.WeiBo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/7.
 */
public class HotFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private HotAdapter adapter;

    private ArrayList<Message> messages;

    private HotHandler handler;

    private final int MSG_LOAD_DATA_FINISH = 0;

    private final String TAG = WeiBoFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(swipeRefreshLayout == null) {
            swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_hot, container, false);
            initView(swipeRefreshLayout);

            messages = new ArrayList<>();

            handler = new HotHandler();

            adapter = new HotAdapter(messages, getActivity());

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);

            swipeRefreshLayout.setOnRefreshListener(this);

            handler.sendEmptyMessageDelayed(1,200);
        }
        return swipeRefreshLayout;
    }

    private void initView(SwipeRefreshLayout swipeRefreshLayout){
        recyclerView = (RecyclerView) swipeRefreshLayout.findViewById(R.id.recycler_hot);
    }

    private  void loadDataFromNetWork(final Handler handler,
                                      final ArrayList<Message> list) {

        LoadMessage loadMessage = new LoadMessage(getActivity());

        loadMessage.loadMessageInfo(new CallBackMessage() {
            @Override
            public void done(ArrayList<Message> messages) {
                list.clear();
                list.addAll(messages);
                handler.sendEmptyMessage(MSG_LOAD_DATA_FINISH);
            }

            @Override
            public void error(Throwable throwable) {

            }
        });

    }

    @Override
    public void onRefresh() {
        loadDataFromNetWork(handler, messages);
    }

    class HotHandler extends Handler{

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case MSG_LOAD_DATA_FINISH:
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
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
