package com.jiusg.aggregation.domain;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/5/8.
 */
public class Info implements Serializable {
    public String timeStr;
    public Calendar time;
    public String title;
    public String content;
    public String source;
    public String url;
}
