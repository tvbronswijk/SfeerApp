package com.hueandme.position;

import android.graphics.PointF;

public class Position {

    private PointF point;

    public Position(PointF point) {
        this.point = point;
    }

    public Position(float x, float y) {
        this.point = new PointF(x, y);
    }

    public PointF getPoint() {
        return point;
    }

    public void setPoint(PointF point) {
        this.point = point;
    }

    public double distanceTo(Position other) {
        return Math.sqrt(Math.pow(point.x - other.point.x, 2) + Math.pow(point.y - other.point.y, 2));
    }

    /**
     * @param location1
     * @param distance1
     * @param location2
     * @param distance2
     * @param location3
     * @param distance3
     * @return
     * @see <a href="http://stackoverflow.com/q/30336278/1729860">StackOverflow source</a>
     */
    public static Position getByTrilateration(
            Position location1, double distance1,
            Position location2, double distance2,
            Position location3, double distance3) {

        double top = 0;
        double bot = 0;
        for (int i = 0; i < 3; i++) {
            Position position1;
            Position position2;
            Position position3;
            double distance;

            if (i == 0) {
                position1 = location1;
                position2 = location2;
                position3 = location3;
                distance = distance1;
            } else if (i == 1) {
                position1 = location2;
                position2 = location1;
                position3 = location3;
                distance = distance2;
            } else {
                position1 = location3;
                position2 = location1;
                position3 = location2;
                distance = distance3;
            }

            double d = position2.getPoint().x - position3.getPoint().x;

            double v1 = (position1.getPoint().x * position1.getPoint().x + position1.getPoint().y * position1.getPoint().y) - (distance * distance);
            top += d * v1;

            double v2 = position1.getPoint().y * d;
            bot += v2;

        }

        double y = top / (2 * bot);
        top = distance2 * distance2 + location1.getPoint().x * location1.getPoint().x + location1.getPoint().y * location1.getPoint().y - distance1 * distance1 - location2.getPoint().x * location2.getPoint().x - location2.getPoint().y * location2.getPoint().y - 2 * (location1.getPoint().y - location2.getPoint().y) * y;
        bot = location1.getPoint().x - location2.getPoint().x;
        double x = top / (2 * bot);

        return new Position((float) x, (float)y);
    }

}
