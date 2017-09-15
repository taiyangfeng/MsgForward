package com.rede.msgforward;

import android.app.Application;

/**
 * @author zhengxh
 * @version 1.0, 2017/9/15 14:34
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefUtil.get().init(this);
    }
}
