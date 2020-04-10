package BounceVectors.Plane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import processing.core.PApplet;

public class Sketch {
    public static Logger log = LogManager.getLogger("Sketch");

    public static PApplet sketch;

    public static void setSketch(PApplet sketch) {
        Sketch.sketch = sketch;
    }
}
