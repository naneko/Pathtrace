package BounceVectors.Plane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Line extends Sketch {
    public Logger logger = LogManager.getLogger(this.getClass().getName());

    float A;
    float B;
    float C;

    Line(float A, float B, float C) {
        this.A = A;
        this.B = B;
        this.C = C;
        logger.trace("Line Created: " + A + ", " + B + ", " + C);
    }

    Line() {
        this(0, 0, 0);
    }
}