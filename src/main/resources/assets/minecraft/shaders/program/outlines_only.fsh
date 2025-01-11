#version 150

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;

const vec3 DARK = vec3(30, 30, 46) / 255.0;
const vec3 LIGHT = vec3(205, 214, 244) / 255.0;

void main() {
    fragColor = vec4(DARK, 1.0);
    vec3 color = texture(DiffuseSampler, texCoord).rgb;
    if (color.x + color.y + color.z > 0.35)
        fragColor = vec4(LIGHT, 1.0);
}