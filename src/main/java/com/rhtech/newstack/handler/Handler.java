package com.rhtech.newstack.handler;

import io.undertow.server.HttpHandler;

public interface Handler extends HttpHandler {

    String getPath();

}
