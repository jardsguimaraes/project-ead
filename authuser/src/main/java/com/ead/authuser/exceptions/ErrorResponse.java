package com.ead.authuser.exceptions;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private int errorCode;
    private String mensagemError;
    private Map<String, String> errorDetails = new HashMap<>();
}
