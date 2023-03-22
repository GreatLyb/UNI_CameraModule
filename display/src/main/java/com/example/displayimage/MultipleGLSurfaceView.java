/*    */ package com.example.displayimage;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.opengl.GLSurfaceView;
/*    */ import android.util.AttributeSet;
/*    */ import android.view.ViewTreeObserver;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MultipleGLSurfaceView
/*    */   extends GLSurfaceView
/*    */ {
/*    */   private GLFrameRenderer glFrameRenderer;
/* 15 */   private int imageWidth = 0;
/* 16 */   private int imageHeight = 0;
/* 17 */   private int viewWidth = 0;
/* 18 */   private int viewHeight = 0;
/*    */   
/*    */   private Context context;
/*    */   private IGlobalLayout iGlobalLayout;
/*    */   
/*    */   public MultipleGLSurfaceView(Context context, IGlobalLayout iGlobalLayout) {
/* 24 */     super(context);
/* 25 */     this.context = context;
/* 26 */     initView(context);
/* 27 */     this.iGlobalLayout = iGlobalLayout;
/*    */   }
/*    */   
/*    */   public MultipleGLSurfaceView(Context context, AttributeSet attrs) {
/* 31 */     super(context, attrs);
/* 32 */     this.context = context;
/* 33 */     initView(context);
/*    */   }
/*    */   
/*    */   private void initView(Context context) {
/* 37 */     ViewTreeObserver viewTreeObserver = getViewTreeObserver();
/* 38 */     viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
/*    */         {
/*    */           public void onGlobalLayout()
/*    */           {
/* 42 */             MultipleGLSurfaceView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
/* 43 */             MultipleGLSurfaceView.this.viewWidth = MultipleGLSurfaceView.this.getWidth();
/* 44 */             MultipleGLSurfaceView.this.viewHeight = MultipleGLSurfaceView.this.getHeight();
/* 45 */             MultipleGLSurfaceView.this.initGLFrameRenderer();
/*    */             
/* 47 */             if (MultipleGLSurfaceView.this.iGlobalLayout != null) {
/* 48 */               MultipleGLSurfaceView.this.iGlobalLayout.lodfinish();
/*    */             }
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   private void initGLFrameRenderer() {
/* 55 */     setEGLContextClientVersion(2);
/* 56 */     this.glFrameRenderer = new GLFrameRenderer(this.context, this, this.viewWidth, this.viewHeight);
/* 57 */     setRenderer(this.glFrameRenderer);
/*    */     
/* 59 */     setRenderMode(0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSize(int w, int h) {
/* 65 */     this.imageWidth = w;
/* 66 */     this.imageHeight = h;
/* 67 */     this.glFrameRenderer.update(this.imageWidth, this.imageHeight);
/*    */   }
/*    */   
/*    */   public void pixelFormat(int pixelFormat) {
/* 71 */     this.glFrameRenderer.setPixelFormat(pixelFormat);
/*    */   }
/*    */   
/*    */   public void updateImage(byte[] datas) {
/* 75 */     this.glFrameRenderer.update(datas);
/*    */   }
/*    */   
/*    */   public void setDisPlayRect(float[] squareVertices, float[] coordVertices) throws Exception {
/* 79 */     if (squareVertices.length != 8 || coordVertices.length != 8) {
/* 80 */       throw new Exception("参数越界");
/*    */     }
/* 82 */     this.glFrameRenderer.setSquareVertices(squareVertices, coordVertices);
/*    */   }
/*    */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\displayimage\MultipleGLSurfaceView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */