package com.servicehub.common.security.encryption;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DecryptedRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] decryptedBody;

    public DecryptedRequestWrapper(HttpServletRequest request, String decryptedJson) {
        super(request);
        this.decryptedBody = decryptedJson.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream(){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBody);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read(){
                return byteArrayInputStream.read();
            }
        };

    }



    @Override
    public BufferedReader getReader(){
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }
}
