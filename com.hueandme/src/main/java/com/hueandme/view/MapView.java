package com.hueandme.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hueandme.position.BeaconPosition;
import com.hueandme.position.Position;
import com.hueandme.position.Room;
import com.hueandme.service.beacon.OnBeaconStatusChangedListener;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;

public class MapView extends View implements OnBeaconStatusChangedListener {

    private static final float SCALE = 100;

    private final List<Room> mRooms = new ArrayList<>();
    private final List<Beacon> mBeaconsFound = new ArrayList<>();
    private final List<BeaconPosition> mBeaconPositions = new ArrayList<>();
    private Position mPosition;
    private Room mActiveRoom = null;

    private final Paint mRoomPaint = new Paint();
    private final Paint mActiveRoomPaint = new Paint();
    private final Paint mBeaconPaint = new Paint();
    private final Paint mPositionPaint = new Paint();
    private final Paint mRangesPaint = new Paint();
    private final Paint mTextPaint = new Paint();

    public MapView(Context context) {
        super(context);

        init();
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        mRoomPaint.setColor(Color.BLACK);
        mRoomPaint.setStrokeWidth(2);
        mRoomPaint.setStyle(Paint.Style.STROKE);

        mActiveRoomPaint.setColor(Color.MAGENTA);
        mActiveRoomPaint.setAlpha(64);
        mActiveRoomPaint.setStyle(Paint.Style.FILL);

        mBeaconPaint.setColor(Color.BLUE);

        mPositionPaint.setColor(Color.RED);

        mRangesPaint.setColor(Color.GREEN);
        mRangesPaint.setAlpha(128);

        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(30);

        mRooms.add(new Room(1, 0, 0, 5, 10));
        mRooms.add(new Room(2, 5, 0, 7, 6));

        mBeaconPositions.add(new BeaconPosition(1, 2, "e584fbcb-829c-48b2-88cc-f7142b926aea"));
        mBeaconPositions.add(new BeaconPosition(5, 5, "e584fbcb-829c-48b2-88cc-f7142b926aeb"));
        mBeaconPositions.add(new BeaconPosition(3, 6, "e584fbcb-829c-48b2-88cc-f7142b926aec"));

        mBeaconsFound.add(new Beacon.Builder().setId1("e584fbcb-829c-48b2-88cc-f7142b926aeb").setTxPower(-66).setRssi(-58).build());
        mBeaconsFound.add(new Beacon.Builder().setId1("e584fbcb-829c-48b2-88cc-f7142b926aec").setTxPower(-66).setRssi(-54).build());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Room room : mRooms) {
            canvas.drawRect(room.getTopLeft().x * SCALE, room.getTopLeft().y * SCALE, room.getBottomRight().x * SCALE, room.getBottomRight().y * SCALE, mRoomPaint);

            if (room == mActiveRoom) {
                canvas.drawRect(room.getTopLeft().x * SCALE, room.getTopLeft().y * SCALE, room.getBottomRight().x * SCALE, room.getBottomRight().y * SCALE, mActiveRoomPaint);
            }
        }

        for (BeaconPosition beaconPosition : mBeaconPositions) {
            canvas.drawCircle(beaconPosition.getPoint().x * SCALE, beaconPosition.getPoint().y * SCALE, 15, mBeaconPaint);
        }

        for (Beacon beacon : mBeaconsFound) {
            for (BeaconPosition beaconPosition : mBeaconPositions) {
                if (beacon.getId1().toString().equals(beaconPosition.getIdentifier())) {
                    float cx = beaconPosition.getPoint().x * SCALE;
                    float cy = beaconPosition.getPoint().y * SCALE;
                    canvas.drawCircle(cx, cy, (float) (beacon.getDistance() * 5f * SCALE), mRangesPaint);
                    canvas.drawText(beacon.getDistance() + "m", cx, cy, mTextPaint);
                    break;
                }
            }
        }

        if (mPosition != null) {
            canvas.drawCircle(mPosition.getPoint().x * SCALE, mPosition.getPoint().y * SCALE, 15, mPositionPaint);
            canvas.drawText("@" + mPosition.getPoint(), 100, 100, mTextPaint);
        }
    }

    @Override
    public void onBeaconFound(Beacon beacon) {
        Log.d("MapView", "Found " + beacon.getId1().toString());
        mBeaconsFound.add(beacon);

        postInvalidate();
    }

    @Override
    public void onBeaconLost(Beacon beacon) {
        Log.d("MapView", "Lost " + beacon.getId1().toString());
        mBeaconsFound.remove(beacon);

        postInvalidate();
    }

    @Override
    public void onRangeChanged(Beacon beacon) {
        Log.d("MapView", "Changed " + beacon.getId1().toString());
        mBeaconsFound.remove(beacon);
        mBeaconsFound.add(beacon);

        if (mBeaconsFound.size() >= 3) {
            mPosition = Position.getByTrilateration(
                    getPositionForBeacon(mBeaconsFound.get(0)), mBeaconsFound.get(0).getDistance() * 2,
                    getPositionForBeacon(mBeaconsFound.get(1)), mBeaconsFound.get(1).getDistance() * 2,
                    getPositionForBeacon(mBeaconsFound.get(2)), mBeaconsFound.get(2).getDistance() * 2
            );

            for (Room room : mRooms) {
                if (room.contains(mPosition)) {
                    mActiveRoom = room;
                    break;
                }
            }
        } else {
            mPosition = null;
        }

        postInvalidate();
    }

    private Position getPositionForBeacon(Beacon beacon) {
        for (BeaconPosition beaconPosition : mBeaconPositions) {
            if (beacon.getId1().toString().equals(beaconPosition.getIdentifier())) {
                return beaconPosition;
            }
        }

        return null;
    }
}
