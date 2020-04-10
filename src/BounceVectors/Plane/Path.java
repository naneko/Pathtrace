package BounceVectors.Plane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

public class Path extends Sketch implements Iterable<Vector> {
    public Logger logger = LogManager.getLogger(this.getClass().getName());

    ArrayList<Vector> path = new ArrayList<>();
    ArrayList<Boundary> boundaries;

    int iterations = -1;

    // TODO: Detect closed loop

    Path(float origin_x, float origin_y, float angle, ArrayList<Boundary> boundaries) {
        this.boundaries = boundaries;
        path.add(new Vector(origin_x, origin_y, angle));
        logger.debug("New Path Created (" + this.toString() + ")");
    }

    Path(float origin_x, float origin_y, float angle, ArrayList<Boundary> boundaries, int iterations) {
        this.boundaries = boundaries;
        path.add(new Vector(origin_x, origin_y, angle));
        logger.debug("New Path Created (" + this.toString() + ")");
        this.iterations = iterations;
    }

    public PathIterator iterator() {
        return new PathIterator(this, iterations);
    }

    Vector getStart() {
        return path.get(0);
    }

    public void addToPath(Vector vec) {
        path.add(vec);
    }

    public void render() {
        for(Vector vec : path){
            vec.draw();
        }
    }
}

class PathIterator extends Sketch implements Iterator<Vector> {
    public Logger logger = LogManager.getLogger(this.getClass().getName());

    Path path;
    Vector current;
    boolean next = true;
    boolean isFirst = true;
    int iterations = -1;
    int iterationCounter = -1;

    public PathIterator(Path path) {
        // TODO: Detect closed loop and repeat path instead of generating new vectors
        this.path = path;
        this.current = this.path.getStart();
    }

    public PathIterator(Path path, int iterations) {
        // TODO: Detect closed loop and repeat path instead of generating new vectors
        this.path = path;
        this.current = this.path.getStart();
        this.iterations = iterations;
        logger.debug("Path Iterator Created");
    }

    @Override
    public boolean hasNext() {
        if(iterations == -1) {
            return true;
        } else if(!next) {
            return false;
        } else return iterationCounter < iterations;
    }

    /*
    Algorithm
        Find the intersection of all the boundaries
        Look for intersection only in the direction the vector is going
        Select closest intersection to vector origin
        Get intersection point
        Draw line from last origin to point
        Get angle of self and intersecting boundary and use it to calculate the reflection angle
        Use trig to solve for the adjacent and opposite lengths in order to create a new vector (I shouldn't have used vectors)
     */
    @Override
    public Vector next() throws ArithmeticException {
        iterations++;
        if(isFirst) {
            isFirst = false;
            return current;
        }
        Point temp;
        Point intersection = null;
        Boundary intersectedBoundary = null;
        for(Boundary boundary : path.boundaries) {
            try {
                temp = current.intersect(boundary);
            } catch(ArithmeticException e) {
                continue;
            }

            /*
                Angle is between 0 and 90, Intersection X+/Y+
                 |O
                -+-
                 |
            */
            if(current.angle >= 0 && 90 > current.angle && temp.x >= current.ori_x && temp.y >= current.ori_y) if(compareToIntersection(temp, intersection)) {
                intersection = temp;
                intersectedBoundary = boundary;
                logger.debug("Valid X+/Y+ Intersection Found: (" + intersection.x + ", " + intersection.y + ")");
                logger.debug("Boundry intersected:" + "(" + intersectedBoundary.ori_x + ", " + intersectedBoundary.ori_y + ") to (" + intersectedBoundary.term_x + ", " + intersectedBoundary.term_y + ")");
            }
            /*
                Angle is between 90 and 180, Intersection X-/Y+
                O|
                -+-
                 |
            */
            if(current.angle >= 90 && 180 > current.angle && temp.x <= current.ori_x && temp.y >= current.ori_y) if(compareToIntersection(temp, intersection)) {
                intersection = temp;
                intersectedBoundary = boundary;
                logger.debug("Valid X+/Y+ Intersection Found: (" + intersection.x + ", " + intersection.y + ")");
                logger.debug("Boundry intersected:" + "(" + intersectedBoundary.ori_x + ", " + intersectedBoundary.ori_y + ") to (" + intersectedBoundary.term_x + ", " + intersectedBoundary.term_y + ")");
            }
            /*
                Angle is between 180 and 270, Intersection X-/Y-
                 |
                -+-
                O|
            */
            if(current.angle >= 180 && 270 > current.angle && temp.x <= current.ori_x && temp.y <= current.ori_y) if(compareToIntersection(temp, intersection)) {
                intersection = temp;
                intersectedBoundary = boundary;
                logger.debug("Valid X+/Y+ Intersection Found: (" + intersection.x + ", " + intersection.y + ")");
                logger.debug("Boundry intersected:" + "(" + intersectedBoundary.ori_x + ", " + intersectedBoundary.ori_y + ") to (" + intersectedBoundary.term_x + ", " + intersectedBoundary.term_y + ")");
            }
            /*
                Angle is between 270 and 270, Intersection X+/Y-
                 |
                -+-
                 |O
            */
            if(current.angle >= 270 && 360 > current.angle && temp.x >= current.ori_x && temp.y <= current.ori_y) if(compareToIntersection(temp, intersection)) {
                intersection = temp;
                intersectedBoundary = boundary;
                logger.debug("Valid X+/Y+ Intersection Found: (" + intersection.x + ", " + intersection.y + ")");
                logger.debug("Boundry intersected:" + "(" + intersectedBoundary.ori_x + ", " + intersectedBoundary.ori_y + ") to (" + intersectedBoundary.term_x + ", " + intersectedBoundary.term_y + ")");
            }
        }
        if(intersection == null || intersectedBoundary == null || iterationCounter >= iterations) {
            logger.debug("No more intersections found");
            next = false;
            Vector output = new Vector(current.ori_x, current.ori_y, current.ori_x, current.ori_y);
            path.addToPath(output);
            return output;
        }
        else {
            logger.debug("Extending path");
            float angle = Math.min(Math.abs(current.angle - intersectedBoundary.angle), Math.abs(intersectedBoundary.angle - current.angle));
            current.terminateAt(intersection);
            this.current = new Vector(intersection.x, intersection.y, angle);
            path.addToPath(current);
            return current;
        }
    }

    private boolean compareToIntersection(Point temp, Point intersection) {
        if(intersection != null) {
            return temp.distanceTo(current.getOrigin()) < intersection.distanceTo(current.getOrigin()) && temp.in_bounds;
        } else {
            return true;
        }
    }
}