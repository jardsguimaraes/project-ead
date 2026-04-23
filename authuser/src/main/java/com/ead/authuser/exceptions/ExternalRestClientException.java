package com.ead.authuser.exceptions;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public class ExternalRestClientException extends RuntimeException {

    private final HttpStatusCode httpStatusCode;
    private final String microservice;

    public ExternalRestClientException(HttpStatusCode httpStatusCode, String message, String microsservice,
            Throwable cause) {
        super(message, cause);

        this.httpStatusCode = httpStatusCode;
        this.microservice = microsservice;
    }
}
