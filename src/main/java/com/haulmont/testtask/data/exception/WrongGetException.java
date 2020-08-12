package com.haulmont.testtask.data.exception;

public class WrongGetException extends Exception{

    public WrongGetException(String message){
        System.out.println(message);
    }
}
