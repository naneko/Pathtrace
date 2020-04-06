package com.saltz.BounceVectors.Plane;

class Line {
    float A;
    float B;
    float C;

    Line(float A, float B, float C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

    Line() {
        this(0, 0, 0);
    }
}