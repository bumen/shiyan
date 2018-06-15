package com.bmn.rt.io;

import java.io.*;

/**
 * Created by Administrator on 2017/9/12.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Test test = new Test();
        test.test();
    }

    private void interfaces() {
        Closeable closeable;
        DataInput dataInput;
        DataOutput dataOutput;
        Externalizable externalizable;

        FileFilter fileFilter;
        FilenameFilter filenameFilter;

        Flushable flushable;
        ObjectInput objectInput;

        ObjectInputValidation objectInputValidation;

        ObjectOutput objectOutput;

        ObjectStreamConstants objectStreamConstants;

        Serializable serializable;
    }

    private void classses() {

        InputStream inputStream;
            ByteArrayInputStream byteArrayInputStream;
            ObjectInputStream objectInputStream;
            FileInputStream fileInputStream;
            PipedInputStream pipedInputStream;
                DataInputStream dataInputStream;
                BufferedInputStream bufferedInputStream;
                PushbackInputStream pushbackInputStream;

        OutputStream outputStream;
            ByteArrayOutputStream byteArrayOutputStream;
            ObjectOutputStream objectOutputStream;
            FileOutputStream fileOutputStream;
            PipedOutputStream pipedOutputStream;
                BufferedOutputStream bufferedOutputStream;
                DataOutputStream dataOutputStream;
                PrintStream printStream;


        Reader reader;
            CharArrayReader charArrayReader;
            PipedReader pipedReader;
            BufferedReader bufferedReader;
                LineNumberReader lineNumberReader;
            StringReader stringReader;
            InputStreamReader inputStreamReader;
                 FileReader fileReader;

        Writer writer;
            CharArrayWriter charArrayWriter;
            PipedWriter pipedWriter;
            BufferedWriter bufferedWriter;
            StringWriter stringWriter;
            PrintWriter printWriter;
            OutputStreamWriter outputStreamWriter;
                FileWriter fileWriter;



        Console console;

        File file;
        FileDescriptor fileDescriptor;


        ObjectInputStream.GetField getField;
        ObjectOutputStream.PutField putField;
        ObjectStreamClass objectStreamClass;
        ObjectStreamField objectStreamField;

        StreamTokenizer streamTokenizer;
    }


    public void test() throws IOException {
            String str = "www.baidu.com" ;		// 定义字符串
            PushbackInputStream push = null ;		// 定义回退流对象
            ByteArrayInputStream bai = null ;		// 定义内存输入流
            bai = new ByteArrayInputStream(str.getBytes()) ;	// 实例化内存输入流
            push = new PushbackInputStream(bai) ;	// 从内存中读取数据
            System.out.print("读取之后的数据为：") ;
            int temp = 0 ;
            while((temp=push.read())!=-1){	// 读取内容
                if(temp=='.'){	// 判断是否读取到了“.”
                    push.unread(temp) ;	// 放回到缓冲区之中
                   // temp = push.read() ;	// 再读一遍
                    //System.out.print("（退回"+(char)temp+"）") ;
                }else{
                    System.out.print((char)temp) ;	// 输出内容
                }
            }
    }
}
