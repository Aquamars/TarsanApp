package ccma.itri.org.com.tw.tarsanapp.WifiAdmin;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ccma.itri.org.com.tw.tarsanapp.CheckService;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by A40503 on 2016/10/12.
 */

public class WifiAdmin {
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;
    private List<WifiConfiguration> mWifiConfiguration;
    private WifiManager.WifiLock mWifiLock;
    private static WifiAdmin Instance = null;
    private WifiAdmin(Context context){
        Instance = this;
        mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo =mWifiManager.getConnectionInfo();
    }

    public static WifiAdmin getInstance(Context context){
        if(Instance == null){
            Instance = new WifiAdmin(context.getApplicationContext());
        }
        return Instance;
    }

    // 打開WIFI
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    // 關閉WIFI
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    // 檢查當前WIFI狀態
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁WifiLock
    public void releaseWifiLock() {
        // 判斷時候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 創建一個WifiLock
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    // 得到配置好的網络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    // 指定配置好的網络進行連接
    public void connectConfiguration(int index) {
        // 索引大於配置好的網络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 連接配置好的指定ID的網络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }

    public void startScan() {
        mWifiManager.startScan();
        // 得到掃描結果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的網络連接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }

    // 得到網络列表
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    // 查看掃描結果
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 將ScanResult信息轉換成一個字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    // 得到MAC地址
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    // 得到接入點的SSID
    public String getSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
    }

    // 得到接入點的BSSID
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    // 得到IP地址
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    // 得到連接的ID
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 得到WifiInfo的所有信息包
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    // 添加一個網络並連接
    public void addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b =  mWifiManager.enableNetwork(wcgID, true);
        Log.d("addNetwork",wcg.SSID+":"+wcgID);
//        mWifiManager.reconnect();

        //# Update connectionInfo
        mWifiInfo =mWifiManager.getConnectionInfo();

        System.out.println("a--" + wcgID);
        System.out.println("b--" + b);
    }

    //# add Network return boolean
    public boolean activeNetwork(WifiConfiguration wcg){
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b =  mWifiManager.enableNetwork(wcgID, true);
        Log.d("activeNetwork",wcg.SSID+":"+wcgID+":"+b);
        //# Update connectionInfo
        mWifiInfo =mWifiManager.getConnectionInfo();
        Log.d("activeNetwork",mWifiInfo.getSSID());
        return b;
    }

    // 斷開指定ID的網络
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }


    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type){

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if(tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if(Type == 1) //WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(Type == 2) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+Password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(Type == 3) //WIFICIPHER_WPA
        {
            config.preSharedKey = "\""+Password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        mWifiManager.enableNetwork(config.networkId,true);
        Log.d("CreateWifiInfo",config.networkId+":"+SSID );
        return config;
    }

    private WifiConfiguration IsExsits(String SSID){
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs)
        {
            if (existingConfig.SSID.equals("\""+SSID+"\""))
            {
                return existingConfig;
            }
        }
        return null;
    }

    //# Observable wifi Control
    public Observable<String> startScan = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("startScan");
            Instance.startScan();
            subscriber.onCompleted();
        }
    });
    public Observable<String> closeWifi = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("closeWifi");
            Instance.closeWifi();
            subscriber.onCompleted();
        }
    }).delay(1000, TimeUnit.MILLISECONDS);
    public Observable<String> openWifi = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("openWifi");
            Instance.openWifi();
            subscriber.onCompleted();
        }
    }).delay(1000, TimeUnit.MILLISECONDS);
    public Observable<String> addNetwork = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("addNetwork");
//            wifiAdmin.activeNetwork(wifiAdmin.CreateWifiInfo("CCMA-GUEST","ITRI02750963",3));
            Instance.activeNetwork(Instance.CreateWifiInfo("ITRI_Free_WiFi_CPE_1","ICLITRI2016",3));
            subscriber.onCompleted();
        }
    }).delay(1300, TimeUnit.MILLISECONDS);

    public String WifiOpen() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        return "OpenWifi";
    }

    public String WifiClose() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
        return "CloseWifi";
    }

    public String WifiStartScan() {
        mWifiManager.startScan();
        // 得到掃描結果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的網络連接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();

        if(mWifiList==null){
            return WifiStartScan();
        }

        return "StartScan";
    }

    public String WifiActiveNetwork(WifiConfiguration wcg){
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b =  mWifiManager.enableNetwork(wcgID, true);
        Log.d("activeNetwork",wcg.SSID+":"+wcgID+":"+b);
        //# Update connectionInfo
        mWifiInfo =mWifiManager.getConnectionInfo();
        Log.d("activeNetwork",mWifiInfo.getSSID());
        if(b)return "ActiveNetwork";

        return "NotActive";
    }

}
