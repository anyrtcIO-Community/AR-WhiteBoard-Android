package org.anyrtc.anyrtcboarddemo;

import android.app.Application;

import org.anyrtc.whiteboard.AnyRTCBoardEngine;

/**
 * Created by liuxiaozhong on 2018/9/30.
 */
public class BoardApplication extends Application{

    public static String hostId="";
    public static String joinerId="";


    @Override
    public void onCreate() {
        super.onCreate();
        //配置开发者信息
        AnyRTCBoardEngine.Inst().initEngineWithAnyrtcInfo(Constans.DEVELOPERID, Constans.APPID, Constans.APPKEY, Constans.APPTOKEN);
        AnyRTCBoardEngine.Inst().setDebugLog(true);

        hostId=(int)((Math.random()*9+1)*100000)+"";
        joinerId=(int)((Math.random()*9+1)*100000)+"";

    }
}
