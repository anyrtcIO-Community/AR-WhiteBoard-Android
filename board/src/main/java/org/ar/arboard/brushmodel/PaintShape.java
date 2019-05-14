package org.ar.arboard.brushmodel;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import org.ar.arboard.bean.PaintBean;

public abstract class  PaintShape {

    protected float mStartX, mStartY, mEndX, mEndY;
    protected Paint mPaint;
    protected String mPointsArray;
    public float DCanvasWidth;
    public float DCanvasHeight;
    public String DColor;
    private String LocalBoardId;
    private int BoardNum;

    public String getLocalBoardId() {
        return LocalBoardId;
    }

    public void setLocalBoardId(String boardId) {
        this.LocalBoardId = boardId;
    }

    public int getBoardNum() {
        return BoardNum;
    }

    public void setBoardNum(int boardNum) {
        BoardNum = boardNum;
    }

    public PaintShape(Paint paint) {
        this.mPaint = paint;
    }

    public PaintShape() {
    }

    public float getDCanvasWidth() {
        return DCanvasWidth;
    }

    public void setDCanvasWidth(float DCanvasWidth) {
        this.DCanvasWidth = DCanvasWidth;
    }

    public float getDCanvasHeight() {
        return DCanvasHeight;
    }

    public void setDCanvasHeight(float DCanvasHeight) {
        this.DCanvasHeight = DCanvasHeight;
    }

    public String getDColor() {
        return DColor;
    }

    public void setDColor(String DColor) {
        this.DColor = DColor;
    }

    public PaintShape(float mStartX, float mStartY, Paint mPaint) {
        this.mStartX = mStartX;
        this.mStartY = mStartY;
        this.mPaint = mPaint;
    }

    /**
     * 画点特有方法
     *
     * @param mx
     * @param my
     */
    public void move(float mx, float my) {
    }




    public float getmStartX() {
        return mStartX;
    }

    public void setmStartX(float mStartX) {
        this.mStartX = mStartX;
    }

    public float getmStartY() {
        return mStartY;
    }

    public void setmStartY(float mStartY) {
        this.mStartY = mStartY;
    }

    public float getmEndX() {
        return mEndX;
    }

    public void setmEndX(float mEndX) {
        this.mEndX = mEndX;
    }

    public float getmEndY() {
        return mEndY;
    }

    public void setmEndY(float mEndY) {
        this.mEndY = mEndY;
    }

    public abstract void onDraw(Canvas canvas);

    public String getmPoints() {
        return mPointsArray;
    }

    public void setmPoints(String mPoints) {
        this.mPointsArray = mPoints;
    }

    protected PaintBean setPaintData() {
        PaintBean paintBean = new PaintBean(mPaint.getStrokeWidth(), DColor, mStartX, mStartY, mEndX, mEndY, DCanvasWidth, DCanvasHeight);
        paintBean.setDPoint(mPointsArray);
        Log.d("111111111111",DColor+""+DCanvasWidth+""+DCanvasHeight);
        return paintBean;
    }

    public  float getSuitablePos(float otherWidth, float postion) {
        float fMyWidth = DCanvasWidth;
        return (fMyWidth / otherWidth) * postion;
    }

}
