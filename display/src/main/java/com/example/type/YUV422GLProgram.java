/*     */ package com.example.type;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.opengl.GLES20;
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
/*     */ public class YUV422GLProgram
/*     */   extends BaseProgram
/*     */   implements IGLProgram
/*     */ {
/*     */   private int mProgram;
/*     */   private int mTexture;
/*     */   private int mIindex;
/*  24 */   private int mPositionHandle = -1;
/*  25 */   private int mCoordHandle = -1;
/*  26 */   private int mUVHandle = -1;
/*  27 */   private int mUVid = -1;
/*     */ 
/*     */   
/*  30 */   private int mVideoWidth = -1;
/*  31 */   private int mVideoHeight = -1;
/*     */   
/*     */   private boolean isProgBuilt = false;
/*  34 */   private int mPixelBuffer = 0;
/*     */   private Context context;
/*     */   
/*     */   public YUV422GLProgram(Context context) {
/*  38 */     this.context = context;
/*  39 */     this.mTexture = 33984;
/*  40 */     this.mIindex = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProgramBuilt() {
/*  46 */     return this.isProgBuilt;
/*     */   }
/*     */ 
/*     */   
/*     */   public void buildProgram() {
/*  51 */     if (this.mProgram <= 0) {
/*     */ 
/*     */       
/*  54 */       String vertexShaderSource = TextResourceReader1.readTextFileFromResource("attribute vec4 vPosition;\nattribute vec2 a_texCoord;\nvarying lowp vec2 tc;\nvoid main() {\n    gl_Position = vPosition;\n    tc = a_texCoord;\n}");
/*     */       
/*  56 */       String fragmentShaderSource = TextResourceReader1.readTextFileFromResource("varying lowp vec2 tc;\nuniform sampler2D tex_yuv;\nbool nFlag = false;\nvoid main(void) {\n    mediump float r,g,b,y,u,v;\n    if (nFlag == false){\n        y=texture2D(tex_yuv, tc).r ;\n        u=texture2D(tex_yuv, tc).g ;\n        v=texture2D(tex_yuv, tc).a ;\n    } else {\n        y=texture2D(tex_yuv, tc).b ;\n        u=texture2D(tex_yuv, tc).g ;\n        v=texture2D(tex_yuv, tc).a ;\n    }\n    nFlag = !nFlag;\n    u=u-0.5;\n    v=v-0.5;\n    y=y*1.1;\n\n    r=y+1.370705*v ;\n    g=y-0.337633*u-0.698001*v;\n    b=y+1.732446*u;\n    gl_FragColor=vec4(r,g,b,1.0);\n\n}\n");
/*     */ 
/*     */       
/*  59 */       int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
/*  60 */       int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
/*  61 */       this.mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);
/*     */     } 
/*     */ 
/*     */     
/*  65 */     this.mPositionHandle = GLES20.glGetAttribLocation(this.mProgram, "vPosition");
/*  66 */     Utils.LOGD("mPositionHandle = " + this.mPositionHandle);
/*  67 */     checkGlError("glGetAttribLocation vPosition");
/*  68 */     if (this.mPositionHandle == -1) {
/*  69 */       throw new RuntimeException("Could not get attribute location for vPosition");
/*     */     }
/*  71 */     this.mCoordHandle = GLES20.glGetAttribLocation(this.mProgram, "a_texCoord");
/*  72 */     Utils.LOGD("mCoordHandle = " + this.mCoordHandle);
/*  73 */     checkGlError("glGetAttribLocation a_texCoord");
/*  74 */     if (this.mCoordHandle == -1) {
/*  75 */       throw new RuntimeException("Could not get attribute location for a_texCoord");
/*     */     }
/*     */     
/*  78 */     this.mUVHandle = GLES20.glGetUniformLocation(this.mProgram, "tex_yuv");
/*  79 */     Utils.LOGD("_uhandle = " + this.mUVHandle);
/*  80 */     checkGlError("glGetUniformLocation tex_u");
/*  81 */     if (this.mUVHandle == -1) {
/*  82 */       throw new RuntimeException("Could not get uniform location for tex_uv");
/*     */     }
/*     */     
/*  85 */     this.isProgBuilt = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void buildTextures(Buffer buffer, int width, int height) {
/*  90 */     boolean videoSizeChanged = (width != this.mVideoWidth || height != this.mVideoHeight);
/*  91 */     if (videoSizeChanged) {
/*  92 */       this.mVideoWidth = width;
/*  93 */       this.mVideoHeight = height;
/*  94 */       Utils.LOGD("buildTextures videoSizeChanged: w=" + this.mVideoWidth + " h=" + this.mVideoHeight);
/*     */     } 
/*     */     
/*  97 */     buffer.position(0);
/*     */     
/*  99 */     if (this.mUVid < 0 || videoSizeChanged) {
/* 100 */       if (this.mUVid >= 0) {
/* 101 */         Utils.LOGD("glDeleteTextures U");
/* 102 */         GLES20.glDeleteTextures(1, new int[] { this.mUVid }, 0);
/* 103 */         checkGlError("glDeleteTextures");
/*     */       } 
/* 105 */       int[] textures = new int[1];
/* 106 */       GLES20.glGenTextures(1, textures, 0);
/* 107 */       checkGlError("glGenTextures");
/* 108 */       this.mUVid = textures[0];
/* 109 */       Utils.LOGD("glGenTextures U = " + this.mUVid);
/*     */       
/* 111 */       GLES20.glBindTexture(3553, this.mUVid);
/* 112 */       GLES20.glTexImage2D(3553, 0, 6408, this.mVideoWidth / 2, this.mVideoHeight, 0, 6408, 5121, buffer);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 117 */     GLES20.glBindTexture(3553, this.mUVid);
/* 118 */     GLES20.glTexSubImage2D(3553, 0, 0, 0, this.mVideoWidth / 2, this.mVideoHeight, 6408, 5121, buffer);
/*     */     
/* 120 */     GLES20.glTexParameterf(3553, 10241, 9728.0F);
/* 121 */     GLES20.glTexParameterf(3553, 10240, 9729.0F);
/* 122 */     GLES20.glTexParameteri(3553, 10242, 33071);
/* 123 */     GLES20.glTexParameteri(3553, 10243, 33071);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawFrame() {
/* 128 */     GLES20.glUseProgram(this.mProgram);
/* 129 */     checkGlError("glUseProgram");
/* 130 */     GLES20.glVertexAttribPointer(this.mPositionHandle, 2, 5126, false, 8, this.mVerticeBuffer);
/* 131 */     checkGlError("glVertexAttribPointer mPositionHandle");
/* 132 */     GLES20.glEnableVertexAttribArray(this.mPositionHandle);
/* 133 */     GLES20.glVertexAttribPointer(this.mCoordHandle, 2, 5126, false, 8, this.mCoordBuffer);
/* 134 */     checkGlError("glVertexAttribPointer maTextureHandle");
/* 135 */     GLES20.glEnableVertexAttribArray(this.mCoordHandle);
/*     */     
/* 137 */     GLES20.glActiveTexture(this.mTexture);
/* 138 */     GLES20.glBindTexture(3553, this.mUVid);
/* 139 */     GLES20.glUniform1i(this.mUVHandle, this.mIindex);
/*     */     
/* 141 */     GLES20.glDrawArrays(5, 0, 4);
/* 142 */     GLES20.glFlush();
/*     */     
/* 144 */     GLES20.glDisableVertexAttribArray(this.mPositionHandle);
/* 145 */     GLES20.glDisableVertexAttribArray(this.mCoordHandle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createBuffers(float[] vert) {
/* 150 */     this.mVerticeBuffer = ByteBuffer.allocateDirect(vert.length * 4);
/* 151 */     this.mVerticeBuffer.order(ByteOrder.nativeOrder());
/* 152 */     this.mVerticeBuffer.asFloatBuffer().put(vert);
/* 153 */     this.mVerticeBuffer.position(0);
/*     */     
/* 155 */     if (this.mCoordBuffer == null) {
/* 156 */       this.mCoordBuffer = ByteBuffer.allocateDirect(this.coordVertices.length * 4);
/* 157 */       this.mCoordBuffer.order(ByteOrder.nativeOrder());
/* 158 */       this.mCoordBuffer.asFloatBuffer().put(this.coordVertices);
/* 159 */       this.mCoordBuffer.position(0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getSquareVertices() {
/* 165 */     return this.squareVertices;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkGlError(String op) {
/*     */     int error;
/* 171 */     if ((error = GLES20.glGetError()) != 0) {
/* 172 */       Utils.LOGE("***** " + op + ": glError " + error);
/* 173 */       throw new RuntimeException(op + ": glError " + error);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\type\YUV422GLProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */