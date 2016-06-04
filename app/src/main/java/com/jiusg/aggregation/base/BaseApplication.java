package com.jiusg.aggregation.base;

import android.app.Application;

/**
 * Created by Administrator on 2016/5/29.
 */
public class BaseApplication extends Application {

    public static String cacheDir = "";

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        if (getApplicationContext().getExternalCacheDir() != null) {
            cacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            cacheDir = getApplicationContext().getCacheDir().toString();
        }
    }
}
