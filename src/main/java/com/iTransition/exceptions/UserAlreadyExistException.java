package com.iTransition.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserAlreadyExistException extends RuntimeException{
    private String message;
    private int errorCode;
}
