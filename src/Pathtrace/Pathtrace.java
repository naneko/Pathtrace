package Pathtrace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processing.core.PApplet;

public class Pathtrace extends PApplet {
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
//        path = new Path(50., height/2., Math.sin(angle), Math.cos(angle), 100);
        path.addQuadBound(50,50,50, height-50, height-50, height-50, height-50, 50);
//        for(int i = 0; i < 360; i+=45) {
//            path.addBound(sin(i)*300+width/2., cos(i)*300+height/2.,sin(i+45)*300+width/2., cos(i+45)*300+height/2.);
//        }
//        path.addQuadBound(50,50,50, height-100, height-50, height-77, height-153, 50);
//        noLoop();
    }

    public void draw() {
        path.setStartDirection(Math.sin(angle), Math.cos(angle));
        background(0);
        iterator = path.iterator();
        stroke(255, 255, 0);
        path.drawBounds();
        stroke(255);
        while (iterator.hasNext()) {
            iterator.next();
        }
        path.drawPath();
//        Sketch.drawDebugVectors();
//        Sketch.drawCheckLines();
        Sketch.checkLines.clear();
        Sketch.debugVectors.clear();

        angle = (angle + 0.0001) % 360;

        if(terminate) {
            stroke(255, 0, 255);
            line(mPosX, mPosY, mouseX, mouseY);
        }
    }

    boolean terminate = false;
    int mPosX = 0;
    int mPosY = 0;

    public void mousePressed() {
        if(mouseButton == LEFT) {
            if(!terminate) {
                mPosX = mouseX;
                mPosY = mouseY;
                terminate = true;
            } else {
                path.addBound(mPosX, mPosY, mouseX, mouseY);
                terminate = false;
            }
        }
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
        String[] processingArgs = {"Pathtrace"};
        Pathtrace pt = new Pathtrace();
        PApplet.runSketch(processingArgs, pt);
    }
}