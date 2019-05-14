package org.ar.arboard.brushmodel;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.ar.arboard.bean.PaintBean;
import org.ar.arboard.utils.ARBoardConfig;

import java.util.ArrayList;

public class PointShape extends PaintShape {
    private Path path;
    ArrayList<Float[]> pointList;
    private PointF pointPre;

    public PointShape(float x, float y, Paint pan) {
        mStartX=x;
        mStartY=y;
        mPaint = pan;
        mPaint.setStyle(Paint.Style.STROKE);
        path = new Path();
        pointList = new ArrayList<>();
        Float[] data=new Float[2];
        data[0]=mStartX;
        data[1]=mStartY;
        pointList.add(data);
        path.moveTo(x, y);
        pointPre = new PointF();
        pointPre.x=x;
        pointPre.y=y;
    }



    @Override
    public void move(float mx, float my) {
        PointF pointNew = new PointF(mx, my);
        path.quadTo(pointPre.x, pointPre.y, (pointPre.x + pointNew.x) / 2, (pointPre.y + pointNew.y) / 2);
        pointPre.x = pointNew.x;
        pointPre.y = pointNew.y;
        Float[] data=new Float[2];
        data[0]=mx;
        data[1]=my;
        pointList.add(data);
    }


    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPath(path, mPaint);
    }

    @Override
    protected PaintBean setPaintData() {
        PaintBean paintBean = super.setPaintData();
        paintBean.setDType(ARBoardConfig.BrushModel.Graffiti);
        JsonArray jsonElements = new JsonArray();
        for (int i = 0; i < pointList.size(); i++) {
            JsonArray position=new JsonArray();
            Float[] arrayPos=pointList.get(i);
            position.add(arrayPos[0]);
            position.add(arrayPos[1]);
            jsonElements.add(position);
        }
        paintBean.setDPoint(jsonElements.toString());
        return paintBean;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(setPaintData());
    }

}
