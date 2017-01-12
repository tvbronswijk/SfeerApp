package com.hueandme;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.hueandme.databinding.ActivityEntryBinding;
import com.hueandme.hue.AccessPointListAdapter;
import com.hueandme.hue.HueSharedPreferences;
import com.hueandme.hue.WizardAlertDialog;
import com.hueandme.service.hue.HueService;
import com.hueandme.sfeer.EmotionController;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeConfiguration;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;

import java.util.ArrayList;
import java.util.List;

public class EntryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "EntryActivity";

    private ActivityEntryBinding mBinding;

    private PHHueSDK mHueSDK;
    private HueSharedPreferences mHuePrefs;
    private AccessPointListAdapter mAccessPointAdapter;

    private boolean lastSearchWasIPScan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_entry);

        startService(new Intent(this, HueService.class));

        mHueSDK = PHHueSDK.create();

        mHueSDK.setAppName("com.hueandme");
        mHueSDK.setDeviceName(Build.MODEL);

        mHueSDK.getNotificationManager().registerSDKListener(mPHSDKListener);

        mAccessPointAdapter = new AccessPointListAdapter(getApplicationContext(), mHueSDK.getAccessPointsFound());

        mBinding.lstBridges.setOnItemClickListener(this);
        mBinding.lstBridges.setAdapter(mAccessPointAdapter);

        mHuePrefs = HueSharedPreferences.getInstance(getApplicationContext());
        String lastIpAddress = mHuePrefs.getLastConnectedIPAddress();
        String lastUsername = mHuePrefs.getUsername();

        if (!lastIpAddress.equals("")) {
            PHAccessPoint lastAccessPoint = new PHAccessPoint(lastIpAddress, lastUsername, null);

            if (!mHueSDK.isAccessPointConnected(lastAccessPoint)) {
                WizardAlertDialog.showProgressDialog(R.string.connecting, this);
                mHueSDK.connect(lastAccessPoint);
            }
        } else {
            doBridgeSearch();
        }
        EmotionController.getInstance().fireEmotionQuery(this);
    }

    private PHSDKListener mPHSDKListener = new PHSDKListener() {
        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge bridge) {
            Log.d(TAG, "Cache updated");
        }

        @Override
        public void onBridgeConnected(PHBridge bridge, String username) {

            PHBridgeConfiguration bridgeConfiguration = bridge.getResourceCache().getBridgeConfiguration();
            Log.i(TAG, "Bridge connected");
            Log.i(TAG, "IP Address: " + bridgeConfiguration.getIpAddress());

            mHueSDK.setSelectedBridge(bridge);
            mHueSDK.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL);
            mHueSDK.getLastHeartbeat().put(bridgeConfiguration.getIpAddress(), System.currentTimeMillis());
            mHuePrefs.setUsername(username);
            mHuePrefs.setLastConnectedIPAddress(bridgeConfiguration.getIpAddress());

            Log.i(TAG, "Groups:");
            for (PHGroup group : bridge.getResourceCache().getAllGroups()) {
                Log.i(TAG, group.getName() + ": " + group.getIdentifier());
            }

            WizardAlertDialog.closeProgressDialog();
            startMainActivity();
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint accessPoint) {
            Log.i(TAG, "Authentication required");
            mHueSDK.startPushlinkAuthentication(accessPoint);
            startActivity(new Intent(EntryActivity.this, ConnectActivity.class));
        }

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> accessPoints) {
            Log.i(TAG, accessPoints.size() + " Access Points found");

            WizardAlertDialog.closeProgressDialog();
            if (accessPoints.size() > 0) {
                final List<PHAccessPoint> accessPointsFound = mHueSDK.getAccessPointsFound();
                accessPointsFound.clear();
                accessPointsFound.addAll(accessPoints);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAccessPointAdapter.updateData(accessPointsFound);
                    }
                });
            }
        }

        @Override
        public void onError(int code, final String message) {
            Log.e(TAG, code + ": " + message);

            switch (code) {
                case PHHueError.NO_CONNECTION:
                    Log.e(TAG, "No connection");
                    break;
                case PHHueError.AUTHENTICATION_FAILED:
                case PHMessageType.PUSHLINK_AUTHENTICATION_FAILED:
                    WizardAlertDialog.closeProgressDialog();
                    break;
                case PHHueError.BRIDGE_NOT_RESPONDING:
                    Log.e(TAG, "Bridge not responding");
                    WizardAlertDialog.closeProgressDialog();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WizardAlertDialog.showErrorDialog(EntryActivity.this, message, android.R.string.ok);
                        }
                    });
                    break;
                case PHMessageType.BRIDGE_NOT_FOUND:
                    if (!lastSearchWasIPScan) {
                        PHBridgeSearchManager searchManager = (PHBridgeSearchManager) mHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
                        searchManager.search(false, false, true);
                        lastSearchWasIPScan = true;
                    } else {
                        WizardAlertDialog.closeProgressDialog();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WizardAlertDialog.showErrorDialog(EntryActivity.this, message, android.R.string.ok);
                            }
                        });
                    }
                    break;
            }
        }

        @Override
        public void onConnectionResumed(PHBridge bridge) {
            if (!isFinishing()) {
                String ipAddress = bridge.getResourceCache().getBridgeConfiguration().getIpAddress();
                Log.d(TAG, "Connection resumed to " + ipAddress);

                mHueSDK.getLastHeartbeat().put(ipAddress, System.currentTimeMillis());

                List<PHAccessPoint> accessPoints = mHueSDK.getDisconnectedAccessPoint();
                List<PHAccessPoint> toRemove = new ArrayList<>();

                for (PHAccessPoint accessPoint : accessPoints) {
                    if (accessPoint.getIpAddress().equals(ipAddress)) {
                        toRemove.add(accessPoint);
                    }
                }

                for (PHAccessPoint accessPoint : toRemove) {
                    accessPoints.remove(accessPoint);
                }
            }
        }

        @Override
        public void onConnectionLost(PHAccessPoint accessPoint) {
            Log.d(TAG, "Connection lost to " + accessPoint.getIpAddress());
            if (!mHueSDK.getDisconnectedAccessPoint().contains(accessPoint)) {
                mHueSDK.getDisconnectedAccessPoint().add(accessPoint);
            }
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> parsingErrors) {
            for (PHHueParsingError parsingError : parsingErrors) {
                Log.e(TAG, "Parsing error: " + parsingError.getMessage());
            }
        }
    };

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        PHAccessPoint accessPoint = mAccessPointAdapter.getItem(position);

        PHBridge connectedBridge = mHueSDK.getSelectedBridge();

        if (connectedBridge != null) {
            String connectedIp = connectedBridge.getResourceCache().getBridgeConfiguration().getIpAddress();
            if (connectedIp != null) {
                mHueSDK.disableHeartbeat(connectedBridge);
                mHueSDK.disconnect(connectedBridge);
            }
        }

        WizardAlertDialog.showProgressDialog(R.string.connecting, this);
        mHueSDK.connect(accessPoint);
    }

    private void doBridgeSearch() {
        WizardAlertDialog.showProgressDialog(R.string.searching, this);
        PHBridgeSearchManager searchManager = (PHBridgeSearchManager) mHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);

        searchManager.search(true, true);
    }
}
