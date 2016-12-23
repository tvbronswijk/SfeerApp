package com.hueandme.service.beacon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hueandme.BeaconActivity;
import com.hueandme.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeaconService extends Service implements BeaconConsumer, OnBeaconStatusChangedListener {

    public static final String TAG = "BeaconService";
    private static final int ONGOING_NOTIFICATION_ID = 1;
    public static final int BEACON_INTERVAL = 5000;

    private final IBinder mBinder = new BeaconBinder();

    private final Set<OnBeaconStatusChangedListener> mBeaconStatusChangedListeners = Collections.synchronizedSet(new HashSet<OnBeaconStatusChangedListener>());
    private final Set<OnRoomChangedListener> mRoomChangedListeners = Collections.synchronizedSet(new HashSet<OnRoomChangedListener>());

    private BeaconManager mBeaconManager;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Map<Beacon, Long> mBeacons = new HashMap<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Starting Beacon service");

        startForeground(ONGOING_NOTIFICATION_ID, getNotification());

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.bind(this);

        addBeaconStatusChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Stopping Beacon service");

        removeBeaconStatusChangedListener(this);

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(ONGOING_NOTIFICATION_ID);
    }

    private Notification getNotification() {
        Intent notificationIntent = new Intent(this, BeaconActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        return new Notification.Builder(this)
                .setContentTitle(getString(R.string.beacon_service))
                .setContentText(getResources().getQuantityString(R.plurals.beacons_found, mBeacons.size(), mBeacons.size()))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_bluetooth)
                .setPriority(Notification.PRIORITY_MIN)
                .build();
    }

    private void updateNotification() {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(ONGOING_NOTIFICATION_ID, getNotification());
    }

    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                for (final Beacon beacon : collection) {
                    if (mBeacons.containsKey(beacon)) {
                        for (final OnBeaconStatusChangedListener listener : mBeaconStatusChangedListeners) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onRangeChanged(beacon);
                                }
                            });
                        }
                    } else {
                        for (final OnBeaconStatusChangedListener listener : mBeaconStatusChangedListeners) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onBeaconFound(beacon);
                                }
                            });
                        }
                    }

                    mBeacons.put(beacon, SystemClock.elapsedRealtime());
                }

                for (final Map.Entry<Beacon, Long> beaconEntry : mBeacons.entrySet()) {
                    if (SystemClock.elapsedRealtime() - beaconEntry.getValue() > BEACON_INTERVAL) {
                        mBeacons.remove(beaconEntry.getKey());

                        for (final OnBeaconStatusChangedListener listener : mBeaconStatusChangedListeners) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onBeaconLost(beaconEntry.getKey());
                                }
                            });
                        }
                    }
                }
            }
        });

        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("id", null, null, null));
        } catch (RemoteException e) {
            Log.e(TAG, "Could not monitor beacons", e);
        }
    }

    public boolean addBeaconStatusChangedListener(OnBeaconStatusChangedListener listener) {
        return mBeaconStatusChangedListeners.add(listener);
    }

    public boolean removeBeaconStatusChangedListener(OnBeaconStatusChangedListener listener) {
        return mBeaconStatusChangedListeners.remove(listener);
    }

    @Override
    public void onBeaconFound(Beacon beacon) {
        updateNotification();
    }

    @Override
    public void onBeaconLost(Beacon beacon) {
        updateNotification();
    }

    @Override
    public void onRangeChanged(Beacon beacon) {
        updateNotification();
    }

    public class BeaconBinder extends Binder {
        public BeaconService getService() {
            return BeaconService.this;
        }
    }
}
