package com.Classes;

import java.io.IOException;

/**
 * Created by WIZE02 on 04/02/2018.
 */

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
