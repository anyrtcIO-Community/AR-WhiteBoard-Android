package org.ar.arboard.weight;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.ar.arboard.utils.ARBoardConfig;

/**
 * Created by liuxiaozhong on 2017-06-12.
 */

public class ARViewpager extends ViewPager {

    public  boolean isTouched;
    
    private ARBoardConfig arBoardConfig;

    public ARBoardConfig getARBoardConfig() {
        return arBoardConfig;
    }

    public void setARBoardConfig(ARBoardConfig arBoardConfig) {
        this.arBoardConfig = arBoardConfig;
    }

    public boolean isTouched() {
        return isTouched;
    }

    public void setTouched(boolean touched) {
        isTouched = touched;
    }

    public ARViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public ARViewpager(Context context) {
        super(context);
    }


    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    //
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (arBoardConfig.getBrushModel() == ARBoardConfig.BrushModel.Transform || arBoardConfig.getBrushModel() == ARBoardConfig.BrushModel.TransformSync) {
            int action = arg0.getAction();
            if (action==MotionEvent.ACTION_UP){
                isTouched=true;
            }
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (arBoardConfig.getBrushModel() == ARBoardConfig.BrushModel.Transform || arBoardConfig.getBrushModel() == ARBoardConfig.BrushModel.TransformSync) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

}
