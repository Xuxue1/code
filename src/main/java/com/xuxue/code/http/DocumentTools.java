package com.xuxue.code.http;

import com.xuxue.code.io.StreamRead;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by HanHan on 2016/7/26.
 */
public class DocumentTools {

    public static String getMeta(Document doc,String metaName){
        Elements e=doc.select("meta[name]");
        if(e==null||e.size()==0)
            return null;
        for(Element ee:e){
            if(ee.attr("name").equalsIgnoreCase(metaName)){
                return ee.attr("content");
            }
        }
        return null;
    }

    public static Document toHtml(HttpGet get,ConfiguredHttpClient client)throws IOException{
        CloseableHttpResponse res=null;
        InputStream in=null;
        try{
            res=client.execute(get);
            if(res.getStatusLine().getStatusCode()!=200){
                throw new IOException("response code id"+res.getStatusLine().getStatusCode());
            }
            Header[] h=res.getHeaders("Content-type");
            boolean isHtml=false;
            for(Header hh:h){
                if(hh.getValue().equalsIgnoreCase("html")){
                    isHtml=true;
                    break;
                }
            }

            if(isHtml){
                in=res.getEntity().getContent();
                Document doc=StreamRead.toDocument(in,get.getURI().toString());
                return doc;
            }else{
                throw new IOException("not a html");
            }
        }finally {
            in.close();
            res.close();
        }
    }

    public static void main(String[] args)throws IOException{
        HttpGet get=new HttpGet("http://www.okooo.com/jingcai/");
        ConfiguredHttpClient client=new ConfiguredHttpClient();
        CloseableHttpResponse res=client.execute(get);
        InputStream in=res.getEntity().getContent();
        Document doc=StreamRead.toDocument(in,get.getURI().toString());
        System.out.println(doc.charset().toString());
        System.out.println(getMeta(doc,"Description"));
    }

}
