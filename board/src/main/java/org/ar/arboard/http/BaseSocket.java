package org.ar.arboard.http;

import com.google.gson.Gson;

import org.ar.arboard.ARBoardEngine;
import org.ar.arboard.utils.LogUtil;
import org.ar.arboard.boardevent.WhiteBoardServerListener;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

/**
 * Created by liuxiaozhong on 2017/11/16.
 */

public class BaseSocket {

    private final static String SERVER_URL = String.format(ARBoardEngine.Inst().isHttps ? "https://%s:%s": "http://%s:%s", ARBoardEngine.Inst().address, ARBoardEngine.Inst().port);

    public static Gson gson;

    protected Socket socket;

    protected WhiteBoardServerListener whiteBoardServerListener;

    protected SSLContext mySSLContext;

    protected IO.Options opts;

    private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }};

    private HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    public BaseSocket() throws URISyntaxException {
        gson = new Gson();
        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
        try {
            mySSLContext = SSLContext.getInstance("TLS");
            try {
                mySSLContext.init(null, trustAllCerts, null);
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder().hostnameVerifier(myHostnameVerifier).sslSocketFactory(mySSLContext.getSocketFactory(), trustManager).build();
        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
        IO.setDefaultOkHttpCallFactory(okHttpClient);

        opts = new IO.Options();
        opts.callFactory = okHttpClient;
        opts.webSocketFactory = okHttpClient;
        socket = IO.socket(SERVER_URL,
                opts);
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
            LogUtil.d("连接画板服务器", "连接成功");
        }
    };

    public Emitter.Listener disconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (whiteBoardServerListener != null) {
                whiteBoardServerListener.onBoardDisconnect();
            }
            LogUtil.d("断开画板服务器", "已断开");
        }
    };

    public Emitter.Listener reconnect_attempt = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("尝试重新连接...", "正在尝试重新连接...");
        }
    };

    public Emitter.Listener reconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("重新连接...", "正在重新连接...");
        }
    };
    public Emitter.Listener connect_timeout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("连接超时...", "连接超时...");
        }
    };

    public Emitter.Listener connect_error = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtil.d("连接失败...", "连接失败...");
        }
    };


}
