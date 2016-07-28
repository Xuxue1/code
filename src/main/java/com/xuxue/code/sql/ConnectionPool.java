package com.xuxue.code.sql;

import java.sql.Connection;
import java.util.ArrayList;



public class ConnectionPool
{


    private ArrayList<MyConnection> connList;

    private int size;

    private String ip;

    private String name;

    private String passwd;

    private String database;

    public ConnectionPool(int size,String ip,String name ,String passwd,String database){
        this.size=size;
        this.ip=ip;
        this.name=name;
        this.passwd=passwd;
        this.database=database;
        init();
    }

    private void init(){
        connList=new ArrayList<MyConnection>(size);
        for(int i=0;i<size;i++)
            connList.add(new MyConnection());
        System.out.println("database init");
    }

    private Connection get(){
        try
        {
           return GetConnection.getMysqlConnection(ip,name,passwd,database);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    public synchronized Connection getConnection(){
        while(true){
            for(MyConnection conn:connList)
                if(conn.getConnection()!=null)
                    return conn.getConnection();
            try
            {
                Thread.sleep(300);
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public void backConnection(Connection conn){
        for(MyConnection mconn:connList)
            if(mconn.getConn()==conn){
                mconn.backConnection(conn);
                return;
            }

        throw new IllegalArgumentException("连接池初五");
    }

    public void close(){
        try
        {
            for(MyConnection conn:connList)
                conn.getConnection().close();
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    class MyConnection
    {
        private Connection conn;

        public boolean busy;

        MyConnection(){
            this.conn=get();
            busy=false;
        }

        public Connection getConnection(){
            if(!busy)
                return conn;
            return null;
        }

        public Connection getConn(){
            return conn;
        }

        public void backConnection(Connection conn){
            this.conn=conn;
            busy=false;
        }
    }
}
