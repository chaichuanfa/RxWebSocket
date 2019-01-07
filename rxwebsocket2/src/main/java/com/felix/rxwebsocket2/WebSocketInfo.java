package com.felix.rxwebsocket2;

import okhttp3.WebSocket;
import okio.ByteString;

class WebSocketInfo {

    private WebSocket mWebSocket;

    private boolean mIsOpen;

    private String mMessage;

    private ByteString mByteString;

    private boolean mIsReconnect;

    WebSocketInfo(WebSocket webSocket, String message) {
        mWebSocket = webSocket;
        this.mMessage = message;
    }

    WebSocketInfo(WebSocket webSocket, ByteString byteString) {
        mWebSocket = webSocket;
        mByteString = byteString;
    }

    WebSocketInfo(WebSocket webSocket, boolean isOpen) {
        mWebSocket = webSocket;
        mIsOpen = isOpen;
    }

    WebSocketInfo(boolean isReconnect) {
        mIsReconnect = isReconnect;
    }

    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public String getMessage() {
        return mMessage;
    }

    public ByteString getByteString() {
        return mByteString;
    }

    public boolean isReconnect() {
        return mIsReconnect;
    }
}
