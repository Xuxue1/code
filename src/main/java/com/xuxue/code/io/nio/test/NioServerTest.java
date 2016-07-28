package com.xuxue.code.io.nio.test;

import com.xuxue.code.io.nio.*;
import com.xuxue.code.util.EndException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Created by HanHan on 2016/7/21.
 */
public class NioServerTest {

    public static void main(String[] args)throws IOException{
        EventLoop loop=new EventLoop(20);
        loop.addHandler(new MyHandler());
        NIOServer server=new NIOServer(8888 ,loop);

        new Thread(server).start();
        System.out.println("start...");
        Scanner s=new Scanner(System.in);
    }
}


class MyHandler implements NIOHandler{

    private Logger logger=Logger.getLogger(getClass());

    private volatile  int time=0;

    public MyHandler(){
        System.out.println("create a handler");
    }

    public synchronized void addTime(){
        time+=1;
    }

    @Override
    public void processEvent(NioEvent event) throws IOException,EndException{

        SelectionKey key=event.key;
        if(key.isValid()&&key.isAcceptable()){
            accept(event);
        }else if(key.isValid()&&key.isConnectable()){
            connect(event);
        }else if(key.isValid()&&key.isReadable()){
            readed(event);
        }

    }



    public void accept(NioEvent event)throws IOException{
        ServerSocketChannel server=(ServerSocketChannel)event.key.channel();
        SocketChannel channel=server.accept();
        if(channel!=null) {
            System.out.println("accpet a socket");
            channel.configureBlocking(false);
            SelectionKey kk=channel.register(event.key.selector(), SelectionKey.OP_READ);
            kk.attach(new Connection(channel));
        }
    }


    public void connect(NioEvent event){
        System.out.println("connect a socket");
        logger.info("connect a socket");
    }

    public void readed(NioEvent event)throws IOException,EndException{
        Connection conn=(Connection)event.key.attachment();
        addTime();
        if(this.time>20){
            throw new EndException();
        }
        System.out.println(conn==null);
        conn.read();
        byte[] data=conn.getData();
        System.out.println("data length--------------"+data.length);
        conn.write("Hello".getBytes());
        conn.clear();
    }
}


