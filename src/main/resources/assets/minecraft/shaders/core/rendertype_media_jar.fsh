#version 150

in vec3 fragPos;
in vec2 texCoord0;
uniform float GameTime;

out vec4 fragColor;

void main() {
    fragColor = vec4(fragPos.x, fragPos.y, 1.0, 1.0);
}