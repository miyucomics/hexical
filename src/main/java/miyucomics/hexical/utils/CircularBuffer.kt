package miyucomics.hexical.utils

class CircularBuffer<T>(private val capacity: Int) {
	private val buffer = ArrayDeque<T>(capacity)

	fun add(item: T) {
		if (buffer.size == capacity)
			buffer.removeFirst()
		buffer.addLast(item)
	}

	fun buffer() = buffer
	fun clear() = buffer.clear()
	fun size(): Int = buffer.size
	override fun toString(): String = buffer.joinToString(", ", "[", "]")
}