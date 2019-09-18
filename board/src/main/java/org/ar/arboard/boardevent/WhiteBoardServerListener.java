package org.ar.arboard.boardevent;

import java.util.List;

/**
 * Created by liuxiaozhong on 2017/11/16.
 */

public abstract class WhiteBoardServerListener {

    public abstract void initBoardSuccess();

    public abstract void initBoardFaild(int code);

    public abstract void switchPage(int currentPage,boolean isInit);

    public abstract void initAppInfoSuccess();

    public abstract void initAppInfoFaild(int code);

    public abstract void onAddBoardSuccess(List<String> imageUrl,int curPageNum,boolean isAdd);

    public abstract void onBoardDestroy();

    public abstract void onNeedDisplay(List<String> imageUrl);

    public abstract void onBoardDisconnect();

    public abstract void onChangeTimestamp(long time);

    public abstract void onMessage(String msgData);

    public abstract void onServerChange(boolean isInit);
    public abstract void onBoardClean(int boardNum);
    public abstract void onUndoSuccess(int boardNum,String localId);
    public abstract void onUndoFaild();
    public abstract void cleanAll();
}
