package com.xuxue.code.io.nio;


import com.xuxue.code.scala.spider.IPList;
import com.xuxue.code.tools.SerializableTools;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by HanHan on 2016/7/22.
 */
public class Connection {

    private Logger logger=Logger.getLogger(getClass());

    private byte[] data;

    private SocketChannel ch;

    private volatile int readedSize;

    private volatile  NIOChannelStatus status=NIOChannelStatus.create;


    private byte[] remoteip;

    private String remoteHost;

    private int remotePort;

    public Connection(SocketChannel channel)throws IOException{
        this.ch=channel;
        InetSocketAddress address=(InetSocketAddress)channel.getRemoteAddress();
        this.remoteHost=address.getHostName();
        this.remotePort=address.getPort();
        this.remoteip=address.getAddress().getAddress();
        logger.info("a client connect ip is"+IPList.bytesToipString(remoteip)
        +" remote host is"+remoteHost+" remote prot is"+remotePort);
    }



    public void clear(){
        status=NIOChannelStatus.create;
        data=null;
        readedSize=0;
    }

    public synchronized boolean read()throws IOException{
        this.readedSize=readSize();
        this.data=readData();
        return true;
    }

    public synchronized boolean write(byte[] data)throws IOException{
        int size=data.length;
        writeSize(size);
        writeData(data);
        System.out.println("ww writed"+data.length);
        return true;
    }

    private int readSize()throws IOException{
        ByteBuffer sizeBuffer=ByteBuffer.allocate(4);
        while(sizeBuffer.hasRemaining()){
            ch.read(sizeBuffer);
        }
        sizeBuffer.flip();
        this.status=NIOChannelStatus.sizeReaded;
        return sizeBuffer.getInt();
    }

    private void writeSize(int size)throws IOException{
        ByteBuffer buffer=ByteBuffer.allocate(4);
        buffer.putInt(size);
        buffer.flip();
        ch.write(buffer);
    }

    private void writeData(byte[] data)throws IOException{
        ByteBuffer buffer=ByteBuffer.wrap(data);
        ch.write(buffer);
    }

    private byte[] readData()throws IOException{
        if(this.status!=NIOChannelStatus.sizeReaded){
            throw new IOException("read error");
        }
        ByteBuffer buffer=ByteBuffer.allocate(readedSize);
        while(buffer.hasRemaining()){
            ch.read(buffer);
        }
        buffer.flip();
        this.status=NIOChannelStatus.dataReaded;
        return buffer.array();
    }

    public String getRemoteIp(){
        return IPList.bytesToipString(remoteip);
    }

    public String getRemoteHost(){
        return remoteHost;
    }

    public int getRemotePort(){
        return remotePort;
    }

    public byte[] getData(){
        return this.data;
    }

    public boolean isClosed(){
        return this.ch.isConnected();
    }

}
