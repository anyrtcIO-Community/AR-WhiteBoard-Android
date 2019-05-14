package org.ar.arboard.brushmodel;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.gson.Gson;

import org.ar.arboard.utils.ARBoardConfig;
import org.ar.arboard.bean.PaintBean;

public class RectShape extends PaintShape {


    public RectShape(float mStartX, float mStartY, Paint mPaint) {
        super(mStartX, mStartY, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
    }
    public RectShape(Paint paint) {
        super(paint);
    }
    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(mStartX, mStartY, mEndX, mEndY, mPaint);
    }

    @Override
    protected PaintBean setPaintData() {
        PaintBean paintBean = super.setPaintData();
        paintBean.setDType(ARBoardConfig.BrushModel.Rect);
        return paintBean;
    }


    @Override
    public String toString() {
        Gson gson=new Gson();
        return gson.toJson(setPaintData());
    }
}
