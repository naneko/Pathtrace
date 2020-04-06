package com.saltz.BounceVectors.Plane;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Plane {
    float ori_x;
    float ori_y;

    ArrayList<Boundary> boundaries = new ArrayList<>();

    public Plane(float origin_x, float origin_y) {
        this.ori_x = origin_x;
        this.ori_y = origin_y;
    }

    public void createBoundary(float start_x, float start_y, float end_x, float end_y) {
        boundaries.add(new Boundary(start_x, start_y, end_x, end_y));
    }
}
