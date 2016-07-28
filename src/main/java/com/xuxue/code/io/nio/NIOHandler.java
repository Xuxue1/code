package com.xuxue.code.io.nio;

import com.xuxue.code.util.EndException;

import java.io.IOException;

/**
 * Created by HanHan on 2016/7/21.
 */
public interface NIOHandler {

    void processEvent(NioEvent event)throws IOException,EndException;
}
