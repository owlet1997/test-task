package com.haulmont.testtask.exception;

public class WrongGetException extends Exception{

    public WrongGetException(String message){
        System.out.println(message);
    }
}
