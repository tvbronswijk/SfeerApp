package com.hueandme.position;

import android.graphics.PointF;

public class BeaconPosition extends Position {

    private String identifier;

    public BeaconPosition(PointF point, String identifier) {
        super(point);
        this.identifier = identifier;
    }

    public BeaconPosition(float x, float y, String identifier) {
        super(x, y);
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
