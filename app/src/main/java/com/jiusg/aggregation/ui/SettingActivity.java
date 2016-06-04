package com.jiusg.aggregation.ui;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.jiusg.aggregation.base.BaseApplication;
import com.jiusg.aggregation.R;
import com.jiusg.aggregation.utils.ACache;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;

/**
 * Created by Administrator on 2016/5/29.
 */
public class SettingActivity extends AppCompatActivity {

    private ACache aCache;

    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setStatusBarColor(R.color.status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back = (ImageButton) findViewById(R.id.back);

        aCache = ACache.get(SettingActivity.this);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment,new SettingFragment()).commit();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    public class  SettingFragment extends PreferenceFragment{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.setting);

            PreferenceScreen cache = (PreferenceScreen) findPreference("cache");
            PreferenceScreen about = (PreferenceScreen) findPreference("about");

            File file = new File(BaseApplication.cacheDir);

            cache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("确定清除缓存?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    aCache.clear();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();

                    return false;
                }
            });

            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("开发者：过往")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();

                    return false;
                }
            });



            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
