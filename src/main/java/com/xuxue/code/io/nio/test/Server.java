package com.xuxue.code.io.nio.test;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.net.InetSocketAddress;
import java.util.Set;
import java.util.Iterator;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class Server implements Runnable{

    private Selector selector;

    private ServerSocketChannel channel;

    private volatile boolean stop;

    private static DateFormat formate=new SimpleDateFormat("YYYY-mm-dd hh:mm:ss");

    public Server(int port) throws IOException{
        selector=Selector.open();
        channel=ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(port),1024);
        channel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server start.....");
    }

    @Override
    public void run(){
        while(!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectedKey=selector.selectedKeys();
                Iterator<SelectionKey> it=selectedKey.iterator();
                SelectionKey key=null;
                while(it.hasNext()){
                    key=it.next();
                    it.remove();

                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            key.cancel();
                            key.channel().close();
                        } catch (NullPointerException e2) {

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(selector!=null){
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void handleInput(SelectionKey key)throws IOException{
        if(key.isValid()){
            System.out.println("put a key");
            if(key.isAcceptable()){
                //接收一个新的请求
                System.out.println("Accept new ");
                ServerSocketChannel channel=(ServerSocketChannel)key.channel();
                SocketChannel s=channel.accept();
                s.configureBlocking(false);
                s.register(key.selector(),SelectionKey.OP_READ);
            }else if(key.isReadable()){
                SocketChannel s=(SocketChannel)key.channel();
                ByteBuffer buffer=ByteBuffer.allocate(6);
                int readBytes=s.read(buffer);
                System.out.println(readBytes);
                if(readBytes>0){
                    buffer.flip();
                    byte[] bytes=new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String body=new String(bytes,"UTF-8");
                    System.out.println("服务器接收的字符为："+body);
                    doWrite(s,formate.format(new Date()));

                }else if(readBytes<0){
                    key.cancel();
                    s.close();
                }else{
                    System.out.println("no used");
                }
            }
        }
    }

    private void doWrite(SocketChannel channel,String response)throws IOException{
        if(response!=null&&response.trim().length()!=0){
            ByteBuffer data2=ByteBuffer.allocate(4);

            byte[] bytes=response.getBytes();
            ByteBuffer buffer=ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            data2.putInt(bytes.length);
            data2.flip();
            channel.write(data2);
            channel.write(buffer);
            System.out.println("服务器写数据="+response);
        }
    }

    public static void main(String[] args) throws IOException{
        new Thread(new Server(8888)).start();
    }
}
