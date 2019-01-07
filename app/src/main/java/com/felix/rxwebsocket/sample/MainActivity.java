package com.felix.rxwebsocket.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openRx2WebSocket(View view) {
        startActivity(new Intent(this, Rx2WebSocketActivity.class));
    }

    public void openRxWebSocket(View view) {
        startActivity(new Intent(this, RxWebSocketActivity.class));
    }
}
