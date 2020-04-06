package com.saltz.BounceVectors.Plane;

class Boundary extends Vector {
    float term_x;
    float term_y;

    Boundary(float origin_x, float origin_y, float terminate_x, float terminate_y) {
        super(origin_x, origin_y, terminate_x-origin_x, terminate_y-origin_y);
        this.term_x = terminate_x;
        this.term_y = terminate_y;
    }
}
