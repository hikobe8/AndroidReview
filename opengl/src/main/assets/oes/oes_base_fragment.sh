#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 textureCoordinate;
uniform samplerExternalOES vTexture;
void main() {
    vec4 nColor = texture2D(vTexture, textureCoordinate);
    float c = nColor.r * 0.299 + nColor.g * 0.587 + nColor.b * 0.114;
    gl_FragColor = vec4(c, c, c, nColor.a);
}