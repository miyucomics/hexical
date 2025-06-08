#version 330 core

in vec2 fragUV;
in vec2 quadSize;

uniform float GameTime;
uniform sampler2D Sampler0;

out vec4 fragColor;

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

    int idx = int(t * float(13));
    idx = clamp(idx, 0, 12);
    return colors[idx];
}

void main() {
    float fasterTime = GameTime * 1000.0;

    vec2 pixelCoord = floor(fragUV * quadSize);
    vec2 pixelatedUV = pixelCoord / quadSize;

    vec2 centerUV = (pixelatedUV - 0.5) * 2.0;
    float dist = length(centerUV);
    float ripple = sin(dist * 10.0 - fasterTime * 4.0) * 0.03;

    vec2 offset = vec2(
        sin(fasterTime + pixelatedUV.y * 20.0),
        cos(fasterTime + pixelatedUV.x * 20.0)
    ) * 0.01;

    vec2 finalUV = pixelatedUV + centerUV * ripple + offset;
    float noiseSample = texture(Sampler0, finalUV).r;
    fragColor = vec4(getGradientColor(noiseSample), 1.0);
}