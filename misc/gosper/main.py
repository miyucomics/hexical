rules = {
    "A": "A-B--B+A++AA+B-",
    "B": "+A-BB--B-A++A+B"
}

path = "A"
for _ in range(5):
    path = "".join(rules.get(char, char) for char in path)

angle_map = "aqwed"  # W should never happen but just in case
cursor = 0
output = ""
while cursor < len(path):
    if path[cursor] in '-+':
        angle = 0
        while cursor < len(path) and path[cursor] in '-+':
            if path[cursor] == '-':
                angle += 1
            else:
                angle -= 1
            cursor += 1
        if cursor < len(path) and path[cursor] in "AB":
            cursor += 1
        output += angle_map[angle + 2]
    elif path[cursor] in "AB":
        output += 'w'
        cursor += 1

print(output)
