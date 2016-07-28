package com.xuxue.code.io.nio;

import java.nio.channels.SelectionKey;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by HanHan on 2016/7/22.
 */
public class KeySet {

    Set<SelectionKey> set=new HashSet<SelectionKey>();

    public synchronized boolean add(SelectionKey key){
        return set.add(key);
    }

    public synchronized void remove(SelectionKey key){
        set.remove(key);
    }

    public synchronized boolean contain(SelectionKey key){
        return set.contains(key);
    }
    
}
