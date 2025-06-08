#version 120

attribute vec3 Position;
attribute vec2 UV0;

varying vec2 vUv;

void main() {
    vUv = UV0;
    gl_Position = vec4(Position, 1.0);
}