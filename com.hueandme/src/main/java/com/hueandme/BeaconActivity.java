package com.hueandme;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hueandme.databinding.ActivityBeaconBinding;
import com.hueandme.service.beacon.BeaconService;

public class BeaconActivity extends AppCompatActivity {

    private ActivityBeaconBinding mBinding;

    private BeaconService mBeaconService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_beacon);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent beaconIntent = new Intent(this, BeaconService.class);
        startService(beaconIntent);
        bindService(beaconIntent, mBeaconServiceConnection, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mBeaconService != null) {
            mBeaconService.removeBeaconStatusChangedListener(mBinding.map);
            mBeaconService.removeRoomChangedListener(mBinding.map);
            try {
                unbindService(mBeaconServiceConnection);
            } catch (IllegalArgumentException e) {
                Log.e(BeaconService.TAG, "Could not unbind service. " + e.getMessage());
            }
        }
    }

    private ServiceConnection mBeaconServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBeaconService = ((BeaconService.BeaconBinder) service).getService();
            mBeaconService.addBeaconStatusChangedListener(mBinding.map);
            mBeaconService.addRoomChangedListener(mBinding.map);

            mBinding.map.setRooms(mBeaconService.getRooms());
            mBinding.map.setBeaconPositions(mBeaconService.getBeaconPositions());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBeaconService = null;
        }
    };
}
