/*     */ package com.example.displayimage;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.opengl.GLES20;
/*     */ import android.opengl.GLSurfaceView;
/*     */ import com.example.type.BaseProgram;
/*     */ import java.nio.ByteBuffer;
/*     */ import javax.microedition.khronos.egl.EGLConfig;
/*     */ import javax.microedition.khronos.opengles.GL10;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GLFrameRenderer
/*     */   implements GLSurfaceView.Renderer
/*     */ {
/*  18 */   Context context = null;
/*  19 */   private BaseProgram prog = null; private GLSurfaceView mTargetSurface; private int mScreenWidth; private int mScreenHeight; private int mVideoWidth; private int mVideoHeight; private ByteBuffer y; private int format;
/*     */   float f1;
/*     */   float f2;
/*     */   float f3;
/*     */   int x;
/*     */   int width;
/*     */   int height;
/*     */   
/*     */   public void setPixelFormat(int format) {
/*  28 */     this.format = format;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  38 */     this.prog = GLProgramFactory.createGLProgram(format, this.context);
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
/*     */   
/*     */   public void setSquareVertices(float[] square, float[] coord) {
/*  51 */     this.prog.setSquareVertices(square, coord);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSurfaceCreated(GL10 gl, EGLConfig config) {
/*  61 */     Utils.LOGD("GLFrameRenderer :: onSurfaceCreated");
/*  62 */     if (this.prog != null && 
/*  63 */       !this.prog.isProgramBuilt()) {
/*  64 */       this.prog.buildProgram();
/*  65 */       Utils.LOGD("GLFrameRenderer :: buildProgram done");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSurfaceChanged(GL10 gl, int width, int height) {
/*  72 */     Utils.LOGD("GLFrameRenderer :: onSurfaceChanged");
/*  73 */     GLES20.glViewport(0, 0, width, height);
/*     */   }
/*     */   
/*     */   public GLFrameRenderer(Context context, GLSurfaceView surface, int width, int height) {
/*  77 */     this.f1 = 1.0F;
/*  78 */     this.f2 = 1.0F;
/*  79 */     this.f3 = 1.0F;
/*  80 */     this.x = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     this.width = 0;
/* 104 */     this.height = 0; this.context = context; this.mTargetSurface = surface; this.mScreenWidth = width; this.mScreenHeight = height; } public GLFrameRenderer(Context context, int width, int height) { this.f1 = 1.0F; this.f2 = 1.0F; this.f3 = 1.0F; this.x = 0; this.width = 0; this.height = 0;
/*     */     this.context = context;
/*     */     this.mScreenWidth = width;
/* 107 */     this.mScreenHeight = height; } public void update(int w, int h) { this.width = w;
/* 108 */     this.height = h;
/* 109 */     Utils.LOGD("INIT E");
/* 110 */     if (w > 0 && h > 0) {
/*     */       
/* 112 */       if (this.mScreenWidth > 0 && this.mScreenHeight > 0) {
/* 113 */         float f1 = 1.0F * this.mScreenHeight / this.mScreenWidth;
/* 114 */         float f2 = 1.0F * h / w;
/* 115 */         if (f1 == f2) {
/* 116 */           this.prog.createBuffers(this.prog.getSquareVertices());
/*     */         }
/* 118 */         else if (f1 < f2) {
/* 119 */           float widScale = f1 / f2;
/* 120 */           this.prog.createBuffers(new float[] { -widScale, -1.0F, widScale, -1.0F, -widScale, 1.0F, widScale, 1.0F });
/*     */         } else {
/*     */           
/* 123 */           float heightScale = f2 / f1;
/* 124 */           this.prog.createBuffers(new float[] { -1.0F, -heightScale, 1.0F, -heightScale, -1.0F, heightScale, 1.0F, heightScale });
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 129 */       if (w != this.mVideoWidth && h != this.mVideoHeight) {
/* 130 */         this.mVideoWidth = w;
/* 131 */         this.mVideoHeight = h;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     Utils.LOGD("INIT X"); }
/*     */   public void onDrawFrame(GL10 gl) { synchronized (this) {
/*     */       if (this.y != null) {
/*     */         this.y.position(0); GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
/*     */         this.prog.buildTextures(this.y, this.mVideoWidth, this.mVideoHeight);
/*     */         this.prog.drawFrame();
/*     */       } 
/* 160 */     }  } public void update(byte[] ydata) { this.y = ByteBuffer.wrap(ydata, 0, ydata.length);
/* 161 */     synchronized (this) {
/*     */ 
/*     */ 
/*     */       
/* 165 */       this.mTargetSurface.requestRender();
/*     */     }  }
/*     */ 
/*     */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\displayimage\GLFrameRenderer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */