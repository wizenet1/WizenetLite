package com.Classes;

import java.io.IOException;

public class HTTPException extends IOException {
    private int responseCode;

    public HTTPException( final int responseCode ) {
        super();
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return this.responseCode;
    }
}
