package com.jiusg.aggregation.common;

import com.jiusg.aggregation.domain.Info;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/18.
 */
public abstract class CallBackInfo {

    public abstract void  done(ArrayList<Info> infos);

    public abstract void  error(Throwable throwable);
}
