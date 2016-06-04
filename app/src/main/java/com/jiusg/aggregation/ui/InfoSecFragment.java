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
import android.widget.Toast;

import com.jiusg.aggregation.adapter.InfoSecAdapter;
import com.jiusg.aggregation.R;
import com.jiusg.aggregation.common.CallBackMessage;
import com.jiusg.aggregation.common.LoadMessage;
import com.jiusg.aggregation.domain.Message;
import com.jiusg.aggregation.utils.ACache;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/7.
 */
public class InfoSecFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private InfoSecAdapter adapter;

    private ArrayList<Message> messages;

    private HotHandler handler;

    private final int MSG_LOAD_DATA_FINISH = 0;

    private final String TAG = AndroidFragment.class.getSimpleName();

    private ACache aCache;

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

            try{
                messages = (ArrayList<Message>) aCache.getAsObject("InfoSec");

                if(null != messages){
                    handler.sendEmptyMessage(0);
                }else {
                    messages = new ArrayList<>();
                }

            }catch (Exception e){

            }

            adapter = new InfoSecAdapter(messages, getActivity());

            aCache = ACache.get(getActivity());

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

    private  void loadDataFromNetWork(final Handler handler) {

        new LoadMessage(getActivity()).getInfoSecurity(new CallBackMessage() {
            @Override
            public void done(ArrayList<Message> list) {
//                if(list == null)
//                    return;
                messages.clear();
                messages.addAll(list);
                aCache.put("InfoSec",messages);
                handler.sendEmptyMessage(MSG_LOAD_DATA_FINISH);

            }

            @Override
            public void error(Throwable throwable) {
                Toast.makeText(getActivity(),throwable.getMessage()+"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        loadDataFromNetWork(handler);
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
