package org.ar.arboard.utils;

import android.util.Log;

/**
 * Created by liuxiaozhong on 2018/9/28.
 */
public class LogUtil {
    private static final String TAG="ARBoard";
    public static void v(String tip, String msg){
        logger("v",tip,msg);
    }

    public static void d(String tip, String msg){
        logger("d",tip,msg);
    }

    public static void i(String tip, String msg){
        logger("i",tip,msg);
    }

    public static void w(String tip, String msg){
        logger("w",tip,msg);
    }

    public static void e(String tip, String msg){
        logger("e",tip,msg);
    }

    private static void logger(String priority, String tip,String msg){
        if (!DevConfig.getInstance().isOpenLog()){
            return;
        }
        String info="====="+tip+"====="+"\n"+msg;
        switch (priority){
            case "v":
                Log.v(TAG,info);
                break;
            case "d":
                Log.d(TAG,info);
                break;
            case "i":
                Log.i(TAG,info);
                break;
            case "w":
                Log.w(TAG,info);
                break;
            default:
                Log.e(TAG,info);
        }
    }
}
