package com.jiusg.aggregation.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiusg.aggregation.R;
import com.jiusg.aggregation.domain.Info;
import com.jiusg.aggregation.domain.WeiBo;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by Administrator on 2015/12/11.
 */
public class WebViewActivity extends Activity implements View.OnClickListener{

    private WebView webView;
    private Info message;
    private RelativeLayout pb;
    private ImageButton back;
    private TextView title;

    private String url;

    private WeiBo weiBo;

    private final String TAG = WebViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

        setStatusBarColor(R.color.status);

        webView = (WebView) findViewById(R.id.webview);
        pb = (RelativeLayout) findViewById(R.id.progress);
        back = (ImageButton) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title) ;

        back.setOnClickListener(this);

        getData();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //开启 Application Caches 功能
        webView.getSettings().setAppCacheEnabled(true);
//        webView.getSettings().setBuiltInZoomControls(true);

        Log.i(TAG,"url="+url);
    }

    /**
     * 设置状态栏颜色
     * 也就是所谓沉浸式状态栏？
     */
    public void setStatusBarColor(int color) {
        /**
         * Android4.4以上可用
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintResource(color);
            tintManager.setStatusBarTintEnabled(true);
        }
    }


    private void getData(){

        try {
            weiBo = (WeiBo) getIntent().getSerializableExtra("weiBo");
            title.setText(weiBo.userName);
            url = "http://weibo.com/"+weiBo.user;
        } catch (Exception e) {
            weiBo = null;
            Log.e(TAG,e.toString());
        }

        try {
            message = (Info) getIntent().getSerializableExtra("message");
            title.setText(message.title);
            url = message.url;
        } catch (Exception e) {
            message = null;
            Log.e(TAG,e.toString());
        }
    }

    public static void synCookies(Context context, String url, String cookies) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, cookies);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public void onClick(View v) {
        finish();
    }


    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // 页面上有数字会导致连接电话
            if (url.indexOf("tel:") < 0) {

                view.loadUrl(url);

            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(WebViewActivity.this);
//            cookieSyncManager.sync();
//            CookieManager cookieManager = CookieManager.getInstance();
//            Log.i("$$$$$$$",""+cookieManager.getCookie("http://www.lingshi321.com/"));
            pb.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pb.setVisibility(View.VISIBLE);
        }

    }

//    @Override
//    public boolean onKeyDown(int keyCoder, KeyEvent event) {
//        if (webView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
//            webView.goBack(); // goBack()表示返回webView的上一页面
//            return true;
//        }
//        return false;
//    }
}
