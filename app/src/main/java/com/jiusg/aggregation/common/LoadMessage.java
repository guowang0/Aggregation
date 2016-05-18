package com.jiusg.aggregation.common;

import android.content.Context;
import android.util.Log;

import com.jiusg.aggregation.domain.Message;
import com.jiusg.aggregation.domain.WeiBo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/9.
 */
public class LoadMessage  {


    private Context context;

    private final String TAG = LoadWooYun.class.getSimpleName();

    public LoadMessage(Context context){
        this.context = context;
    }


    public void loadMessageInfo(final CallBackMessage callBack){

        Observable
                .create(new Observable.OnSubscribe<ArrayList<Message>>() {
                    @Override
                    public void call(final Subscriber<? super ArrayList<Message>> subscriber) {

                        LoadWooYun loadWooYun = new LoadWooYun(context);
                        loadWooYun.loadWooYunInfo(new CallBackMessage() {
                            @Override
                            public void done(ArrayList<Message> messages) {
                                subscriber.onNext(messages);
                            }

                            @Override
                            public void error(Throwable throwable) {
                                subscriber.onError(throwable);
                            }
                        });

                        LoadBaiDu loadBaiDu = new LoadBaiDu(context);
                        loadBaiDu.loadBaiDuInfo(new CallBackMessage() {
                            @Override
                            public void done(ArrayList<Message> messages) {
                                subscriber.onNext(messages);
                            }

                            @Override
                            public void error(Throwable throwable) {
                                subscriber.onError(throwable);
                            }
                        });

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Message>>() {

                    ArrayList<Message> list = new ArrayList<Message>();
                    int count = 0;

                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "【已加载完" + count + "头条信息】");
                        sort(list);
                        callBack.done(list);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        count++;
                        callBack.error(throwable);
                        if(count >= 2){
                            onCompleted();
                        }
                    }

                    @Override
                    public void onNext(ArrayList<Message> messages) {
                        list.addAll(messages);
                        count++;
                        if(count >= 2){
                            onCompleted();
                        }
                    }
                });


    }


    /**
     * 排序
     *
     * @param messages
     */
    private void sort(ArrayList<Message> messages) {
        Comparator<Message> com = new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                return rhs.time.compareTo(lhs.time);
            }
        };
        Collections.sort(messages, com);

        Log.i(TAG, "MESSAGE   【已排序完成！】");

    }
}
