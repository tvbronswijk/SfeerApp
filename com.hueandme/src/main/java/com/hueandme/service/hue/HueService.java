package com.hueandme.service.hue;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hueandme.R;
import com.hueandme.position.Position;
import com.hueandme.position.Room;
import com.hueandme.service.beacon.BeaconService;
import com.hueandme.service.beacon.OnRoomChangedListener;
import com.hueandme.sfeer.HueMixerController;
import com.hueandme.sfeer.SfeerConfiguration;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;

public class HueService extends Service implements OnRoomChangedListener {

    public static final String TAG = "HueService";
    private static final int ONGOING_NOTIFICATION_ID = 2;

    private final IBinder mBinder = new HueBinder();

    private BeaconService mBeaconService;
    private HueMixerController mMixerController;

    private PHHueSDK mHueSDK;
    private PHBridge mSelectedBridge;

    private Room mRoom = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Starting Hue service");

        startForeground(ONGOING_NOTIFICATION_ID, getNotification());

        mHueSDK = PHHueSDK.create();
        mSelectedBridge = mHueSDK.getSelectedBridge();

        mHueSDK.getNotificationManager().registerSDKListener(mPHSDKListener);

        Intent beaconIntent = new Intent(this, BeaconService.class);
        startService(beaconIntent);
        bindService(beaconIntent, mBeaconServiceConnection, 0);

        List<SfeerConfiguration.Setting> settings = new ArrayList<>();
        settings.add(SfeerConfiguration.Setting.Emotion);
        settings.add(SfeerConfiguration.Setting.Time);
        settings.add(SfeerConfiguration.Setting.Weather);

        SfeerConfiguration sfeerConfiguration = new SfeerConfiguration();
        sfeerConfiguration.setSettings(settings);
        mMixerController = new HueMixerController(this, sfeerConfiguration);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Stopping Hue service");

        if (mBeaconService != null) {
            mBeaconService.removeRoomChangedListener(this);
            try {
                unbindService(mBeaconServiceConnection);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Could not unbind service. " + e.getMessage());
            }
        }

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(ONGOING_NOTIFICATION_ID);
    }

    private Notification getNotification() {
        return new Notification.Builder(this)
                .setContentTitle(getString(R.string.hue_service))
                .setContentText(getString(R.string.hue_service_active))
                .setSmallIcon(R.drawable.ic_white_and_color_e27_b22)
                .setPriority(Notification.PRIORITY_MIN)
                .build();
    }

    @Override
    public void onRoomChanged(@Nullable Room room, @Nullable Position position) {
        if (mRoom != room) {
            mRoom = room;

            String hueGroupId = "";
            if (mRoom != null) {
                hueGroupId = mRoom.getHueGroupId();
            }

            updateLights(hueGroupId);
        }
    }

    private void updateLights(String hueGroupId) {
        if (mSelectedBridge != null) {
            float[] xyValues = mMixerController.getSfeer();

            PHLightState lightStateOn = new PHLightState();
            lightStateOn.setOn(true);
            lightStateOn.setX(xyValues[0]);
            lightStateOn.setY(xyValues[1]);

            PHLightState lightStateOff = new PHLightState();
            lightStateOff.setOn(false);

            for (PHGroup group : mSelectedBridge.getResourceCache().getAllGroups()) {
                if (group.getIdentifier().equals(hueGroupId)) {
                    mSelectedBridge.setLightStateForGroup(hueGroupId, lightStateOn);
                } else {
                    mSelectedBridge.setLightStateForGroup(group.getIdentifier(), lightStateOff);
                }
            }
        }
    }

    private ServiceConnection mBeaconServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBeaconService = ((BeaconService.BeaconBinder) service).getService();
            mBeaconService.addRoomChangedListener(HueService.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBeaconService = null;
        }
    };

    private PHSDKListener mPHSDKListener = new PHSDKListener() {
        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
        }

        @Override
        public void onBridgeConnected(PHBridge phBridge, String s) {
            mSelectedBridge = phBridge;
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {
        }

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> list) {
        }

        @Override
        public void onError(int i, String s) {
        }

        @Override
        public void onConnectionResumed(PHBridge phBridge) {
            mSelectedBridge = phBridge;
        }

        @Override
        public void onConnectionLost(PHAccessPoint phAccessPoint) {
            mSelectedBridge = null;
        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> list) {
        }
    };

    public class HueBinder extends Binder {
        public HueService getService() {
            return HueService.this;
        }
    }
}
