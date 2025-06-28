from noise import pnoise3
from PIL import Image
import numpy as np
import math

SIZE = 64
FREQUENCY = 3
SCALAR = FREQUENCY / SIZE
TILES_PER_ROW = 8
TILES_PER_COLUMN = math.ceil(SIZE / TILES_PER_ROW)
tile_width = SIZE * TILES_PER_ROW
tile_height = SIZE * TILES_PER_COLUMN

image = Image.new("L", (tile_width, tile_height))

for z in range(SIZE):
    tile_data = np.zeros((SIZE, SIZE), dtype=np.uint8)
    z_sample = z * SCALAR

    for i in range(SIZE ** 2):
        y, x = divmod(i, SIZE)
        val = pnoise3(
            x * SCALAR,
            y * SCALAR,
            z_sample,
            repeatx=FREQUENCY,
            repeaty=FREQUENCY,
            repeatz=FREQUENCY
        )

        color = int((val + 1) * 128)
        tile_data[y, x] = color

    tile = Image.fromarray(tile_data, mode="L")
    tile_y, tile_x = divmod(z, TILES_PER_ROW)
    image.paste(tile, (tile_x * SIZE, tile_y * SIZE))

image.save("perlin.png")
