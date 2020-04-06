package com.saltz.BounceVectors.Plane;

class Vector {
    float vel_x;
    float vel_y;
    float ori_x;
    float ori_y;

    Vector(float origin_x, float origin_y, float velocity_x, float velocity_y) {
        this.vel_x = velocity_x;
        this.vel_y = velocity_y;
        this.ori_x = origin_x;
        this.ori_y = origin_y;
    }

    Point intersect(Vector vec) {
        // TODO: Cache optimization

        return new Point().storeIntersection(this, vec);
    }

    Point intersect(Boundary vec) {
        // TODO: Cache optimization
        Point point = new Point().storeIntersection(this, vec);

        if(vec.ori_x <= vec.term_x) if(point.x < vec.ori_x || point.x > vec.term_x) point.in_bounds = false;
        else if(point.x > vec.ori_x || point.x < vec.term_x) point.in_bounds = false;

        if(vec.ori_y <= vec.term_y) if(point.y < vec.ori_y || point.y > vec.term_y) point.in_bounds = false;
        else if(point.y > vec.ori_y || point.y < vec.term_y) point.in_bounds = false;

        return point;
    }

    Line to_line() {
        // TODO: Cache optimization
        float A = this.ori_y - (this.ori_y + this.vel_y);
        float B = (this.ori_x + this.vel_x) - this.ori_x;
        float C = this.ori_x * (this.ori_y + this.vel_y) - (this.ori_x + this.vel_x) * this.ori_y;

        return new Line(A, B, C);
    }
}
