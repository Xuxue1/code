package com.xuxue.code.io;

import com.xuxue.code.tools.CpdetectorEncoding;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.charset.Charset;


/**
 * Created by HanHan on 2016/7/21.
 */
public class StreamRead {

    public static byte[] toByte(InputStream in)throws IOException{
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        byte[] buffer=new byte[128];
        int read=0;
        while((read=in.read(buffer,0,128))!=-1){
            out.write(buffer,0,read);
        }
        return out.toByteArray();
    }


    public static String toString(InputStream in)throws IOException{
        byte[] data=toByte(in);
        Charset charset=CpdetectorEncoding.getEncoding(data,false);
        return new String(data,charset);
    }

    public static void writeFile(InputStream in,File f)throws IOException{
        FileOutputStream out=new FileOutputStream(f);
        byte[] buffer=new byte[1024];
        int readSize=0;
        try{
            while((readSize=in.read(buffer,0,buffer.length))!=-1){
                out.write(buffer,0,readSize);
            }
        }finally {
            out.close();
        }
    }

    public static Document toDocument(InputStream in, String url)throws IOException{
        return Jsoup.parse(toString(in),url);
    }
}
