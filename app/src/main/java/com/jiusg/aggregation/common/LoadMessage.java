package com.jiusg.aggregation.common;

import android.content.Context;
import android.util.Log;

import com.jiusg.aggregation.domain.Info;
import com.jiusg.aggregation.domain.Message;
import com.jiusg.aggregation.domain.WeiBo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/28.
 */
public class LoadMessage {

    private Context context;

    private final String TAG = LoadMessage.class.getSimpleName();

    public LoadMessage(Context context) {
        this.context = context;
    }


    public void getAndroid(final CallBackMessage callBack) {

        Observable
                .create(new Observable.OnSubscribe<ArrayList<Message>>() {
                    @Override
                    public void call(final Subscriber<? super ArrayList<Message>> subscriber) {

                        LoadBaiDu loadBaiDu = new LoadBaiDu(context);
                        loadBaiDu.loadBaiDuInfo(LoadBaiDu.URL_ANDROID, new CallBackInfo() {
                            @Override
                            public void done(ArrayList<Info> infos) {
                                ArrayList<Message> messages = new ArrayList<Message>();
                                for (Info info : infos) {
                                    Message message = new Message();
                                    message.type = Message.INFO;
                                    message.info = info;
                                    message.weiBo = null;
                                    messages.add(message);
                                }
                                subscriber.onNext(messages);
                            }

                            @Override
                            public void error(Throwable throwable) {
                                subscriber.onError(throwable);
                            }
                        });

                        LoadWeiBo loadWeiBo = new LoadWeiBo(context);
                        loadWeiBo.loadWeiBoInfoByUsers(getAndroidData(), new CallBackWeiBo() {
                            @Override
                            public void done(ArrayList<WeiBo> weiBos) {
                                ArrayList<Message> messages = new ArrayList<Message>();
                                for (WeiBo weiBo : weiBos) {
                                    Message message = new Message();
                                    message.type = Message.WEIBO;
                                    message.info = null;
                                    message.weiBo = weiBo;
                                    messages.add(message);
                                }
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
                        sort(list);
                        callBack.done(list);
                        Log.i(TAG, "【已加载完" + list.size() + "条Android信息！】");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        count++;
                        callBack.error(throwable);
                        if (count >= 2) {
                            onCompleted();
                        }
                    }

                    @Override
                    public void onNext(ArrayList<Message> messages) {
                        list.addAll(messages);
                        count++;
                        if (count >= 2) {
                            onCompleted();
                        }
                    }
                });

    }


    public void getInfoSecurity(final CallBackMessage callBack) {

        Observable
                .create(new Observable.OnSubscribe<ArrayList<Message>>() {
                    @Override
                    public void call(final Subscriber<? super ArrayList<Message>> subscriber) {

                        LoadInfo loadInfo = new LoadInfo(context);
                        loadInfo.loadMessageInfo(new CallBackInfo() {
                            @Override
                            public void done(ArrayList<Info> infos) {
                                ArrayList<Message> messages = new ArrayList<Message>();
                                for (Info info : infos) {
                                    Message message = new Message();
                                    message.type = Message.INFO;
                                    message.info = info;
                                    message.weiBo = null;
                                    messages.add(message);
                                }
                                subscriber.onNext(messages);
                            }

                            @Override
                            public void error(Throwable throwable) {
                                subscriber.onError(throwable);
                            }
                        });

                        LoadWeiBo loadWeiBo = new LoadWeiBo(context);
                        loadWeiBo.loadWeiBoInfoByUsers(getInfoSafeData(), new CallBackWeiBo() {
                            @Override
                            public void done(ArrayList<WeiBo> weiBos) {
                                ArrayList<Message> messages = new ArrayList<Message>();
                                for (WeiBo weiBo : weiBos) {
                                    Message message = new Message();
                                    message.type = Message.WEIBO;
                                    message.info = null;
                                    message.weiBo = weiBo;
                                    messages.add(message);
                                }
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
                        sort(list);
                        callBack.done(list);
                        Log.i(TAG, "【已加载完" + list.size() + "条信息安全信息！】");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        count++;
                        callBack.error(throwable);
                        if (count >= 2) {
                            onCompleted();
                        }
                    }

                    @Override
                    public void onNext(ArrayList<Message> messages) {
                        list.addAll(messages);
                        count++;
                        if (count >= 2) {
                            onCompleted();
                        }
                    }
                });

    }

    private ArrayList<String> getAndroidData() {

        ArrayList<String> list = new ArrayList<>();

        list.add("apkbus");
        list.add("androidnew");
        list.add("hiapkbbs");
        list.add("lyf0306");

        return list;
    }


    private ArrayList<String> getInfoSafeData() {

        ArrayList<String> list = new ArrayList<>();

        list.add("cnitsec");
//        list.add("benjurry");
//        list.add("caoz");
        list.add("Fenng");
//        list.add("FlashSky");
//        list.add("lake2");
        return list;
    }

    /**
     * 排序
     *
     * @param messages
     */
    private void sort(ArrayList<Message> messages) {
        Comparator<Message> com = new Comparator<Message>() {

            Calendar calendarRhs = null;
            Calendar calendarLhs = null;

            @Override
            public int compare(Message lhs, Message rhs) {

                if (rhs.type == Message.INFO)
                    calendarRhs = rhs.info.time;
                else if (rhs.type == Message.WEIBO) {
                    calendarRhs = rhs.weiBo.calendar;
                } else {
                    calendarRhs = Calendar.getInstance();
                }

                if (lhs.type == Message.INFO)
                    calendarLhs = lhs.info.time;
                else if (lhs.type == Message.WEIBO) {
                    calendarLhs = lhs.weiBo.calendar;
                } else {
                    calendarLhs = Calendar.getInstance();
                }

                return calendarRhs.compareTo(calendarLhs);
            }
        };
        Collections.sort(messages, com);

        Log.i(TAG, "MESSAGE   【已排序完成！】");

    }

}
