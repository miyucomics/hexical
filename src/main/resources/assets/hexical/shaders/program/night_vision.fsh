#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec3 color = texture(DiffuseSampler, texCoord).rgb;
    color *= 3.;
    color = mix(vec3(dot(color, vec3(.5))), color, .5);
    fragColor = vec4(color, 1.0);
}