#version 150

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat4 TextureMat;

out vec3 fragPos;
out vec2 texCoord0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    fragPos = Position;
    texCoord0 = (TextureMat * vec4(1.0, 1.0, 1.0, 1.0)).xy;
}