package com.jiusg.aggregation.common;

import android.content.Context;
import android.util.Log;

import com.jiusg.aggregation.domain.Message;
import com.jiusg.aggregation.domain.WeiBo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/8.
 */
public class LoadWooYun {

    private Context context;

    private final String TAG = LoadWooYun.class.getSimpleName();

    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/49.0.2623.112 Safari/537.36";

    private final String URL = "http://www.wooyun.org/bugs/new_submit/";

    public LoadWooYun(Context context) {
        this.context = context;
    }

    /**
     * 加载乌云网站信息
     */
    public void loadWooYunInfo(final CallBackMessage callBack){
        Observable
                .create(new Observable.OnSubscribe<ArrayList<Message>>() {
                    @Override
                    public void call(Subscriber<? super ArrayList<Message>> subscriber) {

                        Document document = null;
                        try {

                            Connection connection = Jsoup.connect(URL);
//                            connection.request().header("User-Agent", USER_AGENT);
                            document = connection.get();

                        } catch (IOException e) {
                            subscriber.onError(e);
                        }

                        ArrayList<Message> messages = getWooYuns(document);

                        if (messages != null && messages.size() > 0) {
                            subscriber.onNext(messages);
                        } else {
                            subscriber.onError(new Error("null"));
                        }

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Message>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callBack.error(throwable);
                        Log.e(TAG, throwable.toString());
                    }

                    @Override
                    public void onNext(ArrayList<Message> messages) {
                        callBack.done(messages);
                        Log.i(TAG, "WooYun | 已加载完成!");
                    }
                });

    }


    public ArrayList<Message> getWooYuns(Document document){

        Elements elements = document.getElementsByClass("page-post-item");

        ArrayList<Message> messages = new ArrayList<>();

        for (Element element: elements ) {

            Message message = new Message();

            String html = element.getElementsByTag("a").html();
            message.url = element.getElementsByTag("a").attr("abs:href");
            message.title = element.html(html).getElementsByTag("h1").text();
            message.content = "";
            message.source = "WooYun";
            message.timeStr = element.html(html)
                    .getElementsByClass("page-post-item-time").text();
            message.time = getCalendar(message.timeStr);

            messages.add(message);

            Log.i(TAG,message+" | "+message.title
                    +" | "+message.url
                    +" | "+message.content
                    +" | "+message.source
                    +" | "+message.time);

        }

        return messages;
    }


    private Calendar getCalendar(String time){
        Calendar calendar = Calendar.getInstance();

        String str = "";
        for (int i = 0; i < time.length(); i++) {
            if (time.charAt(i) >= '0' && time.charAt(i) <= '9') {
                str += time.charAt(i);
            }
        }

        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str.substring(6, 8)));
        calendar.set(Calendar.MONTH, Integer.parseInt(str.substring(4, 6)) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(str.substring(0, 4)));

        return  calendar;
    }

}
