package com.jiusg.aggregation.common;

import android.content.Context;
import android.util.Log;

import com.jiusg.aggregation.domain.Info;

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
public class LoadInfo {


    private Context context;

    private final String TAG = LoadWooYun.class.getSimpleName();

    public LoadInfo(Context context){
        this.context = context;
    }


    public void loadMessageInfo(final CallBackInfo callBack){

        Observable
                .create(new Observable.OnSubscribe<ArrayList<Info>>() {
                    @Override
                    public void call(final Subscriber<? super ArrayList<Info>> subscriber) {

                        LoadWooYun loadWooYun = new LoadWooYun(context);
                        loadWooYun.loadWooYunInfo(new CallBackInfo() {
                            @Override
                            public void done(ArrayList<Info> messages) {
                                subscriber.onNext(messages);
                            }

                            @Override
                            public void error(Throwable throwable) {
                                subscriber.onError(throwable);
                            }
                        });

                        LoadBaiDu loadBaiDu = new LoadBaiDu(context);
                        loadBaiDu.loadBaiDuInfo(new CallBackInfo() {
                            @Override
                            public void done(ArrayList<Info> messages) {
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
                .subscribe(new Observer<ArrayList<Info>>() {

                    ArrayList<Info> list = new ArrayList<Info>();
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
                    public void onNext(ArrayList<Info> messages) {
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
    private void sort(ArrayList<Info> messages) {
        Comparator<Info> com = new Comparator<Info>() {
            @Override
            public int compare(Info lhs, Info rhs) {
                return rhs.time.compareTo(lhs.time);
            }
        };
        Collections.sort(messages, com);

        Log.i(TAG, "MESSAGE   【已排序完成！】");

    }
}
