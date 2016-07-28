package com.xuxue.code.tools;

import com.xuxue.code.http.ConfiguredHttpClient;
import com.xuxue.code.io.StreamRead;
import com.xuxue.code.util.StringTranslateUtil;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Created by HanHan on 2016/7/23.
 */
public class HttpReader {


    public static Document getHtml(String url, ConfiguredHttpClient client)throws IOException {

        HttpGet get=new HttpGet(url);
        RequestConfig config=RequestConfig.custom()
                .setConnectTimeout(1000)
                .setSocketTimeout(3000)
                .setConnectionRequestTimeout(1000).build();
        get.setConfig(config);
        CloseableHttpResponse res=null;
        try{
            res=client.execute(get);
            Header[] h=res.getHeaders("Content-Type");
            String content="";
            for(Header hs:h){
                content+=hs.getName();
            }
            if(!content.contains("html"))
                return null;
            return StreamRead.toDocument(res.getEntity().getContent(),url);
        }finally {
            if(res!=null)
              res.close();
            if(get!=null)
              get.abort();
        }
    }


    public static String encodeFilePath(String path){
        String[] pathData=path.split("/");
        StringBuilder b=new StringBuilder();
        for(int i=0;i<pathData.length;i++){
            b.append("/");
            if(i==pathData.length-1){
                b.append(pathData[i]);
            }else{
                b.append(StringTranslateUtil.toHex(pathData[i]));
            }
        }
        return b.toString();
    }


    public static boolean readFile(HttpUriRequest request, CloseableHttpResponse res, File baseFile)throws URISyntaxException,IOException{
        URI uri=request.getURI();
        if(uri.getQuery()!=null||uri.getPath()==null){
            return false;
        }
        String filePath=encodeFilePath(uri.getHost()+"/"+uri.getPath());
        File f=new File(baseFile,filePath);
        if(!f.getParentFile().exists()){
            f.getParentFile().mkdirs();
        }
        if(f.exists()){
            return false;
        }else{
            f.createNewFile();
        }
        InputStream in=null;
        try{
            in=res.getEntity().getContent();
            StreamRead.writeFile(in,f);
        }finally {
            if(in!=null)
               in.close();
            if(res!=null)
               res.close();
        }
        return true;
    }

    public static void main(String[] args)throws Exception{
        InetAddress a=InetAddress.getByName("192.168.0.81");
        a.isReachable(200);
    }

}
