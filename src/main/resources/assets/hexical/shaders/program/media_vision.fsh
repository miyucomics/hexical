#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
	if (distance(texture(DiffuseSampler, texCoord).rgb, vec3(0.0)) > 0.5) {
		fragColor = vec4(0.78, 0.56, 0.94, 1.0);
	} else {
		fragColor = vec4(0.0);
	}
}