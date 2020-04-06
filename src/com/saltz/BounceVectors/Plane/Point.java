package com.saltz.BounceVectors.Plane;

class Point {
    float x;
    float y;
    boolean in_bounds;
    int direction;

    Point(float x, float y, boolean in_bounds, int direction) {
        this.x = x;
        this.y = y;
        this.in_bounds = in_bounds;
        this.direction = direction;
    }

    Point(float x, float y, boolean in_bounds) {
        this(x, y, in_bounds, 0);
    }

    Point(float x, float y) {
        this(x, y, true, 0);
    }

    Point() {
        this(0, 0, false, 0);
        System.out.println("Warning: empty point defined");
    }

    Point storeIntersection(Vector vec1, Vector vec2) {
        // Source: https://stackoverflow.com/questions/20677795/how-do-i-compute-the-intersection-point-of-two-lines
        Line L1 = vec1.to_line();
        Line L2 = vec2.to_line();
        float D = L1.A * L2.B - L1.B * L2.A;
        float Dx = L1.C * L2.B - L1.B * L2.C;
        float Dy = L1.A * L2.C - L2.C * L2.A;

        this.x = Dx / D;
        this.y = Dy / D;

        return this;
    }
}
