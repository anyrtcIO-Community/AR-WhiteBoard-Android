package org.ar.arboard.utils;

/**
 * Created by liuxiaozhong on 2018/9/28.
 */
public class ARBoardConfig {


    public int mBrushModel;

    public String mBrushColor;

    public float mBrushWidth=10f;

    public static String mUserId;

    public String mAnyRTCId;

    public String mBoardSeqId;

    public int mCurrentBoardNum;

    public float mBoardWidth;

    public float mBoardHeight;

    public boolean isInitAnyrtcSuccess = false;

    public boolean isSwipe=false;


    public boolean isSwipe() {
        return isSwipe;
    }

    public void setSwipe(boolean swipe) {
        isSwipe = swipe;
    }

    public ARBoardConfig() {
        mBrushColor = "#000000";
    }


    public int getBrushModel() {
        return mBrushModel;
    }

    public void setBrushModel(int mBrushModelType) {
        this.mBrushModel = mBrushModelType;
    }

    public String getBrushColor() {
        return mBrushColor;
    }

    public void setBrushColor(String mBrushColor) {
        this.mBrushColor = mBrushColor;
    }

    public Float getBrushWidth() {
        return mBrushWidth;
    }

    public void setBrushWidth(Float mBrushWidth) {
        this.mBrushWidth = mBrushWidth;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getAnyRTCId() {
        return mAnyRTCId;
    }

    public void setAnyRTCId(String mAnyRTCId) {
        this.mAnyRTCId = mAnyRTCId;
    }

    public String getBoardSeqId() {
        return mBoardSeqId;
    }

    public void setBoardSeqId(String mBoardSeqId) {
        this.mBoardSeqId = mBoardSeqId;
    }

    public int getCurrentBoardNum() {
        return mCurrentBoardNum;
    }

    public void setCurrentBoardNum(int mCurrentBoardNum) {
        this.mCurrentBoardNum = mCurrentBoardNum;
    }

    public float getBoardWidth() {
        return mBoardWidth;
    }

    public void setBoardWidth(float mBoardWidth) {
        this.mBoardWidth = mBoardWidth;
    }

    public float getBoardHeight() {
        return mBoardHeight;
    }

    public void setBoardHeight(float mBoardHeight) {
        this.mBoardHeight = mBoardHeight;
    }

    public boolean isInitAnyrtcSuccess() {
        return isInitAnyrtcSuccess;
    }

    public void setInitAnyrtcSuccess(boolean initAnyrtcSuccess) {
        isInitAnyrtcSuccess = initAnyrtcSuccess;
    }



    public void setBoardInfo(String board_seqid,String anyRTCId,String userId){
        this.mBoardSeqId=board_seqid;
        this.mAnyRTCId=anyRTCId;
        this.mUserId=userId;
    }


    public final static class BrushModel {
        public final static int None = 0;//无效果
        public final static int Graffiti = 1;//涂鸦
        public final static int Arrow = 2;//箭头
        public final static int Line = 3;//直线
        public final static int Rect = 4;//矩形
        public final static int Transform = 5;//缩放、移动
        public final static int TransformSync=6;//缩放、移动,别人也会收到
        public final static int Circle=7;//圆
    }
}
