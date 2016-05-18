package com.jiusg.aggregation.common;

import com.jiusg.aggregation.domain.WeiBo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/18.
 */
public abstract class CallBackWeiBo {

    public abstract void  done(ArrayList<WeiBo> weiBos);

    public abstract void  error(Throwable throwable);
}
