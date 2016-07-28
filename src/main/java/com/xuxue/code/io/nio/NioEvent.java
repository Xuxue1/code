package com.xuxue.code.io.nio;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by HanHan on 2016/7/21.
 */
public class NioEvent {

    private final Logger logger=Logger.getLogger(getClass());

    public final SelectionKey key;


    public NioEvent(SelectionKey key)throws IOException{
        this.key=key;
    }

}
