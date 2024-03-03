package com.qx.mylibrary.util;

import android.util.Log;


import com.qx.mylibrary.OneNetApi;

import okhttp3.logging.HttpLoggingInterceptor;

public class OneNetLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Log.i(OneNetApi.LOG_TAG, message);
    }
}
