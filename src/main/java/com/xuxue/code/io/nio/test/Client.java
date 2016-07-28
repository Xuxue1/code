package com.xuxue.code.io.nio.test;

import com.xuxue.code.http.ConfiguredHttpClient;
import com.xuxue.code.io.StreamRead;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by HanHan on 2016/7/21.
 */
public class Client {


    public static void main(String[] args)throws Exception{

        Socket socket=new Socket("192.168.24.85",8888);
        InputStream in=socket.getInputStream();
        OutputStream out=socket.getOutputStream();
        ConfiguredHttpClient client=new ConfiguredHttpClient();
        HttpGet get=new HttpGet("http://www.baidu.com");
        CloseableHttpResponse res=client.execute(get);
        byte[] data3= StreamRead.toByte(res.getEntity().getContent());

        for(int ii=0;ii<100;ii++){
            byte[] datas="Hello".getBytes();
            ByteArrayOutputStream o=new ByteArrayOutputStream();
            ByteBuffer ss=ByteBuffer.allocate(4);
            ss.putInt(datas.length);
            ss.flip();

            o.write(ss.array(),0,4);
            o.write(datas,0,datas.length);
            out.write(o.toByteArray(),0,o.toByteArray().length);

            System.out.println("writed");
            byte[] size=new byte[4];
            ByteBuffer b=ByteBuffer.wrap(size);
            int i=in.read(size,0,4);
            System.out.println("iii"+i);
            System.out.println(b.position());


            int sized=b.getInt();
            System.out.println(sized);

            byte[] data=new byte[sized];
            in.read(data,0,data.length);
            System.out.println(new String(data));
        }
       // in.close();
       // out.close();
      //  socket.close();
    }

}
