package com.example.demo2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //获取到蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    //用来保存搜索到的设备信息
    private List<String> bluetoothDevice = new ArrayList<>();

    //ListView组件
    private ListView lv_Device;

    //ListView的字符数组转换适配器
    private ArrayAdapter<String> arrayAdapter;

    //UUID,蓝牙建立连接需要的
    private final UUID MY_UUID = UUID.
            fromString("00001101-0000-1000-8000-00805F9B34FB");

    //为其链接创建一个名称
    private final String NAME = "Bluetooth_Socket";

    //选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
    private BluetoothDevice selectDevice;

    //获取选中设备的客户端串口，全局变量，否则连接在方法执行完就结束了
    private BluetoothSocket clientSocket;

    //获取到想设备写的输出流，全局变量，否则连接在方法结束完就结束了
    private OutputStream os;

    //服务端利用线程不断接收客户端信息
    private AcceptThread thread;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv_bleConnect = (TextView) findViewById(R.id.tv_bleConnect);



        //获取到蓝牙默认的适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //获取到ListView组件

    }

    @Override
    public void onClick(View v) {

    }
    private class AcceptThread extends Thread{
        private BluetoothServerSocket serverSocket;//服务端接口
        private BluetoothSocket socket;//获取到客户端接口
        private InputStream is;//获取输入流
        private OutputStream os;//获取输出流

        @SuppressLint("MissingPermission")
        public AcceptThread(){
            try {
                //通过UUID监听请求，然后获取到对应的服务端接口
                serverSocket = mBluetoothAdapter
                        .listenUsingRfcommWithServiceRecord(NAME,MY_UUID);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public void run(){
            try{
                //接收其客户端的接口
                socket = serverSocket.accept();
                //获取输入流
                is = socket.getInputStream();
                //获取到输出流
                os = socket.getOutputStream();

                //无限循环来接收数据
                while(true){
                    //创建一个128字节的缓冲
                    byte[] buffer = new byte[128];
                    //每次读取128字节，并保存其读取的角标
                    int count = is.read(buffer);
                    //创建Message类，想handler 发送数据
                    Message msg = new Message();

                    //发送一个String的数据，让他向上转型为obj类型

                    msg.obj = new String(buffer,0,count,'utf-8');

                    //发送数据
                    handler.sendMessage(msg);
                }
            }
        }
    }
}