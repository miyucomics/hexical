#version 150

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;
uniform vec3 RedMatrix;
uniform vec3 GreenMatrix;
uniform vec3 BlueMatrix;

void main() {
    vec3 color = texture(DiffuseSampler, texCoord).rgb;
    float red = dot(color, RedMatrix);
    float green = dot(color, GreenMatrix);
    float blue = dot(color, BlueMatrix);
    fragColor = vec4(vec3(red, green, blue), 1.0);
}