package com.hueandme.position;

import android.graphics.PointF;

public class Room {

    private final int id;
    private PointF topLeft;
    private PointF bottomRight;
    private String name;
    private String hueGroupId;

    public Room(int id, String name, PointF topLeft, PointF bottomRight) {
        this.id = id;
        this.name = name;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Room(int id, String name, float x1, float y1, float x2, float y2) {
        this.id = id;
        this.name = name;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHueGroupId() {
        return hueGroupId;
    }

    public void setHueGroupId(String hueGroupId) {
        this.hueGroupId = hueGroupId;
    }
}
