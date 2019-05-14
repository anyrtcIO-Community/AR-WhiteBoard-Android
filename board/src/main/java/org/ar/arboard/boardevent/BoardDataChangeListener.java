package org.ar.arboard.boardevent;

/**
 * Created by liuxiaozhong on 2017/11/17.
 */

public interface BoardDataChangeListener {
    public void onServerChange();
    public void onBoardClean(int boardNum);
    public void onUndoSuccess(int boardNum,String localId);
    public void onUndoFaild();
    public void cleanAll();
}
