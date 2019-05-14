package org.ar.arboard.http;

import com.google.gson.Gson;

import org.ar.arboard.utils.LogUtil;
import org.ar.arboard.boardevent.WhiteBoardServerListener;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by liuxiaozhong on 2017/11/16.
 */

public class BaseSocket {

    public static Gson gson;

    protected Socket socket;

    protected WhiteBoardServerListener whiteBoardServerListener;
        private final static String SERVER_URL= "http://board.anyrtc.cc:2662";
//private final static String SERVER_URL= "http://192.168.1.111:2662";
//    private final static String SERVER_URL = "http://192.168.1.112:2662";

    public BaseSocket() throws URISyntaxException {

        gson = new Gson();
        socket = IO.socket(SERVER_URL);
        socket.on(Socket.EVENT_CONNECT, connect);
        socket.on(Socket.EVENT_DISCONNECT, disconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, connect_error);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, connect_timeout);
        socket.on(Socket.EVENT_RECONNECT, reconnect);
        socket.on(Socket.EVENT_RECONNECT_ATTEMPT, reconnect_attempt);

    }



    public Emitter.Listener connect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("连接画板服务器","连接成功");
        }
    };

    public Emitter.Listener disconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (whiteBoardServerListener!=null) {
                whiteBoardServerListener.onBoardDisconnect();
            }
            LogUtil.d("断开画板服务器","已断开");
        }
    };

    public Emitter.Listener reconnect_attempt = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("尝试重新连接...","正在尝试重新连接...");
        }
    };

    public Emitter.Listener reconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("重新连接...","正在重新连接...");
        }
    };
    public Emitter.Listener connect_timeout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("连接超时...","连接超时...");
        }
    };

    public Emitter.Listener connect_error = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("连接失败...","连接失败...");
        }
    };



}
