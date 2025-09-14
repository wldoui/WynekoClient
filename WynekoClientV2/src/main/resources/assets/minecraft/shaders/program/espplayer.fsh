#version 120

uniform sampler2D DiffuseSampler;
uniform vec4 ESPColor;
uniform float OutlineSize;
uniform vec2 InSize;
uniform float Mode; // 0 = только контур, 1 = заливка, 2 = контур+заливка

varying vec2 texCoord0;

void main() {
    vec2 texel = 1.0 / InSize;
    vec4 original = texture2D(DiffuseSampler, texCoord0);
    float alphaCenter = original.a;

    float alpha = 0.0;
    for (int x = -1; x <= 1; x++) {
        for (int y = -1; y <= 1; y++) {
            vec2 offset = vec2(x, y) * texel * OutlineSize;
            alpha = max(alpha, texture2D(DiffuseSampler, texCoord0 + offset).a);
        }
    }

    if (Mode < 0.5) {
        // Только контур
        if (alpha > 0.0 && alphaCenter < 0.1) {
            gl_FragColor = ESPColor;
        } else {
            gl_FragColor = original;
        }
    } else if (Mode < 1.5) {
        // Заливка
        if (alphaCenter > 0.1) {
            gl_FragColor = ESPColor;
        } else {
            gl_FragColor = original;
        }
    } else {
        // Контур + заливка
        if (alphaCenter > 0.1) {
            gl_FragColor = ESPColor;
        } else if (alpha > 0.0) {
            gl_FragColor = ESPColor;
        } else {
            gl_FragColor = original;
        }
    }
}
