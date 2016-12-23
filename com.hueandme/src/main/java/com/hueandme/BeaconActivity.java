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
import com.hueandme.service.beacon.OnBeaconStatusChangedListener;

import org.altbeacon.beacon.Beacon;

public class BeaconActivity extends AppCompatActivity implements OnBeaconStatusChangedListener {

    private ActivityBeaconBinding mBinding;

    private BeaconService mBeaconService;
    private BeaconListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_beacon);

        mAdapter = new BeaconListAdapter(this);
        mBinding.lstBeacons.setAdapter(mAdapter);
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
            mBeaconService.removeBeaconStatusChangedListener(this);
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
            mBeaconService.addBeaconStatusChangedListener(BeaconActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBeaconService = null;
        }
    };

    @Override
    public void onBeaconFound(Beacon beacon) {
        mAdapter.addItem(beacon);
        Log.d("BeaconActivity", "Found beacon");
    }

    @Override
    public void onBeaconLost(Beacon beacon) {
        mAdapter.removeItem(beacon);
        Log.d("BeaconActivity", "Lost beacon");
    }

    @Override
    public void onRangeChanged(Beacon beacon) {
        mAdapter.addItem(beacon);
        Log.d("BeaconActivity", "Changed beacon");
    }
}
