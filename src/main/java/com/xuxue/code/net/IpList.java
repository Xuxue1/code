package com.xuxue.code.net;

import com.xuxue.code.scala.spider.IPList;
import com.xuxue.code.util.EndException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 一个有状态的IpList
 *
 * @time 2016-7-23
 * @author xuxue
 */
public class IpList implements Serializable{

    private long min;

    private long max;


    public IpList(String minList,String maxList){
        min= IPList.ipStringToLong(minList);
        max=IPList.ipStringToLong(maxList);
    }

    public IpList(File f)throws IOException,ClassNotFoundException{
        ObjectInputStream in=new ObjectInputStream(new FileInputStream(f));
        IpList list=(IpList) in.readObject();
        this.min=list.min;
        this.max=list.max;
    }

    public synchronized List<byte[]> get(int size)throws EndException{
        List<byte[]> lists=new ArrayList<byte[]>();

        //如果请求0个  立即返回
        if(size<=0)
            return lists;
        for(int i=0;i<size;i++){
            if(min<=max) {
                lists.add(IPList.longToBytes(min));
                min += 1;
            }
        }
        if(lists.size()==0)
            throw  new EndException("ip 已经扫描完毕了");
        return lists;
    }



    public void close(File f)throws IOException{
        ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(f));
        out.writeObject(this);
        out.close();
    }

    public synchronized int size(){
        return (int)(max-min);
    }

    public static byte[] longToBytes(long ip){
        return IPList.longToBytes(ip);
    }

    public static String longToString(long ip){
        return IPList.longToIpString(ip);
    }

    public static String byteToString(byte[] data){
        return IPList.bytesToipString(data);
    }

    public static long stringTOLong(String ip){
        return IPList.ipStringToLong(ip);
    }


}
