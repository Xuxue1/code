package com.xuxue.code.tools;

import java.io.*;

/**
 * Created by HanHan on 2016/7/25.
 */
public class SerializableTools {

    public static byte[] serializableToBytes(Serializable s)throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        ObjectOutputStream oout=null;
        byte[] data=null;
        try{
            oout=new ObjectOutputStream(out);
            oout.writeObject(s);
            return out.toByteArray();
        }finally{
            oout.close();
        }
    }
    

    public static Serializable bytesToSerializable(byte[] data)throws IOException,ClassNotFoundException{
        ByteArrayInputStream in=new ByteArrayInputStream(data);
        ObjectInputStream oin=new ObjectInputStream(in);
        try{
            return (Serializable) oin.readObject();
        }finally {
            oin.close();
        }
    }


}
