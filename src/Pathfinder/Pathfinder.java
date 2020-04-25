package Pathfinder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processing.core.PApplet;

import java.util.Iterator;

public class Pathfinder extends PApplet {
    public Logger logger = LogManager.getLogger(this.getClass().getName());

    Path path;
    Path.PathIterator iterator;

    double angle = 0;

    public void settings() {
        Sketch.sketch = this;
        size(640, 640);
//        fullScreen();
    }

    public void setup() {
        frameRate(60);
        path = new Path(width/2., height/2., Math.sin(angle), Math.cos(angle));
        path.addQuadBound(50,50,50, height-50, height-50, height-50, height-50, 50);
//        noLoop();
    }

    public void draw() {
        path.setStartDirection(Math.sin(angle), Math.cos(angle));
        background(255);
        iterator = path.iterator();
        stroke(0);
//        ellipse(mouseX, mouseY, 20, 20);
        stroke(0, 255, 0);
        path.drawBounds();
        stroke(0);
        while (iterator.hasNext()) {
            iterator.next();
        }
        path.drawPath();
//        Sketch.drawDebugVectors();
//        Sketch.drawCheckLines();
        Sketch.checkLines.clear();
        Sketch.debugVectors.clear();

        angle = (angle + 0.0001) % 360;

        delay(10);
    }

//    public void mousePressed() {
//        setup();
//        Sketch.debugVectors.clear();
//        Sketch.checkLines.clear();
//    }
//
//    public void keyPressed() {
//        logger.debug("---========================================================================================================================---");
//        redraw();
//    }

    public static void main(String[] args){
        String[] processingArgs = {"Pathfinder"};
        Pathfinder pt = new Pathfinder();
        PApplet.runSketch(processingArgs, pt);
    }
}