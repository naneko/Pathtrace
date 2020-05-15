/**
Iterate Path
------------
Iterates through each bounce on click
Chooses a random starting angle on each run

Left-click to move forward one iteration
Center-click to start the creation of a boundary and left click again to finish it
Right-click in the middle of creation to cancel
Right-click while not creating a line to clear all created boundaries
Press any key to slowly rotate the line

Created by Ben Saltz
**/

import processing.serial.*;
import Pathtrace.Path;

Path path;
Path.PathIterator iterator;

float angle = random(0, 360); // Set a random starting angle
int maxIter = 0; // Number of bounce iterations to start with
boolean rotate = false; // Slowly rotate the line

void setup() {
  size(800, 800); 
  smooth();
  frameRate(60);
  
  Sketch.sketch = this; // Tell pathtrace to use this sketch
  path = new Path(width/2, height/2, sin(angle), cos(angle), maxIter); // Initialize a new path starting in the middle of the sketch
  path.setMaxProjections(1920*2); // This is how many times to move the vector forward to find a boundary before giving up
  path.addQuadBound(50,50,
                    50, height-50,
                    width-50, height-50,
                    width-50, 50); // Add a quad boundary that is 50px away from the edges of the sketch
}

void draw() {
  background(0);
  
  if(rotate) angle = (angle + 0.005) % 360; // Slowly rotate the line
  
  path.setStartDirection(Math.sin(radians(angle)), Math.cos(radians(angle))); // Update the starting direction of the starting vector
  
  // Draw the boundary lines in yellow
  stroke(255, 255, 0);
  path.drawBounds();
  
  // > Calculate the path <
  iterator = path.iterator(); // Initialize the iterator that calculates and draws the path
  // Iterate through until the number of iterations set in maxItar or until it can't find a boundary
  while (iterator.hasNext()) {
      iterator.next();
  }
  
  // > Draw the path <
  stroke(255);
  path.drawPath();
  
  // In the current version, some debug arrays need to be cleared every iteration
  Sketch.checkLines.clear();
  Sketch.debugVectors.clear();
  
  // This draws a pink line when you are creating a new boundary
  if(terminateDrawBoundary) {
      stroke(255, 0, 255);
      line(mPosX, mPosY, mouseX, mouseY);
  }
}

boolean terminateDrawBoundary = false; // Determine weather to create a boundary on the next click
int mPosX, mPosY = 0; // The starting position of the new boundary

public void mousePressed() {
  switch(mouseButton) {
    case CENTER: 
      if(!terminateDrawBoundary) {
        // Start drawing the new boundary
        mPosX = mouseX;
        mPosY = mouseY;
        terminateDrawBoundary = true;
      } else {
        path.addBound(mPosX, mPosY, mouseX, mouseY); // Add a boundary line from the start point to the mouse
        terminateDrawBoundary = false; // Finisn drawing the new boundary
      }
      break;
    case RIGHT:
      if(terminateDrawBoundary) {
        terminateDrawBoundary = false; // Cancel drawing a new boundary
      } else {
        path.clearBounds(); // Clear all the boundaries
        path.addQuadBound(50,50,
                    50, height-50,
                    width-50, height-50,
                    width-50, 50); // Recreate the starting quad boundary
      }
      break;
    case LEFT:
      maxIter++;
      path.setMaxIterations(maxIter);
  }
}

void keyPressed() {
  rotate = true;
}

void keyReleased() {
  rotate = false;
}
