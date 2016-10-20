package ccma.itri.org.com.tw.tarsanapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ccma.itri.org.com.tw.tarsanapp.WifiAdmin.WifiAdmin;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.TimeInterval;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lv;
    private View ll1, ll2;
    private Button open, close, ccma, itri, setting, isEnable, guide;
    private WifiAdmin wifiAdmin;
    private TextView txt1, txt2, txtSsid;
    private WifiManager manager;
    private NetworkInfo wifiCheck;
    private String ssid;
    private String wifis[] = new String[1];
    private int status;
    private boolean enabled = true;
    private boolean watcherEnabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceGuide();


        initComponent();

        lv=(ListView)findViewById(R.id.listView);
        wifiAdmin = WifiAdmin.getInstance(this);
        Log.d("checkState",wifiAdmin.checkState()+"");
        if(wifiAdmin.checkState() == 1){
            wifiAdmin.openWifi();
        }

//        accessFlow(enabled);

        Log.d("Main","new");
    }

    private void accessFlow(boolean enabled){
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("accessFlow", "startScan");
//                    wifiAdmin.startScan();
//                }
//            };
//            h1.postDelayed(runnable, 2000);
        Log.d("accessFlow", String.valueOf(enabled));
        if(enabled){
            Observable.concat(
                    wifiAdmin.startScan.delay(1000, TimeUnit.MILLISECONDS),
                    wifiAdmin.closeWifi.delay(1000, TimeUnit.MILLISECONDS),
                    wifiAdmin.openWifi,
                    wifiAdmin.addNetwork.delay(1300, TimeUnit.MILLISECONDS))
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            Log.d("RxFlow","onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("RxFlow","onError");
                        }

                        @Override
                        public void onNext(String s) {
                            Log.d("RxFlow",s);
                        }
                    });
//            handler.postDelayed(startScan,1000);
//            handler.postDelayed(closeWifi,1500);
//            handler.postDelayed(openWifi,2500);
//            handler.postDelayed(activeNetwork,35000);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("accessFlow","startScan");
//                    wifiAdmin.startScan();
//                }
//            },1000);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("accessFlow","closeWifi");
//                    wifiAdmin.closeWifi();
//                }
//            },1500);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("accessFlow","openWifi");
//                    wifiAdmin.openWifi();
//                }
//            },2500);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("accessFlow","addNetwork");
////                    wifiAdmin.activeNetwork(wifiAdmin.CreateWifiInfo("CCMA-GUEST","ITRI02750963",3));
//                    wifiAdmin.activeNetwork(wifiAdmin.CreateWifiInfo("ITRI_Free_WiFi_CPE_1","ICLITRI2016",3));
//                }
//            },3500);
        }
    }

    private void watchWifi(){
        if(enabled){
            int status = wifiAdmin.checkState();
            manager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            ssid = manager.getConnectionInfo().getSSID();
            txtSsid.setText(ssid);
            Log.d("watchWifi",ssid);
            if(!ssid.equals("\"ITRI_Free_WiFi_CPE_1\""))accessFlow(enabled);

//            lv.destroyDrawingCache();
//            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,wifis));
            switch (status){
                case 1:
                    wifiAdmin.openWifi();
                    accessFlow(enabled);
                    CheckService.getInstance().showToast("wifi disable");
                    break;
                case 2:
                    CheckService.getInstance().showToast("wifi running");
                    break;
                case 3:
                    CheckService.getInstance().showToast(ssid);
                    break;
                case 4:
                    CheckService.getInstance().showToast("wifi status unkown");
            }
        }
    }

    private void updateText(){
        status = wifiAdmin.checkState();
//        if(status == 1){
//            wifiAdmin.openWifi();
////            wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("ITRI_Free_WiFi_CPE_1","ICLITRI2016",3));
//            wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("CCMA-GUEST","ITRI02750963",3));
//        }
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiCheck = connectionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        txt1.setText(wifiAdmin.getSSID());
        if (wifiCheck.isConnected()) {
            txt1.setText("WiFi is Connected");
            txt1.setText(wifiAdmin.getSSID());
        } else {
            txt1.setText("WiFi is not Connected");
        }
        boolean enabled = CheckService.getInstance().checkEnabled();
        if(!enabled){
            txt2.setText("not yet");
        }else {
            txt2.setText("ServiceOn");
        }
    }

    protected void onPause() {
//        unregisterReceiver(wifiReciever);
        Log.d("MainActivity","onPause");
//        updateText();
        serviceGuide();
        super.onPause();
    }

    protected void onResume() {
        Log.d("MainActivity","onResume");
        if(!watcherEnabled){
            manager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            watcherEnabled = true;
            Observable.interval(10000, TimeUnit.MILLISECONDS)
                    .timeInterval()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<TimeInterval<Long>>() {
                        @Override
                        public void onCompleted() {
                            Log.d("RX", "onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("RX", "onError");
                        }

                        @Override
                        public void onNext(TimeInterval<Long> longTimeInterval) {
                            Log.d("RX", String.valueOf(longTimeInterval));
                            if(enabled)watchWifi();
                        }
                    });
        }
//        updateText();
        serviceGuide();
//        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }
    private void initComponent(){
        txt1 = (TextView)findViewById(R.id.txt1);
        txt2 = (TextView)findViewById(R.id.txt2);
        txtSsid = (TextView)findViewById(R.id.txt_ssid);

        open = (Button)findViewById(R.id.open);
        close = (Button)findViewById(R.id.close);
        ccma = (Button)findViewById(R.id.ccma);
        itri = (Button)findViewById(R.id.itri);
        setting = (Button)findViewById(R.id.setting);
        isEnable = (Button)findViewById(R.id.enable);
        guide = (Button)findViewById(R.id.guide);

        ll1 = (View)findViewById(R.id.ll1);
        ll2 = (View)findViewById(R.id.ll2);

        open.setOnClickListener(this);
        close.setOnClickListener(this);
        ccma.setOnClickListener(this);
        itri.setOnClickListener(this);
        setting.setOnClickListener(this);
        isEnable.setOnClickListener(this);
        guide.setOnClickListener(this);

        ll1.setVisibility(View.INVISIBLE);
        ll2.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.open:
                wifiAdmin.openWifi();
                CheckService.getInstance().showToast("openWifi");
                break;
            case R.id.close:
                wifiAdmin.closeWifi();
                CheckService.getInstance().showToast("closeWifi");
                break;
            case R.id.ccma:
                wifiAdmin.openWifi();
                wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("CCMA-GUEST","ITRI02750963",3));
                CheckService.getInstance().showToast("connect");
                txt1.setText(wifiAdmin.getSSID());
                break;
            case R.id.itri:
                wifiAdmin.openWifi();
                wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo("ITRI_Free_WiFi_CPE_1","ICLITRI2016",3));
                CheckService.getInstance().showToast("connect");
                txt1.setText(wifiAdmin.getSSID());
                break;
            case R.id.setting:
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            case R.id.enable:
                updateText();
                break;
            case R.id.guide:
                Intent intent2 = new Intent(this, DefaultGuide.class);
                startActivity(intent2);
                this.finish();
                break;
        }
    }

    private void  serviceGuide(){
//        enabled = CheckService.getInstance().checkEnabled();
//        if(!enabled){
//            Intent intent2 = new Intent(this, DefaultGuide.class);
//            startActivity(intent2);
//            this.finish();
//        }
    }
    //# Observable wifi Control
//    Observable<String> startScan = Observable.create(new Observable.OnSubscribe<String>() {
//        @Override
//        public void call(Subscriber<? super String> subscriber) {
//            subscriber.onNext("startScan");
//            wifiAdmin.startScan();
//            subscriber.onCompleted();
//        }
//    });
//    Observable<String> closeWifi = Observable.create(new Observable.OnSubscribe<String>() {
//        @Override
//        public void call(Subscriber<? super String> subscriber) {
//            subscriber.onNext("closeWifi");
//            wifiAdmin.closeWifi();
//            subscriber.onCompleted();
//        }
//    }).delay(1000, TimeUnit.MILLISECONDS);
//    Observable<String> openWifi = Observable.create(new Observable.OnSubscribe<String>() {
//        @Override
//        public void call(Subscriber<? super String> subscriber) {
//            subscriber.onNext("openWifi");
//            wifiAdmin.openWifi();
//            subscriber.onCompleted();
//        }
//    }).delay(1000, TimeUnit.MILLISECONDS);
//    Observable<String> addNetwork = Observable.create(new Observable.OnSubscribe<String>() {
//        @Override
//        public void call(Subscriber<? super String> subscriber) {
//            subscriber.onNext("addNetwork");
////            wifiAdmin.activeNetwork(wifiAdmin.CreateWifiInfo("CCMA-GUEST","ITRI02750963",3));
//            wifiAdmin.activeNetwork(wifiAdmin.CreateWifiInfo("ITRI_Free_WiFi_CPE_1","ICLITRI2016",3));
//            subscriber.onCompleted();
//        }
//    }).delay(1300, TimeUnit.MILLISECONDS);

}
