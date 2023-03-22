/*     */ package com.example.type;
/*     */ 
/*     */ import android.opengl.GLES30;
/*     */ import com.example.displayimage.IGLProgram;
/*     */ import com.example.displayimage.ShaderHelper;
/*     */ import com.example.displayimage.TextResourceReader1;
/*     */ import com.example.displayimage.Utils;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BmpGLProgram
/*     */   extends BaseProgram
/*     */   implements IGLProgram
/*     */ {
/*     */   private int mProgram;
/*     */   private int mTexture;
/*     */   private int mIindex;
/*  23 */   private int mPositionHandle = -1;
/*  24 */   private int mCoordHandle = -1;
/*  25 */   private int mUVHandle = -1;
/*  26 */   private int mUVid = -1;
/*     */ 
/*     */   
/*  29 */   private int mVideoWidth = -1;
/*  30 */   private int mVideoHeight = -1;
/*     */ 
/*     */   
/*     */   private boolean isProgBuilt = false;
/*     */ 
/*     */   
/*     */   public BmpGLProgram() {
/*  37 */     this.mTexture = 33984;
/*  38 */     this.mIindex = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProgramBuilt() {
/*  45 */     return this.isProgBuilt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildProgram() {
/*  51 */     if (this.mProgram <= 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  59 */       String vertexShaderSource = TextResourceReader1.readTextFileFromResource("attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nvarying vec2 textureCoordinate;\nvoid main()\n{\n     gl_Position = position;\n     textureCoordinate = inputTextureCoordinate.xy;\n}");
/*     */ 
/*     */       
/*  62 */       String fragmentShaderSource = TextResourceReader1.readTextFileFromResource("varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nvoid main()\n{\n    highp float r,g,b;\n    b = texture2D(inputImageTexture, textureCoordinate).r;\n    g = texture2D(inputImageTexture, textureCoordinate).g;\n    r = texture2D(inputImageTexture, textureCoordinate).b;\n    gl_FragColor = vec4(r,g,b,1.0);\n}");
/*     */ 
/*     */       
/*  65 */       int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
/*  66 */       int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
/*  67 */       this.mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  72 */     this.mPositionHandle = GLES30.glGetAttribLocation(this.mProgram, "position");
/*  73 */     Utils.LOGD("mPositionHandle = " + this.mPositionHandle);
/*  74 */     checkGlError("glGetAttribLocation vPosition");
/*  75 */     if (this.mPositionHandle == -1) {
/*  76 */       throw new RuntimeException("Could not get attribute location for vPosition");
/*     */     }
/*  78 */     this.mCoordHandle = GLES30.glGetAttribLocation(this.mProgram, "inputTextureCoordinate");
/*  79 */     Utils.LOGD("mCoordHandle = " + this.mCoordHandle);
/*  80 */     checkGlError("glGetAttribLocation inputTextureCoordinate");
/*  81 */     if (this.mCoordHandle == -1) {
/*  82 */       throw new RuntimeException("Could not get attribute location for inputTextureCoordinate");
/*     */     }
/*     */     
/*  85 */     this.mUVHandle = GLES30.glGetUniformLocation(this.mProgram, "inputImageTexture");
/*  86 */     Utils.LOGD("_uhandle = " + this.mUVHandle);
/*  87 */     checkGlError("glGetUniformLocation inputTextureCoordinate");
/*  88 */     if (this.mUVHandle == -1) {
/*  89 */       throw new RuntimeException("Could not get uniform location for inputImageTexture");
/*     */     }
/*     */     
/*  92 */     this.isProgBuilt = true;
/*     */   }
/*     */ 
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
/*     */   public void drawFrame() {
/* 136 */     GLES30.glUseProgram(this.mProgram);
/* 137 */     checkGlError("glUseProgram");
/* 138 */     GLES30.glVertexAttribPointer(this.mPositionHandle, 2, 5126, false, 0, this.mVerticeBuffer);
/* 139 */     checkGlError("glVertexAttribPointer mPositionHandle");
/* 140 */     GLES30.glEnableVertexAttribArray(this.mPositionHandle);
/* 141 */     GLES30.glVertexAttribPointer(this.mCoordHandle, 2, 5126, false, 0, this.mCoordBuffer);
/* 142 */     checkGlError("glVertexAttribPointer maTextureHandle");
/* 143 */     GLES30.glEnableVertexAttribArray(this.mCoordHandle);
/*     */     
/* 145 */     GLES30.glActiveTexture(this.mTexture);
/* 146 */     GLES30.glBindTexture(3553, this.mUVid);
/* 147 */     GLES30.glUniform1i(this.mUVHandle, this.mIindex);
/*     */     
/* 149 */     GLES30.glDrawArrays(5, 0, 4);
/* 150 */     GLES30.glFlush();
/*     */     
/* 152 */     GLES30.glDisableVertexAttribArray(this.mPositionHandle);
/* 153 */     GLES30.glDisableVertexAttribArray(this.mCoordHandle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createBuffers(float[] vert) {
/* 158 */     this.mVerticeBuffer = ByteBuffer.allocateDirect(vert.length * 4);
/* 159 */     this.mVerticeBuffer.order(ByteOrder.nativeOrder());
/* 160 */     this.mVerticeBuffer.asFloatBuffer().put(vert);
/*     */     
/* 162 */     this.mVerticeBuffer.position(0);
/*     */     
/* 164 */     if (this.mCoordBuffer == null) {
/* 165 */       this.mCoordBuffer = ByteBuffer.allocateDirect(this.coordVertices.length * 4);
/* 166 */       this.mCoordBuffer.order(ByteOrder.nativeOrder());
/* 167 */       this.mCoordBuffer.asFloatBuffer().put(this.coordVertices);
/* 168 */       this.mCoordBuffer.position(0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getSquareVertices() {
/* 174 */     return this.squareVertices;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkGlError(String op) {
/*     */     int error;
/* 180 */     if ((error = GLES30.glGetError()) != 0) {
/* 181 */       Utils.LOGE("***** " + op + ": glError " + error);
/* 182 */       throw new RuntimeException(op + ": glError " + error);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\type\BmpGLProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */