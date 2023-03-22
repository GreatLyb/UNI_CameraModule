/*    */ package com.example.displayimage;
/*    */ 
/*    */ import android.content.res.Resources;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TextResourceReader1
/*    */ {
/*    */   public static String readTextFileFromResource(String resource) {
/* 15 */     StringBuilder body = new StringBuilder();
/*    */     try {
/* 17 */       InputStream inputStream = new ByteArrayInputStream(resource.getBytes());
/* 18 */       InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
/* 19 */       BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
/*    */       String nextLine;
/* 21 */       while ((nextLine = bufferedReader.readLine()) != null) {
/* 22 */         body.append(nextLine);
/* 23 */         body.append('\n');
/*    */       } 
/* 25 */       inputStream.close();
/* 26 */       inputStreamReader.close();
/*    */     }
/* 28 */     catch (IOException e) {
/* 29 */       throw new RuntimeException("Could not open resource:" + e);
/* 30 */     } catch (Resources.NotFoundException nfe) {
/* 31 */       throw new RuntimeException("Resource not found:" + nfe);
/*    */     } 
/* 33 */     return body.toString();
/*    */   }
/*    */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\displayimage\TextResourceReader1.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */