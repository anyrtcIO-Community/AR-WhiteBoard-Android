package org.ar.arboard;


import org.ar.arboard.utils.DevConfig;

/**
 * Created by liuxiaozhong on 2018/9/28.
 */
public class ARBoardEngine {

    public int port=2662;
    public String address="board.anyrtc.cc";

    private static class SingletonHolder {
        private static final ARBoardEngine INSTANCE = new ARBoardEngine();
    }

    public static final ARBoardEngine Inst() {
        return SingletonHolder.INSTANCE;
    }

    private ARBoardEngine() {
    }

    public void initEngine(final String appId,
                                       final String token) {
        DevConfig.getInstance().initARInfo(appId,token);
    }

    public void configServerForPriCloud(final String strAddr, final int nPort) {
        this.address=strAddr;
        this.port=nPort;
    }

    public String getSdkVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public void setDebugLog(boolean open){
        DevConfig.getInstance().setOpenLog(open);
    }

}
