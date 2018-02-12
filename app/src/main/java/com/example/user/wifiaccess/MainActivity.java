package com.example.user.wifiaccess;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    WifiManager wifiManager;
    private Button buttonScan;
    private TextView textViewName, textViewAddress;
    private IntentIntegrator qrScan;
    String networkSSID,networkPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.wifi);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    wifiaccess();

            }
        });
        buttonScan = (Button) findViewById(R.id.buttonScan);

        qrScan = new IntentIntegrator(this);
        //attaching onclick listener
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews

                    networkSSID=obj.getString("name");
                    networkPass=obj.getString("address");
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    public void wifiaccess(){



//        String networkSSID = "ITC-AP-WEST";
//        String networkPass = "itccanada";

        WifiConfiguration conf = new WifiConfiguration();

        conf.SSID = "\"" + networkSSID + "\"";

        conf.preSharedKey = "\""+ networkPass +"\"";

        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiManager.addNetwork(conf);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        try {
            for( WifiConfiguration i : list ) {
                if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {

                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    break;
                }
            }
        }catch (Exception e){
            wifiManager.setWifiEnabled(true);
        }









//        WifiConfiguration conf  = new WifiConfiguration();
//        conf.SSID = "\"" + networkSSID + "\"";
//        conf.wepKeys[0] = "\"" + networkPass + "\"";
//        conf.wepTxKeyIndex = 0;
//        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//        conf.preSharedKey = "\""+ networkPass +"\"";
//        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//
//        wifiManager.addNetwork(conf);
//        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
//        for( WifiConfiguration i : list ) {
//            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
//                wifiManager.disconnect();
//                wifiManager.enableNetwork(i.networkId, true);
//                wifiManager.reconnect();
//
//                break;
//            }
//        }

    }
}


