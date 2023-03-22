/*     */ package com.example.type;
/*     */ 
/*     */

import android.content.Context;
import android.opengl.GLES30;

import com.example.displayimage.IGLProgram;
import com.example.displayimage.ShaderHelper;
import com.example.displayimage.TextResourceReader;
import com.example.displayimage.Utils;
import com.uni.cameraplugin.R;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ public class RGBGLProgram
/*     */   extends BaseProgram
/*     */   implements IGLProgram
/*     */ {
/*  19 */   Context context = null;
/*     */   
/*     */   private int mProgram;
/*     */   
/*     */   private int mTexture;
/*     */   
/*     */   private int mIindex;
/*  26 */   private int mPositionHandle = -1;
/*  27 */   private int mCoordHandle = -1;
/*  28 */   private int mUVHandle = -1;
/*  29 */   private int mUVid = -1;
/*     */ 
/*     */   
/*  32 */   private int mVideoWidth = -1;
/*  33 */   private int mVideoHeight = -1;
/*     */ 
/*     */   
/*     */   private boolean isProgBuilt = false;
/*     */ 
/*     */   
/*     */   public RGBGLProgram(Context context) {
/*  40 */     this.context = context;
/*  41 */     this.mTexture = 33984;
/*  42 */     this.mIindex = 0;
/*     */   }
/*     */   
/*     */   public boolean isProgramBuilt() {
/*  46 */     return this.isProgBuilt;
/*     */   }
/*     */   
/*     */   public void buildProgram() {
/*  50 */     if (this.mProgram <= 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  60 */       String vertexShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.vertex_shader_rgb);
/*     */       
/*  62 */       String fragmentShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.fragment_shader_rgb);
/*     */ 
/*     */ 
/*     */       
/*  66 */       int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
/*  67 */       int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
/*  68 */       this.mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  73 */     this.mPositionHandle = GLES30.glGetAttribLocation(this.mProgram, "position");
/*  74 */     Utils.LOGD("mPositionHandle = " + this.mPositionHandle);
/*  75 */     checkGlError("glGetAttribLocation vPosition");
/*  76 */     if (this.mPositionHandle == -1) {
/*  77 */       throw new RuntimeException("Could not get attribute location for vPosition");
/*     */     }
/*  79 */     this.mCoordHandle = GLES30.glGetAttribLocation(this.mProgram, "inputTextureCoordinate");
/*  80 */     Utils.LOGD("mCoordHandle = " + this.mCoordHandle);
/*  81 */     checkGlError("glGetAttribLocation inputTextureCoordinate");
/*  82 */     if (this.mCoordHandle == -1) {
/*  83 */       throw new RuntimeException("Could not get attribute location for inputTextureCoordinate");
/*     */     }
/*     */     
/*  86 */     this.mUVHandle = GLES30.glGetUniformLocation(this.mProgram, "inputImageTexture");
/*  87 */     Utils.LOGD("_uhandle = " + this.mUVHandle);
/*  88 */     checkGlError("glGetUniformLocation inputTextureCoordinate");
/*  89 */     if (this.mUVHandle == -1) {
/*  90 */       throw new RuntimeException("Could not get uniform location for inputImageTexture");
/*     */     }
/*     */     
/*  93 */     this.isProgBuilt = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void buildTextures(Buffer buffer, int width, int height) {
/*  98 */     boolean videoSizeChanged = (width != this.mVideoWidth || height != this.mVideoHeight);
/*  99 */     if (videoSizeChanged) {
/*     */       
/* 101 */       this.mVideoWidth = width;
/* 102 */       this.mVideoHeight = height;
/* 103 */       Utils.LOGD("buildTextures videoSizeChanged: w=" + this.mVideoWidth + " h=" + this.mVideoHeight);
/*     */     } 
/*     */     
/* 106 */     buffer.position(0);
/*     */     
/* 108 */     if (this.mUVid < 0 || videoSizeChanged) {
/*     */       
/* 110 */       if (this.mUVid >= 0) {
/* 111 */         Utils.LOGD("glDeleteTextures U");
/* 112 */         GLES30.glDeleteTextures(1, new int[] { this.mUVid }, 0);
/* 113 */         checkGlError("glDeleteTextures");
/*     */       } 
/* 115 */       int[] textures = new int[1];
/* 116 */       GLES30.glGenTextures(1, textures, 0);
/* 117 */       checkGlError("glGenTextures");
/* 118 */       this.mUVid = textures[0];
/* 119 */       Utils.LOGD("glGenTextures U = " + this.mUVid);
/*     */       
/* 121 */       GLES30.glBindTexture(3553, this.mUVid);
/* 122 */       GLES30.glTexImage2D(3553, 0, 6407, this.mVideoWidth, this.mVideoHeight, 0, 6407, 5121, buffer);
/*     */     } 
/*     */     
/* 125 */     GLES30.glBindTexture(3553, this.mUVid);
/* 126 */     GLES30.glTexSubImage2D(3553, 0, 0, 0, this.mVideoWidth, this.mVideoHeight, 6407, 5121, buffer);
/*     */     
/* 128 */     GLES30.glTexParameterf(3553, 10241, 9728.0F);
/* 129 */     GLES30.glTexParameterf(3553, 10240, 9729.0F);
/* 130 */     GLES30.glTexParameteri(3553, 10242, 33071);
/* 131 */     GLES30.glTexParameteri(3553, 10243, 33071);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawFrame() {
/* 143 */     GLES30.glUseProgram(this.mProgram);
/* 144 */     checkGlError("glUseProgram");
/* 145 */     GLES30.glVertexAttribPointer(this.mPositionHandle, 2, 5126, false, 0, this.mVerticeBuffer);
/* 146 */     checkGlError("glVertexAttribPointer mPositionHandle");
/* 147 */     GLES30.glEnableVertexAttribArray(this.mPositionHandle);
/* 148 */     GLES30.glVertexAttribPointer(this.mCoordHandle, 2, 5126, false, 0, this.mCoordBuffer);
/* 149 */     checkGlError("glVertexAttribPointer maTextureHandle");
/* 150 */     GLES30.glEnableVertexAttribArray(this.mCoordHandle);
/*     */     
/* 152 */     GLES30.glActiveTexture(this.mTexture);
/* 153 */     GLES30.glBindTexture(3553, this.mUVid);
/* 154 */     GLES30.glUniform1i(this.mUVHandle, this.mIindex);
/*     */     
/* 156 */     GLES30.glDrawArrays(5, 0, 4);
/* 157 */     GLES30.glFlush();
/*     */     
/* 159 */     GLES30.glDisableVertexAttribArray(this.mPositionHandle);
/* 160 */     GLES30.glDisableVertexAttribArray(this.mCoordHandle);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void createBuffers(float[] vert) {
/* 166 */     this.mVerticeBuffer = ByteBuffer.allocateDirect(vert.length * 4);
/* 167 */     this.mVerticeBuffer.order(ByteOrder.nativeOrder());
/* 168 */     this.mVerticeBuffer.asFloatBuffer().put(vert);
/*     */     
/* 170 */     this.mVerticeBuffer.position(0);
/*     */     
/* 172 */     if (this.mCoordBuffer == null) {
/* 173 */       this.mCoordBuffer = ByteBuffer.allocateDirect(this.coordVertices.length * 4);
/* 174 */       this.mCoordBuffer.order(ByteOrder.nativeOrder());
/* 175 */       this.mCoordBuffer.asFloatBuffer().put(this.coordVertices);
/* 176 */       this.mCoordBuffer.position(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float[] getSquareVertices() {
/* 181 */     return this.squareVertices;
/*     */   }
/*     */   
/*     */   public void checkGlError(String op) {
/*     */     int error;
/* 186 */     if ((error = GLES30.glGetError()) != 0) {
/* 187 */       Utils.LOGE("***** " + op + ": glError " + error);
/* 188 */       throw new RuntimeException(op + ": glError " + error);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\type\RGBGLProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */