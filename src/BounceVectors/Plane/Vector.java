package BounceVectors.Plane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Vector extends Sketch {
    public Logger logger = LogManager.getLogger(this.getClass().getName());

    float vel_x;
    float vel_y;
    float ori_x;
    float ori_y;
    float term_x;
    float term_y;
    boolean terminated = false;
    float angle;

    Vector(float origin_x, float origin_y, float angle) {
        this.ori_x = origin_x;
        this.ori_y = origin_y;
        this.angle = angle % 360;
        // Angle can only be between 0 and 359
        // sin(p) = o / h
        // hsin(p) = o
        this.vel_x = (float) Math.cos(Math.toRadians(this.angle));
        this.vel_y = (float) Math.sin(Math.toRadians(this.angle));
        logger.debug("New Vector Created (No Termination) (" + origin_x + ", " + origin_y + ") (" + this.toString() + ")");
        logger.debug("Velocity X: " + vel_x + " | Velocity Y: " + vel_y);
    }

    Vector(float origin_x, float origin_y, float terminate_x, float terminate_y) {
        this.ori_x = origin_x;
        this.ori_y = origin_y;
        this.term_x = terminate_x;
        this.term_y = terminate_y;
        this.vel_x = terminate_x - origin_x;
        this.vel_y = terminate_y - origin_y;
        this.terminated = true;
        // Angle can only be between 0 and 359
        this.angle = (float) (Math.atan(vel_x / vel_y) / Math.PI * 180) % 360;
        logger.debug("New Vector Created" + "(" + origin_x + ", " + origin_y + ") to (" + terminate_x + ", " + terminate_y + ") (" + this.toString() + ")");
    }

    Point intersect(Vector vec) throws ArithmeticException {
        // TODO: Cache optimization
        return new Point().storeIntersection(this, vec);
    }

    Point intersect(Boundary vec) throws ArithmeticException {
        // TODO: Cache optimization

        // TODO: Elsewhere in code, need to check for in_bounds
        Point point = new Point().storeIntersection(this, vec);

        if(vec.ori_x <= vec.term_x) if(point.x < vec.ori_x || point.x > vec.term_x) point.in_bounds = false;
        else if(point.x > vec.ori_x || point.x < vec.term_x) point.in_bounds = false;

        else if(vec.ori_y <= vec.term_y) if(point.y < vec.ori_y || point.y > vec.term_y) point.in_bounds = false;
        else if(point.y > vec.ori_y || point.y < vec.term_y) point.in_bounds = false;

        logger.debug("Boundary intersection in bounds: " + point.in_bounds);

        return point;
    }

    void terminateAt(Point point) {
        this.term_x = point.x;
        this.term_y = point.y;
        this.terminated = true;
        logger.debug("Vector termination set (" + this.toString() + ")");
    }

    Point getOrigin() {
        return new Point(ori_x, ori_y);
    }

    Line toLine() {
        logger.trace("Converting to line (" + this.toString() + ")");

        // TODO: Cache optimization
        float A = this.ori_y - (this.ori_y + this.vel_y);
        float B = (this.ori_x + this.vel_x) - this.ori_x;
        float C = (this.ori_x * (this.ori_y + this.vel_y)) - ((this.ori_x + this.vel_x) * this.ori_y);

        return new Line(A, B, -C);
    }

    public void draw() {
        if(terminated){
            logger.debug("Drawing line: (" + ori_x + ", " + ori_y + ") (" + term_x + ", " + term_y + ")");
            sketch.line(ori_x, ori_y, term_x, term_y);
        }
    }
}
