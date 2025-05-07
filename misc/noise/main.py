import numpy as np
import matplotlib.pyplot as plt
from perlin_noise import PerlinNoise

scale = 100
width, height = 107, 66
noise_image = np.zeros((height, width, 3))

noise = PerlinNoise(octaves=10)

colors = [
    "a678f1",
    "ba8cfa",
    "ba8cfa",
    "c890f0",
    "c890f0",
    "c890f0",
    "cfa0f3",
    "cfa0f3",
    "d89ef9",
    "d89ef9",
    "ffc8e7",
    "ffc8e7",
    "fffdd5"
]
translated_colors = [tuple(int(h[i:i + 2], 16) / 255 for i in (0, 2, 4)) for h in colors]

for y in range(height):
    for x in range(width):
        noise_image[y, x] = translated_colors[int(noise([x / width * 2, y / width * 2]) * len(colors))]

plt.imsave("noise.png", noise_image, cmap='gray', format='png')
plt.axis('off')
