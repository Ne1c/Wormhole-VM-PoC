package utils

const val INT_8_BITS_ARRAY_SIZE = 1
const val INT_16_BITS_ARRAY_SIZE = 2
const val INT_24_BITS_ARRAY_SIZE = 3
const val INT_32_BITS_ARRAY_SIZE = 4

fun Int.toByteArray(length: Int = INT_32_BITS_ARRAY_SIZE): ByteArray {
    val byteArray = ByteArray(length)

    for (i in (length - 1) downTo 0) {
        byteArray[length - 1 - i] = (this shr (8 * i)).toByte()
    }

    return byteArray
}

class ByteArrayNavigator(private val byteArray: ByteArray) {
    private var pointer = 0

    fun readNextBytesAsInt(length: Int): Int {
        val lastIndex = length - 1
        var result = 0

        for (i in lastIndex downTo 0) {
            result = result or ((byteArray[pointer].toInt() and 0xFF) shl (8 * i))
            pointer++
        }

        return result
    }

    fun readNextBytes(length: Int): ByteArray {
        val result = byteArray.copyOfRange(pointer, pointer + length)

        pointer += length

        return result
    }

    fun readNextByte() = byteArray[++pointer]

    fun isEnd() = byteArray.size == pointer
}