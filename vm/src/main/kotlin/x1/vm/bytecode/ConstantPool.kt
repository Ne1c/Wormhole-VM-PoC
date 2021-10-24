package x1.vm.bytecode

import x1.vm.dev.OnlyForDevTime
import x1.hypervisor.api.utils.*

abstract class ConstantPoolEntry(
    val type: Type,
) {
    @OnlyForDevTime
    abstract fun toByteArray(): ByteArray

    fun asIntEntry() = this as IntEntry

    fun asFunEntry() = this as FunctionEntry

    fun asStringEntry() = this as StringEntry

    enum class Type {
        INT,
        STRING,
        FUNCTION
    }
}

data class IntEntry(val value: TValue.IntValue) : ConstantPoolEntry(Type.INT) {
    companion object {
        const val SIZE_IN_BYTES = 4

        fun of(content: ByteArray): IntEntry {
            val value = (content[0].toInt() shl 24) or
                    (content[1].toInt() shl 16) or
                    (content[2].toInt() shl 8) or
                    content[3].toInt()

            return IntEntry(TValue.IntValue(value))
        }
    }

    @OnlyForDevTime
    override fun toByteArray() = value.value.toByteArray()
}

class FunctionEntry(
    val nameIndex: U2,
    val nargs: U1,
    val nlocals: U1,
    val address: U4
) : ConstantPoolEntry(Type.FUNCTION) {
    companion object {
        const val SIZE_IN_BYTES = 2 + 1 + 1 + 4

        fun of(content: ByteArray): FunctionEntry {
            val nameIndex = content[0].toInt()
            val nargs = content[1].toInt()
            val nlocals = content[2].toInt()
            val address = content.copyOfRange(3, content.size).toInt()

            return FunctionEntry(nameIndex, nargs, nlocals, address)
        }
    }

    fun toFunction(name: String): Function {
        return Function(name, nargs, nlocals, address)
    }

    @OnlyForDevTime
    override fun toByteArray(): ByteArray {
        val byteArray = ByteArray(SIZE_IN_BYTES)

        byteArray[0] = (nameIndex shr 8).toByte()
        byteArray[1] = nameIndex.toByte()
        byteArray[2] = nargs.toByte()
        byteArray[3] = nlocals.toByte()

        address.toByteArray().copyInto(byteArray, 4)

        return byteArray
    }
}

class StringEntry private constructor(
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

    constructor(string: String) : this(string.length, string)

    @OnlyForDevTime
    override fun toByteArray(): ByteArray {
        val strArray = string.toByteArray()
        val byteArray = ByteArray(1 + length)

        byteArray[0] = type.ordinal.toByte()

        strArray.copyInto(byteArray, 1)

        return byteArray
    }
}