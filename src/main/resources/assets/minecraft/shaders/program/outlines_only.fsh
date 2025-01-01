#version 150

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;

const vec3 DARK = vec3(18, 18, 22) / 255.0;
const vec3 LIGHT = vec3(232, 230, 225) / 255.0;

void main() {
    fragColor = vec4(DARK, 1.0);
    vec3 color = texture(DiffuseSampler, texCoord).rgb;
    if (color.x + color.y + color.z > 0.35)
        fragColor = vec4(LIGHT, 1.0);
}