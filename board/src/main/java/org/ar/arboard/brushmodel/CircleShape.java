package org.ar.arboard.brushmodel;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.gson.Gson;

import org.ar.arboard.utils.ARBoardConfig;
import org.ar.arboard.bean.PaintBean;

public class CircleShape extends PaintShape {


    public CircleShape(float mStartX, float mStartY, Paint mPaint) {
        super(mStartX, mStartY, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public CircleShape(Paint paint) {
        super(paint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        float a = mEndX - mStartX;
        float b = mEndY - mStartY;
        float c = (float) Math.sqrt(Math.pow(Math.abs(a), 2) + Math.pow(Math.abs(b), 2));
        float centreX = Math.abs((mEndX - mStartX)) / 2 + Math.min(mStartX, mEndX);
        float centreY = Math.abs((mEndY - mStartY)) / 2 + Math.min(mStartY, mEndY);
        canvas.drawCircle(centreX, centreY, c / 2, mPaint);
    }

    @Override
    protected PaintBean setPaintData() {
        PaintBean paintBean = super.setPaintData();
        paintBean.setDType(ARBoardConfig.BrushModel.Circle);
        return paintBean;
    }

    @Override
    public String toString() {
        Gson gson=new Gson();
        return gson.toJson(setPaintData());
    }
}
