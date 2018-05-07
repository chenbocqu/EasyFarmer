package com.cqu.expstuer.listener;


public interface OnRequestListener {

    void onError(Exception e);

    void onResponse(String response);
}
