package com.xuxue.code.util;

/**
 * Created by HanHan on 2016/7/23.
 */
public class StringFormateException extends Throwable{

    public StringFormateException(){
        super();
    }

    public StringFormateException(String massgae){
        super(massgae);
    }

    public StringFormateException(Throwable e){
        super(e);
    }

}
