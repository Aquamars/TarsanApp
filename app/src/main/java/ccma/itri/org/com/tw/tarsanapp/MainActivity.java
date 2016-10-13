package ccma.itri.org.com.tw.tarsanapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ccma.itri.org.com.tw.tarsanapp.WifiAdmin.WifiAdmin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lv;
    private WifiManager wifi;
    private String wifis[];
//    private WifiScanReceiver wifiReciever;
    private WifiManager wifiManager;
    private Button open, close, ccma, itri, setting, isEnable;
    private WifiManager.WifiLock wifiLock;
    private WifiAdmin wifiAdmin;
    private TextView txt1, txt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBtn();
        lv=(ListView)findViewById(R.id.listView);

        wifiAdmin = new WifiAdmin(this);
        wifiAdmin.startScan();
        wifiAdmin.closeWifi();
        wifiAdmin.openWifi();
        wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("ITRI_Free_WiFi_CPE_1","ICLITRI2016",3));
//        getList();
        txt1 = (TextView)findViewById(R.id.txt1);
        txt1.setText(wifiAdmin.getSSID());
        txt2 = (TextView)findViewById(R.id.txt2);
        txt2.setText("not yet");

//        Settings.Secure.putString(getContentResolver(),
//                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "ccma.itri.org.com.tw.tarsanapp/ccma.itri.org.com.tw.tarsanapp.AbService.MyService");
//        Settings.Secure.putString(getContentResolver(),
//                Settings.Secure.ACCESSIBILITY_ENABLED, "1");

//        isAccessibilityEnabled();
//        txt1.setText(wifiAdmin.lookUpScan());

        //# enable WIFI
//        wifiManager  =(WifiManager)getSystemService(Context.WIFI_SERVICE);
//        wifiManager.setWifiEnabled(true);
//
//        wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
//        wifiReciever = new WifiScanReceiver();
//        showToast("Wifi Scan");
//        getWifiList();
//        wifi.startScan();
//        connectWiFi("ITRI_Free_WiFi_CPE_1","ICLITRI2016");

    }

    protected void onPause() {
//        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
//        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    private void initBtn(){
        open = (Button)findViewById(R.id.open);
        close = (Button)findViewById(R.id.close);
        ccma = (Button)findViewById(R.id.ccma);
        itri = (Button)findViewById(R.id.itri);
        setting = (Button)findViewById(R.id.setting);
        isEnable = (Button)findViewById(R.id.enable);
        open.setOnClickListener(this);
        close.setOnClickListener(this);
        ccma.setOnClickListener(this);
        itri.setOnClickListener(this);
        setting.setOnClickListener(this);
        isEnable.setOnClickListener(this);
    }

    private void connectWiFi(String ssid, String pwd){
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "\""+ssid+"\"";
        wifiConfiguration.preSharedKey = "\""+pwd+"\"";
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int netID = wifiManager.addNetwork(wifiConfiguration);
        wifiManager.setWifiEnabled(true);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netID, true); // Seconds parm instructs to  disableOtherNetworks
        wifiManager.reconnect(); // Now we will connect to the only available enabled network.
        showToast(ssid+":"+pwd);
    }

    private void getWifiList(){
        List<ScanResult> wifiScanList = wifi.getScanResults();
        wifis = new String[wifiScanList.size()];

        for(int i = 0; i < wifiScanList.size(); i++){
            wifis[i] = ((wifiScanList.get(i)).toString());
//            if(wifis[i].contains("ITRI_Free_WiFi_CPE_1")){
//                showToast(wifis[i]);
//                showToast("Find !");
////                    connectWiFi("CCMA-GUEST","ITRI02750963");
//
//            }
        }
        lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,wifis));
    }
//    private class WifiScanReceiver extends BroadcastReceiver{
//        public void onReceive(Context c, Intent intent) {
//            List<ScanResult> wifiScanList = wifi.getScanResults();
//            wifis = new String[wifiScanList.size()];
//
//            for(int i = 0; i < wifiScanList.size(); i++){
//                wifis[i] = ((wifiScanList.get(i)).toString());
////                if(wifis[i].contains("ITRI_Free_WiFi_CPE_1")){
////                    showToast(wifis[i]);
////                    showToast("Find !");
//////                    connectWiFi("CCMA-GUEST","ITRI02750963");
////                    connectWiFi("ITRI_Free_WiFi_CPE_1","ICLITRI2016");
////                }
//            }
//            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,wifis));
//        }
//    }

    private void getList(){
        List<ScanResult> wifiScanList = wifiAdmin.getWifiList();
        wifis = new String[wifiScanList.size()];
        for(int i = 0; i < wifiScanList.size(); i++){
            wifis[i] = ((wifiScanList.get(i)).toString());
        }
        lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,wifis));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.open:
                wifiAdmin.openWifi();
                showToast("openWifi");
                break;
            case R.id.close:
                wifiAdmin.closeWifi();
                showToast("closeWifi");
                break;
            case R.id.ccma:
                wifiAdmin.openWifi();
                wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("CCMA-GUEST","ITRI02750963",3));
                showToast("connect");
                txt1.setText(wifiAdmin.getSSID());
                break;
            case R.id.itri:
                wifiAdmin.openWifi();
                wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("ITRI_Free_WiFi_CPE_1","ICLITRI2016",3));
                showToast("connect");
                txt1.setText(wifiAdmin.getSSID());
                break;
            case R.id.setting:
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                break;
            case R.id.enable:
                isAccessibilityEnabled();
                break;
        }
    }

    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public boolean isAccessibilityEnabled(){
        int accessibilityEnabled = 0;
        String LOGTAG = "isAbEnabled";
//        final String LIGHTFLOW_ACCESSIBILITY_SERVICE = "com.example.test/com.example.text.ccessibilityService";
        boolean accessibilityFound = false;

        txt2.setText("");
        try {
            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(),android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.d(LOGTAG, "ACCESSIBILITY: " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(LOGTAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled==1){
            Log.d(LOGTAG, "***ACCESSIBILIY IS ENABLED***: ");

            String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            Log.d(LOGTAG, "Setting: " + settingValue);

            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
                    Log.d(LOGTAG, "Setting: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase("ccma.itri.org.com.tw.tarsanapp/ccma.itri.org.com.tw.tarsanapp.AbService.MyService")){
                        Log.d(LOGTAG, "We've found the correct setting - accessibility is switched on!");
                        txt2.setText("ccma.itri.org.com.tw.tarsanapp/ccma.itri.org.com.tw.tarsanapp.AbService.MyService");
                        return true;
                    }
                }
            }

            Log.d(LOGTAG, "***END***");
        }
        else{
            Log.d(LOGTAG, "***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }
}
