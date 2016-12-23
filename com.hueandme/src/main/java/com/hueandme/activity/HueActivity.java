package com.hueandme.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;

public abstract class HueActivity extends AppCompatActivity implements PHLightListener {

    private PHHueSDK mHueSDK;
    private PHBridge mSelectedBridge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHueSDK = PHHueSDK.create();
        mSelectedBridge = mHueSDK.getSelectedBridge();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHueSDK != null) {
            if (mSelectedBridge != null) {
                if (mHueSDK.isHeartbeatEnabled(mSelectedBridge)) {
                    mHueSDK.disableHeartbeat(mSelectedBridge);
                }

                mHueSDK.disconnect(mSelectedBridge);
            }
        }
    }

    public PHHueSDK getHueSDK() {
        return mHueSDK;
    }

    public PHBridge getSelectedBridge() {
        return mSelectedBridge;
    }
}
