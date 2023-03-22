/*     */ package com.example.type;
/*     */ 
/*     */

import android.opengl.GLES30;
import android.util.Log;

import com.example.displayimage.IGLProgram;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NV21GLProgram
/*     */   extends BaseProgram
/*     */   implements IGLProgram
/*     */ {
/*     */   private int _program;
/*  23 */   private int _tIindex = 0;
/*  24 */   private int _tIIindex = 1;
/*  25 */   private int _tIIIindex = 2;
/*     */ 
/*     */   
/*  28 */   private int _positionHandle = -1, _coordHandle = -1;
/*  29 */   private int _yhandle = -1, _uvhandle = -1;
/*  30 */   private int _ytid = -1, _uvtid = -1;
/*     */ 
/*     */   
/*  33 */   private int _video_width = -1;
/*  34 */   private int _video_height = -1;
/*     */   
/*     */   private boolean isProgBuilt = false;
/*  37 */   private int m_hPixelBuffer = 0;
/*  38 */   private int m_hUvVectorBuffer = 0;
/*  39 */   private ByteBuffer uv = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProgramBuilt() {
/*  50 */     return this.isProgBuilt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildProgram() {
/*  56 */     if (this._program <= 0) {
/*  57 */       this._program = createProgram("attribute vec4 vPosition;\nattribute vec2 a_texCoord;\nvarying lowp vec2 tc;\nvoid main() {\ngl_Position = vPosition;\ntc = a_texCoord;\n}\n", "varying lowp vec2 tc;\nuniform sampler2D tex_y; \nuniform sampler2D tex_uv; \nvoid main(void) {\t \nmediump float r,g,b,y,u,v; \n  y=texture2D(tex_y, tc).r ; \n  v=texture2D(tex_uv, tc).r ; \n  u=texture2D(tex_uv, tc).a ; \n  u=u-0.5;\t\n  v=v-0.5;\t \n  r=y+1.370705*v ; \n  g=y-0.337633*u-0.698001*v;\t \n  b=y+1.732446*u;\t \n  gl_FragColor=vec4(r,g,b,1.0); \n} \n");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     this._positionHandle = GLES30.glGetAttribLocation(this._program, "vPosition");
/*  64 */     checkGlError("glGetAttribLocation vPosition");
/*  65 */     if (this._positionHandle == -1) {
/*  66 */       throw new RuntimeException("Could not get attribute location for vPosition");
/*     */     }
/*  68 */     this._coordHandle = GLES30.glGetAttribLocation(this._program, "a_texCoord");
/*  69 */     checkGlError("glGetAttribLocation a_texCoord");
/*  70 */     if (this._coordHandle == -1) {
/*  71 */       throw new RuntimeException("Could not get attribute location for a_texCoord");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     this._yhandle = GLES30.glGetUniformLocation(this._program, "tex_y");
/*  78 */     checkGlError("glGetUniformLocation tex_y");
/*  79 */     if (this._yhandle == -1) {
/*  80 */       throw new RuntimeException("Could not get uniform location for tex_y");
/*     */     }
/*  82 */     this._uvhandle = GLES30.glGetUniformLocation(this._program, "tex_uv");
/*  83 */     checkGlError("glGetUniformLocation tex_u");
/*  84 */     if (this._uvhandle == -1) {
/*  85 */       throw new RuntimeException("Could not get uniform location for tex_uv");
/*     */     }
/*     */     
/*  88 */     this.isProgBuilt = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void buildTextures(Buffer buffer, int width, int height) {
/*  93 */     boolean videoSizeChanged = (width != this._video_width || height != this._video_height);
/*  94 */     if (videoSizeChanged) {
/*  95 */       this._video_width = width;
/*  96 */       this._video_height = height;
/*     */     } 
/*     */     
/*  99 */     buffer.position(0);
/*     */     
/* 101 */     if (this._ytid < 0 || videoSizeChanged) {
/* 102 */       if (this._ytid >= 0) {
/* 103 */         GLES30.glDeleteTextures(1, new int[] { this._ytid }, 0);
/* 104 */         checkGlError("glDeleteTextures");
/*     */       } 
/* 106 */       int[] textures = new int[1];
/* 107 */       GLES30.glGenTextures(1, textures, 0);
/* 108 */       checkGlError("glGenTextures");
/* 109 */       this._ytid = textures[0];
/*     */       
/* 111 */       GLES30.glBindTexture(3553, this._ytid);
/* 112 */       GLES30.glTexImage2D(3553, 0, 6409, this._video_width, this._video_height, 0, 6409, 5121, null);
/*     */     } 
/*     */     
/* 115 */     if (0 == this.m_hPixelBuffer) {
/* 116 */       int[] hPixelHandle = new int[1];
/* 117 */       GLES30.glGenBuffers(1, hPixelHandle, 0);
/* 118 */       this.m_hPixelBuffer = hPixelHandle[0];
/* 119 */       GLES30.glBindBuffer(35052, this.m_hPixelBuffer);
/* 120 */       GLES30.glBufferData(35052, this._video_width * this._video_height, null, 35044);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 125 */     GLES30.glBindBuffer(35052, this.m_hPixelBuffer);
/*     */     
/* 127 */     GLES30.glBufferSubData(35052, 0, this._video_width * this._video_height, buffer);
/*     */     
/* 129 */     GLES30.glBindTexture(3553, this._ytid);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     GLES30.glTexSubImage2D(3553, 0, 0, 0, this._video_width, this._video_height, 6409, 5121, null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     GLES30.glTexParameterf(3553, 10241, 9728.0F);
/* 143 */     GLES30.glTexParameterf(3553, 10240, 9729.0F);
/* 144 */     GLES30.glTexParameteri(3553, 10242, 33071);
/* 145 */     GLES30.glTexParameteri(3553, 10243, 33071);
/*     */ 
/*     */     
/* 148 */     if (null == this.uv)
/*     */     {
/* 150 */       this.uv = ByteBuffer.allocate(this._video_height * this._video_width / 2);
/*     */     }
/* 152 */     buffer.position(this._video_height * this._video_width);
/*     */     
/* 154 */     this.uv.put((ByteBuffer)buffer);
/* 155 */     this.uv.position(0);
/*     */     
/* 157 */     if (this._uvtid < 0 || videoSizeChanged) {
/* 158 */       if (this._uvtid >= 0) {
/* 159 */         GLES30.glDeleteTextures(1, new int[] { this._uvtid }, 0);
/* 160 */         checkGlError("glDeleteTextures");
/*     */       } 
/* 162 */       int[] textures = new int[1];
/* 163 */       GLES30.glGenTextures(1, textures, 0);
/* 164 */       checkGlError("glGenTextures");
/* 165 */       this._uvtid = textures[0];
/*     */       
/* 167 */       GLES30.glBindTexture(3553, this._uvtid);
/* 168 */       GLES30.glTexImage2D(3553, 0, 6410, this._video_width / 2, this._video_height / 2, 0, 6410, 5121, null);
/*     */     } 
/*     */     
/* 171 */     if (0 == this.m_hUvVectorBuffer) {
/* 172 */       int[] hUvVectorHandle = new int[1];
/* 173 */       GLES30.glGenBuffers(1, hUvVectorHandle, 0);
/* 174 */       this.m_hUvVectorBuffer = hUvVectorHandle[0];
/* 175 */       GLES30.glBindBuffer(35052, this.m_hUvVectorBuffer);
/* 176 */       GLES30.glBufferData(35052, this._video_width * this._video_height / 2, null, 35044);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     GLES30.glBindBuffer(35052, this.m_hUvVectorBuffer);
/*     */     
/* 185 */     GLES30.glBufferSubData(35052, 0, this._video_width * this._video_height / 2, this.uv);
/*     */     
/* 187 */     GLES30.glBindTexture(3553, this._uvtid);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     GLES30.glTexSubImage2D(3553, 0, 0, 0, this._video_width / 2, this._video_height / 2, 6410, 5121, null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     GLES30.glTexParameterf(3553, 10241, 9728.0F);
/* 201 */     GLES30.glTexParameterf(3553, 10240, 9729.0F);
/* 202 */     GLES30.glTexParameteri(3553, 10242, 33071);
/* 203 */     GLES30.glTexParameteri(3553, 10243, 33071);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawFrame() {
/* 208 */     GLES30.glUseProgram(this._program);
/* 209 */     checkGlError("glUseProgram");
/*     */     
/* 211 */     GLES30.glVertexAttribPointer(this._positionHandle, 2, 5126, false, 8, this.mVerticeBuffer);
/* 212 */     checkGlError("glVertexAttribPointer mPositionHandle");
/*     */     
/* 214 */     GLES30.glEnableVertexAttribArray(this._positionHandle);
/*     */     
/* 216 */     GLES30.glVertexAttribPointer(this._coordHandle, 2, 5126, false, 8, this.mCoordBuffer);
/*     */     
/* 218 */     GLES30.glEnableVertexAttribArray(this._coordHandle);
/*     */     
/* 220 */     GLES30.glActiveTexture(33984);
/* 221 */     GLES30.glBindTexture(3553, this._ytid);
/* 222 */     GLES30.glUniform1i(this._yhandle, this._tIindex);
/*     */     
/* 224 */     GLES30.glDrawArrays(5, 0, 4);
/* 225 */     GLES30.glFlush();
/*     */     
/* 227 */     GLES30.glActiveTexture(33985);
/* 228 */     GLES30.glBindTexture(3553, this._uvtid);
/* 229 */     GLES30.glUniform1i(this._uvhandle, this._tIIindex);
/*     */     
/* 231 */     GLES30.glDrawArrays(5, 0, 4);
/* 232 */     GLES30.glFlush();
/*     */     
/* 234 */     GLES30.glDisableVertexAttribArray(this._positionHandle);
/* 235 */     GLES30.glDisableVertexAttribArray(this._coordHandle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createBuffers(float[] vert) {
/* 240 */     this.mVerticeBuffer = ByteBuffer.allocateDirect(vert.length * 4);
/* 241 */     this.mVerticeBuffer.order(ByteOrder.nativeOrder());
/* 242 */     this.mVerticeBuffer.asFloatBuffer().put(vert);
/* 243 */     this.mVerticeBuffer.position(0);
/*     */     
/* 245 */     if (this.mCoordBuffer == null) {
/* 246 */       this.mCoordBuffer = ByteBuffer.allocateDirect(this.coordVertices.length * 4);
/* 247 */       this.mCoordBuffer.order(ByteOrder.nativeOrder());
/* 248 */       this.mCoordBuffer.asFloatBuffer().put(this.coordVertices);
/* 249 */       this.mCoordBuffer.position(0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float[] getSquareVertices() {
/* 255 */     return this.squareVertices;
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkGlError(String op) {
/*     */     int error;
/* 261 */     if ((error = GLES30.glGetError()) != 0) {
/* 262 */       throw new RuntimeException(op + ": glError " + error);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int createProgram(String vertexSource, String fragmentSource) {
/* 268 */     int vertexShader = loadShader(35633, vertexSource);
/* 269 */     int pixelShader = loadShader(35632, fragmentSource);
/*     */ 
/*     */     
/* 272 */     int program = GLES30.glCreateProgram();
/* 273 */     if (program != 0) {
/*     */       
/* 275 */       GLES30.glAttachShader(program, vertexShader);
/* 276 */       checkGlError("glAttachShader");
/* 277 */       GLES30.glAttachShader(program, pixelShader);
/* 278 */       checkGlError("glAttachShader");
/*     */       
/* 280 */       GLES30.glLinkProgram(program);
/* 281 */       int[] linkStatus = new int[1];
/* 282 */       GLES30.glGetProgramiv(program, 35714, linkStatus, 0);
/* 283 */       if (linkStatus[0] != 1) {
/* 284 */         GLES30.glDeleteProgram(program);
/* 285 */         program = 0;
/*     */       } 
/*     */     } 
/* 288 */     return program;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int loadShader(int shaderType, String source) {
/* 295 */     int shader = GLES30.glCreateShader(shaderType);
/* 296 */     if (shader != 0) {
/*     */       
/* 298 */       GLES30.glShaderSource(shader, source);
/* 299 */       GLES30.glCompileShader(shader);
/* 300 */       int[] compiled = new int[1];
/*     */       
/* 302 */       GLES30.glGetShaderiv(shader, 35713, compiled, 0);
/* 303 */       if (compiled[0] == 0) {
/* 304 */         Log.e("加载错误", GLES30.glGetShaderInfoLog(shader));
/* 305 */         GLES30.glDeleteShader(shader);
/* 306 */         shader = 0;
/*     */       } 
/*     */     } 
/* 309 */     return shader;
/*     */   }
/*     */ }


/* Location:              D:\DeskTop\classes.jar!\com\example\type\NV21GLProgram.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */