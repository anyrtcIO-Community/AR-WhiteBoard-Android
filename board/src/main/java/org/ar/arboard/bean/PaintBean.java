package org.ar.arboard.bean;

/**
 * Created by liuxiaozhong on 2017-10-24.
 */

public class PaintBean {


    public int DType;
    public float DWidth;
    public String DColor;
    public float DCanvasWidth;
    public float DCanvasHeight;
    public float DStartX;
    public float DStartY;
    public float DEndX;
    public float DEndY;
    public String DPoint;
    public PaintBean(float DWidth, String DColor, float DStartX, float DStartY, float DEndX, float DEndY,float DCanvasWidth,float DCanvasHeight) {
        this.DWidth = DWidth;
        this.DColor = DColor;
        this.DStartX = DStartX;
        this.DStartY = DStartY;
        this.DEndX = DEndX;
        this.DEndY = DEndY;
        this.DCanvasWidth=DCanvasWidth;
        this.DCanvasHeight=DCanvasHeight;
    }

    public int getDType() {
        return DType;
    }

    public void setDType(int DType) {
        this.DType = DType;
    }

    public float getDWidth() {
        return DWidth;
    }

    public void setDWidth(float DWidth) {
        this.DWidth = DWidth;
    }

    public String getDColor() {
        return DColor;
    }

    public void setDColor(String DColor) {
        this.DColor = DColor;
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

    public float getDStartX() {
        return DStartX;
    }

    public void setDStartX(float DStartX) {
        this.DStartX = DStartX;
    }

    public float getDStartY() {
        return DStartY;
    }

    public void setDStartY(float DStartY) {
        this.DStartY = DStartY;
    }

    public float getDEndX() {
        return DEndX;
    }

    public void setDEndX(float DEndX) {
        this.DEndX = DEndX;
    }

    public float getDEndY() {
        return DEndY;
    }

    public void setDEndY(float DEndY) {
        this.DEndY = DEndY;
    }

    public String getDPoint() {
        return DPoint;
    }

    public void setDPoint(String DPoint) {
        this.DPoint = DPoint;
    }
}
