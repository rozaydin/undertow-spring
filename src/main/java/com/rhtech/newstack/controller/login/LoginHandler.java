package com.rhtech.newstack.controller.login;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rhtech.newstack.model.LoginRequestData;
import com.rhtech.newstack.model.LoginResponseData;
import com.rhtech.newstack.service.JwtService;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginHandler implements HttpHandler {

    private final Gson gson;
    private final JwtService jwtService;

    public LoginHandler(RoutingHandler routingHandler, Gson gson, JwtService jwtService) {
        this.gson = gson;
        this.jwtService = jwtService;
        // register handlers
        routingHandler.post("/login", this::handleRequest);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        // receive body
        exchange.getRequestReceiver().receiveFullString((_exchange, data) -> {
            // success callback
            log.info("Received login request");
            try {
                LoginRequestData loginRequestData = gson.fromJson(data, LoginRequestData.class);
                log.info("trying to login user: {}", loginRequestData.getUsername());
                boolean loginResult = _exchange.getSecurityContext().login(loginRequestData.getUsername(), loginRequestData.getPassword());
                if (loginResult) {
                    log.info("login for user: {}, is successful, generating login token");
                    // generate jwt token
                    String jwtToken = jwtService.generateJwt(_exchange.getSecurityContext());
                    // return jwt token
                    _exchange.setStatusCode(200);
                    _exchange.getResponseSender().send(gson.toJson(new LoginResponseData(jwtToken)));
                } else {
                    // login failed return 401
                    _exchange.setStatusCode(401);
                    _exchange.getResponseSender().send("username/password is not correct!");
                }
            } catch (JsonSyntaxException jse) {
                _exchange.setStatusCode(500);
                _exchange.getResponseSender().send(gson.toJson(new Error("Failed to parse request body, not a valid login request", jse)));
            } catch (JoseException je) {
                _exchange.setStatusCode(500);
                _exchange.getResponseSender().send(gson.toJson(new Error("Failed to generate jwt for user", je)));
            }
        }, (_exchange, ioExc) -> {
            // error callback
            _exchange.setStatusCode(500);
            _exchange.getResponseSender().send(gson.toJson(new Error("Unable to read HTTPRequest body", ioExc)));
        });
    }
}