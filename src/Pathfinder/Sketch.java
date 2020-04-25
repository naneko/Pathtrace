package Pathfinder;

import javafx.geometry.Pos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processing.core.PApplet;

import java.util.ArrayList;

public class Sketch {
    public Logger logger = LogManager.getLogger(this.getClass().getName());

    public static PApplet sketch;

    public static ArrayList<PosVector> debugVectors = new ArrayList<>();

    public static ArrayList<LineSeg> checkLines = new ArrayList<>();

    public static void drawDebugVectors() {
        for(PosVector vec : debugVectors) {
            sketch.stroke(255, 0, 0);
            vec.debugDraw();
        }
    }

    public static void drawCheckLines() {
        for(LineSeg vec : checkLines) {
            sketch.stroke(255, 0, 255);
            vec.draw();
        }
    }
}