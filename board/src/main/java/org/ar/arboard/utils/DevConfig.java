package org.ar.arboard.utils;

import android.text.TextUtils;

/**
 * Created by liuxiaozhong on 2018/9/28.
 */
public class DevConfig {

    protected String anyrtc_developerid="";

    protected String anyrtc_appid="";

    protected String anyrtc_appkey="";

    protected String anyrtc_apptoken="";

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

    public void initARInfo(String strDeveloperId,String strAppId,String strAppKey,String strToken){
        this.anyrtc_developerid=strDeveloperId;
        this.anyrtc_appid=strAppId;
        this.anyrtc_appkey=strAppKey;
        this.anyrtc_apptoken=strToken;
    }

    public String getAnyrtc_developerid() {
        return anyrtc_developerid;
    }

    protected void setAnyrtc_developerid(String anyrtc_developerid) {
        this.anyrtc_developerid = anyrtc_developerid;
    }

    public String getAnyrtc_appid() {
        return anyrtc_appid;
    }

    protected void setAnyrtc_appid(String anyrtc_appid) {
        this.anyrtc_appid = anyrtc_appid;
    }

    public String getAnyrtc_appkey() {
        return anyrtc_appkey;
    }

    protected void setAnyrtc_appkey(String anyrtc_appkey) {
        this.anyrtc_appkey = anyrtc_appkey;
    }

    public String getAnyrtc_apptoken() {
        return anyrtc_apptoken;
    }

    protected void setAnyrtc_apptoken(String anyrtc_apptoken) {
        this.anyrtc_apptoken = anyrtc_apptoken;
    }

    public boolean DevInfoNotFull(){
        return TextUtils.isEmpty(anyrtc_apptoken)||TextUtils.isEmpty(anyrtc_appkey)||TextUtils.isEmpty(anyrtc_appid)||TextUtils.isEmpty(anyrtc_developerid);
    }

    public boolean isOpenLog() {
        return isOpenLog;
    }

    public void setOpenLog(boolean openLog) {
        isOpenLog = openLog;
    }
}
