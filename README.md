# RxWebSocket [ ![Download](https://api.bintray.com/packages/felix0503/maven/rx2-websocket/images/download.svg) ](https://bintray.com/felix0503/maven/rx2-websocket/_latestVersion)

Create WebSocket with RxJava(2) and OKHttp3

## Adding to project
```Groovy
//if you use rxjava
implementation "com.felix:rx-websocket:0.0.1"

//if you use rxjava2
implementation "com.felix:rx2-websocket:0.0.1"

```
Notice: RxWebSocket depend on [okhttp](https://github.com/square/okhttp) and [RxJava](https://github.com/ReactiveX/RxJava)

## Usage
```Java
//init before send message and listen receive message
RxWebSocket.initialize(Constant.WEB_SOCKET_URL,    //websocket url
        new WebSocketConfig.Builder()
                .client(mOkHttpClient)   //okhttp client
                .reconnectInterval(3)    //reconnect interval (s)
                .retryTimes(3)           //retry times
                .build());
               
//send message, if WebSocket not connect, the message will send fail
RxWebSocket.sendMessage(Constant.WEB_SOCKET_URL, message);

//sync send message, if WebSocket not connect, will send message when WebSocket on open
RxWebSocket.syncSendMessage(Constant.WEB_SOCKET_URL, message);

//listen WebSocket message
RxWebSocket.get(Constant.WEB_SOCKET_URL)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new WebSocketSubscriber() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        //WebSocket connect error, will not reconnect
                    }

                    @Override
                    protected void onOpen(@NonNull WebSocket webSocket) {
                        super.onOpen(webSocket);
                        //WebSocket connect success
                    }

                    @Override
                    protected void onMessage(@NonNull String text) {
                        super.onMessage(text);
                        //WebSocket receive message
                    }

                    @Override
                    protected void onMessage(@NonNull ByteString byteString) {
                        super.onMessage(byteString);
                        //WebSocket receive message
                    }

                    @Override
                    protected void onReconnect() {
                        super.onReconnect();
                        //WebSocke reconnect
                    }

                    @Override
                    protected void onClose() {
                        super.onClose();
                        //WebSocket close by server
                    }
                });

```

Notice: You must unsubscribe when destory
