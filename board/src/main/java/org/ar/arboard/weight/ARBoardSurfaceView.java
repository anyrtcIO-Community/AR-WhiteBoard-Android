package org.ar.arboard.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.ar.arboard.R;
import org.ar.arboard.boardevent.ARBoardListener;
import org.ar.arboard.boardevent.WhiteBoardServerListener;
import org.ar.arboard.boardevent.WhiteBoardWHChangeListener;
import org.ar.arboard.brushmodel.PaintView;
import org.ar.arboard.http.HttpServer;
import org.ar.arboard.imageloader.ARBoardScreenShotResult;
import org.ar.arboard.imageloader.DepthPageTransformer;
import org.ar.arboard.imageloader.ImageLoaderInterface;
import org.ar.arboard.imageloader.QuickPageAdapter;
import org.ar.arboard.utils.ARBoardCode;
import org.ar.arboard.utils.ARBoardConfig;
import org.ar.arboard.utils.LogUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxiaozhong on 2017/11/16.
 */

public class ARBoardSurfaceView extends FrameLayout {

    private View view;//画板加下面的内容控件

    private ARViewpager viewPager;

    private PaintView paintView;

    private List<ARPinchImageView> imageViewList = new ArrayList<>();

    private Context context;

    private ImageLoaderInterface imageLoader;

    private QuickPageAdapter<ARPinchImageView> viewPagerAdapter;

    private HttpServer httpServer;

    private ARBoardConfig arBoardConfig;

    private ARBoardListener whiteBoardListener;

    private List<Object> imageUrlList = new ArrayList<>();

    private boolean switchHadEnd = true;

    private String TEMPanyRTCId, TEMPfileId, TEMPuserId;

    private List<String> TEMPimageList;

    private boolean hadSetData=false;
    private boolean hadCheckAnyInfo=false;
    public ARBoardSurfaceView(Context context) {
        super(context);
        init(context);
        //
    }

    public ARBoardSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ARBoardSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ARBoardConfig getARBoardConfig() {
        return arBoardConfig;
    }

    /**
     * 设置图片加载器
     *
     * @param imageLoader
     */
    public void setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
    }


    public void getCurrentSnapShotImage(final ARBoardScreenShotResult result) {
        final Bitmap cacheBitmapFromView = getCacheBitmapFromView(viewPager);
        Canvas canvas = new Canvas(cacheBitmapFromView);
        paintView.doDrawByScreenShot(canvas, new PaintView.DrawByScreenShotListener() {
            @Override
            public void drawFinish() {
                result.result(cacheBitmapFromView);
            }
        });
    }


    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

    private void init(Context context) {

        this.context = context;

        arBoardConfig = new ARBoardConfig();

        view = LayoutInflater.from(context).inflate(R.layout.white_board_layout, this);

        viewPager = (ARViewpager) view.findViewById(R.id.vp);

        viewPager.setARBoardConfig(arBoardConfig);

        paintView = (PaintView) view.findViewById(R.id.paint_view);

        paintView.setZOrderOnTop(true);

        paintView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        viewPager.setPageTransformer(true, new DepthPageTransformer());

        paintView.setARBoardConfig(arBoardConfig);

        try {
            httpServer = new HttpServer();
            httpServer.setWhiteBoardServerListener(whiteBoardServerListener);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        httpServer.setARBoardConfig(arBoardConfig);

        paintView.setHttpServer(httpServer);

        paintView.setWhChangeListener(new WhiteBoardWHChangeListener() {
            @Override
            public void boardWDChange(int width, int height) {
                Log.d("--------------", "boardWDChange " + width + "boardWDChange" + height);
                httpServer.initAnyRTC();
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                arBoardConfig.setCurrentBoardNum((position + 1));
                paintView.doDraw();
                if (imageUrlList.size() > position) {
                    whiteBoardListener.onBoardPageChange(position + 1, viewPagerAdapter.getCount(), (String) imageUrlList.get(position));
                } else {
                    whiteBoardListener.onBoardPageChange(position + 1, viewPagerAdapter.getCount(), "");
                }
                if (arBoardConfig.getBrushModel() == ARBoardConfig.BrushModel.TransformSync) {
                    if (switchHadEnd) {
                        if (viewPager.isTouched()) {
                            httpServer.switchBoard(arBoardConfig.getCurrentBoardNum());
                            switchHadEnd = false;
                            viewPager.setTouched(false);
                        }
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void initWithRoomId(String anyRTCId, String fileId, String userId, List<String> imageList) {
        if (TextUtils.isEmpty(anyRTCId) || TextUtils.isEmpty(fileId) || TextUtils.isEmpty(userId) || imageList.size() == 0) {
            LogUtil.d("initWithRoomId", "初始化失败，请检查初始化参数是否为空");
            whiteBoardListener.onBoardError(ARBoardCode.ParameterEmpty.code);
            return;
        }
        if (!anyRTCId.matches("^[A-Za-z0-9]+$")||!fileId.matches("^[A-Za-z0-9]+$")||!userId.matches("^[A-Za-z0-9]+$")){
            LogUtil.d("initWithRoomId", "参数不合法");
            whiteBoardListener.onBoardError(ARBoardCode.ParameterError.code);
            return;
        }
        this.TEMPanyRTCId = anyRTCId;
        this.TEMPfileId = fileId;
        this.TEMPuserId = userId;
        this.TEMPimageList = imageList;
        paintView.Clean();
        hadSetData=true;
       if (hadCheckAnyInfo){
           initBoard(TEMPanyRTCId, TEMPfileId, TEMPuserId, TEMPimageList);
       }

    }

    private void initBoard(String anyRTCId, String fileId, String userId, List<String> imageList) {
        JSONArray boardArray = new JSONArray();
        for (int i = 0; i < imageList.size(); i++) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("board_number", i + 1);
                jsonObject.put("board_background", imageList.get(i));
                boardArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        httpServer.initAllBoard(fileId, anyRTCId, boardArray, userId);
    }


    public void setWhiteBoardListener(ARBoardListener whiteBoardListener) {
        this.whiteBoardListener = whiteBoardListener;
    }


    /**
     * 移除上一笔
     */
    public void undo() {
        paintView.removeLastOne();
    }

    /**
     * 移出当前涂鸦
     */
    public void cleanCurrentDraw() {
        paintView.cleanCurrentBoard();
    }

    /**
     * 移除所有 画板涂鸦
     */
    public void cleanAllDraws() {
        paintView.cleanAllBoard();
    }


    public void updateCurrentBgImage(String background) {
        httpServer.updataBoardBackground(arBoardConfig.getCurrentBoardNum(), background);
    }


    public int nextPage(boolean isSync) {
        if (viewPagerAdapter == null) {
            return -1;
        }
        if (isSync) {
            if (!switchHadEnd) {
                return -1;
            }
        }
        int curIndex = viewPager.getCurrentItem();
        int totle = viewPagerAdapter.getCount();
        if (curIndex + 1 <= totle - 1) {
            viewPager.setCurrentItem(curIndex + 1);
            if (isSync) {
                httpServer.switchBoard(viewPager.getCurrentItem() + 1);
                switchHadEnd = false;
            }
            return (viewPager.getCurrentItem() + 1);
        } else {
            return totle;
        }
    }

    public int prePage(boolean isSync) {
        if (isSync) {
            if (!switchHadEnd) {
                return -1;
            }
        }
        int curIndex = viewPager.getCurrentItem();//2
        if (curIndex > 0) {
            viewPager.setCurrentItem(curIndex - 1);
            if (isSync) {
                httpServer.switchBoard(curIndex);
                switchHadEnd = false;
            }
            return (viewPager.getCurrentItem() + 1);
        } else {
            return curIndex;
        }
    }

    public int switchPage(int pageNum, boolean isSync) {
        if (pageNum <= viewPagerAdapter.getCount()) {
            viewPager.setCurrentItem(pageNum - 1);
            if (isSync) {
                httpServer.switchBoard(pageNum);
                switchHadEnd = false;
            }
            return pageNum;
        } else {
            return 0;
        }

    }

    private void SetImage(final List<?> imageList, boolean isInit, int pageNum) {
        imageViewList.clear();
        imageUrlList.clear();

        imageUrlList.addAll(imageList);
        ARPinchImageView imageView = null;
        if (imageList == null || imageList.size() == 0) {
            whiteBoardListener.onBoardError(ARBoardCode.ParameterEmpty.code);
            LogUtil.d("初始化失败，请检查初始化参数是否为空", "");
            return;
        } else {
            for (int i = 0; i < imageList.size(); i++) {
                if (imageLoader != null) {
                    imageView = (ARPinchImageView) imageLoader.createImageView(context);
                }
                Object url = null;
                url = imageList.get(i);
                if (imageLoader != null) {
                    if (!TextUtils.isEmpty(url.toString())) {
                        if (url.toString().length() > 1) {
                            if (url.toString().startsWith("#")) {
                                imageView.setBackgroundColor(Color.parseColor(url.toString()));
                            } else {
                                imageLoader.displayImage(context, url, imageView);
                            }
                        }
                    }
                } else {
                    whiteBoardListener.onBoardError(ARBoardCode.ImageLoaderIsNull.code);
                    LogUtil.d("初始化失败，图片加载器不能为空", "");
                    return;
                }
                imageViewList.add(imageView);
            }
        }
        viewPagerAdapter = new QuickPageAdapter<ARPinchImageView>(imageViewList);
        viewPager.setAdapter(viewPagerAdapter);
        if (isInit) {
            whiteBoardListener.onBoardPageChange(1, viewPagerAdapter.getCount(), (String) imageUrlList.get(0));
        } else {
            whiteBoardListener.onBoardPageChange(pageNum, viewPagerAdapter.getCount(), (String) imageUrlList.get(pageNum - 1));
            if (pageNum <= viewPagerAdapter.getCount()) {
                int index = pageNum - 1;
                viewPager.setCurrentItem(index);
            }

        }

    }

    public void addBoard(boolean isBefore, String imageUrl) {
        httpServer.addBoard(isBefore ? 1 : 0, imageUrl);
    }

    public void deleteCurrentBoard() {
        if (imageViewList == null) {
            return;
        }
        if (imageViewList.size() > 1) {
            httpServer.delBoard(arBoardConfig.getCurrentBoardNum());
        }
    }

    public void sendMessage(String message) {
        if (message.isEmpty()) {
            return;
        }
        httpServer.sendMessage(message);
    }

    public PaintView getPaintView() {
        return paintView;
    }

    public WhiteBoardServerListener whiteBoardServerListener = new WhiteBoardServerListener() {
        @Override
        public void initBoardSuccess() {
            whiteBoardListener.initBoardSuccess();
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (viewPager!=null&&paintView!=null) {
                        viewPager.setVisibility(VISIBLE);
                        paintView.setVisibility(VISIBLE);
                    }
                }
            });
        }

        @Override
        public void initBoardFaild(int code) {
            if (whiteBoardListener!=null) {
                whiteBoardListener.onBoardError(code);
            }
        }

        @Override
        public void switchPage(final int i, final boolean isInit) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (viewPagerAdapter != null && viewPager != null&& arBoardConfig !=null) {
                        if (isInit) {
                            if (i <= viewPagerAdapter.getCount()) {
                                arBoardConfig.setCurrentBoardNum(i);
                                int index = i - 1;
                                viewPager.setCurrentItem(index);
                            }
                            return;
                        }
                        switchHadEnd = true;
                        if (i == arBoardConfig.getCurrentBoardNum()) {
                            return;
                        }
                        if (i <= viewPagerAdapter.getCount()) {
                            int index = i - 1;
                            viewPager.setCurrentItem(index);
                        }
                    }
                }
            });
        }

        @Override
        public void initAnyRTCSuccess() {
            if (arBoardConfig !=null) {
                arBoardConfig.setInitAnyrtcSuccess(true);
            }
            hadCheckAnyInfo=true;
            if (hadSetData){
                initBoard(TEMPanyRTCId, TEMPfileId, TEMPuserId, TEMPimageList);
            }

        }

        @Override
        public void initAnyRTCFaild(int code) {
            if (arBoardConfig !=null) {
                arBoardConfig.setInitAnyrtcSuccess(false);
            }
            if (whiteBoardListener!=null) {
                whiteBoardListener.onBoardError(code);
            }
        }


        @Override
        public void onAddBoardSuccess(final List<String> imageUrl, final int curPageNum, boolean isAdd) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    SetImage(imageUrl, false, curPageNum);
                }
            });
        }

        @Override
        public void onBoardDestroy() {
            if (whiteBoardListener!=null) {
                whiteBoardListener.onBoardDestroy();
            }
        }

        @Override
        public void onNeedDisplay(final List<String> imageUrl) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    SetImage(imageUrl, true, 1);
                }
            });

        }

        @Override
        public void onBoardDisconnect() {
            if (whiteBoardListener!=null) {
                whiteBoardListener.onBoardServerDisconnect();
            }
        }

        @Override
        public void onChangeTimestamp(long time) {
            if (whiteBoardListener!=null) {
                whiteBoardListener.onBoardDrawsChangeTimestamp(time);
            }
        }

        @Override
        public void onMessage(String msgData) {
            if (whiteBoardListener!=null) {
                whiteBoardListener.onBoardMessage(msgData);
            }
        }

        @Override
        public void onServerChange(boolean isInit) {
            if (paintView!=null) {
                paintView.onServerChange(isInit);
            }
        }

        @Override
        public void onBoardClean(int boardNum) {
            if (paintView!=null) {
                paintView.onBoardClean(boardNum);
            }
        }

        @Override
        public void onUndoSuccess(int boardNum, String localId) {
            if (paintView!=null) {
                paintView.onUndoSuccess(boardNum, localId);
            }
        }

        @Override
        public void onUndoFaild() {
            if (paintView!=null) {
                paintView.onUndoFaild();
            }
        }

        @Override
        public void cleanAll() {
            if (paintView!=null) {
                paintView.cleanAll();
            }
        }
    };

    public void destoryBoard() {
        httpServer.boardDestory();
    }

    public void leave() {
        httpServer.disconnect();
        paintView.Clean();
    }
}
