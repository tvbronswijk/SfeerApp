package com.hueandme.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.annotations.Nullable;
import com.hueandme.position.BeaconPosition;
import com.hueandme.position.Position;
import com.hueandme.position.Room;
import com.hueandme.service.beacon.OnBeaconStatusChangedListener;
import com.hueandme.service.beacon.OnRoomChangedListener;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapView extends View implements OnBeaconStatusChangedListener, OnRoomChangedListener {

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

        //mBeaconsFound.add(new Beacon.Builder().setId1("e584fbcb-829c-48b2-88cc-f7142b926aeb").setTxPower(-66).setRssi(-58).build());
        mBeaconsFound.add(new Beacon.Builder().setId1("e584fbcb-829c-48b2-88cc-f7142b926aec").setTxPower(-66).setRssi(-54).build());
    }

    public boolean setRooms(Collection<Room> rooms) {
        mRooms.clear();
        return mRooms.addAll(rooms);
    }

    public boolean setBeaconPositions(Collection<BeaconPosition> positions) {
        mBeaconPositions.clear();
        return mBeaconPositions.addAll(positions);
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
            canvas.drawText("@" + mPosition.getPoint(), 50, 50, mTextPaint);
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

        postInvalidate();
    }

    @Override
    public void onRoomChanged(@Nullable Room room, @Nullable Position position) {
        if (room != null) {
            Log.d("MapView", "Room changed " + room.getName());
        } else {
            Log.d("MapView", "Out of room");
        }

        mActiveRoom = room;
        mPosition = position;
    }
}
