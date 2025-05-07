from PIL import Image

def normalize_color(color):
    return tuple(round(c / 255, 3) for c in color[:3])  # Ignore alpha if present

def extract_colors(image_path):
    image = Image.open(image_path).convert("RGB")
    colors = set(image.getdata())

    normalized_colors = [normalize_color(color) for color in colors]
    return normalized_colors

colors = extract_colors("media.png")
for color in colors:
    print(color)
