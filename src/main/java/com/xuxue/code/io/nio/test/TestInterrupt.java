package com.xuxue.code.io.nio.test;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by HanHan on 2016/7/25.
 */
public class TestInterrupt implements Runnable{

    private ArrayBlockingQueue<Integer> list=new ArrayBlockingQueue<Integer>(1000);

    public void add(Integer i)throws InterruptedException{
        list.put(i);
    }

    @Override
    public void run() {
        while(true){
            try{
                System.out.println(list.take());
               while(true){
                    System.out.println(10);
                }
            }catch (InterruptedException e){
                System.out.println("interrpted");
                break;
            }
        }
    }

    public static void main(String[] args)throws InterruptedException{
        TestInterrupt t=new TestInterrupt();
        Thread tt=new Thread(t);
        for(int i=0;i<1000;i++){
            t.add(i);
        }
        tt.start();
        Thread.sleep(50);
        tt.interrupt();
        System.out.println("interrpt call");
    }
}
