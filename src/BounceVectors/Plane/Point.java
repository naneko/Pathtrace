package BounceVectors.Plane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Point extends Sketch {
    public Logger logger = LogManager.getLogger(this.getClass().getName());

    float x;
    float y;
    boolean in_bounds;
    int direction;

    Point(float x, float y, boolean in_bounds, int direction) {
        this.x = x;
        this.y = y;
        this.in_bounds = in_bounds;
        this.direction = direction;
        logger.trace("Point created (" + this.toString() + ")");
    }

    Point(float x, float y, boolean in_bounds) {
        this(x, y, in_bounds, 0);
    }

    Point(float x, float y) {
        this(x, y, true, 0);
    }

    Point() {
        this(0, 0, true, 0);
        logger.trace("Empty point defined");
    }

    Point storeIntersection(Vector vec1, Vector vec2) throws ArithmeticException {
        // Source: https://stackoverflow.com/questions/20677795/how-do-i-compute-the-intersection-point-of-two-lines
        logger.trace("Getting the intersection between " + vec1.toString() + " and " + vec2.toString() + " (" + this.toString() + ")");
        Line L1 = vec1.toLine();
        Line L2 = vec2.toLine();
        float D = L1.A * L2.B - L1.B * L2.A;
        float Dx = L1.C * L2.B - L1.B * L2.C;
        float Dy = L1.A * L2.C - L2.C * L2.A;

        logger.trace("D: " + D + " | Dx: " + Dx + " | Dy: " + Dy);

        // D will be 0 with parallel lines, thus will throw an ArithmeticException
        this.x = Dx / D;
        this.y = Dy / D;

        logger.debug( "Intersection found: " + "(" + this.x + ", " + this.y + ") (" + this.toString() + ")");

        return this;
    }

    float distanceTo(Point point) {
        return (float) Math.sqrt(Math.pow(point.x - this.x, 2) + Math.pow(point.y - this.y, 2));
    }
}
