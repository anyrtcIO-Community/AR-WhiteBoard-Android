package org.ar.board;

import android.app.Application;

import org.ar.arboard.ARBoardEngine;

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
        ARBoardEngine.Inst().initEngineWithARInfo(DeveloperInfo.DEVELOPERID, DeveloperInfo.APPID, DeveloperInfo.APPKEY, DeveloperInfo.APPTOKEN);
        ARBoardEngine.Inst().setDebugLog(true);

        hostId=(int)((Math.random()*9+1)*100000)+"";
        joinerId=(int)((Math.random()*9+1)*100000)+"";

    }
}
