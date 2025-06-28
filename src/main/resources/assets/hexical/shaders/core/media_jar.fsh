#version 330 core

in vec2 fragUV;
in vec2 quadSize;

uniform float GameTime;
uniform sampler2D Sampler0;

out vec4 fragColor;

const int SIZE = 64;
const int TILE_W = 8;
const float TILE_SIZE = 1.0 / float(TILE_W);

float sample3DNoise(vec3 p) {
    float z_index = floor(p.z);
    z_index = mod(z_index, SIZE);

    float xi = mod(z_index, float(TILE_W));
    float yi = floor(z_index / float(TILE_W));

    vec2 sliceOffset = vec2(xi, yi) * TILE_SIZE;

    vec2 uv = p.xy * TILE_SIZE + sliceOffset;
    return texture(Sampler0, uv).r;
}

vec3 getGradientColor(float t) {
    vec3 colors[13] = vec3[](
        vec3(0xa6, 0x78, 0xf1) / 255.0,
        vec3(0xba, 0x8c, 0xfa) / 255.0,
        vec3(0xba, 0x8c, 0xfa) / 255.0,
        vec3(0xc8, 0x90, 0xf0) / 255.0,
        vec3(0xc8, 0x90, 0xf0) / 255.0,
        vec3(0xc8, 0x90, 0xf0) / 255.0,
        vec3(0xcf, 0xa0, 0xf3) / 255.0,
        vec3(0xcf, 0xa0, 0xf3) / 255.0,
        vec3(0xd8, 0x9e, 0xf9) / 255.0,
        vec3(0xd8, 0x9e, 0xf9) / 255.0,
        vec3(0xff, 0xc8, 0xe7) / 255.0,
        vec3(0xff, 0xc8, 0xe7) / 255.0,
        vec3(0xff, 0xfd, 0xd5) / 255.0
    );

    float noiseMin = 0.32;
    float noiseMax = 0.86;

    t = clamp((t - noiseMin) / (noiseMax - noiseMin), 0.0, 1.0);

    int idx = int(t * float(colors.length()));
    idx = clamp(idx, 0, colors.length() - 1);

    return colors[idx];
}

void main() {
    vec2 pixelCoord = floor(fragUV * quadSize);
    vec2 pixelatedUV = pixelCoord / quadSize;
    float zCoordForNoise = GameTime * 20.0 * 64.0 * 10.0;
    float noiseSample = sample3DNoise(vec3(pixelatedUV, zCoordForNoise));
    fragColor = vec4(getGradientColor(noiseSample), 1.0);
}