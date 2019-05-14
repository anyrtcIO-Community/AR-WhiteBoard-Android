package org.ar.arboard.boardevent;

/**
 * Created by liuxiaozhong on 2017/11/17.
 */

public interface ARBoardListener {

    void initBoardSuccess();

    void onBoardError(int nErrorCode);

    void onBoardServerDisconnect();

    void onBoardPageChange(int currentPage,int totlePage,String backgroundUrl);

    void onBoardDrawsChangeTimestamp(long timestamp);

    void onBoardMessage(String msgData);

    void onBoardDestroy();


}
