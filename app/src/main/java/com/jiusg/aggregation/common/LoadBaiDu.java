package com.jiusg.aggregation.common;

import android.content.Context;
import android.util.Log;

import com.jiusg.aggregation.domain.Info;

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
 * Created by Administrator on 2016/5/9.
 */
public class LoadBaiDu {

    private Context context;

    private final String TAG = LoadBaiDu.class.getSimpleName();

    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/49.0.2623.112 Safari/537.36";

    private final String URL = "http://www.baidu.com/s?wd=%E4%BF%A1%E6%81%AF%E5%AE%89%E5%85%A8&pn=0&tn=baidurt&ie=utf-8&rtt=1&bsst=1";

    public static final String URL_ANDROID = "https://www.baidu.com/s?tn=baidurt&rtt=1&bsst=1&wd=android&origin=ps";

    public LoadBaiDu(Context context) {
        this.context = context;
    }



    /**
     * 加载百度网站信息
     */
    public void loadBaiDuInfo(final String url,final CallBackInfo callBack){
        Observable
                .create(new Observable.OnSubscribe<ArrayList<Info>>() {
                    @Override
                    public void call(Subscriber<? super ArrayList<Info>> subscriber) {

                        Document document = null;
                        try {

                            Connection connection = Jsoup.connect(url);
                            connection.request().header("User-Agent", USER_AGENT);
                            document = connection.get();

                        } catch (IOException e) {
                            subscriber.onError(e);
                        }

                        ArrayList<Info> messages = getWooYuns(document);

                        if (messages != null && messages.size() > 0) {
                            subscriber.onNext(messages);
                        } else {
                            subscriber.onError(new Error("null"));
                        }

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Info>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callBack.error(throwable);
                        Log.e(TAG, throwable.toString());
                    }

                    @Override
                    public void onNext(ArrayList<Info> messages) {
                        callBack.done(messages);
//                        Log.i(TAG, user + " | " + weiBos.get(0).userName + " | 已加载完成!");
                    }
                });

    }


    /**
     * 加载乌云网站信息
     */
    public void loadBaiDuInfo(final CallBackInfo callBack){
        Observable
                .create(new Observable.OnSubscribe<ArrayList<Info>>() {
                    @Override
                    public void call(Subscriber<? super ArrayList<Info>> subscriber) {

                        Document document = null;
                        try {

                            Connection connection = Jsoup.connect(URL);
                            connection.request().header("User-Agent", USER_AGENT);
                            document = connection.get();

                        } catch (IOException e) {
                            subscriber.onError(e);
                        }

                        ArrayList<Info> messages = getWooYuns(document);

                        if (messages != null && messages.size() > 0) {
                            subscriber.onNext(messages);
                        } else {
                            subscriber.onError(new Error("null"));
                        }

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Info>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callBack.error(throwable);
                        Log.e(TAG, throwable.toString());
                    }

                    @Override
                    public void onNext(ArrayList<Info> messages) {
                        callBack.done(messages);
//                        Log.i(TAG, user + " | " + weiBos.get(0).userName + " | 已加载完成!");
                    }
                });

    }


    public ArrayList<Info> getWooYuns(Document document){

        Elements elements = document.getElementsByClass("f");

        ArrayList<Info> messages = new ArrayList<>();

        for (Element element: elements ) {

            Info message = new Info();

            String html = element.getElementsByTag("h3").html();
            String html1 = element.getElementsByTag("font").html();
            message.title = element.html(html).getElementsByTag("a").text();
            message.url = element.html(html).getElementsByTag("a").attr("abs:href");
            message.content = getContent(html1);
            message.timeStr = getTime(element.html(html1).getElementsByTag("div").text());
            message.source = getSource(element.html(html1).getElementsByTag("div").text());
            message.time = getCalendar(element.html(html1).getElementsByTag("div").text());

            messages.add(message);

            Log.i(TAG,message+" | "+message.title
                    +" | "+message.url
                    +" | "+message.content
                    +" | "+message.source
                    +" | "+message.timeStr);

        }

        return messages;
    }

    private String getContent(String str){
        int i = str.indexOf("</div>");
        int j = str.indexOf("<br>");
        return str.substring(i+6,j);
    }

    private String getSource(String str){
        try {
            int j = str.indexOf("-");
            String str1 = str.substring(0, j - 1);

            return str1;
        }catch (Exception e){

        }
        return str;
    }

    private String getTime(String str){
        int j = str.indexOf("-");
        String str1 = str.substring(j+2);

        return  str1;
    }

    private Calendar getCalendar(String time){
        Calendar calendar = Calendar.getInstance();

        String str = "";
        for (int i = 0; i < time.length(); i++) {
            if (time.charAt(i) >= '0' && time.charAt(i) <= '9') {
                str += time.charAt(i);
            }
        }

        int k = Integer.parseInt(str);

        String str1 = time.substring(time.length()-2,time.length()-1);

        switch (str1){

            case "天":
                calendar.add(Calendar.DAY_OF_MONTH,-k);
                break;
            case "时":
                calendar.add(Calendar.HOUR_OF_DAY,-k);
                break;
            case "钟":
                calendar.add(Calendar.MINUTE,-k);
                break;
            default:
                break;
        }

        return  calendar;
    }

}

