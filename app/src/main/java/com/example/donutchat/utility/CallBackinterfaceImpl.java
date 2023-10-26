package com.example.donutchat.utility;

public class CallBackinterfaceImpl implements CallBackinterface {

    private boolean[] data = new boolean[2] ;

    public CallBackinterfaceImpl() {

    }

    public boolean[] getData() {
        return data;
    }

    public void setData(boolean[] data) {
        this.data = data;
    }
}
