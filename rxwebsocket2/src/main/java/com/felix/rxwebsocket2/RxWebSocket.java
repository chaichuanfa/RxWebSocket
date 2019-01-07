package com.felix.rxwebsocket2;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okio.ByteString;

/**
 * @author chaichuanfa
 */
public final class RxWebSocket {

    private OkHttpClient mOkHttpClient;

    private RxWebSocket() {
        //no instance
    }

    /**
     * Initialize each url once
     */
    public static void initialize(@NonNull String url, @NonNull WebSocketConfig config) {
        WebSocketProvider.getInstance().setWebSocketConfig(url, config);
    }

    public static void toggleDebug(boolean isDebug) {
        Logger.setDebug(isDebug);
    }

    public static void sendMessage(String url, String message) {
        WebSocketProvider.getInstance().sendMessage(url, message);
    }

    public static void sendMessage(String url, ByteString byteString) {
        WebSocketProvider.getInstance().sendMessage(url, byteString);
    }

    public static void syncSendMessage(String url, String message) {
        WebSocketProvider.getInstance().syncSendMessage(url, message);
    }

    public static void syncSendMessage(String url, ByteString byteString) {
        WebSocketProvider.getInstance().syncSendMessage(url, byteString);
    }

    public static Observable<WebSocketInfo> get(String url) {
        return WebSocketProvider.getInstance().observableWebSocket(url);
    }
}
