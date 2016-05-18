package com.jiusg.aggregation.common;

import android.content.Context;
import android.util.Log;

import com.jiusg.aggregation.domain.WeiBo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/4/18.
 */
public class LoadWeiBo {

    private Context context;

    private final String TAG = LoadWeiBo.class.getSimpleName();

    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/49.0.2623.112 Safari/537.36";

    private final String URL = "http://weibo.cn/";

    public LoadWeiBo(Context context) {
        this.context = context;
    }


    public void loadWeiBoInfoByUsers(final ArrayList<String> strings, final CallBackWeiBo callBack) {

        Observable
                .create(new Observable.OnSubscribe<ArrayList<WeiBo>>() {
                    @Override
                    public void call(final Subscriber<? super ArrayList<WeiBo>> subscriber) {

                        for (String user : strings) {
                            loadWeiBoInfo(user, new CallBackWeiBo() {
                                @Override
                                public void done(ArrayList<WeiBo> weiBos) {
                                    subscriber.onNext(weiBos);
                                }

                                @Override
                                public void error(Throwable throwable) {
                                    subscriber.onError(throwable);
                                }
                            });
                        }

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<WeiBo>>() {

                    ArrayList<WeiBo> list = new ArrayList<WeiBo>();
                    int count = 0;

                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "【已加载完" + count + "个人的微博信息】");
                        sort(list);
                        callBack.done(list);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        count++;
                        if (count >= strings.size())
                            onCompleted();
                    }

                    @Override
                    public void onNext(ArrayList<WeiBo> weiBos) {
                        list.addAll(weiBos);
                        count++;
                        if (count >= strings.size())
                            onCompleted();
                    }
                });
    }

    /**
     * 通过user加载微博信息
     *
     * @param user
     * @param callBack
     */
    public void loadWeiBoInfo(final String user, final CallBackWeiBo callBack) {

        Observable
                .create(new Observable.OnSubscribe<ArrayList<WeiBo>>() {
                    @Override
                    public void call(Subscriber<? super ArrayList<WeiBo>> subscriber) {

                        Document document = null;
                        try {

                            Connection connection = Jsoup.connect(URL + user);
                            connection.request().header("User-Agent", USER_AGENT);
                            document = connection.get();

                        } catch (IOException e) {
                            subscriber.onError(e);
                        }

                        ArrayList<WeiBo> weiBos = getWeiBos(document,user);

                        if (weiBos != null && weiBos.size() > 0) {
                            subscriber.onNext(weiBos);
                        } else {
                            subscriber.onError(new Error("null"));
                        }

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<WeiBo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callBack.error(throwable);
                        Log.e(TAG, throwable.toString());
                    }

                    @Override
                    public void onNext(ArrayList<WeiBo> weiBos) {
                        callBack.done(weiBos);
                        Log.i(TAG, user + " | " + weiBos.get(0).userName + " | 已加载完成!");
                    }
                });

    }

    /**
     * 通过document加载微博数据
     *
     * @param document
     * @return
     */
    private ArrayList<WeiBo> getWeiBos(Document document,String user) {

        if (document == null)
            return null;

        Elements content = document.getElementsByClass("ctt");
        Elements time = document.getElementsByClass("ct");
        Elements ePhoto = document.getElementsByClass("por");

        if (content.size() <= 3) {

            return null;
        }

        int size = content.size() - 3;
        int index = 0;

        if (size > 3)
            index = size - 3;

        String name = disposeName(content.get(0).html());
        String photo = disposePhoto(ePhoto.toString());

        ArrayList<WeiBo> weiBos = new ArrayList<>();
        for (int i = 0; i < size - index; i++) {

            WeiBo weiBo = new WeiBo();

            weiBo.user = user;
            weiBo.userName = name;
            weiBo.userPhoto = photo;
            weiBo.time = disposeTime(time.get(index + i).html());
            weiBo.content = content.get(3 + index + i).html();
            weiBo.calendar = getCalendar(weiBo.time);

            weiBos.add(weiBo);

            Log.i(TAG, weiBo + " | " + weiBo.userName + " | "
                    + weiBo.userPhoto + " | " + weiBo.time + " | " + weiBo.content);

        }

        return weiBos;
    }

    private String disposePhoto(String photo) {
        int s = photo.indexOf("src=\"");
        int e = photo.indexOf("\" alt");
        if (s == -1 || e == -1)
            return photo;
        return photo.substring(s + 5, e);
    }

    private String disposeName(String name) {
        int s = name.indexOf('<');
        if (s == -1)
            return name;
        return name.substring(0, s);
    }

    /**
     * 处理Time字符串
     */
    private String disposeTime(String time) {
        int s = time.indexOf('&');
        if (s == -1)
            return time;
        return time.substring(0, s);
    }

    /**
     * 排序
     *
     * @param weiBos
     */
    private void sort(ArrayList<WeiBo> weiBos) {
        Comparator<WeiBo> com = new Comparator<WeiBo>() {
            @Override
            public int compare(WeiBo lhs, WeiBo rhs) {
                return rhs.calendar.compareTo(lhs.calendar);
            }
        };
        Collections.sort(weiBos, com);

        Log.i(TAG, "WEIBO   【已排序完成！】");

    }

    /**
     * 将time转化成Calendar对象，用于排序
     *
     * @param time
     * @return
     */
    private Calendar getCalendar(String time) {
        Calendar calendar = Calendar.getInstance();

        String str = "";
        for (int i = 0; i < time.length(); i++) {
            if (time.charAt(i) >= '0' && time.charAt(i) <= '9') {
                str += time.charAt(i);
            }
        }

        switch (str.length()) {
            case 1:
                int j = 60 - Integer.parseInt(str.substring(0, 1));
                if (j >= 0)
                    calendar.set(Calendar.SECOND, j);
                break;
            case 2:
                int i = 60 - Integer.parseInt(str.substring(0, 2));
                if (i >= 0)
                    calendar.set(Calendar.SECOND, i);
                break;
            case 4:
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str.substring(0, 2)));
                calendar.set(Calendar.MINUTE, Integer.parseInt(str.substring(2, 4)));
                break;
            case 8:
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str.substring(4, 6)));
                calendar.set(Calendar.MINUTE, Integer.parseInt(str.substring(6, 8)));
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str.substring(2, 4)));
                calendar.set(Calendar.MONTH, Integer.parseInt(str.substring(0, 2)) - 1);
                break;
            case 14:
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str.substring(8, 10)));
                calendar.set(Calendar.MINUTE, Integer.parseInt(str.substring(10, 12)));
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str.substring(6, 8)));
                calendar.set(Calendar.MONTH, Integer.parseInt(str.substring(4, 6)) - 1);
                calendar.set(Calendar.YEAR, Integer.parseInt(str.substring(0, 4)));
                break;
            default:
                calendar.set(Calendar.YEAR, 2000);
                break;
        }
        return calendar;
    }

}


