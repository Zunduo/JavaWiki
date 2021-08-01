package com.zunduo.wiki.service;

import com.zunduo.wiki.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {
    @Resource
    public WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message) {
        //推送消息
        webSocketServer.sendInfo(message);
    }
}
