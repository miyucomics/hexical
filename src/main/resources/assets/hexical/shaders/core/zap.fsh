#version 330 core

in vec2 texCoord0;
in vec4 vertexColor;

uniform sampler2D Sampler0;

out vec4 fragColor;

void main() {
    vec4 texColor = texture(Sampler0, texCoord0);
    if (color.a < 0.1)
        discard;
    fragColor = texColor * vertexColor;
}