package com.felix.rxwebsocket.sample;

import com.felix.rxwebsocket2.RxWebSocket;
import com.felix.rxwebsocket2.WebSocketConfig;
import com.felix.rxwebsocket2.WebSocketSubscriber;

import android.os.Bundle;
import android.support.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.WebSocket;
import okio.ByteString;

public class Rx2WebSocketActivity extends BaseRxWebSocketActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxWebSocket.toggleDebug(BuildConfig.DEBUG);
        RxWebSocket.initialize(Constant.WEB_SOCKET_URL,
                new WebSocketConfig.Builder()
                        .client(mOkHttpClient)
                        .reconnectInterval(3)
                        .retryTimes(3)
                        .build());
        RxWebSocket.get(Constant.WEB_SOCKET_URL)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new WebSocketSubscriber() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    protected void onOpen(@NonNull WebSocket webSocket) {
                        super.onOpen(webSocket);
                    }

                    @Override
                    protected void onMessage(@NonNull String text) {
                        super.onMessage(text);
                        appendMessage(text);
                    }

                    @Override
                    protected void onMessage(@NonNull ByteString byteString) {
                        super.onMessage(byteString);
                    }

                    @Override
                    protected void onReconnect() {
                        super.onReconnect();
                    }

                    @Override
                    protected void onClose() {
                        super.onClose();
                    }
                });
    }

    @Override
    public void sendMessage(String message) {
        RxWebSocket.syncSendMessage(Constant.WEB_SOCKET_URL, message);
    }
}
