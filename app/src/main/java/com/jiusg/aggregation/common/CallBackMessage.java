package com.jiusg.aggregation.common;

import com.jiusg.aggregation.domain.Info;
import com.jiusg.aggregation.domain.Message;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/18.
 */
public abstract class CallBackMessage {

    public abstract void  done(ArrayList<Message> messages);

    public abstract void  error(Throwable throwable);
}
