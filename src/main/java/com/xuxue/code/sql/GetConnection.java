package com.xuxue.code.sql;

import java.sql.*;

/**
 * Created by HanHan on 2016/7/21.
 */
public class GetConnection {

    /**
     * 根据驱动 数据库的url 数据库用户名 数据库密码返回一个数据库连接
     * @param driver 数据库驱动
     * @param url 数据库的url
     * @param name 数据库名
     * @param passwd 数据库密码
     * @return
     * @throws ClassNotFoundException 数据库驱动无法加载
     * @throws SQLException 创建连接发生错误
     */
    public static Connection getConnection(String driver,String url,String name,String passwd)throws ClassNotFoundException,SQLException{
        Class.forName(driver);
        return DriverManager.getConnection(url,name,passwd);
    }



    public static Connection getMysqlConnection(String ip,String name,String passwd,String database)throws ClassNotFoundException,SQLException{
        return getConnection("com.mysql.jdbc.Driver","jdbc:mysql://"+ip+"/"+database,name,passwd);
    }

    public static Connection getMysqlConnection(String ip,String name,String passwd)throws ClassNotFoundException,SQLException{
        return getMysqlConnection(ip,name,passwd,"");
    }

    public static void main(String[] args)throws Exception{
        Connection conn=getMysqlConnection("192.168.24.85","xuxue","qwer");
        Statement sta=conn.createStatement();
        ResultSet set=sta.executeQuery("select database()");

        if(set.next()){
            System.out.println(set.getString(1));
        }
        set.close();
        sta.close();
        conn.close();
        System.out.println("OK");
    }


}
