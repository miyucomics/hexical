#version 150

in vec3 fragPos;
in vec2 fragUV;
uniform float GameTime;

out vec4 fragColor;

void main() {
    fragColor = vec4(fragUV.x, fragUV.y, 1.0, 1.0);
}