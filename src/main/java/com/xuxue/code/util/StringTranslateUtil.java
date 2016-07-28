package com.xuxue.code.util;


import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created by HanHan on 2016/7/23.
 */
public class StringTranslateUtil {

    public static String toHex(String s){

        byte[] data=s.getBytes();
        StringBuilder b=new StringBuilder();
        for(byte d:data){
            HexData da=new HexData(d);
            b.append(da.getHexString());
        }
        return b.toString();
    }


    public static String toString(String hexString)throws StringFormateException{

        char[] data=hexString.toCharArray();
        ByteBuffer buffer=ByteBuffer.allocate(data.length/2);
        if(data.length%2!=0)
            throw new StringFormateException("");

        for(int i=0;i<data.length;i+=2){
            char[] c=new char[]{data[i],data[i+1]};
            HexData h=new HexData(new String(c));
            buffer.put(h.getData());
            System.out.println("put a byte");
        }
        buffer.flip();

        return new String(buffer.array());
    }

    public static void main(String[] args)throws Throwable{
        System.out.println(toString(toHex("許繼強")));
    }

    /**
     *
     */
   static class HexData{

        byte data;

        int number;

        String hexString;



        public int getNumber(){
            return number;
        }

        public String getHexString(){
            return hexString;
        }

        public byte getData(){
            return data;
        }


        public HexData(byte data){
            this((int)data);
        }

        public HexData(int number){
            int a1=(number&0xf0)>>4;
            int a2=number&0x0f;
            char[] c=new char[2];
            c[0]=intToChar(a1);
            c[1]=intToChar(a2);
            this.hexString=new String(c);
            this.number=number;
            this.data=(byte)number;
        }


        public HexData(String hexString)throws StringFormateException{
            this.hexString=hexString.toLowerCase();
            char[] s=this.hexString.toCharArray();
            if(s.length!=2)
                throw new StringFormateException();
            number=charToInt(s[0])*16+charToInt(s[1]);
            data=(byte)number;
        }


        public int charToInt(char c){
            switch (c){
                case '0': return 0;
                case '1': return 1;
                case '2': return 2;
                case '3': return 3;
                case '4': return 4;
                case '5': return 5;
                case '6': return 6;
                case '7': return 7;
                case '8': return 8;
                case '9': return 9;
                case 'a': return 10;
                case 'b': return 11;
                case 'c': return 12;
                case 'd': return 13;
                case 'e': return 14;
                default: return 15;
            }
        }

        public char intToChar(int data){
            switch (data){
                case 0: return '0';
                case 1: return '1';
                case 2: return '2';
                case 3: return '3';
                case 4: return '4';
                case 5: return '5';
                case 6: return '6';
                case 7: return '7';
                case 8: return '8';
                case 9: return '9';
                case 10: return 'a';
                case 11: return 'b';
                case 12: return 'c';
                case 13: return 'd';
                case 14: return 'e';
                default: return 'f';
            }
        }



    }

}
