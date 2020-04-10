package BounceVectors.Plane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Boundary extends Vector {
    public Logger logger = LogManager.getLogger(this.getClass().getName());

    Boundary(float origin_x, float origin_y, float terminate_x, float terminate_y) {
        super(origin_x, origin_y, terminate_x, terminate_y);
    }
}
