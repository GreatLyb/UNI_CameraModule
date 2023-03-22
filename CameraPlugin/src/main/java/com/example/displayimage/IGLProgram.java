package com.example.displayimage;

import java.nio.Buffer;

public interface IGLProgram {
  boolean isProgramBuilt();
  
  void buildProgram();
  
  void buildTextures(Buffer paramBuffer, int paramInt1, int paramInt2);
  
  void drawFrame();
  
  void createBuffers(float[] paramArrayOffloat);
  
  float[] getSquareVertices();
  
  void checkGlError(String paramString);
}


/* Location:              D:\DeskTop\classes.jar!\com\example\displayimage\IGLProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */