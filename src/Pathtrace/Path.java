package Pathtrace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.vecmath.Vector2d;

/**
 * Creates a Pathfilder path that can be rendered with Processing
 */
public class Path extends Sketch implements Iterable<Path> {
    private double startX;
    private double startY;
    private double startDirectionX;
    private double startDirectionY;

    private int maxIterations = 100;
    private int maxProjections = 1000;

    public Logger logger = LogManager.getLogger(this.getClass().getName());

    private ArrayList<LineSeg> path;
    private ArrayList<LineSeg> bounds;

    private Point2D intersect;

    public Path(double startX, double startY, double directionX, double directionY) {
        this.path = new ArrayList<>();
        this.bounds = new ArrayList<>();
        this.startX = startX;
        this.startY = startY;
        this.startDirectionX = directionX;
        this.startDirectionY = directionY;
        logger.debug("Created new path");
    }

    public Path(double startX, double startY, double directionX, double directionY, int maxIterations) {
        this(startX, startY, directionX, directionY);
        this.maxIterations = maxIterations;
        logger.debug("Created new path");
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void setMaxProjections(int maxProjections) {
        this.maxProjections = maxProjections;
    }

    public void setStartPoint(double startX, double startY) {
        this.startX = startX;
        this.startY = startY;
        path.clear();
    }


    public void setStartDirection(double startDirectionX, double startDirectionY) {
        this.startDirectionX = startDirectionX;
        this.startDirectionY = startDirectionY;
        path.clear();
    }

    public void addBound(LineSeg segment) {
        bounds.add(segment);
        logger.debug("Added new bound ({}, {}) -> ({}, {})", segment.x1, segment.y1, segment.x2, segment.y2);
    }

    public void addBound(double x1, double y1, double x2, double y2) {
        bounds.add(new LineSeg(x1, y1, x2, y2));
    }

    public void clearBounds() {
        bounds.clear();
    }

    public void addQuadBound(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        bounds.add(new LineSeg(x1, y1, x2, y2));
        bounds.add(new LineSeg(x2, y2, x3, y3));
        bounds.add(new LineSeg(x3, y3, x4, y4));
        bounds.add(new LineSeg(x4, y4, x1, y1));
    }

    public void drawBounds() {
        for(LineSeg bound : bounds) {
            bound.draw();
        }
    }

    public void drawPath() {
        for(LineSeg seg : path) {
            seg.draw();
        }
    }

    public LineSeg getFirst() {
        return path.get(0);
    }

    public LineSeg getCurrent() {
        return path.get(path.size() - 1);
    }

    private void addPath(LineSeg segment) {
        path.add(segment);
        logger.debug("Added new path segment ({}, {}) -> ({}, {})", segment.x1, segment.y1, segment.x2, segment.y2);
    }

    @Override
    public PathIterator iterator() {
        return new PathIterator(this);
    }

    public static class PathIterator extends Sketch implements Iterator<Path> {
        private final int maxIterations;
        private final int maxProjections;
        private Path path;
        private boolean first;
        private LineSeg lastBoundIntersect;
        private boolean hasNextOverride; // Set to false to stop iteration
        private int iterations;

        public PathIterator(Path path) {
            this.path = path;
            this.first = true;
            this.hasNextOverride = true;
            this.iterations = 0;
            this.maxIterations = path.maxIterations;
            this.maxProjections = path.maxProjections;
            logger.debug("New Iterator Initialized");
        }

        @Override
        public boolean hasNext() {
            if(first) return true;
            if(hasNextOverride && iterations < maxIterations) {
                LineSeg current = path.getCurrent();
                LineSeg first = path.getFirst();
                return current.getX2() != first.getX1() || current.getY2() != first.getX1();
            }
            return false;
        }

        @Override
        public Path next() {
            // TODO: Check for bugs
            iterations++;
            PosVector projection; // The vector that gets casted out to find the intersections
            LineSeg check; // The line segment the vector is converted to to check for intersection
            Point2D currentIntersection; // The current intersection being checked
            Point2D closestIntersection = null; // The closest intersection found so far
            Point2D startPoint; // The start point of the current next line being calculated
            int PROJECTION_COUNT = 0;
            if(this.first) {
                // If this is the first line in the path, make it
                projection = new PosVector(path.startX, path.startY, path.startDirectionX, path.startDirectionY);
                projection.normalize();
                startPoint = new Point2D.Double(path.startX, path.startY);
                this.first = false;
            } else {
                // If this is not the first line in the path, convert the last line to a vector
                projection = path.getCurrent().toVector();
                startPoint = path.getCurrent().getEnd(); // Set the start point to the end of the last line in the path (the last intersection)
                projection.normalize(); // Normalize said vector
                projection = PosVector.reflect(projection, lastBoundIntersect.toVector()).toPosVec(startPoint); // Reflect said vector off the last intersected bound
            }
            logger.debug("=====START NEXT=====");
            logger.debug("Projection is ({}, {}) ==> ({}, {})", projection.getStart().getX(), projection.getStart().getY(), projection.getEnd().getX(), projection.getEnd().getY());
            logger.debug("Start point is ({}, {})", startPoint.getX(), startPoint.getY());
            while(PROJECTION_COUNT++ <= maxProjections) { // Loop until hit MAX_PROJECTIONS
                check = projection.toLine(); // Change the vector to a line so we can check the intersection
//                logger.debug("Check is ({}, {}) -> ({}, {})", check.x1, check.y1, check.x2, check.y2);

                // First check all bounds for intersection of the current projection, and if one is found make sure it is the closest one to startPoint in case there are multiple
                for(LineSeg bound : path.bounds) {
                    currentIntersection = bound.getIntersectionPoint(check);
                    if(currentIntersection != null) {
                        if(currentIntersection.distance(startPoint) <= 0.00000000001) continue; // Error Checking
                        if(closestIntersection != null) {
                            if (currentIntersection.distance(startPoint) < closestIntersection.distance(startPoint)) {
                                closestIntersection = currentIntersection;
                                lastBoundIntersect = bound;
                            }
                        } else {
                            closestIntersection = currentIntersection;
                            lastBoundIntersect = bound;
                        }
                    }
                }

                Sketch.checkLines.add(check);

                if(closestIntersection != null) {
                    logger.debug("Intersection is ({}, {})", closestIntersection.getX(), closestIntersection.getY());
                }

                // If there a closest intersection, stop checking
                // If there isn't, project vector forward and check again
                if(closestIntersection != null) break;
                else projection = projection.project();
            }

            // If an intersection exists, append it to the path, or else set hasNext() to false
            if(closestIntersection != null) {
                this.path.addPath(new LineSeg(startPoint, closestIntersection));
            } else {
                logger.debug("Reached MAX_ITERATIONS. Stopping.");
                this.hasNextOverride = false;
            }
            logger.debug("=====END NEXT=====");
            return this.path;
        }
    }
}

class Vector extends Vector2d {
    public Vector(double directionX, double directionY) {
        super(directionX, directionY);
    }

    public Vector(Vector2d vec) {
        super(vec);
    }

    public PosVector toPosVec(double startX, double startY) {
        return new PosVector(startX, startY, this.x, this.y);
    }

    public PosVector toPosVec(Point2D start) {
        return new PosVector(start, this);
    }

    // region Subtraction
    public static Vector sub(Vector2d vec1, Vector2d vec2) {
        return new Vector(vec1.x - vec2.x, vec1.y - vec2.y);
    }

    public static Vector sub(Vector2d vec1, double constant) {
        return new Vector(vec1.x - constant, vec1.y - constant);
    }

    public Vector sub(double constant) {
        return new Vector(this.x - constant, this.y - constant);
    }

    public Vector sub(Vector2d vec) {
        return new Vector(this.x - vec.x, this.y - vec.y);
    }

    public Vector subFrom(Vector2d vec) {
        return new Vector(vec.x - this.x, vec.y - this.y);
    }
    // endregion sub

    // region Addition
    public static Vector add(Vector2d vec1, Vector2d vec2) {
        return new Vector(vec1.x + vec2.x, vec1.y + vec2.y);
    }

    public static Vector add(Vector2d vec1, double constnat) {
        return new Vector(vec1.x + constnat, vec1.y + constnat);
    }

    public Vector add(double constnat) {
        return new Vector(this.x + constnat, this.y + constnat);
    }

    public Vector add(Vector2d vec) {
        return new Vector(this.x + vec.x, this.y + vec.y);
    }
    // endregion Addition

    // region Multiplication
    public static Vector mult(Vector2d vec1, Vector2d vec2) {
        return new Vector(vec1.x * vec2.x, vec1.y * vec2.y);
    }

    public static Vector mult(Vector2d vec1, double constant) {
        return new Vector(vec1.x * constant, vec1.y * constant);
    }

    public Vector mult(double constant) {
        return new Vector(this.x * constant, this.y * constant);
    }

    public Vector mult(Vector2d vec) {
        return new Vector(this.x * vec.x, this.y * vec.y);
    }
    // endregion Multiplication
}

class PosVector extends Vector {
    private final Point2D start;
    private final Vector direction;

    public PosVector(Point2D start, Vector direction) {
        super(direction);
        this.start = start;
        this.direction = direction;
    }

    public PosVector(double startX, double startY, double directionX, double directionY) {
        this(new Point2D.Double(startX, startY), new Vector(directionX, directionY));
    }

    public Point2D getStart() {
        return start;
    }

    public Point2D getEnd() {
        return new Point2D.Double(start.getX() + direction.x, start.getY() + direction.y);
    }

    public Vector getDirection() {
        return direction;
    }

    public LineSeg toLine(PosVector vec) {
        return new LineSeg(vec.getStart().getX(), vec.getStart().getY(), vec.getStart().getX() + vec.getDirection().x, vec.getStart().getY() + vec.getDirection().y);
    }

    public LineSeg toLine() {
        return toLine(this);
    }

    public PosVector project() {
        // Move the vector forward by 1 of itself
        return new PosVector(this.getStart().getX() + this.getDirection().x, this.getStart().getY() + this.getDirection().y, this.getDirection().x, this.getDirection().y);
    }

    public void debugDraw() {
        LineSeg draw = this.toLine();
        Sketch.sketch.stroke(255, 0, 0);
        draw.draw();
        Sketch.sketch.stroke(0);
    }

    /**
     * Gets the non-positional reflection of vector <code>vector</code> off <code>surface</code>
     *
     * @param vector The positional vector to calculate
     * @param surface The surface to reflect from
     * @return Non-positional vector (use toPosVec to add a start position)
     */
    public static Vector reflect(Vector vector, Vector surface) {
        // NOTE: DO NOT NORMALIZE THE SURFACE VECTOR

        Logger logger = LogManager.getLogger("reflect");

        PosVector normal;

        // Get Normal Vector
        double dx = surface.x;
        double dy = surface.y;
        logger.debug("Angle: {}", Math.toDegrees(vector.angle(surface)));

        if(vector.angle(surface) < Math.PI/2) {
            normal = new PosVector(Sketch.sketch.width/2., Sketch.sketch.height/2., -dy, dx);
            logger.debug("A");
        } else {
            normal = new PosVector(Sketch.sketch.width/2., Sketch.sketch.height/2., dy, -dx);
            logger.debug("B");
        }
        // TODO: Check this actually makes the right normal vector

        Sketch.debugVectors.add(normal);

        normal.normalize();

        return vector.sub(normal.mult(2*normal.dot(vector))); // v - 2(v dot n)n

//        return normal.mult(2*normal.dot(vector)).sub(vector).mult(-1);
    }
}

class LineSeg extends Line2D.Double {
    public LineSeg(double x1, double y1, double x2, double y2) {
        setLine(x1, y1, x2, y2);
    }

    public LineSeg(Point2D p1, Point2D p2) {
        setLine(p1, p2);
    }

    public Point2D.Double getIntersectionPoint(LineSeg line1, LineSeg line2) {
        if (! line1.intersectsLine(line2) ) return null;
        double px = line1.getX1(),
                py = line1.getY1(),
                rx = line1.getX2()-px,
                ry = line1.getY2()-py;
        double qx = line2.getX1(),
                qy = line2.getY1(),
                sx = line2.getX2()-qx,
                sy = line2.getY2()-qy;

        double det = sx*ry - sy*rx;
        if (det == 0) {
            return null;
        } else {
            double z = (sx*(qy-py)+sy*(px-qx))/det;
            if (z==0 ||  z==1) return null;  // intersection at end point!
            return new Point2D.Double(
                    px+z*rx, py+z*ry);
        }
    }

    public Point2D.Double getIntersectionPoint(LineSeg line) {
        return getIntersectionPoint(this, line);
    }

    public PosVector toVector() {
        return new PosVector(x1, y1, x2 - x1, y2 - y1);
    }

    public Point2D getStart() {
        return this.getP1();
    }

    public Point2D getEnd() {
        return this.getP2();
    }

    public void draw() {
        Sketch.sketch.line(
                (float) this.getX1(),
                (float) this.getY1(),
                (float) this.getX2(),
                (float) this.getY2());
    }
}