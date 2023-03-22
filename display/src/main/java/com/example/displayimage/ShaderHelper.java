/*    */ package com.example.displayimage;
/*    */ 
/*    */ import android.opengl.GLES20;
/*    */ import android.opengl.GLES30;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ShaderHelper
/*    */ {
/*    */   public static int compileVertexShader(String shaderCode) {
/* 20 */     return compileShader(35633, shaderCode);
/*    */   }
/*    */   
/*    */   public static int compileFragmentShader(String shaderCode) {
/* 24 */     return compileShader(35632, shaderCode);
/*    */   }
/*    */ 
/*    */   
/*    */   private static int compileShader(int type, String shaderCode) {
/* 29 */     int shaderObjectId = GLES20.glCreateShader(type);
/* 30 */     if (shaderObjectId == 0) {
/* 31 */       return 0;
/*    */     }
/* 33 */     GLES20.glShaderSource(shaderObjectId, shaderCode);
/* 34 */     GLES20.glCompileShader(shaderObjectId);
/* 35 */     int[] compileStatus = new int[1];
/* 36 */     GLES20.glGetShaderiv(shaderObjectId, 35713, compileStatus, 0);
/* 37 */     if (compileStatus[0] == 0) {
/* 38 */       GLES20.glDeleteShader(shaderObjectId);
/* 39 */       return 0;
/*    */     } 
/* 41 */     return shaderObjectId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
/* 47 */     int program = GLES20.glCreateProgram();
/* 48 */     if (program != 0) {
/* 49 */       GLES20.glAttachShader(program, vertexShaderId);
/* 50 */       GLES20.glAttachShader(program, fragmentShaderId);
/* 51 */       GLES20.glLinkProgram(program);
/* 52 */       int[] linkStatus = new int[1];
/* 53 */       GLES30.glGetProgramiv(program, 35714, linkStatus, 0);
/* 54 */       if (linkStatus[0] != 1) {
/* 55 */         Utils.LOGE("Could not link program: ");
/* 56 */         Utils.LOGE(GLES30.glGetProgramInfoLog(program));
/* 57 */         GLES30.glDeleteProgram(program);
/* 58 */         program = 0;
/*    */       } 
/*    */     } 
/* 61 */     return program;
/*    */   }
/*    */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\displayimage\ShaderHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */