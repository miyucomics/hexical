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
		val sx = floor(sampleX).toInt() and 255
		val sy = floor(sampleY).toInt() and 255
		val sz = floor(sampleZ).toInt() and 255
		val sw = floor(time).toInt() and 255

		val x = sampleX - floor(sampleX)
		val y = sampleY - floor(sampleY)
		val z = sampleZ - floor(sampleZ)
		val w = time - floor(time)

		val u = fade(x)
		val v = fade(y)
		val t = fade(z)
		val s = fade(w)

		val a = permutations[sx] + sy
		val aa = permutations[a] + sz
		val aaa = permutations[aa] + sw
		val aab = permutations[aa + 1] + sw
		val ab = permutations[a + 1] + sz
		val aba = permutations[ab] + sw
		val abb = permutations[ab + 1] + sw
		val b = permutations[sx + 1] + sy
		val ba = permutations[b] + sz
		val baa = permutations[ba] + sw
		val bab = permutations[ba + 1] + sw
		val bb = permutations[b + 1] + sz
		val bba = permutations[bb] + sw
		val bbb = permutations[bb + 1] + sw

		return lerp(
			s,
			lerp(
				t,
				lerp(
					v,
					lerp(u, grad(permutations[aaa], x, y, z, w), grad(permutations[baa], x - 1, y, z, w)),
					lerp(u, grad(permutations[aba], x, y - 1, z, w), grad(permutations[bba], x - 1, y - 1, z, w))
				),
				lerp(
					v,
					lerp(u, grad(permutations[aab], x, y, z - 1, w), grad(permutations[bab], x - 1, y, z - 1, w)),
					lerp(u, grad(permutations[abb], x, y - 1, z - 1, w), grad(permutations[bbb], x - 1, y - 1, z - 1, w))
				)
			),
			lerp(
				t,
				lerp(
					v,
					lerp(u, grad(permutations[aaa + 1], x, y, z, w - 1), grad(permutations[baa + 1], x - 1, y, z, w - 1)),
					lerp(u, grad(permutations[aba + 1], x, y - 1, z, w - 1), grad(permutations[bba + 1], x - 1, y - 1, z, w - 1))
				),
				lerp(
					v,
					lerp(u, grad(permutations[aab + 1], x, y, z - 1, w - 1), grad(permutations[bab + 1], x - 1, y, z - 1, w - 1)),
					lerp(u, grad(permutations[abb + 1], x, y - 1, z - 1, w - 1), grad(permutations[bbb + 1], x - 1, y - 1, z - 1, w - 1))
				)
			)
		)
	}
}