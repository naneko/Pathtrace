package BounceVectors;

import BounceVectors.Plane.Plane;
import BounceVectors.Plane.Sketch;
import BounceVectors.Plane.Vector;
import processing.core.PApplet;

import java.util.Iterator;

public class BounceVectors extends PApplet {
    Plane plane;
    Iterator<Vector> pathIterator;

    public void settings() {
        size(640, 640);
    }

    public void setup() {
        Sketch.setSketch(this);
        plane = new Plane(0, 0);
        plane.quad(10, 10, 360, 10, 360, 600, 10, 600);
        plane.spawnPath(15, 15, 45, 10);
        background(255);
        stroke(255, 0, 0);
        plane.renderBoundaries();
        stroke(0);
        plane.renderPaths();
    }

    public void draw() {
//        pathIterator = plane.getPathIterator(0);
    }

//    public void mousePressed() {
//        background(0, 255, 0);
//        Sketch.log.info("Rendering Next");
//        Vector next = pathIterator.next();
//        next.draw();
//    }
//
//    public void mouseReleased() {
//        background(255);
//    }

    public static void main(String[] args){
        String[] processingArgs = {"BounceVectors"};
        BounceVectors bv = new BounceVectors();
        PApplet.runSketch(processingArgs, bv);
    }
}
