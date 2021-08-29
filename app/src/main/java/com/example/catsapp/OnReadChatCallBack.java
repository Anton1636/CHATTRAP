package com.example.catsapp;

import com.example.catsapp.Models.Message;

import java.util.List;

public interface OnReadChatCallBack {
    void onReadSuccess(List<Message> list);
    void onReadFailed();
}
