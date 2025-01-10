package com.flux.flux.v1._shared.websocket.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class HandshakeInterceptorImpl implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        if (request instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
//            HttpServletRequest servletRequest = servletServerRequest.getServletRequest();
//            System.out.println(servletRequest.getCookies());
//            Cookie fingerprint = WebUtils.getCookie(servletRequest, "__fprid");
//            attributes.put("__fprid", fingerprint.getValue());
//        }
        //TODO: ДОДЕЛАТЬ ПОТОМ
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
