package com.mmd.mvpexample;

public interface IUser {

    String getUserName();

    String getUserPassword();

    int checkValidity(String userName, String password);
}
