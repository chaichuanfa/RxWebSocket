package com.felix.rxwebsocket2;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.io.EOFException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

final class WebSocketProvider {

    /**
     * default client
     */
    private OkHttpClient mOkHttpClient;

    private Map<String, WebSocket> mWebSocketMap = new ArrayMap<>();

    private Map<String, WebSocketConfig> mWebSocketConfigMap = new ArrayMap<>();

    private Map<String, Observable<WebSocketInfo>> mObservableMap = new ArrayMap<>();

    private WebSocketProvider() {
    }

    private static final class InstanceHolder {

        private static final WebSocketProvider INSTANCE = new WebSocketProvider();
    }

    public static WebSocketProvider getInstance() {
        return InstanceHolder.INSTANCE;
    }

    void setWebSocketConfig(String url, WebSocketConfig config) {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url can not null");
        }
        if (config == null) {
            throw new NullPointerException("web socket config can not null");
        }
        if (mWebSocketConfigMap.get(url) != null) {
            return;
        }
        mWebSocketConfigMap.put(url, config);
    }

    boolean sendMessage(String url, String message) {
        if (mWebSocketMap.get(url) != null) {
            return mWebSocketMap.get(url).send(message);
        }
        return false;
    }

    boolean sendMessage(String url, ByteString byteString) {
        if (mWebSocketMap.get(url) != null) {
            return mWebSocketMap.get(url).send(byteString);
        }
        return false;
    }

    void syncSendMessage(String url, String message) {
        observableWebSocket(url)
                .map(WebSocketInfo::getWebSocket)
                .firstElement()
                .subscribe(new MaybeObserver<WebSocket>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onSuccess(WebSocket webSocket) {
                        webSocket.send(message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.e(throwable, "send message error, url = " + url);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    void syncSendMessage(String url, ByteString byteString) {
        observableWebSocket(url)
                .map(WebSocketInfo::getWebSocket)
                .firstElement()
                .subscribe(new MaybeObserver<WebSocket>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {

                    }

                    @Override
                    public void onSuccess(WebSocket webSocket) {
                        webSocket.send(byteString);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.e(throwable, "send message error, url = " + url);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    Observable<WebSocketInfo> observableWebSocket(final String url) {
        verifyConfig(url);
        Observable<WebSocketInfo> observable = mObservableMap.get(url);
        if (observable == null) {
            WebSocketConfig config = mWebSocketConfigMap.get(url);
            observable = Observable
                    .create(new WebSocketInfoActionEmitter(url))
                    .retry((integer, throwable) -> {
                        if (config.mRetryTimes >= integer) {
                            SystemClock.sleep(config.mReconnectInterval * 1000);
                            Logger.i("retry connect url : " + url);
                            return true;
                        }
                        return false;
                    })
                    .doOnDispose(() -> {
                        Logger.i("Unsubscribe, remove resource");
                        mObservableMap.remove(url);
                        mWebSocketMap.remove(url);
                        mWebSocketConfigMap.remove(url);
                    })
                    .doOnNext(webSocketInfo -> {
                        if (webSocketInfo.isOpen()) {
                            mWebSocketMap.put(url, webSocketInfo.getWebSocket());
                        }
                    })
                    .share()
                    .subscribeOn(config.mWebSocketScheduler == null ? Schedulers.io()
                            : config.mWebSocketScheduler);
            mObservableMap.put(url, observable);
        } else {
            WebSocket webSocket = mWebSocketMap.get(url);
            if (webSocket != null) {
                observable = observable.startWith(new WebSocketInfo(webSocket, true));
            }
        }
        return observable;
    }

    private void verifyConfig(String url) {
        if (mWebSocketConfigMap.get(url) == null) {
            throw new NullPointerException("not initialize");
        }
    }

    private final class WebSocketInfoActionEmitter implements ObservableOnSubscribe<WebSocketInfo> {

        private WebSocket mWebSocket;

        private String mUrl;

        /**
         * @param url websocket
         */
        WebSocketInfoActionEmitter(@NonNull String url) {
            mUrl = url;
        }

        @Override
        public void subscribe(ObservableEmitter<WebSocketInfo> emitter) {
            if (mWebSocket != null) {
                emitter.onNext(new WebSocketInfo(true));
            }
            initWebSocket(emitter);
        }

        private void initWebSocket(final ObservableEmitter<WebSocketInfo> emitter) {
            OkHttpClient client;
            if (mWebSocketConfigMap.get(mUrl).mOkHttpClient == null) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .writeTimeout(0, TimeUnit.SECONDS)
                            .readTimeout(0, TimeUnit.SECONDS)
                            .pingInterval(30, TimeUnit.SECONDS)
                            .build();
                }
                client = mOkHttpClient;
            } else {
                client = mWebSocketConfigMap.get(mUrl).mOkHttpClient;
            }
            mWebSocket = client.newWebSocket(new Request.Builder()
                    .url(mUrl)
                    .build(), new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    super.onOpen(webSocket, response);
                    mWebSocketMap.put(mUrl, webSocket);
                    emitter.onNext(new WebSocketInfo(webSocket, true));
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    super.onMessage(webSocket, text);
                    emitter.onNext(new WebSocketInfo(webSocket, text));
                }

                @Override
                public void onMessage(WebSocket webSocket, ByteString bytes) {
                    super.onMessage(webSocket, bytes);
                    emitter.onNext(new WebSocketInfo(webSocket, bytes));
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    super.onClosing(webSocket, code, reason);
                    Logger.d("onClosing, url = " + mUrl);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    super.onClosed(webSocket, code, reason);
                    Logger.d("onClosed, url = " + mUrl + ", code = " + code + ", reason = "
                            + reason);
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t,
                        @Nullable Response response) {
                    super.onFailure(webSocket, t, response);
                    if (t instanceof EOFException) {
                        emitter.onComplete();
                    } else {
                        emitter.onError(t);
                    }
                }
            });
            emitter.setCancellable(() -> mWebSocket.close(1000, "Close WebSocket"));
        }

    }

}
