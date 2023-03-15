package com.example.demo2;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Bluetooth Connection Attributes;
    private ListView listView;
    private ArrayAdapter aAdapter;
    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    private HashMap<Integer, BluetoothDevice> deviceHashMaps = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_bleConnect = (Button) findViewById(R.id.btn_bleConnect);
        btn_bleConnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(bAdapter==null){
            Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
                ArrayList list = new ArrayList();

                if (pairedDevices.size() > 0) {
                    int count = 0;
                    for (BluetoothDevice device : pairedDevices) {
                        count++;
                        //将获取到的蓝牙设备添加到hash表中，序号以及设备
                        deviceHashMaps.put(count, device);
                        list.add("id"+count + " )\t" + device.getName() + " [MAC: " + device.getAddress() + "]\n");
                    }
                    listView = (ListView) findViewById(R.id.lv_deviceConnect);
                    aAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(aAdapter);
                }
            } catch (SecurityException se){
                se.printStackTrace();
            }
        }
    }
}