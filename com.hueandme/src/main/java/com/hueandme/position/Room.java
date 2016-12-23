package com.hueandme.position;

import android.graphics.PointF;

public class Room {

    private final int id;
    private PointF topLeft;
    private PointF bottomRight;

    public Room(int id, PointF topLeft, PointF bottomRight) {
        this.id = id;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Room(int id, float x1, float y1, float x2, float y2) {
        this.id = id;
        this.topLeft = new PointF(x1, y1);
        this.bottomRight = new PointF(x2, y2);
    }

    public int getId() {
        return id;
    }

    public PointF getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(PointF topLeft) {
        this.topLeft = topLeft;
    }

    public PointF getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(PointF bottomRight) {
        this.bottomRight = bottomRight;
    }

    public boolean contains(Position position) {
        PointF point = position.getPoint();
        return point.x >= topLeft.x
                && point.x <= bottomRight.x
                && point.y >= topLeft.y
                && point.y <= bottomRight.y;
    }
}
