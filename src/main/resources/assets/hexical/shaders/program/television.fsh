#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;

in vec2 texCoord;
out vec4 fragColor;

float warp = 0.25;
float scan = 1.0;

void main(){
    vec2 alteredDistance = abs(0.5 - texCoord);
    alteredDistance *= alteredDistance;

    vec2 distorted = texCoord;
    distorted.x -= 0.5;
    distorted.x *= 1.0 + (alteredDistance.y * warp);
    distorted.x += 0.5;

    distorted.y -= 0.5;
    distorted.y *= 1.0 + (alteredDistance.x * warp);
    distorted.y += 0.5;

    if (distorted.x < 0.0 || distorted.x > 1.0 || distorted.y < 0.0 || distorted.y > 1.0)
        fragColor = vec4(0.0, 0.0, 0.0, 1.0);
    else {
        float brightness = abs(sin(texCoord.y * OutSize.y) * 0.5 * scan);
        fragColor = vec4(mix(texture(DiffuseSampler, distorted).rgb, vec3(0.0), brightness), 1.0);
    }
}