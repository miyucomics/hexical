package miyucomics.hexical.utils

import kotlin.math.floor

class PerlinNoise(seed: Int) {
	private val permutations: IntArray

	init {
		val random = kotlin.random.Random(seed)
		permutations = IntArray(512) { random.nextInt(256) }
	}

	private fun fade(t: Double): Double {
		return t * t * t * (t * (t * 6 - 15) + 10)
	}

	private fun lerp(t: Double, a: Double, b: Double): Double {
		return a + t * (b - a)
	}

	private fun grad(hash: Int, x: Double, y: Double, z: Double, w: Double): Double {
		val h = hash and 31
		val a = if (h and 1 == 0) x else -x
		val b = if (h and 2 == 0) y else -y
		val c = if (h and 4 == 0) z else -z
		val d = if (h and 8 == 0) w else -w
		return a + b + c + d
	}

	fun noise(sampleX: Double, sampleY: Double, sampleZ: Double, time: Double): Double {
		val X = floor(sampleX).toInt() and 255
		val Y = floor(sampleY).toInt() and 255
		val Z = floor(sampleZ).toInt() and 255
		val W = floor(time).toInt() and 255

		val x = sampleX - floor(sampleX)
		val y = sampleY - floor(sampleY)
		val z = sampleZ - floor(sampleZ)
		val w = time - floor(time)

		val u = fade(x)
		val v = fade(y)
		val t = fade(z)
		val s = fade(w)

		val A = permutations[X] + Y
		val AA = permutations[A] + Z
		val AAA = permutations[AA] + W
		val AAB = permutations[AA + 1] + W
		val AB = permutations[A + 1] + Z
		val ABA = permutations[AB] + W
		val ABB = permutations[AB + 1] + W
		val B = permutations[X + 1] + Y
		val BA = permutations[B] + Z
		val BAA = permutations[BA] + W
		val BAB = permutations[BA + 1] + W
		val BB = permutations[B + 1] + Z
		val BBA = permutations[BB] + W
		val BBB = permutations[BB + 1] + W

		return lerp(s,
			lerp(t,
				lerp(v,
					lerp(u, grad(permutations[AAA], x, y, z, w), grad(permutations[BAA], x - 1, y, z, w)),
					lerp(u, grad(permutations[ABA], x, y - 1, z, w), grad(permutations[BBA], x - 1, y - 1, z, w))
				),
				lerp(v,
					lerp(u, grad(permutations[AAB], x, y, z - 1, w), grad(permutations[BAB], x - 1, y, z - 1, w)),
					lerp(u, grad(permutations[ABB], x, y - 1, z - 1, w), grad(permutations[BBB], x - 1, y - 1, z - 1, w))
				)
			),
			lerp(t,
				lerp(v,
					lerp(u, grad(permutations[AAA + 1], x, y, z, w - 1), grad(permutations[BAA + 1], x - 1, y, z, w - 1)),
					lerp(u, grad(permutations[ABA + 1], x, y - 1, z, w - 1), grad(permutations[BBA + 1], x - 1, y - 1, z, w - 1))
				),
				lerp(v,
					lerp(u, grad(permutations[AAB + 1], x, y, z - 1, w - 1), grad(permutations[BAB + 1], x - 1, y, z - 1, w - 1)),
					lerp(u, grad(permutations[ABB + 1], x, y - 1, z - 1, w - 1), grad(permutations[BBB + 1], x - 1, y - 1, z - 1, w - 1))
				)
			)
		)
	}
}