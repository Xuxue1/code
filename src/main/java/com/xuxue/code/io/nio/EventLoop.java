package com.xuxue.code.io.nio;

import com.xuxue.code.util.EndException;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @time 2016-7-21
 * @author  xuxue
 */
public class EventLoop {

    private Logger logger=Logger.getLogger(getClass());

    private int loopThreadNum;

    private ExecutorService pool;

    private ArrayBlockingQueue<SelectionKey> event;

    private List<NIOHandler> handlers;

    private List<Thread> threadList;

    private KeySet keySets;

    private boolean isClose=false;


    public EventLoop(int loopThread){
        this.loopThreadNum=loopThread;
        this.threadList=new ArrayList<Thread>();
        this.keySets=new KeySet();
        pool= Executors.newCachedThreadPool();
        event=new ArrayBlockingQueue<SelectionKey>(loopThread*10);
        handlers=new ArrayList<NIOHandler>();
        for(int i=0;i<loopThreadNum;i++){
            Thread t=new Thread(new LoopThread(this));
            pool.execute(t);
            threadList.add(t);
        }
        logger.info("event loop start");
    }


    /**
     *
     * @return EventLoop 里的线程数量
     */
    public synchronized int getSize(){
        return threadList.size();
    }

    /**
     * 增加一个线程
     * @return
     */
    public synchronized boolean addLoopThread(){
        Thread t=new Thread(new LoopThread(this));
        threadList.add(t);
        pool.execute(t);
        return true;
    }

    /**
     * 减去一个线程
     * @return
     */
    public synchronized boolean sunLoopThread(){
        Thread t=threadList.remove(threadList.size()-1);
        t.interrupt();
        return true;
    }

    public void addKey(SelectionKey key)throws InterruptedException{
        if(keySets.add(key)){
            event.put(key);
        }
    }

    public void addHandler(NIOHandler handler){
        this.handlers.add(handler);
        logger.info("hander size is"+handlers.size());
    }

    public void removeHandler(NIOHandler handler){
        this.handlers.remove(handler);
    }

    public synchronized void close()throws IOException{
        for(Thread t:threadList){
            t.interrupt();
        }
        pool.shutdownNow();
        this.isClose=true;
    }

    public boolean isClose(){
        return this.isClose;
    }

    class LoopThread implements Runnable{

        private EventLoop loop;

        public LoopThread(EventLoop loop){
            this.loop=loop;
        }


        @Override
        public void run(){
            SelectionKey key=null;
           while(true){
               try{
                   key=event.take();
                   NioEvent event=new NioEvent(key);

                   logger.info("will process a event");
                   for (NIOHandler h : handlers) {
                           h.processEvent(event);
                   }
                   keySets.remove(key);
               }catch (InterruptedException e){
                   logger.info("interrupted a thread",e);
                   break;
               }catch (IOException e){
                   logger.info("Process a event and IOException",e);
                   key.cancel();
                   try{
                       key.channel().close();
                   }catch (IOException se){
                     logger.info("close a channel",e);
                  }
               }catch (EndException e){
                   try{
                       this.loop.close();
                       key.selector().close();
                       break;
                   }catch (IOException ee){
                       logger.info("close exception");
                   }
               }
           }
        }
    }
}
