package org.ar.arboard;


import org.ar.arboard.utils.DevConfig;

/**
 * Created by liuxiaozhong on 2018/9/28.
 */
public class ARBoardEngine {


    private static class SingletonHolder {
        private static final ARBoardEngine INSTANCE = new ARBoardEngine();
    }

    public static final ARBoardEngine Inst() {
        return SingletonHolder.INSTANCE;
    }

    private ARBoardEngine() {
    }

    public void initEngineWithARInfo(final String strDeveloperId, final String strAppId,
                                         final String strAESKey, final String strToken) {
        DevConfig.getInstance().initARInfo(strDeveloperId,strAppId,strAESKey,strToken);
    }

    public String getSdkVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public void setDebugLog(boolean open){
        DevConfig.getInstance().setOpenLog(open);
    }

}
