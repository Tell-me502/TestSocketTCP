package com.example.liyun.testsocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private EditText msg;
    private Button send_msg;
    private TextView display_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msg=findViewById(R.id.msg);
        send_msg=findViewById(R.id.send);
        display_msg=findViewById(R.id.display_msg);

        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMassager();
            }
        });
    }

    private void sendMassager() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Socket socket=null;
                OutputStream os=null;
                InputStream is=null;
                InputStreamReader isReader = null;
                BufferedReader bReader=null;
                try {
                    //1.创建监听指定服务器地址以及指定服务器监听的端口号
                    socket=new Socket("192.168.31.65",9899);
                    //2.拿到客户端的socket对象的输出流发送给服务器数据
                    os=socket.getOutputStream();

                    //写入要发送给服务器端的数据
                    os.write(msg.getText().toString().getBytes());
                    //发送数据到服务端
                    /*PrintWriter printWriter=new PrintWriter(os);
                    printWriter.println(msg.getText().toString());*/
                    os.flush();
                    //关闭socket输出流
                    socket.shutdownOutput();
                    //拿到socket的输入流，这里存储的是服务器返回的数据
                    is=socket.getInputStream();
                    //解析数据
                    isReader=new InputStreamReader(is,"UTF-8");
                    bReader=new BufferedReader(isReader);
                    String s=null;
                    final StringBuffer sb=new StringBuffer();
                    while ((s=bReader.readLine())!=null){
                        sb.append(s);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            display_msg.setText(sb.toString());
                        }
                    });
                    //关闭IO流
                    //socket.shutdownInput();
                    isReader.close();
                    bReader.close();
                    is.close();
                    os.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
