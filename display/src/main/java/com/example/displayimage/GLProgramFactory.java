/*    */ package com.example.displayimage;
/*    */ import android.content.Context;
/*    */ import com.example.type.BaseProgram;
/*    */ import com.example.type.BmpGLProgram;
/*    */ import com.example.type.Mono8GLProgram;
/*    */ import com.example.type.NV21GLProgram;
/*    */ import com.example.type.RGBGLProgram;
/*    */ import com.example.type.YUV422GLProgram;
/*    */ 
/*    */ public class GLProgramFactory {
/*    */   public static BaseProgram createGLProgram(int glPixelFormat, Context context) {
/*    */     BmpGLProgram bmpGLProgram;
/*    */     Mono8GLProgram mono8GLProgram;
/*    */     RGBGLProgram rGBGLProgram;
/*    */     NV21GLProgram nV21GLProgram;
/*    */     YUV422GLProgram yUV422GLProgram;
/* 17 */     BaseProgram glProgram = null;
/*    */     
/* 19 */     switch (glPixelFormat) {
/*    */       case 4:
/* 21 */         glProgram = new BmpGLProgram();
/*    */         break;
/*    */       case 17301505:
/* 24 */         glProgram = new Mono8GLProgram(context);
/*    */         break;
/*    */       case 35127316:
/* 27 */         glProgram = new RGBGLProgram(context);
/*    */         break;
/*    */       case 5:
/* 30 */         glProgram = new NV21GLProgram();
/*    */         break;
/*    */       case 34603058:
/* 33 */         glProgram = new YUV422GLProgram(context);
/*    */         break;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     return glProgram;
/*    */   }
/*    */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\displayimage\GLProgramFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */