#version 330 core

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 fragUV;
out vec2 quadSize;
out vec3 offset;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    fragUV = UV0;
    quadSize = Color.rg * 16;
    offset = Normal;
}