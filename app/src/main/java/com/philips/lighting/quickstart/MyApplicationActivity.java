package com.philips.lighting.quickstart;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import hueandme.sfeerapp.R;
import hueandme.sfeerapp.databinding.ActivityMainBinding;

/**
 * MyApplicationActivity - The starting point for creating your own Hue App.
 * Currently contains a simple view with a button to change your lights to random colours.  Remove this and add your own app implementation here! Have fun!
 *
 * @author SteveyO
 */
public class MyApplicationActivity extends Activity implements BeaconConsumer {
    private PHHueSDK phHueSDK;
    private static final int MAX_HUE = 65535;
    public static final String TAG = "QuickStart";

    private ActivityMainBinding mBinding;
    private BeaconManager mBeaconManager;

    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        phHueSDK = PHHueSDK.create();


        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mBinding.lstBeacons.setAdapter(mAdapter);

        mBeaconManager = BeaconManager.getInstanceForApplication(this);

        mBeaconManager.bind(this);
    }

    public void randomLights() {
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        Random rand = new Random();

        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(rand.nextInt(MAX_HUE));
            // To validate your lightstate is valid (before sending to the bridge) you can use:  
            // String validState = lightState.validateState();
            bridge.updateLightState(light, lightState, listener);
            //  bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form.
        }
    }

    public void updateLight(double distance) {
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(MAX_HUE);
            lightState.setOn(true);

            double distanceClipped = Math.min(distance, 1);
            int brightness = (int) Math.round((1 - distanceClipped) * 254);

            lightState.setBrightness(brightness);
            bridge.updateLightState(light, lightState, listener);
        }
    }

    public void disableLight() {
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setOn(false);

            bridge.updateLightState(light, lightState, listener);
        }
    }

    // If you want to handle the response from the bridge, create a PHLightListener object.
    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {
        }

        @Override
        public void onReceivingLightDetails(PHLight arg0) {
        }

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {
        }

        @Override
        public void onSearchComplete() {
        }
    };

    @Override
    protected void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {

            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }

            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }

        if (mBeaconManager != null) {
            mBeaconManager.unbind(this);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> collection, Region region) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        for (Beacon beacon : collection) {
                            mAdapter.add(beacon.getId1() + " @ " + beacon.getDistance() + "m");

                            // Turn on
                            if (beacon.getDistance() < 1) {
                                updateLight(beacon.getDistance());
                            } else {
                                disableLight();
                            }
                        }

                        if (collection.isEmpty()) {
                            // Turn off
                            disableLight();
                        }
                    }
                });
            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("id", null, null, null));
        } catch (RemoteException e) {
            Log.e(TAG, "Could not monitor beacons", e);
        }
    }
}
