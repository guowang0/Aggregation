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

import com.jiusg.aggregation.Adapter.WeiBoAdapter;
import com.jiusg.aggregation.R;
import com.jiusg.aggregation.common.CallBackWeiBo;
import com.jiusg.aggregation.common.LoadWeiBo;
import com.jiusg.aggregation.domain.WeiBo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/7.
 */
public class WeiBoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private WeiBoAdapter adapter;

    private ArrayList<String> list;
    private ArrayList<WeiBo> weiBos;

    private WeiBoHandler handler;

    private final int MSG_LOAD_DATA_FINISH = 0;

    private final String TAG = WeiBoFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(swipeRefreshLayout == null){
            swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_weibo, container, false);
            initView(swipeRefreshLayout);


            list = new ArrayList<>();
            weiBos = new ArrayList<>();

            addTestData();

            handler = new WeiBoHandler();

            adapter = new WeiBoAdapter(weiBos,getActivity());

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

    private  void loadDataFromNetWork(final Handler handler,ArrayList<String> strings,
                                      final ArrayList<WeiBo> list){

        LoadWeiBo loadWeiBo = new LoadWeiBo(getActivity());

        loadWeiBo.loadWeiBoInfoByUsers(strings, new CallBackWeiBo() {
            @Override
            public void done(ArrayList<WeiBo> weiBos) {
                list.clear();
                list.addAll(weiBos);
                handler.sendEmptyMessage(MSG_LOAD_DATA_FINISH);
            }

            @Override
            public void error(Throwable throwable) {

            }
        });

    }

    @Override
    public void onRefresh() {
        loadDataFromNetWork(handler,list,weiBos);
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

    /**
     * 添加测试数据
     */
    private void addTestData(){

        list.add("cnitsec");
//        list.add("benjurry");
//        list.add("caoz");
        list.add("Fenng");
//        list.add("FlashSky");
//        list.add("lake2");
    }


}
