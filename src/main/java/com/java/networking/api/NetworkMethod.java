package com.java.networking.api;

import java.io.InputStream;

public interface NetworkMethod {

    public void start(String host, int port, InputStream inputStream);
}
