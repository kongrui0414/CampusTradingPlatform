package com.example.campustradingplatform;


import android.content.Context;

import org.litepal.LitePalApplication;

import cn.leancloud.AVException;
import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;
import cn.leancloud.callback.AVCallback;
import cn.leancloud.im.AVIMOptions;
import cn.leancloud.session.AVConnectionManager;

public class MyLeanCloudApp extends LitePalApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();


        // 提供 this、App ID、App Key、Server Host 作为参数
        // 注意这里千万不要调用 cn.leancloud.core.AVOSCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        AVOSCloud.initialize(this, "3NWKB044Bk6K0E9NjO3c30VA-MdYXbMMI", "TXsybLq9Ah0hT1oBhOrhrnug");

        // 在 AVOSCloud#initialize 之后调用，禁止自动发送推送服务的 login 请求。
        AVIMOptions.getGlobalOptions().setDisableAutoLogin4Push(true);

        AVConnectionManager.getInstance().startConnection(new AVCallback() {
            @Override
            protected void internalDone0(Object o, AVException e) {
                if (e == null) {
                    System.out.println("成功建立 WebSocket 链接");
                } else {
                    System.out.println("建立 WebSocket 链接失败：" + e.getMessage());
                }
            }
        });
        // 在 AVOSCloud.initialize() 之前调用
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
    }

}
