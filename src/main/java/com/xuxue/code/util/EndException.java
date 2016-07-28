package com.xuxue.code.util;

/**
 * Created by HanHan on 2016/7/23.
 */
public class EndException extends Throwable{

    public EndException(){
        super();
    }

    public EndException(String message){
        super(message);
    }

    public EndException(Throwable e){
        super(e);
    }
}
