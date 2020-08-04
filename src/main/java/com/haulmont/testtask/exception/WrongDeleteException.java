package com.haulmont.testtask.exception;

public class WrongDeleteException extends Exception{

    public WrongDeleteException(String message){
        System.out.println(message);
    }
}
