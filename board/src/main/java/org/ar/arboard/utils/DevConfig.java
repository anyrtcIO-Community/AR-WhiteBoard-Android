package org.ar.arboard.utils;

import android.text.TextUtils;

/**
 * Created by liuxiaozhong on 2018/9/28.
 */
public class DevConfig {


    protected String appid="";

    protected String apptoken="";

    protected boolean isOpenLog=true;

    static DevConfig instance;

    private DevConfig() {
    }

    public static DevConfig getInstance() {
        if (instance == null) {
            synchronized (DevConfig.class) {
                if (instance == null) {
                    instance = new DevConfig();
                }
            }
        }
        return instance;
    }

    public void initARInfo(String strAppId,String strToken){
        this.appid=strAppId;
        this.apptoken=strToken;
    }



    public String getappid() {
        return appid;
    }

    protected void setappid(String appid) {
        this.appid = appid;
    }


    public String getapptoken() {
        return apptoken;
    }

    protected void setapptoken(String apptoken) {
        this.apptoken = apptoken;
    }

    public boolean DevInfoNotFull(){
        return TextUtils.isEmpty(apptoken)||TextUtils.isEmpty(appid);
    }

    public boolean isOpenLog() {
        return isOpenLog;
    }

    public void setOpenLog(boolean openLog) {
        isOpenLog = openLog;
    }
}
