package com.jiusg.aggregation.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/25.
 */
public class Message implements Serializable{
    public int type = -1;
    public  Info info;
    public WeiBo weiBo;
    public static final int INFO = 0;
    public static final int WEIBO = 1;
}
