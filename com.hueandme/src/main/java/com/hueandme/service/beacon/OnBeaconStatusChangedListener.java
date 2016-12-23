package com.hueandme.service.beacon;

import org.altbeacon.beacon.Beacon;

public interface OnBeaconStatusChangedListener {

    void onBeaconFound(Beacon beacon);

    void onBeaconLost(Beacon beacon);

    void onRangeChanged(Beacon beacon);
}
