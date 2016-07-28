package com.xuxue.code.io.nio.test;

import java.nio.ByteBuffer;

/**
 * Created by HanHan on 2016/7/21.
 */
public class Test {

    public static void main(String[] args){
        ByteBuffer b=ByteBuffer.wrap("Helloqaefubgajgggggggggjgjgjgjgjgjgjgjgjgjgjgjgjgjgjgadsssssssssscasfcjkbca".getBytes());
        ByteBuffer b2=ByteBuffer.allocate(4);
        b2.putInt(100);
        b2.flip();
        System.out.println(b2.position());
    }

}
