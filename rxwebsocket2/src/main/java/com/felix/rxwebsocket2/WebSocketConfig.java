package com.felix.rxwebsocket2;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import io.reactivex.Scheduler;
import okhttp3.OkHttpClient;

public final class WebSocketConfig {

    private WebSocketConfig() {
    }

    OkHttpClient mOkHttpClient;

    long mReconnectInterval;

    int mRetryTimes;

    Scheduler mWebSocketScheduler;

    public static final class Builder {

        private WebSocketConfig config;

        public Builder() {
            config = new WebSocketConfig();
        }

        public Builder client(@Nullable OkHttpClient client) {
            config.mOkHttpClient = client;
            return this;
        }

        /**
         * second
         * @param reconnectInterval
         * @return
         */
        public Builder reconnectInterval(
                @IntRange(from = 1, to = Integer.MAX_VALUE) long reconnectInterval) {
            config.mReconnectInterval = reconnectInterval;
            return this;
        }

        public Builder retryTimes(@IntRange(from = 0, to = Integer.MAX_VALUE) int times) {
            config.mRetryTimes = times;
            return this;
        }

        /**
         * default io thread
         * @param scheduler
         * @return
         */
        public Builder webSocketScheduler(@Nullable Scheduler scheduler) {
            config.mWebSocketScheduler = scheduler;
            return this;
        }

        public WebSocketConfig build() {
            return config;
        }
    }

}
