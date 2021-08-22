package com.mtn.myapplication;

public interface ConnectionResponce {

    String getUrl();
    void onSuccess(String response,Boolean isRefresh);
    void onFail(String message);
}
