package BounceVectors.Plane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Iterator;

public class Plane extends Sketch {
    public Logger logger = LogManager.getLogger(this.getClass().getName());

    // TODO: Implement plane origin
    float ori_x;
    float ori_y;

    ArrayList<Boundary> boundaries = new ArrayList<>();
    ArrayList<Path> paths = new ArrayList<>();

    public Plane(float origin_x, float origin_y) {
        logger.info("Plane Initialized (" + this.toString() + ")");
        logger.warn("Closed loop detection is not implemented. The path renderer will continue infinitely.");
        this.ori_x = origin_x;
        this.ori_y = origin_y;
    }

    public void boundary(float start_x, float start_y, float end_x, float end_y) {
        boundaries.add(new Boundary(start_x, start_y, end_x, end_y));
        logger.info("Boundary Created");
    }

    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        boundaries.add(new Boundary(x1, y1, x2, y2));
        boundaries.add(new Boundary(x2, y2, x3, y3));
        boundaries.add(new Boundary(x3, y3, x4, y4));
        boundaries.add(new Boundary(x4, y4, x1, y1));
        logger.info("Quad Created");
    }

    public void spawnPath(float start_x, float start_y, float angle){
        paths.add(new Path(start_x, start_y, angle, boundaries));
        logger.info("Path Spawned");
    }

    public void spawnPath(float start_x, float start_y, float angle, int iterations){
        paths.add(new Path(start_x, start_y, angle, boundaries, iterations));
        logger.info("Path Spawned");
    }

    public void renderBoundaries() {
        for(Boundary bound : boundaries) {
            bound.draw();
        }
    }

    public void renderPaths() {
        for(Path path : paths) {
            for (Vector next : path) {
                next.draw();
            }
            path.render();
        }
    }

    public Iterator<Boundary> getBoundaryIterator() {
        return boundaries.iterator();
    }

    public PathIterator getPathIterator(int index) {
        return paths.get(index).iterator();
    }
}


// Make a class that all other classes extend that contains a PApplet object