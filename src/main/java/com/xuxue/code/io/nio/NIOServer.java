package com.xuxue.code.io.nio;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 春江潮水连海平，海上明月共潮生。
 滟滟随波千万里，何处春江无月明!
 江流宛转绕芳甸，月照花林皆似霰;
 空里流霜不觉飞，汀上白沙看不见。
 江天一色无纤尘，皎皎空中孤月轮。
 江畔何人初见月？江月何年初照人？
 人生代代无穷已，江月年年望相似。
 不知江月待何人，但见长江送流水。
 白云一片去悠悠，青枫浦上不胜愁。
 谁家今夜扁舟子？何处相思明月楼？
 可怜楼上月徘徊，应照离人妆镜台。
 玉户帘中卷不去，捣衣砧上拂还来。
 此时相望不相闻，愿逐月华流照君。
 鸿雁长飞光不度，鱼龙潜跃水成文。
 昨夜闲潭梦落花，可怜春半不还家。
 江水流春去欲尽，江潭落月复西斜。
 斜月沉沉藏海雾，碣石潇湘无限路。
 不知乘月几人归，落月摇情满江树。
 */

/**
 * Created by HanHan on 2016/7/21.
 */
public class NIOServer implements Runnable{

    private  final Logger logger=Logger.getLogger(getClass());

    private final ServerSocketChannel channel;

    private final Selector selector;

    private boolean run=true;

    private EventLoop loop;

    private final int port;


    public NIOServer(int port,EventLoop loop)throws IOException{
        this.loop=loop;
        this.port=port;
        channel=ServerSocketChannel.open();
        selector=Selector.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(this.port),1024);
        channel.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("server start bind"+port+"  and eventLoop Thrad"+loop);
    }

    public void run(){

        while(run){
            try{
                selector.select(10);
                Set<SelectionKey> selectedKey=selector.selectedKeys();
                Iterator<SelectionKey> it=selectedKey.iterator();
                while(it.hasNext()){
                    SelectionKey  key=it.next();
                    it.remove();
                    if(key.isValid()){
                        loop.addKey(key);
                    }
                }
            }catch (IOException e){
                logger.error("loop selector error",e);
                close();
                break;
            }catch (InterruptedException e){
                logger.info("loop selector interrupted",e);
                close();
                break;
            }catch (ClosedSelectorException e){
                close();
                break;
            }
        }
    }

    public void close(){
        try{
            run=false;
            selector.close();
            channel.close();
        }catch (IOException e){
            logger.info("close server exception",e);
        }
    }
}
