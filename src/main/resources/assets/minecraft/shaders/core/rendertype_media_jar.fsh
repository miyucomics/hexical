#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;

uniform float GameTime;

in vec4 texProj0;

const vec3[] COLORS = vec3[](
    vec3(0.329, 0.224, 0.541),
    vec3(0.478, 0.357, 0.71),
    vec3(0.576, 0.408, 0.824),
    vec3(0.255, 0.169, 0.494),
    vec3(0.365, 0.227, 0.604),
    vec3(0.784, 0.565, 0.941),
    vec3(1.0, 0.992, 0.835),
    vec3(0.651, 0.471, 0.945),
    vec3(0.847, 0.62, 0.976),
    vec3(0.435, 0.31, 0.671),
    vec3(0.812, 0.627, 0.953),
    vec3(1.0, 0.784, 0.906),
    vec3(0.729, 0.549, 0.98),
    vec3(0.392, 0.278, 0.62)
);

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.25,
    0.0, 0.5, 0.0, 0.25,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

mat4 transform_layers(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    vec3 color = COLORS[0];
    for (int i = 0; i < 14; i++) {
        color += textureProj(Sampler0, texProj0 * transform_layers(float(i + 1))).rgb * COLORS[i];
    }
    fragColor = vec4(color, 1.0);
}