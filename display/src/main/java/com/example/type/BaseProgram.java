/*    */ package com.example.type;
/*    */ 
/*    */ import com.example.displayimage.IGLProgram;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.ByteOrder;
/*    */ 
/*    */ public abstract class BaseProgram
/*    */   implements IGLProgram
/*    */ {
/* 10 */   public float[] squareVertices = new float[] { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
/* 11 */   public float[] coordVertices = new float[] { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
/*    */   protected ByteBuffer mVerticeBuffer;
/*    */   protected ByteBuffer mCoordBuffer;
/*    */   
/*    */   public void setSquareVertices(float[] square, float[] coord) {
/*    */     int i;
/* 17 */     for (i = 0; i < square.length; i++) {
/* 18 */       this.squareVertices[i] = square[i];
/*    */     }
/* 20 */     for (i = 0; i < coord.length; i++) {
/* 21 */       this.coordVertices[i] = coord[i];
/*    */     }
/*    */     
/* 24 */     this.mVerticeBuffer = null;
/* 25 */     this.mCoordBuffer = null;
/* 26 */     this.mVerticeBuffer = ByteBuffer.allocateDirect(this.squareVertices.length * 4);
/* 27 */     this.mVerticeBuffer.order(ByteOrder.nativeOrder());
/* 28 */     this.mVerticeBuffer.asFloatBuffer().put(this.squareVertices);
/* 29 */     this.mVerticeBuffer.position(0);
/*    */     
/* 31 */     if (this.mCoordBuffer == null) {
/* 32 */       this.mCoordBuffer = ByteBuffer.allocateDirect(this.coordVertices.length * 4);
/* 33 */       this.mCoordBuffer.order(ByteOrder.nativeOrder());
/* 34 */       this.mCoordBuffer.asFloatBuffer().put(this.coordVertices);
/* 35 */       this.mCoordBuffer.position(0);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\type\BaseProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */