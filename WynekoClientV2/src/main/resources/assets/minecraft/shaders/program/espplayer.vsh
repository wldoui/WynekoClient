#version 120

attribute vec4 Position;
attribute vec2 UV0;

uniform mat4 ProjMat;

varying vec2 texCoord0;

void main() {
    gl_Position = ProjMat * Position;
    texCoord0 = UV0;
}
