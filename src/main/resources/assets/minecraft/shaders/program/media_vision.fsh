#version 150

uniform sampler2D DiffuseSampler;
uniform float ClientTime;

in vec2 texCoord;
out vec4 fragColor;

vec2 hash2(vec2 p) {
    return fract(vec2(5978.23857, 2915.98275) * sin(vec2(p.x * 832.2388 + p.y * 234.9852, p.x * 921.7381 + p.y * 498.2348))) * 2.0 - 1.0;
}

float getPerlinValue(vec2 uv, float scale, float offset) {
    uv *= scale;
    vec2 f = fract(uv);
    vec2 m = f * f * (3.0 - 2.0 * f);
    vec2 p = uv - f;
    float n = mix(mix(dot(hash2(p + offset + vec2(0.0, 0.0)), f - vec2(0.0, 0.0)), dot(hash2(p + offset + vec2(1.0, 0.0)), f - vec2(1.0, 0.0)), m.x), mix(dot(hash2(p + offset + vec2(0.0, 1.0)), f - vec2(0.0, 1.0)), dot(hash2(p + offset + vec2(1.0, 1.0)), f - vec2(1.0, 1.0)), m.x),m.y);
    return 0.5 * n + 0.5;
}

bool getNoiseColor(vec2 uv) {
    uv = uv / 2.0;
    float circlePoints = 0.0;
    for (float i = 0.0; i < 250.0; ++i) {
        vec2 randNum = hash2(vec2(i));
        vec2 position = sin(randNum * ClientTime * 0.0075);
        float n = 2.0 * getPerlinValue(uv + cos(ClientTime * 0.0075), 3.0, 0.0);
        for (float j = 0.0; j < 5.0; ++j) {
            position.x = mod(position.x + cos(n), 1.0) - 0.5;
            position.y = mod(position.y + sin(n), 1.0) - 0.5;
            float dist = length(uv - position);
            circlePoints += 1.0 - smoothstep(0.009, 0.020, dist);
        }
    }

    return circlePoints > 0.5;
}

const vec3 DARK = vec3(27, 28, 41) / 255.0;
const vec3 LIGHT = vec3(203, 166, 247) / 255.0;

void main() {
    bool noise = getNoiseColor(texCoord);
    vec4 original = texture(DiffuseSampler, texCoord);
    if ((original.x + original.y + original.z) / 3.0 > 0.15) {
        if (noise)
            fragColor = vec4(LIGHT, 1.0);
        else
            fragColor = vec4(LIGHT - 0.25, 1.0);
    } else {
        if (noise)
            fragColor = vec4(DARK, 1.0);
        else
            fragColor = vec4(DARK - 0.02, 1.0);
    }
}