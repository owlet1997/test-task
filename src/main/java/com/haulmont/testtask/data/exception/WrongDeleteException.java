package com.haulmont.testtask.data.exception;

public class WrongDeleteException extends Exception{

    public WrongDeleteException(String message){
        System.out.println(message);
    }
}
