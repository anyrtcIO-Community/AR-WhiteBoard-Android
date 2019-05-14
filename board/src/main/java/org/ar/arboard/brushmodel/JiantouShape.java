package org.ar.arboard.brushmodel;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.google.gson.Gson;

import org.ar.arboard.utils.ARBoardConfig;
import org.ar.arboard.bean.PaintBean;


public class JiantouShape extends PaintShape {


    public JiantouShape(float mStartX, float mStartY, Paint mPaint) {
        super(mStartX, mStartY, mPaint);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public JiantouShape(Paint paint) {
        super(paint);
    }
    @Override
    public void onDraw(Canvas canvas) {
        int size = 5;
        int count = 20;
        float x = mEndX - mStartX;
        float y = mEndY - mStartY;
        double d = x * x + y * y;
        double r = Math.sqrt(d);
        float zx = (float) (mEndX - (count * x / r));
        float zy = (float) (mEndY - (count * y / r));
        float xz = zx - mStartX;
        float yz = zy - mStartY;
        double zd = xz * xz + yz * yz;
        double zr = Math.sqrt(zd);
        Path triangle = new Path();
        triangle.moveTo(mStartX, mStartY);
        triangle.lineTo((float) (zx + size * yz / zr), (float) (zy - size * xz / zr));
        triangle.lineTo((float) (zx + size * 2 * yz / zr), (float) (zy - size * 2 * xz / zr));
        triangle.lineTo(mEndX, mEndY);
        triangle.lineTo((float) (zx - size * 2 * yz / zr), (float) (zy + size * 2 * xz / zr));
        triangle.lineTo((float) (zx - size * yz / zr), (float) (zy + size * xz / zr));
        triangle.close();
        canvas.drawPath(triangle, mPaint);
    }

    @Override
    protected PaintBean setPaintData() {
        PaintBean paintBean = super.setPaintData();
        paintBean.setDType(ARBoardConfig.BrushModel.Arrow);
        return paintBean;
    }


    @Override
    public String toString() {
        Gson gson=new Gson();
        return gson.toJson(setPaintData());
    }
}
