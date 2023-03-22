package com.example.displayimage;

public class Shader {
  public static final String RGB_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nvarying vec2 textureCoordinate;\nvoid main()\n{\n     gl_Position = position;\n     textureCoordinate = inputTextureCoordinate.xy;\n}";
  
  public static final String RGB_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nvoid main()\n{\n    gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
  
  public static final String BMP_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nvarying vec2 textureCoordinate;\nvoid main()\n{\n     gl_Position = position;\n     textureCoordinate = inputTextureCoordinate.xy;\n}";
  
  public static final String BMP_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nvoid main()\n{\n    highp float r,g,b;\n    b = texture2D(inputImageTexture, textureCoordinate).r;\n    g = texture2D(inputImageTexture, textureCoordinate).g;\n    r = texture2D(inputImageTexture, textureCoordinate).b;\n    gl_FragColor = vec4(r,g,b,1.0);\n}";
  
  public static final String YUV_VERTEX_SHADER = "attribute vec4 vPosition;\nattribute vec2 a_texCoord;\nvarying lowp vec2 tc;\nvoid main() {\n    gl_Position = vPosition;\n    tc = a_texCoord;\n}";
  
  public static final String YUV_FRAGMENT_SHADER = "varying lowp vec2 tc;\nuniform sampler2D tex_yuv;\nbool nFlag = false;\nvoid main(void) {\n    mediump float r,g,b,y,u,v;\n    if (nFlag == false){\n        y=texture2D(tex_yuv, tc).r ;\n        u=texture2D(tex_yuv, tc).g ;\n        v=texture2D(tex_yuv, tc).a ;\n    } else {\n        y=texture2D(tex_yuv, tc).b ;\n        u=texture2D(tex_yuv, tc).g ;\n        v=texture2D(tex_yuv, tc).a ;\n    }\n    nFlag = !nFlag;\n    u=u-0.5;\n    v=v-0.5;\n    y=y*1.1;\n\n    r=y+1.370705*v ;\n    g=y-0.337633*u-0.698001*v;\n    b=y+1.732446*u;\n    gl_FragColor=vec4(r,g,b,1.0);\n\n}\n";
  
  public static final String MONO8_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nvarying vec2 textureCoordinate;\nvoid main()\n{\n     gl_Position = position;\n     textureCoordinate = inputTextureCoordinate.xy;\n}";
  
  public static final String MONO8_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\nuniform sampler2D inputImageTexture;\nvoid main()\n{\n    gl_FragColor  = texture2D(inputImageTexture, textureCoordinate);\n}";
  
  public static final String NV21_VERTEX_SHADER = "attribute vec4 vPosition;\nattribute vec2 a_texCoord;\nvarying lowp vec2 tc;\nvoid main() {\ngl_Position = vPosition;\ntc = a_texCoord;\n}\n";
  
  public static final String NV21_FRAGMENT_SHADER = "varying lowp vec2 tc;\nuniform sampler2D tex_y; \nuniform sampler2D tex_uv; \nvoid main(void) {\t \nmediump float r,g,b,y,u,v; \n  y=texture2D(tex_y, tc).r ; \n  v=texture2D(tex_uv, tc).r ; \n  u=texture2D(tex_uv, tc).a ; \n  u=u-0.5;\t\n  v=v-0.5;\t \n  r=y+1.370705*v ; \n  g=y-0.337633*u-0.698001*v;\t \n  b=y+1.732446*u;\t \n  gl_FragColor=vec4(r,g,b,1.0); \n} \n";
}


/* Location:              D:\DeskTop\classes.jar!\com\example\displayimage\Shader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */