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
import com.hueandme.position.BeaconPosition;
import com.hueandme.position.Position;
import com.hueandme.position.Room;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    private final List<Room> mRooms = new ArrayList<>();
    private final List<Beacon> mBeaconsFound = new ArrayList<>();
    private final List<BeaconPosition> mBeaconPositions = new ArrayList<>();
    private Position mPosition;
    private Room mActiveRoom = null;

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

        Room roomLivingRoom = new Room(1, "Living Room", 0, 0, 5, 10);
        roomLivingRoom.setHueGroupId("1");
        mRooms.add(roomLivingRoom);

        Room roomKitchen = new Room(2, "Kitchen", 5, 0, 9, 9);
        roomKitchen.setHueGroupId("2");
        mRooms.add(roomKitchen);

        mBeaconPositions.add(new BeaconPosition(5, 1, "e584fbcb-829c-48b2-88cc-f7142b926aea"));
        mBeaconPositions.add(new BeaconPosition(5, 5, "0x00010203040506070809"));
        mBeaconPositions.add(new BeaconPosition(3, 6, "e584fbcb-829c-48b2-88cc-f7142b926aec"));

        //mBeaconsFound.add(new Beacon.Builder().setId1("e584fbcb-829c-48b2-88cc-f7142b926aeb").setTxPower(-66).setRssi(-60).build());
        mBeaconsFound.add(new Beacon.Builder().setId1("e584fbcb-829c-48b2-88cc-f7142b926aec").setTxPower(-66).setRssi(-62).build());

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
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

        String contentText = getResources().getQuantityString(R.plurals.beacons_found, mBeacons.size(), mBeacons.size());
        if (mActiveRoom != null) {
            contentText += "; " + getResources().getString(R.string.current_room, mActiveRoom.getName());
        }
        return new Notification.Builder(this)
                .setContentTitle(getString(R.string.beacon_service))
                .setContentText(contentText)
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

                List<Beacon> toRemove = new ArrayList<>();

                for (final Map.Entry<Beacon, Long> beaconEntry : mBeacons.entrySet()) {
                    if (SystemClock.elapsedRealtime() - beaconEntry.getValue() > BEACON_INTERVAL) {
                        toRemove.add(beaconEntry.getKey());

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

                for (Beacon beacon : toRemove) {
                    mBeacons.remove(beacon);
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

    public boolean addRoomChangedListener(OnRoomChangedListener listener) {
        return mRoomChangedListeners.add(listener);
    }

    public boolean removeRoomChangedListener(OnRoomChangedListener listener) {
        return mRoomChangedListeners.remove(listener);
    }

    @Override
    public void onBeaconFound(Beacon beacon) {
        Log.d(TAG, "Found " + beacon.getId1().toString());
        mBeaconsFound.add(beacon);

        updateNotification();
    }

    @Override
    public void onBeaconLost(Beacon beacon) {
        mBeaconsFound.remove(beacon);

        if (mBeaconsFound.size() < 3) {
            mPosition = null;
            mActiveRoom = null;

            for (final OnRoomChangedListener listener : mRoomChangedListeners) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRoomChanged(mActiveRoom, mPosition);
                    }
                });
            }
        }

        updateNotification();
    }

    @Override
    public void onRangeChanged(Beacon beacon) {
        mBeaconsFound.remove(beacon);
        mBeaconsFound.add(beacon);

        if (mBeaconsFound.size() >= 3) {
            mPosition = Position.getByTrilateration(
                    getPositionForBeacon(mBeaconsFound.get(0)), mBeaconsFound.get(0).getDistance() * 5,
                    getPositionForBeacon(mBeaconsFound.get(1)), mBeaconsFound.get(1).getDistance() * 5,
                    getPositionForBeacon(mBeaconsFound.get(2)), mBeaconsFound.get(2).getDistance() * 5
            );

            mActiveRoom = null;

            for (Room room : mRooms) {
                if (room.contains(mPosition)) {
                    mActiveRoom = room;
                    break;
                }
            }
        } else {
            mPosition = null;
            mActiveRoom = null;
        }

        for (final OnRoomChangedListener listener : mRoomChangedListeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onRoomChanged(mActiveRoom, mPosition);
                }
            });
        }

        updateNotification();
    }

    private Position getPositionForBeacon(Beacon beacon) {
        for (BeaconPosition beaconPosition : mBeaconPositions) {
            if (beacon.getId1().toString().equals(beaconPosition.getIdentifier())) {
                return beaconPosition;
            }
        }

        return null;
    }

    public List<Room> getRooms() {
        return new ArrayList<>(mRooms);
    }

    public List<BeaconPosition> getBeaconPositions() {
        return new ArrayList<>(mBeaconPositions);
    }

    public class BeaconBinder extends Binder {
        public BeaconService getService() {
            return BeaconService.this;
        }
    }
}
