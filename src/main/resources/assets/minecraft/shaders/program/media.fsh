#version 150

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;

vec3 rgbToHsv(vec3 color) {
    float maxC = max(max(color.r, color.g), color.b);
    float minC = min(min(color.r, color.g), color.b);
    float delta = maxC - minC;

    float h = 0.0;
    if (delta > 0.0) {
        if (maxC == color.r) {
            h = mod((color.g - color.b) / delta, 6.0);
        } else if (maxC == color.g) {
            h = (color.b - color.r) / delta + 2.0;
        } else {
            h = (color.r - color.g) / delta + 4.0;
        }
        h /= 6.0;
        if (h < 0.0) h += 1.0;
    }

    float s = maxC == 0.0 ? 0.0 : delta / maxC;
    return vec3(h, s, maxC);
}

vec3 hsvToRgb(vec3 hsv) {
    float c = hsv.z * hsv.y;
    float x = c * (1.0 - abs(mod(hsv.x * 6.0, 2.0) - 1.0));
    float m = hsv.z - c;

    vec3 rgb;
    if (hsv.x < 1.0 / 6.0) {
        rgb = vec3(c, x, 0.0);
    } else if (hsv.x < 2.0 / 6.0) {
        rgb = vec3(x, c, 0.0);
    } else if (hsv.x < 3.0 / 6.0) {
        rgb = vec3(0.0, c, x);
    } else if (hsv.x < 4.0 / 6.0) {
        rgb = vec3(0.0, x, c);
    } else if (hsv.x < 5.0 / 6.0) {
        rgb = vec3(x, 0.0, c);
    } else {
        rgb = vec3(c, 0.0, x);
    }

    return rgb + vec3(m);
}

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);
    vec3 hsv = rgbToHsv(color.rgb);

    if (hsv.x >= 260.0 / 360.0 && hsv.x <= 300.0 / 360.0) {
        hsv.z = min(hsv.z * 2.0, 1.0);
        color.rgb = hsvToRgb(hsv);
    } else {
        color.rgb = vec3(dot(color.rgb, vec3(0.299, 0.587, 0.114)));
    }

    fragColor = color;
}