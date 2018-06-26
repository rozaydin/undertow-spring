package com.rhtech.newstack.controller;

import io.undertow.server.HttpHandler;

public interface Handler extends HttpHandler {

    String getPath();

}
