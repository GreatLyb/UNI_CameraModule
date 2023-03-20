precision mediump float;
uniform sampler2D tex_y;
uniform sampler2D tex_u;
uniform sampler2D tex_v;
varying vec2 tc;
void main() {
    vec4 c = vec4((texture2D(tex_y, tc)));
    gl_FragColor = c;
}
