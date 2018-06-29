package com.p2payment.app.p2payment.models;

/**
 * Created by Lekan Adigun on 6/25/2018.
 */

public class BaseModel<T> {

    public boolean status = false;
    public String message = "";
    public T data;

}
