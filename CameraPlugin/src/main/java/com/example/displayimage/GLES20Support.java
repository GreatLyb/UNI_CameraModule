/*    */ package com.example.displayimage;
/*    */ 
/*    */ import android.app.ActivityManager;
/*    */ import android.content.Context;
/*    */ import android.content.pm.ConfigurationInfo;
/*    */ 
/*    */ public class GLES20Support
/*    */ {
/*    */   public static boolean detectOpenGLES20(Context context) {
/* 10 */     ActivityManager am = (ActivityManager)context.getSystemService("activity");
/* 11 */     ConfigurationInfo info = am.getDeviceConfigurationInfo();
/* 12 */     return (info.reqGlEsVersion >= 131072);
/*    */   }
/*    */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\displayimage\GLES20Support.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */