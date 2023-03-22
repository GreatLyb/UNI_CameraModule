/*     */ package com.example.type;
/*     */ 
/*     */

import android.content.Context;
import android.opengl.GLES20;

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
/*     */ 
/*     */ 
/*     */ public class Mono8GLProgram
/*     */   extends BaseProgram
/*     */   implements IGLProgram
/*     */ {
/*  21 */   Context context = null;
/*     */ 
/*     */   
/*     */   private int _program;
/*     */   
/*     */   private int _textureI;
/*     */   
/*     */   private int _tIindex;
/*     */   
/*  30 */   private int _positionHandle = -1, _coordHandle = -1;
/*  31 */   private int _yhandle = -1;
/*  32 */   private int _ytid = -1;
/*     */   
/*  34 */   private int _video_width = -1;
/*  35 */   private int _video_height = -1;
/*     */   
/*     */   private boolean isProgBuilt = false;
/*     */ 
/*     */   
/*     */   public Mono8GLProgram(Context context) {
/*  41 */     this.context = context;
/*  42 */     setup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setup() {
/*  49 */     this._textureI = 33984;
/*  50 */     this._tIindex = 0;
/*     */   }
/*     */   
/*     */   public boolean isProgramBuilt() {
/*  54 */     return this.isProgBuilt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildProgram() {
/*  60 */     String vertexShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.vertex_shader_mono8);
/*     */     
/*  62 */     String fragmentShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.fragment_shader_mono8);
/*     */ 
/*     */     
/*  65 */     int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
/*  66 */     int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
/*     */     
/*  68 */     if (this._program <= 0) {
/*  69 */       this._program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
/*     */     }
/*  71 */     Utils.LOGD("_program = " + this._program);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     this._positionHandle = GLES20.glGetAttribLocation(this._program, "vPosition");
/*  77 */     Utils.LOGD("_positionHandle = " + this._positionHandle);
/*  78 */     checkGlError("glGetAttribLocation vPosition");
/*  79 */     if (this._positionHandle == -1) {
/*  80 */       throw new RuntimeException("Could not get attribute location for vPosition");
/*     */     }
/*  82 */     this._coordHandle = GLES20.glGetAttribLocation(this._program, "a_texCoord");
/*  83 */     Utils.LOGD("_coordHandle = " + this._coordHandle);
/*  84 */     checkGlError("glGetAttribLocation a_texCoord");
/*  85 */     if (this._coordHandle == -1) {
/*  86 */       throw new RuntimeException("Could not get attribute location for a_texCoord");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     this._yhandle = GLES20.glGetUniformLocation(this._program, "tex_y");
/*  93 */     Utils.LOGD("_yhandle = " + this._yhandle);
/*  94 */     checkGlError("glGetUniformLocation tex_y");
/*  95 */     if (this._yhandle == -1) {
/*  96 */       throw new RuntimeException("Could not get uniform location for tex_y");
/*     */     }
/*     */ 
/*     */     
/* 100 */     this.isProgBuilt = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildTextures(Buffer y, int width, int height) {
/* 107 */     boolean videoSizeChanged = (width != this._video_width || height != this._video_height);
/* 108 */     if (videoSizeChanged) {
/* 109 */       this._video_width = width;
/* 110 */       this._video_height = height;
/* 111 */       Utils.LOGD("buildTextures videoSizeChanged: w=" + this._video_width + " h=" + this._video_height);
/*     */     } 
/*     */ 
/*     */     
/* 115 */     if (this._ytid < 0 || videoSizeChanged) {
/* 116 */       if (this._ytid >= 0) {
/* 117 */         Utils.LOGD("glDeleteTextures Y");
/* 118 */         GLES20.glDeleteTextures(1, new int[] { this._ytid }, 0);
/* 119 */         checkGlError("glDeleteTextures");
/*     */       } 
/*     */ 
/*     */       
/* 123 */       int[] textures = new int[1];
/*     */ 
/*     */       
/* 126 */       GLES20.glGenTextures(1, textures, 0);
/* 127 */       checkGlError("glGenTextures");
/* 128 */       this._ytid = textures[0];
/* 129 */       Utils.LOGD("glGenTextures Y = " + this._ytid);
/*     */     } 
/*     */     
/* 132 */     GLES20.glBindTexture(3553, this._ytid);
/* 133 */     checkGlError("glBindTexture");
/*     */     
/* 135 */     GLES20.glTexImage2D(3553, 0, 6409, this._video_width, this._video_height, 0, 6409, 5121, y);
/*     */     
/* 137 */     checkGlError("glTexImage2D");
/* 138 */     GLES20.glTexParameterf(3553, 10241, 9728.0F);
/* 139 */     GLES20.glTexParameterf(3553, 10240, 9729.0F);
/* 140 */     GLES20.glTexParameteri(3553, 10242, 33071);
/* 141 */     GLES20.glTexParameteri(3553, 10243, 33071);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawFrame() {
/* 152 */     GLES20.glUseProgram(this._program);
/* 153 */     checkGlError("glUseProgram");
/*     */     
/* 155 */     GLES20.glVertexAttribPointer(this._positionHandle, 2, 5126, false, 8, this.mVerticeBuffer);
/* 156 */     checkGlError("glVertexAttribPointer mPositionHandle");
/* 157 */     GLES20.glEnableVertexAttribArray(this._positionHandle);
/*     */     
/* 159 */     GLES20.glVertexAttribPointer(this._coordHandle, 2, 5126, false, 8, this.mCoordBuffer);
/* 160 */     checkGlError("glVertexAttribPointer maTextureHandle");
/* 161 */     GLES20.glEnableVertexAttribArray(this._coordHandle);
/*     */ 
/*     */     
/* 164 */     GLES20.glActiveTexture(this._textureI);
/* 165 */     GLES20.glBindTexture(3553, this._ytid);
/* 166 */     GLES20.glUniform1i(this._yhandle, this._tIindex);
/*     */     
/* 168 */     GLES20.glDrawArrays(5, 0, 4);
/* 169 */     GLES20.glFinish();
/*     */     
/* 171 */     GLES20.glDisableVertexAttribArray(this._positionHandle);
/* 172 */     GLES20.glDisableVertexAttribArray(this._coordHandle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createBuffers(float[] vert) {
/* 183 */     this.mVerticeBuffer = ByteBuffer.allocateDirect(vert.length * 4);
/* 184 */     this.mVerticeBuffer.order(ByteOrder.nativeOrder());
/* 185 */     this.mVerticeBuffer.asFloatBuffer().put(vert);
/* 186 */     this.mVerticeBuffer.position(0);
/*     */     
/* 188 */     if (this.mCoordBuffer == null) {
/* 189 */       this.mCoordBuffer = ByteBuffer.allocateDirect(this.coordVertices.length * 4);
/* 190 */       this.mCoordBuffer.order(ByteOrder.nativeOrder());
/* 191 */       this.mCoordBuffer.asFloatBuffer().put(this.coordVertices);
/* 192 */       this.mCoordBuffer.position(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float[] getSquareVertices() {
/* 197 */     return this.squareVertices;
/*     */   }
/*     */   
/*     */   public void checkGlError(String op) {
/*     */     int error;
/* 202 */     if ((error = GLES20.glGetError()) != 0) {
/* 203 */       Utils.LOGE("***** " + op + ": glError " + error);
/* 204 */       throw new RuntimeException(op + ": glError " + error);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\type\Mono8GLProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */