package org.ar.arboard.http;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.ar.arboard.bean.OtherPushBean;
import org.ar.arboard.bean.PaintBean;
import org.ar.arboard.bean.SyncBean;
import org.ar.arboard.boardevent.WhiteBoardServerListener;
import org.ar.arboard.brushmodel.JiantouShape;
import org.ar.arboard.brushmodel.LineShape;
import org.ar.arboard.brushmodel.PaintShape;
import org.ar.arboard.brushmodel.PointShape;
import org.ar.arboard.brushmodel.RectShape;
import org.ar.arboard.utils.ARBoardConfig;
import org.ar.arboard.utils.DevConfig;
import org.ar.arboard.utils.LogUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import io.socket.emitter.Emitter;

public class HttpServer extends BaseSocket {
    private  Vector<PaintShape> mNeedUploadData;

    private Context context;
    int boardIndex = 0;
    private  HashMap<Integer, Vector<PaintShape>> allData = new HashMap<>();
    private List<String> newImageUrl;
    
    private ARBoardConfig arBoardConfig;

    public HttpServer() throws URISyntaxException {
        this.context = context;
        socket.on("update_board", update_board);
        socket.on("init_all_board", init_all_board);
        socket.on("init_board", init_board);
        socket.on("clear_single_board", clear_single_board);
        socket.on("clear_all_board", clear_all_board);
        socket.on("revoke_board", revoke_board);
        socket.on("push_board", push_board);
        socket.on("update_board_background", update_board_background);
        socket.on("switch_board", switch_board);
        socket.on("init_anyrtc_success", init_anyrtc_success);
        socket.on("init_anyrtc_failed", init_anyrtc_failed);
        socket.on("add_board",add_board);
        socket.on("delete_board",delete_board);
        socket.on("destroy_board",destroy_board);
        socket.on("message", message);
        socket.connect();
    }


    public void setWhiteBoardServerListener(WhiteBoardServerListener whiteBoardServerListener) {
        this.whiteBoardServerListener = whiteBoardServerListener;
    }

    public void setARBoardConfig(ARBoardConfig arBoardConfig) {
        this.arBoardConfig = arBoardConfig;
    }

    /**
     * Listener 事件监听
     */


    private Emitter.Listener init_anyrtc_success = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("验证开发者信息成功",args[0].toString());
            arBoardConfig.setInitSuccess(true);
            whiteBoardServerListener.initAppInfoSuccess();
        }
    };

    private Emitter.Listener init_anyrtc_failed = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("验证开发者信息失败",args[0].toString());
            arBoardConfig.setInitSuccess(false);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(args[0].toString());
                int code = jsonObject.getInt("code");
                whiteBoardServerListener.initAppInfoFaild(code);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    private Emitter.Listener init_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (args.length > 0) {
                LogUtil.d("初始化白板成功",args[0].toString());
                hanldeUpdateData(true, args[0].toString(), false);
            }
        }
    };

    private Emitter.Listener init_all_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("初始化白板",args[0].toString());
            if (args.length > 0) {
                hanldeUpdateData(true, args[0].toString(), false);
            }
        }
    };

    private Emitter.Listener push_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("发送画笔数据成功",args[0].toString());
        }
    };

    private Emitter.Listener update_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            handlerOtherPush(args[0].toString());
            LogUtil.d("收到画笔数据",args[0].toString());
        }
    };

    private Emitter.Listener switch_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject jsonObject = new JSONObject(args[0].toString());
                int page = jsonObject.getJSONObject("switch_info").getInt("board_number");
                whiteBoardServerListener.switchPage(page, false);
                LogUtil.d("翻页 当前页："+page,args[0].toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener update_board_background = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("更新背景",args[0].toString());
            try {
                JSONObject jsonObject=new JSONObject(args[0].toString());
                JSONObject result=new JSONObject(jsonObject.getString("result"));
                long time=jsonObject.getLong("current_time");
                whiteBoardServerListener.onChangeTimestamp(time);
                int boardNum=result.getInt("board_number");
                String board=result.getString("board_background");
                for (int i=0;i<newImageUrl.size();i++){
                    if (i==(boardNum-1)){
                        newImageUrl.set(i,board);
                    }
                }
                whiteBoardServerListener.onAddBoardSuccess(newImageUrl, arBoardConfig.getCurrentBoardNum(),false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener message = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("收到自定义消息",args[0].toString());
            try {
                JSONObject jsonObject=new JSONObject(args[0].toString());
                if (!jsonObject.isNull("msg_data")){
                    whiteBoardServerListener.onMessage(jsonObject.getString("msg_data"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private Emitter.Listener revoke_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (TextUtils.isEmpty(args[0].toString())) {
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(args[0].toString());
                int code = jsonObject.getInt("code");
                long time=jsonObject.getLong("current_time");
                whiteBoardServerListener.onChangeTimestamp(time);
                String localId = new JSONObject(jsonObject.getJSONObject("result").getJSONArray("revoke_board").get(0).toString()).getString("board_localid");
                int boardNum = new JSONObject(jsonObject.getJSONObject("result").getJSONArray("revoke_board").get(0).toString()).getInt("board_number");
                if (code == 0) {
                    whiteBoardServerListener.onUndoSuccess(boardNum, localId);
                    LogUtil.d("撤销成功",args[0].toString());
                } else {
                    LogUtil.d("撤销失败",args[0].toString());
                    whiteBoardServerListener.onUndoFaild();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                whiteBoardServerListener.onUndoFaild();
            }
        }
    };

    private Emitter.Listener destroy_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (TextUtils.isEmpty(args[0].toString())) {
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(args[0].toString());
                int code = jsonObject.getInt("code");
                if (code==0){
                    whiteBoardServerListener.onBoardDestroy();
                }
                LogUtil.d("销毁画板",args[0].toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };




    private Emitter.Listener clear_single_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("清除当前页画笔数据成功",args[0].toString());

            try {
                String boardNum = new JSONObject(args[0].toString()).getJSONObject("clear_info").getString("board_number");
                if (whiteBoardServerListener != null) {
                    whiteBoardServerListener.onBoardClean(Integer.parseInt(boardNum));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(args[0].toString());
                long time=jsonObject.getLong("current_time");
                whiteBoardServerListener.onChangeTimestamp(time);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private Emitter.Listener clear_all_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
                if (whiteBoardServerListener != null) {
                    whiteBoardServerListener.onBoardClean(0);
                }
            try {
                JSONObject jsonObject = new JSONObject(args[0].toString());
                long time=jsonObject.getLong("current_time");
                whiteBoardServerListener.onChangeTimestamp(time);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private Emitter.Listener add_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("添加一页",args[0].toString());
            if (args.length > 0) {
                hanldeAddBoardData(args[0].toString(),true);
            }
        }
    };

    private Emitter.Listener delete_board = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("删除一页",args[0].toString());
            if (args.length > 0) {
                hanldeAddBoardData(args[0].toString(),false);
            }
        }
    };

    /**
     * Action 事件
     */


    public void initAllBoard(String board_seqid, String anyRTCId, JSONArray boardArray, String userId) {
        allData.clear();
        whiteBoardServerListener.cleanAll();
        arBoardConfig.setBoardInfo(board_seqid, anyRTCId, userId);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("board_seqid", board_seqid);
            jsonObject.put("board_anyrtcid", anyRTCId);
            jsonObject.put("board_array", boardArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("init_all_board", jsonObject.toString());
    }

    public void initBoard(String board_seqid, String anyRTCId, String userId) {
        arBoardConfig.setBoardInfo(board_seqid, anyRTCId, userId);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("board_seqid", board_seqid);
            jsonObject.put("board_anyrtcid", anyRTCId);
            jsonObject.put("board_background", "");
            jsonObject.put("board_number", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("init_board", jsonObject.toString());
    }
    public  PaintShape generateShape(PaintBean mPaintBean) throws Exception {
        PaintShape shape = null;

        //解析画笔
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(mPaintBean.getDColor()));
        paint.setStrokeWidth(mPaintBean.getDWidth());
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);
        switch (mPaintBean.getDType()) {
            case ARBoardConfig.BrushModel.Graffiti:
                shape = new PointShape(mPaintBean.getDStartX(), mPaintBean.getDStartY(), paint);
                shape.setDColor(arBoardConfig.getBrushColor());
                shape.setDCanvasWidth(arBoardConfig.getBoardWidth());
                shape.setDCanvasHeight(arBoardConfig.getBoardHeight());
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonArray = jsonParser.parse(mPaintBean.getDPoint()).getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonArray position = jsonArray.get(i).getAsJsonArray();
                    shape.move(getSuitablePos(mPaintBean.getDCanvasWidth(), position.get(0).getAsFloat()), getSuitablePos(mPaintBean.getDCanvasWidth(), position.get(1).getAsFloat()));
                }
                shape.setmPoints(mPaintBean.getDPoint());
                break;
            case ARBoardConfig.BrushModel.Line:
                shape = new LineShape(mPaintBean.getDStartX(), mPaintBean.getDStartY(), paint);
                shape.setDColor(arBoardConfig.getBrushColor());
                shape.setDCanvasWidth(arBoardConfig.getBoardWidth());
                shape.setDCanvasHeight(arBoardConfig.getBoardHeight());
                break;
            case ARBoardConfig.BrushModel.Rect:
                shape = new RectShape(mPaintBean.getDStartX(), mPaintBean.getDStartY(), paint);
                shape.setDColor(arBoardConfig.getBrushColor());
                shape.setDCanvasWidth(arBoardConfig.getBoardWidth());
                shape.setDCanvasHeight(arBoardConfig.getBoardHeight());
                break;
            case ARBoardConfig.BrushModel.Arrow:
                shape = new JiantouShape(mPaintBean.getDStartX(), mPaintBean.getDStartY(), paint);
                shape.setDColor(arBoardConfig.getBrushColor());
                shape.setDCanvasWidth(arBoardConfig.getBoardWidth());
                shape.setDCanvasHeight(arBoardConfig.getBoardHeight());
                break;
            default:
                break;
        }

        shape.setmEndX(mPaintBean.DEndX);
        shape.setmEndY(mPaintBean.DEndY);
        return shape;
    }
    private void hanldeUpdateData(boolean isInit, String data, boolean isSingBoard) {
        if (TextUtils.isEmpty(data)) {
            return;
        }

        SyncBean syncBean = gson.fromJson(data, SyncBean.class);
        if (syncBean.getCode() == 0) {
            whiteBoardServerListener.initBoardSuccess();
        } else {
            LogUtil.d("初始化画板失败","errorCode:"+syncBean.getCode());
            whiteBoardServerListener.initBoardFaild(syncBean.getCode());
            return;
        }
        if (syncBean.getDoc_info() != null && syncBean.getBoard_info() != null) {
            newImageUrl = new ArrayList<>();
            if (syncBean.getBoard_info() != null && syncBean.getBoard_info().size() > 0) {
                for (SyncBean.BoardInfoBean boardInfoBean : syncBean.getBoard_info()) {
                    newImageUrl.add(boardInfoBean.getSys_board_background());
                    try {
                        JSONArray jsonArray = new JSONArray(boardInfoBean.getBoard_info_list());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String PaintData = jsonArray.getJSONObject(i).getString("board_data");
                            String localId = jsonArray.getJSONObject(i).getString("board_localid");
                            try {
                                PaintBean paintBean = gson.fromJson(PaintData, PaintBean.class);
                                paintBean.setDStartX(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDStartX()));
                                paintBean.setDEndX(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDEndX()));
                                paintBean.setDStartY(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDStartY()));
                                paintBean.setDEndY(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDEndY()));
                                paintBean.setDWidth(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDWidth()));
                                PaintShape paintShape = generateShape(paintBean);
                                paintShape.setLocalBoardId(localId);
                                if (!allData.containsKey(boardInfoBean.getSys_board_number())) {
                                    Vector<PaintShape> newData = new Vector<>();
                                    newData.add(paintShape);
                                    allData.put(boardInfoBean.getSys_board_number(), newData);
                                } else {
                                    Vector<PaintShape> newData = allData.get(boardInfoBean.getSys_board_number());
                                    newData.add(paintShape);
                                    allData.put(boardInfoBean.getSys_board_number(), newData);
                                }
                            } catch (Exception e) {
                                LogUtil.d("解析数据出错",e.getMessage()+"");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.d("解析数据出错",e.getMessage()+"");
                    }
                }
                whiteBoardServerListener.onNeedDisplay(newImageUrl);
                if (isInit) {
                    whiteBoardServerListener.switchPage(syncBean.getDoc_info().getSys_docs_curt_number(), true);
                }
                NotifyDataChange(true);
            }
        }
    }

    private void hanldeAddBoardData(String data,boolean isAdd) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        whiteBoardServerListener.cleanAll();
        getAllData().clear();
        SyncBean syncBean = gson.fromJson(data, SyncBean.class);
        whiteBoardServerListener.onChangeTimestamp(syncBean.getCurrent_time());
        if (syncBean.getDoc_info() != null && syncBean.getBoard_info() != null) {
            if (syncBean.getBoard_info() != null && syncBean.getBoard_info().size() > 0) {
               newImageUrl=new ArrayList<>();
                for (SyncBean.BoardInfoBean boardInfoBean : syncBean.getBoard_info()) {
                    newImageUrl.add(boardInfoBean.getSys_board_background());
                    try {
                        JSONArray jsonArray = new JSONArray(boardInfoBean.getBoard_info_list());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String PaintData = jsonArray.getJSONObject(i).getString("board_data");
                            String localId = jsonArray.getJSONObject(i).getString("board_localid");
                            try {
                                PaintBean paintBean = gson.fromJson(PaintData, PaintBean.class);
                                paintBean.setDStartX(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDStartX()));
                                paintBean.setDEndX(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDEndX()));
                                paintBean.setDStartY(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDStartY()));
                                paintBean.setDEndY(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDEndY()));
                                paintBean.setDWidth(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDWidth()));
                                PaintShape paintShape = generateShape(paintBean);
                                paintShape.setLocalBoardId(localId);
                                if (!allData.containsKey(boardInfoBean.getSys_board_number())) {
                                    Vector<PaintShape> newData = new Vector<>();
                                    newData.add(paintShape);
                                    allData.put(boardInfoBean.getSys_board_number(), newData);
                                } else {
                                    Vector<PaintShape> newData = allData.get(boardInfoBean.getSys_board_number());
                                    newData.add(paintShape);
                                    allData.put(boardInfoBean.getSys_board_number(), newData);
                                }
                            } catch (Exception e) {
                                LogUtil.d("解析数据出错",e.getMessage()+"");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.d("解析数据出错",e.getMessage()+"");
                    }
                }
                if (newImageUrl.size()>=syncBean.getDoc_info().getSys_docs_curt_number()) {
                    arBoardConfig.setCurrentBoardNum(syncBean.getDoc_info().getSys_docs_curt_number());
                    whiteBoardServerListener.onAddBoardSuccess(newImageUrl,syncBean.getDoc_info().getSys_docs_curt_number(),true);
                }else {
                    arBoardConfig.setCurrentBoardNum(newImageUrl.size());
                    whiteBoardServerListener.onAddBoardSuccess(newImageUrl,newImageUrl.size(),true);
                }
                NotifyDataChange(false);
                LogUtil.d((isAdd ?"添加":"删除")+"后",(isAdd ?"添加":"删除")+"后总页数"+newImageUrl.size()+"当前页："+syncBean.getDoc_info().getSys_docs_curt_number());
            }
        }
    }

    private void handlerOtherPush(String data) {

        if (TextUtils.isEmpty(data)) {
            return;
        }
        OtherPushBean pushBean = gson.fromJson(data, OtherPushBean.class);
        whiteBoardServerListener.onChangeTimestamp(pushBean.getCurrent_time());
        if (pushBean.getResult() != null && pushBean.getResult().size() > 0) {
            LogUtil.d("收到画笔数据","画笔数：" + pushBean.getResult().size());
            for (int i = 0; i < pushBean.getResult().size(); i++) {
                String localId = pushBean.getResult().get(i).getBoard_localid();
                try {
                    PaintBean paintBean = gson.fromJson(pushBean.getResult().get(i).getBoard_data(), PaintBean.class);
                    paintBean.setDStartX(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDStartX()));
                    paintBean.setDEndX(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDEndX()));
                    paintBean.setDStartY(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDStartY()));
                    paintBean.setDEndY(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDEndY()));
                    paintBean.setDWidth(getSuitablePos(paintBean.getDCanvasWidth(), paintBean.getDWidth()));
                    PaintShape paintShape =generateShape(paintBean);
                    paintShape.setLocalBoardId(localId);
                    if (!allData.containsKey(pushBean.getResult().get(i).getBoard_number())) {
                        Vector<PaintShape> newData = new Vector<>();
                        newData.add(paintShape);
                        allData.put(pushBean.getResult().get(i).getBoard_number(), newData);
                    } else {
                        Vector<PaintShape> newData = allData.get(pushBean.getResult().get(i).getBoard_number());
                        newData.add(paintShape);
                        allData.put(pushBean.getResult().get(i).getBoard_number(), newData);
                    }
                } catch (Exception e) {
                    LogUtil.d("解析数据错误",e.getMessage()+"");
                }
            }
            NotifyDataChange(false);
        }
    }


    public void pushData(Vector mLocalData) {
        mNeedUploadData = mLocalData;
        try {
            PaintShape shape = mNeedUploadData.remove(0);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("board_anyrtcid", arBoardConfig.getAnyRTCId());
            jsonObject.put("board_number", arBoardConfig.getCurrentBoardNum());
            jsonObject.put("board_seqid", arBoardConfig.getBoardSeqId());
            jsonObject.put("board_localid", shape.getLocalBoardId());
            JSONObject paintData = new JSONObject(shape.toString());
            jsonObject.put("board_data", paintData);
            LogUtil.d("上传画笔数据",jsonObject.toString());
            socket.emit("push_board", jsonObject);
            mNeedUploadData.clear();
            mNeedUploadData = null;
        } catch (Exception e) {
        }
    }


    public void switchBoard(int boardNum) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("board_anyrtcid", arBoardConfig.getAnyRTCId());
            jsonObject.put("board_number", boardNum);
            jsonObject.put("board_seqid", arBoardConfig.getBoardSeqId());
            LogUtil.d("切换画板","数据："+jsonObject.toString());
            socket.emit("switch_board", jsonObject);
        } catch (Exception e) {

        }
    }


   public void updataBoardBackground(int boardNum,String newBackground){
        if (boardNum<0||boardNum>newImageUrl.size()){
            return;
        }
       JSONObject jsonObject = new JSONObject();
       try {
           jsonObject.put("board_seqid", arBoardConfig.getBoardSeqId());
           jsonObject.put("board_anyrtcid", arBoardConfig.getAnyRTCId());
           jsonObject.put("board_background", newBackground);
           jsonObject.put("board_number",boardNum);
       } catch (JSONException e) {
           e.printStackTrace();
       }
       LogUtil.d("更新背景","数据："+jsonObject.toString());
       socket.emit("update_board_background", jsonObject.toString());
   }

   public void sendMessage(String message){
       JSONObject jsonObject = new JSONObject();
       try {
           jsonObject.put("board_seqid", arBoardConfig.getBoardSeqId());
           jsonObject.put("board_anyrtcid", arBoardConfig.getAnyRTCId());
           jsonObject.put("msg_data", message);
       } catch (JSONException e) {
           e.printStackTrace();
       }
       LogUtil.d("发送自定义消息","数据："+jsonObject.toString());
       socket.emit("message", jsonObject.toString());
   }


    public  HashMap<Integer, Vector<PaintShape>> getAllData() {
        return allData;
    }

    /**
     * 处理收到的数据
     */
    public void NotifyDataChange(boolean isInit) {
        if (whiteBoardServerListener != null) {
            whiteBoardServerListener.onServerChange(isInit);
        }
    }

    public void cleanCurrentBoard() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("board_anyrtcid", arBoardConfig.getAnyRTCId());
            jsonObject.put("board_number", arBoardConfig.getCurrentBoardNum());
            jsonObject.put("board_seqid", arBoardConfig.getBoardSeqId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d("清除当前页画笔数据","数据："+jsonObject.toString());
        socket.emit("clear_single_board", jsonObject.toString());
    }

    public void cleanAllBoard() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("board_anyrtcid", arBoardConfig.getAnyRTCId());
            jsonObject.put("board_seqid", arBoardConfig.getBoardSeqId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d("清除所有页画笔数据","数据："+jsonObject.toString());
        socket.emit("clear_all_board", jsonObject.toString());
    }

    public void removeLast(String boardid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("board_localid", boardid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d("撤销","数据："+jsonObject.toString());
        socket.emit("revoke_board", jsonObject.toString());
    }




    /**
     * 添加画板
     * @param isBefore  向前一页添加还是后一页
     * @param imageUrl 图片地址
     */
    public void addBoard(int isBefore,String imageUrl){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("is_before", isBefore);
            jsonObject.put("board_seqid", arBoardConfig.getBoardSeqId());
            jsonObject.put("board_anyrtcid", arBoardConfig.getAnyRTCId());
            jsonObject.put("board_number", arBoardConfig.getCurrentBoardNum());
            jsonObject.put("board_background", imageUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d("添加画板","数据："+jsonObject.toString());
        socket.emit("add_board", jsonObject.toString());
    }

    public void delBoard(int boardNum){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("board_seqid", arBoardConfig.getBoardSeqId());
            jsonObject.put("board_anyrtcid", arBoardConfig.getAnyRTCId());
            jsonObject.put("board_number", boardNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d("删除画板","数据："+jsonObject.toString());
        socket.emit("delete_board", jsonObject.toString());
    }


    public float getSuitablePos(float otherWidth, float postion) {
        float fMyWidth = arBoardConfig.getBoardWidth();
        return (fMyWidth / otherWidth) * postion;
    }

    public void boardDestory(){
        JSONObject jsonObject=new JSONObject();
        try {
        jsonObject.put("board_seqid", arBoardConfig.getBoardSeqId());
        jsonObject.put("board_anyrtcid", arBoardConfig.getAnyRTCId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtil.d("销毁画板","数据："+jsonObject.toString());
        socket.emit("destroy_board", jsonObject.toString());
    }

    public void init() {
        if (DevConfig.getInstance().DevInfoNotFull()) {
            throw new IllegalArgumentException("画板未配置AnyRTC信息！");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("anyrtc_appid", DevConfig.getInstance().getappid());
            jsonObject.put("anyrtc_apptoken", DevConfig.getInstance().getapptoken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("init_anyrtc", jsonObject.toString());
    }

    public void disconnect() {
        socket.off();
        socket.close();
        allData.clear();
    }
}
