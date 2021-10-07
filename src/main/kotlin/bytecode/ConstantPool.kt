package bytecode

import utils.toByteArray
import java.nio.ByteBuffer

abstract class ConstantPoolEntry(
    val type: Type,
) {
    abstract fun toByteArray(): ByteArray

    enum class Type {
        INT,
        DOUBLE,
        STRING
    }
}

data class StringEntry(
    val length: Int,
    val string: String
) : ConstantPoolEntry(Type.STRING) {
    companion object {
        fun of(length: Int, content: ByteArray): StringEntry {
            return StringEntry(
                length = length,
                string = String(content)
            )
        }
    }

    override fun toByteArray(): ByteArray {
        val strArray = string.toByteArray()
        val byteArray = ByteArray(1 + strArray.size)

        byteArray[0] = type.ordinal.toByte()

        strArray.copyInto(byteArray, 1)

        return byteArray
    }
}

data class IntEntry(val value: Int) : ConstantPoolEntry(Type.INT) {
    companion object {
        fun of(content: ByteArray): IntEntry {
            return IntEntry(
                value = (content[0].toInt() shl 24) or
                        (content[1].toInt() shl 16) or
                        (content[2].toInt() shl 8) or
                        content[3].toInt()
            )
        }
    }

    override fun toByteArray() = value.toByteArray()
}

data class DoubleEntry(val value: Double) : ConstantPoolEntry(Type.DOUBLE) {
    companion object {
        fun of(content: ByteArray): DoubleEntry {
            return DoubleEntry(
                value = ByteBuffer.wrap(content).double
            )
        }
    }

    override fun toByteArray(): ByteArray {
        TODO("Not yet implemented")
    }
}