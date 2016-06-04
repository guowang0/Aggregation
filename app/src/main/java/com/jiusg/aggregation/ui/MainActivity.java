package com.jiusg.aggregation.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;


import com.jiusg.aggregation.adapter.MainViewPageAdapter;
import com.jiusg.aggregation.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private TextView title;
    private ImageButton hot;
    private ImageButton weiBo;
    private ImageButton setting;

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor(R.color.status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

    }


    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPage);

        title = (TextView) findViewById(R.id.actionbar_title);
        hot = (ImageButton) findViewById(R.id.actionbar_event);
        weiBo = (ImageButton) findViewById(R.id.actionbar_preview);
        setting = (ImageButton) findViewById(R.id.actionbar_setting);

        hot.setOnClickListener(this);
        weiBo.setOnClickListener(this);
        setting.setOnClickListener(this);

        ArrayList<Fragment> fragments = new ArrayList<>(2);

        fragments.add(new InfoSecFragment());
        fragments.add(new AndroidFragment());

        viewPager.setAdapter(new MainViewPageAdapter(getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        title.setText("信息安全");
                        hot.setImageResource(R.mipmap.ic_event_press);
                        weiBo.setImageResource(R.mipmap.ic_preview);
                        break;
                    case 1:
                        title.setText("Android");
                        hot.setImageResource(R.mipmap.ic_event);
                        weiBo.setImageResource(R.mipmap.ic_preview_press);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_event:
                viewPager.setCurrentItem(0);
                break;
            case R.id.actionbar_preview:
                viewPager.setCurrentItem(1);
                break;
            case R.id.action_settings:

                break;
            default:
                break;
        }
    }
}
