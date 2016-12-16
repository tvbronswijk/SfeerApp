package com.hueandme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hueandme.databinding.ActivityConnectBinding;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;

import java.util.List;

public class ConnectActivity extends AppCompatActivity {

    private ActivityConnectBinding mBinding;

    private static final int MAX_TIME = 30;
    private PHHueSDK mHueSDK;
    private boolean isDialogShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_connect);

        mBinding.pbCountdown.setMax(MAX_TIME);

        isDialogShowing = false;
        mHueSDK = PHHueSDK.getInstance();

        mHueSDK.getNotificationManager().registerSDKListener(mPHSDKListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mHueSDK != null) {
            mHueSDK.getNotificationManager().unregisterSDKListener(mPHSDKListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHueSDK != null) {
            mHueSDK.getNotificationManager().unregisterSDKListener(mPHSDKListener);
        }
    }

    private PHSDKListener mPHSDKListener = new PHSDKListener() {
        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {

        }

        @Override
        public void onBridgeConnected(PHBridge phBridge, String s) {

        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {

        }

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> list) {

        }

        @Override
        public void onError(int code, final String message) {
            if (code == PHMessageType.PUSHLINK_BUTTON_NOT_PRESSED) {
                mBinding.pbCountdown.incrementProgressBy(1);
            } else if (code == PHMessageType.PUSHLINK_AUTHENTICATION_FAILED) {
                mBinding.pbCountdown.incrementProgressBy(1);

                if (!isDialogShowing) {
                    isDialogShowing = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(ConnectActivity.this)
                                    .setMessage(message).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            })
                                    .create()
                                    .show();
                        }
                    });
                }

            }
        }

        @Override
        public void onConnectionResumed(PHBridge phBridge) {

        }

        @Override
        public void onConnectionLost(PHAccessPoint phAccessPoint) {

        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> list) {

        }
    };
}
