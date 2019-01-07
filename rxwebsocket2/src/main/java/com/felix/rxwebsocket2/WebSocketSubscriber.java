package com.felix.rxwebsocket2;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.WebSocket;
import okio.ByteString;

public class WebSocketSubscriber implements Observer<WebSocketInfo> {

    @CallSuper
    @Override
    public void onError(Throwable e) {
        Logger.e(e, "WebSocket error");
    }

    @Override
    public final void onComplete() {
        onClose();
    }

    @Override
    public final void onSubscribe(Disposable disposable) {
    }

    @Override
    public final void onNext(@NonNull WebSocketInfo webSocketInfo) {
        if (webSocketInfo.isOpen()) {
            onOpen(webSocketInfo.getWebSocket());
        } else if (!TextUtils.isEmpty(webSocketInfo.getMessage())) {
            onMessage(webSocketInfo.getMessage());
        } else if (webSocketInfo.getByteString() != null) {
            onMessage(webSocketInfo.getByteString());
        } else if (webSocketInfo.isReconnect()) {
            onReconnect();
        }
    }

    @CallSuper
    protected void onOpen(@NonNull WebSocket webSocket) {
        Logger.d("onOpen");
    }

    @CallSuper
    protected void onMessage(@NonNull String text) {
        Logger.d("onMessage : " + text);
    }

    @CallSuper
    protected void onMessage(@NonNull ByteString byteString) {
        Logger.d("onMessage : " + byteString.toString());
    }

    @CallSuper
    protected void onReconnect() {
        Logger.d("onReconnect");
    }

    /**
     * In most cases, the server closes the connection.
     */
    @CallSuper
    protected void onClose() {
        Logger.d("onClose");
    }

}
