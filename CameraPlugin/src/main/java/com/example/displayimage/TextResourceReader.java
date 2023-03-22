/*    */ package com.example.displayimage;
/*    */ 
/*    */ import android.content.Context;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TextResourceReader
/*    */ {
/*    */   public static String readTextFileFromResource(Context context, int resourceId) {
/* 19 */     StringBuilder body = new StringBuilder();
/*    */     
/*    */     try {
/* 22 */       InputStream inputStream = context.getResources().openRawResource(resourceId);
/* 23 */       InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
/* 24 */       BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
/*    */       String nextLine;
/* 26 */       while ((nextLine = bufferedReader.readLine()) != null) {
/*    */         
/* 28 */         body.append(nextLine);
/* 29 */         body.append('\n');
/*    */       } 
/* 31 */     } catch (IOException e) {
/* 32 */       e.printStackTrace();
/*    */     } 
/* 34 */     return body.toString();
/*    */   }
/*    */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\displayimage\TextResourceReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */