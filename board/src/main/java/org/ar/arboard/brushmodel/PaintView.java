package org.ar.arboard.brushmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import org.ar.arboard.boardevent.WhiteBoardWHChangeListener;
import org.ar.arboard.http.HttpServer;
import org.ar.arboard.utils.ARBoardConfig;
import org.ar.arboard.utils.BoardUtil;
import org.ar.arboard.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class PaintView extends GLSurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{


    public  boolean isRunning = true;
    public  boolean isDrawing = false;

    public boolean isCreat=false;
    SurfaceHolder mSurfaceHolder;
    float downX, downY;
    float moveX, moveY;
    Vector<PaintShape> mShapes;//所有需要绘制的数据
    Vector<PaintShape> mNeedUploadShapes;//需要上传的数据
    private HashMap<Integer, Vector<PaintShape>> LocalPaintData = new HashMap<>();//本地数据集合
    PaintShape mShape;
    Canvas canvas = null;
    HttpServer httpServer;
    ARBoardConfig arBoardConfig;
    private boolean isVisiable=true;
    private Context context;
    private WhiteBoardWHChangeListener whChangeListener;

    public boolean printOptionEnable = true;

    public void setWhChangeListener(WhiteBoardWHChangeListener whChangeListener) {
        this.whChangeListener = whChangeListener;
    }

    public void setPrintOptionEnable(boolean printOptionEnable) {
        this.printOptionEnable = printOptionEnable;
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context) {
        super(context);

    }
//    public PaintView(Context context) {
//        this(context, null);
//    }
//
//    public PaintView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//
//    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setOnTouchListener(this);
        mShapes = new Vector<>();
        mNeedUploadShapes = new Vector<>();
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public void setHttpServer(HttpServer httpServer) {
        this.httpServer = httpServer;

    }

    public void setARBoardConfig(ARBoardConfig arBoardConfig) {
        this.arBoardConfig = arBoardConfig;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        LogUtil.d("surfaceCreated","surfaceCreated");
        if (!isVisiable){
            doDraw();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        arBoardConfig.setBoardWidth(width);
        arBoardConfig.setBoardHeight(height);
        if (!isCreat) {
            if (whChangeListener != null) {
                whChangeListener.boardWDChange(width, height);
                isCreat=true;
                isVisiable=true;
            }
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
        isVisiable=false;
        LogUtil.d("surfaceDestroyed","surfaceDestroyed");
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        return false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (arBoardConfig.getBrushModel() == ARBoardConfig.BrushModel.None) {
            return false;
        } else if (arBoardConfig.getBrushModel() == ARBoardConfig.BrushModel.Transform) {
            return false;
        } else if (arBoardConfig.getBrushModel() == ARBoardConfig.BrushModel.TransformSync) {
            return false;
        } else {
            drawMove(event);
            return true;
        }


    }


    public void drawMove(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                downX = event.getX();
                downY = event.getY();
                mShape = generateShape(downX, downY);
                mShape.setBoardNum(arBoardConfig.getCurrentBoardNum());
                mShape.setmEndX(downX);
                mShape.setmEndY(downY);
                mShapes.add(mShape);
                mNeedUploadShapes.add(mShape);
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();
                if (arBoardConfig.getBrushModel() == ARBoardConfig.BrushModel.Graffiti) {
                    mShape.move(moveX, moveY);
                }else {
                    mShape.setmEndX(moveX);
                    mShape.setmEndY(moveY);
                }
                break;
            case MotionEvent.ACTION_UP:
                moveX = event.getX();
                moveY = event.getY();
                mShape.setmEndX(moveX);
                mShape.setmEndY(moveY);
                isDrawing = false;
                mShape.setLocalBoardId(BoardUtil.getPaintId(arBoardConfig.getAnyRTCId()));
                httpServer.pushData(mNeedUploadShapes);
                mNeedUploadShapes.clear();
                if (!LocalPaintData.containsKey(arBoardConfig.getCurrentBoardNum())) {
                    Vector<PaintShape> newData = new Vector<>();
                    newData.add(mShape);
                    LocalPaintData.put(arBoardConfig.getCurrentBoardNum(), newData);
                } else {
                    Vector<PaintShape> newData = LocalPaintData.get(arBoardConfig.getCurrentBoardNum());
                    newData.add(mShape);
                    LocalPaintData.put(arBoardConfig.getCurrentBoardNum(), newData);
                }
                break;

        }
        doDraw();

    }

    private PaintShape generateShape(float downX, float downY) {
        PaintShape shape;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(Color.parseColor(arBoardConfig.getBrushColor()));
        paint.setStrokeWidth(arBoardConfig.getBrushWidth());
        switch (arBoardConfig.getBrushModel()) {
            case ARBoardConfig.BrushModel.Graffiti:
                shape = new PointShape(downX, downY, paint);
                shape.setDColor(arBoardConfig.getBrushColor());
                shape.setDCanvasWidth(arBoardConfig.getBoardWidth());
                shape.setDCanvasHeight(arBoardConfig.getBoardHeight());
                break;
            case ARBoardConfig.BrushModel.Line:
                shape = new LineShape(downX, downY, paint);
                shape.setDColor(arBoardConfig.getBrushColor());
                shape.setDCanvasWidth(arBoardConfig.getBoardWidth());
                shape.setDCanvasHeight(arBoardConfig.getBoardHeight());
                break;
            case ARBoardConfig.BrushModel.Rect:
                shape = new RectShape(downX, downY, paint);
                shape.setDColor(arBoardConfig.getBrushColor());
                shape.setDCanvasWidth(arBoardConfig.getBoardWidth());
                shape.setDCanvasHeight(arBoardConfig.getBoardHeight());
                break;
            case ARBoardConfig.BrushModel.Arrow:
                shape = new JiantouShape(downX, downY, paint);
                shape.setDColor(arBoardConfig.getBrushColor());
                shape.setDCanvasWidth(arBoardConfig.getBoardWidth());
                shape.setDCanvasHeight(arBoardConfig.getBoardHeight());
                break;
            default:
                shape = new PointShape(downX, downY, paint);
                shape.setDColor(arBoardConfig.getBrushColor());
                shape.setDCanvasWidth(arBoardConfig.getBoardWidth());
                shape.setDCanvasHeight(arBoardConfig.getBoardHeight());
                break;
        }
        return shape;
    }

    public void doDraw() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        canvas = mSurfaceHolder.lockCanvas();
                        drawBg(canvas);

                        if (httpServer.getAllData().get(arBoardConfig.getCurrentBoardNum())!=null) {
                            drawSvrContent(canvas, httpServer.getAllData().get(arBoardConfig.getCurrentBoardNum()));

                        }
                        if (mShapes.size()>0){
                            Vector<PaintShape> data = new Vector<>();
                            for (PaintShape paintShape : mShapes) {
                                if (paintShape.getBoardNum() == arBoardConfig.getCurrentBoardNum()) {
                                    data.add(paintShape);
                                }
                            }
                            if (data.size()!=0) {
                                drawContent(canvas, data);
                            }
                        }
                    } catch (Exception e) {

                    } finally {
                        if (canvas != null) {
                            mSurfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }

                }
            }
        }).start();


    }

    public interface DrawByScreenShotListener {
        void drawFinish();
    }

    public void doDrawByScreenShot(final Canvas canvas, final DrawByScreenShotListener drawResutl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    drawSvrContent(canvas, httpServer.getAllData().get(arBoardConfig.getCurrentBoardNum()));
                    Vector<PaintShape> data = new Vector<>();
                    for (PaintShape paintShape : mShapes) {
                        if (paintShape.getBoardNum() == arBoardConfig.getCurrentBoardNum()) {
                            data.add(paintShape);
                        }
                    }
                    drawContent(canvas, data);
                    drawResutl.drawFinish();
                } catch (Exception e) {
                }

            }
        }).start();
    }


    /**
     * 绘制本地图形
     */
    private void drawContent(Canvas canvas, Vector<PaintShape> shapes) {
        if (shapes == null) {
            return;
        }
        for (PaintShape shape : shapes) {
            shape.onDraw(canvas);
        }
    }

    /**
     * 绘制服务端发送过来的图形
     */
    private void drawSvrContent(Canvas canvas, Vector<PaintShape> shapes) {
        if (shapes == null) {
            return;
        }
        for (PaintShape shape : shapes) {
            shape.onDraw(canvas);
        }
    }

    private void drawBg(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清屏
    }

    public void removeLastOne() {
        Vector<PaintShape> localPanit = LocalPaintData.get(arBoardConfig.getCurrentBoardNum());
        if (localPanit != null) {//本地找
            if (localPanit.size() > 0) {//本地找最后一条
                httpServer.removeLast(localPanit.lastElement().getLocalBoardId());
            } else {//服务器拉的数据里找
                Vector<PaintShape> serverPanit = httpServer.getAllData().get(arBoardConfig.getCurrentBoardNum());
                if (serverPanit == null) {
                    return;
                }
                if (serverPanit.size() > 0) {
                    Vector<PaintShape> yourServerData = new Vector<>();
                    for (PaintShape shape : serverPanit) {
                        if (shape.getLocalBoardId().contains(ARBoardConfig.mUserId)) {
                            yourServerData.add(shape);
                        }
                    }
                    if (yourServerData.size() > 0) {//找到了所有你自己画过的 取最后一条删除
                        httpServer.removeLast(yourServerData.lastElement().getLocalBoardId());
                    }
                }
            }
        } else {//服务器拉的数据里找
            Vector<PaintShape> serverPanit = httpServer.getAllData().get(arBoardConfig.getCurrentBoardNum());
            if (serverPanit == null) {
                return;
            }
            if (serverPanit.size() > 0) {
                Vector<PaintShape> yourServerData = new Vector<>();
                for (PaintShape shape : serverPanit) {
                    if (shape.getLocalBoardId().contains(ARBoardConfig.mUserId)) {
                        yourServerData.add(shape);
                    }
                }
                if (yourServerData.size() > 0) {//找到了所有你自己画过的 取最后一条删除
                    httpServer.removeLast(yourServerData.lastElement().getLocalBoardId());
                }
            }
        }
    }

    public void cleanCurrentBoard() {
        if (mShapes != null && mShapes.size() > 0) {
            for (Iterator<PaintShape> it = mShapes.iterator(); it.hasNext(); ) {
                PaintShape paintShape = it.next();
                if (paintShape.getBoardNum() == arBoardConfig.getCurrentBoardNum()) {
                    it.remove();
                }
            }
        }
        LocalPaintData.remove(arBoardConfig.getCurrentBoardNum());//移除本地
        httpServer.getAllData().remove(arBoardConfig.getCurrentBoardNum());//一处远程
        httpServer.cleanCurrentBoard();
        doDraw();
    }

    public void cleanAllBoard() {
        Clean();
        httpServer.getAllData().clear();//一处远程
        httpServer.cleanAllBoard();
        doDraw();
    }

    public void onServerChange(boolean isInit) {
        if (isInit) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        doDraw();

    }

    public void onBoardClean(int boardNum) {
        if (boardNum == 0) {
            Clean();
            httpServer.getAllData().clear();//一处远程
        } else {
            for (Iterator<PaintShape> it = mShapes.iterator(); it.hasNext(); ) {
                PaintShape paintShape = it.next();
                if (paintShape.getBoardNum() == boardNum) {
                    it.remove();
                }
            }
            LocalPaintData.remove(boardNum);
            httpServer.getAllData().remove(boardNum);
        }
        doDraw();
    }

    public void onUndoSuccess(int boardNum, String localId) {
        boolean hadFindInLocal = false;
        Vector<PaintShape> localPanit = LocalPaintData.get(boardNum);
        if (localPanit != null && localPanit.size() > 0) {
            for (PaintShape shape : localPanit) {
                if (shape.getLocalBoardId().equals(localId)) {
                    mShapes.removeElement(shape);
                    localPanit.removeElement(shape);
                    hadFindInLocal = true;
                    break;
                }
            }
        }
        if (!hadFindInLocal) {//如果在本地集合没找到
            Vector<PaintShape> ServerPanit = httpServer.getAllData().get(boardNum);
            if (ServerPanit != null && ServerPanit.size() > 0) {
                for (PaintShape shape : ServerPanit) {
                    if (shape.getLocalBoardId().equals(localId)) {
                        ServerPanit.removeElement(shape);
                        break;
                    }
                }
            }
        }
        doDraw();
    }

    public void onUndoFaild() {

    }

    public void cleanAll() {
        Clean();
    }


    public void Clean() {
        mShapes.clear();
        LocalPaintData.clear();
    }


    public static void screen(Bitmap bitmap) {
        try {
            if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {//内置存储卡不存在
                Log.d("dog", "存储卡不存在");
                return;
            }

            String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
            Log.d("dog", str);
            final String dir = str + "/Camera"; //保存到相册
            File directory = new File(dir);
            if (!directory.exists()) {  //是否存在目录
                Log.d("dog", "路径不存在");
                directory = new File(str); //不存在就是用DCIM目录
            }
            directory.mkdirs();
            // final File smdir=directory; //提供引用 发送广播 ,刷新图库
            String name = System.currentTimeMillis() + ".png"; //图片名
            final File file = new File(directory, name);
            FileOutputStream fos = new FileOutputStream(file);

            final Boolean bol = bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
            fos.flush();
            fos.close();

//            MediaStore.Images.Media.insertImage(instance.getContentResolver(), file.getAbsolutePath(),
//                    "截图-20171018", "这是我的截图"); //刷新

            Log.d("dog", file.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}