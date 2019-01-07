package com.felix.rxwebsocket.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public abstract class BaseRxWebSocketActivity extends AppCompatActivity {

    OkHttpClient mOkHttpClient;

    Button mBtSend;

    EditText mEtContent;

    TextView mMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_web_socket);
        mBtSend = findViewById(R.id.mBtSend);
        mEtContent = findViewById(R.id.mEtContent);
        mMessage = findViewById(R.id.mMessage);
        initOKHttp();

        mBtSend.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(mEtContent.getText())) {
                sendMessage(mEtContent.getText().toString());
                mEtContent.setText("");
            }
        });
    }

    private void initOKHttp() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(0, TimeUnit.SECONDS)
//                .pingInterval(30, TimeUnit.SECONDS)
                .build();
    }

    protected void appendMessage(String message) {
        if (!TextUtils.isEmpty(mMessage.getText())) {
            mMessage.append("\n");
        }
        mMessage.append(Html.fromHtml(message));
    }

    public abstract void sendMessage(String message);

}
